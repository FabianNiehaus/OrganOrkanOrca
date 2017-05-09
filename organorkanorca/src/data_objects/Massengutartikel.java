package data_objects;

public class Massengutartikel extends Artikel{
	
	private int packungsgroesse;
	
	public Massengutartikel(String bezeichnung, int artikelnummer, int bestand, double preis, int packungsgroesse) {
		super(bezeichnung, artikelnummer, bestand, preis);
		// TODO Auto-generated constructor stub
		this.packungsgroesse = packungsgroesse;
	}
	
	public int getPackungsgroesse() {
		return packungsgroesse;
	}
	
	public void setPackungsgroesse(int packungsgroesse) {
		this.packungsgroesse = packungsgroesse;
	}

}
