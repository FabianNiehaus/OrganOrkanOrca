package eshop.server.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Typ;
import eshop.common.exceptions.InvalidPersonDataException;

// TODO: Auto-generated Javadoc
/**
 * The Class FilePersistenceManager.
 */
public class FilePersistenceManager implements PersistenceManager {

	/** The reader. */
	private BufferedReader	reader	= null;
	
	/** The writer. */
	private PrintWriter		writer	= null;

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
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see eshop.server.persistence.PersistenceManager#ladeArtikel()
	 */
	@Override
	public Artikel ladeArtikel() throws IOException {

		int artikelnummer = 0;
		String bezeichnung = "";
		double preis = 0;
		int bestand = 0;
		int packungsgroesse = 0;
		Map<Integer, Integer> bestandsverlauf = new LinkedHashMap<>();
		String artikelinfo = "";
		String picture = "";
		// Lies Artikelnummer
		try {
			artikelnummer = Integer.parseInt(liesZeile());
		} catch (NumberFormatException nfe) {
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
		// Lies Artikelinfo
		artikelinfo = liesZeile();
		// Lies Bild
		picture = liesZeile();
		// Lies Bestandshistory
		try {
			reader.mark(400);
			if (liesZeile().equals("---BEGINHISTORY---")) {
				String content = liesZeile();
				while (!content.equals("---ENDHISTORY---")) {
					String[] contents = content.split(":");
					bestandsverlauf.put(Integer.parseInt(contents[0]), Integer.parseInt(contents[1]));
					reader.mark(30);
					content = liesZeile();
				}
			} else {
				reader.reset();
			}
		} catch (NullPointerException e) {
		}
		if (packungsgroesse == 1) {
			return new Artikel(bezeichnung, artikelnummer, bestand, preis, bestandsverlauf, artikelinfo, picture);
		} else {
			return new Massengutartikel(bezeichnung, artikelnummer, bestand, preis, packungsgroesse, bestandsverlauf,
					artikelinfo, picture);
		}
	}

	/* (non-Javadoc)
	 * @see eshop.server.persistence.PersistenceManager#ladeEreignis()
	 */
	@Override
	public Ereignis ladeEreignis() throws IOException {

		int id = 0;
		int wer_Id = 0;
		String wer_Name = "";
		Typ was = null;
		int womit_Nr = 0;
		String womit_Bezeichnung = "";
		int wieviel = 0;
		String wann;
		try {
			id = Integer.parseInt(liesZeile());
		} catch (NumberFormatException nfe) {
			// Abbruch wenn Leerzeile -> keine Ereignisse mehr vorhanden
			return null;
		}
		wer_Id = Integer.parseInt(liesZeile());
		wer_Name = liesZeile();
		was = Typ.valueOf(liesZeile());
		womit_Nr = Integer.parseInt(liesZeile());
		womit_Bezeichnung = liesZeile();
		wieviel = Integer.parseInt(liesZeile());
		wann = liesZeile();
		Date date = null;
		DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		try {
			date = formatter.parse(wann);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Ereignis(id, wer_Id, wer_Name, was, womit_Nr, womit_Bezeichnung, wieviel, date);
	}

	/* (non-Javadoc)
	 * @see eshop.server.persistence.PersistenceManager#ladeKunde()
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
		} catch (NumberFormatException nfe) {
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

	/* (non-Javadoc)
	 * @see eshop.server.persistence.PersistenceManager#ladeMitarbeiter()
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
		} catch (NumberFormatException nfe) {
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
	 * Lies zeile.
	 *
	 * @return the string
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	private String liesZeile() throws IOException {

		if (reader != null) try {
			return reader.readLine();
		} catch (IOException ie) {
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
		} catch (FileNotFoundException fnfe) {
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
	 * Schreibe zeile.
	 *
	 * @param daten
	 *           the daten
	 */
	private void schreibeZeile(String daten) {

		if (writer != null) writer.println(daten);
	}

	/* (non-Javadoc)
	 * @see eshop.server.persistence.PersistenceManager#speichereArtikel(eshop.common.data_objects.Artikel)
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
		// Schreibe Artikelinfo
		schreibeZeile(art.getArtikelinfo());
		// Schreibe Bild
		schreibeZeile(art.getPicture());
		// Schreibe History
		schreibeZeile("---BEGINHISTORY---");
		// Bestandhistory schreiben
		for (Entry<Integer, Integer> ent : art.getBestandsverlauf().entrySet()) {
			schreibeZeile(String.valueOf(ent.getKey()) + ":" + String.valueOf(ent.getValue()));
		}
		schreibeZeile("---ENDHISTORY---");
		return true;
	}

	/* (non-Javadoc)
	 * @see eshop.server.persistence.PersistenceManager#speichereEreignis(eshop.common.data_objects.Ereignis)
	 */
	@Override
	public boolean speichereEreignis(Ereignis er) throws IOException {

		// Schreibe
		schreibeZeile(String.valueOf(er.getId()));
		schreibeZeile(String.valueOf(er.getWer_Id()));
		schreibeZeile(String.valueOf(er.getWer_Name()));
		schreibeZeile(String.valueOf(er.getTyp()));
		schreibeZeile(String.valueOf(er.getWomit_Nr()));
		schreibeZeile(String.valueOf(er.getWomit_Bezeichnung()));
		schreibeZeile(String.valueOf(er.getWieviel()));
		// Datum wird richtig formatiert
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		schreibeZeile(String.valueOf(dateFormat.format(er.getWann())));
		return true;
	}

	/* (non-Javadoc)
	 * @see eshop.server.persistence.PersistenceManager#speichereKunde(eshop.common.data_objects.Kunde)
	 */
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

	/* (non-Javadoc)
	 * @see eshop.server.persistence.PersistenceManager#speichereMitarbeiter(eshop.common.data_objects.Mitarbeiter)
	 */
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
		// Schreibe Adresse
		schreibeZeile(mi.getAddress_Street());
		schreibeZeile(mi.getAddress_Zip());
		schreibeZeile(mi.getAddress_Town());
		return true;
	}
}
