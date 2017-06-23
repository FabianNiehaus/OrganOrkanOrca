/**
 * 
 */
package data_objects;

import domain.exceptions.InvalidPersonDataException;

/**
 * @author Fabian Niehaus
 *
 */
public class Kunde extends Person {
	
	private Warenkorb wk;
	
	/**
	 * Extra-Konstruktor für Laden aus Persistenz
	 * @param firstname Vorname
	 * @param lastname Nachmane
	 * @param id Eindeutige ID
	 * @param passwort Passwort
	 * @param address_Street Adresse + Hausnummer
	 * @param address_Zip Postleitzahl
	 * @param address_Town Stadt
	 * @throws InvalidPersonDataException 
	 */
	public Kunde(String firstname, String lastname, int id, String passwort, String address_Street, String address_Zip, String address_Town) throws InvalidPersonDataException {
		super(firstname, lastname, id, passwort, address_Town, address_Town, address_Town);
	}
	
	/**
	 * Standard-Konstruktor
	 * @param firstname Vorname
	 * @param lastname Nachmane
	 * @param id Eindeutige ID
	 * @param passwort Passwort
	 * @param address_Street Adresse + Hausnummer
	 * @param address_Zip Postleitzahl
	 * @param address_Town Stadt
	 * @param wk Warenkorb
	 * @throws InvalidPersonDataException 
	 */
	public Kunde(String firstname, String lastname, int id, String passwort, String address_Street, String address_Zip, String address_Town, Warenkorb wk) throws InvalidPersonDataException {
		super(firstname, lastname, id, passwort, address_Town, address_Town, address_Town);
		this.wk = wk;
	}

	/**
	 * @return
	 */
	public Warenkorb getWarenkorb() {
		return wk;
	}

	/**
	 * @param warenkorb
	 */
	public void setWarenkorb(Warenkorb warenkorb) {
		this.wk = warenkorb;
	}
	
	public String toString(){
		return id + ": " + firstname + " " + lastname + " | " + address_Street + " ," + address_Zip + " " + address_Town;
	}

}
