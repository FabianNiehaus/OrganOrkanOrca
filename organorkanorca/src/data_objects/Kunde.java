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
	
	public Kunde(String firstname, String lastname, int id) {
		super(firstname, lastname, id);
		// TODO Auto-generated constructor stub
	}

	public String getAddress_Street() {
		return address_Street;
	}

	public void setAddress_Street(String address_Street) {
		this.address_Street = address_Street;
	}

	public String getAddress_Zip() {
		return address_Zip;
	}

	public void setAddress_Zip(String address_Zip) {
		this.address_Zip = address_Zip;
	}

	public String getAddress_Town() {
		return address_Town;
	}

	public void setAddress_Town(String address_Town) {
		this.address_Town = address_Town;
	}

}
