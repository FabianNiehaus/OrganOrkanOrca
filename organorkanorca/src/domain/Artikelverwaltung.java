package domain;

import java.util.Vector;
import data_objects.Artikel;

public class Artikelverwaltung {

	/**
	 * 
	 */
	public Artikelverwaltung() {
		super();
	}
	

	/**
	 * @param artikel Verwaltete Artikel
	 */
	public Artikelverwaltung(Vector<Artikel> artikel) {
		super();
		this.artikel = artikel;
	}

	private Vector<Artikel> artikel;
	
	/**
	 * @param bezeichnung
	 * @param artikelnummer
	 * @param bestand
	 * @param preis
	 * @return Gibt <b>0</b> zurück, wenn Artikel erfolgreich hinzugefügt wurde. Gibt <b>-1</b> zurück, wenn ein Fehler auftritt.
	 */
	public int artikelHinzufuegen(String bezeichnung, int artikelnummer, int bestand, double preis){
		if (!artikelnummerVorhanden(artikelnummer)){
			artikel.add(new Artikel(bezeichnung,artikelnummer,bestand,preis));
			return 0;
		}
		else {
			return -1;
		}
	}
	
	
	/**
	 * @param artikelnummer Gewünschte Artikelnummer
	 * @return Gibt <b>true</b> zurück, wenn Artikelnummer existiert. Gibt <b>false</b> zurück, wenn Artikelnummer noch nicht existiert.
	 */
	public boolean artikelnummerVorhanden(int artikelnummer){
		for (Artikel a : artikel){
			if(a.getArtikelNr() == artikelnummer){
				return true;
			}
		}
		return false;
	}
	
	public Vector<Artikel> getArtikel() {
		return artikel;
	}

	public void setArtikel(Vector<Artikel> artikel) {
		this.artikel = artikel;
	}
	
	
}
