package eshop.client.components;

import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eshop.client.components.tablemodels.ArtikelTableModel;
import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.net.ShopRemote;

public class ArtikelSichtfenster extends Sichtfenster {
    
    private ArtikelTableModel model;
    
    class ArtikelAnzeigenListener implements ListSelectionListener  {

		@Override
		public void valueChanged(ListSelectionEvent e) {

		    try {
			listener.artikelAnzeigen(
					server.artikelSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
		} catch (RemoteException | ArticleNonexistantException | AccessRestrictedException e1) {
			JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
		}
		    
		}
	}

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5439399681692245672L;

	JButton verlaufAnzeigenButton = new JButton("Verlauf anzeigen");

	public ArtikelSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		auflistung.getSelectionModel().addListSelectionListener(new ArtikelAnzeigenListener());
	}

	@Override
	public void callTableUpdate() {

		try {
				    
			model = new ArtikelTableModel(server.alleArtikelAusgeben(user));
			
			auflistung.setModel(model);
					
			fitTableLayout();
					
			
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e.getMessage());
		}
	}
	
	
}
