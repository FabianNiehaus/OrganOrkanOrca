package eshop.common.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class ArticleAlreadyInBasketException.
 */
public class ArticleAlreadyInBasketException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7541802984036902263L;

	/**
	 * Instantiates a new article already in basket exception.
	 *
	 * @param bezeichnung
	 *           the bezeichnung
	 */
	public ArticleAlreadyInBasketException(String bezeichnung) {
		super("Artikel \"" + bezeichnung + "\" liegt bereits im Warenkorb!");
	}
}
