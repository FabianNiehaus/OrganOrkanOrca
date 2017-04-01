package domain;

import java.util.Vector;
import data_objects.Artikel;

public class Artikelverwaltung {

	/**
	 * @param artikel Verwaltete Artikel
	 */
	public Artikelverwaltung(Vector<Artikel> artikel) {
		super();
		this.artikel = artikel;
	}

	private Vector<Artikel> artikel;

	public Vector<Artikel> getArtikel() {
		return artikel;
	}

	public void setArtikel(Vector<Artikel> artikel) {
		this.artikel = artikel;
	}
	
	
}
