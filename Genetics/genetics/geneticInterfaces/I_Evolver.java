package geneticInterfaces;

import java.util.LinkedList;

import boxes.GenericContainer;
import logic.A_Individuum;

/**
 * this interface is used to implement new Evolving strategies
 * @author hdaiv_000
 *
 */
public interface I_Evolver 
{
	/**
	 * the evolving strategy of your class
	 * @param individuum the individuum to be evolved
	 * @return a new genestrang
	 */
	public abstract LinkedList<GenericContainer<?>> evolve(A_Individuum individuum);
	
	/**
	 * Method used to get the Class name of the strategy
	 * @return the class name
	 */
	public default String getName()
	{
		String s = this.getClass().getName(); 
		return s.substring(s.lastIndexOf('.') + 1,s.length());
	}
}
