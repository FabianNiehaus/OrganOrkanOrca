package domain.exceptions;

public class LoginFailedException extends Exception {

	public LoginFailedException(int id) {
		super("Login f�r Nutzer " + id + " ist fehlgeschlaggen.");
	}
}
