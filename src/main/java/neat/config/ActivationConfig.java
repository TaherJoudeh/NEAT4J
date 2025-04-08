package main.java.neat.config;

import java.io.Serializable;
import java.util.ArrayList;
import main.java.neat.functions.ActivationFunction.ACTIVATION_FUNCTION;

/**
 * 
 * @author Taher Joudeh
 *
 */
public class ActivationConfig implements Serializable {

	private static final long serialVersionUID = -1282542185781644999L;
	
	private ArrayList<ACTIVATION_FUNCTION> allowedActivationFunctions;
	
	protected double sigmoidActivationThreshold = 0.5d;
	protected double tanhActivationThreshold = 0;
	protected double stepActivationThreshold = 0;
	protected double reluActivationThreshold = 0;
	protected double linearActivationThreshold = 0;
	
	protected double reluLeak;
	
	/**
	 * 
	 */
	protected ActivationConfig() {
		allowedActivationFunctions = new ArrayList<> ();
	}
	
	public double getSigmoidActivationThreshold() { return sigmoidActivationThreshold; }
	public double getTanhActivationThreshold() { return tanhActivationThreshold; }
	public double getStepActivationThreshold() { return stepActivationThreshold; }
	public double getReluActivationThreshold() { return reluActivationThreshold; }
	public double getLinearActivationThreshold() { return linearActivationThreshold; }
	
	public double getReluLeak() { return reluLeak; }
	
	/**
	 * 
	 * @param activationFunction
	 */
	protected void addActivationFunction(ACTIVATION_FUNCTION activationFunction) {
		if (!allowedActivationFunctions.contains(activationFunction))
			allowedActivationFunctions.add(activationFunction);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ACTIVATION_FUNCTION> getAllowedActivationFunctions() {
		return allowedActivationFunctions;
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivationConfig defaultActivationConfig() {
		ActivationConfig actConfig = new ActivationConfig();
		actConfig.addActivationFunction(ACTIVATION_FUNCTION.SIGMOID);
		return actConfig;
	}
	
}
