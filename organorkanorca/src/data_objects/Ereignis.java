package data_objects;

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
}
