package main.java.neat.functions;

import java.io.Serializable;
import java.util.Arrays;

/**
 * An abstract class defining aggregation functions for neural network nodes.
 * Aggregation functions determine how a node combines its input values into
 * a single output value before applying the activation function.
 * 
 * @author Taher Joudeh
 */
public abstract class AggregationFunction implements Serializable {
	
	private static final long serialVersionUID = 422002331363828167L;

    /**
     * Enum representing different types of aggregation functions available
     * for neural network nodes.
     */
	public static enum AGGREGATION_FUNCTION {
		/**
		 * Sums all input values.
		 */
		SUM,
		
		/**
		 * Multiplies all input values.
		 */
		PRODUCT,
		
		/**
		 * Returns the minimum input value.
		 */
		MIN,
		
		/**
		 * Returns the maximum input value.
		 */
		MAX,
		
		/**
		 * Calculates the average of input values.
		 */
		MEAN,
		
		/**
		 * Returns the median of input values.
		 */
		MEDIAN,
		
		/**
		 * Returns the maximum absolute value from inputs.
		 */
		MAXABS,
		
		/**
		 * Randomly selects an aggregation function (excludes RANDOM itself).
		 */
		RANDOM
	}
	
    /**
     * Factory method to get an aggregation function instance based on the specified type.
     * 
     * @param aggregationFunction the type of aggregation function to create
     * @return concrete implementation of the specified aggregation function
     * @throws IllegalArgumentException if an unknown aggregation function type is requested
     */
	public final static AggregationFunction getAggregationFunction(AGGREGATION_FUNCTION aggregationFunction) {
		AggregationFunction aggFunction = null;
		
		switch (aggregationFunction) {
		case PRODUCT:
			aggFunction = new ProductAggregationFunction();
			break;
		case MIN:
			aggFunction = new MinAggregationFunction();
			break;
		case MAX:
			aggFunction = new MaxAggregationFunction();
			break;
		case MEAN:
			aggFunction = new MeanAggregationFunction();
			break;
		case MEDIAN:
			aggFunction = new MedianAggregationFunction();
			break;
		case MAXABS:
			aggFunction = new MaxabsAggregationFunction();
			break;
		case SUM:
			aggFunction = new SumAggregationFunction();
			break;
		case RANDOM:
			aggFunction = getRandomAggregationFunction();
		}
		
		return aggFunction;
	}
	
	/**
     * Aggregates an array of input values into a single output value.
     * 
     * @param x array of input values to be aggregated
     * @return the aggregated result
     */
	public abstract double aggregate(double[] x);
	
    /**
     * Returns a random aggregation function instance.
     * 
     * This static utility method selects a random aggregation function from all
     * available functions (excluding the RANDOM function itself). It's typically
     * used during mutation operations when a random aggregation function needs
     * to be assigned to a node.
     * 
     * Note that this method returns an actual AggregationFunction instance,
     * not just the enum value, making it ready for direct use in a node.
     * 
     * @return A randomly selected aggregation function instance
     */
	protected static AggregationFunction getRandomAggregationFunction() {
		
		int numOfAggFuncs = AGGREGATION_FUNCTION.values().length-1;
		return getAggregationFunction(AGGREGATION_FUNCTION.values()[(int)(Math.random()*numOfAggFuncs)]);
		
	}
}

/**
 * Aggregation function that computes the sum of all input values.
 */
class SumAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 6891178657015151989L;

	/**
     * Computes the sum of all input values.
     * 
     * @param x array of input values
     * @return sum of all values in the input array
     */
	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).sum();
	}
	
}

/**
 * Aggregation function that computes the product of all input values.
 */
class ProductAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 6228742391815549339L;

    /**
     * Computes the product of all input values.
     * 
     * @param x array of input values
     * @return product of all values in the input array
     */
	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).reduce(1, (a, b) -> a * b);
	}
	
}

/**
 * Aggregation function that returns the minimum input value.
 */
class MinAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 804872795196076968L;

    /**
     * Finds the minimum value in the input array.
     * 
     * @param x array of input values
     * @return smallest value in the input array, or 0 if array is empty
     */
	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).min().orElse(0);
	}
	
}

/**
 * Aggregation function that returns the maximum input value.
 */
class MaxAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 5916314222809196909L;

    /**
     * Finds the maximum value in the input array.
     * 
     * @param x array of input values
     * @return largest value in the input array, or 0 if array is empty
     */
	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).max().orElse(0);
	}
	
}

/**
 * Aggregation function that computes the arithmetic mean of input values.
 */
class MeanAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 1136425934435429683L;

    /**
     * Calculates the average of input values.
     * 
     * @param x array of input values
     * @return arithmetic mean of input values, or 0 if array is empty
     */
	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).average().orElse(0);
	}
	
}

/**
 * Aggregation function that computes the median of input values.
 */
class MedianAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 4696989465725726346L;

    /**
     * Calculates the median of input values.
     * 
     * @param x array of input values
     * @return median value of the input array
     */
	@Override
	public double aggregate(double[] x) {
		int length = x.length;
		double[] sorted = Arrays.stream(x).sorted().toArray();
		
		double median = sorted[length/2];
		if (length%2 == 0)
			median = (sorted[length/2 - 1] + sorted[length/2])/2d;
		
		return median;
	}
	
}

/**
 * Aggregation function that returns the maximum absolute value from inputs.
 */
class MaxabsAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 1227573177702680345L;

    /**
     * Finds the maximum absolute value in the input array.
     * 
     * @param x array of input values
     * @return value with the largest absolute magnitude in the input array
     */
	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).reduce(1, (a,b) -> Math.max(Math.abs(a), Math.abs(b)));
	}
	
}