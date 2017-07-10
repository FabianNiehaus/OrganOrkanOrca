package eshop.server.domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.server.persistence.FilePersistenceManager;
import eshop.server.persistence.PersistenceManager;

/**
 * @author Fabian Niehaus Klasse zur Verwaltung von Artikeln
 */
public class Artikelverwaltung {

	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();
	// Vektor zur Speicherug der Artikel
	private Vector<Artikel> artikel = new Vector<Artikel>(0);

	/**
	 * Artikel in Liste der Verwalteten Artikel einfuegen
	 * 
	 * @param art
	 *           Einzufuegender Artikel
	 */
	public void einfuegen(Artikel art) {

		try {
			sucheArtikel(art.getArtikelnummer());
		} catch (ArticleNonexistantException anne) {
			artikel.add(art);
		}
	}

	/**
	 * Erhöht den Bestand eines Artikels anhand der Artikelnummer
	 * 
	 * @param artikelnummer
	 *           Artikelnummer des gesuchten Artikels
	 * @param bestand
	 *           Neuer Bestand
	 * @return Gesuchter Artikel
	 * @throws ArticleNonexistantException
	 *            Artikelnummer nicht vorhanden
	 * @throws InvalidAmountException
	 */
	public Artikel erhoeheBestand(Artikel art, int bestand) throws InvalidAmountException {

		if (art instanceof Massengutartikel) {
			Massengutartikel tmp = (Massengutartikel) art;
			if (bestand % tmp.getPackungsgroesse() != 0) {
				throw new InvalidAmountException(tmp);
			} else {
				tmp.setBestand(tmp.getBestand() + bestand);
			}
		} else {
			art.setBestand(art.getBestand() + bestand);
		}
		return art;
	}

	/**
	 * Erstellt einen neuen Artikel und fuegt ihn in die Liste der verwalteten
	 * Artikel ein.
	 * 
	 * @param bezeichnung
	 *           Artikelbezeichnung
	 * @param bestand
	 *           Artikelbestand
	 * @param preis
	 *           Artikelpreis
	 * @return Erstellter Artikel
	 * @throws InvalidAmountException
	 */
	public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, int packungsgroesse)
			throws InvalidAmountException {

		if (packungsgroesse == 1) {
			Artikel art = new Artikel(bezeichnung, getNextID(), bestand, preis, null);
			artikel.add(art);
			art.aktualisiereBestandsverlauf();
			return art;
		}
		if (packungsgroesse > 1) {
			if (bestand % packungsgroesse != 0) {
				throw new InvalidAmountException(bestand);
			} else {
				Massengutartikel art = new Massengutartikel(bezeichnung, getNextID(), bestand, preis, packungsgroesse,
						null);
				artikel.add(art);
				art.aktualisiereBestandsverlauf();
				return art;
			}
		} else {
			throw new InvalidAmountException();
		}
	}

	/**
	 * Gibt alle verwalteten Artikel zurueck
	 * 
	 * @return Verwatlete Artikel
	 */
	public Vector<Artikel> getArtikel() {

		return artikel;
	}

	/**
	 * Erzeugt die nächste zu verwendende Artikelnummer
	 * 
	 * @return Nächste Artikelnummer
	 */
	public int getNextID() {

		int hoechsteID = 0;
		for (Artikel art : artikel) {
			if (art.getArtikelnummer() > hoechsteID) {
				hoechsteID = art.getArtikelnummer();
			}
		}
		return hoechsteID + 1;
	}

	/**
	 * Methode zum Einlesen von Artikeln aus einer Datei.
	 * 
	 * @param datei
	 *           Datei, die einzulesenden Artikelbestand enthält
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException {

		// PersistenzManager fuer Lesevorgänge öffnen
		pm.openForReading(datei);
		Artikel art;
		do {
			// Artikel-Objekt einlesen
			art = pm.ladeArtikel();
			if (art != null) {
				// Artikel in Artikelliste einfuegen
				einfuegen(art);
			}
		} while (art != null);
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	public void loeschen(Artikel art) {

		artikel.remove(art);
	}

	/**
	 * Methode zum Schreiben der Artikeldaten in eine Datei.
	 * 
	 * @param datei
	 *           Datei, in die der Artikelbestand geschrieben werden soll
	 * @throws IOException
	 */
	public void schreibeDaten(String datei) throws IOException {

		// PersistenzManager fuer Schreibvorgänge öffnen
		pm.openForWriting(datei);
		if (!artikel.isEmpty()) {
			Iterator<Artikel> iter = artikel.iterator();
			while (iter.hasNext()) {
				Artikel art = iter.next();
				art.aktualisiereBestandsverlauf();
				pm.speichereArtikel(art);
			}
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	/**
	 * Sucht anhand der Artikelnummer nach einem Artikel
	 * 
	 * @param artikelnummer
	 *           Artikelnummer des gesuchten Artikels
	 * @return Gesuchter Artikel
	 * @throws ArticleNonexistantException
	 *            Artikelnummer nicht vorhanden
	 */
	public Artikel sucheArtikel(int artikelnummer) throws ArticleNonexistantException {

		for (Artikel art : artikel) {
			if (art.getArtikelnummer() == artikelnummer) {
				return art;
			}
		}
		throw new ArticleNonexistantException(artikelnummer);
	}

	/**
	 * Sucht anhand einer (Teil-)Bezeichnung nach einem Artikel
	 * 
	 * @param bezeichnung
	 *           Gesuchte (Teil-)bezeichnung
	 * @return Liste der zur Bezeichnung passenden Artikel
	 * @throws ArticleNonexistantException
	 *            Keine Artikel gefunden
	 */
	public Vector<Artikel> sucheArtikel(String bezeichnung) throws ArticleNonexistantException {

		Vector<Artikel> liste = new Vector<Artikel>(0);
		bezeichnung = bezeichnung.toLowerCase();
		for (Artikel art : artikel) {
			if (art.getBezeichnung().toLowerCase().contains(bezeichnung)) {
				liste.add(art);
			}
		}
		if (liste.isEmpty()) {
			throw new ArticleNonexistantException(bezeichnung);
		} else {
			return liste;
		}
	}

	public Artikel aendereBezeichnung(Artikel art, String bezeichnung) throws ArticleNonexistantException {

		if (artikel.contains(art)) {
			art.setBezeichnung(bezeichnung);
			return art;
		} else {
			throw new ArticleNonexistantException(art.getArtikelnummer());
		}
	}

	public Artikel aenderePreis(Artikel art, double preis) throws ArticleNonexistantException {

		if (artikel.contains(art)) {
			art.setPreis(preis);
			return art;
		} else {
			throw new ArticleNonexistantException(art.getArtikelnummer());
		}
	}
}
