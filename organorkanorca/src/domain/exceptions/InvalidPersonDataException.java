package domain.exceptions;

public class InvalidPersonDataException extends Exception {

	public InvalidPersonDataException(int id, String object){
		
		super(determineOutput(id, object));
		
	}
	
	private static String determineOutput(int id, String object){
		
		switch(id){
			case 0: return object + " ist kein gültiger Vorname!";
			case 1: return object + " ist kein gültiger Nachname!";
			case 2: return object + " ist keine gültige Straße!";
			case 3: return object + " ist keine gültige Stadt!";
			case 4: return object + " ist keine gültige Postleitzahl!";
			case 5: return "Kein gültiges Passwort!";
			case 6: return "Leere Felder sind nicht erlaubt!";
			default: return "";
		}
	}
}
