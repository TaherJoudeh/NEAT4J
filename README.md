# NEAT4J
**NEAT4J** is a Java implementation of the NeuroEvolution of Augmenting Topologies (NEAT) algorithm, designed to evolve artificial neural networks through genetic algorithms. This project provides a modular, configurable framework for experimenting with NEAT, including visualization, file I/O, and pre-built configurations.

## Features
1. Core NEAT Algorithm
   - Genome encoding for nodes and connections.
   - Speciation, crossover, and mutation operations.
   - Dynamic topology evolution (add/remove nodes/connections).
2. Configuration
   - ***NEATConfig***: Centralized settings for population size, mutation rates, etc.
   - ***NEATConfigBuilder***: Fluent API for custom configurations.
   - ***ActivationConfig and AggregationConfig***: Define activation/aggregation functions.
3. Visualization
   - ***GenomeVisualizer***: Capture/Visualize neural network topologies.
   - ***GenomeVisualizerBuilder***: Customize visualization styles.
4. I/O Utilities
   - ***GenomeFileHandler***: Save/load genomes in JSON or binary formats.
5. Pre-Built Functions
   - Activation functions: _**Sigmoid**_, _**ReLU**_, _**Tanh**_, _**Step**_, _**Linear**_.
   - Aggregation functions: _**Sum**_, _**Product**_, **Min**_, **Max**_, **Mean**_, **Median**_, **Maxabs**_.
6. Testing
   - Example: ***TestXOR*** demonstrates evolving a network to solve XOR.
## Installation
- Clone the Repository:
   `git clone https://github.com/yourusername/NEAT4J.git`
## Quick Start
1. Configure NEAT
   
