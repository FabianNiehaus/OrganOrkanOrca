package eshop.common.exceptions;

public class InvalidPersonDataException extends Exception {

    public InvalidPersonDataException(int id, String object) {
	super(determineOutput(id, object));
    }

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
