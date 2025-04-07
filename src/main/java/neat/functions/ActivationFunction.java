package main.java.neat.functions;

import java.io.Serializable;

import main.java.neat.config.ActivationConfig;

public abstract class ActivationFunction implements Serializable {
	
	private static final long serialVersionUID = -6209934906347181073L;
	
	/**
	 * 
	 * @author Taher Joudeh
	 *
	 */
	public static enum ACTIVATION_FUNCTION {
		/**
		 * 
		 */
		SIGMOID,
		
		/**
		 * 
		 */
		TANH,
		
		/**
		 * 
		 */
		STEP,
		
		/**
		 * 
		 */
		RELU,
		
		/**
		 * 
		 */
		LINEAR,
		
		/**
		 * 
		 */
		RANDOM
	}
	
	private double threshold;
	private double activatedValue;
	
	public ActivationFunction(double threshold) {
		this.threshold = threshold;
	}
	
	public double getThreshold() { return threshold; }
	protected void setThreshold(double threshold) { this.threshold = threshold; }
	
	protected void setActivatedValue(double activatedValue) { this.activatedValue = activatedValue; }
	
	public boolean isActivated() { return activatedValue > threshold; }
	public abstract double activate(double x);
	public abstract ActivationFunction clone();
	
	/**
	 * 
	 * @param activationFunction
	 * @return
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
	 * 
	 * @param activationFunction
	 * @return
	 */
	public static ActivationFunction getRandomActivationFunction(ActivationConfig actConfig) {
		int numOfActFuncs = ACTIVATION_FUNCTION.values().length-1;
		return getActivationFunction(ACTIVATION_FUNCTION.values()[(int)(Math.random()*numOfActFuncs)],actConfig);
	}
			
}

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