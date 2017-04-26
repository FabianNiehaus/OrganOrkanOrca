/**
 * 
 */
package domain.exceptions;

import data_objects.Person;

/**
 * @author Fabian Niehaus
 * Exception bei nicht erlaubtem Zugriffversuch
 */
public class AccessRestrictedException extends Exception {

	private static final long serialVersionUID = 5192318380507367574L;

	public AccessRestrictedException(Person p, String methodName) {
		super("Die Funktion " + methodName + " steht für Mitglieder der Gruppe " + p.getClass().getSimpleName() + " nicht zur Verfügung!");
	}
	
}
