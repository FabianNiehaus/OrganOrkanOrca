package domain.exceptions;

public class LoginFailedException extends Exception {

	public LoginFailedException(String login) {
		super("Login für Nutzer " + login + " ist fehlgeschlaggen.");
	}
}
