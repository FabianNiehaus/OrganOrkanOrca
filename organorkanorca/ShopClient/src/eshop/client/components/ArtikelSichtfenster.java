package eshop.client.components;

import java.awt.Color;
import java.awt.Component;
import java.rmi.RemoteException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.PatternFilter;

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
	private ArtikelTableModel	model;
	
	/** The verlauf anzeigen button. */
	JButton							verlaufAnzeigenButton	= new JButton("Verlauf anzeigen");

	/**
	 * Instantiates a new artikel sichtfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param sichtfensterCallbacks
	 *           the sichtfenster callbacks
	 */
	public ArtikelSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks sichtfensterCallbacks) {
		super(server, user, sichtfensterCallbacks,
				new String[] {"Artikelnummer", "Bezeichnung", "Preis", "Einheit", "Bestand"});
		auflistung.getSelectionModel().addListSelectionListener(new ArtikelAnzeigenListener());
	}

	/*
	 * (non-Javadoc)
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

	/*
	 * (non-Javadoc)
	 * @see eshop.client.util.Sichtfenster#initializeHighlighting()
	 */
	@Override
	public void initializeHighlighting() {

		final HighlightPredicate predicate1 = new HighlightPredicate() {

			@Override
			public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {

				return (int) adapter.getValue(4) == 0;
			}
		};
		final HighlightPredicate predicate2 = new HighlightPredicate() {

			@Override
			public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {

				return (int) adapter.getValue(4) < 10 && !((int) adapter.getValue(4) == 0);
			}
		};
		final HighlightPredicate predicate3 = new HighlightPredicate() {

			@Override
			public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {

				return (int) adapter.getValue(4) >= 10;
			}
		};
		ColorHighlighter highlighter1 = new ColorHighlighter(predicate1, new Color(192, 192, 192, 128), Color.GRAY);
		ColorHighlighter highlighter2 = new ColorHighlighter(predicate2, new Color(255, 200, 0, 64), null);
		ColorHighlighter highlighter3 = new ColorHighlighter(predicate3, new Color(0, 255, 0, 64), null);
		auflistung.setHighlighters(new ColorHighlighter[] {highlighter1, highlighter2, highlighter3});
	}

	/*
	 * (non-Javadoc)
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
				new PatternFilter(".*" + sucheField2.getText() + ".*", Pattern.CASE_INSENSITIVE, 1),
				new PatternFilter(".*" + sucheField3.getText() + ".*", Pattern.CASE_INSENSITIVE, 2),
				new PatternFilter(".*" + sucheField4.getText() + ".*", Pattern.CASE_INSENSITIVE, 3),
				new PatternFilter(".*" + sucheField5.getText() + ".*", Pattern.CASE_INSENSITIVE, 4)};
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
				if (auflistung.getSelectedRow() != -1) sichtfensterCallbacks.artikelAnzeigen(
						server.artikelSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
				return;
			} catch (RemoteException | ArticleNonexistantException | AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
			}
		}
	}
}
