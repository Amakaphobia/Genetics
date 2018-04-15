package evolvers;

import java.util.LinkedList;

import boxes.GenericContainer;
import geneticInterfaces.I_Evolver;
import logic.A_Individuum;
import logic.Controller;

/**
 * This Evolver simply completely shuffles the available genes
 * @author Dave
 *
 */
public class AllNewRandomPlaces implements I_Evolver {

	@Override
	public LinkedList<GenericContainer<?>> evolve(A_Individuum individuum) 
	{
		@SuppressWarnings("unchecked")
		LinkedList<GenericContainer<?>> toPop = (LinkedList<GenericContainer<?>>) individuum.getGene().clone();
		
		LinkedList<GenericContainer<?>> toAppend = new LinkedList<>();
		int i;
		
		
		while(toPop.size() != 0)
		{
			i = Controller.randomer.nextInt(
					toPop.size());
			
			toAppend.add(
					toPop.get(i));
			toPop.remove(i);
		}

		return toAppend;
	}

}
