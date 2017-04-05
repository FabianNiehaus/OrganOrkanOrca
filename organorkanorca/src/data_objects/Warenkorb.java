package data_objects;

import java.util.LinkedHashMap;

public class Warenkorb {
	
	/**
	 * @param id Eindeutige ID des Warenkorbs
	 */
	public Warenkorb(int id) {
		super();
		this.id = id;
	}

	private LinkedHashMap<Artikel,Integer> artikel;
	private int id;
	
	/**
	 * Prüft, ob ein bestimmter Artikel in diesem Warenkorb liegt.
	 * @param art Zu überprüfender Artikel
	 * @return Gibt <b>true</b> zurück, wenn zu prüfender Artikel in der HAsMap artikel gespeichert ist. Sonst <b>false</b>.
	 */
	public boolean sucheArtikel(Artikel art){
		if(artikel.containsKey(art)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void aendereAnzahl(Artikel art, int anz){
		if(sucheArtikel(art)){
			artikel.remove(art, artikel.get(art));
			artikel.put(art, anz);
		}
	}
	
	public void speichereArtikel(Artikel art, int anz){
		if(!sucheArtikel(art)){
			artikel.put(art, anz);
		}
	}
	
	public void loescheArtikel(Artikel art){
		if(sucheArtikel(art)){
			artikel.remove(art, artikel.get(art));
		}
	}

	public int getId() {
		return id;
	}
	
	/* ID soll nach Erzeugung nicht verändert werden
	 * 
	public void setId(int id) {
		this.id = id;
	}
	*/
}
