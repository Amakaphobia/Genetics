package evolvers;

import java.util.LinkedList;
import boxes.GenericContainer;
import geneticInterfaces.I_Evolver;
import logic.A_Individuum;
import logic.Controller;

/**
 * Reverses half of the gene sequence
 * @author Dave
 *
 */
public class ReverseHalf implements I_Evolver 
{

	@SuppressWarnings("unchecked")
	@Override
	public LinkedList<GenericContainer<?>> evolve(A_Individuum individuum) 
	{
		LinkedList<GenericContainer<?>> toPop = (LinkedList<GenericContainer<?>>)individuum.getGene().clone();
		LinkedList<GenericContainer<?>> toReverse = new LinkedList<>();
		
		int start;
		int end;
		
		if(Controller.randomer.nextBoolean())
		{
			end = 0;
			start = toPop.size() % 2 == 0 ? toPop.size() / 2 : toPop.size() / 2 +1 ;
		}
		else
		{
			end = toPop.size() % 2 == 0 ? toPop.size() / 2 : toPop.size() / 2 +1 ;
			start = toPop.size() - 1;
		}
		
		for(int i = start; i < end; i--)
		{
			toReverse.add(toPop.get(i));
			toPop.remove(i);
		}
		
		toPop.addAll(toReverse);
		
		return toPop;
		
	}

}
