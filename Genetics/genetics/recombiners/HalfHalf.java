package recombiners;

import java.util.LinkedList;
import java.util.stream.Collectors;

import boxes.GenericContainer;
import geneticInterfaces.I_Recombiner;
import logic.A_Individuum;
import logic.Controller;

/**
 * this class recombines the first half of the first genestrang with the rest of the second
 * @author Dave
 *
 */
public class HalfHalf implements I_Recombiner {

	@Override
	public LinkedList<GenericContainer<?>> recombine(A_Individuum I1, A_Individuum I2) 
	{
		LinkedList<GenericContainer<?>> toAppend = new LinkedList<>();
		@SuppressWarnings("unchecked")
		LinkedList<GenericContainer<?>> list = (LinkedList<GenericContainer<?>>) I2.getGene().clone();
		int size = I1.getGene().size();
		
		
		toAppend.addAll(this.getHalf(I1));
		for(GenericContainer<?> e : toAppend)
			list.remove(e);
		
		toAppend.addAll(
			list.stream()
				.unordered()
				.limit(size - toAppend.size())
				.collect(Collectors.toList()));	
		
		
		return toAppend;	
	}
	
	/**
	 * Method used internally to get half a gene
	 * @param I the gene you want to be cut
	 * @return a half gene
	 */
	private LinkedList<GenericContainer<?>> getHalf(A_Individuum I)
	{
		int halflength = (int) Math.floor(I.getGene().size() / 2);
		LinkedList<GenericContainer<?>> toAppend = new LinkedList<>();

		while(toAppend.size() < halflength)
		{
			toAppend.add(
					I.getGene().get(
							Controller.randomer.nextInt(
									I.getGene().size())));
		}
		return toAppend;
	}

}
