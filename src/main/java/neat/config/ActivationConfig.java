package main.java.neat.config;

import java.io.Serializable;
import java.util.ArrayList;
import main.java.neat.functions.ActivationFunction.ACTIVATION_FUNCTION;

/**
 * Configuration class for neural network activation functions.
 * This class manages the available activation functions for nodes in the NEAT network
 * and their associated threshold parameters. Activation functions determine how a node
 * transforms its input signal into an output signal.
 * 
 * @author Taher Joudeh
 */
public class ActivationConfig implements Serializable {

	private static final long serialVersionUID = -1282542185781644999L;
	
    /**
     * List of activation functions that are allowed to be used in this NEAT implementation.
     * This restricts which activation functions can be assigned to nodes during mutation.
     */
	private ArrayList<ACTIVATION_FUNCTION> allowedActivationFunctions;
	
    /**
     * Threshold value for the sigmoid activation function.
     * Controls the output activation level. Default value is 0.5.
     * Output is 1 if the input is above this threshold, 0 otherwise.
     */
	protected double sigmoidActivationThreshold = 0.5d;
    /**
     * Threshold value for the tanh activation function.
     * Controls the output activation level. Default value is 0.
     * Output is 1 if the input is above this threshold, -1 otherwise.
     */
	protected double tanhActivationThreshold = 0;
    /**
     * Threshold value for the step activation function.
     * Controls the output activation level. Default value is 0.
     * Output is 1 if the input is above this threshold, 0 otherwise.
     */
	protected double stepActivationThreshold = 0;
    /**
     * Threshold value for the ReLU activation function.
     * Controls the output activation level. Default value is 0.
     * Output is the input if the input is above this threshold, 0 otherwise.
     */
	protected double reluActivationThreshold = 0;
    /**
     * Threshold value for the linear activation function.
     * Controls the output activation level. Default value is 0.
     * Typically used in combination with other parameters in the activation function.
     */
	protected double linearActivationThreshold = 0;
	
    /**
     * Leak parameter for Leaky ReLU activation function.
     * Controls the slope of the function for inputs below the activation threshold. Default value is 0.
     * A higher value allows more signal to "leak through" for negative inputs.
     */
	protected double reluLeak = 0;
	
    /**
     * Protected constructor for the ActivationConfig.
     * Creates an empty configuration with no activation functions allowed.
     * Use the builder pattern or defaultActivationConfig() to create instances.
     */
	protected ActivationConfig() {
		allowedActivationFunctions = new ArrayList<> ();
	}
	
	 /**
     * Gets the threshold value for sigmoid activation function.
     * @return The sigmoid activation threshold
     */
	public double getSigmoidActivationThreshold() { return sigmoidActivationThreshold; }
    /**
     * Gets the threshold value for tanh activation function.
     * @return The tanh activation threshold
     */
	public double getTanhActivationThreshold() { return tanhActivationThreshold; }
    /**
     * Gets the threshold value for step activation function.
     * @return The step activation threshold
     */
	public double getStepActivationThreshold() { return stepActivationThreshold; }
    /**
     * Gets the threshold value for ReLU activation function.
     * @return The ReLU activation threshold
     */
	public double getReluActivationThreshold() { return reluActivationThreshold; }
    /**
     * Gets the threshold value for linear activation function.
     * @return The linear activation threshold
     */
	public double getLinearActivationThreshold() { return linearActivationThreshold; }
    /**
     * Gets the leak parameter for Leaky ReLU activation function.
     * @return The ReLU leak parameter
     */
	public double getReluLeak() { return reluLeak; }
	
    /**
     * Adds an activation function to the list of allowed functions.
     * This method ensures each function appears only once in the list.
     * 
     * @param activationFunction The activation function to add
     */
	protected void addActivationFunction(ACTIVATION_FUNCTION activationFunction) {
		if (activationFunction != ACTIVATION_FUNCTION.RANDOM && !allowedActivationFunctions.contains(activationFunction))
			allowedActivationFunctions.add(activationFunction);
	}
	
    /**
     * Gets the list of currently allowed activation functions.
     * These are the functions that can be used by the NEAT algorithm.
     * 
     * @return ArrayList of allowed activation function types
     */
	public ArrayList<ACTIVATION_FUNCTION> getAllowedActivationFunctions() {
		return allowedActivationFunctions;
	}
	
}
