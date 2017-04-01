package user;

import domain.Artikelverwaltung;
import domain.Kundenverwaltung;
import domain.Mitarbeiterverwaltung;
import domain.Warenkorbverwaltung;
import domain.eShopCore;

import util.IO;

public class CUI {
	
	private static String input;
	
	public static void main(String[] args){
		
		eShopCore eShop = new eShopCore(
				new Artikelverwaltung(),
				new Kundenverwaltung(),
				new Mitarbeiterverwaltung(),
				new Warenkorbverwaltung()
		);
		
		while(!input.equals("quit") || !input.equals("exit") ){
			
			IO.println("eShop Hauptseite");
			IO.println("Eigabe \"a\" um alle Artikel auszugeben");
			IO.println("Eigabe \"k\" um alle Kunden auszugeben");
			IO.println("Eigabe \"m\" um alle Mitarbeiter auszugeben");
			IO.println("Eigabe \"quit\" oder \"exit\" um alle Mitarbeiter auszugeben");
			
			input = IO.readString();
			
			switch (input) {
				case "a": 	eShop.alleArtikelAusgeben();
							break;
				case "k":	eShop.alleKundenAusgeben();
							break;
				case "m":	eShop.alleMitarbeiterAusgeben();
							break;
			}
		}
	}
}
