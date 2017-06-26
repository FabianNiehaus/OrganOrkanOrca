package eshop.server.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Typ;
import eshop.common.exceptions.InvalidPersonDataException;

/**
 * @author teschke
 *
 *         Realisierung einer Schnittstelle zur persistenten Speicherung von
 *         Daten in Dateien.
 * @see bib.local.persistence.PersistenceManager
 */
public class FilePersistenceManager implements PersistenceManager {

    private BufferedReader reader = null;
    private PrintWriter	   writer = null;

    /*
     * (non-Javadoc)
     * @see persistence.PersistenceManager#close()
     */
    @Override
    public boolean close() {

	if (writer != null) writer.close();
	if (reader != null) {
	    try {
		reader.close();
	    } catch(IOException e) {
		e.printStackTrace();
		return false;
	    }
	}
	return true;
    }

    /**
     * Methode zum Einlesen der Artikeldaten aus einer externen Datenquelle. Das
     * Verfuegbarkeitsattribut ist in der Datenquelle (Datei) als "t" oder "f"
     * codiert abgelegt.
     * 
     * @return Artikel-Objekt, wenn Einlesen erfolgreich, false null
     */
    @Override
    public Artikel ladeArtikel() throws IOException {

	int artikelnummer = 0;
	String bezeichnung = "";
	double preis = 0;
	int bestand = 0;
	int packungsgroesse = 0;
	Map<Integer, Integer> bestandsverlauf = null;
	// Lies Artikelnummer
	try {
	    artikelnummer = Integer.parseInt(liesZeile());
	} catch(NumberFormatException nfe) {
	    // Abbruch wenn Leerzeile -> keine Artikel mehr vorhanden
	    return null;
	}
	// Lies Artikelbezeichnung
	bezeichnung = liesZeile();
	// Lies Artikel-Preis
	preis = Double.parseDouble(liesZeile());
	// Lies Artikel-Bestand
	bestand = Integer.parseInt(liesZeile());
	// Lies Packungsgröße
	packungsgroesse = Integer.parseInt(liesZeile());
	// Lies Bestandshistory
	try {
	    reader.mark(400);
	    if (liesZeile().equals("---BEGINHISTORY---")) {
		bestandsverlauf = new LinkedHashMap<>();
		String content = liesZeile();
		while (!liesZeile().equals("---ENDHISTORY---")) {
		    String[] contents = content.split(":");
		    bestandsverlauf.put(Integer.parseInt(contents[0]), Integer.parseInt(contents[1]));
		}
	    } else {
		reader.reset();
	    }
	} catch(NullPointerException e) {
	}
	if (packungsgroesse == 1) {
	    return new Artikel(bezeichnung, artikelnummer, bestand, preis, bestandsverlauf);
	} else {
	    return new Massengutartikel(bezeichnung, artikelnummer, bestand, preis, packungsgroesse, bestandsverlauf);
	}
    }

    @Override
    public Vector<Object> ladeEreignis() throws IOException {

	Vector<Object> ret = new Vector<Object>(6);
	int id = 0;
	int werId = 0;
	Typ was = null;
	int womitId = 0;
	int wieviel = 0;
	String wann;
	try {
	    id = Integer.parseInt(liesZeile());
	} catch(NumberFormatException nfe) {
	    // Abbruch wenn Leerzeile -> keine Ereignisse mehr vorhanden
	    return null;
	}
	werId = Integer.parseInt(liesZeile());
	was = Typ.valueOf(liesZeile());
	womitId = Integer.parseInt(liesZeile());
	wieviel = Integer.parseInt(liesZeile());
	wann = liesZeile();
	ret.add(id);
	ret.add(werId);
	ret.add(was);
	ret.add(womitId);
	ret.add(wieviel);
	ret.add(wann);
	return ret;
    }

    /**
     * @author Mathis M�hlenkamp
     * @throws InvalidPersonDataException
     */
    @Override
    public Kunde ladeKunde() throws IOException, InvalidPersonDataException {

	int id = 0;
	String firstname = "";
	String lastname = "";
	String passwort = "";
	String address_Street = "";
	String address_Zip = "";
	String address_Town = "";
	// Lies ID
	try {
	    id = Integer.parseInt(liesZeile());
	} catch(NumberFormatException nfe) {
	    // Abbruch wenn Leerzeile -> keine Kunden mehr vorhanden
	    return null;
	}
	// Lies firstname & lastname
	firstname = liesZeile();
	lastname = liesZeile();
	// Lies passwort
	passwort = liesZeile();
	// Lies Adresse
	address_Street = liesZeile();
	address_Zip = liesZeile();
	address_Town = liesZeile();
	return new Kunde(firstname, lastname, id, passwort, address_Street, address_Zip, address_Town);
    }

