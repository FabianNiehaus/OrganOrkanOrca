package user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import data_objects.Artikel;
import data_objects.Kunde;
import data_objects.Person;
import data_objects.Rechnung;
import data_objects.Mitarbeiter;
import domain.eShopCore;
import domain.exceptions.LoginFailedException;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.ArticleStockNotSufficientException;
import domain.exceptions.BasketNonexistantException;
import util.IO;
import util.StringComparator;

/**
 * @author Fabian Niehaus
 * Command-Line-Interface für den eSHop
 */
/**
 * @author Manic
 *
 */
public class CUI {
	
	private eShopCore eShop;
	private Person user = null; 
	
	public CUI() throws IOException {

		eShop = new eShopCore();

	}
	
	/**
	 * Logik für Nutzer-Login
	 */
	public void login(){
		
		IO.println("Bitte melden Sie Sich an.");
		IO.print("ID: ");
		int id = IO.readInt();
		IO.print("Passwort: ");
		String passwort = IO.readString();
		
		
		//Anmeldelogik
		try {
			user = eShop.anmelden(id, passwort);
			IO.println(user.getFirstname() + " " + user.getLastname() + " hat sich als " + user.getClass().getSimpleName() + " eingeloggt.");
		} catch (LoginFailedException lfe) {
			IO.println(lfe.getMessage());
			IO.println("Bitte noch einmal versuchen!");
			IO.println("Eingabe \"q\" um den eShop zu beenden");
		}
	}
	
	/**
	 * Logik für das Suchen eines Artikels
	 * @param liste
	 */
	private void artikelSuchen(){
		String searchType = "";
		
		IO.println("Artikel suchen");
		IO.println("Eingabe \"nr\" um nach Artikelnummer zu suchen");
		IO.println("Eingabe \"bez\" um nach Bezeichnung zu suchen");
		IO.println("---------------------------------------------------------");
		
		searchType = IO.readString();
		
		if(searchType.equals("nr")){
			IO.println("Bitte Artikelnummer eingeben:");
			int artikelnummer = IO.readInt();
			try {
				Artikel art = eShop.artikelSuchen(artikelnummer, user);
				IO.println(art.toString());
			} catch (ArticleNonexistantException ane) {
				IO.println(ane.getMessage());
			}
		} else if(searchType.equals("bez")){
			IO.println("Bitte Artikelbezeichnung eingeben:");
			String bezeichnung = IO.readString();
			try {
				Vector<Artikel> liste = eShop.artikelSuchen(bezeichnung, user);
				for(Artikel art : liste){
					IO.println(art.toString());
				}
			} catch (ArticleNonexistantException ane) {
				IO.println(ane.getMessage());
			}
		}
	}
	
	/** Logik für die Sortierte Ausgabe der Artikelliste
	 * @param liste
	 */
	private void artikelSortiertAusgeben(Vector<Artikel> liste){
		String sortBy = "";
		
		IO.println("Artikel sortiert ausgeben:");
		IO.println("Eingabe \"nr\" um nach Artikelnummer sortiert auszugeben");
		IO.println("Eingabe \"bez\" um nach Bezeichnung sortiert auszugeben");
		IO.println("---------------------------------------------------------");
		
		sortBy = IO.readString();
		
		Artikel[] artSort = new Artikel[liste.size()];
		if (liste.isEmpty()) {
			System.out.println("Keine Artikel auszugeben!");
		} else {
			int i = 0;
			for(Object artikel : liste){
				if(i < artSort.length){
					artSort[i] = (Artikel) artikel;
					i++;
				}
			}
			
			if (sortBy.equals("nr")){
				//Sortieren nach Artikelnummer
				boolean swapped = true;
				int j = 0;
				Artikel tmp;
				while(swapped){
					swapped = false;
					j++;
					for (int k = 0; k < artSort.length -j; k++){
						if(artSort[k].getArtikelnummer() > artSort[k+1].getArtikelnummer()){
							tmp = artSort[k];
							artSort[k] = artSort[k+1];
							artSort[k+1] = tmp;
							swapped = true;
						}
					}
				}
			
			} else if (sortBy.equals("bez")){
				//Sortieren nach Artikelbezeichnung
				//To-Do: Sortieren von Strings
				boolean swapped = true;
				int j = 0;
				Artikel tmp;
				while(swapped){
					swapped = false;
					j++;
					for (int k = 0; k < artSort.length -j; k++){
						if(StringComparator.compare(artSort[k].getBezeichnung(),artSort[k+1].getBezeichnung())){
							tmp = artSort[k];
							artSort[k] = artSort[k+1];
							artSort[k+1] = tmp;
							swapped = true;
						}
					}
				}
			}
			for (Artikel art : artSort){
				IO.println(art.toString());
			}
		}
	}
	
