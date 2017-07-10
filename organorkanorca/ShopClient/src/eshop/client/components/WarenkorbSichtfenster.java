package eshop.client.components;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eshop.client.components.tablemodels.WarenkorbTableModel;
import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

// TODO: Auto-generated Javadoc
/**
 * The Class WarenkorbSichtfenster.
 */
public class WarenkorbSichtfenster extends Sichtfenster {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID			= -5439399681692245672L;

	/** The model. */
	private WarenkorbTableModel model;
	
	/**
	 * Instantiates a new warenkorb sichtfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param sichtfensterCallbacks
	 *           the sichtfenster callbacks
	 */
	public WarenkorbSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks sichtfensterCallbacks) {
		super(server, user, sichtfensterCallbacks);
		auflistung.getSelectionModel().addListSelectionListener(new ArtikelAusWarenkorbAnzeigenListener());
	}

	/* (non-Javadoc)
	 * @see eshop.client.util.Sichtfenster#callTableUpdate()
	 */
	@Override
	public void callTableUpdate() {

		try {
			model = new WarenkorbTableModel(server.warenkorbAusgeben(user.getId(), user).getArtikel());
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					auflistung.setModel(model);
					fitTableLayout();
				}
			});
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(WarenkorbSichtfenster.this, e.getMessage());
		} catch (PersonNonexistantException e) {
			JOptionPane.showMessageDialog(WarenkorbSichtfenster.this, e.getMessage());
		}
	}

	/**
	 * The listener interface for receiving artikelAusWarenkorbAnzeigen events.
	 * The class that is interested in processing a artikelAusWarenkorbAnzeigen
	 * event implements this interface, and the object created with that class is
	 * registered with a component using the component's
	 * <code>addArtikelAusWarenkorbAnzeigenListener<code> method. When the
	 * artikelAusWarenkorbAnzeigen event occurs, that object's appropriate method
	 * is invoked.
	 *
	 * @see ArtikelAusWarenkorbAnzeigenEvent
	 */
	class ArtikelAusWarenkorbAnzeigenListener implements ListSelectionListener {

		/* (non-Javadoc)
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {

			try {
				if (auflistung.getSelectedRow() != -1) sichtfensterCallbacks.artikelAusWarenkorbAnzeigen(
						server.artikelSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user), (int) auflistung.getValueAt(auflistung.getSelectedRow(), 7));
				return;
			} catch (RemoteException | ArticleNonexistantException | AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(WarenkorbSichtfenster.this, e1.getMessage());
			}
		}
	}
	
	public boolean artikelImWarenkorbPruefen(Artikel art)
			throws RemoteException, AccessRestrictedException, PersonNonexistantException, ArticleNonexistantException {

			for (int i = 0; i < auflistung.getRowCount(); i++) {
				if ((Integer)auflistung.getValueAt(i, 0) == art.getArtikelnummer()) {
					return true;
				}
			}
			return false;
	}
}
