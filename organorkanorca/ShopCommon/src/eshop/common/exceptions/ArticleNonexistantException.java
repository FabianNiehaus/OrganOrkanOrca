/**
 * 
 */
package eshop.common.exceptions;

/**
 * @author Fabian Niehaus
 * Exception bei nicht existierender Artikelnummer
 */
public class ArticleNonexistantException extends Exception {

	private static final long serialVersionUID = 5248914535441215896L;

	public ArticleNonexistantException(int artikelnummer){
		super("Die gesuchte Artikelnummer " + artikelnummer + " existiert nicht!");
	}
	
	public ArticleNonexistantException(String bezeichnung){
		super("Der gesuchte Artikel " + bezeichnung + " existiert nicht!");
	}
}
