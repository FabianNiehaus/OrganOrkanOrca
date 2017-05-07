package data_objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fabian Niehaus
 * Dient zur Ereignisspeicherung für den eShop
 */
public class Ereignis {

	private Person wer;
	private Date wann;
	private Typ was;
	private Artikel womit;
	private int wieviel;
	
	
	/**
	 * @param wer Person, die die Aktion durchgeführt hat
	 * @param was Typ der Aktion (EINLAGERUNG, AUSLAGERUNG, KAUF, NEU)
	 * @param womit Welcher Artikel ist betroffen
	 * @param wieviel Betroffene Stückzahl
	 */
	public Ereignis(Person wer, Typ was, Artikel womit, int wieviel) {
		super();
		this.wer = wer;
		this.was = was;
		this.womit = womit;
		this.wieviel = wieviel;
		this.wann = new Date();
	}
	
	public void setWer(Person wer) {
		this.wer = wer;
	}
	
	public Person getWer() {
		return wer;
	}
	
	public void setWas(Typ was) {
		this.was = was;
	}
	
	public Typ getTyp() {
		return was;
	}
	
	public void setWomit(Artikel womit) {
		this.womit = womit;
	}
	
	public Artikel getWomit() {
		return womit;
	}
	
	public void setWieviel(int wieviel) {
		this.wieviel = wieviel;
	}
	
	public int getWieviel() {
		return wieviel;
	}
	
	public void setWann(Date wann) {
		this.wann = wann;
	}
	
	public Date getWann() {
		return wann;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		//Formatierungsvorlage für Datum
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
				
		return dateFormat.format(wann) + " | " + wer.getId() + ": " + wer.getLastname() + " " + wer.getLastname() + " | " + was + " | " + womit.getArtikelnummer() + ": " + womit.getBezeichnung() + " | " + wieviel; 
	}
}
