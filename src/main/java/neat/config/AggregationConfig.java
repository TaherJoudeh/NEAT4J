package main.java.neat.config;

import java.io.Serializable;
import java.util.ArrayList;
import main.java.neat.functions.AggregationFunction;
import main.java.neat.functions.AggregationFunction.AGGREGATION_FUNCTION;

/**
 * 
 * @author Taher Joudeh
 *
 */
public class AggregationConfig implements Serializable {

	private static final long serialVersionUID = 5248096586882534249L;
	
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
	
	/**
	 * 
	 * @return
	 */
	public static AggregationConfig defaultAggregationConfig() {
		return new AggregationConfig(AGGREGATION_FUNCTION.SUM);
	}
	
}
