/**
 * 
 */
package eshop.common.data_objects;

import java.io.Serializable;

import eshop.common.exceptions.InvalidPersonDataException;

/**
 * @author Fabian Niehaus
 *
 */
public abstract class Person implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5177564072380459834L;
	protected String				address_Street;
	protected String				address_Town;
	protected String				address_Zip;
	protected String				firstname;
	protected int					id;
	protected String				lastname;
	protected String				passwort;

	/**
	 * Erzeugt eine Persion
	 * 
	 * @param firstname
	 *           Vorname
	 * @param lastname
	 *           Nachname
	 * @param id
	 *           Eindeutige Identifikationsnummer
	 * @throws InvalidPersonDataException
	 */
	public Person(String firstname, String lastname, int id, String passwort, String address_Street, String address_Zip,
			String address_Town) throws InvalidPersonDataException {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
		this.setPasswort(passwort);
		this.address_Street = address_Street;
		this.address_Zip = address_Zip;
		this.address_Town = address_Town;
	}

	/**
	 * @return
	 */
	public String getAddress_Street() {

		return address_Street;
	}

	/**
	 * @return
	 */
	public String getAddress_Town() {

		return address_Town;
	}

	/**
	 * @return
	 */
	public String getAddress_Zip() {

		return address_Zip;
	}

	/**
	 * @return
	 */
	public String getFirstname() {

		return firstname;
	}

	/**
	 * @return
	 */
	public int getId() {

		return id;
	}

	/**
	 * @return
	 */
	public String getLastname() {

		return lastname;
	}

	/**
	 * @return
	 */
	public String getPasswort() {

		return passwort;
	}

	/**
	 * @param address_Street
	 * @throws InvalidPersonDataException
	 */
	public void setAddress_Street(String address_Street) throws InvalidPersonDataException {

		if (!address_Street.equals("")) {
			this.address_Street = address_Street;
		} else {
			throw new InvalidPersonDataException(2, address_Street);
		}
	}

	/**
	 * @param address_Town
	 * @throws InvalidPersonDataException
	 */
	public void setAddress_Town(String address_Town) throws InvalidPersonDataException {

		if (!address_Town.equals("")) {
			this.address_Town = address_Town;
		} else {
			throw new InvalidPersonDataException(4, address_Town);
		}
	}

	/**
	 * @param address_Zip
	 * @throws InvalidPersonDataException
	 */
	public void setAddress_Zip(String address_Zip) throws InvalidPersonDataException {

		if (!address_Zip.equals("")) {
			this.address_Zip = address_Zip;
		} else {
			throw new InvalidPersonDataException(5, address_Zip);
		}
	}

	/**
	 * @param firstname
	 * @throws InvalidPersonDataException
	 */
	public void setFirstname(String firstname) throws InvalidPersonDataException {

		if (!firstname.equals("")) {
			this.firstname = firstname;
		} else {
			throw new InvalidPersonDataException(0, firstname);
		}
	}

	/**
	 * @param id
	 */
	public void setId(int id) {

		this.id = id;
	}

	/**
	 * @param lastname
	 * @throws InvalidPersonDataException
	 */
	public void setLastname(String lastname) throws InvalidPersonDataException {

		if (!lastname.equals("")) {
			this.lastname = lastname;
		} else {
			throw new InvalidPersonDataException(1, firstname);
		}
	}

	/**
	 * @param passwort
	 * @throws InvalidPersonDataException
	 */
	public void setPasswort(String passwort) throws InvalidPersonDataException {

		if (!passwort.equals("")) {
			this.passwort = passwort;
		} else {
			throw new InvalidPersonDataException(6, "");
		}
	}
}
