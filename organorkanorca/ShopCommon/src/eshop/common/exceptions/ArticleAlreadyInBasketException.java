package eshop.common.exceptions;

public class ArticleAlreadyInBasketException extends Exception {

	/**
     * 
     */
    private static final long serialVersionUID = 7541802984036902263L;

	public ArticleAlreadyInBasketException(String bezeichnung) {
		super("Artikel \"" + bezeichnung + "\" liegt bereits im Warenkorb!");
	}
}
