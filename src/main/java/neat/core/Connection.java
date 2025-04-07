package main.java.neat.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import main.java.neat.config.NEATConfig;

public class Connection implements Serializable {

	private static final long serialVersionUID = 6206971791599788488L;
	public final static int VOID_INTEGER_VALUE = Integer.MAX_VALUE;
	public final static double VOID_DOUBLE_VALUE = Double.MAX_VALUE;
	
	private static LinkedList<Connection> innovationHistory = new LinkedList<> ();
	private static int globalInnovationNumber;
	
	private double weight;
	private int innovationNumber = VOID_INTEGER_VALUE;
	private Node from;
	private Node to;
	private boolean enabled;
	private boolean nodeAddable = true;
	private boolean recurrent;
	
	protected Connection(Node from, Node to) {
		this.from = from;
		this.to = to;
	}
	
	protected void connect() {
		from.getOutConnections().add(this);
		to.getInConnections().add(this);
	}
	protected void disconnect() {
		from.getOutConnections().remove(this);
		to.getInConnections().remove(this);
	}
	
	public double getWeight() { return weight; }
	protected void setWeight(double weight) { this.weight = weight; }
	protected void adjustWeight(double weightMutationPower, double weightMaxValue, double weightMinValue) {
		weight += new Random().nextGaussian()*weightMutationPower;
		weight = Math.max(Math.min(weightMaxValue, weight), weightMinValue);
	}
	protected void randomizeWeight(double weightMean, double weightStdev, NEATConfig.DISTRIBUTION distribution, double weightMaxValue, double weightMinValue) {
		double newWeight = 0;
		Random random = new Random();
		
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
	
	public Node getFrom() { return from; }
	public Node getTo() { return to; }
	
	public boolean isEnabled() { return enabled; }
	protected void setEnabled(boolean enabled) { this.enabled = enabled; }
		
	public boolean isRecurrent() { return recurrent; }
	protected void setRecurrent(boolean recurrent) { this.recurrent = recurrent; }
	
	public boolean isNodeAddable() { return nodeAddable; }
	protected void setNodeAddable(boolean nodeAddable) { this.nodeAddable = nodeAddable; }
	
	public int getInnovationNumber() { return innovationNumber; }
	protected boolean hasSameInnovationNumberAs(Connection other) { return innovationNumber == other.innovationNumber; }
	protected void setInnovationNumber(int innovationNumber) { this.innovationNumber = innovationNumber; }
	protected void setInnovationNumber() {
		int index = innovationHistory.indexOf(this);
		if (index == -1) {
			innovationNumber = globalInnovationNumber++;
			innovationHistory.add(clone());
		}else innovationNumber = innovationHistory.get(index).innovationNumber;
	}
	
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
	
	protected Connection cloneAndConnect(LinkedList<Node> nodes) {
		
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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Connection))
			return false;
		Connection other = (Connection) obj;
		if (innovationNumber == VOID_INTEGER_VALUE || other.innovationNumber == VOID_INTEGER_VALUE)
			return from.equals(other.from) && to.equals(other.to);
		return innovationNumber == other.innovationNumber;
	}
	
	@Override
	public String toString() {
		return String.format("Innovation number: %d, Recurrent: %b, From: %s, To: %s",
				innovationNumber,
				isRecurrent(),
				from,
				to);
	}
	
}
