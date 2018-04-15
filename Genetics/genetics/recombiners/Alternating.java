package recombiners;

import java.util.LinkedList;

import boxes.GenericContainer;
import geneticInterfaces.I_Recombiner;
import logic.A_Individuum;

/**
 * this class alternates in order between two parents
 * @author Dave
 *
 */
public class Alternating implements I_Recombiner 
{

	@Override
	public LinkedList<GenericContainer<?>> recombine(A_Individuum I1, A_Individuum I2) 
	{
		int length = I1.getGene().size();
		@SuppressWarnings("unchecked")
		LinkedList<GenericContainer<?>> list1 = (LinkedList<GenericContainer<?>>) I1.getGene().clone();
		@SuppressWarnings("unchecked")
		LinkedList<GenericContainer<?>> list2 = (LinkedList<GenericContainer<?>>) I2.getGene().clone();
		LinkedList<GenericContainer<?>> toAppend = new LinkedList<>();
		int i = 0;
		
		while(toAppend.size() < length)
		{
			if(i % 2 == 0)
			{
				toAppend.add(list1.getFirst());
				list2.remove(toAppend.getLast());
				list1.removeFirst();
			}
			else
			{
				toAppend.add(list2.getFirst());
				list1.remove(toAppend.getLast());
				list2.removeFirst();
			}
			i++;
		}
		return toAppend;
	}

}
