package main.java.neat.config;

import main.java.neat.config.NEATConfig.CONNECTIVITY;
import main.java.neat.config.NEATConfig.DISTRIBUTION;
import main.java.neat.config.NEATConfig.FITNESS_CRITERION;
import main.java.neat.config.NEATConfig.SELECTION_TYPE;
import main.java.neat.config.NEATConfig.SPECIES_FITNESS_FUNCTION;
import main.java.neat.functions.ActivationFunction;
import main.java.neat.functions.AggregationFunction;

/**
 * Builder class for creating and configuring NEAT algorithm parameters.
 * 
 * This class implements the Builder pattern to provide a fluent, chainable API 
 * for constructing NEATConfig objects. It allows for clean, readable configuration
 * of all NEAT algorithm parameters without requiring complex constructors or
 * extensive parameter lists.
 * 
 * All builder methods follow the convention of setting a parameter and returning
 * the builder instance for method chaining. Many methods also include validation
 * to ensure parameters are within valid ranges.
 * 
 * Example usage:
 * <pre>
 * NEATConfig config = new NEATConfigBuilder(150, 4, 2)  // Population size, inputs, outputs
 *     .setFeedForward(true)                          // Feed-forward only networks
 *     .setProbAddNode(0.03)                          // Probability of adding nodes
 *     .setProbAddConnection(0.05)                    // Probability of adding connections
 *     .setInitConnectivity(CONNECTIVITY.FULL_DIRECT)  // Initial connection pattern
 *     .build();                                      // Create the configuration
 * </pre>
 * 
 * @author Taher Joudeh
 */
public class NEATConfigBuilder {
	
    /**
     * The NEATConfig instance being constructed by this builder.
     * This is initialized in the constructor and modified by the builder methods.
     */
	private NEATConfig neatConfig;
	
    /**
     * Creates a new NEATConfigBuilder with the specified core parameters.
     * 
     * This constructor initializes a NEATConfig with required parameters and 
     * default configurations for activation and aggregation functions.
     * 
     * @param populationSize The size of the population (number of genomes), if less than 1, then keep it to the default value (1).
     * @param numberOfInputs The number of input nodes in each network.
     * @param numberOfOutputs The number of output nodes in each network.
     */
	public NEATConfigBuilder(int populationSize, int numberOfInputs, int numberOfOutputs) {
		neatConfig = new NEATConfig(populationSize, numberOfInputs, numberOfOutputs,
				ConfigFactory.createDefaultAggregationConfig(),
				ConfigFactory.createDefaultActivationConfig());
	}
	
    /**
     * Creates a new NEATConfigBuilder with the specified core parameters
     * and custom activation and aggregation configurations.
     * The value of populationSize is clamped to be at least one.
     * 
     * This constructor allows full customization of all initial configurations.
     * 
     * @param populationSize The size of the population (number of genomes). If less than 1, then keep it to the default value (1).
     * @param numberOfInputs The number of input nodes in each network.
     * @param numberOfOutputs The number of output nodes in each network.
     * @param aggregationConfig The custom aggregation function configuration.
     * @param activationConfig The custom activation function configuration.
     */
	public NEATConfigBuilder(int populationSize, int numberOfInputs, int numberOfOutputs,
			AggregationConfig aggregationConfig, ActivationConfig activationConfig) {
		neatConfig = new NEATConfig(populationSize, numberOfInputs, numberOfOutputs, aggregationConfig, activationConfig);
	}
	
