package eshop.server.domain;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Person;
import eshop.common.data_objects.Typ;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.server.persistence.FilePersistenceManager;
import eshop.server.persistence.PersistenceManager;

// TODO: Auto-generated Javadoc
/**
 * The Class Ereignisverwaltung.
 */
public class Ereignisverwaltung {

	/** The Artikelverwaltung. */
	private Artikelverwaltung		av;
	
	/** The ereignisse. */
	private Vector<Ereignis>		ereignisse;
	
	/** The Kundenverwaltung. */
	private Kundenverwaltung		kv;
	
	/** The Mitarbeiterverwaltung. */
	private Mitarbeiterverwaltung	mv;
	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	/** The pm. */
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	/**
	 * Einfuegen.
	 *
	 * @param er
	 *           the er
	 */
	public void einfuegen(Ereignis er) {

		if (ereignisse == null) {
			ereignisse = new Vector<Ereignis>(1);
			ereignisse.add(0, null);
		}
		if (ereignisse.elementAt(0) == null) {
			ereignisse.add(0, er);
			ereignisse.remove(1);
		} else {
			ereignisse.add(er);
		}
	}

	/**
	 * Ereignis erstellen.
	 *
	 * @param wer
	 *           the wer
	 * @param was
	 *           the was
	 * @param womit
	 *           the womit
	 * @param wieviel
	 *           the wieviel
	 * @return the ereignis
	 */
	public Ereignis ereignisErstellen(Person wer, Typ was, Artikel womit, int wieviel) {

		String wer_Name = wer.getFirstname().substring(0, 1) + ". " + wer.getLastname();
		Ereignis er = new Ereignis(getNextID(), wer.getId(), wer_Name, was, womit.getArtikelnummer(),
				womit.getBezeichnung(), wieviel, new Date());
		einfuegen(er);
		return er;
	}

	/**
	 * Gets the ereignisse.
	 *
	 * @return the ereignisse
	 */
	public Vector<Ereignis> getEreignisse() {

		return ereignisse;
	}

	/**
	 * Gets the next ID.
	 *
	 * @return the next ID
	 */
	public int getNextID() {

		int hoechsteID = 0;
		if (ereignisse.get(0) != null) {
			for (Ereignis er : ereignisse) {
				if (er.getId() > hoechsteID) {
					hoechsteID = er.getId();
				}
			}
		}
		return hoechsteID + 1;
	}

	/**
	 * Lies daten.
	 *
	 * @param datei
	 *           the datei
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	public void liesDaten(String datei) throws IOException, ArticleNonexistantException, PersonNonexistantException {

		// PersistenzManager f�r Lesevorgänge öffnen
		pm.openForReading(datei);
		Ereignis er;
		try {
			do {
				er = pm.ladeEreignis();
				if (er != null) einfuegen(er);
			} while (er.getId() != 0);
		} catch (NullPointerException npe) {
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
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
		if (!ereignisse.isEmpty()) {
			Iterator<Ereignis> iter = ereignisse.iterator();
			while (iter.hasNext()) {
				Ereignis er = iter.next();
				pm.speichereEreignis(er);
			}
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	/**
	 * Suche ereignis.
	 *
	 * @param ereignisID
	 *           the ereignis ID
	 * @return the ereignis
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 */
	public Ereignis sucheEreignis(int ereignisID) throws ArticleNonexistantException {

		for (Ereignis er : ereignisse) {
			if (er.getId() == ereignisID) {
				return er;
			}
		}
		throw new ArticleNonexistantException(ereignisID);
	}
}
