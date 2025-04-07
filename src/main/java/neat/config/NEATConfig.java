package main.java.neat.config;

import java.io.Serializable;

import main.java.neat.functions.ActivationFunction;
import main.java.neat.functions.AggregationFunction;

/**
 * 
 * @author Taher Joudeh
 *
 */
public class NEATConfig implements Serializable {

	private static final long serialVersionUID = -6643302886500758542L;

	// Class constants
	/**
	 * 
	 * @author Taher Joudeh
	 *
	 */
	public static enum FITNESS_CRITERION {
		/**
		 * 
		 */
		MIN,
		
		/**
		 * 
		 */
		MAX,
	}
	
	/**
	 * 
	 * @author Taher Joudeh
	 *
	 */
	public static enum SPECIES_FITNESS_FUNCTION {
		/**
		 * 
		 */
		MIN,
		
		/**
		 * 
		 */
		MAX,
		
		/**
		 * 
		 */
		MEAN,
		
		/**
		 * 
		 */
		MEDIAN
	}
	
	/**
	 * 
	 * @author Taher Joudeh
	 *
	 */
	public static enum DISTRIBUTION {
		/**
		 * 
		 */
		NORMAL,
		
		/**
		 * 
		 */
		UNIFORM
	}
	
	/**
	 * 
	 * @author Taher Joudeh
	 *
	 */
	public static enum CONNECTIVITY {
		/**
		 * Start with no connections.
		 */
		UNCONNECTED,
		
		/**
		 * Selects one random input node and connects it to all output nodes.
		 */
		FEATURE_SELECTION_NEAT_NO_HIDDEN,
		
		/**
		 * Selects one random input node and connects it to all hidden and output nodes.
		 */
		FEATURE_SELECTION_NEAT_HIDDEN,
		
		/**
		 * Each layer is fully connected to the layer after it.
		 */
		LAYER_BY_LAYER, 
		
		/**
		 * Each input node is connected to all hidden and output nodes.
		 * If there is no hidden nodes, then each input node is connected to all output nodes.
		 * If feedforward parameter is set to false then each hidden and output node will have a recurrent connection to itself.
		 */
		FULL_NO_DIRECT,

		/**
		 * Each input node is connected to all hidden and output nodes, and each hidden
		 * node is connected to all output nodes.
		 * If feedforward parameter is set to false then each hidden and output node will have a recurrent connection to itself.
		 */
		FULL_DIRECT,
		
		/**
		 * Similar to FULL_NO_DIRECT but each connection will have a probability of being added [0, 1] which you can choose by adjusting
		 * the parameter probConnectInit.
		 */
		PARTIAL_NO_DIRECT,
		
		/**
		 * Similar to FULL_DIRECT but each connection will have a probability of being added [0, 1] which you can choose by adjusting
		 * the parameter probConnectInit.
		 */
		PARTIAL_DIRECT
	}
	
	/**
	 * 
	 * @author Taher Joudeh
	 *
	 */
	public static enum SELECTION_TYPE {
		/**
		 * 
		 */
		ROULETTE_WHEEL,
		
		/**
		 * 
		 */
		TOURNAMENT
	}
	
	// Population
	protected int populationSize;
	
	// Termination
	protected FITNESS_CRITERION fitnessCriterion = FITNESS_CRITERION.MAX;
	protected boolean fitnessTermination = false;
	protected double fitnessThreshold = 0;
	protected boolean generationTermination = false;
	protected int generationThreshold = 0;
	
	// Stagnation
	protected SPECIES_FITNESS_FUNCTION speciesFitnessFunction = SPECIES_FITNESS_FUNCTION.MEAN;
	protected int stagnation = 15;
	protected int speciesElitism = 0;
	
	// Reproduction
	protected int elitism = 0;
	protected double survivalThreshold = 0.5d;
	protected SELECTION_TYPE selectionType = SELECTION_TYPE.ROULETTE_WHEEL;
	protected int tournamentSize = 1;
	
	// Speciation
	protected double compatibilityThreshold = 3;
	protected double compatibilityExcessCoefficient = 1;
	protected double compatibilityDisjointCoefficient = 1;
	protected double compatibilityWeightCoefficient = 0.5;
	protected boolean dynamicCompatibilityThreshold = false;
	protected int targetNumberOfSpecies = 0;
	protected double compatabilityThresholdAdjustingFactor = 0.1;
	