    /**
     * Sets the fitness criterion for the NEAT algorithm.
     * 
     * This determines whether the algorithm aims to maximize or minimize fitness values.
     * Default value is MAX.
     * 
     * @param fitnessCriterion The fitness criterion (MIN or MAX). If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setFitnessCriterion(FITNESS_CRITERION fitnessCriterion) {
		if (fitnessCriterion != null)
			neatConfig.fitnessCriterion = fitnessCriterion;
		else neatConfig.fitnessCriterion = fitnessCriterion;
		return this;
	}
	
    /**
     * Sets whether the evolution should terminate upon reaching a fitness threshold.
     * 
     * When enabled, evolution will stop when the best genome's fitness reaches
     * the threshold defined by setFitnessTerminationThreshold().
     * Default value is false.
     * 
     * @param fitnessTermination true to enable fitness-based termination.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setFitnessTermination(boolean fitnessTermination) {
		neatConfig.fitnessTermination = fitnessTermination;
		return this;
	}
	
    /**
     * Sets the fitness threshold for termination.
     * 
     * Evolution stops when the best genome's fitness reaches this value
     * (if fitness termination is enabled).
     * Default value is 0.
     * 
     * @param fitnessThreshold The fitness threshold value. If less than zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setFitnessTerminationThreshold(double fitnessThreshold) {
		if (fitnessThreshold >= 0)
			neatConfig.fitnessTerminationThreshold = fitnessThreshold;
		return this;
	}
	
    /**
     * Sets whether the evolution should terminate after a specific number of generations.
     * 
     * When enabled, evolution will stop after the number of generations defined
     * by setGenerationTerminationThreshold().
     * Default value is false.
     * 
     * @param generationTermination true to enable generation-based termination.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setGenerationTermination(boolean generationTermination) {
		neatConfig.generationTermination = generationTermination;
		return this;
	}
	
    /**
     * Sets the generation threshold for termination.
     * 
     * Evolution stops after this many generations (if generation termination is enabled).
     * Default value is 0.
     * 
     * @param generationThreshold The maximum number of generations to run. If less than 1, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setGenerationTerminationThreshold(int generationThreshold) {
		if (generationThreshold >= 1)
			neatConfig.generationTerminationThreshold = generationThreshold;
		return this;
	}
	
    /**
     * Sets the method used to calculate species fitness from member genome fitnesses.
     * 
     * This affects which species are considered fit for reproduction and survival.
     * Default value is MEAN.
     * 
     * @param speciesFitnessFunction The species fitness function (MIN, MAX, MEAN, or MEDIAN). If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setSpeciesFitnessFunction(SPECIES_FITNESS_FUNCTION speciesFitnessFunction) {
		if (speciesFitnessFunction != null)
			neatConfig.speciesFitnessFunction = speciesFitnessFunction;
		return this;
	}
	
    /**
     * Sets the number of generations with no improvement before a species is considered stagnant.
     * 
     * Stagnant species may be eliminated to make room for innovation.
     * Default value is 15.
     * 
     * @param stagnation The stagnation threshold in generations. If less than 1, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setStagnation(int stagnation) {
		if (stagnation >= 1)
			neatConfig.stagnation = stagnation;
		return this;
	}
	
    /**
     * Sets the number of species to preserve even if they stagnate.
     * 
     * This ensures diversity by maintaining some historical lineages.
     * Default value is 0.
     * 
     * @param speciesElitism The number of species protected from stagnation-based elimination. If less than zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setSpeciesElitism(int speciesElitism) {
		if (speciesElitism >= 0)
			neatConfig.speciesElitism = speciesElitism;
		return this;
	}
	
    /**
     * Sets the number of top performing genomes within each species to preserve unchanged in each generation.
     * 
     * These elite genomes pass to the next generation without mutation.
     * Default value is 0.
     * 
     * @param elitism The number of elite genomes preserved in each generation. If less than zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setElitism(int elitism) {
		if (elitism >= 0)
			neatConfig.elitism = elitism;
		return this;
	}
	
    /**
     * Sets the fraction of genomes in each species that are selected for reproduction.
     * 
     * Only the top-performing genomes within this threshold reproduce.
     * Default value is 0.5.
     * 
     * @param survivalThreshold The survival threshold as a fraction between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setSurvivalThreshold(double survivalThreshold) {
		if (survivalThreshold >= 0 && survivalThreshold <= 1)
			neatConfig.survivalThreshold = survivalThreshold;
		return this;
	}
	
    /**
     * Sets the method used for selecting parent genomes during reproduction.
     * 
     * Influences which genomes get to reproduce and how selection pressure is applied.
     * Default value is ROULETTE_WHEEL.
     * 
     * @param selectionType The selection type (ROULETTE_WHEEL or TOURNAMENT). If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setSelectionType(SELECTION_TYPE selectionType) {
		if (selectionType != null)
			neatConfig.selectionType = selectionType;
		return this;
	}
	
    /**
     * Sets the number of genomes to consider in tournament selection.
     * 
     * Higher values increase selection pressure toward fitter genomes.
     * Only used when selectionType is TOURNAMENT.
     * Default value is 1.
     * 
     * @param tournamentSize The tournament size. If less than 1, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setTournamentSize(int tournamentSize) {
		if (tournamentSize >= 1)
			neatConfig.tournamentSize = tournamentSize;
		return this;
	}
	
    /**
     * Sets the threshold for genome compatibility determining species membership.
     * 
     * Genomes with compatibility distance below this threshold are considered the same species.
     * Higher values result in fewer species as more genomes will be grouped together.
     * Lower values create more species with fewer members in each.
     * Default value is 3.
     * 
     * @param compatibilityThreshold The compatibility threshold value. If less than zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setCompatibilityThreshold(double compatibilityThreshold) {
		if (compatibilityThreshold >= 0)
			neatConfig.compatibilityThreshold = compatibilityThreshold;
		return this;
	}
	
    /**
     * Sets the coefficient for excess genes in compatibility distance calculation.
     * 
     * Higher values give more weight to excess genes when determining species membership.
     * Excess genes are those that exist beyond the highest innovation number in the
     * other genome being compared.
     * Default value is 1.
     * 
     * @param compatibilityExcessCoefficient The excess genes coefficient. If less than zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setCompatibilityExcessCoefficient(double compatibilityExcessCoefficient) {
		if (compatibilityExcessCoefficient >= 0)
			neatConfig.compatibilityExcessCoefficient = compatibilityExcessCoefficient;
		return this;
	}
	
    /**
     * Sets the coefficient for disjoint genes in compatibility distance calculation.
     * 
     * Higher values give more weight to disjoint genes when determining species membership.
     * Disjoint genes are those that exist in one genome but not the other, excluding excess genes.
     * Default value is 1.
     * 
     * @param compatibilityDisjointCoefficient The disjoint genes coefficient. If less than zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setCompatibilityDisjointCoefficient(double compatibilityDisjointCoefficient) {
		if (compatibilityDisjointCoefficient >= 0)
			neatConfig.compatibilityDisjointCoefficient = compatibilityDisjointCoefficient;
		return this;
	}
	
    /**
     * Sets the coefficient for weight differences in compatibility distance calculation.
     * 
     * Higher values give more weight to connection weight differences when determining species membership.
     * This factor accounts for how differently weighted the matching connections are between genomes.
     * Default value is 0.5.
     * 
     * @param compatibilityWeightCoefficient The weight difference coefficient. If less than zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setCompatibilityWeightCoefficient(double compatibilityWeightCoefficient) {
		if (compatibilityWeightCoefficient >= 0)
			neatConfig.compatibilityWeightCoefficient = compatibilityWeightCoefficient;
		return this;
	}
	
    /**
     * Sets whether the compatibility threshold should be adjusted dynamically.
     * 
     * When true, the threshold is adjusted to maintain the target number of species.
     * This can help maintain an appropriate level of diversity in the population.
     * Default value is false.
     * 
     * @param dynamicCompatabilityThreshold true to enable dynamic adjustment.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setDynamicCompatabilityThreshold(boolean dynamicCompatabilityThreshold) {
		neatConfig.dynamicCompatibilityThreshold = dynamicCompatabilityThreshold;
		return this;
	}
	
    /**
     * Sets the target number of species when using dynamic compatibility threshold.
     * 
     * The compatibility threshold is adjusted to try to maintain this many species.
     * Only relevant when dynamic compatibility threshold is enabled.
     * Default value is 1.
     * 
     * @param targetNumberOfSpecies The target number of species. If less than 1, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setTargetNumberOfSpecies(int targetNumberOfSpecies) {
		if (targetNumberOfSpecies >= 1)
			neatConfig.targetNumberOfSpecies = targetNumberOfSpecies;
		return this;
	}
	
    /**
     * Sets the factor by which to adjust compatibility threshold when using dynamic adjustment.
     * 
     * Controls how quickly the threshold is adjusted to reach the target number of species.
     * Higher values cause faster adjustment but may lead to oscillation.
     * Default value is 0.1.
     * 
     * @param compatabilityThresholdAdjustingFactor The threshold adjustment factor. If less than or equal to zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setCompatabilityThresholdAdjustingFactor(double compatabilityThresholdAdjustingFactor) {
		if (compatabilityThresholdAdjustingFactor > 0)
			neatConfig.compatabilityThresholdAdjustingFactor = compatabilityThresholdAdjustingFactor;
		return this;
	}
	
    /**
     * Sets the array specifying the number of hidden nodes in each hidden layer at initialization.
     * 
     * For example, passing (5, 3) would create two hidden layers with 5 and 3 nodes respectively.
     * An empty array creates networks with no initial hidden nodes.
     * Default value is an empty array.
     * 
     * @param hiddenNodes Variable number of integers defining nodes per hidden layer. If there's a value which is less than zero, that value will not be taken.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setStartingHiddenNodes(int...hiddenNodes) {
		neatConfig.startingHiddenNodes = hiddenNodes;
		return this;
	}
	
    /**
     * Sets the maximum number of hidden nodes allowed in the network (mutation exclusive).
     * 
     * Prevents networks from growing too large during evolution.
     * Default value Integer.MAX_VALUE (uncapped).
     * 
     * @param maxNumberOfHiddenNodes The maximum number of hidden nodes allowed. If less than zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setMaxNumberOfHiddenNodes(int maxNumberOfHiddenNodes) {
		if (maxNumberOfHiddenNodes >= 0)
			neatConfig.maxNumberOfHiddenNodes = maxNumberOfHiddenNodes;
		return this;
	}
	
    /**
     * Sets the default aggregation function for new nodes.
     * 
     * This function determines how a node combines its inputs.
     * Default value is SUM.
     * 
     * @param startingAggregationFunction The default aggregation function. If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setStartingAggregationFunction(AggregationFunction.AGGREGATION_FUNCTION startingAggregationFunction) {
		if (startingAggregationFunction != null)
			neatConfig.startingAggregationFunction = startingAggregationFunction;
		return this;
	}
	
    /**
     * Sets the default activation function for new hidden nodes.
     * 
     * This function determines how a hidden node transforms its aggregated input to output.
     * Default value is SIGMOID.
     * 
     * @param startingActivationFunctionForHiddenNodes The default activation function for hidden nodes. If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setStartingActivationFunctionForHiddenNodes(ActivationFunction.ACTIVATION_FUNCTION startingActivationFunctionForHiddenNodes) {
		if (startingActivationFunctionForHiddenNodes != null)
			neatConfig.startingActivationFunctionForHiddenNode = startingActivationFunctionForHiddenNodes;
		return this;
	}
	
    /**
     * Sets the default activation function for new output nodes.
     * 
     * This function determines how an output node transforms its aggregated input to output.
     * Default value is SIGMOID.
     * 
     * @param startingActivationFunctionForOutputNodes The default activation function for output nodes. If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setStartingActivationFunctionForOutputNodes(ActivationFunction.ACTIVATION_FUNCTION startingActivationFunctionForOutputNodes) {
		if (startingActivationFunctionForOutputNodes != null)
			neatConfig.startingActivationFunctionForOutputNode = startingActivationFunctionForOutputNodes;
		return this;
	}
	
    /**
     * Sets the initial connectivity pattern for new genomes.
     * 
     * Determines how nodes are connected when a network is first created.
     * Different patterns provide different starting points for evolution.
     * Default value is UNCONNECTED (no connections).
     * 
     * @param initConnectivity The initial connectivity pattern. If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setInitConnectivity(CONNECTIVITY initConnectivity) {
		if (initConnectivity != null)
			neatConfig.initConnectivity = initConnectivity;
		return this;
	}
	
	/**
	 * Sets the default activation function for nodes created during evolution.
	 * 
	 * This defines which activation function is used when new nodes are created 
	 * through mutation and no specific function is assigned. This is separate from
	 * the starting activation functions for initial network configuration.
	 * 
	 * Common choices:
	 * - SIGMOID (default)
	 * - RELU
	 * - TANH
	 * - LINEAR
	 * - STEP 
	 * 
	 * @param activationDefault Default activation function for new nodes. If null, then the new value will not be set.
	 * @return This builder instance.
	 */
	public NEATConfigBuilder setActivationDefault(ActivationFunction.ACTIVATION_FUNCTION activationDefault) {
		if (activationDefault != null)
			neatConfig.activationDefault = activationDefault;
		return this;
	}
	
