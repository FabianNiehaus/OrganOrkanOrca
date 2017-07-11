package eshop.common.exceptions;

import eshop.common.data_objects.Massengutartikel;

// TODO: Auto-generated Javadoc
/**
 * The Class InvalidAmountException.
 */
public class InvalidAmountException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5480470331902127179L;

	/**
	 * Instantiates a new invalid amount exception.
	 */
	public InvalidAmountException() {
		super("Keine zulässige Packungsgröße!");
	}

	/**
	 * Instantiates a new invalid amount exception.
	 *
	 * @param bestand
	 *           the bestand
	 */
	public InvalidAmountException(int bestand) {
		super(bestand + " ist kein gültiger Bestand!");
	}

	/**
	 * Instantiates a new invalid amount exception.
	 *
	 * @param art
	 *           the artikel
	 */
	public InvalidAmountException(Massengutartikel art) {
		super("Artikel mit Nummer " + art.getArtikelnummer() + " kann nur in Mengen vielfach von "
				+ art.getPackungsgroesse() + " ein-/ausgebucht werden.");
	}
}
