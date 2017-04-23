package domain;

import java.util.Vector;

import data_objects.Kunde;
import data_objects.Person;
import data_objects.Warenkorb;
import domain.exceptions.LoginFailedException;
import domain.exceptions.VectorIsEmptyException;

/**
 * @author Fabian Niehaus
 * Klasse zur Verwaltung von Kunden
 */
public class Kundenverwaltung {
	
	private Vector<Kunde> kunden = new Vector<Kunde>();
	
	/**
	 * Logik zur Anmeldung
	 * @param id
	 * @param passwort
	 * @return
	 * @throws LoginFailedException
	 */
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
	
	/**
	 * Sucht einen Kunden anhand seiner ID
	 * @param id Kundenid
	 * @return Gesuchter Kunde
	 */
	public Kunde sucheKunde(int id){
		for(Kunde ku : kunden){
			if(ku.getId() == id){
				return ku;
				}
			}
			return null;
		}
	
	/**
	 * Erstellt einen neuen Kunden und fügt in zur verwalteten Liste hinzu
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param passwort Passwort
	 * @param wk Zugeordneter Warenkorb
	 * @return Erstellter Kunde
	 */
	public Kunde erstelleKunde(String firstname, String lastname, String passwort, Warenkorb wk) {
		Kunde ku = new Kunde(firstname, lastname, getNextID(), passwort, wk);
		kunden.add(ku);
		return ku;
	}
	
	/**
	 * Löscht einen Kunden aus der verwalteten Liste
	 * @param einKunde Zu löschender Kunde
	 */
	public void loescheKunde(Kunde einKunde) {
		kunden.remove(einKunde);
	}
	
	/* Nicht verwendet
	public void aendereKunde(Kunde einKunde) {
	
	}
	*/
	
	/* Nicht verwendet
	public void pruefeDublikate() {
	
	}
	*/
	
	
	/**
	 * Erzeugt die nächste zu verwendende Kundennummer
	 * @return Erzeugte Kundennummer
	 */
	public int getNextID() {
		int hoechsteID = 0;
		for(Kunde ku : kunden){
			if(ku.getId() > hoechsteID){
				hoechsteID = ku.getId();
			}
		}		
		return hoechsteID+1;
	}
	
	/**
	 * @return Liste der verwalteten Kunden
	 */
	public Vector<Kunde> getKunden() {
		return kunden;
	}
	
	/**
	 * Gibt den zu einem Kunden zugeordneten Warenkorb aus
	 * @param ku Gewünschter Kunde
	 * @return Warenkorb des Kunde
	 */
	public Warenkorb gibWarenkorbVonKunde(Person ku){
		return ((Kunde) ku).getWarenkorb();
	}

	
}
