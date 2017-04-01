/**
 * 
 */
package data_objects;

/**
 * @author Fabian
 *
 */
public class Kunde extends Person {

	private String address_Street;
	private String address_Zip;
	private String address_Town;
	
	public Kunde(String firstname, String lastname, String id) {
		super(firstname, lastname, id);
		// TODO Auto-generated constructor stub
	}

}
