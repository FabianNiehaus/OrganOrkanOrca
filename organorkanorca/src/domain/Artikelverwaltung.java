package domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import data_objects.Artikel;
import domain.exceptions.ArticleNumberNonexistantException;
import persistence.*;

/**
 * @author Fabian Niehaus
 * Klasse zur Verwaltung von Artikeln
 */
public class Artikelverwaltung {	
	
	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();
	
	//Vektor zur Speicherug der Artikel
	private Vector<Artikel> artikel = new Vector<Artikel>(0);
	
	/**
	 * Methode zum Einlesen von Artikeln aus einer Datei.
	 * 
	 * @param datei Datei, die einzulesenden Artikelbestand enthält
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException {
		// PersistenzManager für Lesevorgänge öffnen
		pm.openForReading(datei);

		Artikel art;
		do {
			// Artikel-Objekt einlesen
			art = pm.ladeArtikel();
			
			if (art!= null) {
				// Artikel in Artikelliste einfügen
				einfuegen(art);
			}
		} while (art != null);

		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	/**
	 * Methode zum Schreiben der Artikeldaten in eine Datei.
	 * 
	 * @param datei Datei, in die der Artikelbestand geschrieben werden soll
	 * @throws IOException
	 */
	public void schreibeDaten(String datei) throws IOException  {
		// PersistenzManager für Schreibvorgänge öffnen
		pm.openForWriting(datei);

		if (!artikel.isEmpty()) {
			Iterator<Artikel> iter = artikel.iterator();
			while (iter.hasNext()) {
				Artikel art = (Artikel) iter.next();
				pm.speichereArtikel(art);				
			}
		}
		
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	/**
	 * Artikel in Liste der Verwalteten Artikel einfügen
	 * @param art Einzufügender Artikel
	 */
	public void einfuegen(Artikel art){
		try{
			sucheArtikel(art.getArtikelnummer());
		} catch (ArticleNumberNonexistantException anne){
			artikel.add(art);
		}
	}
	
	/**
	 * Gibt alle verwalteten Artikel zurück
	 * @return Verwatlete Artikel
	 */
	public Vector<Artikel> getArtikel() {
		return artikel;
	}
	
	/**
	 * Erzeugt die nächste zu verwendende Artikelnummer
	 * @return Nächste Artikelnummer
	 */
	public int getNextID() {
		int hoechsteID = 0;
		for(Artikel art : artikel){
			if(art.getArtikelnummer() > hoechsteID){
				hoechsteID = art.getArtikelnummer() ;
			}
		}		
		return hoechsteID+1;
	}
	
	
	/**
	 * Erstellt einen neuen Artikel und fügt ihn in die Liste der verwalteten Artikel ein.
	 * @param bezeichnung Artikelbezeichnung
	 * @param bestand Artikelbestand
	 * @param preis Artikelpreis
	 * @return Erstellter Artikel
	 */
	public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis){
		Artikel art = new Artikel(bezeichnung, getNextID(), bestand, preis);
		artikel.add(art);
		return art;
	}
	
	/**
	 * Sucht anhand der Artikelnummer nach einem Artikel
	 * @param artikelnummer Artikelnummer des gesuchten Artikels
	 * @return Gesuchter Artikel
	 * @throws ArticleNumberNonexistantException Artikelnummer nicht vorhanden
	 */
	public Artikel sucheArtikel(int artikelnummer) throws ArticleNumberNonexistantException{
		for(Artikel art : artikel){
			if(art.getArtikelnummer() == artikelnummer){
				return art;
			}
		}
		throw new ArticleNumberNonexistantException(artikelnummer);
	}
	
	/**
	 * Erhöht den Bestand eines Artikels anhand der Artikelnummer
	 * @param artikelnummer Artikelnummer des gesuchten Artikels
	 * @param bestand Neuer Bestand
	 * @return Gesuchter Artikel
	 * @throws ArticleNumberNonexistantException Artikelnummer nicht vorhanden
	 */
	public Artikel erhoeheBestand(int artikelnummer, int bestand) throws ArticleNumberNonexistantException{
		try{
			Artikel art = sucheArtikel(artikelnummer);
			art.setBestand(art.getBestand() + bestand);
			return art;
		} catch (ArticleNumberNonexistantException anne){
			throw new ArticleNumberNonexistantException(artikelnummer);
		}
		
	}
	
}
