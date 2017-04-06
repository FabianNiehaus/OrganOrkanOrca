/**
 * 
 */
package domain.exceptions;

/**
 * @author Fabian
 *
 */
public class VectorIsEmptyException extends Exception{

	/**
	 * @param arg0
	 */
	public VectorIsEmptyException(String vectorName) {
		super("Der Vektor " + vectorName + " ist leer!");
	}

	
}
