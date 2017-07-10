package eshop.client.components;

import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eshop.client.components.tablemodels.ArtikelTableModel;
import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.net.ShopRemote;

// TODO: Auto-generated Javadoc
/**
 * The Class ArtikelSichtfenster.
 */
public class ArtikelSichtfenster extends Sichtfenster {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID			= -5439399681692245672L;

	/** The model. */
	private ArtikelTableModel model;

	/** The verlauf anzeigen button. */
	JButton							verlaufAnzeigenButton	= new JButton("Verlauf anzeigen");
	
	/**
	 * Instantiates a new artikel sichtfenster.
	 *
	 * @param server the server
	 * @param user the user
	 * @param sichtfensterCallbacks the sichtfensterCallbacks
	 */
	public ArtikelSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks sichtfensterCallbacks) {
		super(server, user, sichtfensterCallbacks);
		auflistung.getSelectionModel().addListSelectionListener(new ArtikelAnzeigenListener());
	}

	/* (non-Javadoc)
	 * @see eshop.client.util.Sichtfenster#callTableUpdate()
	 */
	@Override
	public void callTableUpdate() {

		try {
			model = new ArtikelTableModel(server.alleArtikelAusgeben(user));
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					auflistung.setModel(model);
					fitTableLayout();
				}
			});
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e.getMessage());
		}
	}

	/**
	 * The sichtfensterCallbacks interface for receiving artikelAnzeigen events.
	 * The class that is interested in processing a artikelAnzeigen
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addArtikelAnzeigenListener<code> method. When
	 * the artikelAnzeigen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ArtikelAnzeigenEvent
	 */
	class ArtikelAnzeigenListener implements ListSelectionListener {

		/* (non-Javadoc)
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {

			try {
				if (auflistung.getSelectedRow() != -1) sichtfensterCallbacks.artikelAnzeigen(
						server.artikelSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
				return;
			} catch (RemoteException | ArticleNonexistantException | AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
			}
		}
	}
}
