/**
 * 
 */
package domain.exceptions;

/**
 * @author Fabian Niehaus
 * Exception bei leerem Vektor
 */
public class VectorIsEmptyException extends Exception{

	private static final long serialVersionUID = -3149067205823042688L;

	/**
	 * @param vectorName Leerer Vektor
	 */
	public VectorIsEmptyException(String vectorName) {
		super("Der Vektor " + vectorName + " ist leer!");
	}

	
}
