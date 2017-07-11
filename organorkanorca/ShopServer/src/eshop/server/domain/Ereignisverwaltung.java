package eshop.server.domain;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

/**
 * @author Fabian Niehaus Klasse zur Verwaltung von Ereignissen
 */
public class Ereignisverwaltung {

	private Artikelverwaltung				av;
	private Vector<Ereignis>	ereignisse;
	private Kundenverwaltung				kv;
	private Mitarbeiterverwaltung		mv;
	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	public void einfuegen(Ereignis er) {

		if (ereignisse == null) {
			ereignisse	= new Vector<Ereignis>(1);
			ereignisse.add(0, null);
		}
		
		if(ereignisse.elementAt(0) == null){
			ereignisse.add(0, er);
			ereignisse.remove(1);
		} else {
			ereignisse.add(er);
		}
	}

	/**
	 * Erstellt und speichert ein neues Ereignis
	 * 
	 * @param wer
	 *           Person, die die Aktion durchgefuehrt hat
	 * @param was
	 *           Typ der Aktion (EINLAGERUNG, AUSLAGERUNG, KAUF, NEU)
	 * @param womit
	 *           Welcher Artikel ist betroffen
	 * @param wieviel
	 *           Betroffene Stueckzahl
	 */
	public Ereignis ereignisErstellen(Person wer, Typ was, Artikel womit, int wieviel) {

		String wer_Name = wer.getFirstname().substring(0, 1) + ". " + wer.getLastname();
		Ereignis er = new Ereignis(getNextID(), wer.getId(), wer_Name, was, womit.getArtikelnummer(),
				womit.getBezeichnung(), wieviel, new Date());
		
		einfuegen(er);
		
		return er;
	}

	/**
	 * Gibt alle gespeicherten Ereignisse aus
	 * 
	 * @return Ereignisse
	 */
	public Vector<Ereignis> getEreignisse() {

		return ereignisse;
	}

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

	public void liesDaten(String datei) throws IOException, ArticleNonexistantException, PersonNonexistantException {

		// PersistenzManager f�r Lesevorgänge öffnen
		pm.openForReading(datei);
		Ereignis er;
		try {
			do {
				er = pm.ladeEreignis();
				if(er != null)einfuegen(er);
			} while (er.getId() != 0);
		} catch (NullPointerException npe) {
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

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

	public Ereignis sucheEreignis(int ereignisID) throws ArticleNonexistantException {

		for (Ereignis er : ereignisse) {
			if (er.getId() == ereignisID) {
				return er;
			}
		}
		throw new ArticleNonexistantException(ereignisID);
	}
}
