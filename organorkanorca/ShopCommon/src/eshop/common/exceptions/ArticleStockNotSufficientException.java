package eshop.common.exceptions;

import eshop.common.data_objects.Artikel;

// TODO: Auto-generated Javadoc
/**
 * The Class ArticleStockNotSufficientException.
 */
public class ArticleStockNotSufficientException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6807321841850724912L;

	/**
	 * Instantiates a new article stock not sufficient exception.
	 *
	 * @param art
	 *           the artikel
	 * @param anz
	 *           the anz
	 */
	public ArticleStockNotSufficientException(Artikel art, int anz) {
		super("Bestand nicht ausreichend! Artikel " + art.getBezeichnung() + " hat Bestand " + art.getBestand() + " < "
				+ anz);
	}
}
