package main.java.neat.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import main.java.neat.config.NEATConfig.DISTRIBUTION;
import main.java.neat.functions.ActivationFunction;
import main.java.neat.functions.AggregationFunction;

/**
 * Represents a node in a neural network genome within the NEAT algorithm.
 * Handles activation, aggregation, and connection management for neural network operations.
 * 
 * <p>Key responsibilities:
 * <ul>
 * <li>Maintain node type (input/hidden/output) and network layer position.
 * <li>Store and mutate bias/response values.
 * <li>Manage input/output connections.
 * <li>Perform neural activation calculations.
 * </ul>
 */
public class Node implements Serializable {

	private static final long serialVersionUID = -2812820096558333256L;

	private transient Random random = new Random();
	
    /**
     * Enum representing node types in the neural network.
     */
	public static enum TYPE {
		/**
		 * Input layer node - receives external data as input to the network.
		 */
		INPUT,
		
		/**
		 * Hidden layer node - processes signals between input and output layers.
		 */
		HIDDEN,
		
		/**
		 * Output layer node - produces the final results of the network.
		 */
		OUTPUT
	}
	
	/**
	 * Node type (input/hidden/output) determining its role in the network.
	 */
	private TYPE type;
	
	/**
	 * Layer position in the network (0 = input, hidden layers increment, Integer.MAX_VALUE = output).
	 */
	private int layer;
	
	/**
	 * Bias value added to aggregated inputs before activation.
	 */
	private double bias;

	/**
	 * Scaling factor modifying the activation function output.
	 */
	private double response;
	
	/**
	 * Function for combining input signals (e.g., sum, product).
	 */
	private AggregationFunction aggregationFunction;
	
	/**
	 * Activation function determining node output (e.g., sigmoid, ReLU).
	 */
	private ActivationFunction activationFunction;
	
	/**
	 * Innovation number tracking node creation through connection splitting.
	 */
	private int splitInnovationNumber;
	
	/**
	 * Current computed output value after activation.
	 */
	private double value;
	
	/**
	 * Incoming connections from predecessor nodes.
	 */
	private LinkedList<Connection> inputConnections;
	
	/**
	 * Outgoing connections to successor nodes.
	 */
	private LinkedList<Connection> outputConnections;
	
	/**
	 * Recurrent connection allowing self-loop feedback.
	 */
	private Connection selfRecurrentConnection;
		
    /**
     * Constructs a node with empty connection lists.
     */
	protected Node() {
		inputConnections = new LinkedList<> ();
		outputConnections = new LinkedList<> ();
	}
	
    /**
     * Constructs a node with specified type and empty connections.
     * @param type The node type (INPUT/HIDDEN/OUTPUT).
     */
	protected Node(TYPE type) {
		this();
		this.type = type;
	}
	
    /**
     * @return The node type (INPUT/HIDDEN/OUTPUT).
     */
	public TYPE getType() { return type; }
	
    /**
     * @return The layer position in the network.
     */
	public int getLayer() { return layer; }
	
    /**
     * Sets the node's layer position in the network topology.
     * @param layer Zero-based layer index.
     */
	protected void setLayer(int layer) { this.layer = layer; }
	
    /**
     * @return Innovation number from when this node was created via connection splitting.
     */
	protected int getSplitInnovationNumber() { return splitInnovationNumber; }
	
    /**
     * Sets the innovation number tracking node creation history.
     * @param splitInnovationNumber Unique identifier from speciation.
     */
	protected void setSplitInnovationNumber(int splitInnovationNumber) { this.splitInnovationNumber = splitInnovationNumber; }
	
    /**
     * @return true if the node's activation function is currently active.
     */
	public boolean isActivated() { return activationFunction.isActivated(); }
	
    /**
     * Checks if node has insufficient active connections.
     * @return true if node has no enabled input/output connections.
     */
	public boolean isIsolated() {
		long inCons = inputConnections.stream().filter(c -> c.isEnabled()).count();
		long outCons = outputConnections.stream().filter(c -> c.isEnabled()).count();
		if (selfRecurrentConnection != null)
			return inCons == 1 || outCons == 1;
		return inCons == 0 || outCons == 0;
	}
	
    /**
     * @return true if node has any input connections.
     */
	public boolean hasInputConnections() { return !inputConnections.isEmpty(); }
	
    /**
     * @return true if node has any output connections.
     */
	public boolean hasOutputConnections() { return !outputConnections.isEmpty(); }
	
    /**
     * @return true if node has a recurrent self-connection.
     */
	public boolean isSelfRecurrent() { return selfRecurrentConnection != null; }
	
    /**
     * Removes any existing self-recurrent connection.
     */
	protected void removeSelfRecurrentConnection() { selfRecurrentConnection = null; }
	
