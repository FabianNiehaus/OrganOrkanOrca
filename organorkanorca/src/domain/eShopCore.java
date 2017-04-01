package domain;

import data_objects.*;

public class eShopCore {

	/**
	 * @param av Artikelvewaltung
	 * @param kv Kundenverwaltung
	 * @param mv Mitarbeiterverwaltung
	 * @param wv Warenkorbverwaltung
	 */
	public eShopCore(Artikelverwaltung av, Kundenverwaltung kv, Mitarbeiterverwaltung mv, Warenkorbverwaltung wv) {
		super();
		this.av = av;
		this.kv = kv;
		this.mv = mv;
		this.wv = wv;
	}
	
	private Artikelverwaltung av;
	private Kundenverwaltung kv;
	private Mitarbeiterverwaltung mv;
	private Warenkorbverwaltung wv;
	
	/**
	 * @return Alle in der Artikelverwaltung gespeicherten Artikel
	 */
	public void alleArtikelAusgeben(){
		for (Artikel a : av.getArtikel()){
			a.toString();
		}
	}

	/**
	 * @return Alle in der Kundenverwaltung gespeicherten Kunden
	 */
	public void alleKundenAusgeben(){
		for (Kunde k : kv.getKunden()){
			k.toString();
		}
	}
	
	/**
	 * @return Alle in der Mitarbeiterverwaltung gespeicherten Mitarbeiter
	 */
	public void alleMitarbeiterAusgeben(){
		for (Mitarbeiter m : mv.getMitarbeiter()){
			m.toString();
		}
	}
	
	public Artikelverwaltung getAv() {
		return av;
	}
	public void setAv(Artikelverwaltung av) {
		this.av = av;
	}
	public Kundenverwaltung getKv() {
		return kv;
	}
	public void setKv(Kundenverwaltung kv) {
		this.kv = kv;
	}
	public Mitarbeiterverwaltung getMv() {
		return mv;
	}
	public void setMv(Mitarbeiterverwaltung mv) {
		this.mv = mv;
	}
	public Warenkorbverwaltung getWv() {
		return wv;
	}
	public void setWv(Warenkorbverwaltung wv) {
		this.wv = wv;
	}
	
}
