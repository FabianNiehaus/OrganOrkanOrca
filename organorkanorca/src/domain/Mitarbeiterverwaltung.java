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
		mitarbeiter.add(new Mitarbeiter("Mathis", "Möhlenkamp", 900, "test2"));
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
	public Mitarbeiter sucheMitarbeiter(String firstname, String lastname) throws VectorIsEmptyException{
		if(mitarbeiter.size() > 0){
			for(Mitarbeiter ma : mitarbeiter){
				if (ma.getFirstname().equals(firstname) && ma.getLastname().equals(lastname)){
					return ma;
				}
			}
		}
		return null;
	}
	
	/**
	 * Sucht einen Mitarbeiter anhand seiner ID
	 * @param id Gesuchte ID
	 * @return Gesuchter Mitarbeiter
	 * @throws VectorIsEmptyException Mitarbeiterliste leer
	 */
	public Mitarbeiter suchMitarbeiter(int id) throws VectorIsEmptyException{
		for(Mitarbeiter ma : mitarbeiter){
			if(ma.getId() == id){
				return ma;
				}
			}
			return null;
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
	 */
	public int getNextID() {
		int hoechsteID = 0;
		for(Mitarbeiter ma : mitarbeiter){
			if(ma.getId() > hoechsteID){
				hoechsteID = ma.getId();
			}
		}		
		return hoechsteID+1;
	}
	
	/**
	 * @return Alle Mitarbeiter
	 */
	public Vector<Mitarbeiter> getMitarbeiter() {
		return mitarbeiter;
	}
}
