package domain;

import java.util.Vector;

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
	public Vector<Artikel> alleArtikelAusgeben(){
		return av.getArtikel();
	}

	/**
	 * @return Alle in der Kundenverwaltung gespeicherten Kunden
	 */
	public Vector<Kunde> alleKundenAusgeben(){
		return kv.getKunden();
	}
	
	/**
	 * @return Alle in der Mitarbeiterverwaltung gespeicherten Mitarbeiter
	 */
	public Vector<Mitarbeiter> alleMitarbeiterAusgeben(){
		return mv.getMitarbeiter();
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
