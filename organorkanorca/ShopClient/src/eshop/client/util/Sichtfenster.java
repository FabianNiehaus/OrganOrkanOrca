package eshop.client.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.decorator.SortOrder;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class Sichtfenster.
 */
public abstract class Sichtfenster extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long			serialVersionUID			= 8136926280757449267L;
	/** The action field. */
	protected JPanel						actionField					= new JPanel(new MigLayout("align 50% 50%"));
	/** The alle button. */
	protected JButton						alleButton					= new JButton("Alle");
	/** The auflistung. */
	protected JXTable						auflistung					= new JXTable();
	/** The auflistung container. */
	protected JScrollPane				auflistungContainer		= new JScrollPane(auflistung);
	/** The overview buttons. */
	protected JPanel						overviewButtons			= new JPanel();
	/** The server. */
	protected ShopRemote					server;
	/** The sichtfensterCallbacks. */
	protected SichtfensterCallbacks	sichtfensterCallbacks	= null;
	/** The suche button. */
	protected JButton						sucheButton					= new JButton("Suche");
	/** The suche field. */
	protected JTextField					sucheField					= new JTextField("Bezeichnung", 30);
	/** The suche field 2. */
	protected JTextField					sucheField2					= new JTextField("Artikel Nr.", 30);
	/** The suche field 3. */
	protected JTextField					sucheField3					= new JTextField("Einheit", 30);
	/** The user. */
	protected Person						user;

	/**
	 * Instantiates a new sichtfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param sichtfensterCallbacks
	 *           the sichtfensterCallbacks
	 */
	public Sichtfenster(ShopRemote server, Person user, SichtfensterCallbacks sichtfensterCallbacks) {
		this.sichtfensterCallbacks = sichtfensterCallbacks;
		this.user = user;
		this.server = server;
		this.setLayout(new MigLayout());
		this.add(overviewButtons, "dock west");
		this.add(auflistungContainer, "wrap, w 100%, h 200!");
		overviewButtons.setLayout(new MigLayout());
		overviewButtons.add(alleButton, "wrap 10,w 100!");
		overviewButtons.add(sucheField, "wrap 10,w 100!");
		overviewButtons.add(sucheField2, "wrap 10,w 100!");
		overviewButtons.add(sucheField3, "wrap 10,w 100!");
		overviewButtons.add(sucheButton, "wrap 10, w 100!");
		overviewButtons.setVisible(true);
		auflistung.setAutoCreateRowSorter(true);
		callTableUpdate();
		auflistung.setHorizontalScrollEnabled(true);
		auflistung.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader header = auflistung.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.setReorderingAllowed(false);
		sucheField.setHorizontalAlignment(SwingConstants.CENTER);
		sucheField2.setHorizontalAlignment(SwingConstants.CENTER);
		sucheField3.setHorizontalAlignment(SwingConstants.CENTER);
		sucheField.addFocusListener(new FocusListener() {

			String text;

			@Override
			public void focusGained(FocusEvent e) {

				text = sucheField.getText();
				sucheField.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {

				// TODO Auto-generated method stub
				if (sucheField.getText().equals("")) {
					sucheField.setText("Bezeichnung");
				}
			}
		});
		sucheField2.addFocusListener(new FocusListener() {

			String text;

			@Override
			public void focusGained(FocusEvent e) {

				text = sucheField2.getText();
				sucheField2.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {

				// TODO Auto-generated method stub
				if (sucheField2.getText().equals("")) {
					sucheField2.setText("Artikel Nr.");
				}
			}
		});
		sucheField3.addFocusListener(new FocusListener() {

			String text;

			@Override
			public void focusGained(FocusEvent e) {

				text = sucheField3.getText();
				sucheField3.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {

				// TODO Auto-generated method stub
				if (sucheField3.getText().equals("")) {
					sucheField3.setText("Einheit");
				}
			}
		});
		alleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				TabellenFilterEntfernen();
			}
		});
		sucheButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				TabelleFiltern();
			}
		});
	}

	/**
	 * Call table update.
	 */
	public abstract void callTableUpdate();

	/**
	 * Fit table layout.
	 */
	public void fitTableLayout() {

		TableColumnAdjuster tca = new TableColumnAdjuster(auflistung);
		tca.adjustColumns(SwingConstants.CENTER);
		int columnWidth = auflistung.getWidth() / auflistung.getColumnCount();
		for (int i = 0; i < auflistung.getColumnCount(); i++) {
			auflistung.getColumn(i).setWidth(columnWidth);
		}
		auflistung.setSortOrder(0, SortOrder.ASCENDING);
	}

	/**
	 * Tabelle filtern.
	 */
	public void TabelleFiltern() {

		Filter[] filterArray = {new PatternFilter(".*" + sucheField.getText() + ".*", Pattern.CASE_INSENSITIVE, 0),
				new PatternFilter(".*" + sucheField2.getText() + ".*", Pattern.CASE_INSENSITIVE, 1),
				new PatternFilter(".*" + sucheField3.getText() + ".*", Pattern.CASE_INSENSITIVE, 2)};
		FilterPipeline filters = new FilterPipeline(filterArray);
		auflistung.setFilters(filters);
	}

	/**
	 * Tabellen filter entfernen.
	 */
	public void TabellenFilterEntfernen() {

		auflistung.setFilters(null);
	}

	/**
	 * The Interface SichtfensterCallbacks.
	 */
	public interface SichtfensterCallbacks {

		/**
		 * Artikel anzeigen.
		 *
		 * @param art
		 *           the artikel
		 */
		void artikelAnzeigen(Artikel art);

		void artikelAusWarenkorbAnzeigen(Artikel art, int anzahl);

		/**
		 * Kunde anzeigen.
		 *
		 * @param ku
		 *           the kunde
		 */
		void kundeAnzeigen(Kunde ku);

		/**
		 * Mitarbeiter anzeigen.
		 *
		 * @param mi
		 *           the mitarbeiter
		 */
		void mitarbeiterAnzeigen(Mitarbeiter mi);
	}
}
