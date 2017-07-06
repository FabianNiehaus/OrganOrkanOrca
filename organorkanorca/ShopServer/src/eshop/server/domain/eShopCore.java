package eshop.server.domain;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.data_objects.Rechnung;
import eshop.common.data_objects.Typ;
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
import eshop.common.net.ShopEventListener;
import eshop.common.net.ShopRemote;
import eshop.common.util.IO;

/**
 * @author Fabian Niehaus Zentrales Modul des eShop
 */
public class eShopCore extends UnicastRemoteObject implements ShopRemote {

    private Artikelverwaltung	  av;
    private Kundenverwaltung	  kv;
    private Mitarbeiterverwaltung mv;
    private Warenkorbverwaltung	  wv;
    private Rechnungsverwaltung	  rv;
    private Ereignisverwaltung	  ev;
    private String		  dateipfad = "";
    private Object		  warenkorbKey;
    private Object		  artikelKey;
    private Object		  benutzerKey;

    /**
     * @throws PersonNonexistantException
     * @throws ArticleNonexistantException
     * @throws InvalidPersonDataException
     */
    public eShopCore()
	    throws IOException, ArticleNonexistantException, PersonNonexistantException, InvalidPersonDataException {
	super();
	av = new Artikelverwaltung();
	kv = new Kundenverwaltung();
	mv = new Mitarbeiterverwaltung();
	wv = new Warenkorbverwaltung();
	rv = new Rechnungsverwaltung();
	ev = new Ereignisverwaltung(kv, mv, av);
	ladeDaten();
    }

