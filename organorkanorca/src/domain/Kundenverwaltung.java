package domain;

import java.util.Vector;

import data_objects.Ereignis;
import data_objects.Kunde;
import data_objects.Person;
import data_objects.Typ;
import data_objects.Warenkorb;
import domain.exceptions.LoginFailedException;
import domain.exceptions.VectorIsEmptyException;

public class Kundenverwaltung {
	
	private Vector<Kunde> kunden = new Vector<Kunde>();
	
	public Kundenverwaltung(){
		
	}
	
	
	public Kunde anmelden(int id, String passwort) throws LoginFailedException {
		for (Kunde k : kunden){
			if(k.getId() == id && k.getPasswort().equals(passwort)){
				return k;
			}
		} throw new LoginFailedException(id);
	}
	
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
			if(ku.getId() == id){
				return ku;
				}
			}
			return null;
		}
	
	public Kunde erstelleKunde(String firstname, String lastname, String passwort, Warenkorb wk) {
		Kunde ku = new Kunde(firstname, lastname, getNextID(), passwort, wk);
		kunden.add(ku);
		return ku;
	}
	
	public void loescheKunde(Kunde einKunde) {
		kunden.remove(einKunde);
	}
	
	public void aendereKunde(Kunde einKunde) {
	
	}
	
	public void pruefeDublikate() {
	
	}
	
	public int getNextID() {
		int hoechsteID = 0;
		for(Kunde ku : kunden){
			if(ku.getId() > hoechsteID){
				hoechsteID = ku.getId();
			}
		}		
		return hoechsteID+1;
	}
	
	public Vector<Kunde> getKunden() {
		return kunden;
	}
	
	public Warenkorb gibWarenkorbVonKunde(Person ku){
		return ((Kunde) ku).getWarenkorb();
	}

	
}
