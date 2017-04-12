package user;

import java.io.IOException;
import java.util.Vector;

import data_objects.Artikel;
import data_objects.Kunde;
import data_objects.Person;

import data_objects.Mitarbeiter;
import domain.eShopCore;
import domain.exceptions.LoginFailedException;
import util.IO;
import util.StringComparator;

public class CUI {
	
	private eShopCore eShop;
	private Person user = null; 
	
	public CUI(String datei) throws IOException {

		eShop = new eShopCore();

	}
	
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
	
	private void artikelSuchen(Vector liste){
		String searchType = "";
		
		IO.println("Artikel suchen");
		IO.println("Eingabe \"nr\" um nach Artikelnummer zu suchen");
		IO.println("Eingabe \"bez\" um nach Bezeichnung zu suchen");
		IO.println("---------------------------------------------------------");
		
		searchType = IO.readString();
		
		if(searchType.equals("nr")){
			//Logik Suche nach Artikelnummer
		} else if(searchType.equals("bez")){
			//Logik Suche nach Artikelbezeichnung
		}
	}
	
	private void artikelSortiertAusgeben(Vector liste){
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
						if(artSort[k].getArtikelNr() > artSort[k+1].getArtikelNr()){
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
	
	private void artikelErstellen(){
		
		IO.println("Artikel erstellen");
		IO.print("Bezeichnung:");
		String bezeichnung = IO.readString();
		IO.print("Bestand:");
		int bestand = IO.readInt();
		IO.print("Preis");
		double preis = IO.readDouble();
		IO.println("-----------------------");
		
		Artikel art = eShop.erstelleArtikel(bezeichnung, bestand, preis);
		
		IO.println(art.toString());
	}
	
	private void artikelBestandErhoehen(){		
		IO.print("Artikelnummer:");
		int artikelnummer = IO.readInt();
		IO.print("Bestand erhöhen um:");
		int bestand = IO.readInt();
		IO.println("-----------------------");
		
		Artikel art = eShop.erhoeheArtikelBestand(artikelnummer, bestand);
		
		IO.println(art.toString());
		
	}
	
	private void artikelInWarenkorbLegen(){
		IO.print("Artikelnummer:");
		int artikelnummer = IO.readInt();
		IO.print("Anzahl:");
		int anzahl = IO.readInt();
		IO.println("-----------------------");
		
		eShop.artikelInWarenkorbLegen(artikelnummer, anzahl, user);
		
		gibWarenkorbAus();
	}
	
	private void gibWarenkorbAus(){
		IO.println("Warenkorb");
		IO.println(eShop.warenkorbAusgeben(user).toString());
	}
	
	private void aendereArtiklInWarenkorb(){
		gibWarenkorbAus();
		
		IO.println("Artikel anpassen");
		IO.print("Position eingeben:");
		
		int pos = IO.readInt();
		
		IO.print("Anzahl eingeben:");
		
		int anz = IO.readInt();
		
		eShop.artikelInWarenkorbAendern(pos, anz, user);
		
		gibWarenkorbAus();
	}
	
	private void gibWarenkorbverwaltungAus(){
		String input = "";
		
		do{
			IO.println("");		
			
			IO.println("Eingabe \"w\" um Warenkorb auszugeben");			IO.println("Eingabe \"k\" um Artikel in Warenkorb zu legen");
			IO.println("Eingabe \"a\" um Artikel im Warenkorb zu ändern");
			IO.println("Eingabe \"l\" um Warenkorb zu leeren");
			IO.println("Eingabe \"q\" um zum Hauptmenü zurückzukehren");
			IO.println("---------------------------------------------------------------------");
			
			input = IO.readString();
			
			switch(input){
			case "w": gibWarenkorbAus(); break;
			case "k": artikelInWarenkorbLegen(); break;
			case "a": aendereArtiklInWarenkorb(); break;
			case "l": eShop.warenkorbLeeren(user); break;
			}
		} while (!input.equals("q"));
	}
	
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
			case "a": artikelAusgeben(eShop.alleArtikelAusgeben()); break;
			case "s": artikelSortiertAusgeben(eShop.alleArtikelAusgeben()); break;
			case "e": artikelErstellen(); break;
			case "b": artikelBestandErhoehen(); break;
			case "k": artikelInWarenkorbLegen(); break;
			}
				
		} while (!input.equals("q"));
		
	}
	
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
			}
			
			IO.println("Eingabe \"q\" um den eShop zu beenden");
		} else {
			login();
		}
	}
	
	public void verarbeiteEingabe(String input) throws IOException{
		switch(input){
		case "a": gibArtikelverwaltungAus(); break;
		case "k": artikelAusgeben(eShop.alleKundenAusgeben()); break;
		case "m": artikelAusgeben(eShop.alleMitarbeiterAusgeben()); break;
		case "w": gibWarenkorbverwaltungAus(); break;
		}
	}
	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!input.equals("q"));
		
		IO.println("OrganOrkanOrca wurde beendet.\nAuf Wiedersehen!");
	}
	
	public static void main(String[] args){
		
		CUI cui;
		try {
			cui = new CUI("eShop");
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
	public void artikelAusgeben(Vector liste){
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
	public void  mitarbeiterAusgeben(Vector liste){
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
	public void  kundenAusgeben(Vector liste){
		if (liste.isEmpty()) {
			System.out.println("Keine Kunden auszugeben!");
		} else {
			for(Object kunde : liste){
				IO.println(kunde.toString());
			}
		}
	}
}
