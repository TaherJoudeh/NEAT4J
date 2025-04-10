package main.java.neat.config;

import main.java.neat.functions.ActivationFunction.ACTIVATION_FUNCTION;
import main.java.neat.functions.AggregationFunction.AGGREGATION_FUNCTION;

/**
 * Factory class for creating standard NEAT configurations.
 * 
 * This class centralizes the creation of properly configured objects for the NEAT algorithm,
 * including activation functions, and aggregation functions.
 * It implements the Factory pattern to encapsulate the complexity of creating well-formed
 * configuration objects with appropriate default values.
 * 
 * The ConfigFactory provides several benefits:
 * - Ensures consistent default configurations across the implementation
 * - Simplifies the creation of commonly used configurations
 * - Isolates configuration creation logic from the rest of the system
 * - Provides domain-specific configurations for different problem types
 * 
 * This factory is designed to be extended as needed to include additional
 * specialized configurations for different problem domains.
 * 
 * @author Taher Joudeh
 */
public class ConfigFactory {

	/**
	 * Creates a default aggregation configuration for NEAT networks.
	 * 
	 * This factory method provides a standard configuration using the SUM aggregation
	 * function, which is the most commonly used aggregation method in neural networks.
	 * The SUM function combines multiple inputs by adding them together before
	 * applying the activation function.
	 * 
	 * The SUM aggregation is chosen as the default because:
	 * - It mimics the biological neuron's behavior of summing synaptic inputs
	 * - It works well with most activation functions
	 * - It's computationally efficient
	 * - It's the standard in most neural network implementations
	 * 
	 * @return A new AggregationConfig instance configured with the SUM aggregation function
	 */
	public static AggregationConfig createDefaultAggregationConfig() {		
		return new AggregationConfig(AGGREGATION_FUNCTION.SUM);
	}
	
	/**
	 * Creates a default activation configuration for NEAT networks.
	 * 
	 * This factory method provides a standard configuration that includes only the 
	 * sigmoid activation function, which is widely used as a default in neural networks.
	 * The sigmoid function maps any input value to an output between 0 and 1, with an
	 * S-shaped curve that provides smooth transitions and good gradient properties.
	 * 
	 * The sigmoid activation is chosen as the default because:
	 * - It produces bounded outputs (between 0 and 1)
	 * - It has desirable differentiability properties for training
	 * - It works well for a wide range of problems
	 * - It's historically the most common activation function in neural networks
	 * 
	 * The default configuration uses a threshold of 0.5 for determining when a 
	 * node is considered activated.
	 * 
	 * @return A new ActivationConfig instance configured with the sigmoid activation function
	 */
	public static ActivationConfig createDefaultActivationConfig() {
		ActivationConfig actConfig = new ActivationConfig();
		actConfig.addActivationFunction(ACTIVATION_FUNCTION.SIGMOID);
		return actConfig;
	}
	
}
