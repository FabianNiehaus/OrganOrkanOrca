package eshop.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import eshop.client.components.ArtikelSichtfenster;
import eshop.client.components.ArtikelVerwaltungsfenster;
import eshop.client.components.KundenSichtfenster;
import eshop.client.components.ManagementSichtfenster;
import eshop.client.components.MitarbeiterSichtfenster;
import eshop.client.components.PersonenVerwaltungsfenster;
import eshop.client.components.WarenkorbVerwaltungsfenster;

import eshop.client.util.LoginListener;
import eshop.client.util.Sichtfenster.SichtfensterCallbacks;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;

import eshop.common.exceptions.AccessRestrictedException;

import eshop.common.net.ShopEventListener;
import eshop.common.net.ShopRemote;

import net.miginfocom.swing.MigLayout;

public class MainWindow extends JFrame implements ShopEventListener, SichtfensterCallbacks {

    /**
     * 
     */
    private static final long serialVersionUID = 251175113124520728L;
    Person			user;
    ShopRemote			server;
    LoginListener		loginListener;
    JPanel			main		  = (JPanel) this.getContentPane();
    JPanel			leftArea	  = new JPanel(new MigLayout());
    JPanel			rightArea	  = new JPanel(new MigLayout());
    JPanel			moduleButtons	  = new JPanel();
    JButton			artikelButton	  = new JButton("Artikel");
    JButton			kundenButton	  = new JButton("Kunden");
    JButton			mitarbeiterButton = new JButton("Mitarbeiter");
    JButton			shopButton	  = new JButton("Shop");
    JButton			logoutButton	  = new JButton("Logout");
    KundenSichtfenster		kundensichtfenster;
    ArtikelSichtfenster		artikelsichtfenster;
    MitarbeiterSichtfenster	mitarbeitersichtfenster;
    ManagementSichtfenster	managementsichtfenster;
    WarenkorbVerwaltungsfenster	warenkorbverwaltungsfenster;
    ArtikelVerwaltungsfenster	artikelverwaltungsfenster;
    PersonenVerwaltungsfenster	kundenverwaltungsfenster;
    PersonenVerwaltungsfenster	mitarbeiterverwaltungsfenster;
    double			prefWidth	  = 0;
    double			maxWidthLeft	  = 0;
    double			maxWidthRight	  = 0;

    public MainWindow(String titel, Person user, ShopRemote server, LoginListener loginListener) {
	super(titel);
	this.user = user;
	this.server = server;
	this.loginListener = loginListener;
	initialize();
    }

    @Override
    public void handleArticleChanged(Artikel art) {

	artikelsichtfenster.callTableUpdate();
	if (user instanceof Kunde) {
	    try {
		if (server.artikelInWarenkorb(art, user)) {
		    warenkorbverwaltungsfenster.warenkorbAufrufen();
		}
	    } catch(RemoteException | AccessRestrictedException e) {
		JOptionPane.showMessageDialog(artikelsichtfenster, e.getMessage());
	    }
	} else if (user instanceof Mitarbeiter) {
	    artikelverwaltungsfenster = new ArtikelVerwaltungsfenster();
	}
    }

    @Override
    public void handleEventChanged() {
	// TODO Auto-generated method stub

    }

    @Override
    public void handleUserChanged() {
	// TODO Auto-generated method stub

    }

    public void initialize() {

	this.setLayout(new MigLayout("", "30[]30[]30", "30[]30"));
	artikelButton.addActionListener(new MenuButtonsActionListener());
	moduleButtons.add(artikelButton);
	kundenButton.addActionListener(new MenuButtonsActionListener());
	moduleButtons.add(kundenButton);
	mitarbeiterButton.addActionListener(new MenuButtonsActionListener());
	moduleButtons.add(mitarbeiterButton);
	shopButton.addActionListener(new MenuButtonsActionListener());
	moduleButtons.add(shopButton);
	logoutButton.addActionListener(new MenuButtonsActionListener());
	moduleButtons.add(logoutButton);
	leftArea.add(moduleButtons, "wrap, dock center");
	kundensichtfenster = new KundenSichtfenster(server, user, this);
	artikelsichtfenster = new ArtikelSichtfenster(server, user, this);
	mitarbeitersichtfenster = new MitarbeiterSichtfenster(server, user, this);
	managementsichtfenster = new ManagementSichtfenster(server, user, this);
	leftArea.add(artikelsichtfenster, "dock center");
	if (user instanceof Kunde) {
	    warenkorbverwaltungsfenster = new WarenkorbVerwaltungsfenster();
	    rightArea.add(warenkorbverwaltungsfenster, "dock center");
	} else {
	    artikelverwaltungsfenster = new ArtikelVerwaltungsfenster();
	    rightArea.add(artikelverwaltungsfenster, "dock center");
	}
	main.add(leftArea);
	main.add(rightArea);
	setWindowSize();
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.pack();
	this.setVisible(true);
    }

