package eshop.common.data_objects;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import eshop.common.exceptions.ArticleAlreadyInBasketException;
import eshop.common.exceptions.ArticleStockNotSufficientException;

// TODO: Auto-generated Javadoc
/**
 * The Class Warenkorb.
 */
public class Warenkorb implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= 1864639903738496743L;
	
	/** The artikel. */
	private Map<Artikel, Integer>	artikel				= new LinkedHashMap<>();

	/**
	 * Instantiates a new warenkorb.
	 */
	public Warenkorb() {
		super();
	}

	/**
	 * Aendere anzahl.
	 *
	 * @param art
	 *           the artikel
	 * @param anz
	 *           the anz
	 */
	public void aendereAnzahl(Artikel art, int anz) {

		artikel.replace(art, anz);
	}

	/**
	 * Copy.
	 *
	 * @param wk
	 *           the wk
	 */
	public void copy(Warenkorb wk) {

		artikel.putAll(wk.getArtikel());
	}

	/**
	 * Gets the artikel.
	 *
	 * @return the artikel
	 */
	public Map<Artikel, Integer> getArtikel() {

		return artikel;
	}

	/**
	 * Leere warkenkorb.
	 */
	public void leereWarkenkorb() {

		artikel.clear();
	}

	/**
	 * Loesche artikel.
	 *
	 * @param art
	 *           the artikel
	 */
	public void loescheArtikel(Artikel art) {

		if (sucheArtikel(art)) {
			artikel.remove(art, artikel.get(art));
		}
	}

	/**
	 * Pruefe bestand.
	 *
	 * @param art
	 *           the artikel
	 * @param anz
	 *           the anz
	 * @throws ArticleStockNotSufficientException
	 *            the article stock not sufficient exception
	 */
	private void pruefeBestand(Artikel art, int anz) throws ArticleStockNotSufficientException {

		if (art.getBestand() < anz) {
			throw new ArticleStockNotSufficientException(art, anz);
		}
	}

	/**
	 * Sets the artikel.
	 *
	 * @param map
	 *           the map
	 */
	public void setArtikel(Map<Artikel, Integer> map) {

		for (Map.Entry<Artikel, Integer> ent : map.entrySet()) {
			artikel.clear();
			artikel.put(ent.getKey(), ent.getValue());
		}
	}

	/**
	 * Speichere artikel.
	 *
	 * @param art
	 *           the artikel
	 * @param anz
	 *           the anz
	 * @throws ArticleStockNotSufficientException
	 *            the article stock not sufficient exception
	 * @throws ArticleAlreadyInBasketException
	 *            the article already in basket exception
	 */
	public void speichereArtikel(Artikel art, int anz)
			throws ArticleStockNotSufficientException, ArticleAlreadyInBasketException {

		if (!sucheArtikel(art)) {
			if (anz > 0) {
				pruefeBestand(art, anz);
				artikel.put(art, anz);
			}
		} else {
			anz = anz + artikel.get(art);
			pruefeBestand(art, anz);
			artikel.put(art, anz);
		}
	}

	/**
	 * Suche artikel.
	 *
	 * @param art
	 *           the artikel
	 * @return true, if successful
	 */
	public boolean sucheArtikel(Artikel art) {

		if (!artikel.isEmpty()) {
			if (artikel.containsKey(art)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String retStr = "";
		int pos = 1;
		for (Map.Entry<Artikel, Integer> ent : artikel.entrySet()) {
			retStr += pos + ") " + ent.getKey().getBezeichnung() + " | " + ent.getValue() + " Stk. | á "
					+ ent.getKey().getPreis() + "€\n";
			pos++;
		}
		return retStr;
	}
}
