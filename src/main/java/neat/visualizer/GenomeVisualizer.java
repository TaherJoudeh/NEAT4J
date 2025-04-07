package main.java.neat.visualizer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import main.java.neat.core.Connection;
import main.java.neat.core.Genome;
import main.java.neat.core.Node;

/**
 * 
 * @author Taher Joudeh
 *
 */
public class GenomeVisualizer {

	/**
	 * 
	 * @author Taher Joudeh
	 *
	 */
	public static enum SHAPE {
		CIRCLE,
		SQUARE
	}
	
	protected double nodeSize;
	protected SHAPE nodeShape;
	
	protected String nodeBoundsColor;
	
	protected String nonActivatedNodeColor;
	protected String inputNodeColor;
	protected String hiddenNodeColor;
	protected String outputNodeColor;
	
	protected String posConnectionColor;
	protected String negConnectionColor;
	protected String posRecurrentConnectionColor;
	protected String negRecurrentConnectionColor;
	
	protected String disabledConnectionColor;
	
	/**
	 * Default constructor.
	 */
	protected GenomeVisualizer() {
		/*
		 * Nothing
		 */
	}
	
	/**
	 * 
	 * @param nodeSize
	 * @param nodeShape
	 * @param nodeBoundsColor
	 * @param nonActivatedNodeColor
	 * @param inputNodeColor
	 * @param hiddenNodeColor
	 * @param outputNodeColor
	 * @param posConnectionColor
	 * @param negConnectionColor
	 * @param posRecurrentConnectionColor
	 * @param negRecurrentConnectionColor
	 * @param disabledConnectionColor
	 */
	protected GenomeVisualizer(double nodeSize, SHAPE nodeShape, String nodeBoundsColor, String nonActivatedNodeColor,
			String inputNodeColor, String hiddenNodeColor, String outputNodeColor, String posConnectionColor,
			String negConnectionColor, String posRecurrentConnectionColor, String negRecurrentConnectionColor,
			String disabledConnectionColor) {
		this.nodeSize = nodeSize;
		this.nodeShape = nodeShape;
		this.nodeBoundsColor = nodeBoundsColor;
		this.nonActivatedNodeColor = nonActivatedNodeColor;
		this.inputNodeColor = inputNodeColor;
		this.hiddenNodeColor = hiddenNodeColor;
		this.outputNodeColor = outputNodeColor;
		this.posConnectionColor = posConnectionColor;
		this.negConnectionColor = negConnectionColor;
		this.posRecurrentConnectionColor = posRecurrentConnectionColor;
		this.negRecurrentConnectionColor = negRecurrentConnectionColor;
		this.disabledConnectionColor = disabledConnectionColor;
	}
	
	public double getNodeSize() { return nodeSize; }
	public SHAPE getNodeShape() { return nodeShape; }
	public String getNodeBoundsColor() { return nodeBoundsColor; }
	public String getNonActivatedNodeColor() { return nonActivatedNodeColor; }
	public String getInputNodeColor() { return inputNodeColor; }
	public String getHiddenNodeColor() { return hiddenNodeColor; }
	public String getOutputNodeColor() { return outputNodeColor; }
	public String getPosConnectionColor() { return posConnectionColor; }
	public String getNegConnectionColor() { return negConnectionColor; }
	public String getPosRecurrentConnectionColor() { return posRecurrentConnectionColor; }
	public String getNegRecurrentConnectionColor() { return negRecurrentConnectionColor; }
	public String getDisabledConnectionColor() { return disabledConnectionColor; }
	
