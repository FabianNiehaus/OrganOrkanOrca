/**
 * 
 */
package data_objects;

/**
 * @author Fabian Niehaus
 *
 */
public abstract class Person {
	private String firstname;
	private String lastname;
	private int id;
	
	/**
	 * Erzeugt eine Persion
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param id Eindeutige Identifikationsnummer
	 */
	
	public Person(String firstname, String lastname, int id) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
