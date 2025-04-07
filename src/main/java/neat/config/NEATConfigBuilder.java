package main.java.neat.config;

import main.java.neat.config.NEATConfig.CONNECTIVITY;
import main.java.neat.config.NEATConfig.DISTRIBUTION;
import main.java.neat.config.NEATConfig.FITNESS_CRITERION;
import main.java.neat.config.NEATConfig.SELECTION_TYPE;
import main.java.neat.config.NEATConfig.SPECIES_FITNESS_FUNCTION;
import main.java.neat.functions.ActivationFunction;
import main.java.neat.functions.AggregationFunction;

/**
 * 
 * @author Gamer
 *
 */
public class NEATConfigBuilder {
	
	private NEATConfig neatConfig;
	
	/**
	 * 
	 * @param aggregationConfig
	 * @param activationConfig
	 */
	public NEATConfigBuilder(int populationSize, int numberOfInputs, int numberOfOutputs,
			AggregationConfig aggregationConfig, ActivationConfig activationConfig) {
		neatConfig = new NEATConfig(populationSize, numberOfInputs, numberOfOutputs, aggregationConfig, activationConfig);
	}
	
	/**
	 * 
	 * @param populationSize
	 * @return
	 */
	public NEATConfigBuilder setPopulationSize(int populationSize) {
		neatConfig.populationSize = Math.max(0, populationSize);
		return this;
	}
	
	/**
	 * 
	 * @param fitnessCriterion
	 * @return
	 */
	public NEATConfigBuilder setFitnessCriterion(FITNESS_CRITERION fitnessCriterion) {
		neatConfig.fitnessCriterion = fitnessCriterion;
		return this;
	}
	
	/**
	 * 
	 * @param fitnessTermination
	 * @return
	 */
	public NEATConfigBuilder setFitnessTermination(boolean fitnessTermination) {
		neatConfig.fitnessTermination = fitnessTermination;
		return this;
	}
	
	/**
	 * 
	 * @param fitnessThreshold
	 * @return
	 */
	public NEATConfigBuilder setFitnessThreshold(double fitnessThreshold) {
		neatConfig.fitnessThreshold = Math.max(0, fitnessThreshold);
		return this;
	}
	
	/**
	 * 
	 * @param generationTermination
	 * @return
	 */
	public NEATConfigBuilder setGenerationTermination(boolean generationTermination) {
		neatConfig.generationTermination = generationTermination;
		return this;
	}
	
	/**
	 * 
	 * @param generationThreshold
	 * @return
	 */
	public NEATConfigBuilder setGenerationThreshold(int generationThreshold) {
		neatConfig.generationThreshold = generationThreshold;
		return this;
	}
	
	/**
	 * 
	 * @param speciesFitnessFunction
	 * @return
	 */
	public NEATConfigBuilder setSpeciesFitnessFunction(SPECIES_FITNESS_FUNCTION speciesFitnessFunction) {
		neatConfig.speciesFitnessFunction = speciesFitnessFunction;
		return this;
	}
	
	/**
	 * 
	 * @param stagnation
	 * @return
	 */
	public NEATConfigBuilder setStagnation(int stagnation) {
		neatConfig.stagnation = Math.max(0, stagnation);
		return this;
	}
	
	/**
	 * 
	 * @param speciesElitism
	 * @return
	 */
	public NEATConfigBuilder setSpeciesElitism(int speciesElitism) {
		neatConfig.speciesElitism = Math.max(0, speciesElitism);
		return this;
	}
	
	/**
	 * 
	 * @param elitism
	 * @return
	 */
	public NEATConfigBuilder setElitism(int elitism) {
		neatConfig.elitism = Math.max(0,elitism);
		return this;
	}
	
	/**
	 * 
	 * @param survivalThreshold
	 * @return
	 */
	public NEATConfigBuilder setSurvivalThreshold(double survivalThreshold) {
		neatConfig.survivalThreshold = Math.min(Math.max(0, survivalThreshold), 1);
		return this;
	}
	
