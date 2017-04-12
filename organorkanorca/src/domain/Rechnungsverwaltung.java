package domain;

import java.util.Date;
import java.util.Vector;

import data_objects.Kunde;
import data_objects.Rechnung;
import data_objects.Warenkorb;

public class Rechnungsverwaltung {
	
	private Vector<Rechnung> rechnungen = new Vector<Rechnung>();
	
	public Rechnung rechnungErzeugen(Kunde ku, Date datum, Warenkorb wk){
		Rechnung re = new Rechnung(ku, datum, wk);
		rechnungen.add(re);
		return re;
	}
	
}
