/**
 * 
 */
package domain.exceptions;

/**
 * @author Fabian
 *
 */
public class ArticleNumberNonexistantException extends Exception {
	
	public ArticleNumberNonexistantException(){
		super("Die gesuchte Position existiert nicht!");
	}
}
