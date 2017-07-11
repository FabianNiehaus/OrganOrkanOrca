package eshop.server.domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import eshop.common.data_objects.Mitarbeiter;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.LoginFailedException;
import eshop.common.exceptions.MaxIDsException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.exceptions.VectorIsEmptyException;
import eshop.server.persistence.FilePersistenceManager;
import eshop.server.persistence.PersistenceManager;

// TODO: Auto-generated Javadoc
/**
 * The Class Mitarbeiterverwaltung.
 */
public class Mitarbeiterverwaltung {

	/** The mitarbeiter. */
	private Vector<Mitarbeiter> mitarbeiter = new Vector<Mitarbeiter>();
	// Persistenz-Schnittstelle, die f�r die Details des Dateizugriffs
	/** The pm. */
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	/**
	 * Anmelden.
	 *
	 * @param id
	 *           the id
	 * @param passwort
	 *           the passwort
	 * @return the mitarbeiter
	 * @throws LoginFailedException
	 *            the login failed exception
	 */
	public Mitarbeiter anmelden(int id, String passwort) throws LoginFailedException {

		for (Mitarbeiter m : mitarbeiter) {
			if (m.getId() == id && m.getPasswort().equals(passwort)) {
				return m;
			}
		}
		throw new LoginFailedException(id);
	}

	/**
	 * Einfuegen.
	 *
	 * @param mi
	 *           the mitarbeiter
	 */
	public void einfuegen(Mitarbeiter mi) {

		mitarbeiter.add(mi);
	}

	/**
	 * Erstelle mitarbeiter.
	 *
	 * @param einMa
	 *           the ein ma
	 */
	public void erstelleMitarbeiter(Mitarbeiter einMa) {

		mitarbeiter.add(einMa);
	}

	/**
	 * Erstelle mitarbeiter.
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
	 * @return the mitarbeiter
	 * @throws MaxIDsException
	 *            the max I ds exception
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public Mitarbeiter erstelleMitarbeiter(String firstname, String lastname, String passwort, String address_Street,
			String address_Zip, String address_Town) throws MaxIDsException, InvalidPersonDataException {

		Mitarbeiter mi = new Mitarbeiter(firstname, lastname, getNextID(), passwort, address_Street, address_Zip,
				address_Town);
		mitarbeiter.add(mi);
		return mi;
	}

	/**
	 * Gets the mitarbeiter.
	 *
	 * @return the mitarbeiter
	 */
	public Vector<Mitarbeiter> getMitarbeiter() {

		return mitarbeiter;
	}

	/**
	 * Gets the next ID.
	 *
	 * @return the next ID
	 * @throws MaxIDsException
	 *            the max I ds exception
	 */
	public int getNextID() throws MaxIDsException {

		int hoechsteID = 9000;
		for (Mitarbeiter ma : mitarbeiter) {
			if (ma.getId() > hoechsteID) {
				hoechsteID = ma.getId();
			}
		}
		if (hoechsteID < 9999) {
			return hoechsteID + 1;
		} else {
			throw new MaxIDsException("Mitarbeiter");
		}
	}

	/**
	 * Lies daten.
	 *
	 * @param datei
	 *           the datei
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public void liesDaten(String datei) throws IOException, InvalidPersonDataException {

		// PersistenzManager f�r Lesevorgänge öffnen
		pm.openForReading(datei);
		Mitarbeiter mi = null;
		do {
			// Mitarbeiter-Objekt einlesen
			mi = pm.ladeMitarbeiter();
			if (mi != null) {
				// Mitarbeiter in Mitarbeiterliste einfuegen
				einfuegen(mi);
			}
		} while (mi != null);
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	/**
	 * Loesche mitarbeiter.
	 *
	 * @param einMa
	 *           the ein ma
	 */
	public void loescheMitarbeiter(Mitarbeiter einMa) {

		mitarbeiter.remove(einMa);
	}

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
		if (!mitarbeiter.isEmpty()) {
			Iterator<Mitarbeiter> iter = mitarbeiter.iterator();
			while (iter.hasNext()) {
				Mitarbeiter mi = iter.next();
				pm.speichereMitarbeiter(mi);
			}
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	/**
	 * Suche mitarbeiter.
	 *
	 * @param id
	 *           the id
	 * @return the mitarbeiter
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	public Mitarbeiter sucheMitarbeiter(int id) throws PersonNonexistantException {

		for (Mitarbeiter ma : mitarbeiter) {
			if (ma.getId() == id) {
				return ma;
			}
		}
		throw new PersonNonexistantException(id);
	}

	/**
	 * Suche mitarbeiter.
	 *
	 * @param firstname
	 *           the firstname
	 * @param lastname
	 *           the lastname
	 * @return the mitarbeiter
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	public Mitarbeiter sucheMitarbeiter(String firstname, String lastname) throws PersonNonexistantException {

		if (mitarbeiter.size() > 0) {
			for (Mitarbeiter ma : mitarbeiter) {
				if (ma.getFirstname().equals(firstname) && ma.getLastname().equals(lastname)) {
					return ma;
				}
			}
		}
		throw new PersonNonexistantException(firstname, lastname);
	}
}
