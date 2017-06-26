package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.LoginFailedException;
import eshop.common.exceptions.MaxIDsException;
import eshop.common.net.ShopRemote;
import eshop.server.domain.eShopCore;

import net.miginfocom.swing.MigLayout;

public class LoginWindow extends JFrame {

    JTabbedPane		  tabbedPane	     = new JTabbedPane();
    JLabel		  benutzerLabel	     = new JLabel("Benutzer");
    JLabel		  passwortLabel	     = new JLabel("Passwort");
    JTextField		  benutzerField	     = new JTextField(10);
    JPasswordField	  passwortField	     = new JPasswordField(10);
    JLabel		  headerLabel	     = new JLabel("Willkommen bei OrganOrkanOrca");
    JButton		  anmeldenButton     = new JButton("Login");
    JButton		  registrierenButton = new JButton("Registrieren");
    private ShopRemote	  server	     = null;
    private LoginListener loginListener	     = null;

    public LoginWindow(String titel, ShopRemote server, LoginListener listener) {
	super(titel);
	this.server = server;
	this.loginListener = listener;
	JPanel form = new JPanel();
	form.setLayout(new MigLayout());
	form.add(headerLabel, "span 2, wrap");
	form.add(benutzerLabel);
	form.add(benutzerField, "wrap");
	form.add(passwortLabel);
	form.add(passwortField, "wrap");
	form.add(anmeldenButton);
	form.add(registrierenButton);
	JPanel pU = new JPanel();
	JButton bU = new JButton("Als User anmelden");
	bU.addActionListener(new LoginUserUmgehenListener());
	pU.add(bU);
	JPanel pM = new JPanel();
	JButton bM = new JButton("Als Mitarbeiter anmelden");
	bM.addActionListener(new LoginMitarbeiterUmgehenListener());
	pM.add(bM);
	tabbedPane.addTab("Anmelden", null, form, "Anmeldung");
	tabbedPane.addTab("User", null, pU, "Shortcut User");
	tabbedPane.addTab("Mitarbeiter", null, pM, "Shortcut Mitarbeiter");
	tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	this.getContentPane().add(tabbedPane);
	anmeldenButton.addActionListener(new LoginButtonListener());
	registrierenButton.addActionListener(new LoginNeuerNutzerAnlegenListener());
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLocationRelativeTo(null);
	this.pack();
	this.setVisible(true);
    }

    public int benutzerIdAuslesen() throws NumberFormatException {

	return Integer.parseInt(benutzerField.getText());
    }

    public String passwortAuslesen() {

	char[] pw = passwortField.getPassword();
	passwortField.setText("");
	return new String(pw);
    }

    private class LoginButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    try {
		Person user = server.anmelden(LoginWindow.this.benutzerIdAuslesen(),
			LoginWindow.this.passwortAuslesen());
		loginListener.userLoggedIn(user);
	    } catch(NumberFormatException | LoginFailedException e1) {
		JOptionPane.showMessageDialog(LoginWindow.this, "Anmeldung fehlgeschlagen");
	    } catch(RemoteException e1) {
		JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
	    }
	}
    }

    private class LoginUserUmgehenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    try {
		Person user = server.anmelden(1001, "test");
		loginListener.userLoggedIn(user);
	    } catch(LoginFailedException | RemoteException e1) {
		e1.printStackTrace();
	    }
	}
    }

    private class LoginMitarbeiterUmgehenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    try {
		Person user = server.anmelden(9000, "test2");
		loginListener.userLoggedIn(user);
	    } catch(LoginFailedException | RemoteException e1) {
		JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
	    }
	}
    }

    private class LoginNeuerNutzerAnlegenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    JTextField firstnameField = new JTextField();
	    JTextField lastnameField = new JTextField();
	    JTextField addressStreetField = new JTextField();
	    JTextField addressTownField = new JTextField();
	    JTextField addressZipField = new JTextField();
	    JTextField passwordField = new JPasswordField();
	    Object[] message = { "Vorname:", firstnameField, "Nachname:", lastnameField, "Straße:", addressStreetField,
		    "Stadt:", addressTownField, "PLZ:", addressZipField, "Passwort:", passwordField };
	    int option = JOptionPane.showConfirmDialog(null, message, "Nutzer anlegen", JOptionPane.OK_CANCEL_OPTION);
	    if (option == JOptionPane.OK_OPTION) {
		if (!firstnameField.getText().equals("")) {
		    if (!lastnameField.getText().equals("")) {
			if (!addressStreetField.getText().equals("")) {
			    if (!addressTownField.getText().equals("")) {
				if (!addressZipField.getText().equals("") && addressZipField.getText().length() == 5) {
				    if (!passwordField.getText().equals("")) {
					try {
					    Kunde ku = server.erstelleKunde(firstnameField.getText(),
						    lastnameField.getText(), passwordField.getText(),
						    addressStreetField.getText(), addressZipField.getText(),
						    addressTownField.getText(), null);
					    JOptionPane.showMessageDialog(LoginWindow.this,
						    "Benutzer " + ku.getId() + " erfolgreich erstellt");
					} catch(MaxIDsException e1) {
					    JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
					} catch(AccessRestrictedException e1) {
					    JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
					} catch(InvalidPersonDataException e1) {
					    JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
					} catch(RemoteException e1) {
					    JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
					}
				    }
				}
			    }
			}
		    }
		} else {
		    JOptionPane.showMessageDialog(LoginWindow.this, "Benutzer erstellen fehlgeschlagen");
		}
	    } else {
		JOptionPane.showMessageDialog(LoginWindow.this, "Benutzer erstellen abgebrochen");
	    }
	}
    }
}
