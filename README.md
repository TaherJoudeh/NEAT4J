# NEAT4J
**NEAT4J** is a Java implementation of the NeuroEvolution of Augmenting Topologies (NEAT) algorithm, designed to evolve artificial neural networks through genetic algorithms. This project provides a modular, configurable framework for experimenting with NEAT, including visualization, file I/O, and pre-built configurations.

## Features
1. ### Core NEAT Algorithm
   - Genome encoding for nodes and connections.
   - Speciation, crossover, and mutation operations.
   - Dynamic topology evolution (add/remove nodes/connections).
2. ### Configuration
   - ***NEATConfig***: Centralized settings for population size, mutation rates, etc.
   - ***NEATConfigBuilder***: Fluent API for custom configurations.
   - ***ActivationConfig and AggregationConfig***: Define activation/aggregation functions.
3. ### Visualization
   - ***GenomeVisualizer***: Capture/Visualize neural network topologies.
   - ***GenomeVisualizerBuilder***: Customize visualization styles.
4. ### I/O Utilities
   - ***GenomeFileHandler***: Save/load genomes in Java serialization format.
5. ### Pre-Built Functions
   - Activation functions: _**Sigmoid**_, _**ReLU**_, _**Tanh**_, _**Step**_, _**Linear**_.
   - Aggregation functions: _**Sum**_, _**Product**_, _**Min**_, _**Max**_, _**Mean**_, _**Median**_, _**Maxabs**_.
6. ### Testing
   - Example: ***TestXOR*** demonstrates evolving a network to solve XOR.
## Installation
- Clone the Repository:
   `git clone https://github.com/TaherJoudeh/NEAT4J`
## Quick Start
1. ### Configure NEAT
   	```java
	NEATConfig config = new NEATConfigBuilder(50, 2, 1).build();
	```
   The parameters of the ***NEATConfigBuilder*** constructor are:
      - First parameter: Population size.
      - Second parameter: Number of input nodes.
      - Third parameter: Number of output nodes.

   If you want to modify and configure Activations and Aggregations to your liking, you can:
   ```java
   ActivationConfig activationConfig = new ActivationConfigBuilder()
		.addActivationFunction(ACTIVATION_FUNCTION.SIGMOID)
		.setLinearActivationThreshold(0.5d)
		.build();
   AggregationConfig aggregationConfig = new AggregationConfig(AGGREGATION_FUNCTION.SUM);
   NEATConfig neatConfig = new NEATConfigBuilder(50, 2, 1, aggregationConfig, activationConfig).build();
   ```
2. ### Initialize Neat
   ```java
   Neat neat = new Neat(neatConfig);
   ```
3. ### Run the algorithm
   ```java
   double[][] inputs = {
		{0,0}, // -> 0
		{0,1}, // -> 1
		{1,0}, // -> 1
		{1,1}  // -> 0
   };
   
   Agent[] agents = neat.getPopulation();
   while (!neat.isFoundSolution()) {
   	for (int i = 0; i < neatConfig.getPopulationSize(); i++) {
		double[] output1 = agents[i].think(inputs[0]);
		double[] output2 = agents[i].think(inputs[1]);
		double[] output3 = agents[i].think(inputs[2]);
		double[] output4 = agents[i].think(inputs[3]);
		double fitness = Math.pow((1-output1[0]),2) +
			Math.pow(output2[0],2) +
			Math.pow(output3[0],2) +
			Math.pow((1-output4[0]),2);
   
			agents[i].setFitness(fitness);
	}
	neat.evolve(true);
   }
   
   Agent best = neat.getBest();
   double[] output1 = best.think(inputs[0]);
   double[] output2 = best.think(inputs[1]);
   double[] output3 = best.think(inputs[2]);
   double[] output4 = best.think(inputs[3]);
   System.out.println("0 - " + output1[0] + "\n1 - " + output2[0] + "\n1 - " + output3[0] + "\n0 - " + output4[0]
		+ "\n-------------------");
   ```
   
## Project Structure
## Examples
1. Solving the XOR logic gate.
## Acknowledgments
   - Kenneth O. Stanley and Risto Miikkulainen [Original NEAT paper](https://nn.cs.utexas.edu/downloads/papers/stanley.cec02.pdf).
   - [NEAT-Python](https://neat-python.readthedocs.io/en/latest/index.html).
