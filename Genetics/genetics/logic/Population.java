package logic;


import java.util.Collections;
import java.util.LinkedList;
import boxes.GenericContainer;
import geneticInterfaces.I_Evolver;
import geneticInterfaces.I_Recombiner;
import subscribeAndPublish.I_Publisher;
import subscribeAndPublish.I_Subscriber;
import utils.XStringBuilder;

/**
 * This Class holds the Population, Environment and CycleLogic
 * @author Dave
 *
 */
public class Population implements I_Publisher
{
	/**Messages*/
	private static String welcome				= "Welcome to Evolvemaster 3000";
	/**Messages*/
	private static String initial				= "generating initial generation now";
	/**Messages*/
	private static String line 				= "---------------------------------------------------";
	/**Messages*/
	private static String generationnumber	= "Information about generation: "; 
	/**Messages*/
	private static String MutationText		= "following mutations occured: ";
	/**Messages*/
	private static String RecombineText		= "following recombinations occured";
	/**Messages*/
	private static String popcount			= "Population size: ";	
	/**Messages*/
	private static String MutationIteration 	= " mutated to ";
	/**Messages*/
	private static String und					= " and ";
	/**Messages*/
	private static String RecombineIteration	= " recombined to ";
	/**Messages*/
	private static String using				= " using ";
	
	/**List of subscribers most likely only the controller*/
	private LinkedList<I_Subscriber> subs;
	/**List Containing all Individuums of your population*/
	private LinkedList<A_Individuum> Population;
	/**The Factory used to build the population*/
	private A_IndividuumFactory Factory;	
	
	/**if evolutions happens ordered*/
	private boolean inOrder;
	/**after how many Generations should a notification be send out*/
	private int notificationStep;
	/**the number of the current gen*/
	private int actualGen;
	/**the number of the last generation*/
	private int endgen;
	/**how many individuums should get natSelected*/
	private double natSelectPercent;	
	/**how many individuals recombine in percent*/
	private double recombinePercent;
	/**array containing recombining strategies*/
	private I_Recombiner[] recombiningStrategies;
	/**how many individuals shoult evolve in percent*/
	private double elvolePercent;
	/**array containing the evolving strategies*/
	private I_Evolver[] evolvingStrategies;	
	/**population size in creation*/
	private int startPopulation;
	/**current population size*/
	private int actualPop;
	/**maximal population size*/
	private int maxPopulation;
	
	/**
	 * Constructor
	 * @param Factory the factory you want to use to build the population
	 * @throws Exception if you fucked up
	 */
	public Population(A_IndividuumFactory Factory) throws Exception
	{
		this.Population 		= new LinkedList<>();
		this.subs				= new LinkedList<>();
		this.Factory 			= Factory;
		this.maxPopulation 		= Integer.MAX_VALUE;
		this.notificationStep	= -1;
		this.natSelectPercent	= 0;
		this.inOrder			= false;
	}
	
	/**
	 * Method used by the Controller Class to run the simulation
	 * @return this for data access
	 * @throws Exception if you fucked up
	 */
	public Population runGenerations() throws Exception
	{
		XStringBuilder strb = new XStringBuilder();
		for(I_Subscriber e : this.subs)
		{
			strb.append(welcome)
				.linesep()
				.append(initial)
				.linesep();
			e.getUpdates(strb.toString());
		}
		
		this.populate();
		this.sendGeneralUpdate();
		
		for(int i = 0; i < this.endgen; i++)
		{
			this.runNextGen();
			Collections.sort(this.Population);
			this.actualGen++;
			this.sendGeneralUpdate();
			
			if(this.notificationStep!= -1 && this.actualGen % this.notificationStep == 0)
				System.out.printf("Current Generation: %s \n",this.actualGen);
			
			if(this.Population.getFirst().compareTo(this.Population.getLast()) == 0)
			{
				this.sendUpdate(String.format("Population is Homogen, stopping Lifecycle after %s Generations", this.actualGen));
				break;
			}
		}
		this.subs.forEach(e ->
		e.getUpdates(this.createFinalUpdate()));
		
		return this;
	}
	
	/**
	 * Used to get the initial Population
	 * @throws Exception if you fucked up
	 */
	private void populate() throws Exception
	{
		this.Population = new LinkedList<>();
		for(int i = 0; i < this.startPopulation; i++)
			this.Population.add(this.Factory.getARandomNewOne(this.Factory.getSeparator()));
		
		this.actualPop = this.Population.size();
		this.actualGen = 0;

		Collections.sort(Population);
	}

	/**higher level internal function that is used to advanced from one generation to the next*/
	private void runNextGen()
	{
		LinkedList<A_Individuum> nextGen = new LinkedList<>();
		this.naturalSelect();		
		nextGen.addAll(this.performEvolve());
		nextGen.addAll(this.performRecombine());
		this.addNextGen(nextGen);
	}
	
