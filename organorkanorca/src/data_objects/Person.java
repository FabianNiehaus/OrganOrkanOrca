/**
 * 
 */
package data_objects;

/**
 * @author Fabian Niehaus
 *
 */
public abstract class Person {
	protected String firstname;
	protected String lastname;
	protected int id;
	private String passwort;
	
	/**
	 * Erzeugt eine Persion
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param id Eindeutige Identifikationsnummer
	 */
	public Person(String firstname, String lastname, int id, String passwort) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
		this.setPasswort(passwort);
	}

	/**
	 * @return
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * @param passwort
	 */
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
}
