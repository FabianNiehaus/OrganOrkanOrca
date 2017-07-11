package eshop.common.data_objects;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import eshop.common.exceptions.InvalidAmountException;

// TODO: Auto-generated Javadoc
/**
 * The Class Artikel.
 */
public class Artikel implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= 8273031240889790935L;
	
	/** The artikelnummer. */
	private int							artikelnummer;
	
	/** The bestand. */
	private int							bestand;
	
	/** The bestandsverlauf. */
	private Map<Integer, Integer>	bestandsverlauf	= new LinkedHashMap<>();
	
	/** The bezeichnung. */
	private String						bezeichnung;
	
	/** The preis. */
	private double						preis;
	
	/** The artikelinfo. */
	private String						artikelinfo;
	
	/** The picture. */
	private String						picture;

	/*
	 * Nocht nicht verwendet private String kategorie; private boolean angebot;
	 * private int bewertung;
	 */
	/**
	 * Instantiates a new artikel.
	 *
	 * @param bezeichnung
	 *           the bezeichnung
	 * @param artikelnummer
	 *           the artikelnummer
	 * @param bestand
	 *           the bestand
	 * @param preis
	 *           the preis
	 * @param bestandsverlauf
	 *           the bestandsverlauf
	 * @param artikelinfo
	 *           the artikelinfo
	 * @param picture
	 *           the picture
	 */
	public Artikel(String bezeichnung, int artikelnummer, int bestand, double preis,
			Map<Integer, Integer> bestandsverlauf, String artikelinfo, String picture) {
		this.bezeichnung = bezeichnung;
		this.artikelnummer = artikelnummer;
		this.bestand = bestand;
		this.preis = preis;
		this.bestandsverlauf = bestandsverlauf;
		this.artikelinfo = artikelinfo;
		this.setPicture(picture);
	}

	/**
	 * Aktualisiere bestandsverlauf.
	 */
	public void aktualisiereBestandsverlauf() {

		if (bestandsverlauf.size() >= 30) {
			bestandsverlauf.remove(0);
		}
		int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		bestandsverlauf.put(dayOfYear, bestand);
	}

	/**
	 * Gets the artikelinfo.
	 *
	 * @return the artikelinfo
	 */
	public String getArtikelinfo() {

		return artikelinfo;
	}

	/**
	 * Gets the artikelnummer.
	 *
	 * @return the artikelnummer
	 */
	public int getArtikelnummer() {

		return artikelnummer;
	}

	/**
	 * Gets the bestand.
	 *
	 * @return the bestand
	 */
	public int getBestand() {

		return bestand;
	}

	/**
	 * Gets the bestandsverlauf.
	 *
	 * @return the bestandsverlauf
	 */
	public Map<Integer, Integer> getBestandsverlauf() {

		return bestandsverlauf;
	}

	/**
	 * Gets the bezeichnung.
	 *
	 * @return the bezeichnung
	 */
	public String getBezeichnung() {

		return bezeichnung;
	}

	/**
	 * Gets the picture.
	 *
	 * @return the picture
	 */
	public String getPicture() {

		return picture;
	}

	/**
	 * Gets the preis.
	 *
	 * @return the preis
	 */
	public double getPreis() {

		return preis;
	}

	/**
	 * Sets the artikelinfo.
	 *
	 * @param artikelinfo
	 *           the new artikelinfo
	 */
	public void setArtikelinfo(String artikelinfo) {

		this.artikelinfo = artikelinfo;
	}

	/**
	 * Sets the artikelnummer.
	 *
	 * @param artikelnummer
	 *           the new artikelnummer
	 */
	public void setArtikelnummer(int artikelnummer) {

		this.artikelnummer = artikelnummer;
	}

	/**
	 * Sets the bestand.
	 *
	 * @param bestand
	 *           the new bestand
	 * @throws InvalidAmountException
	 *            the invalid amount exception
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
	 * Sets the bezeichnung.
	 *
	 * @param bezeichnung
	 *           the new bezeichnung
	 */
	public void setBezeichnung(String bezeichnung) {

		this.bezeichnung = bezeichnung;
	}

	/**
	 * Sets the picture.
	 *
	 * @param picture
	 *           the new picture
	 */
	public void setPicture(String picture) {

		this.picture = picture;
	}

	/**
	 * Sets the preis.
	 *
	 * @param preis
	 *           the new preis
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
