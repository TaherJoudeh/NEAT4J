# NEAT4J

A comprehensive Java implementation of NEAT (NeuroEvolution of Augmenting Topologies) for evolving neural network architectures through genetic algorithms.

## Overview

NEAT4J provides a complete implementation of the NEAT algorithm with extensive configuration options, visualization capabilities, and file persistence. The library evolves both neural network weights and topologies, making it suitable for problems where the optimal network structure is unknown.

## Features

- **Complete NEAT Algorithm**: Speciation, crossover, mutation, and innovation tracking
- **Flexible Configuration**: Extensive parameter customization through builder patterns
- **Multiple Activation Functions**: Sigmoid, Tanh, ReLU, Step, and Linear
- **Aggregation Functions**: Sum, Product, Min, Max, Mean, Median, and MaxAbs
- **Network Visualization**: Customizable genome visualization with export capabilities
- **Persistence**: Save and load evolved networks using Java serialization
- **Zero Dependencies**: Pure Java implementation with no external requirements

## Installation

### Option 1: JAR File
Download `neat4j.jar` and add it to your project classpath.

### Option 2: Source Code
```bash
git clone https://github.com/TaherJoudeh/NEAT4J.git
```

**Requirements**: Java 8 or higher

## Quick Start

### Basic XOR Problem

```java
import main.java.neat.core.*;
import main.java.neat.config.*;

// Configure NEAT with 50 agents, 2 inputs, 1 output
NEATConfig config = new NEATConfigBuilder(50, 2, 1)
    .setFitnessTermination(true)
    .setFitnessTerminationThreshold(3.9)
    .build();

// Initialize NEAT
Neat neat = new Neat(config);

// XOR training data
double[][] inputs = {{0,0}, {0,1}, {1,0}, {1,1}};
double[] expected = {0, 1, 1, 0};

// Evolution loop
while (!neat.isTerminated()) {
    Agent[] population = neat.getPopulation();
    
    for (Agent agent : population) {
        double fitness = 4.0;
        for (int i = 0; i < inputs.length; i++) {
            double[] output = agent.think(inputs[i]);
            fitness -= Math.abs(expected[i] - output[0]);
        }
        agent.setFitness(Math.max(0, fitness));
    }
    
    neat.evolve(true);
}

// Test the best solution
Agent best = neat.getBest();
for (int i = 0; i < inputs.length; i++) {
    double[] result = best.think(inputs[i]);
    System.out.printf("%.0f XOR %.0f = %.3f\n", 
        inputs[i][0], inputs[i][1], result[0]);
}
```

## Configuration

### Basic Configuration

```java
NEATConfig config = new NEATConfigBuilder(populationSize, inputNodes, outputNodes)
    .setFeedForward(true)
    .setProbAddNode(0.03)
    .setProbAddConnection(0.05)
    .build();
```

### Custom Activation Functions

```java
ActivationConfig activationConfig = new ActivationConfigBuilder()
    .addActivationFunction(ACTIVATION_FUNCTION.SIGMOID)
    .addActivationFunction(ACTIVATION_FUNCTION.RELU)
    .setSigmoidActivationThreshold(0.5)
    .setReluLeak(0.01)
    .build();

AggregationConfig aggregationConfig = new AggregationConfig(AGGREGATION_FUNCTION.SUM);

NEATConfig config = new NEATConfigBuilder(100, 4, 2, aggregationConfig, activationConfig)
    .setActivationMutationRate(0.1)
    .build();
```

### Advanced Configuration

```java
NEATConfig config = new NEATConfigBuilder(150, 8, 3)
    .setStartingHiddenNodes(5, 3)  // Two hidden layers with 5 and 3 nodes
    .setInitConnectivity(NEATConfig.CONNECTIVITY.LAYER_BY_LAYER)
    .setMaxNumberOfHiddenNodes(20)
    .setElitism(5)
    .setSurvivalThreshold(0.3)
    .setCompatibilityThreshold(3.0)
    .setStagnation(15)
    .setWeightMutationPower(0.8)
    .build();
```

## Network Visualization

```java
// Create visualizer
GenomeVisualizer visualizer = new GenomeVisualizerBuilder()
    .setNodeSize(25)
    .setNodeShape(GenomeVisualizer.SHAPE.CIRCLE)
    .setInputNodeColor("#4CAF50")
    .setHiddenNodeColor("#FF9800")
    .setOutputNodeColor("#2196F3")
    .setPosConnectionColor("#000000")
    .setNegConnectionColor("#FF0000")
    .build();

// Generate visualization
BufferedImage image = GenomeVisualizer.visualizeGenome(
    visualizer,
    "#FFFFFF",  // Background color
    false,      // Draw disabled connections
    true,       // Activate all nodes
    agent.getGenome(),
    5.0,        // Max weight value for scaling
    800, 600    // Image dimensions
);

// Save image
GenomeFileHandler.saveImage(image, "./output", "network", "png");
```