	/**
	 * 
	 * @param selectionType
	 * @return
	 */
	public NEATConfigBuilder setSelectionType(SELECTION_TYPE selectionType){
		neatConfig.selectionType = selectionType;
		return this;
	}
	
	/**
	 * 
	 * @param tournamentSize
	 * @return
	 */
	public NEATConfigBuilder setTournamentSize(int tournamentSize) {
		neatConfig.tournamentSize = tournamentSize;
		return this;
	}
	
	/**
	 * 
	 * @param compatibilityThreshold
	 * @return
	 */
	public NEATConfigBuilder setCompatibilityThreshold(double compatibilityThreshold) {
		neatConfig.compatibilityThreshold = compatibilityThreshold;
		return this;
	}
	
	/**
	 * 
	 * @param compatibilityExcessCoefficient
	 * @return
	 */
	public NEATConfigBuilder setCompatibilityExcessCoefficient(double compatibilityExcessCoefficient) {
		neatConfig.compatibilityExcessCoefficient = compatibilityExcessCoefficient;
		return this;
	}
	
	/**
	 * 
	 * @param compatibilityDisjointCoefficient
	 * @return
	 */
	public NEATConfigBuilder setCompatibilityDisjointCoefficient(double compatibilityDisjointCoefficient) {
		neatConfig.compatibilityDisjointCoefficient = compatibilityDisjointCoefficient;
		return this;
	}
	
	/**
	 * 
	 * @param compatibilityWeightCoefficient
	 * @return
	 */
	public NEATConfigBuilder setCompatibilityWeightCoefficient(double compatibilityWeightCoefficient) {
		neatConfig.compatibilityWeightCoefficient = compatibilityWeightCoefficient;
		return this;
	}
	
	/**
	 * 
	 * @param dynamicCompatabilityThreshold
	 * @return
	 */
	public NEATConfigBuilder setDynamicCompatabilityThreshold(boolean dynamicCompatabilityThreshold) {
		neatConfig.dynamicCompatibilityThreshold = dynamicCompatabilityThreshold;
		return this;
	}
	
	/**
	 * 
	 * @param targetNumberOfSpecies
	 * @return
	 */
	public NEATConfigBuilder setTargetNumberOfSpecies(int targetNumberOfSpecies) {
		neatConfig.targetNumberOfSpecies = targetNumberOfSpecies;
		return this;
	}
	
	/**
	 * 
	 * @param compatabilityThresholdAdjustingFactor
	 * @return
	 */
	public NEATConfigBuilder setCompatabilityThresholdAdjustingFactor(double compatabilityThresholdAdjustingFactor) {
		neatConfig.compatabilityThresholdAdjustingFactor = compatabilityThresholdAdjustingFactor;
		return this;
	}
	
	/**
	 * 
	 * @param numberOfInputs
	 * @return
	 */
	public NEATConfigBuilder setNumberOfInputs(int numberOfInputs) {
		neatConfig.numberOfInputs = numberOfInputs;
		return this;
	}
	
	/**
	 * 
	 * @param numberOfOutputs
	 * @return
	 */
	public NEATConfigBuilder setNumberOfOutputs(int numberOfOutputs) {
		neatConfig.numberOfOutputs = numberOfOutputs;
		return this;
	}
	
	/**
	 * 
	 * @param hiddenNodes
	 * @return
	 */
	public NEATConfigBuilder setStartingHiddenNodes(int...hiddenNodes) {
		neatConfig.startingHiddenNodes = hiddenNodes;
		return this;
	}
	
	/**
	 * 
	 * @param maxNumberOfHiddenNodes
	 * @return
	 */
	public NEATConfigBuilder setMaxNumberOfHiddenNodes(int maxNumberOfHiddenNodes) {
		neatConfig.maxNumberOfHiddenNodes = maxNumberOfHiddenNodes;
		return this;
	}
	
