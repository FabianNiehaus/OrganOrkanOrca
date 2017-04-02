package data_objects;

public class Artikel {
	
	/**
	 * @param bezeichnung	Bezeichnung / Name des Artikels
	 * @param artikelNr	Eindeutige Artikelnummer
	 * @param bestand	Aktueller Bestand des Artikels
	 * @param preis	Preis des Artikels
	 */
	public Artikel(String bezeichnung, int artikelNr, int bestand, double preis) {
		super();
		this.bezeichnung = bezeichnung;
		this.artikelNr = artikelNr;
		this.bestand = bestand;
		this.preis = preis;
	}
	
	/**
	 * @param bezeichnung	Bezeichnung / Name des Artikels
	 * @param artikelNr	Eindeutige Artikelnummer
	 * @param bestand	Aktueller Bestand des Artikels
	 * @param preis	Preis des Artikels
	 * @param kategorie	Kategorie des Artikels
	 * @param angebot	Zeigt an, ob Artikel aktuell im Angebot ist
	 * @param bewertung	Nuzterbewertung des Artikels
	 */
	public Artikel(String bezeichnung, int artikelNr, int bestand, double preis, String kategorie, boolean angebot,
			int bewertung) {
		super();
		this.bezeichnung = bezeichnung;
		this.artikelNr = artikelNr;
		this.bestand = bestand;
		this.preis = preis;
		this.kategorie = kategorie;
		this.angebot = angebot;
		this.bewertung = bewertung;
	}
	
	private String bezeichnung;
	private int artikelNr;
	private int bestand;
	private double preis;
	
	private String kategorie;
	private boolean angebot;
	private int bewertung;
	
	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public int getArtikelNr() {
		return artikelNr;
	}

	public void setArtikelNr(int artikelNr) {
		this.artikelNr = artikelNr;
	}

	public int getBestand() {
		return bestand;
	}

	public void setBestand(int bestand) {
		this.bestand = bestand;
	}

	public double getPreis() {
		return preis;
	}

	public void setPreis(double preis) {
		this.preis = preis;
	}

	public String getKategorie() {
		return kategorie;
	}

	public void setKategorie(String kategorie) {
		this.kategorie = kategorie;
	}

	public boolean isAngebot() {
		return angebot;
	}

	public void setAngebot(boolean angebot) {
		this.angebot = angebot;
	}

	public int getBewertung() {
		return bewertung;
	}

	public void setBewertung(int bewertung) {
		this.bewertung = bewertung;
	}
	
	
}
