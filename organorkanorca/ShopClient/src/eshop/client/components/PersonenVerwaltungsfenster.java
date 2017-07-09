package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

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
	private static final long serialVersionUID = 3593841333668075281L;

	Person p;
	JPanel detailArea = new JPanel();
	JLabel persNrLabel = new JLabel("ID:");
	JTextField persNrField = new JTextField(15);
	JLabel vornameLabel = new JLabel("Vorname:");
	JTextField vornameField = new JTextField(15);
	JLabel nachnameLabel = new JLabel("Nachname:");
	JTextField nachnameField = new JTextField(15);
	JLabel strasseLabel = new JLabel("Straße:");
	JTextField strasseField = new JTextField(15);
	JLabel ortLabel = new JLabel("Stadt");
	JTextField ortField = new JTextField(15);
	JLabel zipLabel = new JLabel("PLZ:");
	JTextField zipField = new JTextField(15);
	JLabel passwordLabel = new JLabel("Passwort:");
	JTextField passwordField = new JTextField("*********", 15);
	JPanel buttons = new JPanel();
	JButton neuAnlegenButton = new JButton("Neu");
	JButton aendernButton = new JButton("Ändern");
	JButton loeschenButton = new JButton("Löschen");
	String typ = "";

	public PersonenVerwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks listener,
			String titel, String personenTyp) {
		super(server, user, listener);
		this.setLayout(new MigLayout());
		detailArea.setLayout(new MigLayout());
		this.add(new JLabel(titel), "span 2, align center, wrap");
		detailArea.add(persNrLabel);
		detailArea.add(persNrField, "wrap");
		detailArea.add(vornameLabel);
		detailArea.add(vornameField, "wrap");
		detailArea.add(nachnameLabel);
		detailArea.add(nachnameField, "wrap");
		detailArea.add(strasseLabel);
		detailArea.add(strasseField, "wrap");
		detailArea.add(ortLabel);
		detailArea.add(ortField, "wrap");
		detailArea.add(zipLabel);
		detailArea.add(zipField, "wrap");
		detailArea.add(passwordLabel);
		detailArea.add(passwordField);
		this.add(detailArea, "wrap");
		
		buttons.add(neuAnlegenButton, "wrap 10, w 100!");
		buttons.add(aendernButton, "wrap 10, w 100!");
		buttons.add(loeschenButton, "wrap 10, w 100!");
		
		aendernButton.addActionListener(new PersonBearbeitenListener(personenTyp));
		neuAnlegenButton.addActionListener(new PersonNeuAnlegenListener(personenTyp));
		loeschenButton.addActionListener(new PersonLoeschenListener());
		this.add(buttons, "align center");
		persNrField.setEditable(false);
		vornameField.setEditable(false);
		nachnameField.setEditable(false);
		strasseField.setEditable(false);
		ortField.setEditable(false);
		zipField.setEditable(false);
		passwordField.setEditable(false);
		this.setVisible(true);
	}

	public void personAnzeigen(Person p) {

	    reset();
	    
		this.p = p;
		persNrField.setText(String.valueOf(p.getId()));
		vornameField.setText(p.getFirstname());
		nachnameField.setText(p.getLastname());
		strasseField.setText(p.getAddress_Street());
		ortField.setText(p.getAddress_Town());
		zipField.setText(p.getAddress_Zip());
		passwordField.setText("*********");
		vornameField.setEditable(false);
		nachnameField.setEditable(false);
		strasseField.setEditable(false);
		ortField.setEditable(false);
		zipField.setEditable(false);
		passwordField.setEditable(false);
		
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

			if (e.getSource().equals(aendernButton) && !isBeingChanged) {
				if (!persNrField.getText().equals("")) {
					// Felder editierbar machen
					vornameField.setEditable(true);
					nachnameField.setEditable(true);
					strasseField.setEditable(true);
					ortField.setEditable(true);
					zipField.setEditable(true);
					passwordField.setEditable(true);
					// Buttons anpassen
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
					
					
					p = server.personAendern(typ, p, firstname, lastname, p.getId(), passwort,
			address_Street, address_Zip, address_Town);

					// Bearbeiteten Kunden anzeigen
					personAnzeigen(p);
					// Buttons anpassen
					aendernButton.setText("Ändern");
					isBeingChanged = false;
					
				} catch (InvalidPersonDataException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					personAnzeigen(p);
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					personAnzeigen(p);
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					personAnzeigen(p);
				} catch (PersonNonexistantException e1) {
					JOptionPane.showMessageDialog(PersonenVerwaltungsfenster.this, e1.getMessage());
					personAnzeigen(p);
				}
			}
		}
	}

	public class PersonLoeschenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				server.personLoeschen(p, user);
				persNrField.setText("");
				vornameField.setText("");
				nachnameField.setText("");
				strasseField.setText("");
				ortField.setText("");
				zipField.setText("");
				passwordField.setText("");
				p = null;
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

		public PersonNeuAnlegenListener(String personenTyp){
			if (personenTyp.equals("Kunde")) {
				typ = "kunde";
			} else if (personenTyp.equals("Mitarbeiter")) {
				typ = "mitarbeiter";
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(neuAnlegenButton) && !isBeingCreated) {
				// Alle Felder leeren
				persNrField.setText("");
				vornameField.setText("");
				nachnameField.setText("");
				strasseField.setText("");
				ortField.setText("");
				zipField.setText("");
				passwordField.setText("");
				// Felder editierbar machen
				vornameField.setEditable(true);
				nachnameField.setEditable(true);
				strasseField.setEditable(true);
				ortField.setEditable(true);
				zipField.setEditable(true);
				passwordField.setEditable(true);
				// Buttons anpassen
				neuAnlegenButton.setText("OK");
				isBeingCreated = true;
				
			} else if (e.getSource().equals(neuAnlegenButton) && isBeingCreated) {
				try {
					Person p = null;
					String firstname = vornameField.getText();
					String lastname = nachnameField.getText();
					String address_Street = strasseField.getText();
					String address_Town = ortField.getText();
					String address_Zip = zipField.getText();
					String passwort = passwordField.getText();
					if (typ.equals("kunde")) {
						p = server.erstelleKunde(firstname, lastname, passwort, address_Street, address_Zip,
								address_Town, user);
					} else if (typ.equals("mitarbeiter")) {
						p = server.erstelleMitatbeiter(firstname, lastname, passwort, address_Street, address_Zip,
								address_Town, user);
					}
					// Neu erstellte Person anzeigen
					personAnzeigen(p);
					// Felder nicht editierbar machen
					vornameField.setEditable(false);
					nachnameField.setEditable(false);
					strasseField.setEditable(false);
					ortField.setEditable(false);
					zipField.setEditable(false);
					passwordField.setEditable(false);
					// Buttons anpassen
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
	
	public Person getPerson(){
		return p;
	}
	
	public void reset(){
	    this.p = null;
		persNrField.setText("");
		vornameField.setText("");
		nachnameField.setText("");
		strasseField.setText("");
		ortField.setText("");
		zipField.setText("");
		passwordField.setText("");
		
		vornameField.setEditable(false);
		nachnameField.setEditable(false);
		strasseField.setEditable(false);
		ortField.setEditable(false);
		zipField.setEditable(false);
		passwordField.setEditable(false);
		
		neuAnlegenButton.setText("Neu");
		isBeingCreated = false;
		aendernButton.setText("Ändern");
		isBeingChanged = false;
	}
}