	/**
	 * 
	 * @param startingAggregationFunction
	 * @return
	 */
	public NEATConfigBuilder setStartingAggregationFunction(AggregationFunction.AGGREGATION_FUNCTION startingAggregationFunction) {
		neatConfig.startingAggregationFunction = startingAggregationFunction;
		return this;
	}
	
	/**
	 * 
	 * @param startingActivationFunctionForHiddenNodes
	 * @return
	 */
	public NEATConfigBuilder setStartingActivationFunctionForHiddenNodes(ActivationFunction.ACTIVATION_FUNCTION startingActivationFunctionForHiddenNodes) {
		neatConfig.startingActivationFunctionForHiddenNode = startingActivationFunctionForHiddenNodes;
		return this;
	}
	
	/**
	 * 
	 * @param startingActivationFunctionForOutputNodes
	 * @return
	 */
	public NEATConfigBuilder setStartingActivationFunctionForOutputNodes(ActivationFunction.ACTIVATION_FUNCTION startingActivationFunctionForOutputNodes) {
		neatConfig.startingActivationFunctionForOutputNode = startingActivationFunctionForOutputNodes;
		return this;
	}
	
	/**
	 * 
	 * @param initConnectivity
	 * @return
	 */
	public NEATConfigBuilder setInitConnectivity(CONNECTIVITY initConnectivity) {
		neatConfig.initConnectivity = initConnectivity;
		return this;
	}
	
	/**
	 * 
	 * @param activationDefault
	 * @return
	 */
	public NEATConfigBuilder setActivationDefault(ActivationFunction.ACTIVATION_FUNCTION activationDefault){
		neatConfig.activationDefault = activationDefault;
		return this;
	}
	
	/**
	 * 
	 * @param activationMutationRate
	 * @return
	 */
	public NEATConfigBuilder setActivationMutationRate(double activationMutationRate) {
		neatConfig.activationMutationRate = activationMutationRate;
		return this;
	}
	
	/**
	 * 
	 * @param aggregationDefault
	 * @return
	 */
	public NEATConfigBuilder setAggregationDefault(AggregationFunction.AGGREGATION_FUNCTION aggregationDefault) {
		neatConfig.aggregationDefault = aggregationDefault;
		return this;
	}
	
	/**
	 * 
	 * @param aggregationMutationRate
	 * @return
	 */
	public NEATConfigBuilder setAggregationMutationRate(double aggregationMutationRate) {
		neatConfig.aggregationMutationRate = aggregationMutationRate;
		return this;
	}
	
	/**
	 * 
	 * @param biasInitMean
	 * @return
	 */
	public NEATConfigBuilder setBiasInitMean(double biasInitMean) {
		neatConfig.biasInitMean = biasInitMean;
		return this;
	}
	
	/**
	 * 
	 * @param biasInitStdev
	 * @return
	 */
	public NEATConfigBuilder setBiasInitStdev(double biasInitStdev) {
		neatConfig.biasInitStdev = biasInitStdev;
		return this;
	}
	
	/**
	 * 
	 * @param biasInitType
	 * @return
	 */
	public NEATConfigBuilder setBiasInitType(DISTRIBUTION biasInitType) {
		neatConfig.biasInitType = biasInitType;
		return this;
	}
	
	/**
	 * 
	 * @param biasMaxValue
	 * @return
	 */
	public NEATConfigBuilder setBiasMaxValue(double biasMaxValue) {
		neatConfig.biasMaxValue = biasMaxValue;
		return this;
	}
	
	/**
	 * 
	 * @param biasMinValue
	 * @return
	 */
	public NEATConfigBuilder setBiasMinValue(double biasMinValue) {
		neatConfig.biasMinValue = biasMinValue;
		return this;
	}
	
	/**
	 * 
	 * @param biasMutationPower
	 * @return
	 */
	public NEATConfigBuilder setBiasMutationPower(double biasMutationPower) {
		neatConfig.biasMutationPower = biasMutationPower;
		return this;
	}
	
