/**
 * 
 */
package eshop.common.exceptions;

import eshop.common.data_objects.Person;

// TODO: Auto-generated Javadoc
/**
 * The Class AccessRestrictedException.
 */
public class AccessRestrictedException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5192318380507367574L;

	/**
	 * Instantiates a new access restricted exception.
	 *
	 * @param p
	 *           the p
	 * @param methodName
	 *           the method name
	 */
	public AccessRestrictedException(Person p, String methodName) {
		super("Das Modul \"" + methodName + "\" steht fuer Mitglieder der Gruppe \"" + p.getClass().getSimpleName()
				+ "\" nicht zur Verfuegung!");
	}
}
