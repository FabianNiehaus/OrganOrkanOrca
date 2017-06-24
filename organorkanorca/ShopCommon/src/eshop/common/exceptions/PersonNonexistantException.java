/**
 * 
 */
package eshop.common.exceptions;

/**
 * @author Mathis M�hlenkamp
 * Exception bei nicht existierender Kunden ID
 */
public class PersonNonexistantException extends Exception {

	private static final long serialVersionUID = -4926085803539481775L;

	public PersonNonexistantException(int id){
		super("Fuer die ID " + id + " ist keine Person angelegt.");
	}
	
	public PersonNonexistantException(String firstname, String lastname){
		super("Es existiert keine Person mit dem Namen " + firstname + " " + lastname);
	}
}
