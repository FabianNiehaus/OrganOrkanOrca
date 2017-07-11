package eshop.client.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eshop.client.util.Verwaltungsfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.MaxIDsException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class PersonenVerwaltungsfenster.
 */
public class PersonenVerwaltungsfenster extends Verwaltungsfenster {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 3593841333668075281L;
	
	/** The aendern button. */
	JButton							aendernButton		= new JButton("Ändern");
	
	/** The buttons. */
	JPanel							buttons				= new JPanel();
	
	/** The detail area. */
	JPanel							detailArea			= new JPanel();
	
	/** The loeschen button. */
	JButton							loeschenButton		= new JButton("Löschen");
	
	/** The nachname field. */
	JTextField						nachnameField		= new JTextField(15);
	
	/** The nachname label. */
	JLabel							nachnameLabel		= new JLabel("Nachname:");
	
	/** The neu anlegen button. */
	JButton							neuAnlegenButton	= new JButton("Neu");
	
	/** The ort field. */
	JTextField						ortField				= new JTextField(15);
	
	/** The ort label. */
	JLabel							ortLabel				= new JLabel("Stadt:");
	
	/** The password field. */
	JTextField						passwordField		= new JTextField("*********", 15);
	
	/** The password label. */
	JLabel							passwordLabel		= new JLabel("Passwort:");
	
	/** The pers nr field. */
	JTextField						persNrField			= new JTextField(15);
	
	/** The pers nr label. */
	JLabel							persNrLabel			= new JLabel("ID:");
	
	/** The person store. */
	Person							personStore;
	
	/** The strasse field. */
	JTextField						strasseField		= new JTextField(15);
	
	/** The strasse label. */
	JLabel							strasseLabel		= new JLabel("Straße:");
	
	/** The typ. */
	String							typ					= "";
	
	/** The vorname field. */
	JTextField						vornameField		= new JTextField(15);
	
	/** The vorname label. */
	JLabel							vornameLabel		= new JLabel("Vorname:");
	
	/** The zip field. */
	JTextField						zipField				= new JTextField(15);
	
	/** The zip label. */
	JLabel							zipLabel				= new JLabel("PLZ:");

	/**
	 * Instantiates a new personen verwaltungsfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param verwaltungsfensterCallbacks
	 *           the verwaltungsfenster callbacks
	 * @param titel
	 *           the titel
	 * @param personenTyp
	 *           the personen typ
	 */
	public PersonenVerwaltungsfenster(ShopRemote server, Person user,
			VerwaltungsfensterCallbacks verwaltungsfensterCallbacks, String titel, String personenTyp) {
		super(server, user, verwaltungsfensterCallbacks);
		this.setLayout(new MigLayout("", "114[]0"));
		detailArea.setLayout(new MigLayout("", "[]10[]"));
		detailArea.add(new JLabel(titel), "wrap 20!, span 2");
		detailArea.add(persNrLabel);
		detailArea.add(persNrField, "");
		detailArea.add(vornameLabel);
		detailArea.add(vornameField, "");
		detailArea.add(strasseLabel);
		detailArea.add(strasseField, "wrap 10!");
		detailArea.add(passwordLabel);
		detailArea.add(passwordField);
		detailArea.add(nachnameLabel);
		detailArea.add(nachnameField);
		detailArea.add(ortLabel);
		detailArea.add(ortField, "wrap 10!");
		detailArea.add(zipLabel, "cell 4 3");
		detailArea.add(zipField, "cell 5 3");
		this.add(detailArea, "w 100%, h 200!, wrap");
		detailArea.setBackground(Color.WHITE);
		detailArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		buttons.setLayout(new MigLayout());
		buttons.add(neuAnlegenButton, "w 100!");
		buttons.add(aendernButton, "w 100!");
		buttons.add(loeschenButton, "w 100!");
		aendernButton.addActionListener(new PersonBearbeitenListener(personenTyp));
		neuAnlegenButton.addActionListener(new PersonNeuAnlegenListener(personenTyp));
		loeschenButton.addActionListener(new PersonLoeschenListener());
		this.add(buttons, "right");
		persNrField.setEditable(false);
		vornameField.setEditable(false);
		nachnameField.setEditable(false);
		strasseField.setEditable(false);
		ortField.setEditable(false);
		zipField.setEditable(false);
		passwordField.setEditable(false);
		this.setVisible(true);
	}

	/**
	 * Clear input fields.
	 */
	private void clearInputFields() {

		persNrField.setText("");
		vornameField.setText("");
		nachnameField.setText("");
		strasseField.setText("");
		ortField.setText("");
		zipField.setText("");
		passwordField.setText("");
	}

	/**
	 * Gets the person.
	 *
	 * @return the person
	 */
	public Person getPerson() {

		return personStore;
	}

	/**
	 * Person anzeigen.
	 *
	 * @param personStore
	 *           the person store
	 */
	public void personAnzeigen(Person personStore) {

		reset();
		this.personStore = personStore;
		persNrField.setText(String.valueOf(personStore.getId()));
		vornameField.setText(personStore.getFirstname());
		nachnameField.setText(personStore.getLastname());
		strasseField.setText(personStore.getAddress_Street());
		ortField.setText(personStore.getAddress_Town());
		zipField.setText(personStore.getAddress_Zip());
		passwordField.setText("*********");
		vornameField.setEditable(false);
		nachnameField.setEditable(false);
		strasseField.setEditable(false);
		ortField.setEditable(false);
		zipField.setEditable(false);
		passwordField.setEditable(false);
	}

