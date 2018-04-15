package evolvers;

import java.util.LinkedList;

import boxes.GenericContainer;
import geneticInterfaces.I_Evolver;
import logic.A_Individuum;

/**
 * Class used to swap two halves
 * @author Dave
 *
 */
public class SwapHalfes implements I_Evolver {

	@Override
	public LinkedList<GenericContainer<?>> evolve(A_Individuum individuum) 
	{
		@SuppressWarnings("unchecked")
		LinkedList<GenericContainer<?>> toPop = (LinkedList<GenericContainer<?>>)individuum.getGene().clone();
		LinkedList<GenericContainer<?>> toSwap = new LinkedList<>();
		
		int end = toPop.size() % 2 == 0 ? toPop.size() / 2 : toPop.size() / 2 +1 ;
		
		for(int i = 0; i < end; i++)
		{
			toSwap.add(toPop.pop());
		}
		
		toPop.addAll(toSwap);
		
		return toPop;
	}
}
