package main.java.neat.functions;

import java.io.Serializable;
import java.util.Arrays;
import main.java.neat.config.AggregationConfig;

public abstract class AggregationFunction implements Serializable {
	
	private static final long serialVersionUID = 422002331363828167L;

	/**
	 * 
	 * @author Taher Joudeh
	 *
	 */
	public static enum AGGREGATION_FUNCTION {
		/**
		 * 
		 */
		SUM,
		
		/**
		 * 
		 */
		PRODUCT,
		
		/**
		 * 
		 */
		MIN,
		
		/**
		 * 
		 */
		MAX,
		
		/**
		 * 
		 */
		MEAN,
		
		/**
		 * 
		 */
		MEDIAN,
		
		/**
		 * 
		 */
		MAXABS,
		
		/**
		 * 
		 */
		RANDOM
	}
	
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
			aggFunction = AggregationConfig.getRandomAggregationFunction();
		}
		
		return aggFunction;
	}
	
	public abstract double aggregate(double[] x);
}

class SumAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 6891178657015151989L;

	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).sum();
	}
	
}

class ProductAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 6228742391815549339L;

	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).reduce(1, (a, b) -> a * b);
	}
	
}

class MinAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 804872795196076968L;

	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).min().orElse(0);
	}
	
}

class MaxAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 5916314222809196909L;

	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).max().orElse(0);
	}
	
}

class MeanAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 1136425934435429683L;

	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).average().orElse(0);
	}
	
}

class MedianAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 4696989465725726346L;

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

class MaxabsAggregationFunction extends AggregationFunction {

	private static final long serialVersionUID = 1227573177702680345L;

	@Override
	public double aggregate(double[] x) {
		return Arrays.stream(x).reduce(1, (a,b) -> Math.max(Math.abs(a), Math.abs(b)));
	}
	
}