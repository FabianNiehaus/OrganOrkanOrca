package eshop.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import eshop.client.components.ArtikelSichtfenster;
import eshop.client.components.ArtikelVerwaltungsfenster;
import eshop.client.components.KundenSichtfenster;
import eshop.client.components.ManagementSichtfenster;
import eshop.client.components.ManagementVerwaltungsfenster;
import eshop.client.components.MitarbeiterSichtfenster;
import eshop.client.components.PersonenVerwaltungsfenster;
import eshop.client.components.WarenkorbSichtfenster;
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
	JButton								artikelButton		= new JButton("Artikel");
	ArtikelSichtfenster				artikelsichtfenster;
	ArtikelVerwaltungsfenster		artikelverwaltungsfenster;
	JButton								kundenButton		= new JButton("Kunden");
	KundenSichtfenster				kundensichtfenster;
	PersonenVerwaltungsfenster		kundenverwaltungsfenster;
	LoginListener						loginListener;
	JButton								logoutButton		= new JButton("Logout");
	ManagementSichtfenster			managementsichtfenster;
	ManagementVerwaltungsfenster	managementverwaltungsfenster;
	JButton								mitarbeiterButton	= new JButton("Mitarbeiter");
	MitarbeiterSichtfenster			mitarbeitersichtfenster;
	PersonenVerwaltungsfenster		mitarbeiterverwaltungsfenster;
	JPanel								moduleButtons		= new JPanel();
	ShopRemote							server;
	JButton								shopButton			= new JButton("Shop");
	JTabbedPane							tabbedPane			= new JTabbedPane();
	Person								user;
	WarenkorbSichtfenster			warenkorbsichtfenster;
	WarenkorbVerwaltungsfenster	warenkorbverwaltungsfenster;

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
	public void artikelAusWarenkorbAnzeigen(Artikel art, int anzahl) {

		warenkorbverwaltungsfenster.artikelAnzeigen(art, anzahl);
	}

	public void handleArticleChanged(Artikel art) {

		if(art != null){
			if (user instanceof Kunde) {
				try {
					if (warenkorbsichtfenster.artikelImWarenkorbPruefen(art)) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster,
								"Artikel \"" + art.getBezeichnung() + "\" wurde geändert. Bitte Warenkorb überprüfen!");
						warenkorbverwaltungsfenster.reset();
					}
				} catch (HeadlessException e) {
					JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e.getMessage());
				} catch (ArticleNonexistantException e) {
					JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, "Ein Artikel im Warenkorb wurde gelöscht!");
					warenkorbverwaltungsfenster.reset();
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
		}
		artikelsichtfenster.callTableUpdate();
	}

	public void handleEventChanged(Ereignis er) {

		if (managementsichtfenster != null) managementsichtfenster.callTableUpdate();
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

	public void initialize() {

		this.getContentPane().setLayout(new BorderLayout());
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setFont(new Font("Arial", Font.BOLD, 17));
		if (user instanceof Kunde) {
			artikelsichtfenster = new ArtikelSichtfenster(server, user, this);
			artikelverwaltungsfenster = new ArtikelVerwaltungsfenster(server, user, this);
			warenkorbsichtfenster = new WarenkorbSichtfenster(server, user, this);
			warenkorbverwaltungsfenster = new WarenkorbVerwaltungsfenster(server, user, this);
			tabbedPane.addTab("Artikel", null, new ContentPanel(artikelsichtfenster, artikelverwaltungsfenster));
			tabbedPane.addTab("Warenkorb", null, new ContentPanel(warenkorbsichtfenster, warenkorbverwaltungsfenster));
		} else if (user instanceof Mitarbeiter) {
			artikelsichtfenster = new ArtikelSichtfenster(server, user, this);
			artikelverwaltungsfenster = new ArtikelVerwaltungsfenster(server, user, this);
			kundensichtfenster = new KundenSichtfenster(server, user, this);
			kundenverwaltungsfenster = new PersonenVerwaltungsfenster(server, user, this, "Kundenverwaltung", "Kunde");
			mitarbeitersichtfenster = new MitarbeiterSichtfenster(server, user, this);
			mitarbeiterverwaltungsfenster = new PersonenVerwaltungsfenster(server, user, this, "Mitarbeiterverwaltung",
					"Mitarbeiter");
			managementsichtfenster = new ManagementSichtfenster(server, user, this);
			managementverwaltungsfenster = new ManagementVerwaltungsfenster(server, user, this);
			tabbedPane.addTab("Artikel", null, new ContentPanel(artikelsichtfenster, artikelverwaltungsfenster));
			tabbedPane.addTab("Kunden", null, new ContentPanel(kundensichtfenster, kundenverwaltungsfenster));
			tabbedPane.addTab("Mitarbeiter", null,
					new ContentPanel(mitarbeitersichtfenster, mitarbeiterverwaltungsfenster));
			tabbedPane.addTab("Management", null, new ContentPanel(managementsichtfenster, managementverwaltungsfenster));
		}
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
		this.getContentPane().add(new JPanel(), BorderLayout.EAST);
		this.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
		this.getContentPane().add(new JPanel(), BorderLayout.WEST);
		this.setPreferredSize(new Dimension(1024, 600));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void kundeAnzeigen(Kunde ku) {

		kundenverwaltungsfenster.personAnzeigen(ku);
	}

	@Override
	public void mitarbeiterAnzeigen(Mitarbeiter mi) {

		mitarbeiterverwaltungsfenster.personAnzeigen(mi);
	}
	

	@Override
	public void ereignisAnzeigen(Ereignis er) {

		managementverwaltungsfenster.ereignisAnzeigen(er);
	}

	@Override
	public void warenkorbAktualisieren() {

		warenkorbsichtfenster.callTableUpdate();
	}

	@Override
	public boolean istWarenkorbLeer() {

		return warenkorbsichtfenster.istWarenkorbLeer();
				
	}
	
	public void handleAllChanged(){
		
		if(artikelsichtfenster != null) artikelsichtfenster.callTableUpdate();
		if(warenkorbsichtfenster != null) warenkorbsichtfenster.callTableUpdate();
		if(kundensichtfenster != null) kundensichtfenster.callTableUpdate();
		if(mitarbeitersichtfenster != null) mitarbeitersichtfenster.callTableUpdate();
		if(managementsichtfenster != null) managementsichtfenster.callTableUpdate();
		
		if(artikelverwaltungsfenster != null) artikelverwaltungsfenster.reset();
		if(warenkorbverwaltungsfenster != null) warenkorbverwaltungsfenster.reset();
		if(kundenverwaltungsfenster != null) kundenverwaltungsfenster.reset();
		if(mitarbeiterverwaltungsfenster != null) mitarbeiterverwaltungsfenster.reset();
		if(managementverwaltungsfenster != null) managementverwaltungsfenster.reset();
		
	}
}