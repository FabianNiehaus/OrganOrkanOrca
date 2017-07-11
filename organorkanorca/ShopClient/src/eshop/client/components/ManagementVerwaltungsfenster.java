package eshop.client.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eshop.client.util.Verwaltungsfenster;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class ManagementVerwaltungsfenster extends Verwaltungsfenster {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3593841333668075281L;
	JPanel							buttons				= new JPanel();
	JPanel							detailArea			= new JPanel();
	JButton							speichernButton	= new JButton("Bestandsdaten speichern");
	JButton							ladenButton	= new JButton("Bestandsdaten laden");
	Ereignis						er;

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
		
		detailArea.add(new JLabel("Ereignis"), "wrap 20!, span 2");
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

		detailArea.setBackground(Color.WHITE);
		detailArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		buttons.setLayout(new MigLayout());

		speichernButton.addActionListener(new PersistenceButtonListener());
		ladenButton.addActionListener(new PersistenceButtonListener());
		buttons.add(speichernButton,"w 200!");
		buttons.add(ladenButton,"w 200!");
		
		setInputFieldsEditable(false);
		
		this.add(detailArea, "w 100%, h 200!, wrap");
		this.add(buttons, "right");
		
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

		return er;
	}

	public void ereignisAnzeigen(Ereignis er) {

		reset();
		this.er = er;
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		datumField.setText(dateFormat.format(er.getWann()));	
		eventNrField.setText(String.valueOf(er.getId()));
		aktionField.setText(String.valueOf(er.getTyp()));
		artikelNrField.setText(String.valueOf(er.getWomit_Nr()));
		artikelBezField.setText(er.getWomit_Bezeichnung());
		mengeField.setText(String.valueOf(er.getWieviel()));
		idField.setText(String.valueOf(er.getWer_Id()));
		nameField.setText(er.getWer_Name());
	}

	@Override
	public void reset() {

		this.er = null;
		clearInputFields();
		setInputFieldsEditable(false);
	}
	
	public void setInputFieldsEditable(boolean editable){
		datumField.setEditable(editable);
		eventNrField.setEditable(editable);
		aktionField.setEditable(editable);
		artikelNrField.setEditable(editable);
		artikelBezField.setEditable(editable);
		mengeField.setEditable(editable);
		idField.setEditable(editable);
		nameField.setEditable(editable);
	}
	
	class PersistenceButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {

			if (ae.getSource().equals(speichernButton)) {
				try {
					server.schreibeDaten();
					JOptionPane.showMessageDialog(ManagementVerwaltungsfenster.this, "Daten erfolgreich gespeichert!");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(ManagementVerwaltungsfenster.this, e1.getMessage());
				}
			} else if (ae.getSource().equals(ladenButton)) {
				try {
					server.ladeDaten();
					JOptionPane.showMessageDialog(ManagementVerwaltungsfenster.this, "Daten erfolgreich geladen!");
				} catch (IOException | ArticleNonexistantException | PersonNonexistantException
						| InvalidPersonDataException e1) {
					JOptionPane.showMessageDialog(ManagementVerwaltungsfenster.this, e1.getMessage());
				}
			}
		}
	}


}
