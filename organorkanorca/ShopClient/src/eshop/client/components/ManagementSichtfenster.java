package eshop.client.components;

import java.rmi.RemoteException;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;

import eshop.client.components.tablemodels.EreignisTableModel;
import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.net.ShopRemote;

// TODO: Auto-generated Javadoc
/**
 * The Class ManagementSichtfenster.
 */
public class ManagementSichtfenster extends Sichtfenster {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -4495211746151221729L;
	
	/** The model. */
	private EreignisTableModel	model;

	/**
	 * Instantiates a new management sichtfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param listener
	 *           the listener
	 */
	public ManagementSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener, new String[] {"Datum", "Aktion", "ArtikelNr", "Bezeichnung", "Person (Name)"});
		auflistung.getSelectionModel().addListSelectionListener(new ArtikelAnzeigenListener());
	}

	/* (non-Javadoc)
	 * @see eshop.client.util.Sichtfenster#callTableUpdate()
	 */
	@Override
	public void callTableUpdate() {

		try {
			model = new EreignisTableModel(server.alleEreignisseAusgeben(user));
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					auflistung.setModel(model);
					fitTableLayout();
				}
			});
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(ManagementSichtfenster.this, e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see eshop.client.util.Sichtfenster#initializeHighlighting()
	 */
	@Override
	public void initializeHighlighting() {

		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see eshop.client.util.Sichtfenster#TabelleFiltern()
	 */
	@Override
	public void TabelleFiltern() {

		if (sucheField1.getText().equals(sucheFieldNames[0])) {
			sucheField1.setText("");
		}
		if (sucheField2.getText().equals(sucheFieldNames[1])) {
			sucheField2.setText("");
		}
		if (sucheField3.getText().equals(sucheFieldNames[2])) {
			sucheField3.setText("");
		}
		if (sucheField4.getText().equals(sucheFieldNames[3])) {
			sucheField4.setText("");
		}
		if (sucheField5.getText().equals(sucheFieldNames[4])) {
			sucheField5.setText("");
		}
		Filter[] filterArray = {new PatternFilter(".*" + sucheField1.getText() + ".*", Pattern.CASE_INSENSITIVE, 0),
				new PatternFilter(".*" + sucheField2.getText() + ".*", Pattern.CASE_INSENSITIVE, 2),
				new PatternFilter(".*" + sucheField3.getText() + ".*", Pattern.CASE_INSENSITIVE, 3),
				new PatternFilter(".*" + sucheField4.getText() + ".*", Pattern.CASE_INSENSITIVE, 4),
				new PatternFilter(".*" + sucheField5.getText() + ".*", Pattern.CASE_INSENSITIVE, 6)};
		FilterPipeline filters = new FilterPipeline(filterArray);
		auflistung.setFilters(filters);
	}

	/**
	 * The listener interface for receiving artikelAnzeigen events. The class
	 * that is interested in processing a artikelAnzeigen event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addArtikelAnzeigenListener<code>
	 * method. When the artikelAnzeigen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ArtikelAnzeigenEvent
	 */
	class ArtikelAnzeigenListener implements ListSelectionListener {

		/*
		 * (non-Javadoc)
		 * @see
		 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.
		 * ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {

			try {
				if (auflistung.getSelectedRow() != -1) sichtfensterCallbacks.ereignisAnzeigen(
						server.ereignisSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 1), user));
				return;
			} catch (RemoteException | ArticleNonexistantException | AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
			}
		}
	}
}