    /**
     * Sets the probability of mutating a node's activation function.
     * 
     * Higher values make activation function mutations more frequent.
     * Default value is 0.
     * 
     * @param activationMutationRate The activation function mutation rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setActivationMutationRate(double activationMutationRate) {
		if (activationMutationRate >= 0 && activationMutationRate <= 1)
			neatConfig.activationMutationRate = activationMutationRate;
		return this;
	}
	
	/**
	 * Sets the default aggregation function for nodes created during evolution.
	 * 
	 * Determines how new nodes will combine their input signals when created
	 * through mutation. This is separate from the starting aggregation function
	 * used during initial network creation.
	 * 
	 * Common choices:
	 * - SUM (default)
	 * - PRODUCT 
	 * - MIN
	 * - MAX
	 * - MEAN
	 * - MAXABS
	 * 
	 * @param aggregationDefault Default aggregation function for new nodes. If null, then the new value will not be set.
	 * @return This builder instance.
	 */
	public NEATConfigBuilder setAggregationDefault(AggregationFunction.AGGREGATION_FUNCTION aggregationDefault) {
		if (aggregationDefault != null)
			neatConfig.aggregationDefault = aggregationDefault;
		return this;
	}
	
    /**
     * Sets the probability of mutating a node's aggregation function.
     * 
     * Higher values make aggregation function mutations more frequent.
     * Default value is 0.
     * 
     * @param aggregationMutationRate The aggregation function mutation rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setAggregationMutationRate(double aggregationMutationRate) {
		if (aggregationMutationRate >= 0 && aggregationMutationRate <= 1)
			neatConfig.aggregationMutationRate = aggregationMutationRate;
		return this;
	}
	
    /**
     * Sets the mean for initializing node bias values.
     * 
     * Used with the bias initialization distribution.
     * Default value is 0.
     * 
     * @param biasInitMean The bias initialization mean
     * @return This builder instance for method chaining
     */
	public NEATConfigBuilder setBiasInitMean(double biasInitMean) {
		neatConfig.biasInitMean = biasInitMean;
		return this;
	}
	
