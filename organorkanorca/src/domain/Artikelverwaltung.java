package domain;

import java.util.Vector;
import data_objects.Artikel;

public class Artikelverwaltung {
	
	public Artikelverwaltung(){
		artikel.add(new Artikel("Orkan", 2, 5, 99999));
		artikel.add(new Artikel("Orca", 3, 100, 5729.45));
		artikel.add(new Artikel("Organ", 1, 2, 1000.00));
	}

	private Vector<Artikel> artikel = new Vector<Artikel>(0);
	
	public Vector<Artikel> getArtikel() {
		return artikel;
	}

	public void setArtikel(Vector<Artikel> artikel) {
		this.artikel = artikel;
	}
	
	
}
