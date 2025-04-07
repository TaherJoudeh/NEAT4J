package main.java.neat.core;

import java.io.Serializable;
import main.java.neat.config.NEATConfig;

/**
 * 
 * @author Taher Joudeh
 *
 */
public class Agent implements Serializable {

	private static final long serialVersionUID = 3215925558128015737L;
	
	private Genome genome;
	private int speciesNumber;
	private double fitness;
	private double adjustedFitness;
	
	/**
	 * 
	 * @param neatConfig
	 */
	protected Agent(NEATConfig neatConfig) {
		genome = new Genome(neatConfig, true);
	}
	
	/**
	 * 
	 * @param genome
	 */
	protected Agent(Genome genome) {
		this.genome = genome;
	}
	
	public Genome getGenome() { return genome; }
	/**
	 * 
	 * @param genome
	 */
	public void setGenome(Genome genome) { this.genome = genome; }
	
	public int getSpeciesNumber() { return speciesNumber; }
	/**
	 * 
	 * @param speciesNumber
	 */
	protected void setSpeciesNumber(int speciesNumber) { this.speciesNumber = speciesNumber; }
	
	public double getFitness() { return fitness; }
	/**
	 * 
	 * @param fitness
	 */
	public void setFitness(double fitness) { this.fitness = fitness; }
	
	protected double getAdjustedFitness() { return adjustedFitness; }
	/**
	 * 
	 * @param adjustedFitness
	 */
	protected void setAdjustedFitness(double adjustedFitness) { this.adjustedFitness = adjustedFitness; }
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public double[] think(double[] input) {
		return genome.feed(input);
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public boolean[] decide(double[] input) {
		return genome.feed2(input);
	}
	
	@Override
	protected Agent clone() {
		Agent clone = new Agent(genome.clone());
		clone.fitness = fitness;
		return clone;
	} 
	
}
