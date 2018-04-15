package logic;

import java.util.Random;
import java.util.function.Consumer;

import geneticInterfaces.I_Evolver;
import geneticInterfaces.I_Map;
import geneticInterfaces.I_Recombiner;
import string.StringPrinter;
import subscribeAndPublish.I_Subscriber;

/**
 * This Class is a Builder and Control Object for the Environment of the Population
 * @author Dave
 *
 */
public class Controller implements I_Subscriber
{
	/**this is used to generate random inputs*/
	public final static Random randomer = new Random();
	/**the map for fitness calculation*/
	public static I_Map map;
	/**the Population*/
	private Population Population;
	/**Consumer(String) used to log*/
	private Consumer<String> Logger;
	/**true if the logger doesnt need closing*/
	private boolean notClose;
	/**used to Print Strings to files*/
	private StringPrinter sp;
	/**used to check if this is subscribed to something*/
	private boolean subscribed;
	
	
	/**
	 * Constructor
	 * @param Factory a A_Individuum Factory thats able to build your Population
	 * @throws Exception if something goes wrong read exception for details
	 */
	public Controller(A_IndividuumFactory Factory) throws Exception
	{
		this.Population = new Population(Factory);
		this.subscribed = true;
		map = Factory.map;
		notClose=false;
	}
	
	/**
	 * Method used to run the defined Population
	 * @return the last Population census
	 * @throws Exception if something goes wrong
	 */
	public Population evolve()throws Exception{
		this.checkLogger();
		if(this.subscribed)
			this.Population.subscribe(this);
		Population ret = this.Population.runGenerations();
		if(!this.notClose)
			this.close();
		return ret;
	}
	
	/**
	 * sets a subscribed flag true if you want to get updates false if you don't want them
	 * @param b your input
	 * @return this for chain building
	 */
	public Controller setSubscribed(boolean b) 
	{
		this.subscribed = b;
		return this;
	}
	
	/**
	 * Method used to set how much of you population should die per cycle
	 * @param d double between 0 and 1
	 * @return this for chain building
	 */
	public Controller setNatSelectPercent(double d)
	{
		if(this.checkForPercent(d))
			this.Population.setNatSelectPercent(d);
		else
			throw new IllegalArgumentException("that parameter mus be between 0 and 1");
		return this;
	}
	
	/**Internal Method used to check how to log*/
	private void checkLogger()
	{
		if(this.sp == null)
		{
			Logger = e -> System.out.println(e);
			this.notClose = true;
		}
		else
			Logger = this.sp.getLogger();
	}
	
	/**Internal Method used to close the FileWriter should it be open*/
	private void close(){
		this.sp.close();
	}
	
	/**
	 * Method used to specify the use of a file logger
	 * @param target the target file you wish to log to
	 * @return this for chain building
	 */
	public Controller setLogger(String target)
	{
		this.sp = new StringPrinter(target);
		return this;
	}
	
	/**
	 * Method used to define the number of organisms that spawn in the 0 Generation
	 * @param start you number
	 * @return this for chain building
	 */
	public Controller setStartPopulation(int start)
	{
		this.Population.setStartPopulation(start);
		return this;
	}
	
	/**
	 * Method used to set a Max Population for Memory savings
	 * @param max integer describing the top boundary for your Population
	 * @return this for chain building
	 */
	public Controller setMaxPopulation(int max)
	{
		this.Population.setMaxPopulation(max);
		return this;
	}
	
	/**
	 * Method used to specify the use of evolving mechanisms in order 
	 * @param order true to use in order
	 * @return this for chain building
	 */
	public Controller setInOrder(boolean order)
	{
		this.Population.setInOrder(order);
		return this;
	}
	
	/**
	 * Method used to define the Recombination strategies used by the population
	 * @param recombiner array holding your I_Recombiner strategies
	 * @return this for chain building
	 */
	public Controller setRecombinationStrategies(I_Recombiner[] recombiner)
	{
		this.Population.setRecombiningStrategy(recombiner);
		return this;
	}
	
	/**
	  * Method used to define the evolving strategies used by the population
	 * @param evolver array holding your I_Evolver strategies
	 * @return this for chain building
	 */
	public Controller setEvolvingStrategies(I_Evolver[] evolver)
	{
		this.Population.setEvolvingStrategy(evolver);
		return this;
	}
	
	/**
	 * MEthhod used to define how many generations should be run through
	 * @param max the number of generations
	 * @return this for chain building
	 */
	public Controller setMaxGenerations(int max)
	{
		this.Population.setEndGen(max);
		return this;
	}
	
	/**
	 * Method used to set how many of your Individuums should evolve
	 * @param d double between 0 and 1
	 * @return this for chain building
	 */
	public Controller setElvolePercent(double d)
	{
		if(this.checkForPercent(d))
			this.Population.setElvolePercent(d);
		else
			throw new IllegalArgumentException("that parameter mus be between 0 and 1");
		return this;
	}
	
	/**
	 * Method used to set how many of your Individuums should recombine
	 * @param d double between 0 and 1
	 * @return this for chain building
	 */
	public Controller setRecombinePercent(double d)
	{
		if(this.checkForPercent(d))
			this.Population.setRecombinePercent(d);
		else
			throw new IllegalArgumentException("that parameter mus be between 0 and 1");
		return this;
	}
	
	@Override
	public <T> void getUpdates(T Update) {
		Logger.accept((String) Update);		
	}
	
	/**
	 * Internal Method that checks if your inputs are allright
	 * @param d a double to be checked if it between 0 and 1
	 * @return true if valid
	 */
	private boolean checkForPercent(double d)
	{
		return d >=0 && d <= 1;
	}

	/**
	 * sets how often the program should notify back to you 
	 * @param i step
	 * @return this for chain building
	 */
	public Controller setNotificationStep(int i) {
		this.Population.setNotificationStep(i);
		return this;
	}
}