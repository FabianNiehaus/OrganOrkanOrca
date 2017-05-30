/**
 * 
 */
package data_objects;

import domain.exceptions.InvalidPersonDataException;

/**
 * @author Fabian Niehaus
 *
 */
public abstract class Person {
	protected String firstname;
	protected String lastname;
	protected int id;
	private String passwort;
	
	/**
	 * Erzeugt eine Persion
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param id Eindeutige Identifikationsnummer
	 * @throws InvalidPersonDataException 
	 */
	public Person(String firstname, String lastname, int id, String passwort) throws InvalidPersonDataException {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
		this.setPasswort(passwort);
	}

	/**
	 * @return
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 * @throws InvalidPersonDataException 
	 */
	public void setFirstname(String firstname) throws InvalidPersonDataException {
		if(!firstname.equals("")){
			this.firstname = firstname;
		} else {
			throw new InvalidPersonDataException(0, firstname); 
		}
	}

	/**
	 * @return
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 * @throws InvalidPersonDataException 
	 */
	public void setLastname(String lastname) throws InvalidPersonDataException {
		if(!lastname.equals("")){
			this.lastname = lastname;
		} else {
			throw new InvalidPersonDataException(1, firstname); 
		}
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * @param passwort
	 * @throws InvalidPersonDataException 
	 */
	public void setPasswort(String passwort) throws InvalidPersonDataException {
		if(!passwort.equals("")){
			this.passwort = passwort;
		} else {
			throw new InvalidPersonDataException(6, ""); 
		}
	}
}
