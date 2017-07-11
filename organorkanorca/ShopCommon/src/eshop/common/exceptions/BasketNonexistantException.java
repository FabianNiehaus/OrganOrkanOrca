package eshop.common.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class BasketNonexistantException.
 */
public class BasketNonexistantException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -381709339963813620L;

	/**
	 * Instantiates a new basket nonexistant exception.
	 */
	public BasketNonexistantException() {
		super("Der Warenkorb existiert nicht!");
	}
}