    /**
     * Updates self-recurrent connection reference from input connections.
     * @return The self-recurrent connection if found, null otherwise.
     */
	protected Connection updateSelfRecurrentConnection() {
		Connection recCon = inputConnections.stream().filter(c -> c.getFrom() == this).findFirst().orElse(null);
		if (recCon != null)
			selfRecurrentConnection = recCon;
		else selfRecurrentConnection = null;
		return selfRecurrentConnection;
	}
	
    /**
     * @return The node's self-recurrent connection (if exists).
     */
	public Connection getSelfRecurrentConnection() { return selfRecurrentConnection; }
	
    /**
     * @return Current bias value added to activation calculation.
     */
	public double getBias() { return bias; }
	
    /**
     * Adjusts bias with Gaussian mutation while respecting value constraints.
     * @param biasMutationPower Standard deviation for bias changes.
     * @param biasMaxValue Maximum allowed bias value.
     * @param biasMinValue Minimum allowed bias value.
     */
	protected void adjustBias(double biasMutationPower, double biasMaxValue, double biasMinValue) {
		bias += random.nextGaussian()*biasMutationPower;
		bias = Math.max(Math.min(biasMaxValue, bias), biasMinValue);
	}
	
    /**
     * Randomizes bias based on specified distribution.
     * @param biasMean Mean value for distribution.
     * @param biasStdev Standard deviation for distribution.
     * @param distribution Weight initialization distribution type.
     * @param biasMaxValue Maximum allowed bias value.
     * @param biasMinValue Minimum allowed bias value.
     */
	protected void randomizeBias(double biasMean, double biasStdev, DISTRIBUTION distribution, double biasMaxValue, double biasMinValue) {
		
		double newBias = 0;
		
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
	
    /**
     * @return Current response multiplier for activation calculation.
     */
	public double getResponse() { return response; }
	
    /**
     * Adjusts response with Gaussian mutation while respecting value constraints.
     * @param responseMutationPower Standard deviation for response changes.
     * @param responseMaxValue Maximum allowed response value.
     * @param responseMinValue Minimum allowed response value.
     */
	protected void adjustResponse(double responseMutationPower, double responseMaxValue, double responseMinValue) {
		response += random.nextGaussian()*responseMutationPower;
		response = Math.max(Math.min(responseMaxValue, response), responseMinValue);
	}
	
    /**
     * Randomizes response based on specified distribution.
     * @param responseMean Mean value for distribution.
     * @param responseStdev Standard deviation for distribution.
     * @param distribution Response initialization distribution type.
     * @param responseMaxValue Maximum allowed response value.
     * @param responseMinValue Minimum allowed response value.
     */
	protected void randomizeResponse(double responseMean, double responseStdev, DISTRIBUTION distribution, double responseMaxValue, double responseMinValue) {
		
		double newResponse = 0;
		
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
	
    /**
     * Sets the aggregation function for combining input signals.
     * @param aggregationFunction The aggregation strategy to use.
     */
	protected void setAggregationFunction(AggregationFunction aggregationFunction) {
		this.aggregationFunction = aggregationFunction;
	}
	
    /**
     * Sets the activation function for transforming aggregated input.
     * @param activationFunction The activation strategy to use.
     */
	protected void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}
	
    /**
     * @return Copy of input connections list.
     */
	public LinkedList<Connection> getInputConnections() { return new LinkedList<> (inputConnections); }
	
    /**
     * @return Copy of output connections list.
     */
	public LinkedList<Connection> getOutputConnections() { return new LinkedList<> (outputConnections); }
	
    /**
     * @return Direct reference to input connections (internal use).
     */
	protected LinkedList<Connection> getInConnections() { return inputConnections; }
	
    /**
     * @return Direct reference to output connections (internal use).
     */
	protected LinkedList<Connection> getOutConnections(){ return outputConnections; }
	
    /**
     * @return Current computed value of the node.
     */
	public double getValue() { return value; }
	
    /**
     * Sets raw node value (primarily for input nodes).
     * @param value New value to assign.
     */
	protected void setValue(double value) { this.value = value; }
	
    /**
     * <p>Performs neural activation calculation:
     * <ul>1. For input nodes: Directly applies activation to input value.</ul>
     * <ul>2. For other nodes:
     * <ul>
     *    <li>Aggregates weighted inputs.
     *    <li>Applies response scaling and bias.
     *    <li>Transforms through activation function.
     * </ul>
     */
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
	
    /**
     * <p>Creates a deep copy of the node including:
     * <ul>
     * <li>Duplicated activation/aggregation functions.
     * <li>Copied bias/response values.
     * <li>Matching layer position
     * </ul>
     * 
     * @return New Node instance with identical properties
     */
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
	
    /**
     * Node equality check based on split innovation number.
     * @param obj Object to compare.
     * @return true if nodes share the same split innovation history.
     */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node))
			return false;
		return splitInnovationNumber == ((Node)(obj)).splitInnovationNumber;
	}
	
    /**
     * @return String representation of node properties.
     */
	@Override
	public String toString() {
		return String.format("Type: %s, Layer: %d, Split innovation: %d",
				type,
				layer,
				splitInnovationNumber);
	}
	
}
