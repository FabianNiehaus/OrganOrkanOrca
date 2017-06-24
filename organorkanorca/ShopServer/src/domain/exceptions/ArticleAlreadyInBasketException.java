package domain.exceptions;

public class ArticleAlreadyInBasketException extends Exception {

	public ArticleAlreadyInBasketException(String bezeichnung){
		
		super("Artikel \"" + bezeichnung + "\" liegt bereits im Warenkorb!");
		
	}
}
