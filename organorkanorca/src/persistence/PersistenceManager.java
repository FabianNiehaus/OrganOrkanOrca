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

	public void openForReading(String datenquelle) throws IOException;
	
	public void openForWriting(String datenquelle) throws IOException;
	
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

	/*
	 *  Wenn später mal eine Kundenverwaltung ergänzt wird:

	public Kunde ladeKunde() throws IOException;

	public boolean speichereKunde(Kunde k) throws IOException;
	
	*/
}