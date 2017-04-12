package domain;

import java.util.Vector;

import data_objects.Artikel;
import data_objects.Ereignis;
import data_objects.Person;
import data_objects.Typ;

public class Ereignisverwaltung {

	private Vector<Ereignis> ereignisse = new Vector<>();
	
	public Vector<Ereignis> getEreignisse() {
		return ereignisse;
	}

	public void ereignisErstellen(Person wer, Typ was, Artikel womit, int wieviel){
		ereignisse.add(new Ereignis(wer, was, womit, wieviel));
	}

}
