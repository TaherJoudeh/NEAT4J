package main.java.neat.core;

import java.io.Serializable;
import main.java.neat.config.NEATConfig;

/**
 * Represents an individual agent in the NEAT (NeuroEvolution of Augmenting Topologies)
 * algorithm. Each agent contains a neural network genome that can be evolved,
 * and maintains fitness information used during the evolutionary process.
 * 
 * @author Taher Joudeh
 */
public class Agent implements Serializable {

	private static final long serialVersionUID = 3215925558128015737L;
	
	/**
	 * Genome or the neural network of the agent.
	 */
	private Genome genome;
	
	/**
	 * Species number that the agent does belong to.
	 */
	private int speciesNumber;
	
	/**
	 * Fitness of the agent.
	 */
	private double fitness;
	
	/**
	 * Adjusted fitness of the agent.
	 */
	private double adjustedFitness;
	
    /**
     * Constructs a new Agent with a randomly initialized genome based on the
     * provided NEAT configuration.
     * 
     * @param neatConfig The NEAT configuration parameters for genome initialization.
     */
	protected Agent(NEATConfig neatConfig) {
		genome = new Genome(neatConfig, true);
	}
	
    /**
     * Constructs a new Agent with a pre-existing genome. Used for cloning
     * and crossover operations during evolution.
     * 
     * @param genome The genome to assign to this agent.
     */
	protected Agent(Genome genome) {
		this.genome = genome;
	}
	
    /**
     * Gets the agent's neural network genome.
     * @return The agent's genome.
     */
	public Genome getGenome() { return genome; }
	
    /**
     * Replaces the agent's current genome with a new one.
     * @param genome The new genome to assign.
     */
	public void setGenome(Genome genome) { this.genome = genome; }
	
    /**
     * Gets the species identifier for this agent.
     * @return The species number this agent belongs to.
     */
	public int getSpeciesNumber() { return speciesNumber; }
	
    /**
     * Sets the species identifier for this agent (used during speciation).
     * @param speciesNumber The new species number to assign.
     */
	protected void setSpeciesNumber(int speciesNumber) { this.speciesNumber = speciesNumber; }
	
    /**
     * Gets the raw fitness score for this agent.
     * @return The agent's current fitness value.
     */
	public double getFitness() { return fitness; }
	
    /**
     * Sets the raw fitness score for this agent.
     * @param fitness The fitness value to assign.
     */
	public void setFitness(double fitness) { this.fitness = fitness; }
	
    /**
     * Gets the adjusted fitness score used for speciation calculations.
     * @return The adjusted fitness value.
     */
	protected double getAdjustedFitness() { return adjustedFitness; }
	
    /**
     * Sets the adjusted fitness score used internally for speciation.
     * @param adjustedFitness The adjusted fitness value to assign.
     */
	protected void setAdjustedFitness(double adjustedFitness) { this.adjustedFitness = adjustedFitness; }
	
    /**
     * Processes input values through the agent's neural network.
     * 
     * @param input Array of input values for the neural network.
     * @return Array of output values from the neural network.
     */
	public double[] think(double[] input) {
		return genome.feed(input);
	}
	
    /**
     * Processes input values and returns thresholded boolean decisions.
     * Output values that hit the activation threshold are considered true, others false.
     * 
     * @param input Array of input values for the neural network.
     * @return Array of boolean decisions based on network outputs.
     */
	public boolean[] decide(double[] input) {
		return genome.feed2(input);
	}
	
    /**
     * 
     * <p>Creates a deep copy of the agent including:
     * <ul>
 	 * <li>Cloned genome instance.
 	 * <li>Copied fitness values.
 	 * </ul> 
     * 
     * @return A new Agent instance with identical genetic makeup.
     */
	@Override
	protected Agent clone() {
		Agent clone = new Agent(genome.clone());
		clone.fitness = fitness;
		return clone;
	} 
	
}
