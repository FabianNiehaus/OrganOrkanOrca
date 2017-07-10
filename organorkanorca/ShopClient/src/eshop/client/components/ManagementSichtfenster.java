package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;

import eshop.client.components.tablemodels.EreignisTableModel;
import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class ManagementSichtfenster extends Sichtfenster {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4495211746151221729L;
	JButton							ladenButton			= new JButton("Bestandsdaten importieren");
	private EreignisTableModel	model;
	JButton							speichernButton	= new JButton("Bestandsdaten speichern");

	public ManagementSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener, new String[]{"Datum","Aktion","ArtikelNr","Bezeichnung","Person (Name)"});
		this.setLayout(new MigLayout());
		actionField.setLayout(new MigLayout());
		speichernButton.addActionListener(new PersistenceButtonListener());
		ladenButton.addActionListener(new PersistenceButtonListener());
		actionField.add(speichernButton, "wrap");
		actionField.add(ladenButton);
		// setPreferredSize(new Dimension(900, 400));
	}

	@Override
	public void callTableUpdate() {

		try {
			model = new EreignisTableModel(server.alleEreignisseAusgeben(user));
			auflistung.setModel(model);
			fitTableLayout();
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(ManagementSichtfenster.this, e.getMessage());
		}
	}

	class PersistenceButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {

			if (ae.getSource().equals(speichernButton)) {
				try {
					server.schreibeDaten();
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, "Daten erfolgreich gespeichert!");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
				}
			} else if (ae.getSource().equals(ladenButton)) {
				try {
					server.ladeDaten();
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, "Daten erfolgreich geladen!");
				} catch (IOException | ArticleNonexistantException | PersonNonexistantException
						| InvalidPersonDataException e1) {
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
				}
			}
		}
	}

	@Override
	public void TabelleFiltern() {

		if(sucheField1.getText().equals(sucheFieldNames[0])){
			sucheField1.setText("");
		}
		if(sucheField2.getText().equals(sucheFieldNames[1])){
			sucheField2.setText("");
		}
		if(sucheField3.getText().equals(sucheFieldNames[2])){
			sucheField3.setText("");
		}
		if(sucheField4.getText().equals(sucheFieldNames[3])){
			sucheField4.setText("");
		}
		if(sucheField5.getText().equals(sucheFieldNames[4])){
			sucheField5.setText("");
		}

		Filter[] filterArray = {new PatternFilter(".*" + sucheField1.getText() + ".*", Pattern.CASE_INSENSITIVE, 0),
				new PatternFilter(".*" + sucheField2.getText() + ".*", Pattern.CASE_INSENSITIVE, 2),
				new PatternFilter(".*" + sucheField3.getText() + ".*", Pattern.CASE_INSENSITIVE, 3),
				new PatternFilter(".*" + sucheField4.getText() + ".*", Pattern.CASE_INSENSITIVE, 4),
				new PatternFilter(".*" + sucheField5.getText() + ".*", Pattern.CASE_INSENSITIVE, 6)};
		FilterPipeline filters = new FilterPipeline(filterArray);
		auflistung.setFilters(filters);
		
	}

	@Override
	public void initializeHighlighting() {

		// TODO Auto-generated method stub
		
	}
}