	/**
	 * Method used internally to add the new Generation to the current one and handle over population
	 * @param nextGen the next generation
	 */
	private void addNextGen(LinkedList<A_Individuum> nextGen)
	{
		while(nextGen.size() != 0)
			this.Population.add(nextGen.pop());
		Collections.sort(this.Population);
		XStringBuilder strb = new XStringBuilder("Killed due to population size");
		strb.linesep();
		while(this.Population.size() > this.maxPopulation)
		{	strb.append("Gene: ")
			.append(this.Population.getLast().getGeneText())
			.append(" Fitness ")
			.append(this.Population.getLast().getFitness())
			.linesep();
			this.Population.removeLast();
		}
		this.sendUpdate(strb.toString());
		this.actualPop = Population.size();
	}
	
	/**
	 * high level function to perform evolving
	 * @return a List of evolved Individuums
	 */
	private LinkedList<A_Individuum> performEvolve()
	{
		int countEvolve = (int)Math.ceil
				(this.elvolePercent * this.Population.size());
		
		LinkedList<A_Individuum> toEvolve =
				this.getToEvolve(countEvolve);
		return this.evolve(toEvolve);
		
	}
	
	/**
	 * high level function to perform recombining
	 * @return a List of recombined Individuums
	 */
	private LinkedList<A_Individuum> performRecombine()
	{
		int countRecombine = (int)Math.ceil
				(this.recombinePercent * this.Population.size());		
		LinkedList<A_Individuum> toRecombine =
				this.getToRecombine(countRecombine);
		return this.recombine(toRecombine);
	}
	
	/**Method used to internally to kill the worst individuums*/
	private void naturalSelect()
	{
		XStringBuilder strb = new XStringBuilder("Natural selection time");
		strb.linesep();
		int countSelect = (int) Math.floor
				(this.natSelectPercent * this.Population.size());
		for(int i = countSelect; i>0;i--)
		{
			strb.append("Killing: ")
				.append(this.Population.getLast().getGeneText())
				.append(" Fitness: ")
				.append(this.Population.getLast().getFitness())
				.linesep();
			this.Population.removeLast();
		}
		this.sendUpdate(strb.toString());
	}
	
	/**
	 * Method used internally to evolve a list of individuums into different ones
	 * @param toEvolve the list you want to evolve
	 * @return a LinkedList with the evolved Individuums
	 */
	private LinkedList<A_Individuum> evolve(LinkedList<A_Individuum> toEvolve)
	{
		XStringBuilder strb 						= new XStringBuilder();
		LinkedList<GenericContainer<?>> newGene;
		LinkedList<A_Individuum> newIndividuals	= new LinkedList<>();
		A_Individuum newIndi;
		A_Individuum e;
		I_Evolver strategy = null;
		int i = 0;
		
		strb.append(MutationText)
			.linesep();
		
		while(toEvolve.size() > 0)
		{
			try
			{	
				if(this.inOrder)
					strategy = this.evolvingStrategies[i++ % this.evolvingStrategies.length];
				else
					strategy = this.evolvingStrategies[Controller.randomer.nextInt(this.evolvingStrategies.length)];
				e = toEvolve.getFirst();
				
				newGene = strategy.evolve(e);
				
				newIndi = this.Factory.getANewOne(newGene);
				
				strb.append(e.getGeneText())
					.append(MutationIteration)
					.append(newIndi.getGeneText())
					.append(using)
					.append(strategy.getName())
					.linesep();
				
				newIndividuals.add((A_Individuum)newIndi.clone());
				this.sendUpdate(strb.pop());
			} catch(Exception ex)
			{
				this.sendUpdate(String.format("Strategy(Evolve): %s threw:\n %s", strategy.getName(),ex.getMessage()));
			}finally{
				toEvolve.removeFirst();
			}
		}
		return newIndividuals; 
	}
	
	/**
	 * Method used internally to recombine a list of individuums into different ones
	 * @param toRecombine the list you want to recombine
	 * @return a LinkedList with the recombined Individuums
	 */
	private LinkedList<A_Individuum> recombine(LinkedList<A_Individuum> toRecombine)
	{
		XStringBuilder strb 	= new XStringBuilder();
		LinkedList<A_Individuum> newIndividuals	= new LinkedList<>();
		A_Individuum newIndi;
		I_Recombiner strategy = null;
		int i = 0;
		
		strb.append(RecombineText)
			.linesep();
		this.sendUpdate(strb.toString());
		while(toRecombine.size() > 0)
		{
			try
			{
				strb.clear();
				if(this.inOrder)
					strategy = this.recombiningStrategies[i++ % this.recombiningStrategies.length];
				else
					strategy = this.recombiningStrategies[Controller.randomer.nextInt(this.recombiningStrategies.length)];
				
				LinkedList<GenericContainer<?>> newGene = strategy.recombine
															(toRecombine.get(0), toRecombine.get(1));				
				newIndi = this.Factory.getANewOne(newGene);
				
				if( !newGene.isEmpty())
				{
					strb.append(toRecombine.get(0).getGeneText())
						.append(und)
						.append(toRecombine.get(1).getGeneText())
						.append(RecombineIteration)
						.append(newIndi.getGeneText())
						.append(using)
						.append(strategy.getName())
						.linesep();
					
					newIndividuals.add((A_Individuum)newIndi.clone());
					this.sendUpdate(strb.pop());
				}
			}
			catch(Exception ex)
			{
				this.sendUpdate(String.format("Strategy(Recombination): %s threw:\n %s", strategy.getName(),ex.getMessage()));
			}finally{
				toRecombine.remove(0);
				toRecombine.remove(0);
			}
		}
		return newIndividuals;
	}
	
