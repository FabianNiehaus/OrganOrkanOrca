package data_objects;

import java.util.LinkedHashMap;
import java.util.Map;

public class Warenkorb {
	
	/**
	 * @param id Eindeutige ID des Warenkorbs
	 */
	public Warenkorb() {
		super();
	}

	private LinkedHashMap<Artikel,Integer> artikel = new LinkedHashMap<>();
	
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
	
	public void aendereAnzahl(int pos, int anz){
		int i = 0;
		
		for(Map.Entry<Artikel, Integer> ent : artikel.entrySet()){
			
			if(i == pos-1){
				Artikel art = ent.getKey();
				artikel.remove(ent);
				artikel.put(art, anz);
			}
			
			i++;
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
		artikel = new LinkedHashMap<>();
	}
	
	public String toString(){
		String retStr = "";
		int pos = 1;
		for (Map.Entry<Artikel, Integer> ent : artikel.entrySet()){
			retStr += pos + ") " + ent.getKey().getBezeichnung() + " | " + ent.getValue() + " Stk. | á " + ent.getKey().getPreis() + "€\n";
			pos++;
		}
		return retStr;
	}
	
	public LinkedHashMap<Artikel, Integer> getArtikel(){
		return artikel;
	}
}