	/**
	 * 
	 * @param visualizer
	 * @param backgroundColor
	 * @param drawDisabled
	 * @param activateAll
	 * @param genome
	 * @param maxWeightValue
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage visualizeGenome(GenomeVisualizer visualizer, String backgroundColor, boolean drawDisabled,
			boolean activateAll, Genome genome, double maxWeightValue, int width, int height) {
		
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (backgroundColor == null)
			g2d.setColor(new Color(1f,1f,1f,0f));
		else g2d.setColor(hexToColor(backgroundColor));
		
		g2d.fill(new Rectangle(0,0,width,height));
		
		HashMap<Node,double[]> nodeCoordinates = Genome.GenomeVisualizationData.getNodesCoordinates(genome, width,
				height, visualizer.getNodeSize());
		double nodeCenter = visualizer.getNodeSize()/2d;
		
		for (Connection connection: genome.getConnections()) {
			
			Node from = connection.getFrom();
			Node to = connection.getTo();
			
			double[] fromCoordinates = nodeCoordinates.get(from);
			double[] toCoordinates = nodeCoordinates.get(to);
			
			double x1 = fromCoordinates[0];
			double y1 = fromCoordinates[1];
			
			double x2 = toCoordinates[0];
			double y2 = toCoordinates[1];
			
			if (!connection.isEnabled()) {
				if (drawDisabled)
					g2d.setColor(hexToColor(visualizer.getDisabledConnectionColor()));
				else continue;
			} else if (connection.getWeight() >= 0) {
				
				if (connection.isRecurrent())
					g2d.setColor(hexToColor(visualizer.getPosRecurrentConnectionColor()));
				else g2d.setColor(hexToColor(visualizer.getPosConnectionColor()));
				
			}else if (connection.getWeight() < 0) {
				if (connection.isRecurrent())
					g2d.setColor(hexToColor(visualizer.getNegRecurrentConnectionColor()));
				else g2d.setColor(hexToColor(visualizer.getNegConnectionColor()));
			}
			
			
			float weight = (float) Math.abs(connection.getWeight());
			weight /= maxWeightValue;
			weight += 0.5f;
			
			g2d.setStroke(new BasicStroke(weight));
			g2d.draw(new Line2D.Double(x1+nodeCenter,y1+nodeCenter,x2+nodeCenter,y2+nodeCenter));
		}
		
		Shape shape = null;
		Shape bound = null;
		
		for (Node node: genome.getNodes()) {
						
			double[] coordinates = nodeCoordinates.get(node);
			double x = coordinates[0];
			double y = coordinates[1];
			
			switch (node.getType()) {
			case INPUT:
				g2d.setColor(hexToColor(visualizer.getInputNodeColor()));
				break;
			case OUTPUT:
				g2d.setColor(hexToColor(visualizer.getOutputNodeColor()));
				break;
			default:
				g2d.setColor(hexToColor(visualizer.getHiddenNodeColor()));
			}
			
			if (!node.isActivated() && !activateAll)
				g2d.setColor(hexToColor(visualizer.getNonActivatedNodeColor()));
			
			switch (visualizer.getNodeShape()) {
			case CIRCLE:
				shape = new Ellipse2D.Double(x,y,visualizer.getNodeSize(),visualizer.getNodeSize());
				bound = new Ellipse2D.Double(x-1,y-1,visualizer.getNodeSize()+1,visualizer.getNodeSize()+1);
				break;
			case SQUARE:
				shape = new Rectangle2D.Double(x,y,visualizer.getNodeSize(),visualizer.getNodeSize());
				bound = new Rectangle2D.Double(x-1,y-1,visualizer.getNodeSize()+1,visualizer.getNodeSize()+1);
				break;
			}
			
			String nodeBoundsColor = visualizer.getNodeBoundsColor();
			if (node.isSelfRecurrent() && node.getSelfRecurrentConnection().isEnabled()) {
				if (node.getSelfRecurrentConnection().getWeight() >= 0)
					nodeBoundsColor = visualizer.getPosRecurrentConnectionColor();
				else nodeBoundsColor = visualizer.getNegRecurrentConnectionColor();
			}
			
			g2d.fill(shape);
			g2d.setColor(hexToColor(nodeBoundsColor));
			g2d.draw(bound);
			
		}
		
		return image;
	}
	
	private static Color hexToColor(String hexColor) {
		
		String color = hexColor.replace("#", "");
	    int rgb = Integer.parseInt(color, 16);
	    return new Color(rgb);
		
	}
	
}
