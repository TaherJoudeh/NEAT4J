package main.java.neat.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import main.java.neat.config.NEATConfig;

/**
 * Represents a connection between two nodes in a neural network genome.
 * Maintains connection properties including weight, innovation history,
 * and topological information for NEAT evolution.
 * 
 * <p>Key responsibilities:
 * <ul>
 * <li>Track innovation numbers for historical marking.
 * <li>Manage connection enable/disable state.
 * <li>Handle weight mutation and randomization.
 * <li>Maintain node connectivity information.
 * </ul>
 * 
 * @author Taher Joudeh
 */
public class Connection implements Serializable {

	private static final long serialVersionUID = 6206971791599788488L;
	protected final static int VOID_INTEGER_VALUE = Integer.MAX_VALUE;
	protected final static double VOID_DOUBLE_VALUE = Double.MAX_VALUE;
	
	private static LinkedList<Connection> innovationHistory = new LinkedList<> ();
	private static int globalInnovationNumber;
	
	private transient Random random = new Random();
	
	/**
	 * Connection weight determining signal strength/amplification.
	 */
	private double weight;
	
	/**
	 * Unique historical marker for tracking structural innovations (default: unassigned).
	 */
	private int innovationNumber = VOID_INTEGER_VALUE;
	
	/**
	 * Source node where this connection originates.
	 */
	private Node from;
	
	/**
	 * Destination node where this connection terminates.
	 */
	private Node to;
	
	/**
	 * Connection status flag (true = active, false = disabled).
	 */
	private boolean enabled;
	
	/**
	 * Mutation flag indicating if this connection can be split to add a new node.
	 */
	private boolean nodeAddable = true;
	
	/**
	 * Recurrency flag (true = connection creates a feedback loop).
	 */
	private boolean recurrent;
	
    /**
     * Creates a new connection between two nodes.
     * @param from Source node of the connection.
     * @param to Destination node of the connection.
     */
	protected Connection(Node from, Node to) {
		this.from = from;
		this.to = to;
	}
	
    /**
     * Establishes the connection between nodes by adding it to their
     * respective connection lists.
     */
	protected void connect() {
		from.getOutConnections().add(this);
		to.getInConnections().add(this);
	}
	
    /**
     * Removes the connection from both nodes' connection lists.
     */
	protected void disconnect() {
		from.getOutConnections().remove(this);
		to.getInConnections().remove(this);
	}
	
    /**
     * @return Current connection weight value.
     */
	public double getWeight() { return weight; }
	
    /**
     * Sets connection weight directly.
     * @param weight New weight value to assign.
     */
	protected void setWeight(double weight) { this.weight = weight; }
	
    /**
     * Adjusts weight with Gaussian mutation while maintaining value constraints.
     * @param weightMutationPower Standard deviation for weight changes.
     * @param weightMaxValue Maximum allowed weight value.
     * @param weightMinValue Minimum allowed weight value.
     */
	protected void adjustWeight(double weightMutationPower, double weightMaxValue, double weightMinValue) {
		weight += random.nextGaussian()*weightMutationPower;
		weight = Math.max(Math.min(weightMaxValue, weight), weightMinValue);
	}
	
    /**
     * Randomizes weight based on specified distribution.
     * @param weightMean Mean value for distribution.
     * @param weightStdev Standard deviation for distribution.
     * @param distribution Weight initialization distribution type.
     * @param weightMaxValue Maximum allowed weight value.
     * @param weightMinValue Minimum allowed weight value.
     */
	protected void randomizeWeight(double weightMean, double weightStdev, NEATConfig.DISTRIBUTION distribution, double weightMaxValue, double weightMinValue) {
		double newWeight = 0;
		
		switch (distribution) {
		case NORMAL:
			newWeight = random.nextGaussian() * weightStdev + weightMean;
			break;
		case UNIFORM:
			double range = weightStdev*Math.sqrt(12);
			newWeight = random.nextDouble()*range + (weightMean - range/2d);
			break;
		}
		
		weight = Math.max(weightMinValue, Math.min(newWeight, weightMaxValue));
	}
	
