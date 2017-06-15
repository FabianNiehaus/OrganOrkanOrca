package domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import data_objects.Artikel;
import data_objects.Massengutartikel;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.InvalidAmountException;
import persistence.*;

/**
 * @author Fabian Niehaus
 * Klasse zur Verwaltung von Artikeln
 */
public class Artikelverwaltung {	
	
	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs verantwortlich ist
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
		// PersistenzManager fuer Lesevorgänge öffnen
		pm.openForReading(datei);

		Artikel art;
		do {
			// Artikel-Objekt einlesen
			art = pm.ladeArtikel();
			
			if (art!= null) {
				// Artikel in Artikelliste einfuegen
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
		// PersistenzManager fuer Schreibvorgänge öffnen
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
	 * Artikel in Liste der Verwalteten Artikel einfuegen
	 * @param art Einzufuegender Artikel
	 */
	public void einfuegen(Artikel art){
		try{
			sucheArtikel(art.getArtikelnummer());
		} catch (ArticleNonexistantException anne){
			artikel.add(art);
		}
	}
	
	/**
	 * Gibt alle verwalteten Artikel zurueck
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
	 * Erstellt einen neuen Artikel und fuegt ihn in die Liste der verwalteten Artikel ein.
	 * @param bezeichnung Artikelbezeichnung
	 * @param bestand Artikelbestand
	 * @param preis Artikelpreis
	 * @return Erstellter Artikel
	 * @throws InvalidAmountException 
	 */
	public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, int packungsgroesse) throws InvalidAmountException{
		if(packungsgroesse == 1){
			Artikel art = new Artikel(bezeichnung, getNextID(), bestand, preis, null);
			artikel.add(art);
			return art;
		} if(packungsgroesse > 1){
			Massengutartikel art = new Massengutartikel(bezeichnung, getNextID(), bestand, preis, packungsgroesse, null);
			artikel.add(art);
			return art;
		} else {
			throw new InvalidAmountException();
		}
	}
	
	/**
	 * Sucht anhand der Artikelnummer nach einem Artikel
	 * @param artikelnummer Artikelnummer des gesuchten Artikels
	 * @return Gesuchter Artikel
	 * @throws ArticleNonexistantException Artikelnummer nicht vorhanden
	 */
	public Artikel sucheArtikel(int artikelnummer) throws ArticleNonexistantException{
		for(Artikel art : artikel){
			if(art.getArtikelnummer() == artikelnummer){
				return art;
			}
		}
		throw new ArticleNonexistantException(artikelnummer);
	}
	
	/**
	 * Sucht anhand einer (Teil-)Bezeichnung nach einem Artikel
	 * @param bezeichnung Gesuchte (Teil-)bezeichnung
	 * @return Liste der zur Bezeichnung passenden Artikel
	 * @throws ArticleNonexistantException Keine Artikel gefunden
	 */
	public Vector<Artikel> sucheArtikel(String bezeichnung) throws ArticleNonexistantException{
		Vector<Artikel> liste = new Vector<Artikel>(0);
		bezeichnung = bezeichnung.toLowerCase();
		
		for(Artikel art : artikel){
			if(art.getBezeichnung().toLowerCase().contains(bezeichnung)){
				liste.add(art);
			}
		}
		
		if (liste.isEmpty()){
			throw new ArticleNonexistantException(bezeichnung);
		} else {
			return liste;
		}
	}
	
	/**
	 * Erhöht den Bestand eines Artikels anhand der Artikelnummer
	 * @param artikelnummer Artikelnummer des gesuchten Artikels
	 * @param bestand Neuer Bestand
	 * @return Gesuchter Artikel
	 * @throws ArticleNonexistantException Artikelnummer nicht vorhanden
	 * @throws InvalidAmountException 
	 */
	public Artikel erhoeheBestand(int artikelnummer, int bestand) throws ArticleNonexistantException, InvalidAmountException{
		try{
			Artikel art = sucheArtikel(artikelnummer);
			if(art instanceof Massengutartikel){
				Massengutartikel tmp = (Massengutartikel) art;
				if(bestand % tmp.getPackungsgroesse() != 0){
					throw new InvalidAmountException(tmp);
				} else {
					tmp.setBestand(tmp.getBestand() + bestand);
				}
			} else {
				art.setBestand(art.getBestand() + bestand);
			}
			return art;
		} catch (ArticleNonexistantException anne){
			throw new ArticleNonexistantException(artikelnummer);
		}
		
	}
	
	public void loeschen(Artikel art){
		artikel.remove(art);
	}
}