	// Genome
	protected int numberOfInputs;
	protected int numberOfOutputs;
	protected int[] startingHiddenNodes = new int[] {};
	protected int maxNumberOfHiddenNodes = Integer.MAX_VALUE;
	protected AggregationFunction.AGGREGATION_FUNCTION startingAggregationFunction = AggregationFunction.AGGREGATION_FUNCTION.SUM;
	protected ActivationFunction.ACTIVATION_FUNCTION startingActivationFunctionForHiddenNode = ActivationFunction.ACTIVATION_FUNCTION.SIGMOID;
	protected ActivationFunction.ACTIVATION_FUNCTION startingActivationFunctionForOutputNode = ActivationFunction.ACTIVATION_FUNCTION.SIGMOID;
	protected AggregationConfig aggregationConfig;
	protected ActivationConfig activationConfig;
	protected CONNECTIVITY initConnectivity = CONNECTIVITY.UNCONNECTED;
	
	protected ActivationFunction.ACTIVATION_FUNCTION activationDefault = ActivationFunction.ACTIVATION_FUNCTION.SIGMOID;
	protected double activationMutationRate = 0;
	protected AggregationFunction.AGGREGATION_FUNCTION aggregationDefault = AggregationFunction.AGGREGATION_FUNCTION.SUM;
	protected double aggregationMutationRate = 0;
	
	protected double biasInitMean = 0;
	protected double biasInitStdev = 1;
	protected DISTRIBUTION biasInitType = DISTRIBUTION.NORMAL;
	protected double biasMaxValue = 50;
	protected double biasMinValue = -50;
	protected double biasMutationPower = 0.1d;
	protected double biasAdjustingRate = 0.8d;
	protected double biasRandomizingRate = 0.1;
	
	protected double probAddConnection = 0.03;
	protected double probRecurrentConnection = 0.1;
	protected double probDeleteConnection = 0;
	protected boolean enabledDefault = true;
	
	protected double enabledMutationRate = 0;
	protected double enabledRateToEnabled = 0;
	protected double enabledRateToDisabled = 0;
	
	protected boolean feedForward = true;
	
	protected double probAddNode = 0.01;
	protected double probDeleteNode = 0;
	
	protected double responseInitMean = 1;
	protected double responseInitStdev = 0;
	protected DISTRIBUTION responseInitType = DISTRIBUTION.NORMAL;
	protected double responseMaxValue = 50;
	protected double responseMinValue = -50;
	protected double responseMutationPower = 0.1d;
	protected double responseAdjustingRate = 0.7d;
	protected double responseRandomizingRate = 0.1d;
	
	protected boolean singleStructuralMutation = false;
	protected boolean structuralMutationAdvisor = false;
	
	protected double weightInitMean = 0;
	protected double weightInitStdev = 1;
	protected DISTRIBUTION weightInitType = DISTRIBUTION.NORMAL;
	protected double weightMaxValue = 50;
	protected double weightMinValue = -50;
	protected double weightMutationPower = 0.1d;
	protected double weightAdjustingRate = 0.8d;
	protected double weightRandomizingRate = 0.1d;
	
	protected double probConnectInit = 0.5d;

	protected NEATConfig(int populationSize, int numberOfInputs, int numberOfOutputs,
			AggregationConfig aggregationConfig, ActivationConfig activationConfig) {
		this.populationSize = populationSize;
		this.numberOfInputs = numberOfInputs;
		this.numberOfOutputs = numberOfOutputs;
		this.aggregationConfig = aggregationConfig;
		this.activationConfig = activationConfig;
	}
	
