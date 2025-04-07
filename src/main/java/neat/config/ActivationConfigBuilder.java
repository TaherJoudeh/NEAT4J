package main.java.neat.config;

import main.java.neat.functions.ActivationFunction.ACTIVATION_FUNCTION;

/**
 * 
 * @author Gamer
 *
 */
public class ActivationConfigBuilder {
		
		private ActivationConfig activationConfig;
		
		/**
		 * 
		 */
		public ActivationConfigBuilder() {
			activationConfig = new ActivationConfig();
		}
		
		/**
		 * 
		 * @param activationFunction
		 * @return
		 */
		public ActivationConfigBuilder addActivationFunction(ACTIVATION_FUNCTION activationFunction) {
			activationConfig.addActivationFunction(activationFunction);
			return this;
		}
		
		/**
		 * 
		 * @param threshold
		 * @return
		 */
		public ActivationConfigBuilder setSigmoidActivationThreshold(double threshold) {
			activationConfig.sigmoidActivationThreshold = threshold;
			return this;
		}
		
		/**
		 * 
		 * @param threshold
		 * @return
		 */
		public ActivationConfigBuilder setTanhActivationThreshold(double threshold) {
			activationConfig.tanhActivationThreshold = threshold;
			return this;
		}
		
		/**
		 * 
		 * @param threshold
		 * @return
		 */
		public ActivationConfigBuilder setStepActivationThreshold(double threshold) {
			activationConfig.stepActivationThreshold = threshold;
			return this;
		}
		
		/**
		 * 
		 * @param threshold
		 * @return
		 */
		public ActivationConfigBuilder setReluActivationThreshold(double threshold) {
			activationConfig.reluActivationThreshold = threshold;
			return this;
		}
		
		/**
		 * 
		 * @param threshold
		 * @return
		 */
		public ActivationConfigBuilder setLinearActivationThreshold(double threshold) {
			activationConfig.linearActivationThreshold = threshold;
			return this;
		}
		
		/**
		 * 
		 * @param leak
		 * @return
		 */
		public ActivationConfigBuilder setReluLeak(double leak) {
			activationConfig.reluLeak = leak;
			return this;
		}
		
		/**
		 * 
		 * @return
		 */
		public ActivationConfig build() {
			return activationConfig;
		}
		
	}
