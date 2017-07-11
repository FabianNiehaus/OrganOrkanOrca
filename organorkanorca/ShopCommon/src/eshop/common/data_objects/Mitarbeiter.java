package eshop.common.data_objects;

import java.io.Serializable;

import eshop.common.exceptions.InvalidPersonDataException;

// TODO: Auto-generated Javadoc
/**
 * The Class Mitarbeiter.
 */
public class Mitarbeiter extends Person implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3719267568371225895L;

	/**
	 * Instantiates a new mitarbeiter.
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
	public Mitarbeiter(String firstname, String lastname, int id, String passwort, String address_Street,
			String address_Zip, String address_Town) throws InvalidPersonDataException {
		super(firstname, lastname, id, passwort, address_Street, address_Zip, address_Town);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return id + ": " + firstname + " " + lastname;
	}
}
