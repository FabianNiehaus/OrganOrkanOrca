package data_objects;

import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
	
	/**
	 * @param id Eindeutige ID des Warenkorbs
	 */
	public Warenkorb() {
		super();
	}

	private HashMap<Artikel,Integer> artikel = new HashMap<>();
	
	/**
	 * Prüft, ob ein bestimmter Artikel in diesem Warenkorb liegt.
	 * @param art Zu überprüfender Artikel
	 * @return Gibt <b>true</b> zurück, wenn zu prüfender Artikel in der HAsMap artikel gespeichert ist. Sonst <b>false</b>.
	 */
	public boolean sucheArtikel(Artikel art){
		if(!artikel.isEmpty()){
			if(artikel.containsKey(art)){
				return true;
			}
			else {
				return false;
			}
		}
		return false;
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
	
	public void leereWarkenkorb(){
		for(Map.Entry<Artikel, Integer> ent : artikel.entrySet()){
			artikel.remove(ent);
		}
	}
	
	public String toString(){
		String retStr = "";
		int pos = 1;
		for (Map.Entry<Artikel, Integer> ent : artikel.entrySet()){
			retStr += pos + ") " + ent.getKey().getBezeichnung() + " | " + ent.getValue() + " Stk.\n";
		}
		return retStr;
	}
}
