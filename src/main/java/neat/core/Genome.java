package main.java.neat.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

import main.java.neat.config.NEATConfig;
import main.java.neat.core.Node.TYPE;
import main.java.neat.functions.ActivationFunction;
import main.java.neat.functions.ActivationFunction.ACTIVATION_FUNCTION;
import main.java.neat.functions.AggregationFunction;

/**
 * Represents a genome in the NEAT (NeuroEvolution of Augmenting Topologies) algorithm.
 * Contains neural network structure with nodes and connections, and implements genetic operations
 * including mutation, crossover, and compatibility distance calculation.
 * 
 * @author Taher Joudeh
 */
public class Genome implements Serializable {

	private static final long serialVersionUID = -1380168298131314999L;

	private transient Random random;
	
	/**
	 * Configuration parameters for evolution (mutation rates, compatibility thresholds, etc.).
	 */
	private NEATConfig neatConfig;
	
	/**
	 * All nodes in the genome (input, hidden, and output nodes).
	 */
	private ArrayList<Node> nodes;
	
	/**
	 * All connections between nodes in the genome.
	 */
	private ArrayList<Connection> connections;
	
	/**
	 * Subset of nodes designated as network inputs.
	 */
	private ArrayList<Node> inputNodes;
	
	/**
	 * Subset of nodes in hidden layers.
	 */
	private ArrayList<Node> hiddenNodes;
	
	/**
	 * Subset of nodes designated as network outputs.
	 */
	private ArrayList<Node> outputNodes;
	
	/**
	 * Nodes organized by layer index (0 = input, 1..n = hidden layers, last = output).
	 */
	private ArrayList<Node>[] nodesByLayer;
	
	/**
	 * Number of hidden layers in the network architecture.
	 */
	private int numOfHiddenlayers;
	
	/**
	 * Highest innovation number among all connections (tracks structural mutations).
	 */
	private int maxInnovationNumber;
	
	/**
	 * Visualization coordinates mapped to nodes (x,y positions for rendering).
	 */
	private HashMap<Node,double[]> nodesCoordinates;
	
	/**
	 * Horizontal dimension of the visualization area in pixels.
	 * Used to calculate node coordinates for rendering.
	 */
	private int width;
	
	/**
	 * Vertical dimension of the visualization area in pixels.
	 * Used to calculate node coordinates for rendering.
	 */
	private int height;
	
    /**
     * Display size of nodes during visualization (diameter in pixels).
     */
	private double nodeSize;
	
    /**
     * Mutation status flag - true if genome has been altered since last generation.
     */
	private boolean mutated;
	
    /**
     * Constructs a new Genome with specified configuration.
     * @param neatConfig NEAT algorithm configuration parameters.
     * @param init If true, initializes basic network structure (input/hidden/output nodes).
     */
	protected Genome(NEATConfig neatConfig, boolean init) {
		random = new Random();
		this.neatConfig = neatConfig;
		nodes = new ArrayList<> ();
		connections = new ArrayList<> ();
		
		inputNodes = new ArrayList<> ();
		hiddenNodes = new ArrayList<> ();
		outputNodes = new ArrayList<> ();
		
		nodesCoordinates = new HashMap<> ();
		width = 0;
		height = 0;
		nodeSize = 0;
		mutated = false;
		
		if (init)
			init();
	}
	
    /**
     * @return Number of connections in the genome.
     */
	public int getNumberOfConnections() { return connections.size(); }
	
    /**
     * @return Number of hidden nodes in the genome.
     */
	public int getNumberOfHiddenNodes() { return hiddenNodes.size(); }
	
    /**
     * @return Number of layers in the genome.
     */
	public int getNumberOfLayers() { return numOfHiddenlayers+2; }
	
    /**
     * @return List of input nodes (copied list).
     */
	public ArrayList<Node> getInputNodes() { return new ArrayList<Node> (inputNodes); }
	
	/**
     * @return List of hidden nodes (copied list).
     */
	public ArrayList<Node> getHiddenNodes() { return new ArrayList<Node> (hiddenNodes); }
	
	/**
     * @return List of output nodes (copied list).
     */
	public ArrayList<Node> getOutputNodes() { return new ArrayList<Node> (outputNodes); }
	
	/**
     * @return List of all nodes (copied list).
     */
	public ArrayList<Node> getNodes() { return new ArrayList<Node> (nodes); }
	
    /**
     * @return List of all connections (copied list).
     */
	public ArrayList<Connection> getConnections() { return new ArrayList<Connection> (connections); }
	
	/**
     * @return List of Arrays of all nodes. Each node in it's own layer (copied list).
     */
	protected ArrayList<Node>[] getNodesByLayers() { return nodesByLayer; }
	
