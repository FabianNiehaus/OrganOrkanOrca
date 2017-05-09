package domain.exceptions;

import data_objects.Massengutartikel;

public class InvalidAmountException extends Exception {

	private static final long serialVersionUID = 5480470331902127179L;

	public InvalidAmountException(){
		super("Keine zulässige Packungsgröße!");
	}
	
	public InvalidAmountException(Massengutartikel art) {
		super("Artikel mit Nummer " + art.getArtikelnummer() + " kann nur in Mengen vielfach von " + art.getPackungsgroesse() + " ein-/ausgebucht werden.");
	}
}
