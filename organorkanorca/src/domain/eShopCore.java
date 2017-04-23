package domain;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import data_objects.Artikel;
import data_objects.Kunde;
import data_objects.Mitarbeiter;
import data_objects.Person;
import data_objects.Rechnung;
import data_objects.Typ;
import data_objects.Warenkorb;
import domain.exceptions.LoginFailedException;
import domain.exceptions.ArticleNumberNonexistantException;

/**
 * @author Fabian Niehaus
 * Zentrales Modul des eShop
 */
public class eShopCore {

	private Artikelverwaltung av;
	private Kundenverwaltung kv;
	private Mitarbeiterverwaltung mv;
	private Warenkorbverwaltung wv;
	private Rechnungsverwaltung rv;
	private Ereignisverwaltung ev;
	
	private String dateipfad = "";
	
	/**
	 */
	public eShopCore()  throws IOException {
		super();
		av = new Artikelverwaltung();
		kv = new Kundenverwaltung();
		mv = new Mitarbeiterverwaltung();
		wv = new Warenkorbverwaltung();
		rv = new Rechnungsverwaltung();
		ev = new Ereignisverwaltung();
		
		try{
			av.liesDaten(dateipfad + "ARTIKEL.txt");
		} catch (IOException ie){
			
		}
		
		Kunde ku = kv.erstelleKunde("Fabian","Niehaus", "test", wv.erstelleWarenkorb());
		System.out.println(ku.getId());
	}
	
	/* Noch nicht verwendet
	//private HashMap<Integer,? extends Person> nutzerzuordnung;
	//-1 = nicht angemeldet, 0 = user, 1 = mitarbeiter
	private byte userClass = -1;
	*/

	/**
	 * Anmelden des Nutzers
	 * @param id Nutzer-ID
	 * @param passwort Nutzer-Passwort
	 * @return Objekt des Nutzers der Klasse Kunde oder Mitarbeiter
	 * @throws LoginFailedException Anmeldung fehlgeschlagen
	 */
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
		//To-Do: Kopie von Artikelliste zurückgeben
		return av.getArtikel();
	}

	/**
	 * @return Alle in der Kundenverwaltung gespeicherten Kunden
	 */
	public Vector<Kunde> alleKundenAusgeben(){
		//To-Do: Kopie von Kundenliste zurückgeben
		return kv.getKunden();
	}
	
	/**
	 * @return Alle in der Mitarbeiterverwaltung gespeicherten Mitarbeiter
	 */
	public Vector<Mitarbeiter> alleMitarbeiterAusgeben(){
		//To-Do: Kopie von Mitarbeiterliste zurückgeben
		return mv.getMitarbeiter();
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
		kv.erstelleKunde(firstname, lastname, passwort, wv.erstelleWarenkorb());
	}
	
	/**
	 * Erstellt einen neuen Artikel
	 * @param bezeichnung Artikelbezeichnung
	 * @param bestand Artikelbestamd
	 * @param preis Artikelpreis
	 * @param p Userobjekt
	 * @return Erstellten Artikel
	 */
	public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, Person p){
		Artikel art = av.erstelleArtikel(bezeichnung, bestand, preis);
		//Ereignis erzeugen
		ev.ereignisErstellen(p, Typ.NEU, art, bestand);
		return art;
	}
	
	/**
	 * Erhöht den Bestand eines Artikels
	 * @param artikelnummer Artikelnummer des zu bearbeitenden Artikels
	 * @param bestand Neuer Bestand
	 * @param p Userobjekt
	 * @return Bearbeiteten Artikel
	 * @throws ArticleNumberNonexistantException Artikelnummer existiert nicht
	 */
	public Artikel erhoeheArtikelBestand(int artikelnummer, int bestand, Person p) throws ArticleNumberNonexistantException{
		try{
			Artikel art = av.erhoeheBestand(artikelnummer, bestand);
			//Ereignis erzeugen
			ev.ereignisErstellen(p, Typ.EINLAGERUNG, art, bestand);
			return art;
		} catch (ArticleNumberNonexistantException anne){
			throw new ArticleNumberNonexistantException();
		}
	}
	
	/**
	 * Legt einen Artikel in den Warenkorb
	 * @param artikelnummer Auszuwählender Artikel
	 * @param anzahl Auszuwählende Anzahl
	 * @param p Userobjekt
	 * @throws ArticleNumberNonexistantException Artikelnummer existiert nicht
	 */
	public void artikelInWarenkorbLegen(int artikelnummer, int anzahl, Person p) throws ArticleNumberNonexistantException{
		Warenkorb wk = kv.gibWarenkorbVonKunde(p);
		try{
		Artikel art = av.sucheArtikel(artikelnummer);
		wv.legeInWarenkorb(wk, art, anzahl);
		} catch (ArticleNumberNonexistantException anne){
			throw new ArticleNumberNonexistantException();
		}
	}
	
	/**
	 * Gibt den Warenkorb eines Kunden zurück
	 * @param p Userobjekt
	 * @return Warenkorb
	 */
	public Warenkorb warenkorbAusgeben(Person p){
		return kv.gibWarenkorbVonKunde(p);
	}
	
	/**
	 * Leert den Warenkorb eines Kunden
	 * @param p Userobjekt
	 */
	public void warenkorbLeeren(Person p){
		Warenkorb wk = kv.gibWarenkorbVonKunde(p);
		wv.leereWarenkorb(wk);
	}
	
	/**
	 * Verändert die Anzahl eines Artikels im Warenkorb eines Kunden
	 * @param pos Position des Artikels
	 * @param anz Neue Anzahl
	 * @param p Userobjekt
	 */
	public void artikelInWarenkorbAendern(int pos, int anz, Person p){
		Warenkorb wk = kv.gibWarenkorbVonKunde(p);
		wv.aendereWarenkorb(wk, pos, anz);
	}
	
	/**
	 * Warenkorb kaufen und Rechnung erstellen
	 * @param p Userobjekt
	 * @return Erstellte Rechnung
	 */
	public Rechnung warenkorbKaufen(Person p){
		//Warenkorb des Benutzers abfragen
		Warenkorb wk = kv.gibWarenkorbVonKunde(p);
		
		//Bestand der Artikel im Warenkorb reduzieren und Gesamtpreis errechnen
		int gesamt = 0;
		LinkedHashMap<Artikel,Integer> inhalt = wk.getArtikel();
		for(Map.Entry<Artikel, Integer> ent : inhalt.entrySet()){
			try{
				av.erhoeheBestand(ent.getKey().getArtikelnummer(), -1 * ent.getValue());
				//Ereignis erstellen
				ev.ereignisErstellen(p, Typ.KAUF, ent.getKey(), (int) ent.getValue());
			} catch (ArticleNumberNonexistantException anne){
				
			}
			gesamt += (ent.getValue() * ent.getKey().getPreis());
		}
		
		
		
		//Rechnung erzeugen und Warenkorb leeren
		Rechnung re = rv.rechnungErzeugen((Kunde) p, new Date(), wk, gesamt);
		wv.leereWarenkorb(wk);
		
		//Rechnungsobjekt an C/GUI zurückgeben
		return re;
	}	
	
	/**
	 * Schreibt die Daten der Verwaltungen in die Persistenz
	 * @throws IOException
	 */
	public void schreibeDaten() throws IOException{
		av.schreibeDaten(dateipfad + "ARTIKEL.txt"); 
	}
}
