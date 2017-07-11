/**
 * 
 */
package eshop.common.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class ArticleNonexistantException.
 */
public class ArticleNonexistantException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5248914535441215896L;

	/**
	 * Instantiates a new article nonexistant exception.
	 *
	 * @param artikelnummer
	 *           the artikelnummer
	 */
	public ArticleNonexistantException(int artikelnummer) {
		super("Die gesuchte Artikelnummer " + artikelnummer + " existiert nicht!");
	}

	/**
	 * Instantiates a new article nonexistant exception.
	 *
	 * @param bezeichnung
	 *           the bezeichnung
	 */
	public ArticleNonexistantException(String bezeichnung) {
		super("Der gesuchte Artikel " + bezeichnung + " existiert nicht!");
	}

	/**
	 * Instantiates a new article nonexistant exception.
	 *
	 * @param bezeichnung
	 *           the bezeichnung
	 * @param b
	 *           the b
	 */
	public ArticleNonexistantException(String bezeichnung, boolean b) {
		super("Der Artikel " + bezeichnung + " wurde gelöscht");
	}
}