	/**
	 * 
	 * @param biasAdjustingRate
	 * @return
	 */
	public NEATConfigBuilder setBiasAdjustingRate(double biasAdjustingRate) {
		neatConfig.biasAdjustingRate = biasAdjustingRate;
		return this;
	}
	
	/**
	 * 
	 * @param biasRandomizingRate
	 * @return
	 */
	public NEATConfigBuilder setBiasRandomizingRate(double biasRandomizingRate) {
		neatConfig.biasRandomizingRate = biasRandomizingRate;
		return this;
	}
	
	/**
	 * 
	 * @param probAddConnection
	 * @return
	 */
	public NEATConfigBuilder setProbAddConnection(double probAddConnection) {
		neatConfig.probAddConnection = probAddConnection;
		return this;
	}
	
	/**
	 * 
	 * @param probRecurrentConnection
	 * @return
	 */
	public NEATConfigBuilder setProbRecurrentConnection(double probRecurrentConnection) {
		neatConfig.probRecurrentConnection = probRecurrentConnection;
		return this;
	}
	
	/**
	 * 
	 * @param probDeleteConnection
	 * @return
	 */
	public NEATConfigBuilder setProbDeleteConnection(double probDeleteConnection) {
		neatConfig.probDeleteConnection = probDeleteConnection;
		return this;
	}
	
	/**
	 * 
	 * @param enabledDefault
	 * @return
	 */
	public NEATConfigBuilder setEnabledDefault(boolean enabledDefault) {
		neatConfig.enabledDefault = enabledDefault;
		return this;
	}
	
	/**
	 * 
	 * @param enabledMutationRate
	 * @return
	 */
	public NEATConfigBuilder setEnabledMutationRate(double enabledMutationRate) {
		neatConfig.enabledMutationRate = enabledMutationRate;
		return this;
	}
	
	/**
	 * 
	 * @param enabledRateToEnabled
	 * @return
	 */
	public NEATConfigBuilder setEnabledRateToEnabled(double enabledRateToEnabled) {
		neatConfig.enabledRateToEnabled = enabledRateToEnabled;
		return this;
	}
	
	/**
	 * 
	 * @param enabledRateToDisabled
	 * @return
	 */
	public NEATConfigBuilder setEnabledRateToDisabled(double enabledRateToDisabled) {
		neatConfig.enabledRateToDisabled = enabledRateToDisabled;
		return this;
	}
	
	/**
	 * 
	 * @param feedForward
	 * @return
	 */
	public NEATConfigBuilder setFeedForward(boolean feedForward) {
		neatConfig.feedForward = feedForward;
		return this;
	}
	
	/**
	 * 
	 * @param probAddNode
	 * @return
	 */
	public NEATConfigBuilder setProbAddNode(double probAddNode) {
		neatConfig.probAddNode = probAddNode;
		return this;
	}
	
	/**
	 * 
	 * @param probDeleteNode
	 * @return
	 */
	public NEATConfigBuilder setProbDeleteNode(double probDeleteNode) {
		neatConfig.probDeleteNode = probDeleteNode;
		return this;
	}
	
	/**
	 * 
	 * @param responseInitMean
	 * @return
	 */
	public NEATConfigBuilder setResponseInitMean(double responseInitMean) {
		neatConfig.responseInitMean = responseInitMean;
		return this;
	}
	
	/**
	 * 
	 * @param responseInitStdev
	 * @return
	 */
	public NEATConfigBuilder setResponseInitStdev(double responseInitStdev) {
		neatConfig.responseInitStdev = responseInitStdev;
		return this;
	}
	
	/**
	 * 
	 * @param responseInitType
	 * @return
	 */
	public NEATConfigBuilder setResponseInitType(DISTRIBUTION responseInitType) {
		neatConfig.responseInitType = responseInitType;
		return this;
	}
	
	/**
	 * 
	 * @param responseMaxValue
	 * @return
	 */
	public NEATConfigBuilder setResponseMaxValue(double responseMaxValue) {
		neatConfig.responseMaxValue = responseMaxValue;
		return this;
	}
	
