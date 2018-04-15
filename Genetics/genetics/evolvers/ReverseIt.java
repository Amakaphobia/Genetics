package evolvers;

import java.util.Collections;
import java.util.LinkedList;

import boxes.GenericContainer;
import geneticInterfaces.I_Evolver;
import logic.A_Individuum;

/**
 * Class used to reverse the genesequence 
 * @author Dave
 *
 */
public class ReverseIt implements I_Evolver {

	@Override
	public LinkedList<GenericContainer<?>> evolve(A_Individuum individuum) 
	{
		@SuppressWarnings("unchecked")
		LinkedList<GenericContainer<?>> toPop = (LinkedList<GenericContainer<?>>) individuum.getGene().clone();
		
		Collections.reverse(toPop);
		
		return toPop;
	}

}
