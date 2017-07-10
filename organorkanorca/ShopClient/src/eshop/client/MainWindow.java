package eshop.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import eshop.client.components.ArtikelSichtfenster;
import eshop.client.components.ArtikelVerwaltungsfenster;
import eshop.client.components.KundenSichtfenster;
import eshop.client.components.ManagementSichtfenster;
import eshop.client.components.MitarbeiterSichtfenster;
import eshop.client.components.PersonenVerwaltungsfenster;
import eshop.client.components.WarenkorbVerwaltungsfenster;
import eshop.client.util.ContentPanel;
import eshop.client.util.LoginListener;
import eshop.client.util.Sichtfenster.SichtfensterCallbacks;
import eshop.client.util.Verwaltungsfenster.VerwaltungsfensterCallbacks;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

public class MainWindow extends JFrame implements SichtfensterCallbacks, VerwaltungsfensterCallbacks {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 251175113124520728L;
	Person								user;
	ShopRemote							server;
	LoginListener						loginListener;
	JTabbedPane							tabbedPane			= new JTabbedPane();
	JPanel								moduleButtons		= new JPanel();
	JButton								artikelButton		= new JButton("Artikel");
	JButton								kundenButton		= new JButton("Kunden");
	JButton								mitarbeiterButton	= new JButton("Mitarbeiter");
	JButton								shopButton			= new JButton("Shop");
	JButton								logoutButton		= new JButton("Logout");
	KundenSichtfenster				kundensichtfenster;
	ArtikelSichtfenster				artikelsichtfenster;
	MitarbeiterSichtfenster			mitarbeitersichtfenster;
	ManagementSichtfenster			managementsichtfenster;
	WarenkorbVerwaltungsfenster	warenkorbverwaltungsfenster;
	ArtikelVerwaltungsfenster		artikelverwaltungsfenster;
	PersonenVerwaltungsfenster		kundenverwaltungsfenster;
	PersonenVerwaltungsfenster		mitarbeiterverwaltungsfenster;

	/*
	 * double prefWidth = 0; double maxWidthLeft = 0; double maxWidthRight = 0;
	 */
	public MainWindow(String titel, Person user, ShopRemote server, LoginListener loginListener,
			WindowListener windowListener) {
		super(titel);
		this.user = user;
		this.server = server;
		this.loginListener = loginListener;
		addWindowListener(windowListener);
		initialize();
	}

	@Override
	public void artikelAnzeigen(Artikel art) {

		artikelverwaltungsfenster.artikelAnzeigen(art);
	}

	@Override
	public void artikelInWarenkorb() {

		warenkorbverwaltungsfenster.warenkorbAufrufen();
	}

