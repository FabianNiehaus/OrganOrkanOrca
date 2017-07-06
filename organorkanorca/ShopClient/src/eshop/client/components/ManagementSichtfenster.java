package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

public class ManagementSichtfenster extends Sichtfenster {

    JButton	       speichernButton	   = new JButton("Bestandsdaten speichern");
    JButton	       ladenButton	   = new JButton("Bestandsdaten importieren");
    JScrollPane	       auflistungContainer = new JScrollPane(auflistung);

    public ManagementSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
	super(server, user, listener);
	this.remove(aktion);
	speichernButton.addActionListener(new PersistenceButtonListener());
	ladenButton.addActionListener(new PersistenceButtonListener());
	this.add(speichernButton, "dock center");
	this.add(ladenButton, "wrap, dock center");
    }

    class PersistenceButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent ae) {

	    if (ae.getSource().equals(speichernButton)) {
		try {
		    server.schreibeDaten();
		    JOptionPane.showMessageDialog(ManagementSichtfenster.this, "Daten erfolgreich gespeichert!");
		} catch(IOException e1) {
		    JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
		}
	    } else if (ae.getSource().equals(ladenButton)) {
		try {
		    server.ladeDaten();
		    JOptionPane.showMessageDialog(ManagementSichtfenster.this, "Daten erfolgreich geladen!");
		} catch(IOException | ArticleNonexistantException | PersonNonexistantException
			| InvalidPersonDataException e1) {
		    JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
		}
	    }
	}
    }

    @Override
    public void callTableUpdate() {

	try {
	    updateTable(server.alleEreignisseAusgeben(user),
		    new String[] { "ArtNr.", "Bezeichnung", "Preis", "Einheit", "Bestand" });
	} catch(RemoteException | AccessRestrictedException e) {
	    JOptionPane.showMessageDialog(ManagementSichtfenster.this, e.getMessage());
	}
    }
}