    /**
     * Sets the standard deviation for initializing node bias values.
     * Default value is 1.
     * 
     * @param biasInitStdev The bias initialization standard deviation. If less than zero, the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setBiasInitStdev(double biasInitStdev) {
		if (biasInitStdev >= 0)
			neatConfig.biasInitStdev = biasInitStdev;
		return this;
	}
	
    /**
     * Sets the distribution type for initializing node bias values.
     * 
     * Determines whether biases are initialized with uniform or normal distribution.
     * Default value is NORMAL.
     * 
     * @param biasInitDistributionType The bias initialization distribution type. If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setBiasInitDistributionType(DISTRIBUTION biasInitDistributionType) {
		if (biasInitDistributionType != null)
			neatConfig.biasInitDistributionType = biasInitDistributionType;
		return this;
	}
	
    /**
     * Sets the maximum allowed value for node biases.
     * 
     * Biases are clamped to this value during initialization and mutation.
     * Default value is 50.
     * 
     * @param biasMaxValue The maximum bias value.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setBiasMaxValue(double biasMaxValue) {
		neatConfig.biasMaxValue = biasMaxValue;
		return this;
	}
	
    /**
     * Sets the minimum allowed value for node biases.
     * 
     * Biases are clamped to this value during initialization and mutation.
     * Default value is -50.
     * 
     * @param biasMinValue The minimum bias value.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setBiasMinValue(double biasMinValue) {
		neatConfig.biasMinValue = biasMinValue;
		return this;
	}
	
    /**
     * Sets the power (magnitude) of bias mutations.
     * 
     * Controls how much a bias can change in a single mutation.
     * Larger values allow for bigger jumps in bias values.
     * Default value is 0.1 (10%).
     * 
     * @param biasMutationPower The bias mutation power. If less than or equal to zero, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setBiasMutationPower(double biasMutationPower) {
		if (biasMutationPower > 0)
			neatConfig.biasMutationPower = biasMutationPower;
		return this;
	}
	
    /**
     * Sets the probability of adjusting a node's bias during mutation.
     * 
     * Higher values make small bias adjustments more frequent.
     * Default value is 0.8 (80%).
     * 
     * @param biasAdjustingRate The bias adjustment rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setBiasAdjustingRate(double biasAdjustingRate) {
		if (biasAdjustingRate >= 0 && biasAdjustingRate <= 1)
			neatConfig.biasAdjustingRate = biasAdjustingRate;
		return this;
	}
	
    /**
     * Sets the probability of completely randomizing a node's bias during mutation.
     * 
     * Higher values make complete bias replacements more frequent.
     * Default value is 0.1 (10%).
     * 
     * @param biasRandomizingRate The bias randomization rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setBiasRandomizingRate(double biasRandomizingRate) {
		if (biasRandomizingRate >= 0 && biasRandomizingRate <= 1)
			neatConfig.biasRandomizingRate = biasRandomizingRate;
		return this;
	}
	
    /**
     * Sets the probability of adding a new connection during mutation.
     * 
     * Controls how frequently new connections are added to the network.
     * Higher values lead to more complex networks more quickly.
     * Default value is 0.03 (3%).
     * 
     * @param probAddConnection The add connection probability between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setProbAddConnection(double probAddConnection) {
		if (probAddConnection >= 0 && probAddConnection <= 1)
			neatConfig.probAddConnection = probAddConnection;
		return this;
	}
	
    /**
     * Sets the probability that a new connection will be recurrent.
     * 
     * Only relevant when feedForward is false. Higher values increase
     * the likelihood of creating recurrent (looping) connections.
     * Default value is 0.1 (10% Knowing the probability for adding a connection has succeeded).
     * 
     * @param probRecurrentConnection The recurrent connection probability between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setProbRecurrentConnection(double probRecurrentConnection) {
		if (probRecurrentConnection >= 0 && probRecurrentConnection <= 1)
			neatConfig.probRecurrentConnection = probRecurrentConnection;
		return this;
	}
	
    /**
     * Sets the probability of deleting a connection during mutation.
     * 
     * Controls how frequently connections are removed from the network.
     * Higher values can lead to simpler networks.
     * Default value is 0 (0%).
     * 
     * @param probDeleteConnection The delete connection probability between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setProbDeleteConnection(double probDeleteConnection) {
		if (probDeleteConnection >= 0 && probDeleteConnection <= 1)
			neatConfig.probDeleteConnection = probDeleteConnection;
		return this;
	}
	
    /**
     * Sets whether new connections are enabled by default.
     * 
     * When false, new connections are created in a disabled state and must
     * be activated through mutation.
     * Default value is true.
     * 
     * @param enabledDefault true if new connections are enabled by default, false otherwise.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setEnabledDefault(boolean enabledDefault) {
		neatConfig.enabledDefault = enabledDefault;
		return this;
	}
	
    /**
     * Sets the probability of toggling a connection's enabled status during mutation.
     * 
     * Controls how frequently connections are enabled or disabled.
     * Default value is 0 (0%).
     * 
     * @param enabledMutationRate The connection enabled status mutation rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setEnabledMutationRate(double enabledMutationRate) {
		if (enabledMutationRate >= 0 && enabledMutationRate <= 1)
			neatConfig.enabledMutationRate = enabledMutationRate;
		return this;
	}
	
	/**
	 * Sets the additional probability specifically for toggling enabled connections.
	 * 
	 * This parameter adds to the base enabledMutationRate when considering whether to
	 * toggle an enabled connection. It allows for asymmetric mutation rates between 
	 * enabled and disabled connections.
	 * Default value is 0 (0%).
	 * 
	 * The total probability for toggling an enabled connection becomes:
	 * (enabledMutationRate + enabledRateForEnabled)
	 * 
	 * This parameter lets you fine-tune how frequently enabled connections get disabled
	 * during mutation, independent of how frequently disabled connections get enabled.
	 * A higher value increases the chance of toggling (disabling) connections that are 
	 * currently enabled.
	 * 
	 * @param enabledRateForEnabled Additional mutation rate specifically for enabled connections.
	 * @return This builder instance for method chaining.
	 */
	public NEATConfigBuilder setEnabledRateForEnabled(double enabledRateForEnabled) {
		neatConfig.enabledRateForEnabled = enabledRateForEnabled;
		return this;
	}
	
