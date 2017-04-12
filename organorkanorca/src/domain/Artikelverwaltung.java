package domain;

import java.util.Vector;
import data_objects.Artikel;
import data_objects.Kunde;
import domain.exceptions.ArticleNumberNonexistantException;

public class Artikelverwaltung {
	
	public Artikelverwaltung(){
		artikel.add(new Artikel("Orkan", 2, 5, 99999));
		artikel.add(new Artikel("Orca", 3, 100, 5729.45));
		artikel.add(new Artikel("Organ", 1, 2, 1000.00));
	}

	private Vector<Artikel> artikel = new Vector<Artikel>(0);
	
	public Vector<Artikel> getArtikel() {
		return artikel;
	}

	public void setArtikel(Vector<Artikel> artikel) {
		this.artikel = artikel;
	}
	
	public int getNextID() {
		int hoechsteID = 0;
		for(Artikel art : artikel){
			if(art.getArtikelNr() > hoechsteID){
				hoechsteID = art.getArtikelNr() ;
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
			if(art.getArtikelNr() == artikelnummer){
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
