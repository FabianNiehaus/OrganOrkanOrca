/**
 * 
 */
package domain.exceptions;

/**
 * @author Fabian Niehaus
 * Exception bei nicht existierender Artikelnummer
 */
public class ArticleNumberNonexistantException extends Exception {

	private static final long serialVersionUID = 5248914535441215896L;

	public ArticleNumberNonexistantException(){
		super("Die gesuchte Position existiert nicht!");
	}
}
