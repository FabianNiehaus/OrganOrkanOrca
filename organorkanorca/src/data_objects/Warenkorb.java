package data_objects;

import java.util.Hashtable;

import util.Pair;

public class Warenkorb {
	
	private Vektor<Pair<Artikel,Integer>> artikel = new Hashtable<Integer,  Pair<Artikel,Integer>>();
	private int id;
	
	
	
	public void setArtikelAnzahl(Integer pos, Integer anz){
		if (artikel.containsKey(pos)){
			artikel.get(pos).setValue(anz);
		}
	}

	/**
	 * @param pos Position des Artikels im Warenkorb
	 * @return Gibt die Anzahl des Artikels an der angegebenen Position an. Ausgabe -1 bedeutet, dass die Position nicht exisitert.
	 */
	public int getArtikelAnzahl(Integer pos){
		if (artikel.containsKey(pos)){
			return artikel.get(pos).getValue();
		}
		return -1;
	}

	public Hashtable<Integer,  Pair<Artikel,Integer>> getArtikel() {
		return artikel;
	}

	public void setArtikel(Hashtable<Integer,  Pair<Artikel,Integer>> artikel) {
		this.artikel = artikel;
	}

	public int getAnzahl() {
		return Anzahl;
	}

	public void setAnzahl(int anzahl) {
		Anzahl = anzahl;
	}			
}