	public int getPopulationSize() { return populationSize; }
	public FITNESS_CRITERION getFitnessCriterion() { return fitnessCriterion; }
	public boolean isFitnessTermination() { return fitnessTermination; }
	public double getFitnessThreshold() { return fitnessThreshold; }
	public boolean isGenerationTermination() { return generationTermination; }
	public int getGenerationThreshold() { return generationThreshold; }
	public SPECIES_FITNESS_FUNCTION getSpeciesFitnessFunction() { return speciesFitnessFunction; }
	public int getStagnation() { return stagnation; }
	public int getSpeciesElitism() { return speciesElitism; }
	public int getElitism() { return elitism; }
	public double getSurvivalThreshold() { return survivalThreshold; }
	public SELECTION_TYPE getSelectionType() { return selectionType; }
	public int getTournamentSize() { return tournamentSize; }
	public double getCompatabilityThreshold() { return compatibilityThreshold; }
	public double getCompatibilityExcessCoefficient() { return compatibilityExcessCoefficient; }
	public double getCompatibilityDisjointCoefficient() { return compatibilityDisjointCoefficient; }
	public double getCompatibilityWeightCoefficient() { return compatibilityWeightCoefficient; }
	public boolean isDynamicCompatabilityThreshold() { return dynamicCompatibilityThreshold; }
	public int getTargetNumberOfSpecies() { return targetNumberOfSpecies; }
	public double getCompatabilityThresholdAdjustingFactor() { return compatabilityThresholdAdjustingFactor; }
	public int getNumberOfInputs() { return numberOfInputs; }
	public int getNumberOfOutputs() { return numberOfOutputs; }
	public int[] getStartingHiddenNodes() { return startingHiddenNodes; }
	public int getMaxNumberOfHiddenNodes() { return maxNumberOfHiddenNodes; }
	public AggregationFunction.AGGREGATION_FUNCTION getStartingAggregationFunction() { return startingAggregationFunction; }
	public ActivationFunction.ACTIVATION_FUNCTION getStartingActivationFunctionForHiddenNodes() { return startingActivationFunctionForHiddenNode; }
	public ActivationFunction.ACTIVATION_FUNCTION getStartingActivationFunctionForOutputNodes(){ return startingActivationFunctionForOutputNode; }
	public AggregationConfig getAggregationConfig() { return aggregationConfig; }
	public ActivationConfig getActivationConfig() { return activationConfig; }
	public CONNECTIVITY getInitConnectivity() { return initConnectivity; }
	public ActivationFunction.ACTIVATION_FUNCTION getActivationDefault(){ return activationDefault; }
	public double getActivationMutationRate() { return activationMutationRate; }
	public AggregationFunction.AGGREGATION_FUNCTION getAggregationDefault() { return aggregationDefault; }
	public double getAggregationMutationRate() { return aggregationMutationRate; }
	public double getBiasInitMean() { return biasInitMean; }
	public double getBiasInitStdev() { return biasInitStdev; }
	public DISTRIBUTION getBiasInitType() { return biasInitType; }
	public double getBiasMaxValue() { return biasMaxValue; }
	public double getBiasMinValue() { return biasMinValue; }
	public double getBiasMutationPower() { return biasMutationPower; }
	public double getBiasAdjustingRate() { return biasAdjustingRate; }
	public double getBiasRandomizingRate() { return biasRandomizingRate; }
	public double getProbAddConnection() { return probAddConnection; }
	public double getProbRecurrentConnection() { return probRecurrentConnection; }
	public double getProbDeleteConnection() { return probDeleteConnection; }
	public boolean enabledDefault() { return enabledDefault; }
	public double getEnabledMutationRate() { return enabledMutationRate; }
	public double getEnabledRateToEnabled() { return enabledRateToEnabled; }
	public double getEnabledRateToDisabled() { return enabledRateToDisabled; }
	public boolean isFeedForward() { return feedForward; }
	public double getProbAddNode() { return probAddNode; }
	public double getProbDeleteNode() { return probDeleteNode; }
	public double getResponseInitMean() { return responseInitMean; }
	public double getResponseInitStdev() { return responseInitStdev; }
	public DISTRIBUTION getResponseInitType() { return responseInitType; }
	public double getResponseMaxValue() { return responseMaxValue; }
	public double getResponseMinValue() { return responseMinValue; }
	public double getResponseMutationPower() { return responseMutationPower; }
	public double getResponseAdjustingRate() { return responseAdjustingRate; }
	public double getResponseRandomizingRate() { return responseRandomizingRate; }
	public boolean isSingleStructuralMutation() { return singleStructuralMutation; }
	public boolean hasStructuralMutationAdvisor() { return structuralMutationAdvisor; }
	public double getWeightInitMean() { return weightInitMean; }
	public double getWeightInitStdev() { return weightInitStdev; }
	public DISTRIBUTION getWeightInitType() { return weightInitType; }
	public double getWeightMaxValue() { return weightMaxValue; }
	public double getWeightMinValue() { return weightMinValue; }
	public double getWeightMutationPower() { return weightMutationPower; }
	public double getWeightAdjustingRate() { return weightAdjustingRate; }
	public double getWeightRandomizingRate() { return weightRandomizingRate; }
	public double getProbConnectInit() { return probConnectInit; }
	
}
