package domain;

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
	
<<<<<<< HEAD
	/**
	 * @param firstname Vorname des Benutzers
	 * @param lastname Nachname des Benutzers
	 * @param id ID des Benutzers
	 * @return Erfolgs-/fehlermeldung
	 */
	public String anmelden(String firstname, String lastname, int id){
=======
	public Person anmelden(String firstname, String lastname, int id) throws LoginFailedException {
		Person p = null; 
		
		try {
			p = mv.anmelden(firstname, lastname);
		} catch (LoginFailedException lfe) {
			//
			p = kv.anmelden(firstname, lastname);
		}
		
		return p;
		
/*		
>>>>>>> refs/remotes/origin/master
		try {
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
		} catch (VectorIsEmptyException e) {
			e.printStackTrace();
			return "Fehler (Vektoren leer)";
		}
		*/
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
	
	/**
	 * Erstellt einen neuen Kunden mit fortlaufender Kundennummer
	 * @param firstname Vorname des anzulegenden Kunden
	 * @param lastname Nachname des anzulegenden Kunden
	 */
	public void erstelleKunde(String firstname, String lastname){
		Kunde k = new Kunde(firstname, lastname, kv.getNextID());
		kv.erstelleKunde(k);
	}
}
