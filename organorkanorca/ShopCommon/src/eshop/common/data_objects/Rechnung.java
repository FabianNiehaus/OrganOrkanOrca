/**
 * 
 */
package eshop.common.data_objects;

import java.io.Serializable;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Rechnung.
 */
/**
 * @author Manic
 *
 */
public class Rechnung implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 6681009839157213957L;
	
	/** The datum. */
	private Date					datum;
	
	/** The gesamt. */
	private double					gesamt;
	
	/** The Kunde. */
	private Kunde					ku;
	
	/** The wk. */
	private Warenkorb				wk;

	/**
	 * Instantiates a new rechnung.
	 *
	 * @param ku
	 *           the kunde
	 * @param datum
	 *           the datum
	 * @param wk
	 *           the wk
	 * @param gesamt
	 *           the gesamt
	 */
	public Rechnung(Kunde ku, Date datum, Warenkorb wk, double gesamt) {
		super();
		this.ku = ku;
		this.datum = datum;
		this.wk = wk;
		this.gesamt = gesamt;
	}

	/**
	 * Gets the datum.
	 *
	 * @return the datum
	 */
	public Date getDatum() {

		return datum;
	}

	/**
	 * Gets the gesamt.
	 *
	 * @return the gesamt
	 */
	public double getGesamt() {

		return gesamt;
	}

	/**
	 * Gets the kunde.
	 *
	 * @return the kunde
	 */
	public Kunde getKu() {

		return ku;
	}

	/**
	 * Gets the wk.
	 *
	 * @return the wk
	 */
	public Warenkorb getWk() {

		return wk;
	}
}
