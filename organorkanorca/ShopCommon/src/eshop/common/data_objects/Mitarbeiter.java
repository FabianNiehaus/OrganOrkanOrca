package eshop.common.data_objects;

import eshop.common.exceptions.InvalidPersonDataException;

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
	public Mitarbeiter(String firstname, String lastname, int id, String passwort, String address_Street, String address_Zip, String address_Town) throws InvalidPersonDataException {
		super(firstname, lastname, id, passwort, address_Town, address_Town, address_Town);
	}
	
	public String toString(){
		return id + ": " + firstname + " " + lastname;
	}

}
