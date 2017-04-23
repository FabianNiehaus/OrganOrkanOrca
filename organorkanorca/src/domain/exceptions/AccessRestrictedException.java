/**
 * 
 */
package domain.exceptions;

/**
 * @author Fabian Niehaus
 * Exception bei nicht erlaubtem Zugriffversuch
 */
public class AccessRestrictedException extends Exception {

	private static final long serialVersionUID = 5192318380507367574L;

	/**
	 * @param userClass
	 * @param methodName
	 */
	public AccessRestrictedException(byte userClass, String methodName) {
		super("Die ausgewählte Methode " + methodName + " kann von Zugriffsklasse " + resolveUserClass(userClass) + " nicht ausgeführt werden!");
	}
	
	/**
	 * @param userClass
	 * @return
	 */
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