	/**
	 * Method used internally to get a list of indis to evolve
	 * @param countEvolve how many you want
	 * @return list containing indis
	 */
	private LinkedList<A_Individuum> getToEvolve(int countEvolve)
	{
		LinkedList<A_Individuum> toEvolve = new LinkedList<>();
		while(countEvolve > toEvolve.size())
		{
			toEvolve.add(
				this.Population.get(
						Controller.randomer.nextInt(
								this.Population.size())));
		}
		return toEvolve;
	}
	
	/**
	 * Method used internally to get a list of indis to recombine
	 * @param countRecombine how many you want
	 * @return list containing indis
	 */
	private LinkedList<A_Individuum> getToRecombine(int countRecombine)
	{
		int anzahlLeft = countRecombine;
		LinkedList<A_Individuum> toRecombine = new LinkedList<A_Individuum>();
		
		if(anzahlLeft %2 == 1)
			if(anzahlLeft +1 > this.maxPopulation)
				anzahlLeft++;
			else
				anzahlLeft--;
		while(anzahlLeft > 0)
		{
			toRecombine.add(
					this.Population.get(
							Controller.randomer.nextInt(
									this.Population.size())));
			anzahlLeft--;
		}
		
		return toRecombine;
	}
	
	/**
	 * Method used to create a general update
	 * @return a string with info about the last generation
	 */
	private String createUpdate()
	{
		XStringBuilder strb = new XStringBuilder();
		strb.append(line)
			.linesep()		
			.append(generationnumber)
			.append(this.actualGen)
			.linesep()
			.append(popcount)
			.append(this.actualPop)
			.linesep();
		this.Population.forEach(e -> strb.append(e.toString()).linesep());
		return strb.toString();
	}
	
	/**
	 * Method used to create the final update
	 * @return a string containing infos
	 */
	private String createFinalUpdate()
	{
		XStringBuilder strb = new XStringBuilder();
		strb.linesep()
			.append(line)
			.linesep()
			.append("The Fittest Individuum found is: ")
			.append(this.Population.getFirst())
			.linesep()
			.append("Total Population size is: ")
			.append(this.Population.size());
		
		
		return strb.toString();
	}
	
	/**method used to send the general update to the subscribers*/
	public void sendGeneralUpdate			()					{this.sendUpdate(this.createUpdate());}
	
	/**
	 * Method used to set how many indis should die
	 * @param d how many you want to die(in percent) between 0 and 1
	 */
	public void setNatSelectPercent			(double d)			{this.natSelectPercent = d;}
	
	/**
	 * Method used to set how many indis should recombine
	 * @param d how many you want to recombine(in percent) between 0 and 1
	 */
	public void setRecombinePercent			(double d)			{this.recombinePercent = d;}
	
	/**
	 * Method used to set how often you want to get a notifictaion even if youre not subscribed
	 * @param i steps
	 */
	public void setNotificationStep			(int i)				{this.notificationStep = i;}
	
	/**
	 * Method used to set how many indis should evolve
	 * @param d how many you want to evolve(in percent) between 0 and 1
	 */
	public void setElvolePercent				(double d)			{this.elvolePercent = d;}
	
	/**
	 * Method used to set how many generation you want to simulate
	 * @param i number of generations
	 */
	public void setEndGen						(int i)				{this.endgen = i;}
	
	/**
	 * Method used to set the starting population size 
	 * @param i the size of generation 0
	 */
	public void setStartPopulation			(int i) 			{this.startPopulation = i;}
	
	/**
	 * Method used to set a maximal population size default is max(Integer)
	 * @param i new population size
	 */
	public void setMaxPopulation				(int i) 			{this.maxPopulation = i;}
	
	/**
	 * Method used to define valid evolution strategies
	 * @param ES the strategies
	 */
	public void setEvolvingStrategy   		(I_Evolver[] ES)	{this.evolvingStrategies = ES;}
	
	/**
	 * Method used to define valid recombination strategies
	 * @param RS the strategies
	 */
	public void setRecombiningStrategy   	(I_Recombiner[] RS)	{this.recombiningStrategies = RS;}
	
	/**
	 * Method used if you want to used defined rules in order instead of randomized
	 * @param b true if you want it ordered
	 */
	public void setInOrder					(boolean b)			{this.inOrder = b;}
	
	/**
	 * Method used to access the population
	 * @return this population
	 */
	public LinkedList<A_Individuum> getPop	()					{return this.Population;}
	
	@Override
	public void subscribe						(I_Subscriber sub) 	{this.subs.add(sub);}
	@Override
	public void unsubscribe					(I_Subscriber sub) 	{this.subs.remove(sub);}
	@Override
	public <T> void sendUpdate				(T s)				{this.subs.forEach(e -> e.getUpdates( s ));}
}











