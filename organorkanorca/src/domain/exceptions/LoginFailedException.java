package domain.exceptions;

public class LoginFailedException extends Exception {

	public LoginFailedException(String login) {
		super("Login f�r Nutzer " + login + " ist fehlgeschlaggen.");
	}
}
