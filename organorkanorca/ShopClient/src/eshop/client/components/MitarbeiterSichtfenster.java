package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

public class MitarbeiterSichtfenster extends Sichtfenster {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3884938912530629406L;

	public MitarbeiterSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		aktion.setText("Bearbeiten");
		aktion.addActionListener(new MitarbeiterBearbeitenListener());
		anzahl.setVisible(false);
	}

	class MitarbeiterBearbeitenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				listener.mitarbeiterBearbeiten(server.mitarbeiterSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e1.getMessage());
			} catch (PersonNonexistantException e1) {
				JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e1.getMessage());
			}
		}
	}

	@Override
	public void callTableUpdate() {

		try {
			updateTable(server.alleMitarbeiterAusgeben(user),
					new String[] { "MaNr.", "Vorname", "Nachname", "Straße", "PLZ", "Ort" });
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e.getMessage());
		}
	}
}