    /**
     * Gets node coordinates for visualization purposes.
     * @param width Visualization area width.
     * @param height Visualization area height.
     * @param nodeSize Node display size.
     * @return Map of nodes to their [x,y] coordinates.
     */
	protected HashMap<Node,double[]> getNodesCoordinates(int width, int height, double nodeSize) {
		if (this.width != width || this.height != height || this.nodeSize != nodeSize || mutated) {
			this.width = width;
			this.height = height;
			this.nodeSize = nodeSize;
			mutated = false;
			updateNodesCoordinates();
		}
		return nodesCoordinates;
	}
	
	private void updateNodesCoordinates() {
		
		int layers = numOfHiddenlayers+2;
		int[] layerCounts = new int[layers];
		
		for (int i = 0; i < nodes.size(); i++) {
			
			Node node = nodes.get(i);
			int nodeLayer = 0;
			
			if (node.getType() == TYPE.OUTPUT)
				nodeLayer = layers-1;
			else nodeLayer = node.getLayer();
			
			int layerSize = nodesByLayer[nodeLayer].size();
			
			double x = nodeLayer/(double)(layers-1);
			double margin = height/(double)layerSize;
			double y = layerCounts[nodeLayer]++*margin;
			
			double margined = (layerSize-1)*margin;
			x *= width;
			y += height/2d - margined/2d;
			
			if (node.getType() == TYPE.OUTPUT)
				x -= (nodeSize+4);
			else if (node.getType() == TYPE.INPUT)
				x += 4;
			else x -= nodeSize/2d;
			
			if (nodesCoordinates.containsKey(node)) {
				nodesCoordinates.get(node)[0] = x;
				nodesCoordinates.get(node)[1] = y;
			}else nodesCoordinates.put(node, new double[] {x,y});
			
		}
		
	}
	
    /**
     * Feeds input through the neural network and returns continuous output values.
     * @param input Array of input values matching number of input nodes.
     * @return Array of continuous output values from output nodes.
     */
	protected double[] feed(double[] input) {
		
		double[] output = new double[neatConfig.getNumberOfOutputs()];
		
		for (int i = 0; i < neatConfig.getNumberOfInputs(); i++)
			inputNodes.get(i).setValue(input[i]);
		
		int count = 0;
		for (int i = 0; i < numOfHiddenlayers+2; i++) {
			for (int j = 0; j < nodesByLayer[i].size(); j++) {
				nodesByLayer[i].get(j).activate();
				if (nodesByLayer[i].get(j).getType() == TYPE.OUTPUT)
					output[count++] = nodesByLayer[i].get(j).getValue();
			}
		}
		
		return output;
	}
	
    /**
     * Feeds input through the neural network and returns binary-activated outputs.
     * @param input Array of input values matching number of input nodes.
     * @return Array of boolean activation states from output nodes.
     */
	protected boolean[] feed2(double[] input) {
		boolean[] output = new boolean[neatConfig.getNumberOfOutputs()];
		
		for (int i = 0; i < neatConfig.getNumberOfInputs(); i++)
			inputNodes.get(i).setValue(input[i]);
		
		int count = 0;
		for (int i = 0; i < numOfHiddenlayers+2; i++) {
			for (int j = 0; j < nodesByLayer[i].size(); j++) {
				nodesByLayer[i].get(j).activate();
				if (nodesByLayer[i].get(j).getType() == TYPE.OUTPUT)
					output[count++] = nodesByLayer[i].get(j).isActivated();
			}
		}
		
		return output;
	}
	
	private void init() {
		
		initNodes();
		setNodesByType();
		initConnecting();
		updateLayers();
		setMaxInnovationNumber();
		
	}
	
