package eshop.common.net;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.data_objects.Rechnung;
import eshop.common.data_objects.Warenkorb;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleAlreadyInBasketException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.ArticleStockNotSufficientException;
import eshop.common.exceptions.BasketNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.LoginFailedException;
import eshop.common.exceptions.MaxIDsException;
import eshop.common.exceptions.PersonNonexistantException;

// TODO: Auto-generated Javadoc
/**
 * The Interface ShopRemote.
 */
public interface ShopRemote extends Remote {

	/**
	 * Adds the shop event listener.
	 *
	 * @param shopEventListener
	 *           the shop event listener
	 * @throws RemoteException
	 *            the remote exception
	 */
	public void addShopEventListener(ShopEventListener shopEventListener) throws RemoteException;

	/**
	 * Alle artikel ausgeben.
	 *
	 * @param user
	 *           the user
	 * @return the vector
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Vector<Artikel> alleArtikelAusgeben(Person user) throws AccessRestrictedException, RemoteException;

	/**
	 * Alle ereignisse ausgeben.
	 *
	 * @param user
	 *           the user
	 * @return the vector
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Vector<Ereignis> alleEreignisseAusgeben(Person user) throws AccessRestrictedException, RemoteException;

	/**
	 * Alle kunden ausgeben.
	 *
	 * @param user
	 *           the user
	 * @return the vector
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Vector<Kunde> alleKundenAusgeben(Person user) throws AccessRestrictedException, RemoteException;

	/**
	 * Alle mitarbeiter ausgeben.
	 *
	 * @param user
	 *           the user
	 * @return the vector
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Vector<Mitarbeiter> alleMitarbeiterAusgeben(Person user) throws AccessRestrictedException, RemoteException;

	/**
	 * Alle warenkoerbe ausgeben.
	 *
	 * @param user
	 *           the user
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	void alleWarenkoerbeAusgeben(Person user) throws AccessRestrictedException, RemoteException;

	/**
	 * Anmelden.
	 *
	 * @param id
	 *           the id
	 * @param passwort
	 *           the passwort
	 * @return the person
	 * @throws LoginFailedException
	 *            the login failed exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Person anmelden(int id, String passwort) throws LoginFailedException, RemoteException;

	/**
	 * Artikel aendern.
	 *
	 * @param artikelnummer
	 *           the artikelnummer
	 * @param person
	 *           the person
	 * @param bezeichnung
	 *           the bezeichnung
	 * @param bestand
	 *           the bestand
	 * @param operator
	 *           the operator
	 * @param preis
	 *           the preis
	 * @param packungsgroesse
	 *           the packungsgroesse
	 * @param artikelinfo
	 *           the artikelinfo
	 * @return the artikel
	 * @throws RemoteException
	 *            the remote exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidAmountException
	 *            the invalid amount exception
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 */
	Artikel artikelAendern(int artikelnummer, Person person, String bezeichnung, int bestand, String operator,
			double preis, int packungsgroesse, String artikelinfo)
			throws RemoteException, AccessRestrictedException, InvalidAmountException, ArticleNonexistantException;

	/**
	 * Artikel aus warenkorb entfernen.
	 *
	 * @param artikelnummer
	 *           the artikelnummer
	 * @param user
	 *           the user
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 */
	void artikelAusWarenkorbEntfernen(int artikelnummer, Person user)
			throws AccessRestrictedException, RemoteException, PersonNonexistantException, ArticleNonexistantException;

	/**
	 * Artikel in warenkorb aendern.
	 *
	 * @param artikelnummer
	 *           the artikelnummer
	 * @param anz
	 *           the anz
	 * @param user
	 *           the user
	 * @throws ArticleStockNotSufficientException
	 *            the article stock not sufficient exception
	 * @throws BasketNonexistantException
	 *            the basket nonexistant exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidAmountException
	 *            the invalid amount exception
	 * @throws RemoteException
	 *            the remote exception
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	void artikelInWarenkorbAendern(int artikelnummer, int anz, Person user)
			throws ArticleStockNotSufficientException, BasketNonexistantException, AccessRestrictedException,
			InvalidAmountException, RemoteException, ArticleNonexistantException, PersonNonexistantException;

	/**
	 * Artikel in warenkorb legen.
	 *
	 * @param artikelnummer
	 *           the artikelnummer
	 * @param anz
	 *           the anz
	 * @param id
	 *           the id
	 * @param user
	 *           the user
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 * @throws ArticleStockNotSufficientException
	 *            the article stock not sufficient exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidAmountException
	 *            the invalid amount exception
	 * @throws ArticleAlreadyInBasketException
	 *            the article already in basket exception
	 * @throws RemoteException
	 *            the remote exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	void artikelInWarenkorbLegen(int artikelnummer, int anz, int id, Person user)
			throws ArticleNonexistantException, ArticleStockNotSufficientException, AccessRestrictedException,
			InvalidAmountException, ArticleAlreadyInBasketException, RemoteException, PersonNonexistantException;

	/**
	 * Artikel loeschen.
	 *
	 * @param artikelnummer
	 *           the artikelnummer
	 * @param user
	 *           the user
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 */
	void artikelLoeschen(int artikelnummer, Person user)
			throws AccessRestrictedException, RemoteException, ArticleNonexistantException;

