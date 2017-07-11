/**
 * 
 */
package eshop.common.data_objects;

import java.io.Serializable;

import eshop.common.exceptions.InvalidPersonDataException;

// TODO: Auto-generated Javadoc
/**
 * The Class Kunde.
 */
public class Kunde extends Person implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -1133891813422452141L;
	
	/** The wk. */
	private Warenkorb				wk;

	/**
	 * Instantiates a new kunde.
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
	public Kunde(String firstname, String lastname, int id, String passwort, String address_Street, String address_Zip,
			String address_Town) throws InvalidPersonDataException {
		super(firstname, lastname, id, passwort, address_Street, address_Zip, address_Town);
	}

	/**
	 * Instantiates a new kunde.
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
	 * @param wk
	 *           the wk
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public Kunde(String firstname, String lastname, int id, String passwort, String address_Street, String address_Zip,
			String address_Town, Warenkorb wk) throws InvalidPersonDataException {
		super(firstname, lastname, id, passwort, address_Street, address_Zip, address_Town);
		this.wk = wk;
	}

	/**
	 * Gets the warenkorb.
	 *
	 * @return the warenkorb
	 */
	public Warenkorb getWarenkorb() {

		return wk;
	}

	/**
	 * Sets the warenkorb.
	 *
	 * @param warenkorb
	 *           the new warenkorb
	 */
	public void setWarenkorb(Warenkorb warenkorb) {

		this.wk = warenkorb;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return id + ": " + firstname + " " + lastname + " | " + address_Street + " ," + address_Zip + " " + address_Town;
	}
}
