package main.java.neat.config;

import main.java.neat.functions.ActivationFunction.ACTIVATION_FUNCTION;

/**
 * Builder class for creating and configuring ActivationConfig instances.
 * 
 * This class implements the Builder pattern to provide a fluent API for configuring
 * activation functions in a NEAT implementation. It allows for method chaining to set
 * multiple parameters in a readable and maintainable way.
 * 
 * Example usage:
 * <pre>
 * ActivationConfig config = new ActivationConfigBuilder()
 *     .addActivationFunction(ACTIVATION_FUNCTION.SIGMOID)
 *     .addActivationFunction(ACTIVATION_FUNCTION.RELU)
 *     .setSigmoidActivationThreshold(0.3)
 *     .setReluLeak(0.1)
 *     .build();
 * </pre>
 * 
 * @author Taher Joudeh
 */
public class ActivationConfigBuilder {
		
		/**
		 * The ActivationConfig instance being built by this builder.
		 * This is initialized in the constructor and modified by the builder methods.
		 */
		private ActivationConfig activationConfig;
		
        /**
         * Creates a new ActivationConfigBuilder with an empty ActivationConfig.
         * The created ActivationConfig has no activation functions allowed by default.
         */
		public ActivationConfigBuilder() {
			activationConfig = new ActivationConfig();
		}
		
        /**
         * Adds an activation function to the list of allowed functions.
         * 
         * This method allows the NEAT algorithm to use the specified activation function
         * during network evolution. Multiple activation functions can be added by calling
         * this method multiple times.
         * 
         * @param activationFunction The activation function to allow in the network
         * @return This builder instance for method chaining
         */
		public ActivationConfigBuilder addActivationFunction(ACTIVATION_FUNCTION activationFunction) {
			activationConfig.addActivationFunction(activationFunction);
			return this;
		}
		
        /**
         * Sets the activation threshold for the sigmoid function.
         * 
         * The sigmoid activation threshold determines the input value at which the
         * activation is considered "on" (1) or "off" (0).
         * Default value is 0.5.
         * 
         * @param threshold The threshold value for the sigmoid activation function
         * @return This builder instance for method chaining
         */
		public ActivationConfigBuilder setSigmoidActivationThreshold(double threshold) {
			activationConfig.sigmoidActivationThreshold = threshold;
			return this;
		}
		
        /**
         * Sets the activation threshold for the tanh function.
         * 
         * The tanh activation threshold determines the input value at which the
         * activation is considered "on" (1) or "off" (-1).
         * Default value is 0.
         * 
         * @param threshold The threshold value for the tanh activation function
         * @return This builder instance for method chaining
         */
		public ActivationConfigBuilder setTanhActivationThreshold(double threshold) {
			activationConfig.tanhActivationThreshold = threshold;
			return this;
		}
		
        /**
         * Sets the activation threshold for the step function.
         * 
         * The step activation threshold determines the input value at which the
         * function output steps from 0 to 1.
         * Default value is 0.
         * 
         * @param threshold The threshold value for the step activation function
         * @return This builder instance for method chaining
         */
		public ActivationConfigBuilder setStepActivationThreshold(double threshold) {
			activationConfig.stepActivationThreshold = threshold;
			return this;
		}
		
        /**
         * Sets the activation threshold for the ReLU function.
         * 
         * The ReLU activation threshold determines the input value below which
         * the output is zero. Default value is 0.
         * 
         * @param threshold The threshold value for the ReLU activation function
         * @return This builder instance for method chaining
         */
		public ActivationConfigBuilder setReluActivationThreshold(double threshold) {
			activationConfig.reluActivationThreshold = threshold;
			return this;
		}
		
        /**
         * Sets the activation threshold for the linear function.
         * 
         * The linear activation threshold provides an offset to the linear function.
         * Default value is 0.
         * 
         * @param threshold The threshold value for the linear activation function
         * @return This builder instance for method chaining
         */
		public ActivationConfigBuilder setLinearActivationThreshold(double threshold) {
			activationConfig.linearActivationThreshold = threshold;
			return this;
		}
		
        /**
         * Sets the leak parameter for Leaky ReLU activation.
         * 
         * The leak parameter determines the slope of the ReLU function for inputs
         * below the activation threshold. A value of 0 gives standard ReLU behavior,
         * while positive values allow some signal to "leak through" for negative inputs.
         * 
         * @param leak The leak value for Leaky ReLU (typically a small positive value like 0.01). If less than zero, the new value will not be set.
         * @return This builder instance for method chaining
         */
		public ActivationConfigBuilder setReluLeak(double leak) {
			if (leak >= 0)
				activationConfig.reluLeak = leak;
			return this;
		}
		
        /**
         * Builds and returns the configured ActivationConfig instance.
         * 
         * This method finalizes the building process and returns the fully
         * configured ActivationConfig object. After calling this method,
         * the builder should no longer be used.
         * 
         * @return The configured ActivationConfig instance
         */
		public ActivationConfig build() {
			return activationConfig;
		}
		
	}
