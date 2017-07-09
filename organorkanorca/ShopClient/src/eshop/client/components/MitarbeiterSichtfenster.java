package eshop.client.components;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eshop.client.components.tablemodels.PersonenTableModel;
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
	
	private PersonenTableModel model;
	public MitarbeiterSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		auflistung.getSelectionModel().addListSelectionListener(new MitarbeiterAnzeigenListener());
	}

	class MitarbeiterAnzeigenListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {

		    try {
			if(auflistung.getSelectedRow()!= -1) listener.mitarbeiterAnzeigen(server.mitarbeiterSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
			return;
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
		    	model = new PersonenTableModel(server.alleMitarbeiterAusgeben(user));
		    	
		    	SwingUtilities.invokeLater(new Runnable(){public void run(){
				auflistung.setModel(model);
				fitTableLayout();

			}});

			
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e.getMessage());
		}
	}
}