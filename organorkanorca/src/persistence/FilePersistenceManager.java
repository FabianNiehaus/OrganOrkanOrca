package persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import data_objects.*;

/**
 * @author teschke
 *
 * Realisierung einer Schnittstelle zur persistenten Speicherung von
 * Daten in Dateien.
 * @see bib.local.persistence.PersistenceManager
 */
public class FilePersistenceManager implements PersistenceManager {

	private BufferedReader reader = null;
	private PrintWriter writer = null;
	
	/* (non-Javadoc)
	 * @see persistence.PersistenceManager#openForReading(java.lang.String)
	 */
	public void openForReading(String datei) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(datei));
	}

	/* (non-Javadoc)
	 * @see persistence.PersistenceManager#openForWriting(java.lang.String)
	 */
	public void openForWriting(String datei) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
	}

	/* (non-Javadoc)
	 * @see persistence.PersistenceManager#close()
	 */
	public boolean close() {
		if (writer != null)
			writer.close();
		
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return false;
			}
		}

		return true;
	}

	/**
	 * Methode zum Einlesen der Artikeldaten aus einer externen Datenquelle.
	 * Das Verfügbarkeitsattribut ist in der Datenquelle (Datei) als "t" oder "f"
	 * codiert abgelegt.
	 * 
	 * @return Artikel-Objekt, wenn Einlesen erfolgreich, false null
	 */
	public Artikel ladeArtikel() throws IOException {
		/*
		//Artikel-Header suchen
		while(!liesZeile().equals("<---ARTIKEL--->"));{}
		*/
		
		int artikelnummer = 0;
		String bezeichnung = "";
		double preis = 0;
		int bestand = 0;
		
		//Lies Artikelnummer
		try{
			artikelnummer = Integer.parseInt(liesZeile());
		} catch (NumberFormatException nfe) {
			//Abbruch wenn Leerzeile -> keine Artikel mehr vorhanden
			return null;
		}
				
		//Lies Artikelbezeichnung
		bezeichnung = liesZeile();
		
		//Lies Artikel-Preis
		preis = Double.parseDouble(liesZeile());
		
		//Lies Artikel-Bestand
		bestand = Integer.parseInt(liesZeile());
		
		/*
		while(!liesZeile().equals("<---END ARTIKEL--->"));{}
		*/
		
		return new Artikel(bezeichnung, artikelnummer, bestand, preis);
	}

	/**
	 * Methode zum Schreiben der Artikeldaten in eine externe Datenquelle.
	 * Das Verfügbarkeitsattribut wird in der Datenquelle (Datei) als "t" oder "f"
	 * codiert abgelegt.
	 * 
	 * @param art Artikel-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereArtikel(Artikel art) throws IOException {
		/*
		//Schreibe Artikel-Header
		schreibeZeile("<---ARTIKEL--->");
		*/
		
		//Schreibe Artikelnummer
		schreibeZeile(String.valueOf(art.getArtikelnummer()));
		//Schreibe Artikelbezeichnung
		schreibeZeile(art.getBezeichnung());
		//Schreibe Preis
		schreibeZeile(String.valueOf(art.getPreis()));
		//Schreibe Bestand
		schreibeZeile(String.valueOf(art.getBestand()));
		
		/*
		//Schreibe Artikel-Limiter
		schreibeZeile("<---END ARTIKEL--->");
		*/
		
		return true;
	}
	
	/**
	 * @author Mathis M�hlenkamp
	 */
	public Kunde ladeKunde() throws IOException {
		/*
		//Kunden-Header suchen
		while(!liesZeile().equals("<---Kunden--->"));{}
		*/
		int id = 0;
		String firstname = "";
		String lastname = "";
		String passwort = "";
		Warenkorb warenkorb = new Warenkorb();  //richtig??
		
		//Lies ID
		try{
			id = Integer.parseInt(liesZeile());
		} catch (NumberFormatException nfe) {
			//Abbruch wenn Leerzeile -> keine Kunden mehr vorhanden
			return null;
		}
				
		//Lies firstname & lastname
		firstname = liesZeile();
		lastname = liesZeile();
		
		//Lies passwort
		passwort = liesZeile();
		
		/*
		while(!liesZeile().equals("<---END Kunden--->"));{}
		*/
		
		return new Kunde(firstname, lastname, id, passwort, warenkorb);
	}
	
	public boolean speichereKunde(Kunde art) throws IOException {
		/*
		//Schreibe Kunden-Header
		schreibeZeile("<---ARTIKEL--->");
		*/
		
		//Schreibe ID
		schreibeZeile(String.valueOf(art.getId()));
		//Schreibe firstname
		schreibeZeile(art.getFirstname());
		//Schreibe lastname
		schreibeZeile(art.getLastname());
		//Schreibe passwort
		schreibeZeile(art.getPasswort());
		//Schreibe Warenkorb
		schreibeZeile(String.valueOf(art.getWarenkorb()));
		
		/*
		//Schreibe Kunden-Limiter
		schreibeZeile("<---END ARTIKEL--->");
		*/
		
		return true;
	}

	/**
	 * Liest eine Zeile aus
	 * @return Inhalt der Zeile als String
	 * @throws IOException
	 */
	private String liesZeile() throws IOException {
		if (reader != null)
			try{
				return reader.readLine();
			} catch (IOException ie){
				return "";
			}
		else
			return "";
	}

	/**
	 * Schreibt eine Zeile
	 * @param daten Zu schreibende Zeile als String
	 */
	private void schreibeZeile(String daten) {
		if (writer != null)
			writer.println(daten);
	}
}
