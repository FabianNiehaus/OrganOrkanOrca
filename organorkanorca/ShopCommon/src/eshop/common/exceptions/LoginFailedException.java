package eshop.common.exceptions;

/**
 * @author Fabian Niehaus
 * Exception bei fehlgeschlagenem Login
 */
public class LoginFailedException extends Exception {

	private static final long serialVersionUID = 4399203773116521889L;

	public LoginFailedException(int id) {
		super("Login fuer Nutzer " + id + " ist fehlgeschlaggen.");
	}
	public LoginFailedException() {
		super("Nutzer-ID ist nicht gueltig.");
	}
}