	/* (non-Javadoc)
	 * @see eshop.client.util.Verwaltungsfenster#reset()
	 */
	@Override
	public void reset() {

		this.personStore = null;
		clearInputFields();
		setInputFieldsEditable(false);
		neuAnlegenButton.setText("Neu");
		isBeingCreated = false;
		aendernButton.setText("Ändern");
		isBeingChanged = false;
	}

	/**
	 * Sets the input fields editable.
	 *
	 * @param editable
	 *           the new input fields editable
	 */
	private void setInputFieldsEditable(boolean editable) {

		vornameField.setEditable(editable);
		nachnameField.setEditable(editable);
		strasseField.setEditable(editable);
		ortField.setEditable(editable);
		zipField.setEditable(editable);
		passwordField.setEditable(editable);
	}

	/**
	 * The listener interface for receiving personBearbeiten events. The class
	 * that is interested in processing a personBearbeiten event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addPersonBearbeitenListener<code>
	 * method. When the personBearbeiten event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see PersonBearbeitenEvent
	 */
	public class PersonBearbeitenListener implements ActionListener {

		/**
		 * Instantiates a new person bearbeiten listener.
		 *
		 * @param personenTyp
		 *           the personen typ
		 */
		public PersonBearbeitenListener(String personenTyp) {
			if (personenTyp.equals("kunde")) {
				typ = "kunde";
			} else if (personenTyp.equals("Mitarbeiter")) {
				typ = "mitarbeiter";
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(aendernButton) && !isBeingChanged && !isBeingCreated) {
				if (!persNrField.getText().equals("")) {
					setInputFieldsEditable(true);
					aendernButton.setText("OK");
					isBeingChanged = true;
				}
			} else if (e.getSource().equals(aendernButton) && isBeingChanged) {
				try {
					String firstname = vornameField.getText();
					String lastname = nachnameField.getText();
					String address_Street = strasseField.getText();
					String address_Town = ortField.getText();
					String address_Zip = zipField.getText();
					String passwort = passwordField.getText();
					personStore = server.personAendern(typ, personStore, firstname, lastname, personStore.getId(), passwort,
							address_Street, address_Zip, address_Town);
					setInputFieldsEditable(false);
					aendernButton.setText("Ändern");
					isBeingChanged = false;
				} catch (InvalidPersonDataException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					personAnzeigen(personStore);
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					personAnzeigen(personStore);
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					personAnzeigen(personStore);
				} catch (PersonNonexistantException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					personAnzeigen(personStore);
				}
			}
		}
	}

	/**
	 * The listener interface for receiving personLoeschen events. The class that
	 * is interested in processing a personLoeschen event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addPersonLoeschenListener<code>
	 * method. When the personLoeschen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see PersonLoeschenEvent
	 */
	public class PersonLoeschenListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				server.personLoeschen(personStore, user);
			} catch (AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
			} catch (InvalidPersonDataException e1) {
				JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
			} catch (PersonNonexistantException e1) {
				JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
			}
		}
	}

	/**
	 * The listener interface for receiving personNeuAnlegen events. The class
	 * that is interested in processing a personNeuAnlegen event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addPersonNeuAnlegenListener<code>
	 * method. When the personNeuAnlegen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see PersonNeuAnlegenEvent
	 */
	public class PersonNeuAnlegenListener implements ActionListener {

		/**
		 * Instantiates a new person neu anlegen listener.
		 *
		 * @param personenTyp
		 *           the personen typ
		 */
		public PersonNeuAnlegenListener(String personenTyp) {
			if (personenTyp.equals("Kunde")) {
				typ = "kunde";
			} else if (personenTyp.equals("Mitarbeiter")) {
				typ = "mitarbeiter";
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(neuAnlegenButton) && !isBeingCreated && !isBeingChanged) {
				// Alle Felder leeren
				clearInputFields();
				setInputFieldsEditable(true);
				neuAnlegenButton.setText("OK");
				isBeingCreated = true;
			} else if (e.getSource().equals(neuAnlegenButton) && isBeingCreated) {
				try {
					String firstname = vornameField.getText();
					String lastname = nachnameField.getText();
					String address_Street = strasseField.getText();
					String address_Town = ortField.getText();
					String address_Zip = zipField.getText();
					String passwort = passwordField.getText();
					if (typ.equals("kunde")) {
						personStore = server.erstelleKunde(firstname, lastname, passwort, address_Street, address_Zip,
								address_Town, user);
					} else if (typ.equals("mitarbeiter")) {
						personStore = server.erstelleMitatbeiter(firstname, lastname, passwort, address_Street, address_Zip,
								address_Town, user);
					}
					setInputFieldsEditable(false);
					neuAnlegenButton.setText("Neu");
					isBeingCreated = false;
				} catch (InvalidPersonDataException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					reset();
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					reset();
				} catch (MaxIDsException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					reset();
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
				}
			}
		}
	}
}
