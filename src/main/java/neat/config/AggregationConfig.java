package main.java.neat.config;

import java.io.Serializable;
import java.util.ArrayList;

import main.java.neat.functions.AggregationFunction;

/**
 * 
 * @author Taher Joudeh
 *
 */
public class AggregationConfig implements Serializable {

	private static final long serialVersionUID = 5248096586882534249L;

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
	
	private ArrayList<AGGREGATION_FUNCTION> allowedAggregationFunctions;
	
	/**
	 * 
	 */
	public AggregationConfig(AGGREGATION_FUNCTION... allowedAggregationFunctions) {
		this.allowedAggregationFunctions = new ArrayList<> ();
		for (AGGREGATION_FUNCTION aggFunc: allowedAggregationFunctions)
			addAggregationFunction(aggFunc);
	}
	
	/**
	 * 
	 * @param aggregationFunction
	 */
	private void addAggregationFunction(AGGREGATION_FUNCTION aggregationFunction) {
		if (!allowedAggregationFunctions.contains(aggregationFunction) && aggregationFunction != AGGREGATION_FUNCTION.RANDOM)
			allowedAggregationFunctions.add(aggregationFunction);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<AGGREGATION_FUNCTION> getAllowedAggregationFunctions() {
		return allowedAggregationFunctions;
	}
	
	/**
	 * 
	 * @return
	 */
	public static AggregationFunction getRandomAggregationFunction() {
		
		int numOfAggFuncs = AGGREGATION_FUNCTION.values().length-1;
		return AggregationFunction.getAggregationFunction(AGGREGATION_FUNCTION.values()[(int)(Math.random()*numOfAggFuncs)]);
		
	}
	
}
