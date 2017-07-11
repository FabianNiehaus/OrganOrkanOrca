package eshop.server.persistence;

import java.io.IOException;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.exceptions.InvalidPersonDataException;

// TODO: Auto-generated Javadoc
/**
 * The Interface PersistenceManager.
 */
public interface PersistenceManager {

	/**
	 * Close.
	 *
	 * @return true, if successful
	 */
	public boolean close();

	/**
	 * Lade artikel.
	 *
	 * @return the artikel
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public Artikel ladeArtikel() throws IOException;

	/**
	 * Lade ereignis.
	 *
	 * @return the ereignis
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public Ereignis ladeEreignis() throws IOException;

	/**
	 * Lade kunde.
	 *
	 * @return the kunde
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public Kunde ladeKunde() throws IOException, InvalidPersonDataException;

	/**
	 * Lade mitarbeiter.
	 *
	 * @return the mitarbeiter
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public Mitarbeiter ladeMitarbeiter() throws IOException, InvalidPersonDataException;

	/**
	 * Open for reading.
	 *
	 * @param datenquelle
	 *           the datenquelle
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void openForReading(String datenquelle) throws IOException;

	/**
	 * Open for writing.
	 *
	 * @param datenquelle
	 *           the datenquelle
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void openForWriting(String datenquelle) throws IOException;

	/**
	 * Speichere artikel.
	 *
	 * @param art
	 *           the artikel
	 * @return true, if successful
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public boolean speichereArtikel(Artikel art) throws IOException;

	/**
	 * Speichere ereignis.
	 *
	 * @param er
	 *           the er
	 * @return true, if successful
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public boolean speichereEreignis(Ereignis er) throws IOException;

	/**
	 * Speichere kunde.
	 *
	 * @param art
	 *           the artikel
	 * @return true, if successful
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public boolean speichereKunde(Kunde art) throws IOException;

	/**
	 * Speichere mitarbeiter.
	 *
	 * @param mi
	 *           the mitarbeiter
	 * @return true, if successful
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public boolean speichereMitarbeiter(Mitarbeiter mi) throws IOException;
}