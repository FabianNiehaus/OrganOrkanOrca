package domain;

import java.io.IOException;
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

		//Ereignis er;
			
		
	}
	
	public void schreibeDaten(String datei) throws IOException {
		
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
	public void ereignisErstellen(int id,Person wer, Typ was, Artikel womit, int wieviel){
		ereignisse.add(new Ereignis(id,wer, was, womit, wieviel));
	}

}
