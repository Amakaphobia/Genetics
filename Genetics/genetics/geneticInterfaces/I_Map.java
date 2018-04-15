package geneticInterfaces;

import java.util.LinkedList;
import boxes.GenericContainer;

/**
 * This Interface is used to define a Map holding the tools to generate new random individuums and compute fitness
 * @author Dave
 *
 */
public interface I_Map 
{
	/**
	 * Method used to compute the fitness of a given indi
	 * @param genes the genes you want to check
	 * @return double containing the fitness
	 */
	public abstract double computeFitness(LinkedList<GenericContainer<?>> genes);
	
	/**
	 * returns the seperator
	 * @return a string containing the seperator
	 */
	public abstract String getSeparator();
	
	/**
	 * Method used to get the basic genepool
	 * @return the genepool
	 */
	public abstract LinkedList<GenericContainer<?>> getData();
}
