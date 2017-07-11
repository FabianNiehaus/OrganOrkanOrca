/**
 * 
 */
package eshop.common.data_objects;

import java.io.Serializable;

import eshop.common.exceptions.InvalidPersonDataException;

// TODO: Auto-generated Javadoc
/**
 * The Class Person.
 */
public abstract class Person implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 5177564072380459834L;
	
	/** The address street. */
	protected String				address_Street;
	
	/** The address town. */
	protected String				address_Town;
	
	/** The address zip. */
	protected String				address_Zip;
	
	/** The firstname. */
	protected String				firstname;
	
	/** The id. */
	protected int					id;
	
	/** The lastname. */
	protected String				lastname;
	
	/** The passwort. */
	protected String				passwort;

	/**
	 * Instantiates a new person.
	 *
	 * @param firstname
	 *           the firstname
	 * @param lastname
	 *           the lastname
	 * @param id
	 *           the id
	 * @param passwort
	 *           the passwort
	 * @param address_Street
	 *           the address street
	 * @param address_Zip
	 *           the address zip
	 * @param address_Town
	 *           the address town
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
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
	 * Gets the address street.
	 *
	 * @return the address street
	 */
	public String getAddress_Street() {

		return address_Street;
	}

	/**
	 * Gets the address town.
	 *
	 * @return the address town
	 */
	public String getAddress_Town() {

		return address_Town;
	}

	/**
	 * Gets the address zip.
	 *
	 * @return the address zip
	 */
	public String getAddress_Zip() {

		return address_Zip;
	}

	/**
	 * Gets the firstname.
	 *
	 * @return the firstname
	 */
	public String getFirstname() {

		return firstname;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {

		return id;
	}

	/**
	 * Gets the lastname.
	 *
	 * @return the lastname
	 */
	public String getLastname() {

		return lastname;
	}

	/**
	 * Gets the passwort.
	 *
	 * @return the passwort
	 */
	public String getPasswort() {

		return passwort;
	}

	/**
	 * Sets the address street.
	 *
	 * @param address_Street
	 *           the new address street
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public void setAddress_Street(String address_Street) throws InvalidPersonDataException {

		if (!address_Street.equals("")) {
			this.address_Street = address_Street;
		} else {
			throw new InvalidPersonDataException(2, address_Street);
		}
	}

	/**
	 * Sets the address town.
	 *
	 * @param address_Town
	 *           the new address town
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public void setAddress_Town(String address_Town) throws InvalidPersonDataException {

		if (!address_Town.equals("")) {
			this.address_Town = address_Town;
		} else {
			throw new InvalidPersonDataException(4, address_Town);
		}
	}

	/**
	 * Sets the address zip.
	 *
	 * @param address_Zip
	 *           the new address zip
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public void setAddress_Zip(String address_Zip) throws InvalidPersonDataException {

		if (!address_Zip.equals("")) {
			this.address_Zip = address_Zip;
		} else {
			throw new InvalidPersonDataException(5, address_Zip);
		}
	}

	/**
	 * Sets the firstname.
	 *
	 * @param firstname
	 *           the new firstname
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public void setFirstname(String firstname) throws InvalidPersonDataException {

		if (!firstname.equals("")) {
			this.firstname = firstname;
		} else {
			throw new InvalidPersonDataException(0, firstname);
		}
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *           the new id
	 */
	public void setId(int id) {

		this.id = id;
	}

	/**
	 * Sets the lastname.
	 *
	 * @param lastname
	 *           the new lastname
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public void setLastname(String lastname) throws InvalidPersonDataException {

		if (!lastname.equals("")) {
			this.lastname = lastname;
		} else {
			throw new InvalidPersonDataException(1, firstname);
		}
	}

	/**
	 * Sets the passwort.
	 *
	 * @param passwort
	 *           the new passwort
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public void setPasswort(String passwort) throws InvalidPersonDataException {

		if (!passwort.equals("")) {
			this.passwort = passwort;
		} else {
			throw new InvalidPersonDataException(6, "");
		}
	}
}
