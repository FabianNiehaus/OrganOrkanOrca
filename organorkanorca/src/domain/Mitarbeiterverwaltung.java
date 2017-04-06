package domain;

import java.util.Vector;

import data_objects.Kunde;
import data_objects.Mitarbeiter;

public class Mitarbeiterverwaltung {

	private Vector<Mitarbeiter> mitarbeiter;
	
	/**
	 * Prüft, ob ein bestimmter Mitarbeiter in diesem Warenkorb liegt.
	 * @param art Zu überprüfender Mitarbeiter
	 * @return Gibt <b>true</b> zurück, wenn zu prüfender Mitarbeiter in der HAsMap Mitarbeiter gespeichert ist. Sonst <b>false</b>.
	 */
	public Mitarbeiter sucheMitarbeiter(String firstname, String lastname){
		for(Mitarbeiter ma : mitarbeiter){
			if (ma.getFirstname().equals(firstname) && ma.getLastname().equals(lastname)){
				return ma;
			}
		}
		return null;
	}
	
	public Mitarbeiter suchMitarbeiter(int id){
		for(Mitarbeiter ma : mitarbeiter){
			if(ma.getId().equals(id)){
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
	
	/*
	 * 
	 */
	public Vector<Mitarbeiter> getMitarbeiter() {
		return mitarbeiter;
	}

	public void setMitarbeiter(Vector<Mitarbeiter> mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}
}
