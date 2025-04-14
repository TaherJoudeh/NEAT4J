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
 * Renders neural network architectures as visual images. Handles visualization of
 * genomes including nodes, connections, colors, and shapes based on network properties.
 * Supports customizable appearance through color schemes and node shapes.
 * 
 * <p>Key features:
 * <ul>
 * <li>Different colors for input/hidden/output nodes</li>
 * <li>Visual distinction between positive/negative/recurrent connections</li>
 * <li>Configurable node shapes and sizes</li>
 * <li>Transparency support through ARGB images</li>
 * </ul>
 * 
 * @author Taher Joudeh
 */
public class GenomeVisualizer {

    /**
     * Enum representing node visualization shapes.
     */
	public static enum SHAPE {
		/**
		 * Circular node representation - nodes will be rendered as perfect circles.
		 */
		CIRCLE,
		
		/**
		 * Square node representation - nodes will be rendered as squares with equal width and height.
		 */
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
     * Protected default constructor for inheritance/configuration purposes.
     */
	protected GenomeVisualizer() {
		/*
		 * Nothing
		 */
	}
	
    /**
     * Creates a fully configured visualizer with custom appearance settings.
     * 
     * @param nodeSize               Diameter/width of nodes in pixels.
     * @param nodeShape              Geometric shape for nodes (CIRCLE/SQUARE).
     * @param nodeBoundsColor        Border color in hex format (#RRGGBB).
     * @param nonActivatedNodeColor  Color for inactive nodes.
     * @param inputNodeColor         Color for input layer nodes.
     * @param hiddenNodeColor        Color for hidden layer nodes.
     * @param outputNodeColor        Color for output layer nodes.
     * @param posConnectionColor     Color for positive-weight connections.
     * @param negConnectionColor     Color for negative-weight connections.
     * @param posRecurrentConnectionColor Color for positive recurrent connections.
     * @param negRecurrentConnectionColor Color for negative recurrent connections.
     * @param disabledConnectionColor Color for disabled connections.
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
	
    /**
     * @return Diameter/width of nodes in pixels for visualization.
     */
	public double getNodeSize() { return nodeSize; }
	
    /**
     * @return Currently configured node geometry shape.
     */
	public SHAPE getNodeShape() { return nodeShape; }
	
    /**
     * @return Hexadecimal color code for node borders.
     */
	public String getNodeBoundsColor() { return nodeBoundsColor; }
	
    /**
     * @return Hexadecimal color code for inactive nodes.
     */
	public String getNonActivatedNodeColor() { return nonActivatedNodeColor; }
	
    /**
     * @return Hexadecimal color code for input layer nodes.
     */
	public String getInputNodeColor() { return inputNodeColor; }
	
    /**
     * @return Hexadecimal color code for hidden layer nodes.
     */
	public String getHiddenNodeColor() { return hiddenNodeColor; }
	
    /**
     * @return Hexadecimal color code for output layer nodes.
     */
	public String getOutputNodeColor() { return outputNodeColor; }
	
    /**
     * @return Hexadecimal color code for positive-weight connections.
     */
	public String getPosConnectionColor() { return posConnectionColor; }
	
    /**
     * @return Hexadecimal color code for negative-weight connections.
     */
	public String getNegConnectionColor() { return negConnectionColor; }
	
    /**
     * @return Hexadecimal color code for positive recurrent connections.
     */
	public String getPosRecurrentConnectionColor() { return posRecurrentConnectionColor; }
	
    /**
     * @return Hexadecimal color code for negative recurrent connections.
     */
	public String getNegRecurrentConnectionColor() { return negRecurrentConnectionColor; }
	
    /**
     * @return Hexadecimal color code for disabled connections.
     */
	public String getDisabledConnectionColor() { return disabledConnectionColor; }
	
    /**
     * Generates visualization image of a genome's neural network.
     * 
     * @param visualizer         Configured visualizer instance.
     * @param backgroundColor    Background color in hex format (#RRGGBB) or null for transparent.
     * @param drawDisabled       Whether to render disabled connections.
     * @param activateAll        Force all nodes to appear activated.
     * @param genome             Genome to visualize.
     * @param maxWeightValue     Maximum weight value for connection thickness scaling.
     * @param width              Image width in pixels.
     * @param height             Image height in pixels.
     * @return BufferedImage containing network visualization.
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
