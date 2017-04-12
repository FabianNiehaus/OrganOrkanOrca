package domain;

import java.util.Vector;

import data_objects.Artikel;
import data_objects.Kunde;
import data_objects.Mitarbeiter;
import data_objects.Person;
import data_objects.Warenkorb;
import domain.exceptions.LoginFailedException;

public class eShopCore {

	private Artikelverwaltung av;
	private Kundenverwaltung kv;
	private Mitarbeiterverwaltung mv;
	private Warenkorbverwaltung wv;
	
	/**
	 */
	public eShopCore() {
		super();
		av = new Artikelverwaltung();
		kv = new Kundenverwaltung();
		mv = new Mitarbeiterverwaltung();
		wv = new Warenkorbverwaltung();
	}
	
	//private HashMap<Integer,? extends Person> nutzerzuordnung;
	//-1 = nicht angemeldet, 0 = user, 1 = mitarbeiter
	private byte userClass = -1;
	

	public Person anmelden(int id, String passwort) throws LoginFailedException {
		Person p = null; 
		
		try {
			p = mv.anmelden(id, passwort);
		} catch (LoginFailedException lfe) {
			//
			p = kv.anmelden(id, passwort);
		}
		
		return p;
	}
	
	/**
	 * @return Alle in der Artikelverwaltung gespeicherten Artikel
	 */
	public Vector<Artikel> alleArtikelAusgeben(){
		return av.getArtikel();
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
	
	/**
	 * Erstellt einen neuen Kunden mit fortlaufender Kundennummer
	 * @param firstname Vorname des anzulegenden Kunden
	 * @param lastname Nachname des anzulegenden Kunden
	 */
	public void erstelleKunde(String firstname, String lastname, String passwort){
		Kunde k = new Kunde(firstname, lastname, kv.getNextID(), passwort);
		kv.erstelleKunde(k);
	}
}
