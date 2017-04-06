package domain;

import java.util.HashMap;

import util.AccessControl;

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
	
	//private HashMap<Integer,? extends Person> nutzerzuordnung;
	//-1 = nicht angemeldet, 0 = user, 1 = mitarbeiter
	private byte userClass = -1;
	
	public String anmelden(String firstname, String lastname){
		if(mv.sucheMitarbeiter(firstname, lastname) != null){
			userClass = 1;
			return "Angemeldet als Mitarbeiter " + firstname + " " + lastname;
			
		} else if(kv.sucheKunde(firstname, lastname) != null){
			userClass = 0;
			return "Angemeldet als Kunde " + firstname + " " + lastname;
			
		} else {
			userClass = -1;
			return "Keine Kunde / Mitarbeiter für Anmeldedaten " + firstname + " " + lastname + " gefunden!";
		}
	}
	
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
	
	/**
	 * @return Alle in der Warenkorbverwaltung gespeicherten Warenkörbe
	 */
	public void alleWarenkoerbeAusgeben(){
		for (Warenkorb w : wv.getWarenkoerbe()){
			w.toString();
		}
	}
	
	
}
