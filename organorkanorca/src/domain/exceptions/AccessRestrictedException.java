/**
 * 
 */
package domain.exceptions;

/**
 * @author Fabian
 *
 */
public class AccessRestrictedException extends Exception {

	/**
	 * 
	 */
	public AccessRestrictedException(byte userClass, String methodName) {
		super("Die ausgewählte Methode " + methodName + " kann von Zugriffsklasse " + resolveUserClass(userClass) + " nicht ausgeführt werden!");
	}
	
	public static String resolveUserClass(byte userClass){
		if(userClass == 0){
			return "Kunde";
		} else if(userClass == 1){
			return "Mitarbeiter";
		} else {
			return "Unbekannt";
		}
	}
	
}
