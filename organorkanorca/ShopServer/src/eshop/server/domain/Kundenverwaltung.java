package eshop.server.domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Person;
import eshop.common.data_objects.Warenkorb;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.LoginFailedException;
import eshop.common.exceptions.MaxIDsException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.server.persistence.FilePersistenceManager;
import eshop.server.persistence.PersistenceManager;

// TODO: Auto-generated Javadoc
/**
 * The Class Kundenverwaltung.
 */
public class Kundenverwaltung {

	/** The kunden. */
	private Vector<Kunde> kunden = new Vector<Kunde>();
	// Persistenz-Schnittstelle, die f�r die Details des Dateizugriffs
	/** The pm. */
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	/**
	 * Instantiates a new kundenverwaltung.
	 *
	 * @param wv
	 *           the warenkorbverwaltung
	 */
	public Kundenverwaltung(Warenkorbverwaltung wv) {
	}

	/**
	 * Anmelden.
	 *
	 * @param id
	 *           the id
	 * @param passwort
	 *           the passwort
	 * @return the kunde
	 * @throws LoginFailedException
	 *            the login failed exception
	 */
	public Kunde anmelden(int id, String passwort) throws LoginFailedException {

		for (Kunde k : kunden) {
			if (k.getId() == id && k.getPasswort().equals(passwort)) {
				return k;
			}
		}
		throw new LoginFailedException(id);
	}

	/**
	 * Einfuegen.
	 *
	 * @param ku
	 *           the kunde
	 */
	public void einfuegen(Kunde ku) {

		kunden.add(ku);
	}

	/**
	 * Erstelle kunde.
	 *
	 * @param firstname
	 *           the firstname
	 * @param lastname
	 *           the lastname
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
	 * @return the kunde
	 * @throws MaxIDsException
	 *            the max I ds exception
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public Kunde erstelleKunde(String firstname, String lastname, String passwort, String address_Street,
			String address_Zip, String address_Town, Warenkorb wk) throws MaxIDsException, InvalidPersonDataException {

		Kunde ku = new Kunde(firstname, lastname, getNextID(), passwort, address_Street, address_Zip, address_Town, wk);
		kunden.add(ku);
		return ku;
	}

	/**
	 * Gets the kunden.
	 *
	 * @return the kunden
	 */
	public Vector<Kunde> getKunden() {

		return kunden;
	}

	/**
	 * Gets the next ID.
	 *
	 * @return the next ID
	 * @throws MaxIDsException
	 *            the max I ds exception
	 */
	public int getNextID() throws MaxIDsException {

		int hoechsteID = 1000;
		for (Kunde ku : kunden) {
			if (ku.getId() > hoechsteID) {
				hoechsteID = ku.getId();
			}
		}
		if (hoechsteID < 8999) {
			return hoechsteID + 1;
		} else {
			throw new MaxIDsException("Kunden");
		}
	}

	/**
	 * Gib warenkorb von kunde.
	 *
	 * @param ku
	 *           the kunde
	 * @return the warenkorb
	 */
	public Warenkorb gibWarenkorbVonKunde(Person ku) {

		return ((Kunde) ku).getWarenkorb();
	}

	/**
	 * Lies daten.
	 *
	 * @param datei
	 *           the datei
	 * @param wv
	 *           the warenkorbverwaltung
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public void liesDaten(String datei, Warenkorbverwaltung wv) throws IOException, InvalidPersonDataException {

		// PersistenzManager f�r Lesevorgänge öffnen
		pm.openForReading(datei);
		Kunde ku = null;
		do {
			// Kunde-Objekt einlesen
			ku = pm.ladeKunde();
			if (ku != null) {
				ku.setWarenkorb(wv.erstelleWarenkorb());
				// Mitarbeiter in Mitarbeiterliste einfuegen
				einfuegen(ku);
			}
		} while (ku != null);
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	/**
	 * Loesche kunde.
	 *
	 * @param einKunde
	 *           the ein kunde
	 */
	public void loescheKunde(Kunde einKunde) {

		kunden.remove(einKunde);
	}
	/*
	 * Nicht verwendet public void aendereKunde(Kunde einKunde) { }
	 */
	/*
	 * Nicht verwendet public void pruefeDublikate() { }
	 */

	/**
	 * Schreibe daten.
	 *
	 * @param datei
	 *           the datei
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void schreibeDaten(String datei) throws IOException {

		// PersistenzManager fuer Schreibvorgänge öffnen
		pm.openForWriting(datei);
		if (!kunden.isEmpty()) {
			Iterator<Kunde> iter = kunden.iterator();
			while (iter.hasNext()) {
				Kunde ku = iter.next();
				pm.speichereKunde(ku);
			}
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	/**
	 * Suche kunde.
	 *
	 * @param id
	 *           the id
	 * @return the kunde
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	public Kunde sucheKunde(int id) throws PersonNonexistantException {

		for (Kunde ku : kunden) {
			if (ku.getId() == id) {
				return ku;
			}
		}
		throw new PersonNonexistantException(id);
	}

	/**
	 * Suche kunde.
	 *
	 * @param firstname
	 *           the firstname
	 * @param lastname
	 *           the lastname
	 * @return the kunde
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	public Kunde sucheKunde(String firstname, String lastname) throws PersonNonexistantException {

		for (Kunde ku : kunden) {
			if (ku.getFirstname().equals(firstname) && ku.getLastname().equals(lastname)) {
				return ku;
			}
		}
		throw new PersonNonexistantException(firstname, lastname);
	}

	/**
	 * Weise warenkorbzu.
	 *
	 * @param ku
	 *           the kunde
	 * @param wk
	 *           the wk
	 */
	public void weiseWarenkorbzu(Kunde ku, Warenkorb wk) {

		ku.setWarenkorb(wk);
	};
}