	/**
	 * 
	 * @param responseMinValue
	 * @return
	 */
	public NEATConfigBuilder setResponseMinValue(double responseMinValue) {
		neatConfig.responseMinValue = responseMinValue;
		return this;
	}
	
	/**
	 * 
	 * @param responseMutationPower
	 * @return
	 */
	public NEATConfigBuilder setResponseMutationPower(double responseMutationPower) {
		neatConfig.responseMutationPower = responseMutationPower;
		return this;
	}
	
	/**
	 * 
	 * @param responseAdjustinRate
	 * @return
	 */
	public NEATConfigBuilder setResponseAdjustingRate(double responseAdjustinRate) {
		neatConfig.responseAdjustingRate = responseAdjustinRate;
		return this;
	}
	
	/**
	 * 
	 * @param responseRandomizingRate
	 * @return
	 */
	public NEATConfigBuilder setResponseRandomizingRate(double responseRandomizingRate) {
		neatConfig.responseRandomizingRate = responseRandomizingRate;
		return this;
	}
	
	/**
	 * 
	 * @param singleStructuralMutation
	 * @return
	 */
	public NEATConfigBuilder setSingleStructuralMutation(boolean singleStructuralMutation) {
		neatConfig.singleStructuralMutation = singleStructuralMutation;
		return this;
	}
	
	/**
	 * 
	 * @param structuralMutationAdvisor
	 * @return
	 */
	public NEATConfigBuilder setStructuralMutationAdvisor(boolean structuralMutationAdvisor) {
		neatConfig.structuralMutationAdvisor = structuralMutationAdvisor;
		return this;
	}
	
	/**
	 * 
	 * @param weightInitMean
	 * @return
	 */
	public NEATConfigBuilder setWeightInitMean(double weightInitMean) {
		neatConfig.weightInitMean = weightInitMean;
		return this;
	}
	
	/**
	 * 
	 * @param weightInitStdev
	 * @return
	 */
	public NEATConfigBuilder setWeightInitStdev(double weightInitStdev) {
		neatConfig.weightInitStdev = weightInitStdev;
		return this;
	}
	
	/**
	 * 
	 * @param weightInitType
	 * @return
	 */
	public NEATConfigBuilder setWeightInitType(DISTRIBUTION weightInitType) {
		neatConfig.weightInitType = weightInitType;
		return this;
	}
	
	/**
	 * 
	 * @param weightMaxValue
	 * @return
	 */
	public NEATConfigBuilder setWeightMaxValue(double weightMaxValue) {
		neatConfig.weightMaxValue = weightMaxValue;
		return this;
	}
	
	/**
	 * 
	 * @param weightMinValue
	 * @return
	 */
	public NEATConfigBuilder setWeightMinValue(double weightMinValue) {
		neatConfig.weightMinValue = weightMinValue;
		return this;
	}
	
	/**
	 * 
	 * @param weightMutationPower
	 * @return
	 */
	public NEATConfigBuilder setWeightMutationPower(double weightMutationPower) {
		neatConfig.weightMutationPower = weightMutationPower;
		return this;
	}
	
	/**
	 * 
	 * @param weightAdjustingRate
	 * @return
	 */
	public NEATConfigBuilder setWeightAdjustingRate(double weightAdjustingRate) {
		neatConfig.weightAdjustingRate = weightAdjustingRate;
		return this;
	}
	
	/**
	 * 
	 * @param weightRandomizingRate
	 * @return
	 */
	public NEATConfigBuilder setWeightRandomizingRate(double weightRandomizingRate) {
		neatConfig.weightRandomizingRate = weightRandomizingRate;
		return this;
	}
	
	/**
	 * 
	 * @param probConnectInit
	 * @return
	 */
	public NEATConfigBuilder setProbConnectInit(double probConnectInit) {
		neatConfig.probConnectInit = probConnectInit;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public NEATConfig build() {
		return neatConfig;
	}
	
}