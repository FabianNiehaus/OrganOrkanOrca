/**
 * 
 */
package eshop.common.exceptions;

import eshop.common.data_objects.Person;

/**
 * @author Fabian Niehaus Exception bei nicht erlaubtem Zugriffversuch
 */
public class AccessRestrictedException extends Exception {

	private static final long serialVersionUID = 5192318380507367574L;

	public AccessRestrictedException(Person p, String methodName) {
		super("Das Modul \"" + methodName + "\" steht fuer Mitglieder der Gruppe \"" + p.getClass().getSimpleName()
				+ "\" nicht zur Verfuegung!");
	}
}
