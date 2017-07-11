/**
 * 
 */
package eshop.common.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class VectorIsEmptyException.
 */
public class VectorIsEmptyException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3149067205823042688L;

	/**
	 * Instantiates a new vector is empty exception.
	 *
	 * @param vectorName
	 *           the vector name
	 */
	public VectorIsEmptyException(String vectorName) {
		super("Der Vektor " + vectorName + " ist leer!");
	}
}
