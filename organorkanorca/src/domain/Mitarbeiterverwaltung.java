package domain;

import java.util.Vector;

import data_objects.Kunde;
import data_objects.Mitarbeiter;
import domain.exceptions.*;

public class Mitarbeiterverwaltung {

	private Vector<Mitarbeiter> mitarbeiter = new Vector<Mitarbeiter>();
	
	
	public Mitarbeiter anmelden(String login, String passwort) throws LoginFailedException {
		throw new LoginFailedException(login);
	}

	/**
	 * Prüft, ob ein bestimmter Mitarbeiter in diesem Warenkorb liegt.
	 * @param art Zu überprüfender Mitarbeiter
	 * @return Gibt <b>true</b> zurück, wenn zu prüfender Mitarbeiter in der HAsMap Mitarbeiter gespeichert ist. Sonst <b>false</b>.
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
	
	public Mitarbeiter suchMitarbeiter(int id){
		for(Mitarbeiter ma : mitarbeiter){
			if(ma.getId() == id){
				return ma;
				}
			}
			return null;
		}
	
	public void erstelleMitarbeiter(Mitarbeiter einMa) {
		mitarbeiter.add(einMa);
	}
	
	public void loescheMitarbeiter(Mitarbeiter einMa) {
		mitarbeiter.remove(einMa);
	}
	
	public int getNextID() {
		int hoechsteID = 0;
		for(Mitarbeiter ma : mitarbeiter){
			if(ma.getId() > hoechsteID){
				hoechsteID = ma.getId();
			}
		}		
		return hoechsteID+1;
	}
	
	public Vector<Mitarbeiter> getMitarbeiter() {
		return mitarbeiter;
	}

	public void setMitarbeiter(Vector<Mitarbeiter> mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}
}
