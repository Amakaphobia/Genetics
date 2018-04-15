package logic;

import java.util.LinkedList;

import boxes.GenericContainer;
import exceptions.NoSeperator;
import geneticInterfaces.I_Map;

/**
 * this class is the individuum superclass 
 * @author Dave
 *
 */
public abstract class A_Individuum implements Comparable<A_Individuum>, Cloneable
{
	/**List of genes*/
	protected LinkedList<GenericContainer<?>> genes;
	/**fitness value*/
	protected double fitness;
	/**seperator for single genes*/
	protected String seperator;
	/**the map describing the fitness*/
	protected I_Map map;
	
	/**
	 * Constructor
	 * @param map the map used by the factory to build the indis
	 * @param gene the gene you want the indi to have
	 * @throws Exception if you fucked up
	 */
	public A_Individuum(I_Map map, LinkedList<GenericContainer<?>> gene) throws Exception
	{
		this.genes = new LinkedList<>();
		this.map = map;
		this.seperator = map.getSeparator();
		this.setGene(gene);
		this.computeFitness();
	}
	
	/**
	 * Used to get all genepart reduced into a string uses separator to separate them
	 * @return the string
	 */
	public String getGeneText(){
		String text = this.genes.stream()
					.map(e -> e.getValue().toString())
					.reduce("",(carry,  e)-> carry +e +this.seperator);
		if(text.length() > 0)
			return text.substring(0,text.length() - this.seperator.length());
		else
			return "";
	}

	/**
	 * Method used to get Fitness
	 * @return double containing Fitness
	 */
	public double getFitness()			{return this.fitness;}	
	
	/**
	 * Method used to compute fitness
	 */
	protected void computeFitness()		{this.fitness = this.map.computeFitness(this.genes);}	

	/**
	 * Used to get a specific part of the gene
	 * @param i the index 
	 * @return the gene at the index
	 */
	public GenericContainer<?> getGeneAt(int i)			{return this.genes.get(i);}
	
	/**
	 * gets the first genepart
	 * @return the first genepart
	 */
	public GenericContainer<?> getGeneFirst()			{return this.genes.getFirst();}
	
	/**
	 * gets the last genepart
	 * @return the last genepart
	 */
	public GenericContainer<?> getGeneLast()			{return this.genes.getLast();}
	
	/**
	 * gets the whole gene
	 * @return the whole gene
	 */
	public LinkedList<GenericContainer<?>> getGene()	{return this.genes;}
	
	/**
	 * Method used to set the gene of the indi
	 * @param sequence the gene
	 * @throws Exception if you fucked up or it isnt livdable
	 */
	protected void setGene(LinkedList<GenericContainer<?>> sequence) throws Exception
	{
		if(this.seperator.isEmpty())
			throw new NoSeperator
			("You forgot to specify a Gene seperator");
		if(sequence.isEmpty())
			throw new Exception("Your Gene is empty");
		this.genes = sequence;
	}
	
	@Override
	public Object clone() 
	{
		try 
		{
			return super.clone();	
		} 
		catch (CloneNotSupportedException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public abstract String toString();
	
}