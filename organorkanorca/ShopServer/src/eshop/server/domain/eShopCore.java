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

import javax.swing.JOptionPane;

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

// TODO: Auto-generated Javadoc
/**
 * The Class eShopCore.
 */
public class eShopCore extends UnicastRemoteObject implements ShopRemote {

	/** The Constant serialVersionUID. */
	private static final long				serialVersionUID		= -1852260814852420682L;
	
	/** The Artikel action key. */
	private Object								artActionKey			= new Object();
	
	/** The Artikelverwaltung. */
	private Artikelverwaltung				av;
	
	/** The dateipfad. */
	private String								dateipfad				= "";
	
	/** The Ereignisverwaltung. */
	private Ereignisverwaltung				ev;
	
	/** The Kunde action key. */
	private Object								kuActionKey				= new Object();
	
	/** The Kundenverwaltung. */
	private Kundenverwaltung				kv;
	
	/** The Mitarbeiter action key. */
	private Object								miActionKey				= new Object();
	
	/** The Mitarbeiterverwaltung. */
	private Mitarbeiterverwaltung			mv;
	
	/** The Rechnungsverwaltung. */
	private Rechnungsverwaltung			rv;
	
	/** The shop event listeners. */
	private Vector<ShopEventListener>	shopEventListeners	= new Vector<ShopEventListener>();
	
	/** The Warenkorbverwaltung. */
	private Warenkorbverwaltung			wv;