    public static void main(String[] args) {

	String serviceName = "eShopServer";
	try {
	    ShopRemote eShop = new eShopCore();
	    LocateRegistry.createRegistry(1099);
	    Naming.rebind("//" + InetAddress.getLocalHost().getHostName() + ":1099/" + serviceName, eShop);
	    IO.println("Shop erfolgreich gestartet. Bindung an " + "//" + InetAddress.getLocalHost().getHostAddress()
		    + ":1099/");
	    IO.println("Zum Beenden \"exit\" eingeben");
	    while (!IO.readString().equals("exit")) {
	    }
	    IO.println("eShop wird beendet");
	    System.exit(0);
	} catch(IOException | ArticleNonexistantException | PersonNonexistantException | InvalidPersonDataException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void addShopEventListener(ShopEventListener listener) throws RemoteException {

	// TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#alleArtikelAusgeben(data_objects.Person)
     */
    @Override
    public synchronized Vector<Artikel> alleArtikelAusgeben(Person p)
	    throws AccessRestrictedException, RemoteException {

	// synchronized (av) {
	if (istKunde(p) || istMitarbeiter(p)) {
	    return av.getArtikel();
	} else {
	    throw new AccessRestrictedException(p, "\"Alle Artikel ausgeben\"");
	}
	// }
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#alleEreignisseAusgeben(data_objects.Person)
     */
    @Override
    public Vector<Ereignis> alleEreignisseAusgeben(Person p) throws AccessRestrictedException, RemoteException {

	if (istMitarbeiter(p)) {
	    return ev.getEreignisse();
	} else {
	    throw new AccessRestrictedException(p, "Kunde löschen");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#alleKundenAusgeben(data_objects.Person)
     */
    @Override
    public synchronized Vector<Kunde> alleKundenAusgeben(Person p) throws AccessRestrictedException, RemoteException {

	if (istMitarbeiter(p)) {
	    return kv.getKunden();
	} else {
	    throw new AccessRestrictedException(p, "Kundenverwaltung");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#alleMitarbeiterAusgeben(data_objects.Person)
     */
    @Override
    public synchronized Vector<Mitarbeiter> alleMitarbeiterAusgeben(Person p)
	    throws AccessRestrictedException, RemoteException {

	if (istMitarbeiter(p)) {
	    return mv.getMitarbeiter();
	} else {
	    throw new AccessRestrictedException(p, "Mitarbeiterverwaltung");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#alleWarenkoerbeAusgeben(data_objects.Person)
     */
    @Override
    public void alleWarenkoerbeAusgeben(Person p) throws AccessRestrictedException, RemoteException {

	if (istMitarbeiter(p)) {
	    for (Warenkorb w : wv.getWarenkoerbe()) {
		w.toString();
	    }
	} else {
	    throw new AccessRestrictedException(p, "\"Alle Warenkörbe ausgeben\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#anmelden(int, java.lang.String)
     */
    @Override
    public synchronized Person anmelden(int id, String passwort) throws LoginFailedException, RemoteException {

	if (id >= 1000 && id < 9000) {
	    return kv.anmelden(id, passwort);
	} else if (id >= 9000 && id < 10000) {
	    return mv.anmelden(id, passwort);
	} else {
	    throw new LoginFailedException();
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#artikelAusWarenkorbEntfernen(data_objects.Artikel,
     * data_objects.Person)
     */
    @Override
    public void artikelAusWarenkorbEntfernen(Artikel art, Person p) throws AccessRestrictedException, RemoteException {

	if (istKunde(p)) {
	    Warenkorb wk = kv.gibWarenkorbVonKunde(p);
	    wv.loescheAusWarenkorn(wk, art);
	} else {
	    throw new AccessRestrictedException(p, "\"Artikel aus Warenkorb löschen\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see
     * eshop.common.net.ShopRemote#artikelInWarenkorb(eshop.common.data_objects.
     * Artikel, eshop.common.data_objects.Person)
     */
    @Override
    public boolean artikelInWarenkorb(Artikel art, Person p) throws RemoteException {

	if (istKunde(p)) return ((Kunde) p).getWarenkorb().sucheArtikel(art);
	else return false;
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#artikelInWarenkorbAendern(data_objects.Artikel,
     * int, data_objects.Person)
     */
    @Override
    public synchronized void artikelInWarenkorbAendern(Artikel art, int anz, Person p)
	    throws ArticleStockNotSufficientException, BasketNonexistantException, AccessRestrictedException,
	    InvalidAmountException, RemoteException {

	if (istKunde(p)) {
	    Warenkorb wk = kv.gibWarenkorbVonKunde(p);
	    wv.aendereWarenkorb(wk, art, anz);
	} else {
	    throw new AccessRestrictedException(p, "\"Anzahl Artikel in Warenkorb ändern\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#artikelInWarenkorbLegen(int, int,
     * data_objects.Person)
     */
    @Override
    public void artikelInWarenkorbLegen(int artikelnummer, int anz, Person p)
	    throws ArticleNonexistantException, ArticleStockNotSufficientException, AccessRestrictedException,
	    InvalidAmountException, ArticleAlreadyInBasketException, RemoteException {

	if (istKunde(p)) {
	    Warenkorb wk = kv.gibWarenkorbVonKunde(p);
	    if (wk == null) {
		wk = wv.erstelleWarenkorb();
		kv.weiseWarenkorbzu((Kunde) p, wk);
	    }
	    Artikel art = av.sucheArtikel(artikelnummer);
	    wv.legeInWarenkorb(wk, art, anz);
	} else {
	    throw new AccessRestrictedException(p, "\"Artikel in Warenkorb legen\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#artikelLoeschen(data_objects.Artikel,
     * data_objects.Person)
     */
    @Override
    public void artikelLoeschen(Artikel art, Person p) throws AccessRestrictedException, RemoteException {

	if (istMitarbeiter(p)) {
	    av.loeschen(art);
	} else {
	    throw new AccessRestrictedException(p, "\"Artikel suchen (Artikelnummer)\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#artikelSuchen(int, data_objects.Person)
     */
    @Override
    public Artikel artikelSuchen(int artikelnummer, Person p)
	    throws ArticleNonexistantException, AccessRestrictedException, RemoteException {

	if (istMitarbeiter(p) || istKunde(p)) {
	    return av.sucheArtikel(artikelnummer);
	} else {
	    throw new AccessRestrictedException(p, "\"Artikel suchen (Artikelnummer)\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#artikelSuchen(java.lang.String,
     * data_objects.Person)
     */
    @Override
    public Vector<Artikel> artikelSuchen(String bezeichnung, Person p)
	    throws ArticleNonexistantException, AccessRestrictedException, RemoteException {

	if (istMitarbeiter(p) || istKunde(p)) {
	    return av.sucheArtikel(bezeichnung);
	} else {
	    throw new AccessRestrictedException(p, "\"Artikel suchen (Bezeichnung)\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#erhoeheArtikelBestand(int, int,
     * data_objects.Person)
     */
    @Override
    public synchronized Artikel erhoeheArtikelBestand(int artikelnummer, int bestand, Person p)
	    throws ArticleNonexistantException, AccessRestrictedException, InvalidAmountException, RemoteException {

	if (istMitarbeiter(p)) {
	    Artikel art = av.erhoeheBestand(artikelnummer, bestand);
	    // Ereignis erzeugen
	    ev.ereignisErstellen(p, Typ.EINLAGERUNG, art, bestand);
	    return art;
	} else {
	    throw new AccessRestrictedException(p, "\"Bestand erhöhen\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#erstelleArtikel(java.lang.String, int, double,
     * int, data_objects.Person)
     */
    @Override
    public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, int packungsgroesse, Person p)
	    throws AccessRestrictedException, InvalidAmountException, RemoteException {

	if (istMitarbeiter(p)) {
	    Artikel art = av.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse);
	    // Ereignis erzeugen
	    ev.ereignisErstellen(p, Typ.NEU, art, bestand);
	    return art;
	} else {
	    throw new AccessRestrictedException(p, "\"Artikel anlegen\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#erstelleKunde(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * data_objects.Person)
     */
    @Override
    public Kunde erstelleKunde(String firstname, String lastname, String passwort, String address_Street,
	    String address_Zip, String address_Town, Person p)
	    throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException, RemoteException {

	if (istMitarbeiter(p) || p == null) {
	    return kv.erstelleKunde(firstname, lastname, passwort, address_Street, address_Zip, address_Town,
		    wv.erstelleWarenkorb());
	} else {
	    throw new AccessRestrictedException(p, "\"Kunde anlegen\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#erstelleMitatbeiter(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, data_objects.Person)
     */
    @Override
    public Mitarbeiter erstelleMitatbeiter(String firstname, String lastname, String passwort, String address_Street,
	    String address_Zip, String address_Town, Person p)
	    throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException, RemoteException {

	if (istMitarbeiter(p) || p == null) {
	    return mv.erstelleMitarbeiter(firstname, lastname, passwort, address_Street, address_Zip, address_Town);
	} else {
	    throw new AccessRestrictedException(p, "\"Mitarbeiter anlegen\"");
	}
    }

    private boolean istKunde(Person p) {

	if (p instanceof Kunde) {
	    return true;
	} else {
	    return false;
	}
    }

    private boolean istMitarbeiter(Person p) {

	if (p instanceof Mitarbeiter) {
	    return true;
	} else {
	    return false;
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#kundeSuchen(int, data_objects.Person)
     */
    @Override
    public Kunde kundeSuchen(int id, Person p) throws PersonNonexistantException, RemoteException {

	return kv.sucheKunde(id);
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#ladeDaten()
     */
    @Override
    public synchronized void ladeDaten() throws IOException, ArticleNonexistantException, PersonNonexistantException,
	    InvalidPersonDataException, RemoteException {

	av.liesDaten(dateipfad + "ARTIKEL.txt");
	kv.liesDaten(dateipfad + "KUNDEN.txt", wv);
	mv.liesDaten(dateipfad + "MITARBEITER.txt");
	ev.liesDaten(dateipfad + "EREIGNISSE.txt");
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#mitarbeiterSuchen(int, data_objects.Person)
     */
    @Override
    public Mitarbeiter mitarbeiterSuchen(int id, Person p) throws PersonNonexistantException, RemoteException {

	return mv.sucheMitarbeiter(id);
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#personLoeschen(data_objects.Person,
     * data_objects.Person)
     */
    @Override
    public void personLoeschen(Person loeschen, Person p) throws AccessRestrictedException, RemoteException {

	if (istMitarbeiter(p)) {
	    if (kv.loescheKunde((Kunde) loeschen)) {
	    } else if (mv.loescheMitarbeiter((Mitarbeiter) loeschen)) ;
	} else {
	    throw new AccessRestrictedException(p, "Kunde löschen");
	}
    }

    @Override
    public void removeShopEventListener(ShopEventListener listener) throws RemoteException {

	// TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#schreibeDaten()
     */
    @Override
    public synchronized void schreibeDaten() throws IOException {

	av.schreibeDaten(dateipfad + "ARTIKEL.txt");
	kv.schreibeDaten(dateipfad + "KUNDEN.txt");
	mv.schreibeDaten(dateipfad + "MITARBEITER.txt");
	ev.schreibeDaten(dateipfad + "EREIGNISSE.txt");
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#warenkorbAusgeben(data_objects.Person)
     */
    @Override
    public synchronized Warenkorb warenkorbAusgeben(Person p) throws AccessRestrictedException, RemoteException {

	if (istKunde(p)) {
	    return kv.gibWarenkorbVonKunde(p);
	} else {
	    throw new AccessRestrictedException(p, "\"Warenkorb anzeigen\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#warenkorbKaufen(data_objects.Person)
     */
    @Override
    public synchronized Rechnung warenkorbKaufen(Person p)
	    throws AccessRestrictedException, InvalidAmountException, RemoteException {

	if (istKunde(p)) {
	    // Warenkorb des Benutzers abfragen
	    Warenkorb wk = kv.gibWarenkorbVonKunde(p);
	    // Bestand der Artikel im Warenkorb reduzieren und Gesamtpreis
	    // errechnen
	    int gesamt = 0;
	    Map<Artikel, Integer> inhalt = wk.getArtikel();
	    for (Map.Entry<Artikel, Integer> ent : inhalt.entrySet()) {
		try {
		    av.erhoeheBestand(ent.getKey().getArtikelnummer(), -1 * ent.getValue());
		    // Ereignis erstellen
		    ev.ereignisErstellen(p, Typ.KAUF, ent.getKey(), ent.getValue());
		    // TODO Ereigniserstellung in Verwaltungen auslagern
		} catch(ArticleNonexistantException anne) {
		    // TODO
		}
		gesamt += (ent.getValue() * ent.getKey().getPreis());
	    }
	    // Warenkorb fuer Rechnung erzeugen
	    Warenkorb temp = new Warenkorb();
	    temp.copy(wk);
	    // Rechnung erzeugen
	    Rechnung re = rv.rechnungErzeugen((Kunde) p, new Date(), temp, gesamt);
	    // Warenkorb von Kunde leeren
	    wv.leereWarenkorb(wk);
	    // Rechnungsobjekt an C/GUI zurueckgeben
	    return re;
	} else {
	    throw new AccessRestrictedException(p, "\"Warenkorb bezahlen\"");
	}
    }

    /*
     * (non-Javadoc)
     * @see domain.ShopRemote#warenkorbLeeren(data_objects.Person)
     */
    @Override
    public void warenkorbLeeren(Person p) throws AccessRestrictedException, RemoteException {

	if (istKunde(p)) {
	    Warenkorb wk = kv.gibWarenkorbVonKunde(p);
	    wv.leereWarenkorb(wk);
	} else {
	    throw new AccessRestrictedException(p, "\"Warenkorb leeren\"");
	}
    }
}
