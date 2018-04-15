package evolvers;

import java.util.LinkedList;
import boxes.Pair;
import geneticInterfaces.I_Evolver;

/**
 * Class used to generate the evolvers
 * @author Dave
 *
 */
public class EvolverFactory 
{
	/**LIst containing all evolvers*/
	private LinkedList<Pair<Integer, I_Evolver>> list;
	/**Identifies AllNewRandomPlaces*/
	public static final int AllNewRandomPlaces 	= 0;
	/**Identifies ReverseHalf*/
	public static final int ReverseHalf	 		= 1;
	/**Identifies ReverseIt*/
	public static final int ReverseIt			= 2;
	/**Identifies SwapHalfes*/
	public static final int SwapHalfes 			= 3;
	/**Identifies SwapTwo*/
	public static final int SwapTwo				= 4;
	
	/**method used to generate the List containing all evolvers*/
	private void generateEvolverList()
	{
		list.add(new Pair<Integer, I_Evolver>(AllNewRandomPlaces, new AllNewRandomPlaces()));
		list.add(new Pair<Integer, I_Evolver>(ReverseHalf, new ReverseHalf()));
		list.add(new Pair<Integer, I_Evolver>(ReverseIt, new ReverseIt()));
		list.add(new Pair<Integer, I_Evolver>(SwapHalfes, new SwapHalfes()));
		list.add(new Pair<Integer, I_Evolver>(SwapTwo, new SwapTwo()));
	}
	
	/**Constructor*/
	public EvolverFactory()
	{
		this.list = new LinkedList<>();
		this.generateEvolverList();
	}
	
	/**
	 * Method used to find a concrete Evolver
	 * @param name the name of the evolver
	 * @return the found evolver
	 */
	private I_Evolver find(int name)
	{
		return this.list.stream()
						.filter(pair -> pair.getKey() == name)
						.map(pair -> pair.getValue())
						.findFirst()
						.orElse(null)						
						;
	}
	
	/**
	 * used to get a Evolver given a name
	 * @param name the name
	 * @return the found evolver
	 */
	public I_Evolver getByName(int name)
	{
		if(name == list.get(name).getKey())
			return list.get(name).getValue();
		
		I_Evolver search = this.find(name);		
		if(search != null)
			return search;
		throw new IllegalArgumentException("Spezifizierter Evolver ist nicht implementiert.");
	}
	
	/**
	 * Method used to get a array containing all evolvers
	 * @return the array
	 */
	public I_Evolver[] getAll()
	{		
		return this.list.stream()
						.map(Pair -> Pair.getValue())
						.toArray(size -> new I_Evolver [size]);
	}	
}
