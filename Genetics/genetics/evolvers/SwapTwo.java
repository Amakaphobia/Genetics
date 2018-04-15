package evolvers;

import java.util.Collections;
import java.util.LinkedList;

import boxes.GenericContainer;
import boxes.Pair;
import geneticInterfaces.I_Evolver;
import logic.A_Individuum;
import logic.Controller;

/**
 * This Class is used to swap the places of two GeneContainer
 * @author Dave
 *
 */
public class SwapTwo implements I_Evolver {

	@SuppressWarnings("unchecked")
	@Override
	public LinkedList<GenericContainer<?>> evolve(A_Individuum individuum) 
	{
		Pair<Integer, Integer> p = this.getSwapps(individuum.getGene().size());
		
		LinkedList<GenericContainer<?>> toSwap = (LinkedList<GenericContainer<?>>) individuum.getGene().clone(); 
	
		Collections.swap(toSwap
					 	, p.getKey()
					 	, p.getValue());
		return toSwap;

	}
	
	
	/**
	 * Method used internally to get a Pair of two different integers between 0 and a bound
	 * @param bound the upper bound (exclusive)
	 * @return a pair containing two integers
	 */
	private Pair<Integer, Integer> getSwapps(int bound)
	{
		int a;
		int b;
		do
		{
			a = Controller.randomer.nextInt(bound);
			b = Controller.randomer.nextInt(bound);
		}while(a == b);
		
		Pair<Integer, Integer> retObj =
				a > b ?
						new Pair<>(b,a):
						new Pair<>(a,b);
		return retObj;
	}

}