## File Operations

### Save and Load Genomes

```java
// Save genome
GenomeFileHandler.saveGenome(agent.getGenome(), "./models", "best_network");

// Load genome
Genome genome = GenomeFileHandler.loadGenome("./models/best_network.neat");
Agent agent = new Agent(genome);
```

## Configuration Parameters

### Population Settings
- `populationSize`: Number of individuals in population (default: 1)
- `fitnessCriterion`: Optimization direction - MAX or MIN (default: MAX)
- `elitism`: Number of best individuals preserved each generation within each species (default: 0)
- `survivalThreshold`: Fraction of population selected for reproduction (default: 0.5)
- `selectionType`: Selection method - ROULETTE_WHEEL or TOURNAMENT (default: ROULETTE_WHEEL)
- `tournamentSize`: Number of genomes in tournament selection (default: 1)

### Termination Conditions
- `fitnessTermination`: Enable fitness-based termination (default: false)
- `fitnessTerminationThreshold`: Fitness threshold for termination (default: 0)
- `generationTermination`: Enable generation-based termination (default: false)
- `generationTerminationThreshold`: Maximum number of generations (default: 0)

### Speciation Parameters
- `compatibilityThreshold`: Threshold for species membership (default: 3.0)
- `compatibilityExcessCoefficient`: Weight for excess genes in distance calculation (default: 1.0)
- `compatibilityDisjointCoefficient`: Weight for disjoint genes in distance calculation (default: 1.0)
- `compatibilityWeightCoefficient`: Weight for weight differences in distance calculation (default: 0.5)
- `dynamicCompatibilityThreshold`: Enable dynamic threshold adjustment (default: false)
- `targetNumberOfSpecies`: Target number of species for dynamic threshold (default: 1)
- `compatibilityThresholdAdjustingFactor`: Factor for threshold adjustment (default: 0.1)
- `speciesFitnessFunction`: Species fitness calculation - MIN, MAX, MEAN, MEDIAN (default: MEAN)
- `stagnation`: Generations before species elimination (default: 15)
- `speciesElitism`: Number of species protected from elimination (default: 0)

### Network Structure
- `numberOfInputs`: Number of input nodes (required)
- `numberOfOutputs`: Number of output nodes (required)
- `startingHiddenNodes`: Array of hidden layer sizes (default: empty array)
- `maxNumberOfHiddenNodes`: Maximum hidden nodes allowed (default: Integer.MAX_VALUE)
- `initConnectivity`: Initial connection pattern (default: UNCONNECTED)
- `feedForward`: Restrict to feed-forward networks (default: true)

### Activation Functions
- `startingActivationFunctionForHiddenNode`: Default activation for hidden nodes (default: SIGMOID)
- `startingActivationFunctionForOutputNode`: Default activation for output nodes (default: SIGMOID)
- `activationDefault`: Default activation for new nodes (default: SIGMOID)
- `activationMutationRate`: Probability of changing activation function (default: 0)
- `activationConfig`: Configuration of available activation functions

### Aggregation Functions
- `startingAggregationFunction`: Default aggregation function (default: SUM)
- `aggregationDefault`: Default aggregation for new nodes (default: SUM)
- `aggregationMutationRate`: Probability of changing aggregation function (default: 0)
- `aggregationConfig`: Configuration of available aggregation functions

### Node Mutation Parameters
- `probAddNode`: Probability of adding a new node (default: 0.01)
- `probDeleteNode`: Probability of deleting a node (default: 0)

### Connection Mutation Parameters
- `probAddConnection`: Probability of adding a new connection (default: 0.03)
- `probDeleteConnection`: Probability of deleting a connection (default: 0)
- `probRecurrentConnection`: Probability new connection is recurrent (default: 0.1)
- `probConnectInit`: Connection probability for partial connectivity (default: 0.5)

### Connection Enable/Disable
- `enabledDefault`: New connections enabled by default (default: true)
- `enabledMutationRate`: Base probability of toggling connection state (default: 0)
- `enabledRateForEnabled`: Additional rate for enabled connections (default: 0)
- `enabledRateForDisabled`: Additional rate for disabled connections (default: 0)

### Weight Parameters
- `weightInitMean`: Mean for weight initialization (default: 0)
- `weightInitStdev`: Standard deviation for weight initialization (default: 1)
- `weightInitDistributionType`: Weight initialization distribution - NORMAL or UNIFORM (default: NORMAL)
- `weightMaxValue`: Maximum weight value (default: 50)
- `weightMinValue`: Minimum weight value (default: -50)
- `weightMutationPower`: Magnitude of weight mutations (default: 0.1)
- `weightAdjustingRate`: Probability of adjusting weights (default: 0.8)
- `weightRandomizingRate`: Probability of randomizing weights (default: 0.1)

