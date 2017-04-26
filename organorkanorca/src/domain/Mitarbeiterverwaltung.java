package domain;

import java.util.Vector;

import data_objects.Mitarbeiter;
import domain.exceptions.*;

public class Mitarbeiterverwaltung {

	private Vector<Mitarbeiter> mitarbeiter = new Vector<Mitarbeiter>();
	
	/**
	 * 
	 */
	public Mitarbeiterverwaltung(){
		mitarbeiter.add(new Mitarbeiter("Mathis", "Möhlenkamp", 9000, "test2"));
	}
	
	/**
	 * Logik zur Anmeldung
	 * @param id Mitarbeiter-ID
	 * @param passwort Mitarbeiter-Passwort
	 * @return Angemeldeter Mitarbeiter
	 * @throws LoginFailedException Login fehlgeschlagen
	 */
	public Mitarbeiter anmelden(int id, String passwort) throws LoginFailedException {
		for (Mitarbeiter m : mitarbeiter){
			if(m.getId() == id && m.getPasswort().equals(passwort)){
				return m;
			}
		} throw new LoginFailedException(id);
	}

	
	/**
	 * Sucht einen Mitarbeiter anhand Vor- und Nachname
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @return Gesuchter Mitarbeiter
	 * @throws VectorIsEmptyException Mitarbeiterliste leer
	 */
	public Mitarbeiter sucheMitarbeiter(String firstname, String lastname) throws PersonNonexistantException{
		if(mitarbeiter.size() > 0){
			for(Mitarbeiter ma : mitarbeiter){
				if (ma.getFirstname().equals(firstname) && ma.getLastname().equals(lastname)){
					return ma;
				}
			}
		}
		throw new PersonNonexistantException(firstname, lastname);
	}
	
	/**
	 * Sucht einen Mitarbeiter anhand seiner ID
	 * @param id Gesuchte ID
	 * @return Gesuchter Mitarbeiter
	 * @throws VectorIsEmptyException Mitarbeiterliste leer
	 */
	public Mitarbeiter suchMitarbeiter(int id) throws PersonNonexistantException{
		for(Mitarbeiter ma : mitarbeiter){
			if(ma.getId() == id){
				return ma;
				}
			}
			throw new PersonNonexistantException(id);
		}
	
	/**
	 * Fügt einen neuen Mitarbeiter hinzu
	 * @param einMa Neuer Mitarbeiter
	 */
	public void erstelleMitarbeiter(Mitarbeiter einMa) {
		mitarbeiter.add(einMa);
	}
	
	/**
	 * Entfernt einen Mitarbeiter
	 * @param einMa Zu entfernender Mitarbeiter
	 */
	public void loescheMitarbeiter(Mitarbeiter einMa) {
		mitarbeiter.remove(einMa);
	}
	
	/**
	 * Erzeugt die nächste zu verwendende Mitarbeiternummer
	 * @return Erzeugte Mitarbeiternummer
	 * @throws MaxIDsException 
	 */
	public int getNextID() throws MaxIDsException {
		int hoechsteID = 9000;
		for(Mitarbeiter ma : mitarbeiter){
			if(ma.getId() > hoechsteID){
				hoechsteID = ma.getId();
			}
		}		
		
		if(hoechsteID < 9999){
			return hoechsteID+1;
		} else {
			throw new MaxIDsException("Mitarbeiter");
		}
		
	}
	
	/**
	 * @return Alle Mitarbeiter
	 */
	public Vector<Mitarbeiter> getMitarbeiter() {
		return mitarbeiter;
	}
}
