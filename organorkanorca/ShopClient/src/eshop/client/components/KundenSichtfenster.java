package eshop.client.components;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
		auflistung.getSelectionModel().addListSelectionListener(new KundeAnzeigenListener());
	}

	class KundeAnzeigenListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {

		    try {
			if(auflistung.getSelectedRow()!= -1) listener.kundeAnzeigen(server.kundeSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
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
			auflistung.getSelectionModel().addListSelectionListener(new KundeAnzeigenListener());
			
			fitTableLayout();

		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(KundenSichtfenster.this, e.getMessage());
		}
	}

}
