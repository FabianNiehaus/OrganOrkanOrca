package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
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
	try {
	    updateTable(server.alleMitarbeiterAusgeben(user),
		    new String[] { "ID", "Vorname", "Nachname", "Straße", "PLZ", "Ort" });
	} catch(RemoteException | AccessRestrictedException e) {
	    JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e.getMessage());
	}
    }

    class MitarbeiterBearbeitenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    listener.mitarbeiterBearbeiten();
	}
    }

    @Override
    public void callTableUpdate() {

	try {
	    updateTable(server.alleMitarbeiterAusgeben(user),
		    new String[] { "ArtNr.", "Bezeichnung", "Preis", "Einheit", "Bestand" });
	} catch(RemoteException | AccessRestrictedException e) {
	    JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e.getMessage());
	}
    }
}