    /**
     * 
     */
    public void setWindowSize() {

	if (artikelsichtfenster != null)
	    maxWidthLeft = Math.max(maxWidthLeft, artikelsichtfenster.getPreferredSize().getWidth() + 15);
	if (kundensichtfenster != null)
	    maxWidthLeft = Math.max(maxWidthLeft, kundensichtfenster.getPreferredSize().getWidth() + 15);
	if (mitarbeitersichtfenster != null)
	    maxWidthLeft = Math.max(maxWidthLeft, mitarbeitersichtfenster.getPreferredSize().getWidth() + 15);
	if (managementsichtfenster != null)
	    maxWidthLeft = Math.max(maxWidthLeft, managementsichtfenster.getPreferredSize().getWidth() + 15);
	if (warenkorbverwaltungsfenster != null)
	    maxWidthRight = Math.max(maxWidthRight, warenkorbverwaltungsfenster.getPreferredSize().getWidth() + 15);
	if (artikelverwaltungsfenster != null)
	    maxWidthRight = Math.max(maxWidthRight, artikelverwaltungsfenster.getPreferredSize().getWidth() + 15);
	if (kundenverwaltungsfenster != null)
	    maxWidthRight = Math.max(maxWidthRight, kundenverwaltungsfenster.getPreferredSize().getWidth() + 15);
	if (mitarbeiterverwaltungsfenster != null)
	    maxWidthRight = Math.max(maxWidthRight, mitarbeiterverwaltungsfenster.getPreferredSize().getWidth() + 15);
	leftArea.setPreferredSize(new Dimension((int) maxWidthLeft, (int) leftArea.getPreferredSize().getHeight()));
	// leftArea.setMinimumSize(leftArea.getPreferredSize());
	rightArea.setPreferredSize(new Dimension((int) maxWidthRight, (int) rightArea.getPreferredSize().getHeight()));
	// rightArea.setMinimumSize(rightArea.getPreferredSize());
	prefWidth = maxWidthLeft + maxWidthRight;
	this.setPreferredSize(new Dimension((int) prefWidth, (int) this.getPreferredSize().getHeight()));
    }

    class MenuButtonsActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent ae) {

	    if (ae.getSource().equals(artikelButton)) {
		leftArea.remove(kundensichtfenster);
		leftArea.remove(mitarbeitersichtfenster);
		leftArea.remove(managementsichtfenster);
		leftArea.add(artikelsichtfenster, BorderLayout.CENTER);
		leftArea.repaint();
		rightArea.removeAll();
		if (user instanceof Kunde) {
		    rightArea.add(warenkorbverwaltungsfenster);
		} else {
		    artikelverwaltungsfenster = new ArtikelVerwaltungsfenster();
		    rightArea.add(artikelverwaltungsfenster);
		}
		rightArea.repaint();
		MainWindow.this.pack();
	    } else if (ae.getSource().equals(kundenButton)) {
		if (user instanceof Mitarbeiter) {
		    leftArea.remove(artikelsichtfenster);
		    leftArea.remove(mitarbeitersichtfenster);
		    leftArea.remove(managementsichtfenster);
		    leftArea.add(kundensichtfenster, BorderLayout.CENTER);
		    leftArea.repaint();
		    rightArea.removeAll();
		    try {
			kundenverwaltungsfenster = new PersonenVerwaltungsfenster("Kundenverwaltung", "Kunde");
			rightArea.add(kundenverwaltungsfenster);
		    } catch(Exception e) {
			JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
		    }
		    MainWindow.this.pack();
		} else {
		    JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
		}
	    } else if (ae.getSource().equals(mitarbeiterButton)) {
		if (user instanceof Mitarbeiter) {
		    leftArea.remove(artikelsichtfenster);
		    leftArea.remove(kundensichtfenster);
		    leftArea.remove(managementsichtfenster);
		    leftArea.add(mitarbeitersichtfenster, BorderLayout.CENTER);
		    leftArea.repaint();
		    rightArea.removeAll();
		    try {
			mitarbeiterverwaltungsfenster = new PersonenVerwaltungsfenster("Mitarbeiterverwaltung",
				"Mitarbeiter");
			rightArea.add(mitarbeiterverwaltungsfenster);
		    } catch(Exception e) {
			JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
		    }
		    MainWindow.this.pack();
		} else {
		    JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
		}
	    } else if (ae.getSource().equals(shopButton)) {
		if (user instanceof Mitarbeiter) {
		    leftArea.remove(artikelsichtfenster);
		    leftArea.remove(kundensichtfenster);
		    leftArea.remove(mitarbeitersichtfenster);
		    leftArea.add(managementsichtfenster, BorderLayout.CENTER);
		    leftArea.revalidate();
		    leftArea.repaint();
		    rightArea.removeAll();
		    MainWindow.this.pack();
		} else {
		    JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
		}
	    } else if (ae.getSource().equals(logoutButton)) {
		loginListener.logout();
	    }
	}
    }

    @Override
    public void artikelInWarenkorb() {

	try {
	    warenkorbverwaltungsfenster.warenkorbAufrufen();
	} catch(RemoteException | AccessRestrictedException e) {
	    JOptionPane.showMessageDialog(this, e.getMessage());
	}
    }

    @Override
    public void artikelBearbeiten(Artikel art) {

	artikelverwaltungsfenster.artikelAnzeigen(art);
    }

    @Override
    public void kundeBearbeiten() {
	// TODO Auto-generated method stub

    }

    @Override
    public void mitarbeiterBearbeiten() {
	// TODO Auto-generated method stub

    }

    @Override
    public void alleSichtfensterErneuern() {

	try {
	    artikelsichtfenster = new ArtikelSichtfenster(server, user, this);
	    kundensichtfenster = new KundenSichtfenster(server, user, this);
	    mitarbeitersichtfenster = new MitarbeiterSichtfenster(server, user, this);
	    artikelverwaltungsfenster = new ArtikelVerwaltungsfenster();
	    kundenverwaltungsfenster = new PersonenVerwaltungsfenster("Kundenverwaltung", "Kunde");
	    mitarbeiterverwaltungsfenster = new PersonenVerwaltungsfenster("Mitarbeiterverwaltung", "Mitarbeiter");
	} catch(Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}