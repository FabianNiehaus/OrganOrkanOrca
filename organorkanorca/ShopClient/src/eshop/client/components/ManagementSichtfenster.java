package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import eshop.client.components.tablemodels.EreignisTableModel;
import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

public class ManagementSichtfenster extends Sichtfenster {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4495211746151221729L;
	
	private EreignisTableModel model;
	JButton speichernButton = new JButton("Bestandsdaten speichern");
	JButton ladenButton = new JButton("Bestandsdaten importieren");

	public ManagementSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		actionField.remove(aktion);
		actionField.remove(anzahl);
		speichernButton.addActionListener(new PersistenceButtonListener());
		ladenButton.addActionListener(new PersistenceButtonListener());
		actionField.add(speichernButton);
		actionField.add(ladenButton);
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
	public void callTableUpdate() {

		try {
		    model = new EreignisTableModel(server.alleEreignisseAusgeben(user));
			
			auflistung.setModel(model);
			
			fitTableLayout();
			
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(ManagementSichtfenster.this, e.getMessage());
		}
	}
	
}
