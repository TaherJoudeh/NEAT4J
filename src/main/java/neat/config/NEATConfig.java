package main.java.neat.config;

import java.io.Serializable;

import main.java.neat.functions.ActivationFunction;
import main.java.neat.functions.AggregationFunction;

/**
 * Configuration class for NEAT (NeuroEvolution of Augmenting Topologies) algorithm.
 * 
 * This class encapsulates all customizable parameters that control the NEAT evolutionary
 * process. It serves as a central configuration hub, allowing fine-tuning of:
 * - Population dynamics
 * - Speciation mechanisms
 * - Genome structure and initialization
 * - Mutation rates and behaviors
 * - Termination conditions
 * - Selection mechanisms
 * 
 * The class is designed to be immutable after construction and is typically created
 * using the NEATConfigBuilder to provide a fluent configuration API.
 * 
 * @author Taher Joudeh
 */
public class NEATConfig implements Serializable {

	private static final long serialVersionUID = -6643302886500758542L;

    /**
     * Defines the optimization direction for fitness evaluation.
     * 
     * MIN: Lower fitness values are better (minimization problems)
     * MAX: Higher fitness values are better (maximization problems)
     */
	public static enum FITNESS_CRITERION {
        /**
         * Indicates that lower fitness values are better.
         * Used for problems where the goal is to minimize an error or cost.
         */
		MIN,
		
        /**
         * Indicates that higher fitness values are better.
         * Used for problems where the goal is to maximize performance or reward.
         */
		MAX,
	}
	
    /**
     * Defines how to calculate the fitness of a species from its member genomes.
     * 
     * Different aggregation methods can lead to different evolutionary dynamics
     * and selection pressures on species.
     */
	public static enum SPECIES_FITNESS_FUNCTION {
        /**
         * Uses the minimum fitness value among all genomes in the species.
         * Conservative approach that focuses on worst-case performance.
         */
		MIN,
		
        /**
         * Uses the maximum fitness value among all genomes in the species.
         * Optimistic approach that focuses on best-case performance.
         */
		MAX,
		
        /**
         * Uses the arithmetic mean of all fitness values in the species.
         * Balanced approach that considers overall performance.
         */
		MEAN,
		
        /**
         * Uses the median fitness value of all genomes in the species.
         * Robust approach that is less affected by outliers.
         */
		MEDIAN
	}
	
    /**
     * Defines the probability distribution types used for initializing
     * weights, biases, and other numeric parameters.
     */
	public static enum DISTRIBUTION {
        /**
         * Normal (Gaussian) distribution.
         * Values cluster around the mean with standard deviation controlling spread.
         */
		NORMAL,
		
        /**
         * Uniform distribution.
         * Values have equal probability across the defined range.
         */
		UNIFORM
	}
	
    /**
     * Defines the initial connectivity patterns for creating new genomes.
     * 
     * These patterns determine how nodes are connected when initializing
     * a network, dramatically affecting the starting point for evolution.
     */
	public static enum CONNECTIVITY {
        /**
         * Start with no connections.
         * The network must evolve all connections from scratch.
         */
		UNCONNECTED,
		
        /**
         * Selects one random input node and connects it to all output nodes.
         * Implementation of feature-selective NEAT without hidden nodes.
         */
		FEATURE_SELECTION_NEAT_NO_HIDDEN,
		
        /**
         * Selects one random input node and connects it to all hidden and output nodes.
         * Implementation of feature-selective NEAT with hidden nodes.
         */
		FEATURE_SELECTION_NEAT_HIDDEN,
		
        /**
         * Each layer is fully connected to the layer after it.
         * Creates a traditional layered neural network structure.
         */
		LAYER_BY_LAYER, 
		
        /**
         * Each input node is connected to all hidden and output nodes.
         * If there are no hidden nodes, then each input node is connected to all output nodes.
         * If feedforward parameter is set to false, then each hidden and output node
         * will have a recurrent connection to itself.
         */
		FULL_NO_DIRECT,

        /**
         * Each input node is connected to all hidden and output nodes, and each hidden
         * node is connected to all output nodes.
         * If feedforward parameter is set to false, then each hidden and output node
         * will have a recurrent connection to itself.
         */
		FULL_DIRECT,
		
        /**
         * Similar to FULL_NO_DIRECT but each connection has a probability of being added [0, 1]
         * controlled by the probConnectInit parameter.
         */
		PARTIAL_NO_DIRECT,
		
        /**
         * Similar to FULL_DIRECT but each connection has a probability of being added [0, 1]
         * controlled by the probConnectInit parameter.
         */
		PARTIAL_DIRECT
	}
	
