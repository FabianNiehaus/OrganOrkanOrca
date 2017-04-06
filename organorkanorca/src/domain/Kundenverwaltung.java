package domain;

import java.util.Vector;
import data_objects.Kunde;

public class Kundenverwaltung {
	
	private Vector<Kunde> kunden;
	
	
	/**
	 * Prüft, ob ein bestimmter Kunde in diesem Warenkorb liegt.
	 * @param art Zu überprüfender Kunde
	 * @return Gibt <b>true</b> zurück, wenn zu prüfender Kunde in der HAsMap Kunde gespeichert ist. Sonst <b>false</b>.
	 */
	public Kunde sucheKunde(String firstname, String lastname){
		for(Kunde ku : kunden){
			if (ku.getFirstname().equals(firstname) && ku.getLastname().equals(lastname)){
				return ku;
			}
		}
		return null;
	}

	public Vector<Kunde> getKunden() {
		return kunden;
	}

	
}
