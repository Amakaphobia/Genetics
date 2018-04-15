package exceptions;

/**
 * This Exception is thrown if you try to build a Gene without Specifying a gene seperator first
 * @author hdaiv_000
 *
 */
public class NoSeperator extends Exception 
{
	/**svid*/
	private static final long serialVersionUID = -8440006443781678573L;

	/**
	 * Constructor
	 * @param s string
	 */
	public NoSeperator(String s) {
		super(s);
	}
}
