package domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import data_objects.Kunde;
import data_objects.Mitarbeiter;
import data_objects.Person;
import data_objects.Warenkorb;
import domain.exceptions.InvalidPersonDataException;
import domain.exceptions.LoginFailedException;

import domain.exceptions.MaxIDsException;
import domain.exceptions.PersonNonexistantException;
import persistence.FilePersistenceManager;
import persistence.PersistenceManager;


/**
 * @author Fabian Niehaus
 * Klasse zur Verwaltung von Kunden
 */
public class Kundenverwaltung {
	
	// Persistenz-Schnittstelle, die f�r die Details des Dateizugriffs verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();
	
	private Vector<Kunde> kunden = new Vector<Kunde>();
		
	/**@author Mathis M�hlenkamp
	 * Methode zum Einlesen von Kunden aus einer Datei.
	 * 
	 * @param datei Datei, die einzulesenden 
	 * @throws IOException
	 * @throws InvalidPersonDataException 
	 */
	public void liesDaten(String datei, Warenkorbverwaltung wv) throws IOException, InvalidPersonDataException {
		// PersistenzManager f�r Lesevorgänge öffnen
		pm.openForReading(datei);

		Kunde ku = null;
		do {
			// Kunde-Objekt einlesen
			ku = pm.ladeKunde();
			if (ku!= null) {
				// Mitarbeiter in Mitarbeiterliste einfuegen
				einfuegen(ku);
			}
		} while (ku != null);
			
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	/**
	 * Methode zum Schreiben der Kundendaten in eine Datei.
	 * 
	 * @param datei Datei, in die der...
	 * @throws IOException
	 */
	public void schreibeDaten(String datei) throws IOException  {
		// PersistenzManager fuer Schreibvorgänge öffnen
		pm.openForWriting(datei);

		if (!kunden.isEmpty()) {
			Iterator<Kunde> iter = kunden.iterator();
			while (iter.hasNext()) {
				Kunde ku = (Kunde) iter.next();
				pm.speichereKunde(ku);				
			}
		}
		
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	/**
	 * Fuegt einen Kunden hinzu
	 * @param ku Kunde
	 */
	public void einfuegen(Kunde ku){
		kunden.add(ku);
	}
	
	/**
	 * Logik zur Anmeldung
	 * @param id Benutzer-ID
	 * @param passwort Benutzer-Passwort
	 * @return Angemeldeter Benutzer
	 * @throws LoginFailedException Anmeldung fehlgeschlagen
	 */
	public Kunde anmelden(int id, String passwort) throws LoginFailedException {
		for (Kunde k : kunden){
			if(k.getId() == id && k.getPasswort().equals(passwort)){
				return k;
			}
		} throw new LoginFailedException(id);
	}
	
	/**
	 * Prueft, ob ein bestimmter Kunde in der Kundenverwaltung liegt.
	 * @param art Zu ueberpruefender Kunde
	 * @return Gibt <b>true</b> zurueck, wenn zu pruefender Kunde in der HAsMap Kunde gespeichert ist. Sonst <b>false</b>.
	 * suche nach ID oder Name 
	 */
	public Kunde sucheKunde(String firstname, String lastname) throws PersonNonexistantException {
		for(Kunde ku : kunden){
			if (ku.getFirstname().equals(firstname) && ku.getLastname().equals(lastname)){
				return ku;
			}
		}
		
		throw new PersonNonexistantException(firstname, lastname);
	}
	
	/**
	 * Sucht einen Kunden anhand seiner ID
	 * @param id Kundenid
	 * @return Gesuchter Kunde
	 */
	public Kunde sucheKunde(int id) throws PersonNonexistantException{
		for(Kunde ku : kunden){
			if(ku.getId() == id){
				return ku;
				}
			}
		
		throw new PersonNonexistantException(id);
		
	}
	
	/**
	 * Erstellt einen neuen Kunden und fuegt in zur verwalteten Liste hinzu
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param passwort Passwort
	 * @param address_Street Straße + Hausnummer
	 * @param address_Zip Postleitzahl
	 * @param address_Town Stadt
	 * @param wk Zugeordneter Warenkorb
	 * @return
	 * @throws MaxIDsException
	 * @throws InvalidPersonData 
	 */
	public Kunde erstelleKunde(String firstname, String lastname, String passwort, String address_Street, String address_Zip, String address_Town, Warenkorb wk) throws MaxIDsException, InvalidPersonDataException {
		Kunde ku = new Kunde(firstname, lastname, getNextID(), passwort, address_Street, address_Zip, address_Town, wk);
		kunden.add(ku);
		return ku;
	}
	
	/**
	 * Löscht einen Kunden aus der verwalteten Liste
	 * @param einKunde Zu löschender Kunde
	 */
	public boolean loescheKunde(Kunde einKunde) {
		return kunden.remove(einKunde);
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
	 * @throws MaxIDsException 
	 */
	public int getNextID() throws MaxIDsException {
		int hoechsteID = 1000;
		for(Kunde ku : kunden){
			if(ku.getId() > hoechsteID){
				hoechsteID = ku.getId();
			}
		}		

		if(hoechsteID < 8999){
			return hoechsteID+1;
		} else {
			throw new MaxIDsException("Kunden");
		}
	}
	
	/**
	 * @return Liste der verwalteten Kunden
	 */
	public Vector<Kunde> getKunden() {
		return kunden;
	}
	
	/**
	 * Gibt den zu einem Kunden zugeordneten Warenkorb aus
	 * @param ku Gewuenschter Kunde
	 * @return Warenkorb des Kunde
	 */
	public Warenkorb gibWarenkorbVonKunde(Person ku){
		return ((Kunde) ku).getWarenkorb();
	}
	
	public void weiseWarenkorbzu(Kunde ku, Warenkorb wk){
		ku.setWarenkorb(wk);
	};
	
}
