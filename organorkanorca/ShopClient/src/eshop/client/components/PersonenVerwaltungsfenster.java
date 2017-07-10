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

public class PersonenVerwaltungsfenster extends Verwaltungsfenster {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3593841333668075281L;
	JButton							aendernButton		= new JButton("Ändern");
	JPanel							buttons				= new JPanel();
	JPanel							detailArea			= new JPanel();
	JButton							loeschenButton		= new JButton("Löschen");
	JTextField						nachnameField		= new JTextField(15);
	JLabel							nachnameLabel		= new JLabel("Nachname:");
	JButton							neuAnlegenButton	= new JButton("Neu");
	JTextField						ortField				= new JTextField(15);
	JLabel							ortLabel				= new JLabel("Stadt:");
	JTextField						passwordField		= new JTextField("*********", 15);
	JLabel							passwordLabel		= new JLabel("Passwort:");
	JTextField						persNrField			= new JTextField(15);
	JLabel							persNrLabel			= new JLabel("ID:");
	Person							personStore;
	JTextField						strasseField		= new JTextField(15);
	JLabel							strasseLabel		= new JLabel("Straße:");
	String							typ					= "";
	JTextField						vornameField		= new JTextField(15);
	JLabel							vornameLabel		= new JLabel("Vorname:");
	JTextField						zipField				= new JTextField(15);
	JLabel							zipLabel				= new JLabel("PLZ:");

	public PersonenVerwaltungsfenster(ShopRemote server, Person user,
			VerwaltungsfensterCallbacks verwaltungsfensterCallbacks, String titel, String personenTyp) {
		super(server, user, verwaltungsfensterCallbacks);
		this.setLayout(new MigLayout("", "114[]0"));
		detailArea.setLayout(new MigLayout("", "[]10[]"));
		detailArea.add(new JLabel(titel), "wrap 10!, span 2");
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

	private void clearInputFields() {

		persNrField.setText("");
		vornameField.setText("");
		nachnameField.setText("");
		strasseField.setText("");
		ortField.setText("");
		zipField.setText("");
		passwordField.setText("");
	}

	public Person getPerson() {

		return personStore;
	}

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

	private void setInputFieldsEditable(boolean editable) {

		vornameField.setEditable(editable);
		nachnameField.setEditable(editable);
		strasseField.setEditable(editable);
		ortField.setEditable(editable);
		zipField.setEditable(editable);
		passwordField.setEditable(editable);
	}

	public class PersonBearbeitenListener implements ActionListener {

		public PersonBearbeitenListener(String personenTyp) {
			if (personenTyp.equals("kunde")) {
				typ = "kunde";
			} else if (personenTyp.equals("Mitarbeiter")) {
				typ = "mitarbeiter";
			}
		}

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

	public class PersonLoeschenListener implements ActionListener {

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

	public class PersonNeuAnlegenListener implements ActionListener {

		public PersonNeuAnlegenListener(String personenTyp) {
			if (personenTyp.equals("Kunde")) {
				typ = "kunde";
			} else if (personenTyp.equals("Mitarbeiter")) {
				typ = "mitarbeiter";
			}
		}

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