    /**
     * Defines the selection methods used for choosing parent genomes during reproduction.
     */
	public static enum SELECTION_TYPE {
        /**
         * Selects parents with probability proportional to their fitness.
         * Better performing genomes have higher chances of being selected.
         */
		ROULETTE_WHEEL,
		
        /**
         * Randomly selects a group of genomes and chooses the best one.
         * The tournament size parameter controls selection pressure.
         */
		TOURNAMENT
	}
	
    /**
     * The total number of genomes in the population.
     * Larger populations provide more genetic diversity but require more computation.
     * Default is 1.
     */
	protected int populationSize = 1;
	
    /**
     * The fitness criterion to use (maximize or minimize).
     * Default is MAX (higher fitness values are better).
     */
	protected FITNESS_CRITERION fitnessCriterion = FITNESS_CRITERION.MAX;
	
    /**
     * Whether to terminate evolution when a fitness threshold is reached.
     * Default is false.
     */
	protected boolean fitnessTermination = false;
	
    /**
     * The fitness threshold for termination (if fitnessTermination is true).
     * Default is 0.
     */
	protected double fitnessTerminationThreshold = 0;
	
    /**
     * Whether to terminate evolution after a specific number of generations.
     * Default is false.
     */
	protected boolean generationTermination = false;
	
    /**
     * The generation threshold for termination (if generationTermination is true).
     * Default is 0.
     */
	protected int generationTerminationThreshold = 0;
	
    /**
     * Method to calculate species fitness from member genome fitnesses.
     * Default is MEAN.
     */
	protected SPECIES_FITNESS_FUNCTION speciesFitnessFunction = SPECIES_FITNESS_FUNCTION.MEAN;
	
    /**
     * Number of generations with no improvement before a species is considered stagnant.
     * Default is 15.
     */
	protected int stagnation = 15;
	
    /**
     * Number of species to preserve even if they stagnate.
     * Default is 0.
     */
	protected int speciesElitism = 0;
	
    /**
     * Number of top performing genomes within each species to preserve unchanged in each generation.
     * Default is 0.
     */
	protected int elitism = 0;
	
    /**
     * Fraction of genomes in each species that are selected for reproduction.
     * Default is 0.5 (50%).
     */
	protected double survivalThreshold = 0.5d;
	
    /**
     * Method used for selecting parent genomes during reproduction.
     * Default is ROULETTE_WHEEL.
     */
	protected SELECTION_TYPE selectionType = SELECTION_TYPE.ROULETTE_WHEEL;
	
    /**
     * Number of genomes to consider in tournament selection.
     * Only used if selectionType is TOURNAMENT. Default is 1.
     */
	protected int tournamentSize = 1;
	
    /**
     * Threshold for genome compatibility determining species membership.
     * Genomes with compatibility distance below this threshold are considered the same species.
     * Default is 3.
     */
	protected double compatibilityThreshold = 3;
	
    /**
     * Coefficient for excess genes in compatibility distance calculation.
     * Higher values give more weight to excess genes. Default is 1.
     */
	protected double compatibilityExcessCoefficient = 1;
	
    /**
     * Coefficient for disjoint genes in compatibility distance calculation.
     * Higher values give more weight to disjoint genes. Default is 1.
     */
	protected double compatibilityDisjointCoefficient = 1;
	
    /**
     * Coefficient for weight differences in compatibility distance calculation.
     * Higher values give more weight to connection weight differences. Default is 0.5.
     */
	protected double compatibilityWeightCoefficient = 0.5;
	
    /**
     * Whether to dynamically adjust the compatibility threshold.
     * If true, the threshold is adjusted to maintain targetNumberOfSpecies.
     * Default is false.
     */
	protected boolean dynamicCompatibilityThreshold = false;
	
    /**
     * Target number of species when using dynamic compatibility threshold.
     * Default is 0.
     */
	protected int targetNumberOfSpecies = 0;
	
    /**
     * Factor by which to adjust compatibility threshold when using dynamic adjustment.
     * Default is 0.1.
     */
	protected double compatabilityThresholdAdjustingFactor = 0.1;
	
    /**
     * Number of input nodes in the network.
     * Determined by the problem domain.
     */
	protected int numberOfInputs;
	
    /**
     * Number of output nodes in the network.
     * Determined by the problem domain.
     */
	protected int numberOfOutputs;
	
    /**
     * Array specifying the number of hidden nodes in each hidden layer at initialization.
     * Default is an empty array (no hidden nodes).
     */
	protected int[] startingHiddenNodes = new int[] {};
	
    /**
     * Maximum number of hidden nodes allowed in the network.
     * Default is Integer.MAX_VALUE (unlimited).
     */
	protected int maxNumberOfHiddenNodes = Integer.MAX_VALUE;
	
