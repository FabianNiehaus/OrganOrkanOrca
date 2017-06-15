package data_objects;

import java.util.LinkedHashMap;
import java.util.Map;

import domain.exceptions.ArticleAlreadyInBasketException;
import domain.exceptions.ArticleStockNotSufficientException;

/**
 * @author FabianNiehaus
 *
 */
public class Warenkorb {
	
	public Warenkorb() {
		super();
	}

	private Map<Artikel,Integer> artikel = new LinkedHashMap<>();
	
	/**
	 * Prueft, ob ein bestimmter Artikel in diesem Warenkorb liegt.
	 * @param art Zu ueberpruefender Artikel
	 * @return Gibt <b>true</b> zurueck, wenn zu pruefender Artikel in der HAsMap artikel gespeichert ist. Sonst <b>false</b>.
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
	
	/**
	 * Dient zur Änderung der Anzahl eines Artikels im Warenkorb. Der Zugriff erfolgt (nutzerfreundlich) ueber die Position des Artikels im Warenkorb.
	 * @param pos Position des Artikels im Warenkorb
	 * @param anz Neue Anzahl (muss größer 0 sein)
	 * @throws Nicht genug Artikel auf Lager
	 */
	public void aendereAnzahl(int pos, int anz) throws ArticleStockNotSufficientException{
		int i = 0;
		
		for(Map.Entry<Artikel, Integer> ent : artikel.entrySet()){
			
			if(i == pos){
				Artikel art = ent.getKey();
				artikel.remove(ent.getKey());
				if (anz > 0){
					pruefeBestand(art, anz);
					artikel.put(art, anz);
				}
				
				break;
			}
			
			i++;
		}
	}
	
	/**
	 * Legt einen Artikel im Warenkorb ab
	 * @param art Gewuenschter Artikel
	 * @param anz Gewuenschte Anzahl (muss größer 0 sein)
	 * @throws ArticleAlreadyInBasketException 
	 * @throws Nicht genug Artikel auf Lager
	 */
	public void speichereArtikel(Artikel art, int anz) throws ArticleStockNotSufficientException, ArticleAlreadyInBasketException{
		if(!sucheArtikel(art)){
			if(anz > 0){
				pruefeBestand(art, anz);
				artikel.put(art, anz);
			} 
		} else {
			// TODO Stattdessen Anzahl im WK erhöhen
			throw new ArticleAlreadyInBasketException(art.getBezeichnung());
		}
	}
	
	/**
	 * Entfernt einen Artikel aus dem Warenkorb
	 * @param art
	 */
	public void loescheArtikel(Artikel art){
		if(sucheArtikel(art)){
			artikel.remove(art, artikel.get(art));
		}
	}
	
	/**
	 * Leert Warenkorb, indem Artikelliste verworfen wird
	 */
	public void leereWarkenkorb(){
		artikel.clear();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String retStr = "";
		int pos = 1;
		for (Map.Entry<Artikel, Integer> ent : artikel.entrySet()){
			retStr += pos + ") " + ent.getKey().getBezeichnung() + " | " + ent.getValue() + " Stk. | á " + ent.getKey().getPreis() + "€\n";
			pos++;
		}
		return retStr;
	}
	
	/**
	 * @return Gibt die LinkedHashMap mit Artikeln und Anzahl zurueck
	 */
	public Map<Artikel, Integer> getArtikel(){
		return artikel;
	}
	
	/**
	 * Prueft, ob genug Bestand von einem Artikel verfuegbar ist 
	 * @param art Gewuenschter Artikel
	 * @param anz Gewuenschte Anzahl
	 */
	private void pruefeBestand(Artikel art, int anz) throws ArticleStockNotSufficientException{
		if (art.getBestand() < anz){
			throw new ArticleStockNotSufficientException(art, anz);
		}
	}
	
	public void setArtikel(Map<Artikel,Integer> map){
		for(Map.Entry<Artikel, Integer> ent : map.entrySet()){
			artikel.clear();
			artikel.put(ent.getKey(), ent.getValue());
		}
	}
	
	public void copy(Warenkorb wk){
		artikel.putAll(wk.getArtikel());
	}
}