	/**
	 * Sets the additional probability specifically for toggling disabled connections.
	 * 
	 * This parameter adds to the base enabledMutationRate when considering whether to
	 * toggle a disabled connection. It allows for asymmetric mutation rates between 
	 * enabled and disabled connections.
	 * 
	 * The total probability for toggling a disabled connection becomes:
	 * (enabledMutationRate + enabledRateForDisabled)
	 * 
	 * This parameter lets you fine-tune how frequently disabled connections get enabled
	 * during mutation, independent of how frequently enabled connections get disabled.
	 * A higher value increases the chance of toggling (enabling) connections that are 
	 * currently disabled.
	 * Default value is 0 (0%).
	 * 
	 * @param enabledRateForEnabled Additional mutation rate specifically for disabled connections.
	 * @return This builder instance for method chaining
	 */
	public NEATConfigBuilder setEnabledRateForDisabled(double enabledRateForDisabled) {
		neatConfig.enabledRateForDisabled = enabledRateForDisabled;
		return this;
	}
	
    /**
     * Sets whether the network must be strictly feed-forward (no recurrent connections).
     * 
     * When true, connections from later nodes to earlier nodes are not allowed.
     * This is typically used for problems that don't have a temporal component.
     * Default value is true.
     * 
     * @param feedForward true if the network must be feed-forward, false if recurrent connections are allowed.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setFeedForward(boolean feedForward) {
		neatConfig.feedForward = feedForward;
		return this;
	}
	
    /**
     * Sets the probability of adding a new node during mutation.
     * 
     * Controls how frequently the network topology grows with new neurons.
     * Higher values lead to more complex networks more quickly.
     * Default value is 0.01 (1%).
     * 
     * @param probAddNode The add node probability between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setProbAddNode(double probAddNode) {
		if (probAddNode >= 0 && probAddNode <= 1)
			neatConfig.probAddNode = probAddNode;
		return this;
	}
	
    /**
     * Sets the probability of deleting a node during mutation.
     * 
     * Controls how frequently nodes are removed from the network.
     * Higher values can lead to simpler networks.
     * Default value is 0 (0%).
     * 
     * @param probDeleteNode The delete node probability between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setProbDeleteNode(double probDeleteNode) {
		if (probDeleteNode >= 0 && probDeleteNode <= 1)
			neatConfig.probDeleteNode = probDeleteNode;
		return this;
	}
	
    /**
     * Sets the mean for initializing node response values.
     * 
     * Response values scale the output of a node's activation function.
     * Default value is 0.
     * 
     * @param responseInitMean The response initialization mean.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setResponseInitMean(double responseInitMean) {
		neatConfig.responseInitMean = responseInitMean;
		return this;
	}
	
    /**
     * Sets the standard deviation for initializing node response values.
     * Default value is 0.
     * 
     * @param responseInitStdev The response initialization standard deviation. If less than zero, the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setResponseInitStdev(double responseInitStdev) {
		if (responseInitStdev >= 0)
			neatConfig.responseInitStdev = responseInitStdev;
		return this;
	}
	
    /**
     * Sets the distribution type for initializing node response values.
     * 
     * Determines whether responses are initialized with uniform or normal distribution.
     * Default value is NORMAL.
     * 
     * @param responseInitDistributionType The response initialization distribution type. If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setResponseInitDistributionType(DISTRIBUTION responseInitDistributionType) {
		if (responseInitDistributionType != null)
			neatConfig.responseInitDistributionType = responseInitDistributionType;
		return this;
	}
	
    /**
     * Sets the maximum allowed value for node responses.
     * 
     * Responses are clamped to this value during initialization and mutation.
     * Default value is 50.
     * 
     * @param responseMaxValue The maximum response value.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setResponseMaxValue(double responseMaxValue) {
		neatConfig.responseMaxValue = responseMaxValue;
		return this;
	}
	
    /**
     * Sets the minimum allowed value for node responses.
     * 
     * Responses are clamped to this value during initialization and mutation.
     * Default value is -50.
     * 
     * @param responseMinValue The minimum response value.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setResponseMinValue(double responseMinValue) {
		neatConfig.responseMinValue = responseMinValue;
		return this;
	}
	
    /**
     * Sets the power (magnitude) of response mutations.
     * 
     * Controls how much a response can change in a single mutation.
     * Default value is 0.1.
     * 
     * @param responseMutationPower The response mutation power.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setResponseMutationPower(double responseMutationPower) {
		neatConfig.responseMutationPower = responseMutationPower;
		return this;
	}
	
    /**
     * Sets the probability of adjusting a node's response during mutation.
     * 
     * Higher values make small response adjustments more frequent.
     * Default value is 0.7 (70%).
     * 
     * @param responseAdjustingRate The response adjustment rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setResponseAdjustingRate(double responseAdjustinRate) {
		if (responseAdjustinRate >= 0 && responseAdjustinRate <= 1)
			neatConfig.responseAdjustingRate = responseAdjustinRate;
		return this;
	}
	
    /**
     * Sets the probability of completely randomizing a node's response during mutation.
     * 
     * Higher values make complete response replacements more frequent.
     * Default value is 0.1 (10%).
     * 
     * @param responseRandomizingRate The response randomization rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setResponseRandomizingRate(double responseRandomizingRate) {
		if (responseRandomizingRate >= 0 && responseRandomizingRate <= 1)
			neatConfig.responseRandomizingRate = responseRandomizingRate;
		return this;
	}
	
    /**
     * Sets whether to allow only one structural mutation per genome per generation.
     * 
     * When true, only one structural change (add/delete node/connection) can occur per genome.
     * This can help prevent too rapid topology changes.
     * Default value is false.
     * 
     * @param singleStructuralMutation true if only one structural mutation is allowed, false otherwise.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setSingleStructuralMutation(boolean singleStructuralMutation) {
		neatConfig.singleStructuralMutation = singleStructuralMutation;
		return this;
	}
	
	/**
	 * Sets whether to use a structural mutation advisor to guide mutation decisions.
	 * 
	 * When enabled, the structural mutation advisor implements the following behaviors:
	 * 
	 * 1. If a "add node" mutation is attempted on a genome with no connections,
	 *    it automatically redirects to attempting an "add connection" mutation instead,
	 *    since nodes cannot be added without existing connections to split.
	 * 
	 * 2. If an "add connection" mutation attempts to create a connection that already exists,
	 *    it automatically toggles that connection's enabled status instead of failing,
	 *    allowing the mutation to have a meaningful effect.
	 * 
	 * These intelligent redirects help avoid wasted mutation attempts and increase the
	 * efficiency of the evolutionary process by ensuring mutations have meaningful effects
	 * even in edge cases.
	 * Default value is false.
	 * 
	 * @param structuralMutationAdvisor true to enable the structural mutation advisor, false to disable it.
	 * @return This builder instance for method chaining.
	 */
	public NEATConfigBuilder setStructuralMutationAdvisor(boolean structuralMutationAdvisor) {
		neatConfig.structuralMutationAdvisor = structuralMutationAdvisor;
		return this;
	}
	
