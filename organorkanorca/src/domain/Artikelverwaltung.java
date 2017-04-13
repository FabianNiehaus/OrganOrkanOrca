package domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import data_objects.Artikel;
import data_objects.Kunde;
import domain.exceptions.ArticleNumberNonexistantException;
import persistence.*;

public class Artikelverwaltung {
	
	public Artikelverwaltung(){
		
	}
	
	
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
			Iterator iter = artikel.iterator();
			while (iter.hasNext()) {
				Artikel art = (Artikel) iter.next();
				pm.speichereArtikel(art);				
			}
		}
		
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	public void einfuegen(Artikel art){
		try{
			sucheArtikel(art.getArtikelnummer());
		} catch (ArticleNumberNonexistantException anne){
			artikel.add(art);
		}
	}
	
	public Vector<Artikel> getArtikel() {
		return artikel;
	}

	public void setArtikel(Vector<Artikel> artikel) {
		this.artikel = artikel;
	}
	
	public int getNextID() {
		int hoechsteID = 0;
		for(Artikel art : artikel){
			if(art.getArtikelnummer() > hoechsteID){
				hoechsteID = art.getArtikelnummer() ;
			}
		}		
		return hoechsteID+1;
	}
	
	public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis){
		Artikel art = new Artikel(bezeichnung, getNextID(), bestand, preis);
		artikel.add(art);
		return art;
	}
	
	public Artikel sucheArtikel(int artikelnummer) throws ArticleNumberNonexistantException{
		for(Artikel art : artikel){
			if(art.getArtikelnummer() == artikelnummer){
				return art;
			}
		}
		throw new ArticleNumberNonexistantException();
	}
	
	public Artikel erhoeheBestand(int artikelnummer, int bestand) throws ArticleNumberNonexistantException{
		try{
			Artikel art = sucheArtikel(artikelnummer);
			art.setBestand(art.getBestand() + bestand);
			return art;
		} catch (ArticleNumberNonexistantException anne){
			throw new ArticleNumberNonexistantException();
		}
		
	}
	
}
