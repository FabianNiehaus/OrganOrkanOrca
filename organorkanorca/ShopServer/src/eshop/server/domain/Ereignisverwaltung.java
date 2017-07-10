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

	Artikelverwaltung				av;
	private Vector<Ereignis>	ereignisse	= new Vector<Ereignis>();
	Kundenverwaltung				kv;
	Mitarbeiterverwaltung		mv;
	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	public Ereignisverwaltung(Kundenverwaltung kv, Mitarbeiterverwaltung mv, Artikelverwaltung av) {
		this.kv = kv;
		this.av = av;
		this.mv = mv;
	}

	public void einfuegen(Ereignis er) {

		ereignisse.add(er);
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

		Ereignis er = new Ereignis(getNextID(), wer, was, womit, wieviel, new Date());
		ereignisse.add(er);
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
		for (Ereignis er : ereignisse) {
			if (er.getId() > hoechsteID) {
				hoechsteID = er.getId();
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
				Vector<Object> info = pm.ladeEreignis();
				Person p = null;
				int personennummer = (int) info.elementAt(1);
				// wenn Person ein Kunde ist (Unterscheidung durch
				// Personennummern)
				if (personennummer > 1000 && personennummer < 9000) {
					p = kv.sucheKunde(personennummer);
					// wenn Person ein Mitarbeiter ist
				} else if (personennummer >= 9000 && personennummer < 10000) {
					p = mv.sucheMitarbeiter(personennummer);
				} else {
					throw new PersonNonexistantException(personennummer);
				}
				// Artikel wird durch Artikelnummer gesucht
				Artikel art = null;
				art = av.sucheArtikel((int) info.elementAt(3));
				// Datum wird eingelesen und von String zu date
				Date date = null;
				DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
				try {
					date = formatter.parse((String) info.elementAt(5));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// Ereignis wird aus dem Vector Elementen erstellt
				er = new Ereignis((int) info.elementAt(0), p, (Typ) info.elementAt(2), art, (int) info.elementAt(4), date);
				// Ereignisse in die Ereignisliste einfuegen
				einfuegen(er);
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