	public void handleArticleChanged(Artikel art) {

		if (user instanceof Kunde) {
			try {
				if (warenkorbverwaltungsfenster.artikelImWarenkorbPruefen(art)) {
					JOptionPane.showMessageDialog(warenkorbverwaltungsfenster,
							"Artikel \"" + art.getBezeichnung() + "\" wurde geändert. Bitte Warenkorb überprüfen!");
					warenkorbverwaltungsfenster.warenkorbAufrufen();
				}
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e.getMessage());
			} catch (HeadlessException e) {
				JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e.getMessage());
			} catch (ArticleNonexistantException e) {
				JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, "Ein Artikel im Warenkorb wurde gelöscht!");
				warenkorbverwaltungsfenster.warenkorbAufrufen();
			} catch (AccessRestrictedException e) {
				JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e.getMessage());
			} catch (PersonNonexistantException e) {
				JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e.getMessage());
			}
		} else if (user instanceof Mitarbeiter) {
			if (artikelverwaltungsfenster.getArtikel().getArtikelnummer() == art.getArtikelnummer()) {
				if (art.getBezeichnung().equals("deleted!")) {
					JOptionPane.showMessageDialog(artikelverwaltungsfenster, "Der ausgewählte Artikel wurde gelöscht!");
					artikelverwaltungsfenster.reset();
				} else {
					JOptionPane.showMessageDialog(artikelverwaltungsfenster, "Der ausgewählte Artikel wurde geändert!");
					artikelverwaltungsfenster.artikelAnzeigen(art);
				}
			}
		}
		artikelsichtfenster.callTableUpdate();
	}

	public void initialize() {

		this.getContentPane().setLayout(new BorderLayout());
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		if (user instanceof Kunde) {
			artikelsichtfenster = new ArtikelSichtfenster(server, user, this);
			artikelverwaltungsfenster = new ArtikelVerwaltungsfenster(server, user, this);
			warenkorbverwaltungsfenster = new WarenkorbVerwaltungsfenster(server, user, this);
			tabbedPane.addTab("Artikel", null, new ContentPanel(artikelsichtfenster, artikelverwaltungsfenster));
			tabbedPane.addTab("Warenkorb", null, warenkorbverwaltungsfenster);
		} else if (user instanceof Mitarbeiter) {
			artikelsichtfenster = new ArtikelSichtfenster(server, user, this);
			artikelverwaltungsfenster = new ArtikelVerwaltungsfenster(server, user, this);
			kundensichtfenster = new KundenSichtfenster(server, user, this);
			kundenverwaltungsfenster = new PersonenVerwaltungsfenster(server, user, this, "Kundenverwaltung", "Kunde");
			mitarbeitersichtfenster = new MitarbeiterSichtfenster(server, user, this);
			mitarbeiterverwaltungsfenster = new PersonenVerwaltungsfenster(server, user, this, "Mitarbeiterverwaltung",
					"Mitarbeiter");
			managementsichtfenster = new ManagementSichtfenster(server, user, this);
			tabbedPane.addTab("Artikel", null, new ContentPanel(artikelsichtfenster, artikelverwaltungsfenster));
			tabbedPane.addTab("Kunden", null, new ContentPanel(kundensichtfenster, kundenverwaltungsfenster));
			tabbedPane.addTab("Mitarbeiter", null,
					new ContentPanel(mitarbeitersichtfenster, mitarbeiterverwaltungsfenster));
			tabbedPane.addTab("Management", null, managementsichtfenster);
		}
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
		this.getContentPane().add(new JPanel(), BorderLayout.EAST);
		this.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
		this.getContentPane().add(new JPanel(), BorderLayout.WEST);
		this.setPreferredSize(new Dimension(1024, 800));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void mitarbeiterAnzeigen(Mitarbeiter mi) {

		mitarbeiterverwaltungsfenster.personAnzeigen(mi);
	}

	public void handleEventChanged(Ereignis er) {

		managementsichtfenster.callTableUpdate();
	}

	public void handleStaffChanged(Mitarbeiter mi) {

		if (mitarbeiterverwaltungsfenster.getPerson().getId() == mi.getId()) {
			if (mi.getFirstname().equals("deleted!")) {
				JOptionPane.showMessageDialog(mitarbeiterverwaltungsfenster, "Der ausgewählte Mitarbeiter wurde gelöscht!");
				kundenverwaltungsfenster.reset();
			} else {
				kundenverwaltungsfenster.personAnzeigen(mi);
				JOptionPane.showMessageDialog(mitarbeiterverwaltungsfenster,
						"Der ausgewählte Mitarbeiter wurde verändert!");
			}
		}
		mitarbeitersichtfenster.callTableUpdate();
	}

	public void handleUserChanged(Kunde ku) {

		if (kundenverwaltungsfenster.getPerson().getId() == ku.getId()) {
			if (ku.getFirstname().equals("deleted!")) {
				JOptionPane.showMessageDialog(kundenverwaltungsfenster, "Der ausgewählte Kunde wurde gelöscht!");
				kundenverwaltungsfenster.reset();
			} else {
				kundenverwaltungsfenster.personAnzeigen(ku);
				JOptionPane.showMessageDialog(kundenverwaltungsfenster, "Der ausgewählte Kunde wurde verändert!");
			}
		}
		kundensichtfenster.callTableUpdate();
	}

	@Override
	public void kundeAnzeigen(Kunde ku) {

		kundenverwaltungsfenster.personAnzeigen(ku);
	}
}