	private void initNodes() {
		int splitInnovation = -1;
		for (int i = 0; i < neatConfig.getNumberOfInputs(); i++) {
			Node node = new Node(TYPE.INPUT);
			node.setSplitInnovationNumber(splitInnovation--);
			node.setActivationFunction(ActivationFunction.getActivationFunction(ACTIVATION_FUNCTION.LINEAR,
					neatConfig.getActivationConfig()));
			nodes.add(node);
		}
		
		for (int i = 0; i < neatConfig.getStartingHiddenNodes().length; i++) {
			for (int j = 0; j < neatConfig.getStartingHiddenNodes()[i]; j++) {
				Node node = new Node(TYPE.HIDDEN);
				node.setSplitInnovationNumber(splitInnovation--);
				node.setLayer(i+1);
				
				node.setAggregationFunction(AggregationFunction.getAggregationFunction(
						neatConfig.getStartingAggregationFunction()
						));
				node.setActivationFunction(ActivationFunction.getActivationFunction(
						neatConfig.getStartingActivationFunctionForHiddenNodes(), neatConfig.getActivationConfig()
						));
				node.randomizeBias(neatConfig.getBiasInitMean(),neatConfig.getBiasInitStdev(),neatConfig.getBiasInitDistributionType(),
						neatConfig.getBiasMaxValue(),neatConfig.getBiasMinValue());
				node.randomizeResponse(neatConfig.getResponseInitMean(),neatConfig.getResponseInitStdev(),neatConfig.getResponseInitDistributionType(),
						neatConfig.getResponseMaxValue(),neatConfig.getResponseMinValue());
				
				nodes.add(node);
			}
		}
		
		for (int i = 0; i < neatConfig.getNumberOfOutputs(); i++) {
			Node node = new Node(TYPE.OUTPUT);
			node.setLayer(Integer.MAX_VALUE);
			node.setSplitInnovationNumber(splitInnovation--);
			
			node.setAggregationFunction(AggregationFunction.getAggregationFunction(
					neatConfig.getStartingAggregationFunction()
					));
			node.setActivationFunction(ActivationFunction.getActivationFunction(
					neatConfig.getStartingActivationFunctionForOutputNodes(), neatConfig.getActivationConfig()
					));
			node.randomizeBias(neatConfig.getBiasInitMean(),neatConfig.getBiasInitStdev(),neatConfig.getBiasInitDistributionType(),
					neatConfig.getBiasMaxValue(),neatConfig.getBiasMinValue());
			node.randomizeResponse(neatConfig.getResponseInitMean(),neatConfig.getResponseInitStdev(),neatConfig.getResponseInitDistributionType(),
					neatConfig.getResponseMaxValue(),neatConfig.getResponseMinValue());
			
			nodes.add(node);
		}
		
		numOfHiddenlayers = neatConfig.getStartingHiddenNodes().length;
		
	}
	private void initConnecting() {
		
		switch (neatConfig.getInitConnectivity()) {
		
		case FEATURE_SELECTION_NEAT_NO_HIDDEN:
			featureSelectionNeatNoHiddenInit();
			break;
		case FEATURE_SELECTION_NEAT_HIDDEN:
			featureSelectionNeatHiddenInit();
			break;
		case LAYER_BY_LAYER:
			layerByLayerInit();
			break;
		case FULL_NO_DIRECT:
			noDirectInit(1);
			break;
		case FULL_DIRECT:
			directInit(1);
			break;
		case PARTIAL_NO_DIRECT:
			noDirectInit(neatConfig.getProbConnectInit());
			break;
		case PARTIAL_DIRECT:
			directInit(neatConfig.getProbConnectInit());
			break;
		default:
			break;
		}
	}
	private void featureSelectionNeatNoHiddenInit() {
		
		Node randomInputFeature = inputNodes.get(random.nextInt(neatConfig.getNumberOfInputs()));
		
		for (int i = 0; i < neatConfig.getNumberOfOutputs(); i++)
			addConnection(randomInputFeature,outputNodes.get(i), Connection.VOID_DOUBLE_VALUE, false);
		
	}
	private void featureSelectionNeatHiddenInit() {
		
		Node randomInputFeature = inputNodes.get(random.nextInt(neatConfig.getNumberOfInputs()));
		
		for (int i = 0; i < hiddenNodes.size(); i++)
			addConnection(randomInputFeature, hiddenNodes.get(i), Connection.VOID_DOUBLE_VALUE, false);
		for (int i = 0; i < hiddenNodes.size(); i++)
			for (int j = 0; j < neatConfig.getNumberOfOutputs(); j++)
				addConnection(hiddenNodes.get(i), outputNodes.get(j), Connection.VOID_DOUBLE_VALUE, false);
		for (int i = 0; i < neatConfig.getNumberOfOutputs(); i++)
			addConnection(randomInputFeature,outputNodes.get(i), Connection.VOID_DOUBLE_VALUE, false);
		
	}
	private void layerByLayerInit() {
		
		setNodesByLayer();
		for (int i = 0; i < numOfHiddenlayers+1; i++)
			for (int j = 0; j < nodesByLayer[i].size(); j++)
				for (int k = 0; k < nodesByLayer[i+1].size(); k++)
					addConnection(nodesByLayer[i].get(j), nodesByLayer[i+1].get(k), Connection.VOID_DOUBLE_VALUE, false);
		
	}
	private void noDirectInit(double probConnectInit) {
		
		for (int i = 0; i < neatConfig.getNumberOfInputs(); i++) {
			for (int j = 0; j < hiddenNodes.size(); j++) {
				if (random.nextDouble() < probConnectInit)
					addConnection(inputNodes.get(i), hiddenNodes.get(j), Connection.VOID_DOUBLE_VALUE, false);
			}
		}
		
		for (int i = 0; i < hiddenNodes.size(); i++) {
			if (!neatConfig.isFeedForward() && random.nextDouble() < probConnectInit)
				addConnection(hiddenNodes.get(i),hiddenNodes.get(i), Connection.VOID_DOUBLE_VALUE, true);
			for (int j = 0; j < neatConfig.getNumberOfOutputs(); j++) {
				if (random.nextDouble() < probConnectInit)
					addConnection(hiddenNodes.get(i), outputNodes.get(j), Connection.VOID_DOUBLE_VALUE, false);
			}
		}
		
		if (hiddenNodes.size() == 0) {
			for (int i = 0; i < neatConfig.getNumberOfInputs(); i++) {
				for (int j = 0; j < outputNodes.size(); j++) {
					if (random.nextDouble() < probConnectInit)
						addConnection(inputNodes.get(i), outputNodes.get(j), Connection.VOID_DOUBLE_VALUE, false);
				}
			}
		}
		
		for (int i = 0; i < outputNodes.size(); i++)
			if (!neatConfig.isFeedForward() && random.nextDouble() < probConnectInit)
				addConnection(outputNodes.get(i), outputNodes.get(i), Connection.VOID_DOUBLE_VALUE, true);
		
	}
	private void directInit(double probConnectInit) {
		for (int i = 0; i < neatConfig.getNumberOfInputs(); i++) {
			for (int j = 0; j < hiddenNodes.size(); j++) {
				if (random.nextDouble() < probConnectInit)
					addConnection(inputNodes.get(i), hiddenNodes.get(j), Connection.VOID_DOUBLE_VALUE, false);
			}
		}
		
		for (int i = 0; i < hiddenNodes.size(); i++) {
			if (!neatConfig.isFeedForward() && random.nextDouble() < probConnectInit)
				addConnection(hiddenNodes.get(i),hiddenNodes.get(i), Connection.VOID_DOUBLE_VALUE, true);
			for (int j = 0; j < neatConfig.getNumberOfOutputs(); j++) {
				if (random.nextDouble() < probConnectInit)
					addConnection(hiddenNodes.get(i), outputNodes.get(j), Connection.VOID_DOUBLE_VALUE, false);
			}
		}
		
		for (int i = 0; i < neatConfig.getNumberOfInputs(); i++) {
			for (int j = 0; j < neatConfig.getNumberOfOutputs(); j++) {
				if (!neatConfig.isFeedForward() && i == 0 && random.nextDouble() < probConnectInit)
					addConnection(outputNodes.get(j), outputNodes.get(j), Connection.VOID_DOUBLE_VALUE, true);
				if (random.nextDouble() < probConnectInit)
					addConnection(inputNodes.get(i), outputNodes.get(j), Connection.VOID_DOUBLE_VALUE, false);
			}
		}
	}
	
