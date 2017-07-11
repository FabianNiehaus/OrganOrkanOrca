package eshop.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import eshop.client.util.LoginListener;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.LoginFailedException;
import eshop.common.exceptions.MaxIDsException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginWindow.
 */
public class LoginWindow extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID		= -5042186875268113945L;
	
	/** The anmelden button. */
	JButton							anmeldenButton			= new JButton("Login");
	
	/** The benutzer field. */
	JTextField						benutzerField			= new JTextField(10);
	
	/** The benutzer label. */
	JLabel							benutzerLabel			= new JLabel("Benutzer");
	
	/** The header label. */
	JLabel							headerLabel				= new JLabel("Willkommen bei OrganOrkanOrca");
	
	/** The login listener. */
	private LoginListener		loginListener			= null;
	
	/** The passwort field. */
	JPasswordField					passwortField			= new JPasswordField(10);
	
	/** The passwort label. */
	JLabel							passwortLabel			= new JLabel("Passwort");
	
	/** The registrieren button. */
	JButton							registrierenButton	= new JButton("Registrieren");
	
	/** The server. */
	private ShopRemote			server					= null;
	
	/** The tabbed pane. */
	JTabbedPane						tabbedPane				= new JTabbedPane();

	/**
	 * Instantiates a new login window.
	 *
	 * @param titel
	 *           the titel
	 * @param server
	 *           the server
	 * @param listener
	 *           the listener
	 * @param windowListener
	 *           the window listener
	 */
	public LoginWindow(String titel, ShopRemote server, LoginListener listener, WindowListener windowListener) {
		super(titel);
		this.server = server;
		this.loginListener = listener;
		addWindowListener(windowListener);
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
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Benutzer id auslesen.
	 *
	 * @return the int
	 * @throws NumberFormatException
	 *            the number format exception
	 */
	public int benutzerIdAuslesen() throws NumberFormatException {

		return Integer.parseInt(benutzerField.getText());
	}

	/**
	 * Passwort auslesen.
	 *
	 * @return the string
	 */
	public String passwortAuslesen() {

		char[] pw = passwortField.getPassword();
		passwortField.setText("");
		return new String(pw);
	}

	/**
	 * The listener interface for receiving loginButton events. The class that is
	 * interested in processing a loginButton event implements this interface,
	 * and the object created with that class is registered with a component
	 * using the component's <code>addLoginButtonListener<code> method. When the
	 * loginButton event occurs, that object's appropriate method is invoked.
	 *
	 * @see LoginButtonEvent
	 */
	private class LoginButtonListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				Person user = server.anmelden(LoginWindow.this.benutzerIdAuslesen(), LoginWindow.this.passwortAuslesen());
				loginListener.userLoggedIn(user);
			} catch (NumberFormatException | LoginFailedException e1) {
				JOptionPane.showMessageDialog(LoginWindow.this, "Anmeldung fehlgeschlagen");
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
			}
		}
	}

	/**
	 * The listener interface for receiving loginMitarbeiterUmgehen events. The
	 * class that is interested in processing a loginMitarbeiterUmgehen event
	 * implements this interface, and the object created with that class is
	 * registered with a component using the component's
	 * <code>addLoginMitarbeiterUmgehenListener<code> method. When the
	 * loginMitarbeiterUmgehen event occurs, that object's appropriate method is
	 * invoked.
	 *
	 * @see LoginMitarbeiterUmgehenEvent
	 */
	private class LoginMitarbeiterUmgehenListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				Person user = server.anmelden(9000, "test2");
				loginListener.userLoggedIn(user);
			} catch (LoginFailedException | RemoteException e1) {
				JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
			}
		}
	}

	/**
	 * The listener interface for receiving loginNeuerNutzerAnlegen events. The
	 * class that is interested in processing a loginNeuerNutzerAnlegen event
	 * implements this interface, and the object created with that class is
	 * registered with a component using the component's
	 * <code>addLoginNeuerNutzerAnlegenListener<code> method. When the
	 * loginNeuerNutzerAnlegen event occurs, that object's appropriate method is
	 * invoked.
	 *
	 * @see LoginNeuerNutzerAnlegenEvent
	 */
	private class LoginNeuerNutzerAnlegenListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			JTextField firstnameField = new JTextField();
			JTextField lastnameField = new JTextField();
			JTextField addressStreetField = new JTextField();
			JTextField addressTownField = new JTextField();
			JTextField addressZipField = new JTextField();
			JTextField passwordField = new JPasswordField();
			Object[] message = {"Vorname:", firstnameField, "Nachname:", lastnameField, "Straße:", addressStreetField,
					"Stadt:", addressTownField, "PLZ:", addressZipField, "Passwort:", passwordField};
			int option = JOptionPane.showConfirmDialog(null, message, "Nutzer anlegen", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				if (!firstnameField.getText().equals("")) {
					if (!lastnameField.getText().equals("")) {
						if (!addressStreetField.getText().equals("")) {
							if (!addressTownField.getText().equals("")) {
								if (!addressZipField.getText().equals("") && addressZipField.getText().length() == 5) {
									if (!passwordField.getText().equals("")) {
										try {
											Kunde ku = server.erstelleKunde(firstnameField.getText(), lastnameField.getText(),
													passwordField.getText(), addressStreetField.getText(), addressZipField.getText(),
													addressTownField.getText(), null);
											JOptionPane.showMessageDialog(LoginWindow.this,
													"Benutzer " + ku.getId() + " erfolgreich erstellt");
										} catch (MaxIDsException e1) {
											JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
										} catch (AccessRestrictedException e1) {
											JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
										} catch (InvalidPersonDataException e1) {
											JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
										} catch (RemoteException e1) {
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

	/**
	 * The listener interface for receiving loginUserUmgehen events. The class
	 * that is interested in processing a loginUserUmgehen event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addLoginUserUmgehenListener<code>
	 * method. When the loginUserUmgehen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see LoginUserUmgehenEvent
	 */
	private class LoginUserUmgehenListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				Person user = server.anmelden(1001, "test");
				loginListener.userLoggedIn(user);
			} catch (LoginFailedException | RemoteException e1) {
				JOptionPane.showMessageDialog(LoginWindow.this, e1.getMessage());
			}
		}
	}
}