	/**
	 * Logik für das Erstellen eines Artikels
	 */
	private void artikelErstellen(){
		
		IO.println("Artikel erstellen");
		IO.print("Bezeichnung:");
		String bezeichnung = IO.readString();
		IO.print("Bestand:");
		int bestand = IO.readInt();
		IO.print("Preis");
		double preis = IO.readDouble();
		IO.println("-----------------------");
		
		Artikel art = eShop.erstelleArtikel(bezeichnung, bestand, preis, user);
		
		IO.println(art.toString());
	}
	
	/**
	 * Logik für das Erhöhen des Bestands eines Artikels
	 */
	private void artikelBestandErhoehen(){		
		IO.print("Artikelnummer:");
		int artikelnummer = IO.readInt();
		IO.print("Bestand erhöhen um:");
		int bestand = IO.readInt();
		IO.println("-----------------------");
		
		try{
			Artikel art = eShop.erhoeheArtikelBestand(artikelnummer, bestand, user);			
			IO.println(art.toString());
		} catch (ArticleNonexistantException ane){
			IO.println("Artikelnummer existiert nicht!");
			ane.printStackTrace();
		}
		
	}
	
	/**
	 * Logik für das Hinzufügen eines Artikels zu einem Warenkorb
	 */
	private void artikelInWarenkorbLegen(){
		IO.print("Artikelnummer:");
		int artikelnummer = IO.readInt();
		IO.print("Anzahl:");
		int anzahl = IO.readInt();
		IO.println("-----------------------");
		
		try{
			eShop.artikelInWarenkorbLegen(artikelnummer, anzahl, user);
			gibWarenkorbAus();
		} catch (ArticleNonexistantException ane){
			IO.println(ane.getMessage());
		} catch (ArticleStockNotSufficientException asnse){
			IO.println(asnse.getMessage());
		}
	}
	
	/**
	 * Logik für das Ausgeben des Warenkorb des Kunden
	 */
	private void gibWarenkorbAus(){
		IO.println("Warenkorb");
		IO.println(eShop.warenkorbAusgeben(user).toString());
	}
	
	/**
	 * Logik für die Änderung der Anzahl eines Artikels im Warenkorb des Kunden Warenkorb
	 */
	private void aendereArtiklInWarenkorb(){
		gibWarenkorbAus();
		
		IO.println("Artikel anpassen");
		IO.print("Position eingeben:");
		
		int pos = IO.readInt();
		
		IO.print("Anzahl eingeben:");
		
		int anz = IO.readInt();
		try{
			eShop.artikelInWarenkorbAendern(pos, anz, user);
			gibWarenkorbAus();
		} catch (ArticleStockNotSufficientException asnse){
			asnse.getMessage();
		} catch(BasketNonexistantException bne){
			bne.getMessage();
		}
	}
	
	/**
	 * Logik fpr das Kaufen des Warenkorbs des Kunden
	 */
	private void warenkorbKaufen(){
		//Formatierungsvorlage für Datum
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		//Kauf abwickeln und Rechnung erzeugen
		Rechnung re = eShop.warenkorbKaufen(user);
		
		IO.println("Rechnung");
		IO.println("");
		IO.println(dateFormat.format(re.getDatum()));
		IO.println("");
		IO.println("Kunde:");
		IO.println("Kundennummer: " + re.getKu().getId());
		IO.println(re.getKu().getFirstname() + " " + re.getKu().getLastname());
		IO.println(re.getKu().getAddress_Street());
		IO.println(re.getKu().getAddress_Zip() + " " + re.getKu().getAddress_Town());
		IO.println("");
		gibWarenkorbAus();
		IO.println("Gesamtbetrag: " + re.getGesamt() + "€");
	}
	
	/**
	 * Logik für die Anzeige der Warenkorbverwaltung des Kunden
	 */
	private void gibWarenkorbverwaltungAus(){
		String input = "";
		
		do{
			IO.println("");		
			
			IO.println("Eingabe \"w\" um Warenkorb auszugeben");			
			IO.println("Eingabe \"k\" um Artikel in Warenkorb zu legen");
			IO.println("Eingabe \"a\" um Artikel im Warenkorb zu ändern");
			IO.println("Eingabe \"l\" um Warenkorb zu leeren");
			IO.println("Eingabe \"b\" um Warenkorb zu bezahlen");
			IO.println("Eingabe \"q\" um zum Hauptmenü zurückzukehren");
			IO.println("---------------------------------------------------------------------");
			
			input = IO.readString();
			
			switch(input){
			case "w": gibWarenkorbAus(); break;
			case "k": artikelInWarenkorbLegen(); break;
			case "a": aendereArtiklInWarenkorb(); break;
			case "l": eShop.warenkorbLeeren(user); break;
			case "b": warenkorbKaufen(); break;
			}
		} while (!input.equals("q"));
	}
	
