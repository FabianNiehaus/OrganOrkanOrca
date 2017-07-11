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

// TODO: Auto-generated Javadoc
/**
 * The Class Warenkorbverwaltung.
 */
public class Warenkorbverwaltung {

	/** The warenkoerbe. */
	private Vector<Warenkorb> warenkoerbe = new Vector<>();

	/**
	 * Aendere warenkorb.
	 *
	 * @param wk
	 *           the wk
	 * @param art
	 *           the artikel
	 * @param anz
	 *           the anz
	 * @throws ArticleStockNotSufficientException
	 *            the article stock not sufficient exception
	 * @throws BasketNonexistantException
	 *            the basket nonexistant exception
	 * @throws InvalidAmountException
	 *            the invalid amount exception
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

	/**
	 * Erstelle warenkorb.
	 *
	 * @return the warenkorb
	 */
	public Warenkorb erstelleWarenkorb() {

		Warenkorb wk = new Warenkorb();
		warenkoerbe.addElement(wk);
		return wk;
	}

	/**
	 * Gets the artikel.
	 *
	 * @param wk
	 *           the wk
	 * @return the artikel
	 */
	public Map<Artikel, Integer> getArtikel(Warenkorb wk) {

		return wk.getArtikel();
	}

	/**
	 * Gets the warenkoerbe.
	 *
	 * @return the warenkoerbe
	 */
	public Vector<Warenkorb> getWarenkoerbe() {

		return warenkoerbe;
	}

	/**
	 * Gets the warenkorb.
	 *
	 * @param wk
	 *           the wk
	 * @return the warenkorb
	 * @throws BasketNonexistantException
	 *            the basket nonexistant exception
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
	 * Leere warenkorb.
	 *
	 * @param wk
	 *           the wk
	 */
	public void leereWarenkorb(Warenkorb wk) {

		wk.leereWarkenkorb();
	}

	/**
	 * Lege in warenkorb.
	 *
	 * @param wk
	 *           the wk
	 * @param art
	 *           the artikel
	 * @param anz
	 *           the anz
	 * @throws ArticleStockNotSufficientException
	 *            the article stock not sufficient exception
	 * @throws ArticleAlreadyInBasketException
	 *            the article already in basket exception
	 * @throws InvalidAmountException
	 *            the invalid amount exception
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
	 * Loesche aus warenkorn.
	 *
	 * @param wk
	 *           the wk
	 * @param art
	 *           the artikel
	 */
	public void loescheAusWarenkorn(Warenkorb wk, Artikel art) {

		wk.loescheArtikel(art);
	}

	/**
	 * Loesche warenkorb.
	 *
	 * @param wk
	 *           the wk
	 */
	public void loescheWarenkorb(Warenkorb wk) {

		for (Warenkorb del : warenkoerbe) {
			if (del.equals(wk)) {
				warenkoerbe.remove(del);
			}
		}
	}
}
