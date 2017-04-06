package domain;

import java.util.Vector;
import data_objects.Kunde;
import domain.exceptions.VectorIsEmptyException;

public class Kundenverwaltung {
	
	private Vector<Kunde> kunden = new Vector<Kunde>();
	
	
	/**
	 * Prüft, ob ein bestimmter Kunde in der Kundenverwaltung liegt.
	 * @param art Zu überprüfender Kunde
	 * @return Gibt <b>true</b> zurück, wenn zu prüfender Kunde in der HAsMap Kunde gespeichert ist. Sonst <b>false</b>.
	 * suche nach ID oder Name 
	 */
	public Kunde sucheKunde(String firstname, String lastname) throws VectorIsEmptyException{
		for(Kunde ku : kunden){
			if (ku.getFirstname().equals(firstname) && ku.getLastname().equals(lastname)){
				return ku;
			}
		}
		return null;
	}
	
	public Kunde sucheKunde(int id){
		for(Kunde ku : kunden){
			if(ku.getId().equals(id)){
				return ku;
				}
			}
			return null;
		}
	
	public void erstelleKunde(Kunde einKunde) {
		kunden.add(einKunde);
		
	}
	
	public void loescheKunde(Kunde einKunde) {
		kunden.remove(einKunde);
	}
	
	public void aendereKunde(Kunde einKunde) {
	
	}
	
	public void pruefeDublikate() {
	
	}
	
	
	public Vector<Kunde> getKunden() {
		return kunden;
	}

	
}
