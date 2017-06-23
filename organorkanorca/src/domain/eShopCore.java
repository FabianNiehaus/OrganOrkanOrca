package domain;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import data_objects.Artikel;
import data_objects.Ereignis;
import data_objects.Kunde;
import data_objects.Mitarbeiter;
import data_objects.Person;
import data_objects.Rechnung;
import data_objects.Typ;
import data_objects.Warenkorb;
import domain.exceptions.LoginFailedException;
import domain.exceptions.MaxIDsException;
import domain.exceptions.PersonNonexistantException;
import domain.exceptions.AccessRestrictedException;
import domain.exceptions.ArticleAlreadyInBasketException;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.ArticleStockNotSufficientException;
import domain.exceptions.BasketNonexistantException;
import domain.exceptions.InvalidAmountException;
import domain.exceptions.InvalidPersonDataException;

/**
 * @author Fabian Niehaus
 * Zentrales Modul des eShop
 */
public class eShopCore {

	Artikelverwaltung av;
	private Kundenverwaltung kv;
	private Mitarbeiterverwaltung mv;
	private Warenkorbverwaltung wv;
	private Rechnungsverwaltung rv;
	private Ereignisverwaltung ev;
	
	private String dateipfad = "";
	
	/**
	 * @throws PersonNonexistantException 
	 * @throws ArticleNonexistantException 
	 * @throws InvalidPersonDataException 
	 */
	public eShopCore() throws IOException, ArticleNonexistantException, PersonNonexistantException, InvalidPersonDataException {
		super();
		av = new Artikelverwaltung();
		kv = new Kundenverwaltung();
		mv = new Mitarbeiterverwaltung();
		wv = new Warenkorbverwaltung();
		rv = new Rechnungsverwaltung();
		ev = new Ereignisverwaltung(kv,mv,av);
		
		ladeDaten();

	}

	/**
	 * Anmelden des Nutzers
	 * Kunden-ID 1000 - 8999, Mitarbeiter-ID 9000 - 9999
	 * @param id Nutzer-ID
	 * @param passwort Nutzer-Passwort
	 * @return Objekt des Nutzers der Klasse Kunde oder Mitarbeiter
	 * @throws LoginFailedException Anmeldung fehlgeschlagen
	 */
	public Person anmelden(int id, String passwort) throws LoginFailedException {

		if (id >= 1000 && id < 9000){
			return kv.anmelden(id, passwort);
		} else if (id >= 9000 && id < 10000){
			return mv.anmelden(id, passwort);
		} else {
			throw new LoginFailedException();
		}
	}
	
	/**
	 * @return Alle in der Artikelverwaltung gespeicherten Artikel
	 * @throws AccessRestrictedException 
	 */
	public Vector<Artikel> alleArtikelAusgeben(Person p) throws AccessRestrictedException{
		if(istKunde(p) || istMitarbeiter(p)){
			return av.getArtikel();
		} else {
			throw new AccessRestrictedException(p, "\"Alle Artikel ausgeben\"");
		}
	}

	/**
	 * @return Alle in der Kundenverwaltung gespeicherten Kunden
	 * @throws AccessRestrictedException
	 */
	public Vector<Kunde> alleKundenAusgeben(Person p) throws AccessRestrictedException{
		if(istMitarbeiter(p)){
			return kv.getKunden();
		} else {
			throw new AccessRestrictedException(p, "Kundenverwaltung");
		}
	}
	
	/**
	 * @return Alle in der Mitarbeiterverwaltung gespeicherten Mitarbeiter
	 */
	public Vector<Mitarbeiter> alleMitarbeiterAusgeben(Person p) throws AccessRestrictedException{
		if(istMitarbeiter(p)){
			return mv.getMitarbeiter();
		} else {
			throw new AccessRestrictedException(p, "Mitarbeiterverwaltung");
		}
	}
	
	/**
	 * @return Alle in der Warenkorbverwaltung gespeicherten Warenkörbe
	 * @throws AccessRestrictedException 
	 */
	public void alleWarenkoerbeAusgeben(Person p) throws AccessRestrictedException{
		if(istMitarbeiter(p)){
			for (Warenkorb w : wv.getWarenkoerbe()){
				w.toString();
			}
		} else {
			throw new AccessRestrictedException(p, "\"Alle Warenkörbe ausgeben\"");
		}
	}
	
