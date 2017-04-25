/**
 * 
 */
package domain.exceptions;

/**
 * @author Mathis Möhlenkamp
 * Exception bei nicht existierender Kunden ID
 */
public class KundenIdNonexistantException extends Exception {

	private static final long serialVersionUID = 5248914535441215896L; //nachfragen??

	public KundenIdNonexistantException(){
		super("Die gesuchte Position existiert nicht!");
	}
}
