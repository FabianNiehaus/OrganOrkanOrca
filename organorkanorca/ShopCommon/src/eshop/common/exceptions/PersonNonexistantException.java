/**
 * 
 */
package eshop.common.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class PersonNonexistantException.
 */
public class PersonNonexistantException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4926085803539481775L;

	/**
	 * Instantiates a new person nonexistant exception.
	 *
	 * @param id
	 *           the id
	 */
	public PersonNonexistantException(int id) {
		super("Fuer die ID " + id + " ist keine Person angelegt.");
	}

	/**
	 * Instantiates a new person nonexistant exception.
	 *
	 * @param firstname
	 *           the firstname
	 * @param lastname
	 *           the lastname
	 */
	public PersonNonexistantException(String firstname, String lastname) {
		super("Es existiert keine Person mit dem Namen " + firstname + " " + lastname);
	}
}
