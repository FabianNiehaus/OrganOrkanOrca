package eshop.common.data_objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fabian Niehaus Dient zur Ereignisspeicherung fuer den eShop
 */
public class Ereignis implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1817043294145890221L;
	private int						id;
	private Date					wann;
	private Typ						was;
	private String					wer_Name;
	private int						wer_Id;
	private int						wieviel;
	private int						womit_Nr;
	private String					womit_Bezeichnung;

	/**
	 * @param wer
	 *           Person, die die Aktion durchgefuehrt hat
	 * @param was
	 *           Typ der Aktion (EINLAGERUNG, AUSLAGERUNG, KAUF, NEU)
	 * @param womit
	 *           Welcher Artikel ist betroffen
	 * @param wieviel
	 *           Betroffene Stueckzahl
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

	public int getId() {

		return id;
	}

	public Typ getTyp() {

		return was;
	}

	public Date getWann() {

		return wann;
	}

	public int getWieviel() {

		return wieviel;
	}

	public void setId(int id) {

		this.id = id;
	}

	public void setWann(Date wann) {

		this.wann = wann;
	}

	public void setWas(Typ was) {

		this.was = was;
	}

	public void setWieviel(int wieviel) {

		this.wieviel = wieviel;
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

	public String getWer_Name() {

		return wer_Name;
	}

	public void setWer_Name(String wer_Name) {

		this.wer_Name = wer_Name;
	}

	public int getWer_Id() {

		return wer_Id;
	}

	public void setWer_Id(int wer_Id) {

		this.wer_Id = wer_Id;
	}

	public int getWomit_Nr() {

		return womit_Nr;
	}

	public void setWomit_Nr(int womit_Nr) {

		this.womit_Nr = womit_Nr;
	}

	public String getWomit_Bezeichnung() {

		return womit_Bezeichnung;
	}

	public void setWomit_Bezeichnung(String womit_Bezeichnung) {

		this.womit_Bezeichnung = womit_Bezeichnung;
	}
}
