package main.java.neat.visualizer;

import main.java.neat.visualizer.GenomeVisualizer.SHAPE;

/**
 * Builder class for constructing configured GenomeVisualizer instances.
 * Implements the fluent builder pattern to simplify visualization configuration.
 * 
 * <p>Example usage:
 * <pre>
 * GenomeVisualizer visualizer = new GenomeVisualizerBuilder()
 *     .setNodeSize(25)
 *     .setNodeShape(SHAPE.SQUARE)
 *     .build();
 * </pre>
 * 
 * @author Taher Joudeh
 */
public class GenomeVisualizerBuilder {

	private GenomeVisualizer genomeVisualizer;
	
    /**
     * Constructs a new builder with default empty configuration.
     */
	public GenomeVisualizerBuilder() {
		genomeVisualizer = new GenomeVisualizer();
	}
	
    /**
     * Sets the diameter/width of nodes in pixels.
     * @param nodeSize Positive value for node dimensions.
     * @return This builder instance for chaining.
     */
	public GenomeVisualizerBuilder setNodeSize(double nodeSize) {
		genomeVisualizer.nodeSize = nodeSize;
		return this;
	}
	
    /**
     * Sets the geometric shape for nodes.
     * @param nodeShape CIRCLE or SQUARE from SHAPE enum.
     * @return This builder instance for chaining.
     */
	public GenomeVisualizerBuilder setNodeShape(SHAPE nodeShape) {
		genomeVisualizer.nodeShape = nodeShape;
		return this;
	}
	
    /**
     * Sets border color for nodes.
     * @param nodeBoundsColor Hexadecimal color code (#RRGGBB).
     * @return This builder instance for chaining.
     */
	public GenomeVisualizerBuilder setNodeBoundsColor(String nodeBoundsColor) {
		genomeVisualizer.nodeBoundsColor = nodeBoundsColor;
		return this;
	}
	
    /**
     * Sets color for inactive nodes.
     * @param nonActivatedNodeColor Hexadecimal color code (#RRGGBB).
     * @return This builder instance for chaining.
     */
	public GenomeVisualizerBuilder setNonActivatedNodeColor(String nonActivatedNodeColor) {
		genomeVisualizer.nonActivatedNodeColor = nonActivatedNodeColor;
		return this;
	}
	
    /**
     * Defines the color for input layer nodes.
     * @param inputNodeColor Hexadecimal color code (#RRGGBB format).
     * @return This builder instance for method chaining.
     */
	public GenomeVisualizerBuilder setInputNodeColor(String inputNodeColor) {
		genomeVisualizer.inputNodeColor = inputNodeColor;
		return this;
	}
	
    /**
     * Specifies the color for hidden layer nodes.
     * @param hiddenNodeColor Hexadecimal color code (#RRGGBB format).
     * @return This builder instance for method chaining.
     */
	public GenomeVisualizerBuilder setHiddenNodeColor(String hiddenNodeColor) {
		genomeVisualizer.hiddenNodeColor = hiddenNodeColor;
		return this;
	}
	
    /**
     * Sets the color for output layer nodes.
     * @param outputNodeColor Hexadecimal color code (#RRGGBB format).
     * @return This builder instance for method chaining.
     */
	public GenomeVisualizerBuilder setOutputNodeColor(String outputNodeColor) {
		genomeVisualizer.outputNodeColor = outputNodeColor;
		return this;
	}
	
    /**
     * Configures the color for positive-weight connections.
     * @param posConnectionColor Hexadecimal color code (#RRGGBB format).
     * @return This builder instance for method chaining.
     */
	public GenomeVisualizerBuilder setPosConnectionColor(String posConnectionColor) {
		genomeVisualizer.posConnectionColor = posConnectionColor;
		return this;
	}
	
    /**
     * Configures the color for negative-weight connections.
     * @param negConnectionColor Hexadecimal color code (#RRGGBB format).
     * @return This builder instance for method chaining.
     */
	public GenomeVisualizerBuilder setNegConnectionColor(String negConnectionColor) {
		genomeVisualizer.negConnectionColor = negConnectionColor;
		return this;
	}
	
    /**
     * Sets the color for positive-weight recurrent connections.
     * @param posRecurrentConnectionColor Hexadecimal color code (#RRGGBB format).
     * @return This builder instance for method chaining.
     */
	public GenomeVisualizerBuilder setPosRecurrentConnectionColor(String posRecurrentConnectionColor) {
		genomeVisualizer.posRecurrentConnectionColor = posRecurrentConnectionColor;
		return this;
	}
	
    /**
     * Sets the color for negative-weight recurrent connections.
     * @param negRecurrentConnectionColor Hexadecimal color code (#RRGGBB format).
     * @return This builder instance for method chaining.
     */
	public GenomeVisualizerBuilder setNegRecurrentConnectionColor(String negRecurrentConnectionColor) {
		genomeVisualizer.negRecurrentConnectionColor = negRecurrentConnectionColor;
		return this;
	}
	
    /**
     * Defines the color for disabled connections.
     * @param disabledConnectionColor Hexadecimal color code (#RRGGBB format).
     * @return This builder instance for method chaining.
     */
	public GenomeVisualizerBuilder setDisabledConnectionColor(String disabledConnectionColor) {
		genomeVisualizer.disabledConnectionColor = disabledConnectionColor;
		return this;
	}
	
    /**
     * Applies a predefined set of default visualization parameters:
     * <ul>
     * <li>Node Size: 20px.</li>
     * <li>Shape: Circle.</li>
     * <li>Input Nodes: Green (#0f9900).</li>
     * <li>Hidden Nodes: Red (#db2100).</li>
     * <li>Output Nodes: Cyan (#00afbf).</li>
     * <li>Positive Connections: Dark Red (#960032).</li>
     * <li>Negative Connections: Purple (#320096).</li>
     * </ul>
     * @return This builder instance with default configuration.
     */
	public GenomeVisualizerBuilder defaultGenomeVisuals() {
		genomeVisualizer = new GenomeVisualizer(
				20d,
				SHAPE.CIRCLE,
				"#ffffff",
				"#000000",
				"#0f9900",
				"#db2100",
				"#00afbf",
				"#960032",
				"#320096",
				"#0f9900",
				"#830999",
				"#8a8a8a"
				);
		return this;
	}
	
    /**
     * Finalizes the configuration and constructs the {@link GenomeVisualizer}.
     * @return Fully configured GenomeVisualizer instance ready for use.
     */
	public GenomeVisualizer build() {
		return genomeVisualizer;
	}
	
}
