/**
 * 
 */
package data_objects;

import java.util.Date;

/**
 * @author Fabian
 *
 */
public class Rechnung {

	/**
	 * @param ku
	 * @param datum
	 * @param wk
	 * @param gesamt
	 */
	public Rechnung(Kunde ku, Date datum, Warenkorb wk, double gesamt) {
		super();
		this.ku = ku;
		this.datum = datum;
		this.wk = wk;
		this.gesamt = gesamt;
	}
	
	private Kunde ku;
	private Date datum;
	private Warenkorb wk;
	private double gesamt;
	
	public Kunde getKu() {
		return ku;
	}
	public Date getDatum() {
		return datum;
	}
	public Warenkorb getWk() {
		return wk;
	}
	public double getGesamt() {
		return gesamt;
	}
	
}
