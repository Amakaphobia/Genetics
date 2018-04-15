package recombiners;

import java.util.LinkedList;
import boxes.Pair;
import geneticInterfaces.I_Recombiner;

/**
 * Class used to build recombiners
 * @author Dave
 *
 */
public class RecombinerFactory 
{
	/**list conating all recombiners*/
	private LinkedList<Pair<Integer, I_Recombiner>> list;
	/**identifies Alternating*/
	public final static int Alternating = 0;
	/**identifies HalfHalf*/
	public final static int HalfHalf 	= 1;
	/**identifies RandomOrder*/
	public final static int RandomOrder = 2;
	
	/**method used to generate the List containing all recombiners*/
	private void generateRecombinerList()
	{
		list.add(new Pair<Integer, I_Recombiner>(Alternating, new Alternating()));
		list.add(new Pair<Integer, I_Recombiner>(HalfHalf, new HalfHalf()));
		list.add(new Pair<Integer, I_Recombiner>(RandomOrder, new RandomOrder()));
	}
	
	/**Constructor*/
	public RecombinerFactory()
	{
		this.list = new LinkedList<>();
		this.generateRecombinerList();
	}
	
	/**
	 * Method used to find a concrete recombiner
	 * @param name the name of the recombiner
	 * @return the found recombiner
	 */
	private I_Recombiner find(int name)
	{
		return this.list.stream()
						.filter(pair -> pair.getKey() == name)
						.map(pair -> pair.getValue())
						.findFirst()
						.orElse(null)						
						;
	}
	
	/**
	 * used to get a recombiner given a name
	 * @param name the name
	 * @return the found recombiner
	 */
	public I_Recombiner getByName(int name)
	{
		if(name == list.get(name).getKey())
			return list.get(name).getValue();
		
		I_Recombiner search = this.find(name);		
		if(search != null)
			return search;
		throw new IllegalArgumentException("Spezifizierter Recombiner ist nicht implementiert.");
	}
	
	/**
	 * Method used to get a array containing all recombiner
	 * @return the array
	 */
	public I_Recombiner[] getAll()
	{		
		return this.list.stream()
						.map(Pair -> Pair.getValue())
						.toArray(size -> new I_Recombiner [size]);
	}
}
