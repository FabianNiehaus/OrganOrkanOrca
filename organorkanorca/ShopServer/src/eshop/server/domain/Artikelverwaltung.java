package eshop.server.domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.server.persistence.FilePersistenceManager;
import eshop.server.persistence.PersistenceManager;

// TODO: Auto-generated Javadoc
/**
 * The Class Artikelverwaltung.
 */
public class Artikelverwaltung {

	/** The artikel. */
	// Vektor zur Speicherug der Artikel
	private Vector<Artikel> artikel = new Vector<Artikel>(0);
	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	/** The pm. */
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	/**
	 * Aendere bezeichnung.
	 *
	 * @param art
	 *           the artikel
	 * @param bezeichnung
	 *           the bezeichnung
	 * @return the artikel
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 */
	public Artikel aendereBezeichnung(Artikel art, String bezeichnung) throws ArticleNonexistantException {

		if (artikel.contains(art)) {
			art.setBezeichnung(bezeichnung);
			return art;
		} else {
			throw new ArticleNonexistantException(art.getArtikelnummer());
		}
	}

	/**
	 * Aendere preis.
	 *
	 * @param art
	 *           the artikel
	 * @param preis
	 *           the preis
	 * @return the artikel
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 */
	public Artikel aenderePreis(Artikel art, double preis) throws ArticleNonexistantException {

		if (artikel.contains(art)) {
			art.setPreis(preis);
			return art;
		} else {
			throw new ArticleNonexistantException(art.getArtikelnummer());
		}
	}

	/**
	 * Einfuegen.
	 *
	 * @param art
	 *           the artikel
	 */
	public void einfuegen(Artikel art) {

		try {
			sucheArtikel(art.getArtikelnummer());
		} catch (ArticleNonexistantException anne) {
			artikel.add(art);
		}
	}

	/**
	 * Erhoehe bestand.
	 *
	 * @param art
	 *           the artikel
	 * @param bestand
	 *           the bestand
	 * @return the artikel
	 * @throws InvalidAmountException
	 *            the invalid amount exception
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
	 * Erstelle artikel.
	 *
	 * @param bezeichnung
	 *           the bezeichnung
	 * @param bestand
	 *           the bestand
	 * @param preis
	 *           the preis
	 * @param packungsgroesse
	 *           the packungsgroesse
	 * @param artikelinfo
	 *           the artikelinfo
	 * @param picture
	 *           the picture
	 * @return the artikel
	 * @throws InvalidAmountException
	 *            the invalid amount exception
	 */
	public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, int packungsgroesse,
			String artikelinfo, String picture) throws InvalidAmountException {

		if (picture.equals("")) {
			picture = "pictures/orkan.jpg";
		}
		if (packungsgroesse == 1) {
			Artikel art = new Artikel(bezeichnung, getNextID(), bestand, preis, new LinkedHashMap<Integer, Integer>(),
					artikelinfo, picture);
			artikel.add(art);
			art.aktualisiereBestandsverlauf();
			return art;
		}
		if (packungsgroesse > 1) {
			if (bestand % packungsgroesse != 0) {
				throw new InvalidAmountException(bestand);
			} else {
				Massengutartikel art = new Massengutartikel(bezeichnung, getNextID(), bestand, preis, packungsgroesse,
						new LinkedHashMap<Integer, Integer>(), artikelinfo, "pictures/orkan.jpg");
				artikel.add(art);
				art.aktualisiereBestandsverlauf();
				return art;
			}
		} else {
			throw new InvalidAmountException();
		}
	}

	/**
	 * Gets the artikel.
	 *
	 * @return the artikel
	 */
	public Vector<Artikel> getArtikel() {

		return artikel;
	}

	/**
	 * Gets the next ID.
	 *
	 * @return the next ID
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
	 * Lies daten.
	 *
	 * @param datei
	 *           the datei
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
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

	/**
	 * Loeschen.
	 *
	 * @param art
	 *           the artikel
	 */
	public void loeschen(Artikel art) {

		artikel.remove(art);
	}

	/**
	 * Schreibe daten.
	 *
	 * @param datei
	 *           the datei
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
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
	 * Suche artikel.
	 *
	 * @param artikelnummer
	 *           the artikelnummer
	 * @return the artikel
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
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
	 * Suche artikel.
	 *
	 * @param bezeichnung
	 *           the bezeichnung
	 * @return the vector
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
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
}
