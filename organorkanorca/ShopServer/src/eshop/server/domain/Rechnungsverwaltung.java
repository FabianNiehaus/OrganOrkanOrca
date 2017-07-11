package eshop.server.domain;

import java.util.Date;
import java.util.Vector;

import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Rechnung;
import eshop.common.data_objects.Warenkorb;

// TODO: Auto-generated Javadoc
/**
 * The Class Rechnungsverwaltung.
 */
public class Rechnungsverwaltung {

	/** The rechnungen. */
	private Vector<Rechnung> rechnungen = new Vector<Rechnung>();

	/**
	 * Rechnung erzeugen.
	 *
	 * @param ku
	 *           the kunde
	 * @param datum
	 *           the datum
	 * @param wk
	 *           the wk
	 * @param gesamt
	 *           the gesamt
	 * @return the rechnung
	 */
	public Rechnung rechnungErzeugen(Kunde ku, Date datum, Warenkorb wk, double gesamt) {

		Rechnung re = new Rechnung(ku, datum, wk, gesamt);
		rechnungen.add(re);
		return re;
	}
}