    /**
     * Sets the mean for initializing connection weight values.
     * 
     * Used with the weight initialization distribution.
     * Default value is 0.
     * 
     * @param weightInitMean The weight initialization mean.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setWeightInitMean(double weightInitMean) {
		neatConfig.weightInitMean = weightInitMean;
		return this;
	}
	
    /**
     * Sets the standard deviation for initializing connection weight values.
     * Default value is 1.
     * 
     * @param weightInitStdev The weight initialization standard deviation. If less than zero, the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setWeightInitStdev(double weightInitStdev) {
		if (weightInitStdev >= 0)
			neatConfig.weightInitStdev = weightInitStdev;
		return this;
	}
	
    /**
     * Sets the distribution type for initializing connection weight values.
     * 
     * Determines whether weights are initialized with uniform or normal distribution.
     * Default value is NORMAL.
     * 
     * @param weightInitType The weight initialization distribution type. If null, then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setWeightInitDistributionType(DISTRIBUTION weightInitDistributionType) {
		if (weightInitDistributionType != null)
			neatConfig.weightInitDistributionType = weightInitDistributionType;
		return this;
	}
	
    /**
     * Sets the maximum allowed value for connection weights.
     * 
     * Weights are clamped to this value during initialization and mutation.
     * Default value is 50.
     * 
     * @param weightMaxValue The maximum weight value.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setWeightMaxValue(double weightMaxValue) {
		neatConfig.weightMaxValue = weightMaxValue;
		return this;
	}
	
    /**
     * Sets the minimum allowed value for connection weights.
     * 
     * Weights are clamped to this value during initialization and mutation.
     * Default value is -50.
     * 
     * @param weightMinValue The minimum weight value.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setWeightMinValue(double weightMinValue) {
		neatConfig.weightMinValue = weightMinValue;
		return this;
	}
	
    /**
     * Sets the power (magnitude) of weight mutations.
     * 
     * Controls how much a weight can change in a single mutation.
     * Default value is 0.1.
     * 
     * @param weightMutationPower The weight mutation power.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setWeightMutationPower(double weightMutationPower) {
		neatConfig.weightMutationPower = weightMutationPower;
		return this;
	}
	
    /**
     * Sets the probability of adjusting a connection's weight during mutation.
     * 
     * Higher values make small weight adjustments more frequent.
     * Default value is 0.8 (80%).
     * 
     * @param weightAdjustingRate The weight adjustment rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setWeightAdjustingRate(double weightAdjustingRate) {
		if (weightAdjustingRate >= 0 && weightAdjustingRate <= 1)
			neatConfig.weightAdjustingRate = weightAdjustingRate;
		return this;
	}
	
    /**
     * Sets the probability of completely randomizing a connection's weight during mutation.
     * 
     * Higher values make complete weight replacements more frequent.
     * Default value is 0.1 (10%).
     * 
     * @param weightRandomizingRate The weight randomization rate between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setWeightRandomizingRate(double weightRandomizingRate) {
		if (weightRandomizingRate >= 0 && weightRandomizingRate <= 1)
			neatConfig.weightRandomizingRate = weightRandomizingRate;
		return this;
	}
	
    /**
     * Sets the probability of creating each connection when using PARTIAL_* connectivity types.
     * 
     * For partial connectivity patterns, each potential connection is created with this probability.
     * A value of 1.0 would create all possible connections, effectively making it equivalent to
     * a FULL_* connectivity type.
     * Default value is 0.5 (50%).
     * 
     * @param probConnectInit The connection creation probability between 0 and 1. If not [0~1], then the new value will not be set.
     * @return This builder instance for method chaining.
     */
	public NEATConfigBuilder setProbConnectInit(double probConnectInit) {
		if (probConnectInit >= 0 && probConnectInit <= 1)
			neatConfig.probConnectInit = probConnectInit;
		return this;
	}

    /**
     * Builds and returns the configured NEATConfig instance.
     * 
     * This method finalizes the building process and returns the completely
     * configured NEATConfig object. After calling this method, this builder
     * instance should no longer be used.
     * 
     * @return The fully configured NEATConfig instance.
     */
	public NEATConfig build() {
		return neatConfig;
	}
	
}