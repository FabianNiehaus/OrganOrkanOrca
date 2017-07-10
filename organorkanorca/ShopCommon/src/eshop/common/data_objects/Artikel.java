package eshop.common.data_objects;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import eshop.common.exceptions.InvalidAmountException;

public class Artikel implements Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 8273031240889790935L;
	private int							artikelnummer;
	private int							bestand;
	private Map<Integer, Integer>	bestandsverlauf	= new LinkedHashMap<>();
	private String						bezeichnung;
	private double						preis;
	private String artikelinfo;

	/*
	 * Nocht nicht verwendet private String kategorie; private boolean angebot;
	 * private int bewertung;
	 */
	/**
	 * @param bezeichnung
	 *           Bezeichnung / Name des Artikels
	 * @param artikelnummer
	 *           Eindeutige Artikelnummer
	 * @param bestand
	 *           Aktueller Bestand des Artikels
	 * @param preis
	 *           Preis des Artikels
	 */
	public Artikel(String bezeichnung, int artikelnummer, int bestand, double preis,
			Map<Integer, Integer> bestandsverlauf, String artikelinfo) {
		this.bezeichnung = bezeichnung;
		this.artikelnummer = artikelnummer;
		this.bestand = bestand;
		this.preis = preis;
		this.bestandsverlauf = bestandsverlauf;
		this.artikelinfo = artikelinfo;
	}

	
	public String getArtikelinfo() {
	
		return artikelinfo;
	}

	
	public void setArtikelinfo(String artikelinfo) {
	
		this.artikelinfo = artikelinfo;
	}

	public void aktualisiereBestandsverlauf() {

		if (bestandsverlauf.size() >= 30) {
			bestandsverlauf.remove(0);
		}
		int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		bestandsverlauf.put(dayOfYear, bestand);
	}

	/**
	 * @return
	 */
	public int getArtikelnummer() {

		return artikelnummer;
	}

	/**
	 * @return
	 */
	public int getBestand() {

		return bestand;
	}

	public Map<Integer, Integer> getBestandsverlauf() {

		return bestandsverlauf;
	}

	/**
	 * @return Gibt die Bezeichnung des Artikels aus
	 */
	public String getBezeichnung() {

		return bezeichnung;
	}

	/**
	 * @return
	 */
	public double getPreis() {

		return preis;
	}

	/**
	 * @param artikelnummer
	 */
	public void setArtikelnummer(int artikelnummer) {

		this.artikelnummer = artikelnummer;
	}

	/**
	 * @param bestand
	 * @throws InvalidAmountException
	 */
	public void setBestand(int bestand) throws InvalidAmountException {

		if (bestand >= 0) {
			this.bestand = bestand;
			aktualisiereBestandsverlauf();
		} else {
			throw new InvalidAmountException(bestand);
		}
	}

	/**
	 * Setzt die Bezeichnung des Artikel
	 * 
	 * @param bezeichnung
	 *           Gewuenschte Artikelbezeichnung
	 */
	public void setBezeichnung(String bezeichnung) {

		this.bezeichnung = bezeichnung;
	}

	/**
	 * @param preis
	 */
	public void setPreis(double preis) {

		this.preis = preis;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return artikelnummer + " | " + bezeichnung + " | " + preis + " | " + bestand;
	}
}
