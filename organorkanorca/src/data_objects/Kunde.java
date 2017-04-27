/**
 * 
 */
package data_objects;

/**
 * @author Fabian Niehaus
 *
 */
public class Kunde extends Person {

	private String address_Street;
	private String address_Zip;
	private String address_Town;
	
	private Warenkorb warenkorb;
	
	/**
	 * @param firstname Vorname
	 * @param lastname Nachmane
	 * @param id Eindeutige ID
	 * @param passwort Passwort
	 * @param wk Zugeordneter Warenkorb
	 */
	public Kunde(String firstname, String lastname, int id, String passwort, Warenkorb wk) {
		super(firstname, lastname, id, passwort);
		this.warenkorb = wk;
	}

	/**
	 * @return
	 */
	public String getAddress_Street() {
		return address_Street;
	}

	/**
	 * @param address_Street
	 */
	public void setAddress_Street(String address_Street) {
		this.address_Street = address_Street;
	}

	/**
	 * @return
	 */
	public String getAddress_Zip() {
		return address_Zip;
	}

	/**
	 * @param address_Zip
	 */
	public void setAddress_Zip(String address_Zip) {
		this.address_Zip = address_Zip;
	}

	/**
	 * @return
	 */
	public String getAddress_Town() {
		return address_Town;
	}

	/**
	 * @param address_Town
	 */
	public void setAddress_Town(String address_Town) {
		this.address_Town = address_Town;
	}

	/**
	 * @return
	 */
	public Warenkorb getWarenkorb() {
		return warenkorb;
	}

	/**
	 * @param warenkorb
	 */
	public void setWarenkorb(Warenkorb warenkorb) {
		this.warenkorb = warenkorb;
	}
	
	public String toString(){
		return id + ": " + firstname + " " + lastname + " | " + address_Street + " ," + address_Zip + " " + address_Town;
	}

}