    /**
     * Aggregation function to use for new nodes.
     * Default is SUM.
     */
	protected AggregationFunction.AGGREGATION_FUNCTION startingAggregationFunction = AggregationFunction.AGGREGATION_FUNCTION.SUM;
	
    /**
     * Activation function to use for new hidden nodes.
     * Default is SIGMOID.
     */
	protected ActivationFunction.ACTIVATION_FUNCTION startingActivationFunctionForHiddenNode = ActivationFunction.ACTIVATION_FUNCTION.SIGMOID;
	
    /**
     * Activation function to use for new output nodes.
     * Default is SIGMOID.
     */
	protected ActivationFunction.ACTIVATION_FUNCTION startingActivationFunctionForOutputNode = ActivationFunction.ACTIVATION_FUNCTION.SIGMOID;
	
    /**
     * Configuration for allowed aggregation functions.
     */
	protected AggregationConfig aggregationConfig;
	
    /**
     * Configuration for allowed activation functions.
     */
	protected ActivationConfig activationConfig;
	
    /**
     * Initial connectivity pattern for new genomes.
     * Default is UNCONNECTED.
     */
	protected CONNECTIVITY initConnectivity = CONNECTIVITY.UNCONNECTED;
	
    /**
     * Default activation function for new nodes.
     * Default is SIGMOID.
     */
	protected ActivationFunction.ACTIVATION_FUNCTION activationDefault = ActivationFunction.ACTIVATION_FUNCTION.SIGMOID;
	
    /**
     * Probability of mutating a node's activation function.
     * Default is 0.
     */
	protected double activationMutationRate = 0;
	
    /**
     * Default aggregation function for new nodes.
     * Default is SUM.
     */
	protected AggregationFunction.AGGREGATION_FUNCTION aggregationDefault = AggregationFunction.AGGREGATION_FUNCTION.SUM;
	
    /**
     * Probability of mutating a node's aggregation function.
     * Default is 0.
     */
	protected double aggregationMutationRate = 0;
	
    /**
     * Mean for initializing node bias values.
     * Default is 0.
     */
	protected double biasInitMean = 0;
	
    /**
     * Standard deviation for initializing node bias values when using normal distribution.
     * Default is 1.
     */
	protected double biasInitStdev = 1;
	
    /**
     * Distribution type for initializing node bias values.
     * Default is NORMAL.
     */
	protected DISTRIBUTION biasInitDistributionType = DISTRIBUTION.NORMAL;
	
    /**
     * Maximum allowed value for node biases.
     * Default is 50.
     */
	protected double biasMaxValue = 50;
	
    /**
     * Minimum allowed value for node biases.
     * Default is -50.
     */
	protected double biasMinValue = -50;
	
    /**
     * Power (magnitude) of bias mutations.
     * Default is 0.1.
     */
	protected double biasMutationPower = 0.1d;
	
    /**
     * Probability of adjusting a node's bias during mutation.
     * Default is 0.8.
     */
	protected double biasAdjustingRate = 0.8d;
	
    /**
     * Probability of completely randomizing a node's bias during mutation.
     * Default is 0.1.
     */
	protected double biasRandomizingRate = 0.1;
	
    /**
     * Probability of adding a new connection during mutation.
     * Default is 0.03.
     */
	protected double probAddConnection = 0.03;
	
    /**
     * Probability that a new connection will be recurrent.
     * Only relevant when feedForward is false. Default is 0.1.
     */
	protected double probRecurrentConnection = 0.1;
	
    /**
     * Probability of deleting a connection during mutation.
     * Default is 0.
     */
	protected double probDeleteConnection = 0;
	
    /**
     * Whether new connections are enabled by default.
     * Default is true.
     */
	protected boolean enabledDefault = true;
	
    /**
     * Probability of toggling a connection's enabled status during mutation.
     * Default is 0.
     */
	protected double enabledMutationRate = 0;
	
    /**
     * Additional probability of toggling an disabled connection to enabled.
     * Default is 0.
     */
	protected double enabledRateForEnabled = 0;
	
    /**
     * Additional probability of toggling a disabled connection to enabled.
     * Default is 0.
     */
	protected double enabledRateForDisabled = 0;
	
    /**
     * Whether the network must be strictly feed-forward (no recurrent connections).
     * Default is true.
     */
	protected boolean feedForward = true;
	
    /**
     * Probability of adding a new node during mutation.
     * Default is 0.01.
     */
	protected double probAddNode = 0.01;
	
    /**
     * Probability of deleting a node during mutation.
     * Default is 0.
     */
	protected double probDeleteNode = 0;
	