    /**
     * @return Source node of the connection.
     */
	public Node getFrom() { return from; }
	
    /**
     * @return Destination node of the connection.
     */
	public Node getTo() { return to; }
	
    /**
     * @return true if connection is active in the network.
     */
	public boolean isEnabled() { return enabled; }
	
    /**
     * Sets connection activation state.
     * @param enabled New enabled state.
     */
	protected void setEnabled(boolean enabled) { this.enabled = enabled; }
		
    /**
     * @return true if connection creates a recurrent loop.
     */
	public boolean isRecurrent() { return recurrent; }
	
    /**
     * Marks connection as recurrent.
     * @param recurrent New recurrent state.
     */
	protected void setRecurrent(boolean recurrent) { this.recurrent = recurrent; }
	
    /**
     * @return true if node addition is allowed on this connection.
     */
	public boolean isNodeAddable() { return nodeAddable; }
	
    /**
     * Controls whether new nodes can be inserted on this connection.
     * @param nodeAddable New node addition permission.
     */
	protected void setNodeAddable(boolean nodeAddable) { this.nodeAddable = nodeAddable; }
	
    /**
     * @return Unique innovation number for historical tracking.
     */
	public int getInnovationNumber() { return innovationNumber; }
	
    /**
     * Checks if another connection shares the same innovation history.
     * @param other Connection to compare with.
     * @return true if innovations numbers match.
     */
	protected boolean hasSameInnovationNumberAs(Connection other) { return innovationNumber == other.innovationNumber; }
	
    /**
     * Assigns a specific innovation number (for cross-network alignment).
     * @param innovationNumber Predefined innovation number.
     */
	protected void setInnovationNumber(int innovationNumber) { this.innovationNumber = innovationNumber; }
	
    /**
     * Automatically assigns innovation number from global registry,
     * reusing existing numbers for equivalent historical connections.
     */
	protected void setInnovationNumber() {
		int index = innovationHistory.indexOf(this);
		if (index == -1) {
			innovationNumber = globalInnovationNumber++;
			innovationHistory.add(clone());
		}else innovationNumber = innovationHistory.get(index).innovationNumber;
	}
	
    /**
     * Creates a deep copy of the connection with cloned nodes.
     * @return New Connection instance with identical properties.
     */
	@Override
	protected Connection clone() {
		Connection clone = new Connection(from.clone(), to.clone());
		clone.weight = weight;
		clone.enabled = enabled;
		clone.innovationNumber = innovationNumber;
		clone.nodeAddable = nodeAddable;
		clone.recurrent = recurrent;
		return clone;
	}
	
    /**
     * Clones connection while maintaining references to nodes in provided list.
     * @param nodes List of nodes from parent genome.
     * @return New connected clone with shared node references.
     */
	protected Connection cloneAndConnect(ArrayList<Node> nodes) {
		
		Connection c = clone();
		
		int indexFrom = nodes.indexOf(c.from);
		int indexTo = nodes.indexOf(c.to);
		
		if (indexFrom == -1)
			System.out.println(c);
		if (indexTo == -1)
			System.out.println(c);
		
		c.from = nodes.get(indexFrom);
		c.to = nodes.get(indexTo);
		c.connect();
		return c;
		
	}
	
    /**
     * Connection equality check using innovation numbers or node pairs.
     * @param obj Object to compare.
     * @return true if connections are equivalent.
     */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Connection))
			return false;
		Connection other = (Connection) obj;
		if (innovationNumber == VOID_INTEGER_VALUE || other.innovationNumber == VOID_INTEGER_VALUE)
			return from.equals(other.from) && to.equals(other.to);
		return innovationNumber == other.innovationNumber;
	}
	
    /**
     * @return String representation of connection properties.
     */
	@Override
	public String toString() {
		return String.format("Innovation number: %d, Recurrent: %b, From: %s, To: %s",
				innovationNumber,
				isRecurrent(),
				from,
				to);
	}
	
}
