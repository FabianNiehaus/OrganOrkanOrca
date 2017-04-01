package domain;

import java.util.Vector;
import data_objects.Warenkorb;

public class Warenkorbverwaltung {
	
	/**
	 * 
	 */
	public Warenkorbverwaltung() {
		super();
	}

	/**
	 * @param warenkoerbe Verwaltete Warenkörbe
	 */
	public Warenkorbverwaltung(Vector<Warenkorb> warenkoerbe) {
		super();
		this.warenkoerbe = warenkoerbe;
	}

	private Vector<Warenkorb> warenkoerbe;

	public Vector<Warenkorb> getWarenkoerbe() {
		return warenkoerbe;
	}

	public void setWarenkoerbe(Vector<Warenkorb> warenkoerbe) {
		this.warenkoerbe = warenkoerbe;
	}
}
