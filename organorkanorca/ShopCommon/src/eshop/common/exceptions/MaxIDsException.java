package eshop.common.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class MaxIDsException.
 */
public class MaxIDsException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5393028241416887104L;

	/**
	 * Instantiates a new max I ds exception.
	 *
	 * @param typ
	 *           the typ
	 */
	public MaxIDsException(String typ) {
		super("Maximale Anzahl an Nutzer fuer " + typ + " erreicht");
	}
}