	private void setNodesByLayer() {
		
		@SuppressWarnings("unchecked")
		ArrayList<Node>[] nodesByLayer = new ArrayList[numOfHiddenlayers+2];
		
		for (int i = 0; i < numOfHiddenlayers+2; i++)
			nodesByLayer[i] = new ArrayList<> ();
		
		for (Node node: nodes) {
			if (node.getType() == TYPE.OUTPUT)
				nodesByLayer[numOfHiddenlayers+1].add(node);
			else
				nodesByLayer[node.getLayer()].add(node);
		}
		
		this.nodesByLayer = nodesByLayer;
		
	}
	
	private void setNodesByType() {
		inputNodes.clear();
		hiddenNodes.clear();
		outputNodes.clear();
		
		for (Node node: nodes) {
			if (node.getType() == TYPE.INPUT)
				inputNodes.add(node);
			else if (node.getType() == TYPE.HIDDEN)
				hiddenNodes.add(node);
			else outputNodes.add(node);
		}
	}
	
	private void updateLayers() {
		
		Queue<Node> nodes = new LinkedList<> ();
		HashMap<Node,Integer> nodeLayer = new HashMap<> ();
		
		for (Node node: inputNodes) {
			for (Connection connection: node.getOutConnections()) {
				if (connection.getTo().getType() == TYPE.HIDDEN) {
					nodes.add(connection.getTo());
					nodeLayer.put(connection.getTo(), 1);
				}
			}
		}
		
		while (!nodes.isEmpty()) {
			
			Node node = nodes.poll();
			int currentLayer = nodeLayer.get(node);
			
			for (Connection connection: node.getOutConnections()) {
				Node toNode = connection.getTo();
				if (toNode.getType() != TYPE.HIDDEN || connection.isRecurrent())
					continue;
				if (nodeLayer.containsKey(toNode))
	                nodeLayer.put(toNode, Math.max(currentLayer+1, nodeLayer.get(toNode)));
	            else 
	                nodeLayer.put(toNode, currentLayer+1);
				nodes.add(toNode);
			}
			
		}
		
		int max = 0;
		for (Node node: hiddenNodes) {
			if (!nodeLayer.containsKey(node))
				continue;
			int layer = nodeLayer.get(node);
			node.setLayer(layer);
			if (layer > max)
				max  = layer;
		}
		
		handleRecurrentConnections();
		numOfHiddenlayers = max;
		setNodesByLayer();
		
	}
	
