package main.java.neat.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import main.java.neat.config.NEATConfig.DISTRIBUTION;
import main.java.neat.functions.ActivationFunction;
import main.java.neat.functions.AggregationFunction;

public class Node implements Serializable {

	private static final long serialVersionUID = -2812820096558333256L;

	public static enum TYPE {
		INPUT,
		HIDDEN,
		OUTPUT
	}
	
	private TYPE type;
	private int layer;
	private double bias;
	private double response;
	private AggregationFunction aggregationFunction;
	private ActivationFunction activationFunction;
	
	private int splitInnovationNumber;
	private double value;
	private LinkedList<Connection> inputConnections;
	private LinkedList<Connection> outputConnections;
	private Connection selfRecurrentConnection;
		
	protected Node() {
		inputConnections = new LinkedList<> ();
		outputConnections = new LinkedList<> ();
	}
	
	protected Node(TYPE type) {
		this();
		this.type = type;
	}
	
	public TYPE getType() { return type; }
	
	public int getLayer() { return layer; }
	protected void setLayer(int layer) { this.layer = layer; }
	
	protected int getSplitInnovationNumber() { return splitInnovationNumber; }
	protected void setSplitInnovationNumber(int splitInnovationNumber) { this.splitInnovationNumber = splitInnovationNumber; }
	
	public boolean isActivated() { return activationFunction.isActivated(); }
	
	public boolean isIsolated() {
		long inCons = inputConnections.stream().filter(c -> c.isEnabled()).count();
		long outCons = outputConnections.stream().filter(c -> c.isEnabled()).count();
		if (selfRecurrentConnection != null)
			return inCons == 1 || outCons == 1;
		return inCons == 0 || outCons == 0;
	}
	public boolean hasInputConnections() { return !inputConnections.isEmpty(); }
	public boolean hasOutputConnections() { return !outputConnections.isEmpty(); }
	
	public boolean isSelfRecurrent() { return selfRecurrentConnection != null; }
	protected void removeSelfRecurrentConnection() { selfRecurrentConnection = null; }
	protected Connection updateSelfRecurrentConnection() {
		Connection recCon = inputConnections.stream().filter(c -> c.getFrom() == this).findFirst().orElse(null);
		if (recCon != null)
			selfRecurrentConnection = recCon;
		else selfRecurrentConnection = null;
		return selfRecurrentConnection;
	}
	
	public Connection getSelfRecurrentConnection() { return selfRecurrentConnection; }
	
	public double getBias() { return bias; }
	protected void adjustBias(double biasMutationPower, double biasMaxValue, double biasMinValue) {
		bias += new Random().nextGaussian()*biasMutationPower;
		bias = Math.max(Math.min(biasMaxValue, bias), biasMinValue);
	}
	protected void randomizeBias(double biasMean, double biasStdev, DISTRIBUTION distribution, double biasMaxValue, double biasMinValue) {
		
		double newBias = 0;
		Random random = new Random();
		
		switch (distribution) {
		case NORMAL:
			newBias = random.nextGaussian() * biasStdev + biasMean;
			break;
		case UNIFORM:
			double range = biasStdev*Math.sqrt(12);
			newBias = random.nextDouble()*range + (biasMean - range/2d);
			break;
		}
		
		bias = Math.max(biasMinValue, Math.min(newBias, biasMaxValue));
		
	}
	
	public double getResponse() { return response; }
	protected void adjustResponse(double responseMutationPower, double responseMaxValue, double responseMinValue) {
		response += new Random().nextGaussian()*responseMutationPower;
		response = Math.max(Math.min(responseMaxValue, response), responseMinValue);
	}
	protected void randomizeResponse(double responseMean, double responseStdev, DISTRIBUTION distribution, double responseMaxValue, double responseMinValue) {
		
		double newResponse = 0;
		Random random = new Random();
		
		switch (distribution) {
		case NORMAL:
			newResponse = random.nextGaussian() * responseStdev + responseMean;
			break;
		case UNIFORM:
			double range = responseStdev*Math.sqrt(12);
			newResponse = random.nextDouble()*range + (responseMean - range/2d);
			break;
		}
		
		response = Math.max(responseMinValue, Math.min(newResponse, responseMaxValue));
		
	}
	
	protected void setAggregationFunction(AggregationFunction aggregationFunction) {
		this.aggregationFunction = aggregationFunction;
	}
	protected void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}
	
	public LinkedList<Connection> getInputConnections() { return new LinkedList<> (inputConnections); }
	public LinkedList<Connection> getOutputConnections() { return new LinkedList<> (outputConnections); }
	
	protected LinkedList<Connection> getInConnections() { return inputConnections; }
	protected LinkedList<Connection> getOutConnections(){ return outputConnections; }
	
	public double getValue() { return value; }
	protected void setValue(double value) { this.value = value; }
	
	protected void activate() {
		
		if (type == TYPE.INPUT) {
			value = activationFunction.activate(value);
			return;
		}
		
		double[] product = new double[inputConnections.size()];
		int count = 0;
		for (int i = 0; i < inputConnections.size(); i++)
			if (inputConnections.get(i).isEnabled())
				product[count++] = inputConnections.get(i).getFrom().value*inputConnections.get(i).getWeight();
		
		product = Arrays.copyOfRange(product, 0, count);
		
		double shiftedAggregation = response*aggregationFunction.aggregate(product) + bias;
		
		value = activationFunction.activate(shiftedAggregation);
		
	}
	
	protected Node clone() {
		Node clone = new Node(type);
		clone.layer = layer;
		clone.bias = bias;
		clone.response = response;
		clone.splitInnovationNumber = splitInnovationNumber;
		clone.aggregationFunction = aggregationFunction;
		clone.activationFunction = activationFunction.clone();
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node))
			return false;
		return splitInnovationNumber == ((Node)(obj)).splitInnovationNumber;
	}
	
	@Override
	public String toString() {
		return String.format("Type: %s, Layer: %d, Split innovation: %d",
				type,
				layer,
				splitInnovationNumber);
	}
	
}
