package main.java.neat.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import main.java.neat.config.NEATConfig;
import main.java.neat.config.NEATConfig.FITNESS_CRITERION;
import main.java.neat.functions.AggregationFunction;
import main.java.neat.functions.AggregationFunction.AGGREGATION_FUNCTION;

/**
 * 
 * @author Taher Joudeh
 *
 */
public class Neat {

	private final static int RUNNING = 0, GENERATION_TERMINATION = 1, FITNESS_TERMINATION = 2;
	private final String GENERATION_TERMINATION_MESSAGE,
			FITNESS_TERMINATION_MESSAGE;
	private int state = RUNNING;
	
	private NEATConfig neatConfig;
	private Agent[] population;
	private Agent best, currentBest;
	private LinkedList<Species> species;
	private static int speciesNumber;
	
	private AggregationFunction speciesFitnessFunction;
	private AggregationFunction fitnessCriterion;
	
	private double compatabilityThreshold;
	private int generation = 1;
	private double populationFitness;
	private double populationAdjustedFitness;
		
	/**
	 * 
	 * @param neatConfig
	 */
	public Neat(NEATConfig neatConfig) {
		this.neatConfig = neatConfig;
		
		GENERATION_TERMINATION_MESSAGE = "Terminated due to reaching the generation threshold [generationThreshold: " + neatConfig.getGenerationThreshold() + "]";
		FITNESS_TERMINATION_MESSAGE = "Terminated due to reaching the fitness threshold [fitnessThreshold: " + neatConfig.getFitnessThreshold() + "]";
		
		this.compatabilityThreshold = neatConfig.getCompatabilityThreshold();
		init();
	}
	
	private void init() {
		population = new Agent[this.neatConfig.getPopulationSize()];
		species = new LinkedList<> ();
		
		switch (neatConfig.getSpeciesFitnessFunction()) {
		case MAX:
			speciesFitnessFunction = AggregationFunction.getAggregationFunction(AGGREGATION_FUNCTION.MAX); 
			break;
		case MIN:
			speciesFitnessFunction = AggregationFunction.getAggregationFunction(AGGREGATION_FUNCTION.MIN);
			break;
		case MEAN:
			speciesFitnessFunction = AggregationFunction.getAggregationFunction(AGGREGATION_FUNCTION.MEAN);
			break;
		case MEDIAN:
			speciesFitnessFunction = AggregationFunction.getAggregationFunction(AGGREGATION_FUNCTION.MEDIAN);
			break;
		}
		
		if (neatConfig.getFitnessCriterion() != null)
			switch (neatConfig.getFitnessCriterion()) {
			case MAX:
				fitnessCriterion = AggregationFunction.getAggregationFunction(AGGREGATION_FUNCTION.MAX);
				break;
			case MIN:
				fitnessCriterion = AggregationFunction.getAggregationFunction(AGGREGATION_FUNCTION.MIN);
				break;
			}
		
		initAgents();
	}
	
	/**
	 * 
	 * @return
	 */
	public Agent getBest() { return best; }

	private void initAgents() {
		for (int i = 0; i < neatConfig.getPopulationSize(); i++)
			population[i] = new Agent(neatConfig);
		speciate();
	}
	
	public Agent[] getPopulation() { return population; }
	public int getGeneration() { return generation; }
	public double getPopulationFitness() { return populationFitness; }
	public boolean isTerminated() { return state != RUNNING; }
	public int getNumberOfSpecies() { return species.size(); }
	public double getCurrentCompatabilityThreshold() { return compatabilityThreshold; }

	private int terminationCheck() {
		
		if (state != RUNNING || (!neatConfig.isGenerationTermination() && !neatConfig.isFitnessTermination()))
			return state;
		
		if (neatConfig.isGenerationTermination() && generation >= neatConfig.getGenerationThreshold())
			return GENERATION_TERMINATION;
		if (neatConfig.isFitnessTermination()) {
			double[] fitness = new double[neatConfig.getPopulationSize()];
			for (int i = 0; i < neatConfig.getPopulationSize(); i++)
				fitness[i] = population[i].getFitness();
			if ((neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX &&
					fitnessCriterion.aggregate(fitness) >= neatConfig.getFitnessThreshold())
					||
				(neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MIN &&
					fitnessCriterion.aggregate(fitness) <= neatConfig.getFitnessThreshold())) {
				return FITNESS_TERMINATION;
			}
		}
		
		return RUNNING;
	}
	