    /**
     * Mean for initializing node response values.
     * Default is 1.
     */
	protected double responseInitMean = 1;
	
    /**
     * Standard deviation for initializing node response values when using normal distribution.
     * Default is 0.
     */
	protected double responseInitStdev = 0;
	
    /**
     * Distribution type for initializing node response values.
     * Default is NORMAL.
     */
	protected DISTRIBUTION responseInitDistributionType = DISTRIBUTION.NORMAL;
	
    /**
     * Maximum allowed value for node responses.
     * Default is 50.
     */
	protected double responseMaxValue = 50;
	
    /**
     * Minimum allowed value for node responses.
     * Default is -50.
     */
	protected double responseMinValue = -50;
	
    /**
     * Power (magnitude) of response mutations.
     * Default is 0.1.
     */
	protected double responseMutationPower = 0.1d;
	
    /**
     * Probability of adjusting a node's response during mutation.
     * Default is 0.7.
     */
	protected double responseAdjustingRate = 0.7d;
	
    /**
     * Probability of completely randomizing a node's response during mutation.
     * Default is 0.1.
     */
	protected double responseRandomizingRate = 0.1d;
	
    /**
     * Whether to allow only one structural mutation (add/delete node/connection) per genome per generation.
     * Default is false.
     */
	protected boolean singleStructuralMutation = false;
	
    /**
     * Whether to use a structural mutation advisor to guide mutation decisions.
     * Default is false.
     */
	protected boolean structuralMutationAdvisor = false;
	
    /**
     * Mean for initializing connection weight values.
     * Default is 0.
     */
	protected double weightInitMean = 0;
	
    /**
     * Standard deviation for initializing connection weight values when using normal distribution.
     * Default is 1.
     */
	protected double weightInitStdev = 1;
	
    /**
     * Distribution type for initializing connection weight values.
     * Default is NORMAL.
     */
	protected DISTRIBUTION weightInitDistributionType = DISTRIBUTION.NORMAL;
	
    /**
     * Maximum allowed value for connection weights.
     * Default is 50.
     */
	protected double weightMaxValue = 50;
	
    /**
     * Minimum allowed value for connection weights.
     * Default is -50.
     */
	protected double weightMinValue = -50;
	
    /**
     * Power (magnitude) of weight mutations.
     * Default is 0.1.
     */
	protected double weightMutationPower = 0.1d;
	
    /**
     * Probability of adjusting a connection's weight during mutation.
     * Default is 0.8.
     */
	protected double weightAdjustingRate = 0.8d;
	
    /**
     * Probability of completely randomizing a connection's weight during mutation.
     * Default is 0.1.
     */
	protected double weightRandomizingRate = 0.1d;
	
    /**
     * Probability of creating each connection when using PARTIAL_* connectivity types.
     * Default is 0.5 (50% chance per potential connection).
     */
	protected double probConnectInit = 0.5d;

    /**
     * Creates a new NEATConfig with the specified core parameters.
     * 
     * This constructor is protected to encourage use of the NEATConfigBuilder pattern.
     * It establishes the minimum required parameters while setting reasonable defaults
     * for other parameters.
     * 
     * @param populationSize Size of the population
     * @param numberOfInputs Number of input nodes
     * @param numberOfOutputs Number of output nodes
     * @param aggregationConfig Configuration for aggregation functions
     * @param activationConfig Configuration for activation functions
     */
	protected NEATConfig(int populationSize, int numberOfInputs, int numberOfOutputs,
			AggregationConfig aggregationConfig, ActivationConfig activationConfig) {
		if (populationSize > 1)
			this.populationSize = populationSize;
		this.numberOfInputs = numberOfInputs;
		this.numberOfOutputs = numberOfOutputs;
		this.aggregationConfig = aggregationConfig;
		this.activationConfig = activationConfig;
	}
	
	/**
	 * Returns the size of the population.
	 * This value determines how many genomes exist in each generation.
	 * 
	 * @return The number of genomes in the population
	 */
	public int getPopulationSize() { return populationSize; }
	
	/**
	 * Returns the fitness criterion used for evaluation.
	 * Determines whether the algorithm aims to maximize or minimize fitness values.
	 * 
	 * @return The fitness criterion (MIN or MAX)
	 */
	public FITNESS_CRITERION getFitnessCriterion() { return fitnessCriterion; }
	
	/**
	 * Indicates whether the evolution should terminate upon reaching a fitness threshold.
	 * 
	 * @return true if evolution should stop when fitness threshold is reached, false otherwise
	 */
	public boolean isFitnessTermination() { return fitnessTermination; }
	
