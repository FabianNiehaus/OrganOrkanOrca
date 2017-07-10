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
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.MaxIDsException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class ManagementVerwaltungsfenster extends Verwaltungsfenster {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3593841333668075281L;
	JButton							aendernButton		= new JButton("Ändern");
	JPanel							buttons				= new JPanel();
	JPanel							detailArea			= new JPanel();
	JButton							loeschenButton		= new JButton("Löschen");
	JButton							neuAnlegenButton	= new JButton("Neu");
	Ereignis						ereignisStore;

	JLabel							datumLabel			= new JLabel("Datum:");
	JTextField						datumField			= new JTextField(15);
	JLabel							eventNrLabel			= new JLabel("Event Nr.:");
	JTextField						eventNrField			= new JTextField(15);
	JLabel							aktionLabel			= new JLabel("Aktion:");
	JTextField						aktionField			= new JTextField(15);
	JLabel							artikelNrLabel			= new JLabel("Artikel Nr.:");
	JTextField						artikelNrField			= new JTextField(15);
	JLabel							artikelBezLabel			= new JLabel("Artikel Bez.:");
	JTextField						artikelBezField			= new JTextField(15);
	JLabel							mengeLabel			= new JLabel("Menge:");
	JTextField						mengeField			= new JTextField(15);
	JLabel							idLabel			= new JLabel("ID:");
	JTextField						idField			= new JTextField(15);
	JLabel							nameLabel			= new JLabel("Name:");
	JTextField						nameField			= new JTextField(15);

	public ManagementVerwaltungsfenster(ShopRemote server, Person user,
			VerwaltungsfensterCallbacks verwaltungsfensterCallbacks) {
		super(server, user, verwaltungsfensterCallbacks);
		this.setLayout(new MigLayout("", "114[]0"));
		detailArea.setLayout(new MigLayout("", "[]10[]"));
		
		detailArea.add(new JLabel("Ereignis"), "wrap 10!, span 2");
		detailArea.add(datumLabel);
		detailArea.add(datumField, "");
		detailArea.add(idLabel);
		detailArea.add(idField);
		detailArea.add(artikelNrLabel);
		detailArea.add(artikelNrField, "wrap 10!");
		detailArea.add(eventNrLabel);
		detailArea.add(eventNrField, "");
		detailArea.add(nameLabel);
		detailArea.add(nameField);
		detailArea.add(artikelBezLabel);
		detailArea.add(artikelBezField, "wrap 10!");
		detailArea.add(aktionLabel, "cell 2 3");
		detailArea.add(aktionField, "cell 3 3");
		detailArea.add(mengeLabel, "cell 4 3");
		detailArea.add(mengeField, "cell 5 3");


		this.add(detailArea, "w 100%, h 200!, wrap");
		detailArea.setBackground(Color.WHITE);
		detailArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		buttons.setLayout(new MigLayout());

		//aendernButton.addActionListener(new PersonBearbeitenListener(personenTyp));
		//neuAnlegenButton.addActionListener(new PersonNeuAnlegenListener(personenTyp));
		//loeschenButton.addActionListener(new PersonLoeschenListener());
		
		datumField.setEditable(false);
		eventNrField.setEditable(false);
		aktionField.setEditable(false);
		artikelNrField.setEditable(false);
		artikelBezField.setEditable(false);
		mengeField.setEditable(false);
		idField.setEditable(false);
		nameField.setEditable(false);
		this.setVisible(true);
	}

	private void clearInputFields() {

		datumField.setText("");
		eventNrField.setText("");
		aktionField.setText("");
		artikelNrField.setText("");
		artikelNrField.setText("");
		mengeField.setText("");
		idField.setText("");
		nameField.setText("");
	}

	public Ereignis getEreignis() {

		return ereignisStore;
	}

	public void ereignisAnzeigen(Ereignis ereignisStore) {

		reset();
		this.ereignisStore = ereignisStore;
		datumField.setText(String.valueOf(ereignisStore.getWann()));	
		eventNrField.setText(String.valueOf(ereignisStore.getId()));
		aktionField.setText(String.valueOf(ereignisStore.getTyp()));
		artikelNrField.setText(String.valueOf(ereignisStore.getWomit().getArtikelnummer()));
		artikelNrField.setText(ereignisStore.getWomit().getBezeichnung());
		mengeField.setText(String.valueOf(ereignisStore.getWieviel()));
		idField.setText(String.valueOf(ereignisStore.getWer().getId()));
		nameField.setText(ereignisStore.getWer().getLastname());
		
		
		datumField.setEditable(false);	
		eventNrField.setEditable(false);
		aktionField.setEditable(false);
		artikelNrField.setEditable(false);
		artikelNrField.setEditable(false);
		mengeField.setEditable(false);
		idField.setEditable(false);
		nameField.setEditable(false);
		
	}

	@Override
	public void reset() {

		this.ereignisStore = null;
		clearInputFields();
		//setInputFieldsEditable(false);
		neuAnlegenButton.setText("Neu");
		isBeingCreated = false;
		aendernButton.setText("Ändern");
		isBeingChanged = false;
	}


}
