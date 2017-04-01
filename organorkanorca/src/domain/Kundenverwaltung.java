package domain;

import java.util.Vector;
import data_objects.Kunde;

public class Kundenverwaltung {

	/**
	 * 
	 */
	public Kundenverwaltung() {
		super();
	}

	/**
	 * @param kunden Verwaltete Kunden
	 */
	public Kundenverwaltung(Vector<Kunde> kunden) {
		super();
		this.kunden = kunden;
	}

	private Vector<Kunde> kunden;

	public Vector<Kunde> getKunden() {
		return kunden;
	}

	public void setKunden(Vector<Kunde> kunden) {
		this.kunden = kunden;
	}
	
}
