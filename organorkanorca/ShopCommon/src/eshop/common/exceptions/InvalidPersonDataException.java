package eshop.common.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class InvalidPersonDataException.
 */
public class InvalidPersonDataException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5794550176553643715L;

	/**
	 * Instantiates a new invalid person data exception.
	 *
	 * @param id
	 *           the id
	 * @param object
	 *           the object
	 */
	public InvalidPersonDataException(int id, String object) {
		super(determineOutput(id, object));
	}

	/**
	 * Determine output.
	 *
	 * @param id
	 *           the id
	 * @param object
	 *           the object
	 * @return the string
	 */
	private static String determineOutput(int id, String object) {

		switch (id) {
			case 0:
				return object + " ist kein gueltiger Vorname!";
			case 1:
				return object + " ist kein gueltiger Nachname!";
			case 2:
				return object + " ist keine gueltige Straße!";
			case 3:
				return object + " ist keine gueltige Stadt!";
			case 4:
				return object + " ist keine gueltige Postleitzahl!";
			case 5:
				return "Kein gueltiges Passwort!";
			case 6:
				return "Leere Felder sind nicht erlaubt!";
			default:
				return "";
		}
	}
}