	/**
	 * Returns the fitness threshold for termination.
	 * Evolution stops when the best genome's fitness reaches this value (if fitnessTermination is true).
	 * 
	 * @return The fitness threshold value
	 */
	public double getFitnessTerminationThreshold() { return fitnessTerminationThreshold; }
	
	/**
	 * Indicates whether the evolution should terminate after a specific number of generations.
	 * 
	 * @return true if evolution should stop after a fixed number of generations, false otherwise
	 */
	public boolean isGenerationTermination() { return generationTermination; }
	
	/**
	 * Returns the generation threshold for termination.
	 * Evolution stops after this many generations (if generationTermination is true).
	 * 
	 * @return The maximum number of generations to run
	 */
	public int getGenerationTerminationThreshold() { return generationTerminationThreshold; }
	
	/**
	 * Returns the method used to calculate species fitness from member genome fitnesses.
	 * This affects which species are considered fit for reproduction and survival.
	 * 
	 * @return The species fitness function (MIN, MAX, MEAN, or MEDIAN)
	 */
	public SPECIES_FITNESS_FUNCTION getSpeciesFitnessFunction() { return speciesFitnessFunction; }
	
	/**
	 * Returns the number of generations with no improvement before a species is considered stagnant.
	 * Stagnant species may be eliminated to make room for innovation.
	 * 
	 * @return The stagnation threshold in generations
	 */
	public int getStagnation() { return stagnation; }
	
	/**
	 * Returns the number of species to preserve even if they stagnate.
	 * This ensures diversity by maintaining some historical lineages.
	 * 
	 * @return The number of species protected from stagnation-based elimination
	 */
	public int getSpeciesElitism() { return speciesElitism; }
	
	/**
	 * Returns the number of top performing genomes to preserve unchanged in each generation.
	 * These elite genomes pass to the next generation without mutation.
	 * 
	 * @return The number of elite genomes preserved in each generation
	 */
	public int getElitism() { return elitism; }
	
	/**
	 * Returns the fraction of genomes in each species that are selected for reproduction.
	 * Only the top-performing genomes within this threshold reproduce.
	 * 
	 * @return The survival threshold as a fraction between 0 and 1
	 */
	public double getSurvivalThreshold() { return survivalThreshold; }
	
	/**
	 * Returns the method used for selecting parent genomes during reproduction.
	 * Influences which genomes get to reproduce and how selection pressure is applied.
	 * 
	 * @return The selection type (ROULETTE_WHEEL or TOURNAMENT)
	 */
	public SELECTION_TYPE getSelectionType() { return selectionType; }
	
	/**
	 * Returns the number of genomes to consider in tournament selection.
	 * Higher values increase selection pressure toward fitter genomes.
	 * Only used when selectionType is TOURNAMENT.
	 * 
	 * @return The tournament size
	 */
	public int getTournamentSize() { return tournamentSize; }
	
	/**
	 * Returns the threshold for genome compatibility determining species membership.
	 * Genomes with compatibility distance below this threshold are considered the same species.
	 * 
	 * @return The compatibility threshold value
	 */
	public double getCompatabilityThreshold() { return compatibilityThreshold; }
	
	/**
	 * Returns the coefficient for excess genes in compatibility distance calculation.
	 * Higher values give more weight to excess genes when determining species membership.
	 * 
	 * @return The excess genes coefficient
	 */
	public double getCompatibilityExcessCoefficient() { return compatibilityExcessCoefficient; }
	
	/**
	 * Returns the coefficient for disjoint genes in compatibility distance calculation.
	 * Higher values give more weight to disjoint genes when determining species membership.
	 * 
	 * @return The disjoint genes coefficient
	 */
	public double getCompatibilityDisjointCoefficient() { return compatibilityDisjointCoefficient; }
	
	/**
	 * Returns the coefficient for weight differences in compatibility distance calculation.
	 * Higher values give more weight to connection weight differences when determining species membership.
	 * 
	 * @return The weight difference coefficient
	 */
	public double getCompatibilityWeightCoefficient() { return compatibilityWeightCoefficient; }
	
	/**
	 * Indicates whether the compatibility threshold should be adjusted dynamically.
	 * When true, the threshold is adjusted to maintain the target number of species.
	 * 
	 * @return true if dynamic adjustment is enabled, false otherwise
	 */
	public boolean isDynamicCompatabilityThreshold() { return dynamicCompatibilityThreshold; }
	
	/**
	 * Returns the target number of species when using dynamic compatibility threshold.
	 * The compatibility threshold is adjusted to try to maintain this many species.
	 * 
	 * @return The target number of species
	 */
	public int getTargetNumberOfSpecies() { return targetNumberOfSpecies; }
	
