package recombiners;

import java.util.LinkedList;

import boxes.GenericContainer;
import geneticInterfaces.I_Recombiner;
import logic.A_Individuum;
import logic.Controller;

/**
 * This class randomly picks the next gene part from either parent
 * @author Dave
 *
 */
public class RandomOrder implements I_Recombiner {

	@SuppressWarnings("unchecked")
	@Override
	public LinkedList<GenericContainer<?>> recombine(A_Individuum I1, A_Individuum I2)
	{
		LinkedList<GenericContainer<?>> toAppend = new LinkedList<>();
		LinkedList<GenericContainer<?>> list1 = (LinkedList<GenericContainer<?>>) I1.getGene().clone();
		LinkedList<GenericContainer<?>> list2 = (LinkedList<GenericContainer<?>>) I2.getGene().clone();
		int j;
		int threshold = I1.getGene().size();
		while(toAppend.size()<threshold)
		{
			if(Controller.randomer.nextBoolean())
			{
				j = Controller.randomer.nextInt(list1.size());
				toAppend.add(list1.get(j));
				list2.remove(toAppend.getLast());
				list1.remove(j);
			}
			else
			{
				j = Controller.randomer.nextInt(list2.size());
				toAppend.add(list2.get(j));
				list1.remove(toAppend.getLast());
				list2.remove(j);
			}
				
		}
		return toAppend;
	}
}
