package user;

import domain.Artikelverwaltung;
import domain.Kundenverwaltung;
import domain.Mitarbeiterverwaltung;
import domain.Warenkorbverwaltung;
import domain.eShopCore;

import util.IO;

public class CUI {
	
	private static String input = "";
	private boolean loggedIn;
	
	public static void main(String[] args){
		
		eShopCore eShop = new eShopCore(
				new Artikelverwaltung(),
				new Kundenverwaltung(),
				new Mitarbeiterverwaltung(),
				new Warenkorbverwaltung()
		);
		
		IO.println("Willkommen bei OrganOrkanOrca.org.");
		IO.println("Bitte melden Sie Sich an.");
		IO.print("Vorname: ");
		String firstname = IO.readString();
		IO.print("Nachname: ");
		String lastname = IO.readString();
		IO.print("ID: ");
		int id = IO.readInt();
		
		IO.println(eShop.anmelden(firstname, lastname, id));
		
		while(!input.equals("quit") || !input.equals("exit")){
			
			IO.println("eShop Hauptseite");
			IO.println("Eigabe \"a\" um alle Artikel auszugeben");
			IO.println("Eigabe \"k\" um alle Kunden auszugeben");
			IO.println("Eigabe \"m\" um alle Mitarbeiter auszugeben");
			IO.println("Eigabe \"quit\" oder \"exit\" um alle Mitarbeiter auszugeben");
			
			input = IO.readString();
			
			
		}
	}
}
