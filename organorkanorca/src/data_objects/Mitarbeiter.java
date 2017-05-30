package data_objects;

import domain.exceptions.InvalidPersonDataException;

/**
 * @author Fabian Niehaus
 *
 */
public class Mitarbeiter extends Person {

	/**
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param id Eindeutige ID
	 * @param passwort Passwort
	 * @throws InvalidPersonDataException 
	 */
	public Mitarbeiter(String firstname, String lastname, int id, String passwort) throws InvalidPersonDataException {
		super(firstname, lastname, id, passwort);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
		return id + ": " + firstname + " " + lastname;
	}

}
