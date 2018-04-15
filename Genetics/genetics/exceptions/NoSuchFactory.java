package exceptions;

/**
 * is thrown if you ty to create a factory that doesnt exist
 * @author hdaiv_000
 *
 */
public class NoSuchFactory extends NoSeperator 
{
	/**svid*/
	private static final long serialVersionUID = 6884600544688982561L;
	/**
	 * Constructor
	 * @param s string
	 */
	public NoSuchFactory(String s) {
		super(s);
	}
}
