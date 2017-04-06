package user;

import data_objects.Kunde;
import data_objects.Person;
import domain.eShopCore;
import domain.exceptions.LoginFailedException;
import util.IO;

public class CUI {
	
	private static String input = "";
	private boolean loggedIn;
	
	public static void main(String[] args){
		
		eShopCore eShop = new eShopCore();
//				new Artikelverwaltung(),
//				new Kundenverwaltung(),
//				new Mitarbeiterverwaltung(),
//				new Warenkorbverwaltung()
//		);
		
		IO.println("Willkommen bei OrganOrkanOrca.org.");
		IO.println("Bitte melden Sie Sich an.");
		IO.print("Vorname: ");
		String firstname = IO.readString();
		IO.print("Nachname: ");
		String lastname = IO.readString();
		IO.print("ID: ");
		int id = IO.readInt();
		
		try {
			Person p = eShop.anmelden(firstname, lastname, id);
			String typ = (p instanceof Kunde) ? "Kunde" : "Mitarbeiter";
			IO.println(p.getFirstname() + " hat sich als " + typ + " eingeloggt.");
			IO.println(p.getFirstname() + " hat sich als " + p.getClass().getSimpleName() + " eingeloggt.");
		} catch (LoginFailedException lfe) {
			System.out.println(lfe.getMessage());
			System.out.println("Bitte noch einmal versuchen!");
		}
		
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
