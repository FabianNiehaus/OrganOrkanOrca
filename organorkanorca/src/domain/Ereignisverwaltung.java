package domain;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import data_objects.Artikel;
import data_objects.Ereignis;
import data_objects.Person;
import data_objects.Typ;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.PersonNonexistantException;
import persistence.FilePersistenceManager;
import persistence.PersistenceManager;

/**
 * @author Fabian Niehaus
 * Klasse zur Verwaltung von Ereignissen
 */
public class Ereignisverwaltung {
	
	Kundenverwaltung kv;
	Mitarbeiterverwaltung mv;
	Artikelverwaltung av;
	
	public Ereignisverwaltung(Kundenverwaltung kv, Mitarbeiterverwaltung mv, Artikelverwaltung av){
		this.kv = kv;
		this.av = av;
		this.mv = mv;
	}

	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();
	
	private Vector<Ereignis> ereignisse = new Vector<Ereignis>();
	
	public void liesDaten(String datei) throws IOException, ArticleNonexistantException, PersonNonexistantException {
		// PersistenzManager f�r Lesevorgänge öffnen
		pm.openForReading(datei);

		Ereignis er;
		
		try{
			do {
			
				Vector<Object> info = pm.ladeEreignis();
				
				Person p = null;
				int personennummer = (int) info.elementAt(1);
				
				if (personennummer > 1000 && personennummer < 9000){
					p = kv.sucheKunde(personennummer);
				} else if (personennummer >= 9000 && personennummer < 10000 ) {
					p = mv.suchMitarbeiter(personennummer);
				} else {
					throw new PersonNonexistantException(personennummer);
				}
				
				Artikel art = null;
				art = av.sucheArtikel((int) info.elementAt(3));
				
				er = new Ereignis((int) info.elementAt(0), p, (Typ) info.elementAt(2), art, (int) info.elementAt(4), (Date) info.elementAt(5)); 
				
				// Ereignisse in die Ereignisliste einfügen
				einfuegen(er);
				
			} while (er.getId() != 0);
		} catch (NullPointerException npe){
			
		}

		// Persistenz-Schnittstelle wieder schließen
		pm.close();
				
	}
	
	public void schreibeDaten(String datei) throws IOException {
		// PersistenzManager für Schreibvorgänge öffnen
		pm.openForWriting(datei);

		if (!ereignisse.isEmpty()) {
			Iterator<Ereignis> iter = ereignisse.iterator();
			while (iter.hasNext()) {
				Ereignis er = (Ereignis) iter.next();
				pm.speichereEreignis(er);				
			}
		}
		
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	public void einfuegen(Ereignis er) {
		ereignisse.add(er);
	}
	/**
	 * Gibt alle gespeicherten Ereignisse aus
	 * @return Ereignisse
	 */
	public Vector<Ereignis> getEreignisse() {
		return ereignisse;
	}
	
	/**
	 * Erstellt und speichert ein neues Ereignis
	 * @param wer Person, die die Aktion durchgeführt hat
	 * @param was Typ der Aktion (EINLAGERUNG, AUSLAGERUNG, KAUF, NEU)
	 * @param womit Welcher Artikel ist betroffen
	 * @param wieviel Betroffene Stückzahl
	 */
	public void ereignisErstellen(Person wer, Typ was, Artikel womit, int wieviel){
		ereignisse.add(new Ereignis(getNextID(), wer, was, womit, wieviel, new Date()));
	}
	
	public int getNextID() {
		int hoechsteID = 0;
		for(Ereignis er : ereignisse){
			if(er.getId() > hoechsteID){
				hoechsteID = er.getId();
			}
		}		
		return hoechsteID+1;
	}

}
