package main.java.test.xor;

import main.java.neat.config.NEATConfig;
import main.java.neat.config.NEATConfig.CONNECTIVITY;
import main.java.neat.config.NEATConfig.SELECTION_TYPE;
import main.java.neat.config.NEATConfig.SPECIES_FITNESS_FUNCTION;
import main.java.neat.config.NEATConfigBuilder;
import main.java.neat.core.Agent;
import main.java.neat.core.Neat;
import main.java.neat.io.GenomeFileHandler;
import main.java.neat.visualizer.GenomeVisualizer;
import main.java.neat.visualizer.GenomeVisualizerBuilder;

/**
 * Demonstrates NEAT algorithm solving the XOR logic gate problem.
 * Configures and evolves neural networks to learn XOR behavior through
 * neuroevolution. Includes visualization and persistence of results.
 * 
 * <p>This test case:
 * <ul>
 *   <li>Configures NEAT with XOR-specific parameters.</li>
 *   <li>Uses mean squared error for fitness calculation.</li>
 *   <li>Saves best genome and network visualization.</li>
 *   <li>Terminates when fitness reaches 3.9/4.0 possible.</li>
 * </ul>
 * 
 * @author Taher Joudeh
 */
public class TestXOR {

    /**
     * Executes the XOR neuroevolution test with configured parameters:
     * <ol>
     *   <li>Initializes NEAT configuration for XOR (2 inputs, 1 output).</li>
     *   <li>Sets up evolutionary parameters and mutation rates.</li>
     *   <li>Runs evolution until termination criteria met.</li>
     *   <li>Evaluates population on all XOR combinations.</li>
     *   <li>Saves best performing network and visualization.</li>
     * </ol>
     * 
     * @param args Command-line arguments (unused).
     */
	public static void main(String[] args) {
		
		NEATConfig neatConfig = new NEATConfigBuilder(150, 2, 1)
				.setMaxNumberOfHiddenNodes(1)
				
				.setCompatibilityExcessCoefficient(1)
				.setCompatibilityDisjointCoefficient(1)
				.setCompatibilityWeightCoefficient(0.4)
				
				.setCompatibilityThreshold(5)
				.setDynamicCompatabilityThreshold(true)
				.setCompatabilityThresholdAdjustingFactor(0.2)
				.setTargetNumberOfSpecies(20)
				.setSpeciesFitnessFunction(SPECIES_FITNESS_FUNCTION.MAX)
				
				.setFitnessTermination(true)
				.setFitnessTerminationThreshold(3.9)
				
				.setProbAddConnection(0.1)
				.setProbAddNode(0.06)
				
				.setEnabledMutationRate(0.01)
				.setEnabledRateForEnabled(-0.01)
				.setInitConnectivity(CONNECTIVITY.FULL_DIRECT)
				.setSurvivalThreshold(0.2)
				.setStagnation(10)
				
				.setWeightAdjustingRate(0.8)
				.setWeightRandomizingRate(0.1)
				.setWeightMutationPower(0.5)
				.setWeightMaxValue(20)
				.setWeightMinValue(-20)
				.setWeightInitStdev(20)
				
				.setBiasAdjustingRate(0.7)
				.setBiasRandomizingRate(0.1)
				.setBiasMutationPower(0.5)
				.setBiasMaxValue(20)
				.setBiasMinValue(-20)
				.setBiasInitStdev(20)
				
				.setSpeciesElitism(1)
				.setElitism(1)
				
				.setSelectionType(SELECTION_TYPE.TOURNAMENT)
				.setTournamentSize(4)
				.build();

		GenomeVisualizer visualizer = new GenomeVisualizerBuilder().defaultGenomeVisuals().build();

		Neat neat = new Neat(neatConfig);
		
		double[][] inputs = {
				{0,0}, // -> 0
				{0,1}, // -> 1
				{1,0}, // -> 1
				{1,1}  // -> 0
		};
		
		Agent[] agents = neat.getPopulation();
		
		while (!neat.isTerminated()) {
			for (int i = 0; i < neatConfig.getPopulationSize(); i++) {
				double[] output1 = agents[i].think(inputs[0]);
				double[] output2 = agents[i].think(inputs[1]);
				double[] output3 = agents[i].think(inputs[2]);
				double[] output4 = agents[i].think(inputs[3]);
				
				double fitness = Math.pow((1-output1[0]),2) +
						Math.pow(output2[0],2) +
						Math.pow(output3[0],2) +
						Math.pow((1-output4[0]),2);
								
				agents[i].setFitness(
						fitness
						);

			}
			
			neat.evolve(true);
		}
		
		Agent best = neat.getBest();
		double[] output1 = best.think(inputs[0]);
		double[] output2 = best.think(inputs[1]);
		double[] output3 = best.think(inputs[2]);
		double[] output4 = best.think(inputs[3]);
		System.out.println("0 - " + output1[0] + "\n1 - " + output2[0] + "\n1 - " + output3[0] + "\n0 - " + output4[0]
				+ "\n-------------------");
		
		GenomeFileHandler.saveImage(GenomeVisualizer.visualizeGenome(visualizer, "#000000", true, true, best.getGenome(), neatConfig.getWeightMaxValue(), 500, 500)
				,"src\\main\\java\\test\\xor"
				,"xor"
				,"png");
		
		GenomeFileHandler.saveGenome(best.getGenome(), "src\\main\\java\\test\\xor", "best");
		
	}
	
}
