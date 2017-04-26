package domain.exceptions;

import data_objects.Artikel;

/**
 * @author Fabian Niehaus
 * Exception bei unzureichendem Artikelbestand
 */
public class ArticleStockNotSufficientException extends Exception {

	private static final long serialVersionUID = 6807321841850724912L;

	public ArticleStockNotSufficientException(Artikel art, int anz) {
		super("Bestand nicht ausreichend! Artikel " + art.getBezeichnung() + " hat Bestand " + art.getBestand() + " < " + anz);
	}

}