	private void handleIsolatedNodes() {
		LinkedList<Node> hNodes = new LinkedList<> (hiddenNodes);
		hNodes.removeIf(hn -> !hn.isIsolated());
		while (!hNodes.isEmpty()) {
			Node node = hNodes.remove();
			deleteNode(node);
			if (hNodes.isEmpty()) {
				hNodes.addAll(hiddenNodes);
				hNodes.removeIf(hn -> !hn.isIsolated());
			}
		}
		setMaxInnovationNumber();
	}
	
	private void handleRecurrentConnections() {
		if (neatConfig.isFeedForward())
			return;
		
		LinkedList<Connection> recCon = new LinkedList<> (connections);
		recCon.removeIf(c -> !c.isRecurrent());
		
		for (Connection recC: recCon) {
			if (recC.getFrom() == recC.getTo())
				continue;
			if (recC.getFrom().getLayer() == recC.getTo().getLayer())
				recC.setEnabled(false);
			else if (recC.getFrom().getLayer() < recC.getTo().getLayer())
				recC.setRecurrent(false);
		}
	}
	
	// Genome modifications
	private boolean mutateAddConnection() {

		if (random.nextDouble() >= neatConfig.getProbAddConnection())
			return false;
		
		int numOfLayers = numOfHiddenlayers+2;
		Node node1 = null, node2 = null;
		
		if (!neatConfig.isFeedForward() && random.nextDouble() < neatConfig.getProbRecurrentConnection()) {
			
			int randomLayer = random.nextInt(numOfLayers-1)+1;
			int secondRandomLayer = random.nextInt(randomLayer)+1;
			
			int sizeOfRandomLayer = nodesByLayer[randomLayer].size();
			int sizeOfSecondRandomLayer = nodesByLayer[secondRandomLayer].size();
			
			if (randomLayer == secondRandomLayer) {
				Node node = nodesByLayer[randomLayer].get(random.nextInt(sizeOfRandomLayer));
				node1 = node;
				node2 = node;
			} else {
				node1 = nodesByLayer[randomLayer].get(random.nextInt(sizeOfRandomLayer));
				node2 = nodesByLayer[secondRandomLayer].get(random.nextInt(sizeOfSecondRandomLayer));
			}
		}else {
			int randomLayer = random.nextInt(numOfLayers-1);
			int secondRandomLayer = random.nextInt(numOfLayers-(randomLayer+1))+(randomLayer+1);
			
			int sizeOfRandomLayer = nodesByLayer[randomLayer].size();
			int sizeOfSecondRandomLayer = nodesByLayer[secondRandomLayer].size();
			
			node1 = nodesByLayer[randomLayer].get(random.nextInt(sizeOfRandomLayer));
			node2 = nodesByLayer[secondRandomLayer].get(random.nextInt(sizeOfSecondRandomLayer));
		}
		
		Connection newConnection = new Connection(node1,node2);
		int index = connections.indexOf(newConnection);
		
		if (index != -1) {
			if (!connections.get(index).isEnabled() && neatConfig.hasStructuralMutationAdvisor()) {
					connections.get(index).setEnabled(true);
					return true;
			}else return false;
		}
			
		addConnection(node1, node2, Connection.VOID_DOUBLE_VALUE, node1.getLayer() >= node2.getLayer());
		setMaxInnovationNumber();
		mutated = true;
		
		return true;
		
	}
	private void addConnection(Node from, Node to, double weight, boolean recurrent) {
		
		Connection connection = new Connection(from,to);
		connection.setRecurrent(recurrent);
		connection.setInnovationNumber();
		connection.connect();
		
		connection.setEnabled(neatConfig.enabledDefault());

		if (weight == Connection.VOID_DOUBLE_VALUE)
			connection.randomizeWeight(neatConfig.getWeightInitMean(), neatConfig.getWeightInitStdev(), neatConfig.getWeightInitDistributionType(),
				neatConfig.getWeightMaxValue(), neatConfig.getWeightMinValue());
		else connection.setWeight(Math.max(Math.min(neatConfig.getWeightMaxValue(), weight), neatConfig.getWeightMinValue()));
		
		Node node = getNodeBySplitInnovationNumber(connection.getInnovationNumber());
		if (node != null)
			connection.setNodeAddable(false);
		
		if (from == to)
			from.updateSelfRecurrentConnection();
		
		connections.add(connection);
		
	}
	private boolean mutateAddNode() {
		
		if (random.nextDouble() >= neatConfig.getProbAddNode() || hiddenNodes.size() >= neatConfig.getMaxNumberOfHiddenNodes())
			return false;
		
		LinkedList<Connection> nodeAddableConnections = connections.stream().filter(c -> c.isNodeAddable() && c.isEnabled() && !c.isRecurrent())
				.collect(Collectors.toCollection(LinkedList::new));
		
		if (nodeAddableConnections.isEmpty()) {
			if (neatConfig.hasStructuralMutationAdvisor())
				return mutateAddConnection();
			return false;
		}
		
		addNode(nodeAddableConnections.get(random.nextInt(nodeAddableConnections.size())));
		updateLayers();
		setMaxInnovationNumber();
		mutated = true;
		return true;
		
	}
	private void addNode(Connection rc) {
		
		rc.setEnabled(false);
		rc.setNodeAddable(false);
		
		Node newNode = new Node(TYPE.HIDDEN);
		newNode.setSplitInnovationNumber(rc.getInnovationNumber());
		newNode.setAggregationFunction(AggregationFunction.getAggregationFunction(
				neatConfig.getStartingAggregationFunction()
				));
		newNode.setActivationFunction(ActivationFunction.getActivationFunction(
				neatConfig.getStartingActivationFunctionForHiddenNodes(), neatConfig.getActivationConfig()
				));
		newNode.randomizeBias(neatConfig.getBiasInitMean(),neatConfig.getBiasInitStdev(),neatConfig.getBiasInitDistributionType(),
				neatConfig.getBiasMaxValue(),neatConfig.getBiasMinValue());
		newNode.randomizeResponse(neatConfig.getResponseInitMean(),neatConfig.getResponseInitStdev(),neatConfig.getResponseInitDistributionType(),
				neatConfig.getResponseMaxValue(),neatConfig.getResponseMinValue());
				
		addConnection(rc.getFrom(), newNode, rc.getWeight(), false);
		addConnection(newNode, rc.getTo(), 1, false);
		nodes.add(newNode);
		hiddenNodes.add(newNode);
		
	}
	private boolean mutateDeleteConnection() {
		
		if (random.nextDouble() >= neatConfig.getProbDeleteConnection() || connections.isEmpty())
			return false;
		
		Connection rc = connections.get(random.nextInt(connections.size()));
		deleteConnection(rc);
		handleIsolatedNodes();
		updateLayers();
		mutated = true;
		return true;
		
	}
	private void deleteConnection(Connection connection) {
		connection.disconnect();
		if (connection.getFrom() == connection.getTo())
			connection.getFrom().removeSelfRecurrentConnection();
		connections.remove(connection);
	}
	private boolean mutateDeleteNode() {
		
		if (random.nextDouble() >= neatConfig.getProbDeleteNode() || hiddenNodes.isEmpty())
			return false;
				
		deleteNode(hiddenNodes.get(random.nextInt(hiddenNodes.size())));
		handleIsolatedNodes();
		updateLayers();
		setMaxInnovationNumber();
		mutated = true;
				
		return true;
	}
	private void deleteNode(Node node) {
		
		while (node.hasInputConnections())
			deleteConnection(node.getInConnections().get(0));
		while (node.hasOutputConnections())
			deleteConnection(node.getOutConnections().get(0));
		
		nodes.remove(node);
		hiddenNodes.remove(node);
		Connection c = getConnectionByInnovationNumber(node.getSplitInnovationNumber());
		if (c != null)
			c.setNodeAddable(true);
	}
	private void mutateStructure() {
						
		Stack<Integer> premutations = new Stack<> ();
		premutations.push(1);
		premutations.push(2);
		premutations.push(3);
		premutations.push(4);
		Collections.shuffle(premutations);
		
		while (!premutations.isEmpty()) {
			int roll = premutations.pop();
			boolean hasMutated = false;
			switch (roll) {
			case 1:
				hasMutated = mutateAddConnection();
				break;
			case 2:
				hasMutated = mutateAddNode();
				break;
			case 3:
				hasMutated = mutateDeleteConnection();
				break;
			case 4:
				hasMutated = mutateDeleteNode();
				break;
			}
			
			if (neatConfig.isSingleStructuralMutation() && hasMutated)
				break;
			
		}
				
	}
	private void mutateParameters() {
		
		for (Node node: nodes) {
			
			double roll = random.nextDouble();
			if (roll < neatConfig.getResponseAdjustingRate())
				node.adjustResponse(neatConfig.getResponseMutationPower(), neatConfig.getResponseMaxValue(), neatConfig.getResponseMinValue());
			else if (roll < neatConfig.getResponseAdjustingRate() + neatConfig.getResponseRandomizingRate())
				node.randomizeResponse(neatConfig.getResponseInitMean(), neatConfig.getResponseInitStdev(), neatConfig.getResponseInitDistributionType(),
						neatConfig.getResponseMaxValue(), neatConfig.getResponseMinValue());
			
			roll = random.nextDouble();
			if (roll < neatConfig.getBiasAdjustingRate())
				node.adjustBias(neatConfig.getBiasMutationPower(), neatConfig.getBiasMaxValue(), neatConfig.getBiasMinValue());
			else if (roll < neatConfig.getBiasAdjustingRate() + neatConfig.getBiasRandomizingRate())
				node.randomizeBias(neatConfig.getBiasInitMean(), neatConfig.getBiasInitStdev(), neatConfig.getBiasInitDistributionType(),
						neatConfig.getBiasMaxValue(), neatConfig.getBiasMinValue()); 
			
			if (node.getType() == TYPE.HIDDEN && random.nextDouble() < neatConfig.getAggregationMutationRate()) {
				int size = neatConfig.getAggregationConfig().getAllowedAggregationFunctions().size();
				int randomIndex = random.nextInt(size);
				node.setAggregationFunction(AggregationFunction.getAggregationFunction(
						neatConfig.getAggregationConfig().getAllowedAggregationFunctions().get(randomIndex)
						));
			}
			if (node.getType() != TYPE.INPUT && random.nextDouble() < neatConfig.getActivationMutationRate()) {
				int size = neatConfig.getActivationConfig().getAllowedActivationFunctions().size();
				int randomIndex = random.nextInt(size);
				node.setActivationFunction(ActivationFunction.getActivationFunction(
						neatConfig.getActivationConfig().getAllowedActivationFunctions().get(randomIndex), neatConfig.getActivationConfig()
						));
			}
			
			if (node.hasInputConnections()) {
				for (Connection connection: node.getInConnections()) {
					roll = random.nextDouble();
					if (roll < neatConfig.getWeightAdjustingRate())
						connection.adjustWeight(neatConfig.getWeightMutationPower(), neatConfig.getWeightMaxValue(), neatConfig.getWeightMinValue());
					else if (roll < neatConfig.getWeightAdjustingRate() + neatConfig.getWeightRandomizingRate())
						connection.randomizeWeight(neatConfig.getWeightInitMean(), neatConfig.getWeightInitStdev(), neatConfig.getWeightInitDistributionType(),
								neatConfig.getWeightMaxValue(), neatConfig.getWeightMinValue());
					
					double totalProbForEnable = neatConfig.getEnabledMutationRate();
					if (connection.isEnabled())
						totalProbForEnable += neatConfig.getEnabledRateForEnabled();
					else totalProbForEnable += neatConfig.getEnabledRateForDisabled();
					
					totalProbForEnable = Math.max(0, Math.min(totalProbForEnable, 1));
					
					if (random.nextDouble() < totalProbForEnable)
						connection.setEnabled(!connection.isEnabled());
				}
			}
		}
		
	}
	
