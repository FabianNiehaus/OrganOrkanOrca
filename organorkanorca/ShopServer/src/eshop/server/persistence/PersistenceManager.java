package eshop.server.persistence;

import java.io.IOException;
import java.util.Vector;

import eshop.common.data_objects.*;
import eshop.common.exceptions.InvalidPersonDataException;

/**
 * @author teschke
 *
 * Allgemeine Schnittstelle fuer den Zugriff auf ein Speichermedium
 * (z.B. Datei oder Datenbank) zum Ablegen von beispielsweise
 * Buecher- oder Kundendaten.
 * 
 * Das Interface muss von Klassen implementiert werden, die eine
 * Persistenz-Schnittstelle realisieren wollen.
 */
public interface PersistenceManager {

	/**
	 * Erstellt neuen Reader fuer jeweiligen Persistenz-Typ
	 * @param datenquelle Datenquelle des Readers
	 * @throws IOException
	 */
	public void openForReading(String datenquelle) throws IOException;
	
	/**
	 * Erstellt neuen Writer fuer jeweiligen Persistenz-Typ
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
	 * @throws InvalidPersonDataException 
	 * 
	 */
	public Kunde ladeKunde() throws IOException, InvalidPersonDataException;
	
	/**
	 * Methode zum Schreiben der Kundendaten in eine externe Datenquelle.
	 * 
	 * @param b Kunden-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereKunde(Kunde art) throws IOException;
	
	/** 
	 * @author Mathis M�hlenkamp
	 * 
	 * Methode zum Einlesen der Mitarbeiterdaten aus einer externen Datenqulle.
	 * 
	 * @return Mitarbeiter-Objekt, wenn Einlesen erfolgreich, false null
	 * @throws InvalidPersonData 
	 * 
	 */
	public Mitarbeiter ladeMitarbeiter() throws IOException, InvalidPersonDataException;
	
	/**
	 * Methode zum Schreiben der Mitarbeiterdaten in eine externe Datenquelle.
	 * 
	 * @param b Mitarbeiter-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereMitarbeiter(Mitarbeiter mi) throws IOException;
	
	public Vector<Object> ladeEreignis() throws IOException;
	
	public boolean speichereEreignis(Ereignis er) throws IOException;
	


}