    /**
     * @author Mathis M�hlenkamp
     * @throws InvalidPersonData
     */
    @Override
    public Mitarbeiter ladeMitarbeiter() throws IOException, InvalidPersonDataException {

	int id = 0;
	String firstname = "";
	String lastname = "";
	String passwort = "";
	String address_Street = "";
	String address_Zip = "";
	String address_Town = "";
	// Lies ID
	try {
	    id = Integer.parseInt(liesZeile());
	} catch(NumberFormatException nfe) {
	    // Abbruch wenn Leerzeile -> keine Kunden mehr vorhanden
	    return null;
	}
	// Lies firstname & lastname
	firstname = liesZeile();
	lastname = liesZeile();
	// Lies passwort
	passwort = liesZeile();
	// Lies Adresse
	address_Street = liesZeile();
	address_Zip = liesZeile();
	address_Town = liesZeile();
	return new Mitarbeiter(firstname, lastname, id, passwort, address_Street, address_Zip, address_Town);
    }

    /**
     * Liest eine Zeile aus
     * 
     * @return Inhalt der Zeile als String
     * @throws IOException
     */
    private String liesZeile() throws IOException {

	if (reader != null) try {
	    return reader.readLine();
	} catch(IOException ie) {
	    return "";
	}
	else return "";
    }

    /*
     * (non-Javadoc)
     * @see persistence.PersistenceManager#openForReading(java.lang.String)
     */
    @Override
    public void openForReading(String datei) throws FileNotFoundException {

	try {
	    reader = new BufferedReader(new FileReader(datei));
	} catch(FileNotFoundException fnfe) {
	    throw new FileNotFoundException(datei);
	}
    }

    /*
     * (non-Javadoc)
     * @see persistence.PersistenceManager#openForWriting(java.lang.String)
     */
    @Override
    public void openForWriting(String datei) throws IOException {

	writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
	// Dokument leeren
	if (writer != null) {
	    writer.print("");
	    close();
	    writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
	}
    }

    /**
     * Schreibt eine Zeile
     * 
     * @param daten
     *            Zu schreibende Zeile als String
     */
    private void schreibeZeile(String daten) {

	if (writer != null) writer.println(daten);
    }

    /**
     * Methode zum Schreiben der Artikeldaten in eine externe Datenquelle. Das
     * Verfuegbarkeitsattribut wird in der Datenquelle (Datei) als "t" oder "f"
     * codiert abgelegt.
     * 
     * @param art
     *            Artikel-Objekt, das gespeichert werden soll
     * @return true, wenn Schreibvorgang erfolgreich, false sonst
     */
    @Override
    public boolean speichereArtikel(Artikel art) throws IOException {

	// Schreibe Artikelnummer
	schreibeZeile(String.valueOf(art.getArtikelnummer()));
	// Schreibe Artikelbezeichnung
	schreibeZeile(art.getBezeichnung());
	// Schreibe Preis
	schreibeZeile(String.valueOf(art.getPreis()));
	// Schreibe Bestand
	schreibeZeile(String.valueOf(art.getBestand()));
	// wenn Artikel ein Massengutartikel ist, wird die Packungsgr��e
	// geschrieben, ansonsten "0"
	if (art instanceof Massengutartikel) {
	    Massengutartikel tmp = (Massengutartikel) art;
	    schreibeZeile(String.valueOf(tmp.getPackungsgroesse()));
	} else {
	    schreibeZeile(String.valueOf(1));
	}
	schreibeZeile("---BEGINHISTORY---");
	// Bestandhistory schreiben
	for (Entry<Integer, Integer> ent : art.getBestandsverlauf().entrySet()) {
	    schreibeZeile(String.valueOf(ent.getKey()) + ":" + String.valueOf(ent.getValue()));
	}
	schreibeZeile("---ENDHISTORY---");
	return true;
    }

    @Override
    public boolean speichereEreignis(Ereignis er) throws IOException {

	// Schreibe
	schreibeZeile(String.valueOf(er.getId()));
	schreibeZeile(String.valueOf(er.getWer().getId()));
	schreibeZeile(String.valueOf(er.getTyp()));
	schreibeZeile(String.valueOf(er.getWomit().getArtikelnummer()));
	schreibeZeile(String.valueOf(er.getWieviel()));
	// Datum wird richtig formatiert
	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	schreibeZeile(String.valueOf(dateFormat.format(er.getWann())));
	return true;
    }

    @Override
    public boolean speichereKunde(Kunde ku) throws IOException {

	// Schreibe ID
	schreibeZeile(String.valueOf(ku.getId()));
	// Schreibe firstname
	schreibeZeile(ku.getFirstname());
	// Schreibe lastname
	schreibeZeile(ku.getLastname());
	// Schreibe passwort
	schreibeZeile(ku.getPasswort());
	// Schreibe Adresse
	schreibeZeile(ku.getAddress_Street());
	schreibeZeile(ku.getAddress_Zip());
	schreibeZeile(ku.getAddress_Town());
	return true;
    }

    @Override
    public boolean speichereMitarbeiter(Mitarbeiter mi) throws IOException {

	// Schreibe ID
	schreibeZeile(String.valueOf(mi.getId()));
	// Schreibe firstname
	schreibeZeile(mi.getFirstname());
	// Schreibe lastname
	schreibeZeile(mi.getLastname());
	// Schreibe passwort
	schreibeZeile(mi.getPasswort());
	return true;
    }
}
