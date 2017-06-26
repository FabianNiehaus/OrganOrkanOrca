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

public interface ShopRemote extends Remote {

    public void addShopEventListener(ShopEventListener listener) throws RemoteException;

    /**
     * @return Alle in der Artikelverwaltung gespeicherten Artikel
     * @throws AccessRestrictedException
     */
    Vector<Artikel> alleArtikelAusgeben(Person p) throws AccessRestrictedException, RemoteException;

    Vector<Ereignis> alleEreignisseAusgeben(Person p) throws AccessRestrictedException, RemoteException;

    /**
     * @return Alle in der Kundenverwaltung gespeicherten Kunden
     * @throws AccessRestrictedException
     */
    Vector<Kunde> alleKundenAusgeben(Person p) throws AccessRestrictedException, RemoteException;

    /**
     * @return Alle in der Mitarbeiterverwaltung gespeicherten Mitarbeiter
     */
    Vector<Mitarbeiter> alleMitarbeiterAusgeben(Person p) throws AccessRestrictedException, RemoteException;

    /**
     * @return Alle in der Warenkorbverwaltung gespeicherten Warenkörbe
     * @throws AccessRestrictedException
     */
    void alleWarenkoerbeAusgeben(Person p) throws AccessRestrictedException, RemoteException;

    /**
     * Anmelden des Nutzers Kunden-ID 1000 - 8999, Mitarbeiter-ID 9000 - 9999
     * 
     * @param id
     *            Nutzer-ID
     * @param passwort
     *            Nutzer-Passwort
     * @return Objekt des Nutzers der Klasse Kunde oder Mitarbeiter
     * @throws LoginFailedException
     *             Anmeldung fehlgeschlagen
     */
    Person anmelden(int id, String passwort) throws LoginFailedException, RemoteException;

    void artikelAusWarenkorbEntfernen(Artikel art, Person p) throws AccessRestrictedException, RemoteException;

    /**
     * @param art
     * @param p
     * @return
     */
    public boolean artikelInWarenkorb(Artikel art, Person p) throws RemoteException;

    /**
     * Verändert die Anzahl eines Artikels im Warenkorb eines Kunden
     * 
     * @param pos
     *            Position des Artikels
     * @param anz
     *            Neue Anzahl
     * @param p
     *            Userobjekt
     * @throws ArticleStockNotSufficientException
     *             Artikelbestand nicht ausreichend
     * @throws AccessRestrictedException
     * @throws InvalidAmountException
     */
    void artikelInWarenkorbAendern(Artikel art, int anz, Person p) throws ArticleStockNotSufficientException,
	    BasketNonexistantException, AccessRestrictedException, InvalidAmountException, RemoteException;

    /**
     * Legt einen Artikel in den Warenkorb
     * 
     * @param artikelnummer
     *            Auszuwählender Artikel
     * @param anzahl
     *            Auszuwählende Anzahl
     * @param p
     *            Userobjekt
     * @throws ArticleNonexistantException
     *             Artikelnummer existiert nicht
     * @throws ArticleStockNotSufficientException
     *             Artikelbestand nicht ausreichend
     * @throws AccessRestrictedException
     * @throws InvalidAmountException
     * @throws ArticleAlreadyInBasketException
     */
    void artikelInWarenkorbLegen(int artikelnummer, int anz, Person p)
	    throws ArticleNonexistantException, ArticleStockNotSufficientException, AccessRestrictedException,
	    InvalidAmountException, ArticleAlreadyInBasketException, RemoteException;

    void artikelLoeschen(Artikel art, Person p) throws AccessRestrictedException, RemoteException;

    /**
     * Erlaubt die Suche nach einer Artikelnummer
     * 
     * @param artikelnummer
     *            Artikelnumemr von geuschtem Artikel
     * @return Gesuchter Artikel
     * @throws ArticleNonexistantException
     * @throws AccessRestrictedException
     */
    Artikel artikelSuchen(int artikelnummer, Person p)
	    throws ArticleNonexistantException, AccessRestrictedException, RemoteException;

