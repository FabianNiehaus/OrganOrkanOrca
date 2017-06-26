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
    private static final long serialVersionUID = -1817043294145890221L;
    private int		      id;
    private Person	      wer;
    private Date	      wann;
    private Typ		      was;
    private Artikel	      womit;
    private int		      wieviel;

    /**
     * @param wer
     *            Person, die die Aktion durchgefuehrt hat
     * @param was
     *            Typ der Aktion (EINLAGERUNG, AUSLAGERUNG, KAUF, NEU)
     * @param womit
     *            Welcher Artikel ist betroffen
     * @param wieviel
     *            Betroffene Stueckzahl
     */
    public Ereignis(int id, Person wer, Typ was, Artikel womit, int wieviel, Date wann) {
	super();
	this.id = id;
	this.wer = wer;
	this.was = was;
	this.womit = womit;
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

    public Person getWer() {

	return wer;
    }

    public int getWieviel() {

	return wieviel;
    }

    public Artikel getWomit() {

	return womit;
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

    public void setWer(Person wer) {

	this.wer = wer;
    }

    public void setWieviel(int wieviel) {

	this.wieviel = wieviel;
    }

    public void setWomit(Artikel womit) {

	this.womit = womit;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

	// Formatierungsvorlage fuer Datum
	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	return dateFormat.format(wann) + " | " + wer.getId() + ": " + wer.getLastname() + " " + wer.getLastname()
		+ " | " + was + " | " + womit.getArtikelnummer() + ": " + womit.getBezeichnung() + " | " + wieviel;
    }
}