	/**
	 * Instantiates a new e shop core.
	 *
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws ArticleNonexistantException
	 *            the article nonexistant exception
	 * @throws PersonNonexistantException
	 *            the person nonexistant exception
	 * @throws InvalidPersonDataException
	 *            the invalid person data exception
	 */
	public eShopCore()
			throws IOException, ArticleNonexistantException, PersonNonexistantException, InvalidPersonDataException {
		super();
		ladeDaten();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *           the arguments
	 */
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
		} catch (IOException | ArticleNonexistantException | PersonNonexistantException | InvalidPersonDataException e) {
			JOptionPane.showMessageDialog(null,
					"Server konnte nicht gestartet werden!\nWahrscheinlich läuft bereits eine Instanz des Servers!",
					"Fehler", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see eshop.common.net.ShopRemote#addShopEventListener(eshop.common.net.
	 * ShopEventListener)
	 */
	@Override
	public void addShopEventListener(ShopEventListener listener) throws RemoteException {

		shopEventListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#alleArtikelAusgeben(data_objects.Person)
	 */
	@Override
	public Vector<Artikel> alleArtikelAusgeben(Person person) throws AccessRestrictedException, RemoteException {

		synchronized (artActionKey) {
			if (istKunde(person) || istMitarbeiter(person)) {
				return av.getArtikel();
			} else {
				throw new AccessRestrictedException(person, "\"Alle Artikel ausgeben\"");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#alleEreignisseAusgeben(data_objects.Person)
	 */
	@Override
	public Vector<Ereignis> alleEreignisseAusgeben(Person person) throws AccessRestrictedException, RemoteException {

		if (istMitarbeiter(person)) {
			return ev.getEreignisse();
		} else {
			throw new AccessRestrictedException(person, "Kunde löschen");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#alleKundenAusgeben(data_objects.Person)
	 */
	@Override
	public Vector<Kunde> alleKundenAusgeben(Person person) throws AccessRestrictedException, RemoteException {

		if (istMitarbeiter(person)) {
			return kv.getKunden();
		} else {
			throw new AccessRestrictedException(person, "Kundenverwaltung");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#alleMitarbeiterAusgeben(data_objects.Person)
	 */
	@Override
	public Vector<Mitarbeiter> alleMitarbeiterAusgeben(Person person) throws AccessRestrictedException, RemoteException {

		if (istMitarbeiter(person)) {
			return mv.getMitarbeiter();
		} else {
			throw new AccessRestrictedException(person, "Mitarbeiterverwaltung");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#alleWarenkoerbeAusgeben(data_objects.Person)
	 */
	@Override
	public void alleWarenkoerbeAusgeben(Person person) throws AccessRestrictedException, RemoteException {

		if (istMitarbeiter(person)) {
			for (Warenkorb w : wv.getWarenkoerbe()) {
				w.toString();
			}
		} else {
			throw new AccessRestrictedException(person, "\"Alle Warenkörbe ausgeben\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#anmelden(int, java.lang.String)
	 */
	@Override
	public Person anmelden(int id, String passwort) throws LoginFailedException, RemoteException {

		if (id >= 1000 && id < 9000) {
			synchronized (kuActionKey) {
				return kv.anmelden(id, passwort);
			}
		} else if (id >= 9000 && id < 10000) {
			synchronized (miActionKey) {
				return mv.anmelden(id, passwort);
			}
		} else {
			throw new LoginFailedException();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see eshop.common.net.ShopRemote#artikelAendern(int,
	 * eshop.common.data_objects.Person, java.lang.String, int, java.lang.String,
	 * double, int)
	 */
	@Override
	public Artikel artikelAendern(int artikelnummer, Person person, String bezeichnung, int bestand, String operator,
			double preis, int packungsgroesse, String artikelinfo)
			throws RemoteException, AccessRestrictedException, InvalidAmountException, ArticleNonexistantException {

		synchronized (artActionKey) {
			Artikel art = av.sucheArtikel(artikelnummer);
			art = av.aendereBezeichnung(art, bezeichnung);
			art = av.aenderePreis(art, preis);
			if (packungsgroesse > 1) {
				if (art.getBestand() % packungsgroesse == 0) {
					if (art instanceof Massengutartikel) {
						((Massengutartikel) art).setPackungsgroesse(packungsgroesse);
					} else {
						int tmpBestand = art.getBestand();
						artikelLoeschen(art.getArtikelnummer(), person);
						art = erstelleArtikel(bezeichnung, tmpBestand, preis, packungsgroesse, person, artikelinfo,
								art.getPicture());
					}
				} else {
					throw new InvalidAmountException(art.getBestand());
				}
			} else if (packungsgroesse == 1) {
				if (art instanceof Massengutartikel) {
					int tmpBestand = art.getBestand();
					artikelLoeschen(art.getArtikelnummer(), person);
					art = erstelleArtikel(bezeichnung, tmpBestand, preis, 1, person, artikelinfo, art.getPicture());
				}
			}
			artikelBestandAendern(art, bestand, operator, person);
			final Artikel artBack = art;
			for (ShopEventListener listener : shopEventListeners) {
				// notify every listener in a dedicated thread
				// (a notification should not block another one).
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							listener.handleArticleChanged(artBack);
						} catch (RemoteException e) {
							JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
						} catch (NullPointerException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
			}
			return art;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#artikelAusWarenkorbEntfernen(data_objects.Artikel,
	 * data_objects.Person)
	 */
	@Override
	public void artikelAusWarenkorbEntfernen(int artikelnummer, Person person)
			throws AccessRestrictedException, RemoteException, PersonNonexistantException, ArticleNonexistantException {

		Kunde user = kundeSuchen(person.getId(), person);
		if (istKunde(user)) {
			Warenkorb wk = kv.gibWarenkorbVonKunde(user);
			wv.loescheAusWarenkorn(wk, av.sucheArtikel(artikelnummer));
		} else {
			throw new AccessRestrictedException(user, "\"Artikel aus Warenkorb löschen\"");
		}
	}

	/**
	 * Artikel bestand aendern.
	 *
	 * @param art
	 *           the artikel
	 * @param bestand
	 *           the bestand
	 * @param operator
	 *           the operator
	 * @param user
	 *           the user
	 * @return the artikel
	 * @throws AccessRestrictedException
	 *            the access restricted exception
	 * @throws InvalidAmountException
	 *            the invalid amount exception
	 * @throws RemoteException
	 *            the remote exception
	 */
	private Artikel artikelBestandAendern(Artikel art, int bestand, String operator, Person user)
			throws AccessRestrictedException, InvalidAmountException, RemoteException {

		if (istMitarbeiter(user)) {
			int tmpBestand = 0;
			if (operator.equals("+")) {
				art = av.erhoeheBestand(art, bestand);
				ev.ereignisErstellen(user, Typ.EINLAGERUNG, art, bestand);
			} else if (operator.equals("-")) {
				art = av.erhoeheBestand(art, bestand * -1);
				ev.ereignisErstellen(user, Typ.AUSLAGERUNG, art, bestand);
			} else if (operator.equals("0")) {
				tmpBestand = art.getBestand();
				art = av.erhoeheBestand(art, tmpBestand * -1);
				ev.ereignisErstellen(user, Typ.AUSLAGERUNG, art, tmpBestand);
			}
			for (ShopEventListener listener : shopEventListeners) {
				// notify every listener in a dedicated thread
				// (a notification should not block another one).
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							listener.handleEventChanged(null);
						} catch (RemoteException e) {
							JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
						} catch (NullPointerException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
			}
			return art;
		} else {
			throw new AccessRestrictedException(user, "\"Bestand erhöhen\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#artikelInWarenkorbAendern(data_objects.Artikel,
	 * int, data_objects.Person)
	 */
	@Override
	public void artikelInWarenkorbAendern(int artikelnummer, int anz, Person person)
			throws ArticleStockNotSufficientException, BasketNonexistantException, AccessRestrictedException,
			InvalidAmountException, RemoteException, ArticleNonexistantException, PersonNonexistantException {

		synchronized (artActionKey) {
			Kunde user = kundeSuchen(person.getId(), person);
			if (istKunde(user)) {
				Warenkorb wk = kv.gibWarenkorbVonKunde(user);
				wv.aendereWarenkorb(wk, av.sucheArtikel(artikelnummer), anz);
			} else {
				throw new AccessRestrictedException(user, "\"Anzahl Artikel in Warenkorb ändern\"");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#artikelInWarenkorbLegen(int, int,
	 * data_objects.Person)
	 */
	@Override
	public void artikelInWarenkorbLegen(int artikelnummer, int anz, int id, Person person)
			throws ArticleNonexistantException, ArticleStockNotSufficientException, AccessRestrictedException,
			InvalidAmountException, ArticleAlreadyInBasketException, RemoteException, PersonNonexistantException {

		synchronized (artActionKey) {
			Kunde user = kundeSuchen(id, person);
			if (istKunde(user)) {
				Warenkorb wk = kv.gibWarenkorbVonKunde(user);
				if (wk == null) {
					wk = wv.erstelleWarenkorb();
					kv.weiseWarenkorbzu(user, wk);
				}
				Artikel art = av.sucheArtikel(artikelnummer);
				wv.legeInWarenkorb(wk, art, anz);
			} else {
				throw new AccessRestrictedException(person, "\"Artikel in Warenkorb legen\"");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#artikelLoeschen(data_objects.Artikel,
	 * data_objects.Person)
	 */
	@Override
	public void artikelLoeschen(int artikelnummer, Person person)
			throws AccessRestrictedException, RemoteException, ArticleNonexistantException {

		synchronized (artActionKey) {
			Artikel art = av.sucheArtikel(artikelnummer);
			if (istMitarbeiter(person)) {
				for (Warenkorb wk : wv.getWarenkoerbe()) {
					wk.loescheArtikel(art);
				}
				av.loeschen(art);
				ev.ereignisErstellen(person, Typ.LOESCHEN, art, 0);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				throw new AccessRestrictedException(person, "\"Artikel löschen\"");
			}
			art.setBezeichnung("deleted!");
			final Artikel artBack = art;
			for (ShopEventListener listener : shopEventListeners) {
				// notify every listener in a dedicated thread
				// (a notification should not block another one).
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							listener.handleArticleChanged(artBack);
							listener.handleEventChanged(null);
						} catch (RemoteException e) {
							JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
						} catch (NullPointerException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#artikelSuchen(int, data_objects.Person)
	 */
	@Override
	public Artikel artikelSuchen(int artikelnummer, Person person)
			throws ArticleNonexistantException, AccessRestrictedException, RemoteException {

		synchronized (person) {
			if (istMitarbeiter(person) || istKunde(person)) {
				return av.sucheArtikel(artikelnummer);
			} else {
				throw new AccessRestrictedException(person, "\"Artikel suchen (Artikelnummer)\"");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#artikelSuchen(java.lang.String,
	 * data_objects.Person)
	 */
	@Override
	public Vector<Artikel> artikelSuchen(String bezeichnung, Person person)
			throws ArticleNonexistantException, AccessRestrictedException, RemoteException {

		synchronized (artActionKey) {
			if (istMitarbeiter(person) || istKunde(person)) {
				return av.sucheArtikel(bezeichnung);
			} else {
				throw new AccessRestrictedException(person, "\"Artikel suchen (Bezeichnung)\"");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#artikelSuchen(int, data_objects.Person)
	 */
	@Override
	public Ereignis ereignisSuchen(int ereignisID, Person person)
			throws ArticleNonexistantException, AccessRestrictedException, RemoteException {

		synchronized (person) {
			if (istMitarbeiter(person) || istKunde(person)) {
				return ev.sucheEreignis(ereignisID);
			} else {
				throw new AccessRestrictedException(person, "\"Artikel suchen (Artikelnummer)\"");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#erstelleArtikel(java.lang.String, int, double, int,
	 * data_objects.Person)
	 */
	@Override
	public Artikel erstelleArtikel(String bezeichnung, int bestand, double preis, int packungsgroesse, Person person,
			String artikelinfo, String picture) throws AccessRestrictedException, InvalidAmountException, RemoteException {

		synchronized (artActionKey) {
			if (istMitarbeiter(person)) {
				Artikel art = av.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse, artikelinfo, picture);
				// Ereignis erzeugen
				ev.ereignisErstellen(person, Typ.NEU, art, bestand);
				final Artikel artikelBack = art;
				for (ShopEventListener listener : shopEventListeners) {
					// notify every listener in a dedicated thread
					// (a notification should not block another one).
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								listener.handleArticleChanged(artikelBack);
								listener.handleEventChanged(null);
							} catch (RemoteException e) {
								JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
							} catch (NullPointerException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					t.start();
				}
				return art;
			} else {
				throw new AccessRestrictedException(person, "\"Artikel anlegen\"");
			}
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
			String address_Zip, String address_Town, Person person)
			throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException, RemoteException {

		synchronized (kuActionKey) {
			if (istMitarbeiter(person) || person == null) {
				Kunde ku = kv.erstelleKunde(firstname, lastname, passwort, address_Street, address_Zip, address_Town,
						wv.erstelleWarenkorb());
				final Kunde kundeBack = ku;
				for (ShopEventListener listener : shopEventListeners) {
					// notify every listener in a dedicated thread
					// (a notification should not block another one).
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								listener.handleUserChanged(kundeBack);
							} catch (RemoteException e) {
								JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
							} catch (NullPointerException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					t.start();
				}
				return ku;
			} else {
				throw new AccessRestrictedException(person, "\"Kunde anlegen\"");
			}
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
			String address_Zip, String address_Town, Person person)
			throws MaxIDsException, AccessRestrictedException, InvalidPersonDataException, RemoteException {

		synchronized (miActionKey) {
			if (istMitarbeiter(person) || person == null) {
				Mitarbeiter mi = mv.erstelleMitarbeiter(firstname, lastname, passwort, address_Street, address_Zip,
						address_Town);
				final Mitarbeiter mitarbeiterBack = mi;
				for (ShopEventListener listener : shopEventListeners) {
					// notify every listener in a dedicated thread
					// (a notification should not block another one).
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								listener.handleStaffChanged(mitarbeiterBack);
							} catch (RemoteException e) {
								JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
							} catch (NullPointerException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					t.start();
				}
				return mi;
			} else {
				throw new AccessRestrictedException(person, "\"Mitarbeiter anlegen\"");
			}
		}
	}

	/**
	 * Checks if is t kunde.
	 *
	 * @param person
	 *           the person
	 * @return true, if is t kunde
	 */
	private boolean istKunde(Person person) {

		if (person instanceof Kunde) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if is t mitarbeiter.
	 *
	 * @param person
	 *           the person
	 * @return true, if is t mitarbeiter
	 */
	private boolean istMitarbeiter(Person person) {

		if (person instanceof Mitarbeiter) {
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
	public Kunde kundeSuchen(int id, Person person) throws PersonNonexistantException, RemoteException {

		synchronized (kuActionKey) {
			return kv.sucheKunde(id);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#ladeDaten()
	 */
	@Override
	public void ladeDaten() throws IOException, ArticleNonexistantException, PersonNonexistantException,
			InvalidPersonDataException, RemoteException {

		av = new Artikelverwaltung();
		wv = new Warenkorbverwaltung();
		kv = new Kundenverwaltung(wv);
		mv = new Mitarbeiterverwaltung();
		rv = new Rechnungsverwaltung();
		ev = new Ereignisverwaltung();
		av.liesDaten(dateipfad + "ARTIKEL.txt");
		kv.liesDaten(dateipfad + "KUNDEN.txt", wv);
		mv.liesDaten(dateipfad + "MITARBEITER.txt");
		ev.liesDaten(dateipfad + "EREIGNISSE.txt");
		for (ShopEventListener listener : shopEventListeners) {
			// notify every listener in a dedicated thread
			// (a notification should not block another one).
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						listener.handleAllChanged();
					} catch (RemoteException e) {
						JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
					} catch (NullPointerException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#mitarbeiterSuchen(int, data_objects.Person)
	 */
	@Override
	public Mitarbeiter mitarbeiterSuchen(int id, Person person) throws PersonNonexistantException, RemoteException {

		synchronized (miActionKey) {
			return mv.sucheMitarbeiter(id);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see eshop.common.net.ShopRemote#personAendern(java.lang.String,
	 * eshop.common.data_objects.Person, java.lang.String, java.lang.String, int,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Person personAendern(String typ, Person person, String firstname, String lastname, int id, String passwort,
			String address_Street, String address_Zip, String address_Town)
			throws RemoteException, AccessRestrictedException, InvalidPersonDataException, PersonNonexistantException {

		if (typ.equals("kunde")) {
			synchronized (kuActionKey) {
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
				for (ShopEventListener listener : shopEventListeners) {
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								listener.handleUserChanged(kundeBack);
							} catch (RemoteException e) {
								JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
							} catch (NullPointerException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					t.start();
				}
				person = ku;
			}
		} else if (typ.equals("mitarbeiter")) {
			synchronized (miActionKey) {
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
				for (ShopEventListener listener : shopEventListeners) {
					// notify every listener in a dedicated thread
					// (a notification should not block another one).
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								listener.handleStaffChanged(mitarbeiterBack);
							} catch (RemoteException e) {
								JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
							} catch (NullPointerException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					t.start();
				}
				person = mi;
			}
		}
		return person;
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#personLoeschen(data_objects.Person,
	 * data_objects.Person)
	 */
	@Override
	public void personLoeschen(Person loeschen, Person person)
			throws AccessRestrictedException, RemoteException, InvalidPersonDataException, PersonNonexistantException {

		if (istMitarbeiter(person)) {
			if (loeschen.getId() >= 1000 && loeschen.getId() < 9000) {
				synchronized (kuActionKey) {
					kv.loescheKunde(kv.sucheKunde(loeschen.getId()));
					loeschen.setFirstname("deleted!");
					final Kunde kundeBack = (Kunde) loeschen;
					for (ShopEventListener listener : shopEventListeners) {
						// notify every listener in a dedicated thread
						// (a notification should not block another one).
						Thread t = new Thread(new Runnable() {

							@Override
							public void run() {

								try {
									listener.handleUserChanged(kundeBack);
								} catch (RemoteException e) {
									JOptionPane.showMessageDialog(null,
											"Übermittlungsfehler bei Listener " + listener.toString());
								} catch (NullPointerException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
						t.start();
					}
				}
			} else if (loeschen.getId() >= 9000 && loeschen.getId() < 10000) {
				synchronized (miActionKey) {
					mv.loescheMitarbeiter(mv.sucheMitarbeiter(loeschen.getId()));
					loeschen.setFirstname("deleted!");
					final Mitarbeiter mitarbeiterBack = (Mitarbeiter) loeschen;
					for (ShopEventListener listener : shopEventListeners) {
						// notify every listener in a dedicated thread
						// (a notification should not block another one).
						Thread t = new Thread(new Runnable() {

							@Override
							public void run() {

								try {
									listener.handleStaffChanged(mitarbeiterBack);
								} catch (RemoteException e) {
									JOptionPane.showMessageDialog(null,
											"Übermittlungsfehler bei Listener " + listener.toString());
								} catch (NullPointerException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
						t.start();
					}
				}
			} else {
				throw new InvalidPersonDataException(loeschen.getId(), "");
			}
		} else {
			throw new AccessRestrictedException(person, "Kunde löschen");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see eshop.common.net.ShopRemote#removeShopEventListener(eshop.common.net.
	 * ShopEventListener)
	 */
	@Override
	public void removeShopEventListener(ShopEventListener listener) throws RemoteException {

		shopEventListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#schreibeDaten()
	 */
	@Override
	public void schreibeDaten() throws IOException {

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
	public Warenkorb warenkorbAusgeben(int id, Person person)
			throws AccessRestrictedException, RemoteException, PersonNonexistantException {

		synchronized (artActionKey) {
			Person user = kundeSuchen(id, person);
			if (istKunde(user)) {
				return kv.gibWarenkorbVonKunde(user);
			} else {
				throw new AccessRestrictedException(person, "\"Warenkorb anzeigen\"");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#warenkorbKaufen(data_objects.Person)
	 */
	@Override
	public Rechnung warenkorbKaufen(Person person)
			throws AccessRestrictedException, InvalidAmountException, RemoteException, PersonNonexistantException {

		synchronized (artActionKey) {
			Kunde user = kundeSuchen(person.getId(), person);
			if (istKunde(user)) {
				// Warenkorb des Benutzers abfragen
				Warenkorb wk = kv.gibWarenkorbVonKunde(user);
				// Bestand der Artikel im Warenkorb reduzieren und Gesamtpreis
				// errechnen
				double gesamt = 0;
				Map<Artikel, Integer> inhalt = wk.getArtikel();
				for (Map.Entry<Artikel, Integer> ent : inhalt.entrySet()) {
					av.erhoeheBestand(ent.getKey(), -1 * ent.getValue());
					ev.ereignisErstellen(user, Typ.KAUF, ent.getKey(), ent.getValue());
					gesamt += ent.getValue() * ent.getKey().getPreis();
				}
				// Warenkorb fuer Rechnung erzeugen
				Warenkorb temp = new Warenkorb();
				temp.copy(wk);
				// Rechnung erzeugen
				Rechnung re = rv.rechnungErzeugen(user, new Date(), temp, gesamt);
				// Warenkorb von Kunde leeren
				wv.leereWarenkorb(wk);
				for (ShopEventListener listener : shopEventListeners) {
					// notify every listener in a dedicated thread
					// (a notification should not block another one).
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								listener.handleArticleChanged(null);
								listener.handleEventChanged(null);
							} catch (RemoteException e) {
								JOptionPane.showMessageDialog(null, "Übermittlungsfehler bei Listener " + listener.toString());
							} catch (NullPointerException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					t.start();
				}
				return re;
			} else {
				throw new AccessRestrictedException(user, "\"Warenkorb bezahlen\"");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see domain.ShopRemote#warenkorbLeeren(data_objects.Person)
	 */
	@Override
	public void warenkorbLeeren(Person person)
			throws AccessRestrictedException, RemoteException, PersonNonexistantException {

		Kunde user = kundeSuchen(person.getId(), person);
		if (istKunde(person)) {
			Warenkorb wk = kv.gibWarenkorbVonKunde(user);
			wv.leereWarenkorb(wk);
		} else {
			throw new AccessRestrictedException(user, "\"Warenkorb leeren\"");
		}
	}
}
