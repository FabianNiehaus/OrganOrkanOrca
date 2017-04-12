package user;

import java.util.Vector;

import data_objects.Kunde;
import data_objects.Person;
import domain.eShopCore;
import domain.exceptions.LoginFailedException;
import util.IO;

public class CUI {
	
	private static String input = "";
	
	public static void main(String[] args){
		
		eShopCore eShop = new eShopCore();

		IO.println("Willkommen bei OrganOrkanOrca.org.");
		IO.println("Bitte melden Sie Sich an.");
		IO.print("ID: ");
		int id = IO.readInt();
		IO.print("Passwort: ");
		String passwort = IO.readString();
		
		try {
			Person p = eShop.anmelden(id, passwort);
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
			
			if(input.equals("a")){
				eShop.alleArtikelAusgeben();
			}
		}
	}
	
	public void artikelAusgeben(Vector<Object> liste){
		if (liste.isEmpty()) {
			System.out.println("Keine Artikel auszugeben!");
		} else {
			for(Object artikel : liste){
				IO.println(artikel.toString());
			}
		}
	}
}
