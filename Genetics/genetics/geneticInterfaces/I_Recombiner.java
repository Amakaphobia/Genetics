package geneticInterfaces;

import java.util.LinkedList;

import boxes.GenericContainer;
import logic.A_Individuum;

/**
 * This Interface is used to implement new recombining strategies
 * @author hdaiv_000
 *
 */
public interface I_Recombiner 
{
	/**
	 * This method is the abstract strategies
	 * @param I1 the first parent
	 * @param I2 the second parent
	 * @return a new GeneStrang
	 */
	public abstract LinkedList<GenericContainer<?>> recombine(A_Individuum I1, A_Individuum I2);
	
	/**
	 * Method used to get the Class name of the strategy
	 * @return class name of the strategy
	 */
	public default String getName()
	{
		String s = this.getClass().getName(); 
		return s.substring(s.lastIndexOf('.') + 1,s.length());
	}
}