    /**
     * Applies both structural and parameter mutations to the genome
     * according to probabilities defined in NEATConfig.
     */
	protected void mutate() {
		
		mutateStructure();
		mutateParameters();
		
	}
	
    /**
     * Calculates compatibility distance between two genomes for speciation.
     * @param g1 First genome to compare.
     * @param g2 Second genome to compare.
     * @return Compatibility distance measure based on excess/disjoint genes and weight differences.
     */
	protected static double distance(Genome g1, Genome g2) {
		
		if (g1.connections.isEmpty() || g2.connections.isEmpty())
			return Math.abs((g1.connections.size()-g2.connections.size())/2d);
		
		int excess = getNumberOfExcessGenes(g1, g2);
		int disjoint = getNumberOfDisjointGenes(g1, g2);
		double weightDiff = getWeightAbsoluteAverageDifference(g1, g2);
		
		double N = Math.max(g1.connections.size(), g2.connections.size());
		
		N = (N < 20) ? 1 : N;
		
		double distance = (g1.neatConfig.getCompatibilityExcessCoefficient()*(double)excess)/N
				+ (g1.neatConfig.getCompatibilityDisjointCoefficient()*(double)disjoint)/N
				+ (g1.neatConfig.getCompatibilityWeightCoefficient()*(double)weightDiff);
		
		return distance;
				
	}
	private static int getNumberOfExcessGenes(Genome g1, Genome g2) {
		
		int numberOfExcessGenes = 0;
		for (Connection connection: g1.connections) {
			if (connection.getInnovationNumber() > g2.maxInnovationNumber)
				numberOfExcessGenes++;
		}
		
		for (Connection connection: g2.connections) {
			if (connection.getInnovationNumber() > g1.maxInnovationNumber)
				numberOfExcessGenes++;
		}
		
		return numberOfExcessGenes;
		
	}
	private static int getNumberOfDisjointGenes(Genome g1, Genome g2) {
		
		int numberOfDisjointGenes = 0;
		for (Connection connection: g1.connections) {
			if (g2.connections.indexOf(connection) == -1 && connection.getInnovationNumber() <= g2.maxInnovationNumber)
				numberOfDisjointGenes++;
		}
		
		for (Connection connection: g2.connections) {
			if (g1.connections.indexOf(connection) == -1 && connection.getInnovationNumber() <= g1.maxInnovationNumber)
				numberOfDisjointGenes++;
		}
		
		return numberOfDisjointGenes;
		
	}
	private static double getWeightAbsoluteAverageDifference(Genome g1, Genome g2) {
		
		int numberOfSimilarGenes = 0;
		double sumOfAbsWeightDiff = 0;
		
		for (Connection connection: g1.connections) {
			int indexInOther = g2.connections.indexOf(connection);
			if (indexInOther != -1) {
				numberOfSimilarGenes++;
				sumOfAbsWeightDiff += Math.abs(connection.getWeight() - g2.connections.get(indexInOther).getWeight());
			}
		}
		
		if (numberOfSimilarGenes == 0)
			return 5;
		return sumOfAbsWeightDiff/(double)numberOfSimilarGenes;
		
	}
	private void setMaxInnovationNumber() {
		int max = Integer.MIN_VALUE;
		for (Connection connection: connections)
			if (connection.getInnovationNumber() > max)
				max = connection.getInnovationNumber();
		
		maxInnovationNumber = max;
	}
	private Connection getConnectionByInnovationNumber(int innovationNumber) {
		
		for (Connection connection: connections) {
			if (connection.getInnovationNumber() == innovationNumber)
				return connection;
		}
		
		return null;
		
	}
	private Node getNodeBySplitInnovationNumber(int innovationNumber) {
		for (Node node: nodes)
			if (node.getSplitInnovationNumber() == innovationNumber)
				return node;
		
		return null;
	}
	
