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
import eshop.common.data_objects.Massengutartikel;
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

	/**
     * 
     */
    private static final long serialVersionUID = -1852260814852420682L;
	private Artikelverwaltung av;
	private Kundenverwaltung kv;
	private Mitarbeiterverwaltung mv;
	private Warenkorbverwaltung wv;
	private Rechnungsverwaltung rv;
	private Ereignisverwaltung ev;
	private String dateipfad = "";

	private Vector<ShopEventListener> listeners = new Vector<ShopEventListener>();

	/**
	 * @throws PersonNonexistantException
	 * @throws ArticleNonexistantException
	 * @throws InvalidPersonDataException
	 */
	public eShopCore()
			throws IOException, ArticleNonexistantException, PersonNonexistantException, InvalidPersonDataException {
		super();
		wv = new Warenkorbverwaltung();
		av = new Artikelverwaltung();
		kv = new Kundenverwaltung(wv);
		mv = new Mitarbeiterverwaltung();
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
		} catch (IOException | ArticleNonexistantException | PersonNonexistantException
				| InvalidPersonDataException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addShopEventListener(ShopEventListener listener) throws RemoteException {

		listeners.add(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#alleArtikelAusgeben(data_objects.Person)
	 */
	@Override
	public  Vector<Artikel> alleArtikelAusgeben(Person p)
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
	 * 
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
	 * 
	 * @see domain.ShopRemote#alleKundenAusgeben(data_objects.Person)
	 */
	@Override
	public  Vector<Kunde> alleKundenAusgeben(Person p) throws AccessRestrictedException, RemoteException {

		if (istMitarbeiter(p)) {
			return kv.getKunden();
		} else {
			throw new AccessRestrictedException(p, "Kundenverwaltung");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#alleMitarbeiterAusgeben(data_objects.Person)
	 */
	@Override
	public  Vector<Mitarbeiter> alleMitarbeiterAusgeben(Person p)
			throws AccessRestrictedException, RemoteException {

		if (istMitarbeiter(p)) {
			return mv.getMitarbeiter();
		} else {
			throw new AccessRestrictedException(p, "Mitarbeiterverwaltung");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * 
	 * @see domain.ShopRemote#anmelden(int, java.lang.String)
	 */
	@Override
	public  Person anmelden(int id, String passwort) throws LoginFailedException, RemoteException {

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
	 * 
	 * @see domain.ShopRemote#artikelAusWarenkorbEntfernen(data_objects.Artikel,
	 * data_objects.Person)
	 */
	@Override
	public void artikelAusWarenkorbEntfernen(int artikelnummer, Person p) throws AccessRestrictedException, RemoteException, PersonNonexistantException, ArticleNonexistantException {

	    Kunde user = kundeSuchen(p.getId(),p);
	    
		if (istKunde(user)) {
			Warenkorb wk = kv.gibWarenkorbVonKunde(user);
			wv.loescheAusWarenkorn(wk, artikelSuchen(artikelnummer, user));
		} else {
			throw new AccessRestrictedException(user, "\"Artikel aus Warenkorb löschen\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eshop.common.net.ShopRemote#artikelInWarenkorb(eshop.common.data_objects.
	 * Artikel, eshop.common.data_objects.Person)
	 */
	@Override
	public boolean artikelInWarenkorb(Artikel art, Person p) throws RemoteException {

		if (istKunde(p))
			return ((Kunde) p).getWarenkorb().sucheArtikel(art);
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#artikelInWarenkorbAendern(data_objects.Artikel,
	 * int, data_objects.Person)
	 */
	@Override
	public  void artikelInWarenkorbAendern(int artikelnummer, int anz, Person p)
			throws ArticleStockNotSufficientException, BasketNonexistantException, AccessRestrictedException,
			InvalidAmountException, RemoteException, ArticleNonexistantException, PersonNonexistantException {

	    Kunde user = kundeSuchen(p.getId(), p);
	    
		if (istKunde(user)) {
			Warenkorb wk = kv.gibWarenkorbVonKunde(user);
			wv.aendereWarenkorb(wk, artikelSuchen(artikelnummer, p), anz);
		} else {
			throw new AccessRestrictedException(user, "\"Anzahl Artikel in Warenkorb ändern\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#artikelInWarenkorbLegen(int, int,
	 * data_objects.Person)
	 */
	@Override
	public void artikelInWarenkorbLegen(int artikelnummer, int anz, int id, Person p)
			throws ArticleNonexistantException, ArticleStockNotSufficientException, AccessRestrictedException,
			InvalidAmountException, ArticleAlreadyInBasketException, RemoteException, PersonNonexistantException {

	    Kunde user = kundeSuchen(id, p);
	    
		if (istKunde(user)) {
			Warenkorb wk = kv.gibWarenkorbVonKunde(user);
			if (wk == null) {
				wk = wv.erstelleWarenkorb();
				kv.weiseWarenkorbzu(user, wk);
			}
			Artikel art = av.sucheArtikel(artikelnummer);
			wv.legeInWarenkorb(wk, art, anz);
		} else {
			throw new AccessRestrictedException(p, "\"Artikel in Warenkorb legen\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#artikelLoeschen(data_objects.Artikel,
	 * data_objects.Person)
	 */
	@Override
	public void artikelLoeschen(int artikelnummer, Person p) throws AccessRestrictedException, RemoteException, ArticleNonexistantException {

		Artikel art = av.sucheArtikel(artikelnummer);
		
		if (istMitarbeiter(p)) {
			for (Warenkorb wk : wv.getWarenkoerbe()) {
				wk.loescheArtikel(art);
			}
			av.loeschen(art);
		} else {
			throw new AccessRestrictedException(p, "\"Artikel löschen\"");
		}
		
		art.setBezeichnung("deleted!");

		final Artikel artBack = art;

		for (ShopEventListener listener : listeners) {

			// notify every listener in a dedicated thread
			// (a notification should not block another one).
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						listener.handleArticleChanged(artBack);
						listener.handleBasketChanged(artBack);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * 
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
	 * 
	 * @see domain.ShopRemote#erhoeheArtikelBestand(int, int,
	 * data_objects.Person)
	 */
	@Override
	public  Artikel aendereArtikelBestand(Artikel art, int bestand, String operator, Person p)
			throws AccessRestrictedException, InvalidAmountException, RemoteException {

		if (istMitarbeiter(p)) {

			int tmpBestand = 0;

			if (operator.equals("+")) {
				art = av.erhoeheBestand(art, bestand);
				ev.ereignisErstellen(p, Typ.EINLAGERUNG, art, bestand);
			} else if (operator.equals("-")) {
				art = av.erhoeheBestand(art, bestand * -1);
				ev.ereignisErstellen(p, Typ.AUSLAGERUNG, art, bestand);
			} else if (operator.equals("0")) {
				tmpBestand = art.getBestand();
				art = av.erhoeheBestand(art, tmpBestand * -1);
				ev.ereignisErstellen(p, Typ.AUSLAGERUNG, art, tmpBestand);
			}

			return art;

		} else {
			throw new AccessRestrictedException(p, "\"Bestand erhöhen\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * 
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
	 * 
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
	 * 
	 * @see domain.ShopRemote#kundeSuchen(int, data_objects.Person)
	 */
	@Override
	public Kunde kundeSuchen(int id, Person p) throws PersonNonexistantException, RemoteException {

		return kv.sucheKunde(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#ladeDaten()
	 */
	@Override
	public  void ladeDaten() throws IOException, ArticleNonexistantException, PersonNonexistantException,
			InvalidPersonDataException, RemoteException {

		av.liesDaten(dateipfad + "ARTIKEL.txt");
		kv.liesDaten(dateipfad + "KUNDEN.txt", wv);
		mv.liesDaten(dateipfad + "MITARBEITER.txt");
		ev.liesDaten(dateipfad + "EREIGNISSE.txt");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#mitarbeiterSuchen(int, data_objects.Person)
	 */
	@Override
	public Mitarbeiter mitarbeiterSuchen(int id, Person p) throws PersonNonexistantException, RemoteException {

		return mv.sucheMitarbeiter(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#personLoeschen(data_objects.Person,
	 * data_objects.Person)
	 */
	@Override
	public void personLoeschen(Person loeschen, Person p) throws AccessRestrictedException, RemoteException, InvalidPersonDataException, PersonNonexistantException {

		if (istMitarbeiter(p)) {
			if (loeschen.getId() >= 1000 && loeschen.getId() < 9000) {
				kv.loescheKunde(kv.sucheKunde(loeschen.getId()));
				loeschen.setFirstname("deleted!");
				
				final Kunde kundeBack = (Kunde) loeschen;

				for (ShopEventListener listener : listeners) {

					// notify every listener in a dedicated thread
					// (a notification should not block another one).
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								listener.handleUserChanged(kundeBack);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
				
			} else if (loeschen.getId() >= 9000 && loeschen.getId() < 10000) {
				mv.loescheMitarbeiter(mv.sucheMitarbeiter(loeschen.getId()));
				loeschen.setFirstname("deleted!");
				
				final Mitarbeiter mitarbeiterBack = (Mitarbeiter) loeschen;

				for (ShopEventListener listener : listeners) {

					// notify every listener in a dedicated thread
					// (a notification should not block another one).
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								listener.handleStaffChanged(mitarbeiterBack);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
				
			} else {
				throw new InvalidPersonDataException(loeschen.getId(), "");
			}
		} else {
			throw new AccessRestrictedException(p, "Kunde löschen");
		}

	}

	@Override
	public void removeShopEventListener(ShopEventListener listener) throws RemoteException {

		listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#schreibeDaten()
	 */
	@Override
	public  void schreibeDaten() throws IOException {

		av.schreibeDaten(dateipfad + "ARTIKEL.txt");
		kv.schreibeDaten(dateipfad + "KUNDEN.txt");
		mv.schreibeDaten(dateipfad + "MITARBEITER.txt");
		ev.schreibeDaten(dateipfad + "EREIGNISSE.txt");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#warenkorbAusgeben(data_objects.Person)
	 */
	@Override
	public  Warenkorb warenkorbAusgeben(int id, Person p) throws AccessRestrictedException, RemoteException, PersonNonexistantException {

	    Person user = kundeSuchen(id, p);
	    
		if (istKunde(user)) {
			return kv.gibWarenkorbVonKunde(user);
		} else {
			throw new AccessRestrictedException(p, "\"Warenkorb anzeigen\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#warenkorbKaufen(data_objects.Person)
	 */
	@Override
	public  Rechnung warenkorbKaufen(Person p)
			throws AccessRestrictedException, InvalidAmountException, RemoteException, PersonNonexistantException {

	    Kunde user = kundeSuchen(p.getId(),p);
	    
		if (istKunde(user)) {
			// Warenkorb des Benutzers abfragen
			Warenkorb wk = kv.gibWarenkorbVonKunde(user);
			// Bestand der Artikel im Warenkorb reduzieren und Gesamtpreis
			// errechnen
			int gesamt = 0;
			Map<Artikel, Integer> inhalt = wk.getArtikel();
			for (Map.Entry<Artikel, Integer> ent : inhalt.entrySet()) {

				av.erhoeheBestand(ent.getKey(), -1 * ent.getValue());

				ev.ereignisErstellen(user, Typ.KAUF, ent.getKey(), ent.getValue());

				gesamt += (ent.getValue() * ent.getKey().getPreis());
			}
			// Warenkorb fuer Rechnung erzeugen
			Warenkorb temp = new Warenkorb();
			temp.copy(wk);
			// Rechnung erzeugen
			Rechnung re = rv.rechnungErzeugen(user, new Date(), temp, gesamt);
			// Warenkorb von Kunde leeren
			wv.leereWarenkorb(wk);
			// Rechnungsobjekt an C/GUI zurueckgeben
			return re;
		} else {
			throw new AccessRestrictedException(user, "\"Warenkorb bezahlen\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see domain.ShopRemote#warenkorbLeeren(data_objects.Person)
	 */
	@Override
	public void warenkorbLeeren(Person p) throws AccessRestrictedException, RemoteException, PersonNonexistantException {

	   Kunde user = kundeSuchen(p.getId(),p);
	    
		if (istKunde(p)) {
			Warenkorb wk = kv.gibWarenkorbVonKunde(user);
			wv.leereWarenkorb(wk);
		} else {
			throw new AccessRestrictedException(user, "\"Warenkorb leeren\"");
		}
	}

	@Override
	public Artikel artikelAendern(int artikelnummer, Person p, String bezeichnung, int bestand, String operator,
			double preis, int packungsgroesse)
			throws RemoteException, AccessRestrictedException, InvalidAmountException, ArticleNonexistantException {

		Artikel art = av.sucheArtikel(artikelnummer);

		art = av.aendereBezeichnung(art, bezeichnung);

		art = av.aenderePreis(art, preis);

		if (packungsgroesse > 1 && packungsgroesse != ((Massengutartikel) art).getPackungsgroesse()) {
			int tmpBestand = art.getBestand();
			artikelLoeschen(art.getArtikelnummer(), p);
			art = erstelleArtikel(bezeichnung, tmpBestand, preis, packungsgroesse, p);
		}

		aendereArtikelBestand(art, bestand, operator, p);

		final Artikel artBack = art;

		for (ShopEventListener listener : listeners) {

			// notify every listener in a dedicated thread
			// (a notification should not block another one).
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						listener.handleArticleChanged(artBack);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

		return art;

	}

	@Override
	public Person personAendern(String typ, Person p, String firstname, String lastname, int id, String passwort,
			String address_Street, String address_Zip, String address_Town)
			throws RemoteException, AccessRestrictedException, InvalidPersonDataException, PersonNonexistantException {

		Person person = null;

		if (typ.equals("kunde")) {
			Kunde ku = kv.sucheKunde(id);
			ku.setFirstname(firstname);
			ku.setLastname(lastname);
			ku.setAddress_Street(address_Street);
			ku.setAddress_Zip(address_Zip);
			ku.setAddress_Town(address_Town);
			if (!passwort.equals("") && !passwort.equals("*********")) {
				ku.setPasswort(passwort);
			}

			final Kunde kundeBack = ku;

			for (ShopEventListener listener : listeners) {

				// notify every listener in a dedicated thread
				// (a notification should not block another one).
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							listener.handleUserChanged(kundeBack);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

			person = ku;

		} else if (typ.equals("mitarbeiter")) {
			Mitarbeiter mi = mv.sucheMitarbeiter(id);
			mi.setFirstname(firstname);
			mi.setLastname(lastname);
			mi.setAddress_Street(address_Street);
			mi.setAddress_Zip(address_Zip);
			mi.setAddress_Town(address_Town);
			if (!passwort.equals("") && !passwort.equals("*********")) {
				mi.setPasswort(passwort);
			}

			final Mitarbeiter mitarbeiterBack = mi;

			for (ShopEventListener listener : listeners) {

				// notify every listener in a dedicated thread
				// (a notification should not block another one).
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							listener.handleStaffChanged(mitarbeiterBack);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

			person = mi;
		}

		return person;

	}
}
