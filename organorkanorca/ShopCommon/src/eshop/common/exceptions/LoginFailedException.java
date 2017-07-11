package eshop.common.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginFailedException.
 */
public class LoginFailedException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4399203773116521889L;

	/**
	 * Instantiates a new login failed exception.
	 */
	public LoginFailedException() {
		super("Nutzer-ID ist nicht gueltig.");
	}

	/**
	 * Instantiates a new login failed exception.
	 *
	 * @param id
	 *           the id
	 */
	public LoginFailedException(int id) {
		super("Login fuer Nutzer " + id + " ist fehlgeschlaggen.");
	}
}
