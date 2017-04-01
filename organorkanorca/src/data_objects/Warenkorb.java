package data_objects;

import java.util.Vector;

public class Warenkorb {
	
	private Vector<Artikel> artikel;
	private int Anzahl;
	
	/**
	 * @param artikel Artikel im Warenkorb
	 * @param anzahl Anzahl der Artikel im Warenkorb
	 */
	public Warenkorb(Vector<Artikel> artikel, int anzahl) {
		super();
		this.artikel = artikel;
		Anzahl = anzahl;
	}

	public Vector<Artikel> getArtikel() {
		return artikel;
	}

	public void setArtikel(Vector<Artikel> artikel) {
		this.artikel = artikel;
	}

	public int getAnzahl() {
		return Anzahl;
	}

	public void setAnzahl(int anzahl) {
		Anzahl = anzahl;
	}
				
}
