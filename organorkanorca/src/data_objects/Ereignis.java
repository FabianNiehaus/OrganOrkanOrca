package data_objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ereignis {

	private Person wer;
	private Date wann;
	private Typ was;
	private Artikel womit;
	private int wieviel;
	
	public Ereignis(Person wer, Typ was, Artikel womit, int wieviel) {
		super();
		this.wer = wer;
		this.was = was;
		this.womit = womit;
		this.wieviel = wieviel;
		this.wann = new Date();
	}
	
	public String toString(){
		//Formatierungsvorlage für Datum
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
				
		return dateFormat.format(wann) + " | " + wer.getId() + ": " + wer.getLastname() + " " + wer.getLastname() + " | " + was + " | " + womit.getArtikelNr() + ": " + womit.getBezeichnung() + " | " + wieviel; 
	}
}