	/**
	 * Returns the factor by which to adjust compatibility threshold when using dynamic adjustment.
	 * Controls how quickly the threshold is adjusted to reach the target number of species.
	 * 
	 * @return The threshold adjustment factor
	 */
	public double getCompatabilityThresholdAdjustingFactor() { return compatabilityThresholdAdjustingFactor; }
	
	/**
	 * Returns the number of input nodes in the network.
	 * This is determined by the problem domain (e.g., number of input features).
	 * 
	 * @return The number of input nodes
	 */
	public int getNumberOfInputs() { return numberOfInputs; }
	
	/**
	 * Returns the number of output nodes in the network.
	 * This is determined by the problem domain (e.g., number of classes in classification).
	 * 
	 * @return The number of output nodes
	 */
	public int getNumberOfOutputs() { return numberOfOutputs; }
	
	/**
	 * Returns the array specifying the number of hidden nodes in each hidden layer at initialization.
	 * For example, {5, 3} would create two hidden layers with 5 and 3 nodes respectively.
	 * 
	 * @return Array containing the number of nodes in each hidden layer
	 */
	public int[] getStartingHiddenNodes() { return startingHiddenNodes; }
	
	/**
	 * Returns the maximum number of hidden nodes allowed in the network.
	 * Prevents networks from growing too large during evolution.
	 * 
	 * @return The maximum number of hidden nodes allowed
	 */
	public int getMaxNumberOfHiddenNodes() { return maxNumberOfHiddenNodes; }
	
	/**
	 * Returns the default aggregation function for new nodes.
	 * This function determines how a node combines its inputs.
	 * 
	 * @return The default aggregation function
	 */
	public AggregationFunction.AGGREGATION_FUNCTION getStartingAggregationFunction() { return startingAggregationFunction; }
	
	/**
	 * Returns the default activation function for new hidden nodes.
	 * This function determines how a hidden node transforms its aggregated input to output.
	 * 
	 * @return The default activation function for hidden nodes
	 */
	public ActivationFunction.ACTIVATION_FUNCTION getStartingActivationFunctionForHiddenNodes() { return startingActivationFunctionForHiddenNode; }
	
	/**
	 * Returns the default activation function for new output nodes.
	 * This function determines how an output node transforms its aggregated input to output.
	 * 
	 * @return The default activation function for output nodes
	 */
	public ActivationFunction.ACTIVATION_FUNCTION getStartingActivationFunctionForOutputNodes(){ return startingActivationFunctionForOutputNode; }
	
	/**
	 * Returns the configuration for allowed aggregation functions.
	 * This controls which aggregation functions can be used during evolution.
	 * 
	 * @return The aggregation configuration
	 */
	public AggregationConfig getAggregationConfig() { return aggregationConfig; }
	
	/**
	 * Returns the configuration for allowed activation functions.
	 * This controls which activation functions can be used during evolution.
	 * 
	 * @return The activation configuration
	 */
	public ActivationConfig getActivationConfig() { return activationConfig; }
	
	/**
	 * Returns the initial connectivity pattern for new genomes.
	 * Determines how nodes are connected when a network is first created.
	 * 
	 * @return The initial connectivity pattern
	 */
	public CONNECTIVITY getInitConnectivity() { return initConnectivity; }
	
	/**
	 * Returns the default activation function for nodes created during evolution.
	 * 
	 * @return The default activation function
	 */
	public ActivationFunction.ACTIVATION_FUNCTION getActivationDefault(){ return activationDefault; }
	
	/**
	 * Returns the probability of mutating a node's activation function.
	 * Higher values make activation function mutations more frequent.
	 * 
	 * @return The activation function mutation rate between 0 and 1
	 */
	public double getActivationMutationRate() { return activationMutationRate; }
	
	/**
	 * Returns the default aggregation function for nodes created during evolution.
	 * 
	 * @return The default aggregation function
	 */
	public AggregationFunction.AGGREGATION_FUNCTION getAggregationDefault() { return aggregationDefault; }
	
	/**
	 * Returns the probability of mutating a node's aggregation function.
	 * Higher values make aggregation function mutations more frequent.
	 * 
	 * @return The aggregation function mutation rate between 0 and 1
	 */
	public double getAggregationMutationRate() { return aggregationMutationRate; }
	
	/**
	 * Returns the mean for initializing node bias values.
	 * Used with the bias initialization distribution.
	 * 
	 * @return The bias initialization mean
	 */
	public double getBiasInitMean() { return biasInitMean; }
	
	/**
	 * Returns the standard deviation for initializing node bias values.
	 * Used when bias initialization type is NORMAL.
	 * 
	 * @return The bias initialization standard deviation
	 */
	public double getBiasInitStdev() { return biasInitStdev; }
	