    /**
     * Erlaubt die Suche nach einer Artikelbezeichnung
     * 
     * @param bezeichnung
     *            (Teil-)Bezeichnung des gesuchten Artikels
     * @return Liste der zur Bezeichnung passenden Artikel
     * @throws ArticleNonexistantException
     *             Keine Artikel gefunden
     * @throws AccessRestrictedException
     */
    Vector<Artikel> artikelSuchen(String bezeichnung, Person p)
	    throws ArticleNonexistantException, AccessRestrictedException, RemoteException;

    /**
     * Erhöht den Bestand eines Artikels
     * 
     * @param artikelnummer
     *            Artikelnummer des zu bearbeitenden Artikels
     * @param bestand
     *            Neuer Bestand
     * @param p
     *            Userobjekt
     * @return Bearbeiteten Artikel
     * @throws ArticleNonexistantException
     *             Artikelnummer existiert nicht
     * @throws AccessRestrictedException
     * @throws InvalidAmountException
     */
    Artikel erhoeheArtikelBestand(int artikelnummer, int bestand, Person p)
	    throws ArticleNonexistantException, AccessRestrictedException, InvalidAmountException, RemoteException;

    /**
     * Erstellt einen neuen Artikel
     * 
     * @param bezeichnung
     *            Artikelbezeichnung
     * @param bestand
     *            Artikelbestamd
     * @param preis
     *            Artikelpreis
     * @param p
     *            Userobjekt
     * @return Erstellten Artikel
     * @throws AccessRestrictedException
     * @throws InvalidAmountException
     */
    Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, int packungsgroesse, Person p)
	    throws AccessRestrictedException, InvalidAmountException, RemoteException;

    /**
     * Erstellt einen neuen Kunden mit fortlaufender Kundennummer
     * 
     * @param firstname
     *            Vorname des anzulegenden Kunden
     * @param lastname
     *            Nachname des anzulegenden Kunden
     * @throws AccessRestrictedException
     * @throws InvalidPersonDataException
     */
    Kunde erstelleKunde(String firstname, String lastname, String passwort, String address_Street, String address_Zip,
	    String address_Town, Person p)
	    throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException, RemoteException;

    /**
     * Erstellt einen neuen Mitarbeiter mit fortlaufender Kundennummer
     * 
     * @param firstname
     *            Vorname des anzulegenden Kunden
     * @param lastname
     *            Nachname des anzulegenden Kunden
     * @throws AccessRestrictedException
     * @throws InvalidPersonDataException
     */
    Mitarbeiter erstelleMitatbeiter(String firstname, String lastname, String passwort, String address_Street,
	    String address_Zip, String address_Town, Person p)
	    throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException, RemoteException;

    Kunde kundeSuchen(int id, Person p) throws PersonNonexistantException, RemoteException;

    void ladeDaten() throws IOException, ArticleNonexistantException, PersonNonexistantException,
	    InvalidPersonDataException, RemoteException;

    Mitarbeiter mitarbeiterSuchen(int id, Person p) throws PersonNonexistantException, RemoteException;

    void personLoeschen(Person loeschen, Person p) throws AccessRestrictedException, RemoteException;

    public void removeShopEventListener(ShopEventListener listener) throws RemoteException;

    /**
     * Schreibt die Daten der Verwaltungen in die Persistenz
     * 
     * @throws IOException
     */
    void schreibeDaten() throws IOException;

    /**
     * Gibt den Warenkorb eines Kunden zurueck
     * 
     * @param p
     *            Userobjekt
     * @return Warenkorb
     * @throws AccessRestrictedException
     */
    Warenkorb warenkorbAusgeben(Person p) throws AccessRestrictedException, RemoteException;

    /**
     * Warenkorb kaufen und Rechnung erstellen
     * 
     * @param p
     *            Userobjekt
     * @return Erstellte Rechnung
     * @throws AccessRestrictedException
     * @throws InvalidAmountException
     */
    Rechnung warenkorbKaufen(Person p) throws AccessRestrictedException, InvalidAmountException, RemoteException;

    /**
     * Leert den Warenkorb eines Kunden
     * 
     * @param p
     *            Userobjekt
     * @throws AccessRestrictedException
     */
    void warenkorbLeeren(Person p) throws AccessRestrictedException, RemoteException;
}
