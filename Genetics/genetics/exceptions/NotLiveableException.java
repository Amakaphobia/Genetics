package exceptions;

/**
 * Exception thrown if your Individuum isn't able to live
 * @author Dave
 *
 */
public class NotLiveableException extends Exception 
{
	/**svid*/
	private static final long serialVersionUID = -8870729027817069761L;

	/**
	 * Constructor
	 * @param s string
	 */
	public NotLiveableException(String s) {
		super(s);
	}
}