	/**
	 * 
	 * @param print
	 */
	public void evolve(boolean printLastGenerationInfo) {
		
		int numberOfSpecs = species.size();
		if (neatConfig.isDynamicCompatabilityThreshold())
			adjustCompatabilityThreshold();
		
		LinkedList<Genome> nextGeneration = new LinkedList<> ();
		
		calculateFitness();
		sortAll();
		calculatePopulationFitness();
		
		getCurrentBest();
		
		if (printLastGenerationInfo)
			System.out.println("Generation: " + generation + " - " + populationFitness + " - [BEST: " + currentBest.getFitness() + "] | " + numberOfSpecs);
				
		state = terminationCheck();
		if (state != RUNNING) {
			if (state == GENERATION_TERMINATION)
				System.out.println(GENERATION_TERMINATION_MESSAGE);
			else System.out.println(FITNESS_TERMINATION_MESSAGE);
			return;
		}
		
		cull();
		giveNumberOfOffspring();
		fillElitesGenomes(nextGeneration);
		fillReproducedGenomes(nextGeneration);
		fillNewGenomes(nextGeneration);
		
		for (int i = 0; i < neatConfig.getPopulationSize(); i++) {
			population[i].setGenome(nextGeneration.get(i));
			population[i].setFitness(0);
		}
		
		speciate();
		
		generation++;
	}
	
	private void sortAll() {
		for (Species s: species)
			s.sort(neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MIN ? true : false);
		
		if (neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX)
			species.sort((s1,s2) -> Double.compare(s1.agents.get(0).getFitness(), s2.agents.get(0).getFitness()));
		else species.sort((s1,s2) -> Double.compare(s2.agents.get(0).getFitness(), s1.agents.get(0).getFitness()));
	}
	private void calculateFitness() {
		for (Species species: species) {
			species.calculateFitness();
			species.calculateAdjustedFitness();
		}
	}
	private void calculatePopulationFitness() {
		populationFitness = 0;
		populationAdjustedFitness = 0;
	    for (Species s : species) {
	        for (Agent agent : s.agents) {
	            populationAdjustedFitness += agent.getAdjustedFitness();
	            populationFitness += agent.getFitness();
	        }
	    }
	    
	    populationFitness = populationFitness/(double)neatConfig.getPopulationSize();
	}
	
	private void getCurrentBest() {
		currentBest = species.getLast().agents.get(0);
		if (best == null ||
				(neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX && currentBest.getFitness() > best.getFitness()) ||
				(neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MIN && currentBest.getFitness() < best.getFitness()))
			best = currentBest;
	}
	
	private void cull() {
		for (int i = 0; i < species.size()-neatConfig.getSpeciesElitism(); i++) {
			if (species.get(i).cull())
				species.remove(i--);
		}
	}
	
	private void giveNumberOfOffspring() {
		
		int numberOfChildren = 0;
		int numberOfElites = 0;
		for (int i = 0; i < species.size(); i++) {
			
			double factor = species.get(i).adjustedFitness / populationAdjustedFitness;
			int numberOfOffspring = (int) Math.round(factor * neatConfig.getPopulationSize()); 
			
			if (numberOfOffspring == 0) {
				species.remove(i--);
				continue;
			}
			
			int elites = Math.min(numberOfOffspring, Math.min(species.get(i).size(),neatConfig.getElitism()));
			numberOfOffspring -= elites;
						
			species.get(i).numberOfElites = elites;
			species.get(i).numberOfOffspring = numberOfOffspring;
			numberOfChildren += numberOfOffspring;
			numberOfElites += elites;
		}
		
		numberOfChildren += numberOfElites;

		if (numberOfChildren > neatConfig.getPopulationSize())
			purgeSpecies(numberOfChildren-neatConfig.getPopulationSize());
		
	}
	
	private void fillReproducedGenomes(LinkedList<Genome> nextGenerationGenomes) {
		for (Species s: species) {
			s.prepareSelectionPool();
			nextGenerationGenomes.addAll(s.reproduce());
		}
	}
	
	private void fillElitesGenomes(LinkedList<Genome> nextGenerationGenomes) {
		for (Species s: species)
			nextGenerationGenomes.addAll(s.getElites());
	}
	
