package domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import data_objects.Artikel;
import data_objects.Ereignis;
import data_objects.Kunde;
import data_objects.Person;
import data_objects.Typ;
import persistence.FilePersistenceManager;
import persistence.PersistenceManager;

/**
 * @author Fabian Niehaus
 * Klasse zur Verwaltung von Ereignissen
 */
public class Ereignisverwaltung {

	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();
	
	private Vector<Ereignis> ereignisse = new Vector<Ereignis>();
	
	public void liesDaten(String datei) throws IOException {
		// PersistenzManager f�r Lesevorgänge öffnen
		pm.openForReading(datei);

		Ereignis er;
		
		
		try{
			do {
			
				Vector<Object> info = pm.ladeEreignis();
				
				er = new Ereignis((int) info.elementAt(0), (int) info.elementAt(1), (Typ) info.elementAt(2), (int) info.elementAt(3), (int) info.elementAt(4), (String) info.elementAt(5)); 
				
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
		ereignisse.add(new Ereignis(wer, was, womit, wieviel));
	}

}
