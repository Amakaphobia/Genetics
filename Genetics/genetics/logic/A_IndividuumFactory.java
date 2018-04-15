package logic;

import java.util.LinkedList;

import boxes.GenericContainer;
import exceptions.NotLiveableException;
import geneticInterfaces.I_Map;
import utils.XStringBuilder;

/**
 * Superclass for all individuum factories
 * @author Dave
 *
 */
public abstract class A_IndividuumFactory 
{
	/**map for fitness calculation*/
	protected I_Map map;
	/**first individual*/
	protected A_Individuum firstIndi;
	/**separator*/
	protected String separator;
	
	/**
	 * Constructor
	 * @param map the map you want the fabric to use while build organisms
	 * @throws Exception if you fucked up
	 */
	public A_IndividuumFactory(I_Map map) throws Exception
	{
		this.map = map;
		this.separator = this.map.getSeparator();
		this.firstIndi = this.getARandomNewOne(this.separator);
	}
	
	/**
	 * Method used internally to generate a new liveable and random gene and to create a new organism with it
	 * @param Seperator the separator of the gene
	 * @return the new indi
	 * @throws Exception if you fucked up
	 */
	protected abstract A_Individuum getARandomNewOne(String Seperator) throws Exception;
	
	/**
	 *  Method used internally to generate a new organism given a gene
	 * @param sequence the gene
	 * @return the organism
	 * @throws Exception if you fucked up or its not valid
	 */
	protected abstract A_Individuum getASpecifiedOne(LinkedList<GenericContainer<?>> sequence) throws Exception;
	
	/**
	 * Method used from the outside to create a new random individuum 
	 * @return the new indi
	 * @throws Exception if you fucked up
	 */
	public A_Individuum getANewOne() throws Exception
	{
		return this.getANewOne(null);
	}
	
	/**
	 * Method used from the outside to create a new individuum
	 * @param gene the gene you want it to have
	 * @return the new indi
	 * @throws Exception if you fucked up or it isnt liveable
	 */
	public A_Individuum getANewOne(LinkedList<GenericContainer<?>> gene) throws Exception
	{
		if(gene == null)
			return this.getARandomNewOne(this.separator);
		
		A_Individuum newIndi = getASpecifiedOne(gene);

		if(this.checkValidity(newIndi.getGene()))
		{	
			newIndi.computeFitness();
			return newIndi;
		}
		else
		{
			XStringBuilder strb = new XStringBuilder("Individuum specified with ");
			strb.append(newIndi.getGeneText())
				.append(" isnt able to live")
				.linesep();
			
			throw new NotLiveableException	(strb.toString());
			
		}
	}
	
	/**
	 * Method used to get the separator
	 * @return the separator
	 */
	public String getSeparator()	{return this.separator;}
	
	/**
	 * Method used internally to check if a organism can live
	 * @param gene the gene you want to validate
	 * @return true if it can live
	 */
	public abstract boolean checkValidity(LinkedList<GenericContainer<?>> gene);
}