	/**
	 * Artikel suchen.
	 *
	 * @param artikelnummer
	 *           the artikelnummer
	 * @param user
	 *           the user
	 * @return the artikel
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Artikel artikelSuchen(int artikelnummer, Person user)
			throws ArticleNonexistantException, AccessRestrictedException, RemoteException;

	/**
	 * Artikel suchen.
	 *
	 * @param artikelbezeichnung
	 *           the artikelbezeichnung
	 * @param user
	 *           the user
	 * @return the vector
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Vector<Artikel> artikelSuchen(String artikelbezeichnung, Person user)
			throws ArticleNonexistantException, AccessRestrictedException, RemoteException;

	/**
	 * Ereignis suchen.
	 *
	 * @param ereignisID
	 *           the ereignis ID
	 * @param person
	 *           the person
	 * @return the ereignis
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Ereignis ereignisSuchen(int ereignisID, Person person)
			throws ArticleNonexistantException, AccessRestrictedException, RemoteException;

	/**
	 * Erstelle artikel.
	 *
	 * @param bezeichnung
	 *           the bezeichnung
	 * @param bestand
	 *           the bestand
	 * @param preis
	 *           the preis
	 * @param packungsgroesse
	 *           the packungsgroesse
	 * @param person
	 *           the person
	 * @param artikelinfo
	 *           the artikelinfo
	 * @param picture
	 *           the picture
	 * @return the artikel
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidAmountException
	 *            the invalid amount exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, int packungsgroesse, Person person,
			String artikelinfo, String picture) throws AccessRestrictedException, InvalidAmountException, RemoteException;

	/**
	 * Erstelle kunde.
	 *
	 * @param firstname
	 *           the firstname
	 * @param lastname
	 *           the lastname
	 * @param passwort
	 *           the passwort
	 * @param address_Street
	 *           the address street
	 * @param address_Zip
	 *           the address zip
	 * @param address_Town
	 *           the address town
	 * @param user
	 *           the user
	 * @return the kunde
	 * @throws MaxIDsException
	 *            the max I ds exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Kunde erstelleKunde(String firstname, String lastname, String passwort, String address_Street, String address_Zip,
			String address_Town, Person user)
			throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException, RemoteException;

	/**
	 * Erstelle mitatbeiter.
	 *
	 * @param firstname
	 *           the firstname
	 * @param lastname
	 *           the lastname
	 * @param passwort
	 *           the passwort
	 * @param address_Street
	 *           the address street
	 * @param address_Zip
	 *           the address zip
	 * @param address_Town
	 *           the address town
	 * @param user
	 *           the user
	 * @return the mitarbeiter
	 * @throws MaxIDsException
	 *            the max I ds exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Mitarbeiter erstelleMitatbeiter(String firstname, String lastname, String passwort, String address_Street,
			String address_Zip, String address_Town, Person user)
			throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException, RemoteException;

	/**
	 * Kunde suchen.
	 *
	 * @param kundenId
	 *           the kunden id
	 * @param user
	 *           the user
	 * @return the kunde
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Kunde kundeSuchen(int kundenId, Person user) throws PersonNonexistantException, RemoteException;

	/**
	 * Lade daten.
	 *
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	void ladeDaten() throws IOException, ArticleNonexistantException, PersonNonexistantException,
			InvalidPersonDataException, RemoteException;

	/**
	 * Mitarbeiter suchen.
	 *
	 * @param mitarbeiterId
	 *           the mitarbeiter id
	 * @param user
	 *           the user
	 * @return the mitarbeiter
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	Mitarbeiter mitarbeiterSuchen(int mitarbeiterId, Person user) throws PersonNonexistantException, RemoteException;

	/**
	 * Person aendern.
	 *
	 * @param personenTyp
	 *           the personen typ
	 * @param user
	 *           the user
	 * @param firstname
	 *           the firstname
	 * @param lastname
	 *           the lastname
	 * @param personenId
	 *           the personen id
	 * @param passwort
	 *           the passwort
	 * @param address_Street
	 *           the address street
	 * @param address_Zip
	 *           the address zip
	 * @param address_Town
	 *           the address town
	 * @return the person
	 * @throws RemoteException
	 *            the remote exception
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	Person personAendern(String personenTyp, Person user, String firstname, String lastname, int personenId,
			String passwort, String address_Street, String address_Zip, String address_Town)
			throws RemoteException, AccessRestrictedException, InvalidPersonDataException, PersonNonexistantException;

	/**
	 * Person loeschen.
	 *
	 * @param zuloeschendePerson
	 *           the zuloeschende person
	 * @param user
	 *           the user
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	void personLoeschen(Person zuloeschendePerson, Person user)
			throws AccessRestrictedException, RemoteException, InvalidPersonDataException, PersonNonexistantException;

	/**
	 * Removes the shop event listener.
	 *
	 * @param listener
	 *           the listener
	 * @throws RemoteException
	 *            the remote exception
	 */
	public void removeShopEventListener(ShopEventListener listener) throws RemoteException;

	/**
	 * Schreibe daten.
	 *
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	void schreibeDaten() throws IOException;

	/**
	 * Warenkorb ausgeben.
	 *
	 * @param id
	 *           the id
	 * @param user
	 *           the user
	 * @return the warenkorb
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	Warenkorb warenkorbAusgeben(int id, Person user)
			throws AccessRestrictedException, RemoteException, PersonNonexistantException;

	/**
	 * Warenkorb kaufen.
	 *
	 * @param user
	 *           the user
	 * @return the rechnung
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidAmountException
	 *            the invalid amount exception
	 * @throws RemoteException
	 *            the remote exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	Rechnung warenkorbKaufen(Person user)
			throws AccessRestrictedException, InvalidAmountException, RemoteException, PersonNonexistantException;

	/**
	 * Warenkorb leeren.
	 *
	 * @param user
	 *           the user
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws RemoteException
	 *            the remote exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 */
	void warenkorbLeeren(Person user) throws AccessRestrictedException, RemoteException, PersonNonexistantException;
}
