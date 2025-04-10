package main.java.neat.functions;

import java.io.Serializable;

import main.java.neat.config.ActivationConfig;

/**
 * Abstract base class for neural network activation functions.
 * 
 * This class defines the structure and behavior of activation functions used in the
 * NEAT implementation. Activation functions transform the aggregated input of a neural
 * network node into an output signal. Different activation functions provide different
 * non-linear transformations that are suitable for various problem domains.
 * 
 * The class also provides factory methods for creating specific activation function
 * instances based on type or at random.
 * 
 * @author Taher Joudeh
 */
public abstract class ActivationFunction implements Serializable {
	
	private static final long serialVersionUID = -6209934906347181073L;
	
    /**
     * Enumeration of supported activation function types.
     * Each type corresponds to a specific mathematical function that determines
     * node activation behavior.
     */
	public static enum ACTIVATION_FUNCTION {
		/**
		 * Sigmoid activation: f(x) = 1/(1+e^(-x))
		 * Smooth S-shaped curve that maps any input to (0,1) range.
		 * Historically common in neural networks and useful for classification.
		 */
		SIGMOID,
		
		/**
		 * Tanh activation: f(x) = tanh(x)
		 * Similar to sigmoid but maps to (-1,1) range instead.
		 * Better for hidden layers as it addresses vanishing gradient problem better than sigmoid.
		 */
		TANH,
		
		/**
		 * Step function: f(x) = x > 0 ? 1 : 0
		 * Binary output based on whether input is positive.
		 * Useful for binary classification problems.
		 */
		STEP,
		
		/**
		 * ReLU (Rectified Linear Unit): f(x) = max(0,x)
		 * Returns 0 for negative inputs and x for positive inputs.
		 * Helps with vanishing gradient problem and computational efficiency.
		 */
		RELU,
		
		/**
		 * Linear activation: f(x) = x
		 * Output is proportional to input with no transformation.
		 * Often used for input nodes or when unbounded output is desired.
		 */
		LINEAR,
		
        /**
         * Not an actual activation function but a placeholder.
         * When this type is used, a random activation function is selected.
         */
		RANDOM
	}
	
    /**
     * The threshold value that determines when a node is considered "activated".
     * When the output of the activation function exceeds this threshold,
     * isActivated() will return true.
     */
	private double threshold;
	
    /**
     * The last value computed by the activate() method.
     * This allows checking activation status without recomputation.
     */
	private double activatedValue;
	
    /**
     * Creates a new activation function with the specified threshold.
     * 
     * @param threshold The value above which the function is considered activated
     */
	public ActivationFunction(double threshold) {
		this.threshold = threshold;
	}
	
    /**
     * Gets the activation threshold for this function.
     * 
     * @return The current threshold value
     */
	public double getThreshold() { return threshold; }
	
    /**
     * Sets the activation threshold for this function.
     * 
     * @param threshold The new threshold value
     */
	protected void setThreshold(double threshold) { this.threshold = threshold; }
	
    /**
     * Sets the stored activated value after computation.
     * 
     * @param activatedValue The computed activation value to store
     */
	protected void setActivatedValue(double activatedValue) { this.activatedValue = activatedValue; }
	
    /**
     * Checks if the last computed activation value exceeds the threshold.
     * 
     * @return true if the node is activated, false otherwise
     */
	public boolean isActivated() { return activatedValue > threshold; }
	
    /**
     * Applies the activation function to the input value.
     * This method must be implemented by all concrete activation functions.
     * 
     * @param x The input value to be transformed
     * @return The transformed output value
     */
	public abstract double activate(double x);
	
    /**
     * Creates a deep copy of this activation function.
     * 
     * @return A new instance of the same activation function with the same parameters
     */
	public abstract ActivationFunction clone();
	
    /**
     * Factory method to create an activation function of the specified type.
     * 
     * This method uses the provided ActivationConfig to initialize the function
     * with appropriate threshold values.
     * 
     * @param activationFunction The type of activation function to create
     * @param actConfig Configuration object containing threshold parameters
     * @return A new activation function instance of the requested type
     */
	public final static ActivationFunction getActivationFunction(ACTIVATION_FUNCTION activationFunction, ActivationConfig actConfig) {
		
		ActivationFunction actFunc = null;
		
		switch (activationFunction) {
		case TANH:
			actFunc = new TanhActivationFunction(actConfig.getTanhActivationThreshold());
			break;
		case STEP:
			actFunc = new StepActivationFunction(actConfig.getStepActivationThreshold());
			actFunc.threshold = actConfig.getStepActivationThreshold();
			break;
		case RELU:
			actFunc = new ReluActivationFunction(actConfig.getReluActivationThreshold(), actConfig.getReluLeak());
			break;
		case LINEAR:
			actFunc = new LinearActivationFunction(actConfig.getLinearActivationThreshold());
			actFunc.threshold = actConfig.getLinearActivationThreshold();
			break;
		case SIGMOID:
			actFunc = new SigmoidActivationFunction(actConfig.getSigmoidActivationThreshold());
			actFunc.threshold = actConfig.getSigmoidActivationThreshold();
			break;
		case RANDOM:
			actFunc = getRandomActivationFunction(actConfig);
			break;
		}
		
		return actFunc;
	}
	
