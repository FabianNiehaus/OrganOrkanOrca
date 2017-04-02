package data_objects;

import java.util.Hashtable;

public class Warenkorb {
	
	private Hashtable<Integer, Artikel> artikel = new Hashtable<Integer, Artikel>();
	private int Anzahl;
	
	/**
	 * @param artikel Artikel im Warenkorb
	 * @param anzahl Anzahl der Artikel im Warenkorb
	 */
	public Warenkorb(Hashtable<Integer, Artikel> artikel, int anzahl) {
		super();
		this.artikel = artikel;
		Anzahl = anzahl;
	}

	public Hashtable<Integer, Artikel> getArtikel() {
		return artikel;
	}

	public void setArtikel(Hashtable<Integer, Artikel> artikel) {
		this.artikel = artikel;
	}

	public int getAnzahl() {
		return Anzahl;
	}

	public void setAnzahl(int anzahl) {
		Anzahl = anzahl;
	}
				
}
