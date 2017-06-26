package eshop.server.domain;

import java.util.Map;
import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.data_objects.Warenkorb;
import eshop.common.exceptions.ArticleAlreadyInBasketException;
import eshop.common.exceptions.ArticleStockNotSufficientException;
import eshop.common.exceptions.BasketNonexistantException;
import eshop.common.exceptions.InvalidAmountException;

/**
 * @author Fabian Niehaus Klasse zur Verwaltung von Warenkörben
 */
public class Warenkorbverwaltung {

    private Vector<Warenkorb> warenkoerbe = new Vector<>();

    /**
     * Gibt einen Warenkorb aus, sofern dieser in der Verwaltung exisitert
     * 
     * @param wk
     *            Gesuchter Warenkorb
     * @return Gesuchter Warenkorb
     */
    public Warenkorb getWarenkorb(Warenkorb wk) throws BasketNonexistantException {

	for (Warenkorb ret : warenkoerbe) {
	    if (ret.equals(wk)) {
		return ret;
	    }
	}
	throw new BasketNonexistantException();
    }

    /**
     * Erzeugt einen neuen (leeren) Warenkorb
     * 
     * @return Erzeugter Warenkorb
     */
    public Warenkorb erstelleWarenkorb() {

	Warenkorb wk = new Warenkorb();
	warenkoerbe.addElement(wk);
	return wk;
    }

    /**
     * Löscht einen Warenkorb
     * 
     * @param wk
     *            Zu löschender Warenkorb
     */
    public void loescheWarenkorb(Warenkorb wk) {

	for (Warenkorb del : warenkoerbe) {
	    if (del.equals(wk)) {
		warenkoerbe.remove(del);
	    }
	}
    }

    /**
     * Ändert die Anzahl eines Artikels in einem Warenkorb. Die Artikelauswahl
     * erfolgt nach Position.
     * 
     * @param aend
     *            Zu bearbeitender Warenkorb
     * @param position
     *            Position des zu verändernden Artikels
     * @param anz
     *            Neue Anzahl
     * @throws InvalidAmountException
     * @throws Nicht
     *             genug Artikel auf Lager
     */
    public void aendereWarenkorb(Warenkorb wk, Artikel art, int anz)
	    throws ArticleStockNotSufficientException, BasketNonexistantException, InvalidAmountException {

	if (art.getBestand() >= anz) {
	    if (art instanceof Massengutartikel) {
		Massengutartikel tmp = (Massengutartikel) art;
		if (anz % tmp.getPackungsgroesse() != 0) {
		    throw new InvalidAmountException(tmp);
		} else {
		    wk.aendereAnzahl(art, anz);
		}
	    } else {
		wk.aendereAnzahl(art, anz);
	    }
	} else {
	    throw new ArticleStockNotSufficientException(art, anz);
	}
    }

    public void loescheAusWarenkorn(Warenkorb wk, Artikel art) {

	wk.loescheArtikel(art);
    }

    /**
     * Fuegt einem Warenkorb einen Artikel hinzu
     * 
     * @param wk
     *            Zu bearbeitender Warenkorb
     * @param art
     *            Hinzuzufuegender Artikel
     * @param anz
     *            Anzahl des Artikels
     * @throws ArticleStockNotSufficientException
     * @throws ArticleAlreadyInBasketException
     * @throws InvalidAmountException
     */
    public void legeInWarenkorb(Warenkorb wk, Artikel art, int anz)
	    throws ArticleStockNotSufficientException, ArticleAlreadyInBasketException, InvalidAmountException {

	if (art instanceof Massengutartikel) {
	    Massengutartikel tmp = (Massengutartikel) art;
	    if (anz % tmp.getPackungsgroesse() != 0) {
		throw new InvalidAmountException(tmp);
	    } else {
		wk.speichereArtikel(tmp, anz);
	    }
	} else {
	    wk.speichereArtikel(art, anz);
	}
    }

    /**
     * @return Alle Warenkörbe
     */
    public Vector<Warenkorb> getWarenkoerbe() {

	return warenkoerbe;
    }

    /**
     * Leert einen Warenkorb
     * 
     * @param wk
     *            Zu leerender Warenkorb
     */
    public void leereWarenkorb(Warenkorb wk) {

	wk.leereWarkenkorb();
    }

    /**
     * Gibt den Inhalt eines Warenkorbs aus
     * 
     * @param wk
     *            Gewuenschter Warenkorb
     * @return Alle Artikel mit Anzahl
     */
    public Map<Artikel, Integer> getArtikel(Warenkorb wk) {

	return wk.getArtikel();
    }
}
