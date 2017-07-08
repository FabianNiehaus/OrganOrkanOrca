package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import eshop.client.components.tablemodels.PersonenTableModel;
import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

public class KundenSichtfenster extends Sichtfenster {

    private PersonenTableModel model;
    /**
	 * 
	 */
	private static final long serialVersionUID = 4821072292018595904L;

	public KundenSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		aktion.setText("Bearbeiten");
		aktion.addActionListener(new KundeBearbeitenListener());
		anzahl.setVisible(false);
	}

	class KundeBearbeitenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				listener.kundeBearbeiten(
						server.kundeSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(KundenSichtfenster.this, e1.getMessage());
			} catch (PersonNonexistantException e1) {
				JOptionPane.showMessageDialog(KundenSichtfenster.this, e1.getMessage());
			}
		}
	}

	@Override
	public void callTableUpdate() {

		try {

		    model = new PersonenTableModel(server.alleKundenAusgeben(user));
			
			auflistung.setModel(model);
			
			fitTableLayout();

		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(KundenSichtfenster.this, e.getMessage());
		}
	}

}
