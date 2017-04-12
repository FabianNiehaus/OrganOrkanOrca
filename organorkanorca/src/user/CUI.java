package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

import data_objects.Kunde;
import data_objects.Person;
import data_objects.Mitarbeiter;
import domain.eShopCore;
import domain.exceptions.LoginFailedException;
import util.IO;

public class CUI {
	
	private eShopCore eShop;
	private BufferedReader in;
	private BufferedWriter out;
	private Person user = null; 
	
	public CUI(String datei) throws IOException {

		eShop = new eShopCore();

		// Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new BufferedWriter(new OutputStreamWriter(System.out));
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
			System.out.println(lfe.getMessage());
			System.out.println("Bitte noch einmal versuchen!");
		}
	}
	
	public void gibMenueAus(){
		if((user instanceof Kunde)){
			// Menüeingaben für Kunde
		} else if((user instanceof Mitarbeiter)) {
			// Menüeingaben für Mitarbeiter
			IO.println("eShop Hauptseite");
			IO.println("Eingabe \"a\" um alle Artikel auszugeben");
			IO.println("Eingabe \"k\" um alle Kunden auszugeben");
			IO.println("Eingabe \"m\" um alle Mitarbeiter auszugeben");
			IO.println("Eingabe \"q\" um alle den eShop zu beenden");
		} else {
			login();
		}
	}
	
	private String liesEingabe() throws IOException {
		return in.readLine();
	}
	
	public void verarbeiteEingabe(String input) throws IOException{
		switch(input){
		case "a": artikelAusgeben(eShop.alleArtikelAusgeben()); break;
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
				input = liesEingabe();
				verarbeiteEingabe(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!input.equals("q"));
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
	
	public void artikelAusgeben(Vector liste){
		if (liste.isEmpty()) {
			System.out.println("Keine Artikel auszugeben!");
		} else {
			for(Object artikel : liste){
				IO.println(artikel.toString());
			}
		}
	}
}