	private void fillNewGenomes(LinkedList<Genome> nextGenerationGenomes) {
		int i = 0;
		while (nextGenerationGenomes.size() < neatConfig.getPopulationSize()) {
			Genome newGenome = species.get(i%species.size()).getRandomAgent().getGenome().clone();
			newGenome.mutate();
			nextGenerationGenomes.add(newGenome);
			i++;
		}
	}
	
	private void speciate() {
		
		clearSpecies();
		for (int i = 0; i < neatConfig.getPopulationSize(); i++) {
			double min = Double.POSITIVE_INFINITY;
			Species s = null;
			for (Species species: species) {
				double dist = Genome.distance(population[i].getGenome(), species.representative.getGenome());
				if (dist < min) {
					min = dist;
					s = species;
				}
			}
			if (s != null && min < compatabilityThreshold)
				s.add(population[i]);
			else
				species.add(new Species(population[i]));
		}
		removeEmptySpecies();
	}
	
	/*
	private void reinforceSpecies(int exceeding) {
		int i = species.size()-1;
//		System.out.println(exceeding);
		while (exceeding < 0) {
			Species s = species.get(i);
			s.numberOfOffspring++;
			exceeding++;
			i--;
			if (i < 0)
				i = species.size()-1;
		}
	}
	*/
	
	private void purgeSpecies(int exceeding) {
		int i = 0;
		while (exceeding > 0) {
			Species s = species.get(i);
			s.numberOfOffspring--;
			exceeding--;
			if (s.numberOfOffspring < 0)
				species.remove(i--);
			i++;
			if (i > species.size()-1)
				i = 0;
		}
	}
	private void clearSpecies() {
		for (Species s: species)
			s.clearSpecies();
	}
	private void removeEmptySpecies() {
		species.removeIf(s -> s.isEmpty());
	}
	
	private void adjustCompatabilityThreshold() {
		int numberOfSpecies = species.size();
		if (numberOfSpecies > neatConfig.getTargetNumberOfSpecies())
			compatabilityThreshold *= (1+neatConfig.getCompatabilityThresholdAdjustingFactor());
		if (numberOfSpecies < neatConfig.getTargetNumberOfSpecies())
			compatabilityThreshold *= (1-neatConfig.getCompatabilityThresholdAdjustingFactor());
	}
	
	private class Species {
		
		private Random random;
		private int number;
		
		private Agent representative;
		private LinkedList<Agent> agents;
		private LinkedList<Agent> selectionPool;
		
		private int numberOfOffspring;
		private int numberOfElites;
		private double fitness, adjustedFitness;
		private double maxFitness, maxHighscore;
		private int stagnationCounter;
		
		private Species() {
			random = new Random();
			agents = new LinkedList<> ();
			selectionPool = new LinkedList<> ();
			number = ++speciesNumber;
		}
		private Species(Agent agent) {
			this();
			agent.setSpeciesNumber(number);
			representative = agent;
			agents.add(agent);
		}
		
		private void add(Agent agent) {
			agent.setSpeciesNumber(number);
			agents.add(agent);
		}
		
		private int size() { return agents.size(); }
		
		private void clearSpecies() {
			agents.clear();
		}
		
		private boolean isEmpty() { return agents.isEmpty(); }
		private void sort(boolean ascending) {
			agents.sort((a1,a2) -> Double.compare(a1.getFitness(), a2.getFitness()));
						
			if (!ascending)
				Collections.reverse(agents);
		}
		
		private boolean cull() {
			double currentHighscore = agents.get(0).getFitness();
			if ((currentHighscore <= maxHighscore && neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX) ||
					(currentHighscore >= maxHighscore && neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MIN))
				stagnationCounter++;
			else {
				stagnationCounter = 0;
				maxHighscore = currentHighscore;
			}
			
			return stagnationCounter >= neatConfig.getStagnation();
		}
		
		private void calculateFitness() {
			fitness = speciesFitnessFunction.aggregate(getAgentsFitness());
			if ((neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX && fitness > maxFitness) ||
					(neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MIN && fitness < maxFitness))
				maxFitness = fitness;
		}
		private void calculateAdjustedFitness() {
			double sum = 0;
			for (Agent agent: agents) {
				double adjustedFitness = 0;
				if (neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX)
					adjustedFitness = agent.getFitness()/(double)size();
				else adjustedFitness = (1d/(1d+agent.getFitness()) / (double)size());
				agent.setAdjustedFitness(adjustedFitness);
				sum += adjustedFitness;
			}
			
			adjustedFitness = sum;
		}

