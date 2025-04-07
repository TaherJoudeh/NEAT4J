package main.java.neat.visualizer;

import main.java.neat.visualizer.GenomeVisualizer.SHAPE;

public class GenomeVisualizerBuilder {

	private GenomeVisualizer genomeVisualizer;
	
	public GenomeVisualizerBuilder() {
		genomeVisualizer = new GenomeVisualizer();
	}
	
	public GenomeVisualizerBuilder setNodeSize(double nodeSize) {
		genomeVisualizer.nodeSize = nodeSize;
		return this;
	}
	public GenomeVisualizerBuilder setNodeShape(SHAPE nodeShape) {
		genomeVisualizer.nodeShape = nodeShape;
		return this;
	}
	public GenomeVisualizerBuilder setNodeBoundsColor(String nodeBoundsColor) {
		genomeVisualizer.nodeBoundsColor = nodeBoundsColor;
		return this;
	}
	public GenomeVisualizerBuilder setNonActivatedNodeColor(String nonActivatedNodeColor) {
		genomeVisualizer.nonActivatedNodeColor = nonActivatedNodeColor;
		return this;
	}
	public GenomeVisualizerBuilder setInputNodeColor(String inputNodeColor) {
		genomeVisualizer.inputNodeColor = inputNodeColor;
		return this;
	}
	public GenomeVisualizerBuilder setHiddenNodeColor(String hiddenNodeColor) {
		genomeVisualizer.hiddenNodeColor = hiddenNodeColor;
		return this;
	}
	public GenomeVisualizerBuilder setOutputNodeColor(String outputNodeColor) {
		genomeVisualizer.outputNodeColor = outputNodeColor;
		return this;
	}
	public GenomeVisualizerBuilder setPosConnectionColor(String posConnectionColor) {
		genomeVisualizer.posConnectionColor = posConnectionColor;
		return this;
	}
	public GenomeVisualizerBuilder setNegConnectionColor(String negConnectionColor) {
		genomeVisualizer.negConnectionColor = negConnectionColor;
		return this;
	}
	public GenomeVisualizerBuilder setPosRecurrentConnectionColor(String posRecurrentConnectionColor) {
		genomeVisualizer.posRecurrentConnectionColor = posRecurrentConnectionColor;
		return this;
	}
	public GenomeVisualizerBuilder setNegRecurrentConnectionColor(String negRecurrentConnectionColor) {
		genomeVisualizer.negRecurrentConnectionColor = negRecurrentConnectionColor;
		return this;
	}
	public GenomeVisualizerBuilder setDisabledConnectionColor(String disabledConnectionColor) {
		genomeVisualizer.disabledConnectionColor = disabledConnectionColor;
		return this;
	}
	
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
	
	public GenomeVisualizer build() {
		return genomeVisualizer;
	}
	
}