	/**
	 * Logik für die Anzeige der Artikelverwaltung 
	 */
	private void gibArtikelverwaltungAus(){
		String input = "";
		
		do{
			IO.println("");		
			
			IO.println("Eingabe \"a\" um alle Artikel auszugeben");
			IO.println("Eingabe \"s\" um Artikel sortiert auszugeben");
			IO.println("Eingabe \"f\" um Artikel zu suchen");
			
			
			if((user instanceof Kunde)){
				// Menüeingaben speziell für Kunde
				IO.println("Eingabe \"k\" um Artikel in Warenkorb zu legen");
			} else if (user instanceof Mitarbeiter){
				// Menüeingaben speziel für Mitarbeiter
				IO.println("Eingabe \"e\" um Artikel zu erstellen");
				IO.println("Eingabe \"b\" um den Bestand zu erhöhen");
			}
			
			IO.println("Eingabe \"q\" um zum Hauptmenü zurückzukehren");
			IO.println("---------------------------------------------------------------------");
			
			input = IO.readString();
			
			switch(input){
			case "a": artikelAusgeben(eShop.alleArtikelAusgeben(user)); break;
			case "s": artikelSortiertAusgeben(eShop.alleArtikelAusgeben(user)); break;
			case "e": artikelErstellen(); break;
			case "b": artikelBestandErhoehen(); break;
			case "k": artikelInWarenkorbLegen(); break;
			case "f": artikelSuchen(); break;
			}
				
		} while (!input.equals("q"));
		
	}
	
	/**
	 * Logik für die Ausgabe des Hauptmenüs
	 */
	public void gibMenueAus(){
		IO.println("");
		
		if ((user instanceof Kunde) || user instanceof Mitarbeiter){
			IO.println("eShop Hauptseite");
			
			//Menüeingaben für alle
			IO.println("Eingabe \"a\" um zur Artikelverwaltung zu gelangen");
			
			if((user instanceof Kunde)){
				// Menüeingaben speziell für Kunde
				IO.println("Eingabe \"w\" zur Warenkobverwaltung zu gelangen");
				
			} else if((user instanceof Mitarbeiter)) {
				// Menüeingaben speziel für Mitarbeiter
				
				IO.println("Eingabe \"k\" um alle Kunden auszugeben");
				IO.println("Eingabe \"m\" um alle Mitarbeiter auszugeben");
				IO.println("Eingabe \"s\" um alle Laufzeitdaten zu speichern");
			}
			
			IO.println("Eingabe \"q\" um den eShop zu beenden");
		} else {
			login();
		}
	}
	
	/**
	 * Logik für die Verarbeitung der Nutzereingabe im Hauptmenü
	 * @param input Nutzereingabe
	 * @throws IOException
	 */
	public void verarbeiteEingabe(String input) throws IOException{
		switch(input){
		case "a": gibArtikelverwaltungAus(); break;
		case "k": kundenAusgeben(eShop.alleKundenAusgeben(user)); break;
		case "m": mitarbeiterAusgeben(eShop.alleMitarbeiterAusgeben(user)); break;
		case "w": gibWarenkorbverwaltungAus(); break;
		case "s": eShop.schreibeDaten(); break;
		}
	}
	
	/**
	 * Hauptmethode der CUI
	 */
	public void run(){
		String input = ""; 
		
		//Willkommensnachrich
		IO.println("Willkommen bei OrganOrkanOrca.org.");
		
		//Login
		login();
		
		// Hauptschleife der Benutzungsschnittstelle
		do {
			gibMenueAus();
			try {
				input = IO.readString();
				verarbeiteEingabe(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (!input.equals("q"));
		
		IO.println("OrganOrkanOrca wurde beendet.\nAuf Wiedersehen!");
	}
	
	/**
	 * Start-Methode der CUI
	 * @param args
	 */
	public static void main(String[] args){
		
		CUI cui;
		try {
			cui = new CUI();
			cui.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gibt in einer Liste gespeicherte Artikel auf der Konsole aus;
	 * @param liste Liste der auszugebenden Artikel
	 */
	public void artikelAusgeben(Vector<Artikel> liste){
		if (liste.isEmpty()) {
			System.out.println("Keine Artikel auszugeben!");
		} else {
			for(Object artikel : liste){
				IO.println(artikel.toString());
			}
		}
	}
	
	/**
	 * Gibt in einer Liste gespeicherte Mitarbeiter auf der Konsole aus;
	 * @param liste Liste der auszugebenden Mitarbeiter
	 */
	public void  mitarbeiterAusgeben(Vector<Mitarbeiter> liste){
		if (liste.isEmpty()) {
			System.out.println("Keine Mitarbeiter auszugeben!");
		} else {
			for(Object mitarbeiter : liste){
				IO.println(mitarbeiter.toString());
			}
		}
	}
	
	/**
	 * Gibt in einer Liste gespeicherte Kunden auf der Konsole aus;
	 * @param liste Liste der auszugebenden Kunden
	 */
	public void  kundenAusgeben(Vector<Kunde> liste){
		if (liste.isEmpty()) {
			System.out.println("Keine Kunden auszugeben!");
		} else {
			for(Object kunde : liste){
				IO.println(kunde.toString());
			}
		}
	}
}