	/**
	 * Returns the distribution type for initializing node bias values.
	 * Determines whether biases are initialized with uniform or normal distribution.
	 * 
	 * @return The bias initialization distribution type
	 */
	public DISTRIBUTION getBiasInitDistributionType() { return biasInitDistributionType; }
	
	/**
	 * Returns the maximum allowed value for node biases.
	 * Biases are clamped to this value during initialization and mutation.
	 * 
	 * @return The maximum bias value
	 */
	public double getBiasMaxValue() { return biasMaxValue; }
	
	/**
	 * Returns the minimum allowed value for node biases.
	 * Biases are clamped to this value during initialization and mutation.
	 * 
	 * @return The minimum bias value
	 */
	public double getBiasMinValue() { return biasMinValue; }
	
	/**
	 * Returns the power (magnitude) of bias mutations.
	 * Controls how much a bias can change in a single mutation.
	 * 
	 * @return The bias mutation power
	 */
	public double getBiasMutationPower() { return biasMutationPower; }
	
	/**
	 * Returns the probability of adjusting a node's bias during mutation.
	 * Higher values make small bias adjustments more frequent.
	 * 
	 * @return The bias adjustment rate between 0 and 1
	 */
	public double getBiasAdjustingRate() { return biasAdjustingRate; }
	
	/**
	 * Returns the probability of completely randomizing a node's bias during mutation.
	 * Higher values make complete bias replacements more frequent.
	 * 
	 * @return The bias randomization rate between 0 and 1
	 */
	public double getBiasRandomizingRate() { return biasRandomizingRate; }
	
	/**
	 * Returns the probability of adding a new connection during mutation.
	 * Controls how frequently new connections are added to the network.
	 * 
	 * @return The add connection probability between 0 and 1
	 */
	public double getProbAddConnection() { return probAddConnection; }
	
	/**
	 * Returns the probability that a new connection will be recurrent.
	 * Only relevant when feedForward is false.
	 * 
	 * @return The recurrent connection probability between 0 and 1
	 */
	public double getProbRecurrentConnection() { return probRecurrentConnection; }
	
	/**
	 * Returns the probability of deleting a connection during mutation.
	 * Controls how frequently connections are removed from the network.
	 * 
	 * @return The delete connection probability between 0 and 1
	 */
	public double getProbDeleteConnection() { return probDeleteConnection; }
	
	/**
	 * Indicates whether new connections are enabled by default.
	 * When false, new connections are created in a disabled state.
	 * 
	 * @return true if new connections are enabled by default, false otherwise
	 */
	public boolean enabledDefault() { return enabledDefault; }
	
	/**
	 * Returns the probability of toggling a connection's enabled status during mutation.
	 * Controls how frequently connections are enabled or disabled.
	 * 
	 * @return The connection enabled status mutation rate between 0 and 1
	 */
	public double getEnabledMutationRate() { return enabledMutationRate; }
	
	/**
	 * Returns the additional probability of toggling an enabled connection to disabled.
	 * 
	 * @return The rate of maintaining enabled status for enabled connections
	 */
	public double getEnabledRateForEnabled() { return enabledRateForEnabled; }
	
	/**
	 * Returns the additional probability of toggling a disabled connection to enabled.
	 * 
	 * @return The rate of maintaining disabled status for disabled connections
	 */
	public double getEnabledRateForDisabled() { return enabledRateForDisabled; }
	
	/**
	 * Indicates whether the network must be strictly feed-forward (no recurrent connections).
	 * When true, connections from later nodes to earlier nodes are not allowed.
	 * 
	 * @return true if the network must be feed-forward, false if recurrent connections are allowed
	 */
	public boolean isFeedForward() { return feedForward; }
	
	/**
	 * Returns the probability of adding a new node during mutation.
	 * Controls how frequently the network topology grows with new neurons.
	 * 
	 * @return The add node probability between 0 and 1
	 */
	public double getProbAddNode() { return probAddNode; }
	
	/**
	 * Returns the probability of deleting a node during mutation.
	 * Controls how frequently nodes are removed from the network.
	 * 
	 * @return The delete node probability between 0 and 1
	 */
	public double getProbDeleteNode() { return probDeleteNode; }
	
	/**
	 * Returns the mean for initializing node response values.
	 * Used with the response initialization distribution.
	 * 
	 * @return The response initialization mean
	 */
	public double getResponseInitMean() { return responseInitMean; }
	
	/**
	 * Returns the standard deviation for initializing node response values.
	 * Used when response initialization type is NORMAL.
	 * 
	 * @return The response initialization standard deviation
	 */
	public double getResponseInitStdev() { return responseInitStdev; }
	