    /**
     * Creates a random activation function from the available types.
     * 
     * @param actConfig Configuration object containing threshold parameters
     * @return A randomly selected activation function instance
     */
	public static ActivationFunction getRandomActivationFunction(ActivationConfig actConfig) {
		int numOfActFuncs = ACTIVATION_FUNCTION.values().length-1;
		return getActivationFunction(ACTIVATION_FUNCTION.values()[(int)(Math.random()*numOfActFuncs)],actConfig);
	}
			
}

/**
 * Sigmoid activation function implementation.
 * 
 * The sigmoid function has the form f(x) = 1/(1+e^(-x)) and produces
 * outputs in the range (0,1). It has a smooth, S-shaped curve that
 * approaches but never reaches 0 or 1.
 */
class SigmoidActivationFunction extends ActivationFunction {

	private static final long serialVersionUID = 7594902945375388265L;

	public SigmoidActivationFunction(double threshold) {
		super(threshold);
	}
	
	@Override
	public double activate(double x) {
		double activatedValue = 1d/(1d+Math.exp(-x));
		setActivatedValue(activatedValue);
		return activatedValue;
	}
	
	@Override
	public ActivationFunction clone() {
		return new SigmoidActivationFunction(getThreshold());
	}
	
}

/**
 * Hyperbolic tangent (tanh) activation function implementation.
 * 
 * The tanh function has the form f(x) = (e^x - e^(-x))/(e^x + e^(-x))
 * and produces outputs in the range (-1,1). It is similar to the sigmoid
 * but is zero-centered, which can help with learning dynamics.
 */
class TanhActivationFunction extends ActivationFunction {

	public TanhActivationFunction(double threshold) {
		super(threshold);
	}

	private static final long serialVersionUID = 1389955211882518512L;

	@Override
	public double activate(double x) {
		double activatedValue = (Math.exp(x)-Math.exp(-x))/(Math.exp(x)+Math.exp(-x));
		setActivatedValue(activatedValue);
		return activatedValue;
	}
	
	@Override
	public ActivationFunction clone() {
		return new TanhActivationFunction(getThreshold());
	}
	
}

/**
 * Step activation function implementation.
 * 
 * The step function produces a binary output: 1 if the input is
 * non-negative, 0 otherwise. It creates a hard threshold which can
 * be useful for binary classification tasks.
 */
class StepActivationFunction extends ActivationFunction {

	public StepActivationFunction(double threshold) {
		super(threshold);
	}
	private static final long serialVersionUID = 1650866511669973514L;

	@Override
	public double activate(double x) {
		double activatedValue = x >= 0 ? 1 : 0;
		setActivatedValue(activatedValue);
		return activatedValue;
	}
	@Override
	public ActivationFunction clone() {
		return new StepActivationFunction(getThreshold());
	}
	
}

/**
 * Rectified Linear Unit (ReLU) activation function implementation.
 * 
 * The ReLU function returns the input value if it's positive, otherwise
 * returns a leaky value (input * leak). When leak is 0, it's standard ReLU.
 * When leak is small but positive (e.g., 0.01), it's Leaky ReLU.
 * ReLU helps address the vanishing gradient problem in deep networks.
 */
class ReluActivationFunction extends ActivationFunction {

	private static final long serialVersionUID = -6013329598788818087L;

	private double reluLeak;
	public ReluActivationFunction(double threshold, double reluLeak) {
		super(threshold);
		this.reluLeak = reluLeak;
	}
	
	@Override
	public double activate(double x) {
		double activatedValue = x >= 0 ? x : x*reluLeak;
		setActivatedValue(activatedValue);
		return activatedValue;
	}
	
	@Override
	public ActivationFunction clone() {
		return new ReluActivationFunction(getThreshold(), reluLeak);
	}
	
}

/**
 * Linear activation function implementation.
 * 
 * The linear function simply returns the input value unchanged.
 * It's often used for output nodes in regression tasks or for
 * input nodes to pass through the input value.
 */
class LinearActivationFunction extends ActivationFunction {

	private static final long serialVersionUID = 8450725363654557037L;
	
	public LinearActivationFunction(double threshold) {
		super(threshold);
	}

	@Override
	public double activate(double x) {
		double activatedValue = x;
		setActivatedValue(activatedValue);
		return activatedValue;
	}
	
	@Override
	public ActivationFunction clone() {
		return new LinearActivationFunction(getThreshold());
	}

}