	/**
	 * Erstellt einen neuen Kunden mit fortlaufender Kundennummer
	 * @param firstname Vorname des anzulegenden Kunden
	 * @param lastname Nachname des anzulegenden Kunden
	 * @throws AccessRestrictedException 
	 * @throws InvalidPersonDataException 
	 */
	public Kunde erstelleKunde(String firstname, String lastname, String passwort, String address_Street, String address_Zip, String address_Town, Person p) throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException{
		if(istMitarbeiter(p) || p == null){
			return kv.erstelleKunde(firstname, lastname, passwort, address_Street, address_Zip, address_Town, wv.erstelleWarenkorb());
		} else {
			throw new AccessRestrictedException(p, "\"Kunde anlegen\"");
		}
	}
	
	/**
	 * Erstellt einen neuen Mitarbeiter mit fortlaufender Kundennummer
	 * @param firstname Vorname des anzulegenden Kunden
	 * @param lastname Nachname des anzulegenden Kunden
	 * @throws AccessRestrictedException 
	 * @throws InvalidPersonDataException 
	 */
	public Mitarbeiter erstelleMitatbeiter(String firstname, String lastname, String passwort, String address_Street, String address_Zip, String address_Town, Person p) throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException{
		if(istMitarbeiter(p) || p == null){
			return mv.erstelleMitarbeiter(firstname, lastname, passwort, address_Street, address_Zip, address_Town);
		} else {
			throw new AccessRestrictedException(p, "\"Mitarbeiter anlegen\"");
		}
	}
	