	/**
	 * Returns the distribution type for initializing node response values.
	 * Determines whether responses are initialized with uniform or normal distribution.
	 * 
	 * @return The response initialization distribution type
	 */
	public DISTRIBUTION getResponseInitDistributionType() { return responseInitDistributionType; }
	
	/**
	 * Returns the maximum allowed value for node responses.
	 * Responses are clamped to this value during initialization and mutation.
	 * 
	 * @return The maximum response value
	 */
	public double getResponseMaxValue() { return responseMaxValue; }
	
	/**
	 * Returns the minimum allowed value for node responses.
	 * Responses are clamped to this value during initialization and mutation.
	 * 
	 * @return The minimum response value
	 */
	public double getResponseMinValue() { return responseMinValue; }
	
	/**
	 * Returns the power (magnitude) of response mutations.
	 * Controls how much a response can change in a single mutation.
	 * 
	 * @return The response mutation power
	 */
	public double getResponseMutationPower() { return responseMutationPower; }
	
	/**
	 * Returns the probability of adjusting a node's response during mutation.
	 * Higher values make small response adjustments more frequent.
	 * 
	 * @return The response adjustment rate between 0 and 1
	 */
	public double getResponseAdjustingRate() { return responseAdjustingRate; }
	
	/**
	 * Returns the probability of completely randomizing a node's response during mutation.
	 * Higher values make complete response replacements more frequent.
	 * 
	 * @return The response randomization rate between 0 and 1
	 */
	public double getResponseRandomizingRate() { return responseRandomizingRate; }
	
	/**
	 * Indicates whether to allow only one structural mutation per genome per generation.
	 * When true, only one structural change (add/delete node/connection) can occur per genome.
	 * 
	 * @return true if only one structural mutation is allowed, false otherwise
	 */
	public boolean isSingleStructuralMutation() { return singleStructuralMutation; }
	
	/**
	 * Indicates whether to use a structural mutation advisor to guide mutation decisions.
	 * When true, the algorithm uses heuristics to determine when to make structural changes.
	 * 
	 * @return true if using a structural mutation advisor, false otherwise
	 */
	public boolean hasStructuralMutationAdvisor() { return structuralMutationAdvisor; }
	
	/**
	 * Returns the mean for initializing connection weight values.
	 * Used with the weight initialization distribution.
	 * 
	 * @return The weight initialization mean
	 */
	public double getWeightInitMean() { return weightInitMean; }
	
	/**
	 * Returns the standard deviation for initializing connection weight values.
	 * Used when weight initialization type is NORMAL.
	 * 
	 * @return The weight initialization standard deviation
	 */
	public double getWeightInitStdev() { return weightInitStdev; }
	
	/**
	 * Returns the distribution type for initializing connection weight values.
	 * Determines whether weights are initialized with uniform or normal distribution.
	 * 
	 * @return The weight initialization distribution type
	 */
	public DISTRIBUTION getWeightInitDistributionType() { return weightInitDistributionType; }
	
	/**
	 * Returns the maximum allowed value for connection weights.
	 * Weights are clamped to this value during initialization and mutation.
	 * 
	 * @return The maximum weight value
	 */
	public double getWeightMaxValue() { return weightMaxValue; }
	
	/**
	 * Returns the minimum allowed value for connection weights.
	 * Weights are clamped to this value during initialization and mutation.
	 * 
	 * @return The minimum weight value
	 */
	public double getWeightMinValue() { return weightMinValue; }
	
	/**
	 * Returns the power (magnitude) of weight mutations.
	 * Controls how much a weight can change in a single mutation.
	 * 
	 * @return The weight mutation power
	 */
	public double getWeightMutationPower() { return weightMutationPower; }
	
	/**
	 * Returns the probability of adjusting a connection's weight during mutation.
	 * Higher values make small weight adjustments more frequent.
	 * 
	 * @return The weight adjustment rate between 0 and 1
	 */
	public double getWeightAdjustingRate() { return weightAdjustingRate; }
	
	/**
	 * Returns the probability of completely randomizing a connection's weight during mutation.
	 * Higher values make complete weight replacements more frequent.
	 * 
	 * @return The weight randomization rate between 0 and 1
	 */
	public double getWeightRandomizingRate() { return weightRandomizingRate; }
	
	/**
	 * Returns the probability of creating each connection when using PARTIAL_* connectivity types.
	 * For partial connectivity patterns, each potential connection is created with this probability.
	 * 
	 * @return The connection creation probability between 0 and 1
	 */
	public double getProbConnectInit() { return probConnectInit; }
	
}
