package data_objects;

/**
 * @author Fabian Niehaus
 *
 */
public class Mitarbeiter extends Person {

	/**
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param id Eindeutige ID
	 * @param passwort Passwort
	 */
	public Mitarbeiter(String firstname, String lastname, int id, String passwort) {
		super(firstname, lastname, id, passwort);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
		return id + ": " + firstname + " " + lastname;
	}

}
