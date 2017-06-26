package eshop.server.domain;

import java.util.Date;
import java.util.Vector;

import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Rechnung;
import eshop.common.data_objects.Warenkorb;

/**
 * @author Fabian Niehaus Klasse zur Verwaltung von Rechnungen
 */
public class Rechnungsverwaltung {

    private Vector<Rechnung> rechnungen = new Vector<Rechnung>();

    /**
     * Erzeugt eine neue Rechung
     * 
     * @param ku
     *            Zugehöriger Kunde
     * @param datum
     *            Rechnungsdatum
     * @param wk
     *            Zugehöriger Warenkorb
     * @param gesamt
     *            Gesamtsumme der Rechnung
     * @return Erzeugte Rechnung
     */
    public Rechnung rechnungErzeugen(Kunde ku, Date datum, Warenkorb wk, int gesamt) {
	Rechnung re = new Rechnung(ku, datum, wk, gesamt);
	rechnungen.add(re);
	return re;
    }
}