		private LinkedList<Genome> getElites(){
			LinkedList<Genome> elites = new LinkedList<> ();
			for (int i = 0; i < size() && i < numberOfElites; i++)
				elites.add(agents.get(i).getGenome().clone());
			
			return elites;
		}
		
		private double[] getAgentsFitness() {
			double[] agentsFitness = new double[size()];
			for (int i = 0; i < size(); i++)
				agentsFitness[i] = agents.get(i).getFitness();
			return agentsFitness;
		}
				
		private double[] getPoolFitness() {
			double[] poolFitness = new double[selectionPool.size()];
			for (int i = 0; i < selectionPool.size(); i++) {
				if (neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX)
					poolFitness[i] = selectionPool.get(i).getFitness();
				else poolFitness[i] = 1d/(1d+selectionPool.get(i).getFitness());
			}
			return poolFitness;
		}

		private void prepareSelectionPool() {
			
			int selectionPoolSize = (int)(neatConfig.getSurvivalThreshold()*(double)size());
			
			selectionPoolSize = Math.max(1, selectionPoolSize);
			
			for (int i = 0; i < size() && i < selectionPoolSize; i++)
				selectionPool.add(agents.get(i));
			
			Collections.shuffle(selectionPool);
			
		}
		
		private Agent getRandomAgent() {
			return agents.get(random.nextInt(agents.size()));
		}
		
		private LinkedList<Genome> reproduce() {
			
			LinkedList<Genome> children = new LinkedList<> ();
			
			double sum = AggregationFunction.getAggregationFunction(AGGREGATION_FUNCTION.SUM).aggregate(getPoolFitness());			
			while (numberOfOffspring > 0) {
				
				Genome child = null;
				Agent parent1 = selectAgent(sum);
				Agent parent2 = selectAgent(sum);
				
				if (parent1 == parent2)
					child = parent1.getGenome().clone();
				else if (parent1.getFitness() != parent2.getFitness()) {
					if (neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX) {
						if (parent1.getFitness() > parent2.getFitness())
							child = Genome.crossover(parent1.getGenome(), parent2.getGenome(), false);
						else if (parent2.getFitness() > parent1.getFitness())
							child = Genome.crossover(parent2.getGenome(), parent1.getGenome(), false);
					}else {
						if (parent1.getFitness() > parent2.getFitness())
							child = Genome.crossover(parent2.getGenome(), parent1.getGenome(), false);
						else if (parent2.getFitness() > parent1.getFitness())
							child = Genome.crossover(parent1.getGenome(), parent2.getGenome(), false);
					}
				}else child = Genome.crossover(parent2.getGenome(), parent1.getGenome(), true);
				
				child.mutate();
				children.add(child);
				
				numberOfOffspring--;
			}
			
			selectionPool.clear();
			return children;
			
		}
		
		private Agent selectAgent(double sum) {
			
			Agent agent = null;
			
			switch (neatConfig.getSelectionType()) {
			case ROULETTE_WHEEL:
				agent = rouletteWheelSelection(sum);
				break;
			case TOURNAMENT:
				agent = tournamentSelection();
				break;
			}
			
			return agent;
			
		}
		
		private Agent rouletteWheelSelection(double sum) {
			
			double stopThreshold = random.nextDouble()*sum;
			double runningSum = 0;
			
			int i = -1;
			while (runningSum < stopThreshold)
				runningSum += selectionPool.get(++i).getFitness();
			
			return selectionPool.get(i);
			
		}
		private Agent tournamentSelection() {
			
			Agent agent = selectionPool.get(random.nextInt(selectionPool.size()));
			for (int i = 0; i < neatConfig.getTournamentSize()-1; i++) {
				Agent ag = selectionPool.get(random.nextInt(selectionPool.size()));
				if ((neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MAX && agent.getFitness() < ag.getFitness()) ||
						(neatConfig.getFitnessCriterion() == FITNESS_CRITERION.MIN && agent.getFitness() > ag.getFitness()))
					agent = ag;
			}
			
			return agent;
		}
		
	}
	
}
