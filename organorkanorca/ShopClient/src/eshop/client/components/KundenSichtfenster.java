package eshop.client.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JOptionPane;

import eshop.client.MainWindow;
import eshop.client.util.Sichtfenster;
import eshop.client.util.Sichtfenster.SichtfensterCallbacks;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

class KundenSichtfenster extends Sichtfenster {

	public KundenSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		aktion.setText("Bearbeiten");
		aktion.addActionListener(new KundeBearbeitenListener());
		anzahl.setVisible(false);
		
		try {
			updateTable(server.alleKundenAusgeben(user), new String[] { "Kundennummer", "Vorname", "Nachname", "Straße", "PLZ", "Ort" } );
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(KundenSichtfenster.this, e.getMessage());
		}
	}

	class KundeBearbeitenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			listener.kundeBearbeiten();

		}
	}
	
	@Override
	public void callTableUpdate() {
		try {
			updateTable(server.alleKundenAusgeben(user), new String[] { "ArtNr.", "Bezeichnung", "Preis", "Einheit", "Bestand" } );
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(KundenSichtfenster.this, e.getMessage());
		}
		
	}
}


