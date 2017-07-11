package eshop.common.data_objects;

import java.io.Serializable;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class Massengutartikel.
 */
public class Massengutartikel extends Artikel implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 5298586861209608292L;
	
	/** The packungsgroesse. */
	private int						packungsgroesse;

	/**
	 * Instantiates a new massengutartikel.
	 *
	 * @param bezeichnung
	 *           the bezeichnung
	 * @param artikelnummer
	 *           the artikelnummer
	 * @param bestand
	 *           the bestand
	 * @param preis
	 *           the preis
	 * @param packungsgroesse
	 *           the packungsgroesse
	 * @param bestandsverlauf
	 *           the bestandsverlauf
	 * @param artikelinfo
	 *           the artikelinfo
	 * @param picture
	 *           the picture
	 */
	public Massengutartikel(String bezeichnung, int artikelnummer, int bestand, double preis, int packungsgroesse,
			Map<Integer, Integer> bestandsverlauf, String artikelinfo, String picture) {
		super(bezeichnung, artikelnummer, bestand, preis, bestandsverlauf, artikelinfo, picture);
		// TODO Auto-generated constructor stub
		this.packungsgroesse = packungsgroesse;
	}

	/**
	 * Gets the packungsgroesse.
	 *
	 * @return the packungsgroesse
	 */
	public int getPackungsgroesse() {

		return packungsgroesse;
	}

	/**
	 * Sets the packungsgroesse.
	 *
	 * @param packungsgroesse
	 *           the new packungsgroesse
	 */
	public void setPackungsgroesse(int packungsgroesse) {

		this.packungsgroesse = packungsgroesse;
	}
}