    /**
     * Performs crossover operation between two genomes to produce offspring.
     * @param g1 First parent genome (typically more fit).
     * @param g2 Second parent genome.
     * @param sameFitness If true, considers both parents equally fit for gene selection.
     * @return New child genome combining characteristics of both parents.
     */
	protected static Genome crossover(Genome g1, Genome g2, boolean sameFitness) {
		
		Genome child = new Genome(g1.neatConfig, false);
		
		for (Node node: g1.nodes)
			child.nodes.add(node.clone());
		
		for (Node node: g2.hiddenNodes)
			if (!child.nodes.contains(node))
				child.nodes.add(node.clone());
						
		for (Connection connection: g1.connections)
			child.connections.add(connection.cloneAndConnect(child.nodes));
		
		for (Connection connection: g2.connections) {
			int indexInChildConnections = child.connections.indexOf(connection);
			
			if (indexInChildConnections != -1) {
				if (child.random.nextDouble() < 0.5)
					child.connections.get(indexInChildConnections).setWeight(connection.getWeight());
				if (!child.connections.get(indexInChildConnections).isEnabled() || !connection.isEnabled()) {
					if (child.random.nextDouble() < 0.75)
						child.connections.get(indexInChildConnections).setEnabled(false);
					else child.connections.get(indexInChildConnections).setEnabled(true);
				}
			}
			else if (sameFitness) {
				Node from = child.getNodeBySplitInnovationNumber(connection.getFrom().getSplitInnovationNumber());
				Node to = child.getNodeBySplitInnovationNumber(connection.getTo().getSplitInnovationNumber());
				
				if (!child.neatConfig.isFeedForward() || 
				        from.getLayer() < to.getLayer() || 
				        to.getType() == TYPE.OUTPUT)
					child.connections.add(connection.cloneAndConnect(child.nodes));
			}
				
		}
		
		for (Node node: child.nodes) {
			Connection connection = child.getConnectionByInnovationNumber(node.getSplitInnovationNumber());
			if (connection != null)
				connection.setNodeAddable(false);
		}
		
		for (Node node: child.nodes)
			node.updateSelfRecurrentConnection();

		child.setNodesByType();
		child.handleIsolatedNodes();
		child.updateLayers();
				
		return child;
	}
	
