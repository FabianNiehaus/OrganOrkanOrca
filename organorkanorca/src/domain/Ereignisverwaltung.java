package domain;

import java.util.Vector;

import data_objects.Artikel;
import data_objects.Ereignis;
import data_objects.Person;
import data_objects.Typ;

/**
 * @author Fabian Niehaus
 * Klasse zur Verwaltung von Ereignissen
 */
public class Ereignisverwaltung {

	private Vector<Ereignis> ereignisse = new Vector<>();
	
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
