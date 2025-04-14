package main.java.neat.config;

import java.io.Serializable;
import java.util.ArrayList;
import main.java.neat.functions.AggregationFunction.AGGREGATION_FUNCTION;

/**
 * Configuration class for neural network aggregation functions.
 * 
 * Aggregation functions determine how a neural network node combines multiple
 * input signals before applying an activation function. Common aggregation methods
 * include summation, product, mean, median, maxabs, max, and min operations.
 * 
 * This class manages which aggregation functions are allowed in the NEAT network
 * evolution process. Restricting the allowed functions can help guide the evolutionary
 * process toward more effective network topologies for specific problem domains.
 * 
 * @author Taher Joudeh
 */
public class AggregationConfig implements Serializable {

	private static final long serialVersionUID = 5248096586882534249L;
	
    /**
     * List of aggregation functions that are allowed to be used in this NEAT implementation.
     * This restricts which aggregation functions can be assigned to nodes during mutation.
     */
	private ArrayList<AGGREGATION_FUNCTION> allowedAggregationFunctions;
	
    /**
     * Creates a new AggregationConfig with the specified allowed aggregation functions.
     * 
     * This constructor accepts a variable number of aggregation functions to allow.
     * The RANDOM function is automatically excluded from the allowed functions list
     * as it's a special case used only for random selection.
     * 
     * Example:
     * <pre>
     * // Allow SUM and PRODUCT functions
     * AggregationConfig config = new AggregationConfig(
     *     AGGREGATION_FUNCTION.SUM,
     *     AGGREGATION_FUNCTION.PRODUCT
     * );
     * </pre>
     * 
     * @param allowedAggregationFunctions Variable number of aggregation functions to allow
     */
	public AggregationConfig(AGGREGATION_FUNCTION... allowedAggregationFunctions) {
		this.allowedAggregationFunctions = new ArrayList<> ();
		for (AGGREGATION_FUNCTION aggFunc: allowedAggregationFunctions)
			addAggregationFunction(aggFunc);
	}
	
    /**
     * Adds an aggregation function to the list of allowed functions.
     * 
     * This private method ensures each function appears only once in the list
     * and specifically prevents the RANDOM function from being added directly.
     * The RANDOM function is excluded because it's a special case used only
     * for random selection and not as an actual aggregation method.
     * 
     * @param aggregationFunction The aggregation function to add to the allowed list
     */
	private void addAggregationFunction(AGGREGATION_FUNCTION aggregationFunction) {
		if (aggregationFunction != AGGREGATION_FUNCTION.RANDOM && !allowedAggregationFunctions.contains(aggregationFunction))
			allowedAggregationFunctions.add(aggregationFunction);
	}
	
    /**
     * Gets the list of currently allowed aggregation functions.
     * 
     * These are the functions that can be used by the NEAT algorithm
     * during network creation and mutation.
     * 
     * @return ArrayList of allowed aggregation function types
     */
	public ArrayList<AGGREGATION_FUNCTION> getAllowedAggregationFunctions() {
		return allowedAggregationFunctions;
	}
	
}