	/**
	 * Erstellt einen neuen Artikel
	 * @param bezeichnung Artikelbezeichnung
	 * @param bestand Artikelbestamd
	 * @param preis Artikelpreis
	 * @param p Userobjekt
	 * @return Erstellten Artikel
	 * @throws AccessRestrictedException 
	 * @throws InvalidAmountException 
	 */
	public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, int packungsgroesse, Person p) throws AccessRestrictedException, InvalidAmountException{
		if(istMitarbeiter(p)){
			Artikel art = av.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse);
			//Ereignis erzeugen
			ev.ereignisErstellen(p, Typ.NEU, art, bestand);
			return art;
		} else {
			throw new AccessRestrictedException(p, "\"Artikel anlegen\"");
		}
	}
	
	/**
	 * Erhöht den Bestand eines Artikels
	 * @param artikelnummer Artikelnummer des zu bearbeitenden Artikels
	 * @param bestand Neuer Bestand
	 * @param p Userobjekt
	 * @return Bearbeiteten Artikel
	 * @throws ArticleNonexistantException Artikelnummer existiert nicht
	 * @throws AccessRestrictedException 
	 * @throws InvalidAmountException 
	 */
	public Artikel erhoeheArtikelBestand(int artikelnummer, int bestand, Person p) throws ArticleNonexistantException, AccessRestrictedException, InvalidAmountException{
		if(istMitarbeiter(p)){
			Artikel art = av.erhoeheBestand(artikelnummer, bestand);
			//Ereignis erzeugen
			ev.ereignisErstellen(p, Typ.EINLAGERUNG, art, bestand);
			return art;
		} else {
			throw new AccessRestrictedException(p, "\"Bestand erhöhen\"");
		}
	}
	
	/**
	 * Legt einen Artikel in den Warenkorb
	 * @param artikelnummer Auszuwählender Artikel
	 * @param anzahl Auszuwählende Anzahl
	 * @param p Userobjekt
	 * @throws ArticleNonexistantException Artikelnummer existiert nicht
	 * @throws ArticleStockNotSufficientException Artikelbestand nicht ausreichend
	 * @throws AccessRestrictedException 
	 * @throws InvalidAmountException 
	 * @throws ArticleAlreadyInBasketException 
	 */
	public void artikelInWarenkorbLegen(int artikelnummer, int anz, Person p) throws ArticleNonexistantException, ArticleStockNotSufficientException, AccessRestrictedException, InvalidAmountException, ArticleAlreadyInBasketException{
		if(istKunde(p)){
			Warenkorb wk = kv.gibWarenkorbVonKunde(p);
			
			if(wk == null){
				wk = wv.erstelleWarenkorb(); 
				kv.weiseWarenkorbzu((Kunde)p, wk);
			}
			
			Artikel art = av.sucheArtikel(artikelnummer);
			wv.legeInWarenkorb(wk, art, anz);
		} else {
			throw new AccessRestrictedException(p, "\"Artikel in Warenkorb legen\"");
		}
	}

	/**
	 * Gibt den Warenkorb eines Kunden zurueck
	 * @param p Userobjekt
	 * @return Warenkorb
	 * @throws AccessRestrictedException 
	 */
	public Warenkorb warenkorbAusgeben(Person p) throws AccessRestrictedException{
		if(istKunde(p)){
			return kv.gibWarenkorbVonKunde(p);
		} else {
			throw new AccessRestrictedException(p, "\"Warenkorb anzeigen\"");
		}
	}
	
	/**
	 * Leert den Warenkorb eines Kunden
	 * @param p Userobjekt
	 * @throws AccessRestrictedException 
	 */
	public void warenkorbLeeren(Person p) throws AccessRestrictedException{
		if(istKunde(p)){
			Warenkorb wk = kv.gibWarenkorbVonKunde(p);
			wv.leereWarenkorb(wk);
		} else {
			throw new AccessRestrictedException(p, "\"Warenkorb leeren\"");
		}
	}
	
	/**
	 * Verändert die Anzahl eines Artikels im Warenkorb eines Kunden
	 * @param pos Position des Artikels
	 * @param anz Neue Anzahl
	 * @param p Userobjekt
	 * @throws ArticleStockNotSufficientException Artikelbestand nicht ausreichend
	 * @throws AccessRestrictedException 
	 * @throws InvalidAmountException 
	 */
	public void artikelInWarenkorbAendern(Artikel art, int anz, Person p) throws ArticleStockNotSufficientException, BasketNonexistantException, AccessRestrictedException, InvalidAmountException{
		if(istKunde(p)){
			Warenkorb wk = kv.gibWarenkorbVonKunde(p);
			wv.aendereWarenkorb(wk, art, anz);
		} else {
			throw new AccessRestrictedException(p, "\"Anzahl Artikel in Warenkorb ändern\"");
		}
	}
	
	public void artikelAusWarenkorbEntfernen(Artikel art, Person p) throws AccessRestrictedException{
		if(istKunde(p)){
			Warenkorb wk = kv.gibWarenkorbVonKunde(p);
			wv.loescheAusWarenkorn(wk, art);
		} else {
			throw new AccessRestrictedException(p, "\"Artikel aus Warenkorb löschen\"");
		}
	}
	
	/**
	 * Warenkorb kaufen und Rechnung erstellen
	 * @param p Userobjekt
	 * @return Erstellte Rechnung
	 * @throws AccessRestrictedException 
	 * @throws InvalidAmountException 
	 */
	public Rechnung warenkorbKaufen(Person p) throws AccessRestrictedException, InvalidAmountException{
		if(istKunde(p)){
		
			//Warenkorb des Benutzers abfragen
			Warenkorb wk = kv.gibWarenkorbVonKunde(p);
			
			//Bestand der Artikel im Warenkorb reduzieren und Gesamtpreis errechnen
			int gesamt = 0;
			Map<Artikel,Integer> inhalt = wk.getArtikel();
			for(Map.Entry<Artikel, Integer> ent : inhalt.entrySet()){
				try{
					av.erhoeheBestand(ent.getKey().getArtikelnummer(), -1 * ent.getValue());
					//Ereignis erstellen
					ev.ereignisErstellen(p, Typ.KAUF, ent.getKey(), (int) ent.getValue());
					//TODO Ereigniserstellung in Verwaltungen auslagern
				} catch (ArticleNonexistantException anne){
					//TODO
				}
				gesamt += (ent.getValue() * ent.getKey().getPreis());
			}
			
			//Warenkorb fuer Rechnung erzeugen
			Warenkorb temp = new Warenkorb();
			temp.copy(wk);
			
			//Rechnung erzeugen
			Rechnung re = rv.rechnungErzeugen((Kunde) p, new Date(), temp, gesamt);
			
			//Warenkorb von Kunde leeren
			wv.leereWarenkorb(wk);
			
			//Rechnungsobjekt an C/GUI zurueckgeben
			return re;
			
		} else {
			throw new AccessRestrictedException(p, "\"Warenkorb bezahlen\"");
		}
	}	
	
	/**
	 * Schreibt die Daten der Verwaltungen in die Persistenz
	 * @throws IOException
	 */
	public void schreibeDaten() throws IOException{
		av.schreibeDaten(dateipfad + "ARTIKEL.txt"); 
		kv.schreibeDaten(dateipfad + "KUNDEN.txt");
		mv.schreibeDaten(dateipfad + "MITARBEITER.txt");
		ev.schreibeDaten(dateipfad + "EREIGNISSE.txt");
	}
	
	public void ladeDaten() throws IOException, ArticleNonexistantException, PersonNonexistantException, InvalidPersonDataException {
		av.liesDaten(dateipfad + "ARTIKEL.txt");
		kv.liesDaten(dateipfad + "KUNDEN.txt", wv);
		mv.liesDaten(dateipfad + "MITARBEITER.txt");
		ev.liesDaten(dateipfad + "EREIGNISSE.txt"); 
	}
	
	/**
	 * Erlaubt die Suche nach einer Artikelnummer
	 * @param artikelnummer Artikelnumemr von geuschtem Artikel
	 * @return Gesuchter Artikel
	 * @throws ArticleNonexistantException
	 * @throws AccessRestrictedException 
	 */
	public Artikel artikelSuchen(int artikelnummer, Person p) throws ArticleNonexistantException, AccessRestrictedException{
		if(istMitarbeiter(p) || istKunde(p)){
			return av.sucheArtikel(artikelnummer);
		} else {
			throw new AccessRestrictedException(p, "\"Artikel suchen (Artikelnummer)\"");
		}
	}
	
	/**
	 * Erlaubt die Suche nach einer Artikelbezeichnung
	 * @param bezeichnung (Teil-)Bezeichnung des gesuchten Artikels
	 * @return Liste der zur Bezeichnung passenden Artikel
	 * @throws ArticleNonexistantException Keine Artikel gefunden
	 * @throws AccessRestrictedException 
	 */
	public Vector<Artikel> artikelSuchen(String bezeichnung, Person p) throws ArticleNonexistantException, AccessRestrictedException{
		if(istMitarbeiter(p) || istKunde(p)){
			return av.sucheArtikel(bezeichnung);
		} else {
			throw new AccessRestrictedException(p, "\"Artikel suchen (Bezeichnung)\"");
		}
	}
	
	public void artikelLoeschen(Artikel art, Person p) throws AccessRestrictedException{
		if(istMitarbeiter(p)){
			av.loeschen(art);
		} else {
			throw new AccessRestrictedException(p, "\"Artikel suchen (Artikelnummer)\"");
		}
	}
	
	public void personLoeschen(Person loeschen, Person p) throws AccessRestrictedException {
		if(istMitarbeiter(p)){
			if(kv.loescheKunde((Kunde)loeschen)){}
			else if (mv.loescheMitarbeiter((Mitarbeiter)loeschen));
		} else {
			throw new AccessRestrictedException(p, "Kunde löschen");
		}
	}
	
	public boolean istMitarbeiter(Person p){
		if(p instanceof Mitarbeiter){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean istKunde(Person p){
		if(p instanceof Kunde){
			return true;
		} else {
			return false;
		}

	}

	public Kunde kundeSuchen(int id, Person p) throws PersonNonexistantException {
		return kv.sucheKunde(id);
	}
	
	public Mitarbeiter mitarbeiterSuchen(int id, Person p) throws PersonNonexistantException {
		return mv.sucheMitarbeiter(id);
	}
	
	public Vector<Ereignis> alleEreignisseAusgeben(Person p) throws AccessRestrictedException{
		if(istMitarbeiter(p)){
			return ev.getEreignisse();
		} else {
			throw new AccessRestrictedException(p, "Kunde löschen");
		}
	}
}
