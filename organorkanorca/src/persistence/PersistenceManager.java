package persistence;

import java.io.IOException;

import data_objects.*;

/**
 * @author teschke
 *
 * Allgemeine Schnittstelle für den Zugriff auf ein Speichermedium
 * (z.B. Datei oder Datenbank) zum Ablegen von beispielsweise
 * Bücher- oder Kundendaten.
 * 
 * Das Interface muss von Klassen implementiert werden, die eine
 * Persistenz-Schnittstelle realisieren wollen.
 */
public interface PersistenceManager {

	/**
	 * Erstellt neuen Reader für jeweiligen Persistenz-Typ
	 * @param datenquelle Datenquelle des Readers
	 * @throws IOException
	 */
	public void openForReading(String datenquelle) throws IOException;
	
	/**
	 * Erstellt neuen Writer für jeweiligen Persistenz-Typ
	 * @param datenquelle Datenquelle des Writers
	 * @throws IOException
	 */
	public void openForWriting(String datenquelle) throws IOException;
	
	/**
	 * Schließt / 'löscht' den Writer & Writer
	 * @return <b>true</b>, wenn Schließen erfolgreich, sonst <b>false</b>
	 */
	public boolean close();

	/**
	 * Methode zum Einlesen der Artikeldaten aus einer externen Datenquelle.
	 * 
	 * @return Artikel-Objekt, wenn Einlesen erfolgreich, false null
	 */
	public Artikel ladeArtikel() throws IOException;

	/**
	 * Methode zum Schreiben der Artikeldaten in eine externe Datenquelle.
	 * 
	 * @param b Artikel-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereArtikel(Artikel art) throws IOException;
	
	/** 
	 * @author Mathis M�hlenkamp
	 * 
	 * Methode zum Einlesen der Kundendaten aus einer externen Datenqulle.
	 * 
	 * @return Kunden-Objekt, wenn Einlesen erfolgreich, false null
	 * 
	 */
	public Kunde ladeKunde() throws IOException;
	
	/**
	 * Methode zum Schreiben der Kundendaten in eine externe Datenquelle.
	 * 
	 * @param b Kunden-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereKunde(Kunde art) throws IOException;

}