package eshop.common.data_objects;

import java.io.Serializable;
import java.util.Map;

public class Massengutartikel extends Artikel implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5298586861209608292L;
	private int						packungsgroesse;

	public Massengutartikel(String bezeichnung, int artikelnummer, int bestand, double preis, int packungsgroesse,
			Map<Integer, Integer> bestandsverlauf, String artikelinfo) {
		super(bezeichnung, artikelnummer, bestand, preis, bestandsverlauf, artikelinfo);
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
