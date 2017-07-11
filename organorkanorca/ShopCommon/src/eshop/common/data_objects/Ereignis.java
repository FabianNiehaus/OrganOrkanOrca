package eshop.common.data_objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Ereignis.
 */
public class Ereignis implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -1817043294145890221L;
	
	/** The id. */
	private int						id;
	
	/** The wann. */
	private Date					wann;
	
	/** The was. */
	private Typ						was;
	
	/** The wer name. */
	private String					wer_Name;
	
	/** The wer id. */
	private int						wer_Id;
	
	/** The wieviel. */
	private int						wieviel;
	
	/** The womit nr. */
	private int						womit_Nr;
	
	/** The womit bezeichnung. */
	private String					womit_Bezeichnung;

	/**
	 * Instantiates a new ereignis.
	 *
	 * @param id
	 *           the id
	 * @param wer_Id
	 *           the wer id
	 * @param wer_Name
	 *           the wer name
	 * @param was
	 *           the was
	 * @param womit_Nr
	 *           the womit nr
	 * @param womit_Bezeichnung
	 *           the womit bezeichnung
	 * @param wieviel
	 *           the wieviel
	 * @param wann
	 *           the wann
	 */
	public Ereignis(int id, int wer_Id, String wer_Name, Typ was, int womit_Nr, String womit_Bezeichnung, int wieviel,
			Date wann) {
		super();
		this.id = id;
		this.wer_Id = wer_Id;
		this.wer_Name = wer_Name;
		this.was = was;
		this.womit_Nr = womit_Nr;
		this.womit_Bezeichnung = womit_Bezeichnung;
		this.wieviel = wieviel;
		this.wann = wann;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {

		return id;
	}

	/**
	 * Gets the typ.
	 *
	 * @return the typ
	 */
	public Typ getTyp() {

		return was;
	}

	/**
	 * Gets the wann.
	 *
	 * @return the wann
	 */
	public Date getWann() {

		return wann;
	}

	/**
	 * Gets the wer id.
	 *
	 * @return the wer id
	 */
	public int getWer_Id() {

		return wer_Id;
	}

	/**
	 * Gets the wer name.
	 *
	 * @return the wer name
	 */
	public String getWer_Name() {

		return wer_Name;
	}

	/**
	 * Gets the wieviel.
	 *
	 * @return the wieviel
	 */
	public int getWieviel() {

		return wieviel;
	}

	/**
	 * Gets the womit bezeichnung.
	 *
	 * @return the womit bezeichnung
	 */
	public String getWomit_Bezeichnung() {

		return womit_Bezeichnung;
	}

	/**
	 * Gets the womit nr.
	 *
	 * @return the womit nr
	 */
	public int getWomit_Nr() {

		return womit_Nr;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *           the new id
	 */
	public void setId(int id) {

		this.id = id;
	}

	/**
	 * Sets the wann.
	 *
	 * @param wann
	 *           the new wann
	 */
	public void setWann(Date wann) {

		this.wann = wann;
	}

	/**
	 * Sets the was.
	 *
	 * @param was
	 *           the new was
	 */
	public void setWas(Typ was) {

		this.was = was;
	}

	/**
	 * Sets the wer id.
	 *
	 * @param wer_Id
	 *           the new wer id
	 */
	public void setWer_Id(int wer_Id) {

		this.wer_Id = wer_Id;
	}

	/**
	 * Sets the wer name.
	 *
	 * @param wer_Name
	 *           the new wer name
	 */
	public void setWer_Name(String wer_Name) {

		this.wer_Name = wer_Name;
	}

	/**
	 * Sets the wieviel.
	 *
	 * @param wieviel
	 *           the new wieviel
	 */
	public void setWieviel(int wieviel) {

		this.wieviel = wieviel;
	}

	/**
	 * Sets the womit bezeichnung.
	 *
	 * @param womit_Bezeichnung
	 *           the new womit bezeichnung
	 */
	public void setWomit_Bezeichnung(String womit_Bezeichnung) {

		this.womit_Bezeichnung = womit_Bezeichnung;
	}

	/**
	 * Sets the womit nr.
	 *
	 * @param womit_Nr
	 *           the new womit nr
	 */
	public void setWomit_Nr(int womit_Nr) {

		this.womit_Nr = womit_Nr;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		// Formatierungsvorlage fuer Datum
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return dateFormat.format(wann) + " | " + wer_Id + ": " + wer_Name + " | " + was + " | " + womit_Nr + ": "
				+ womit_Bezeichnung + " | " + wieviel;
	}
}