### Bias Parameters
- `biasInitMean`: Mean for bias initialization (default: 0)
- `biasInitStdev`: Standard deviation for bias initialization (default: 1)
- `biasInitDistributionType`: Bias initialization distribution - NORMAL or UNIFORM (default: NORMAL)
- `biasMaxValue`: Maximum bias value (default: 50)
- `biasMinValue`: Minimum bias value (default: -50)
- `biasMutationPower`: Magnitude of bias mutations (default: 0.1)
- `biasAdjustingRate`: Probability of adjusting bias (default: 0.8)
- `biasRandomizingRate`: Probability of randomizing bias (default: 0.1)

### Response Parameters
- `responseInitMean`: Mean for response initialization (default: 1)
- `responseInitStdev`: Standard deviation for response initialization (default: 0)
- `responseInitDistributionType`: Response initialization distribution (default: NORMAL)
- `responseMaxValue`: Maximum response value (default: 50)
- `responseMinValue`: Minimum response value (default: -50)
- `responseMutationPower`: Magnitude of response mutations (default: 0.1)
- `responseAdjustingRate`: Probability of adjusting response (default: 0.7)
- `responseRandomizingRate`: Probability of randomizing response (default: 0.1)

### Structural Mutation Control
- `singleStructuralMutation`: Allow only one structural change per genome (default: false)
- `structuralMutationAdvisor`: Enable intelligent mutation guidance (default: false)

## Examples

### Control Problem

```java
public class ControllerEvolution {
    public static void main(String[] args) {
        NEATConfig config = new NEATConfigBuilder(200, 4, 2)
            .setFeedForward(false)  // Allow recurrent connections
            .setProbAddNode(0.02)
            .setProbAddConnection(0.05)
            .build();
        
        Neat neat = new Neat(config);
        
        while (!neat.isTerminated()) {
            for (Agent agent : neat.getPopulation()) {
                double fitness = evaluateController(agent);
                agent.setFitness(fitness);
            }
            neat.evolve(true);
        }
    }
    
    private static double evaluateController(Agent agent) {
        // Implement your control evaluation logic here
        return 0.0;
    }
}
```

### Pattern Classification

```java
public class ClassificationEvolution {
    private double[][] trainingData;
    private double[] labels;
    
    public void evolveClassifier() {
        NEATConfig config = new NEATConfigBuilder(100, trainingData[0].length, 1)
            .setFitnessTermination(true)
            .setFitnessTerminationThreshold(0.95)
            .build();
        
        Neat neat = new Neat(config);
        
        while (!neat.isTerminated()) {
            for (Agent agent : neat.getPopulation()) {
                double accuracy = calculateAccuracy(agent);
                agent.setFitness(accuracy);
            }
            neat.evolve(true);
        }
    }
    
    private double calculateAccuracy(Agent agent) {
        int correct = 0;
        for (int i = 0; i < trainingData.length; i++) {
            double[] output = agent.think(trainingData[i]);
            double prediction = output[0] > 0.5 ? 1.0 : 0.0;
            if (prediction == labels[i]) {
                correct++;
            }
        }
        return (double) correct / trainingData.length;
    }
}
```

## Project Structure

```
src/main/java/neat/
├── core/                    # Core NEAT algorithm implementation
│   ├── Agent.java          # Individual with genome and fitness
│   ├── Genome.java         # Neural network representation
│   ├── Node.java           # Network node implementation
│   ├── Connection.java     # Connection between nodes
│   └── Neat.java           # Main NEAT algorithm controller
├── config/                  # Configuration classes
│   ├── NEATConfig.java     # Main configuration
│   ├── NEATConfigBuilder.java
│   ├── ActivationConfig.java
│   ├── ActivationConfigBuilder.java
│   ├── AggregationConfig.java
│   └── ConfigFactory.java
├── functions/               # Activation and aggregation functions
│   ├── ActivationFunction.java
│   └── AggregationFunction.java
├── io/                      # File I/O utilities
│   └── GenomeFileHandler.java
└── visualizer/              # Network visualization
    ├── GenomeVisualizer.java
    └── GenomeVisualizerBuilder.java
```

## Other Examples
- [Google Dino Game](https://github.com/TaherJoudeh/NEAT-Dino-Game)

## Acknowledgments

- Kenneth O. Stanley and Risto Miikkulainen for the original NEAT algorithm
- [Original NEAT Paper](https://nn.cs.utexas.edu/downloads/papers/stanley.cec02.pdf)
- [NEAT-Python](https://neat-python.readthedocs.io/en/latest/) for API design inspiration