    /**
     * Creates a deep copy of the genome.
     * @return New Genome instance with identical structure and parameters.
     */
	@Override
	protected Genome clone() {
		Genome clone = new Genome(neatConfig, false);
						
		for (Node node: nodes)
			clone.nodes.add(node.clone());
		
		for (Connection connection: connections)
			clone.connections.add(connection.cloneAndConnect(clone.nodes));
		
		for (Node node: clone.nodes)
			node.updateSelfRecurrentConnection();
		
		clone.maxInnovationNumber = maxInnovationNumber;
		clone.numOfHiddenlayers = numOfHiddenlayers;
		clone.setNodesByType();
		clone.setNodesByLayer();
		
		return clone;
	}
	
	@Override
	public String toString() {
		String res = String.format("%d Connections, %d Nodes, %d layers.\n",
				connections.size(),
				nodes.size(),
				numOfHiddenlayers+2);
		for (Connection connection: connections)
			res += connection + "\n";
		res += "||\n";
		for (Node node: nodes)
			res += node + "\n";
		
		return res;
	}
	
    /**
     * Helper class for genome visualization data handling.
     */
	public static class GenomeVisualizationData {
		
        /**
         * Retrieves node coordinates for visualization.
         * @param genome Genome to visualize.
         * @param width Visualization area width.
         * @param height Visualization area height.
         * @param nodeSize Node display size.
         * @return Map of nodes to their calculated coordinates.
         */
		public static HashMap<Node, double[]> getNodesCoordinates(Genome genome, int width, int height, double nodeSize) {
            return genome.getNodesCoordinates(width, height, nodeSize);
        }
		
	}
	
}
