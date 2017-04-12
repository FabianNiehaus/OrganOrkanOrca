/**
 * 
 */
package domain.exceptions;

/**
 * @author Fabian
 *
 */
public class PositionNotExistantException extends Exception {
	
	public PositionNotExistantException(){
		super("Die gesuchte Position existiert nicht!");
	}
}
