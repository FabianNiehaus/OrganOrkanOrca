package eshop.client.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.SortOrder;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
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
	protected JPanel						actionField					= new JPanel(new MigLayout(""));
	
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
	
	/** The sichtfenster callbacks. */
	protected SichtfensterCallbacks	sichtfensterCallbacks	= null;
	
	/** The suche button. */
	protected JButton						sucheButton					= new JButton("Suche");
	
	/** The suche field 1. */
	protected JTextField					sucheField1					= new JTextField("", 30);
	
	/** The suche field 2. */
	protected JTextField					sucheField2					= new JTextField("", 30);
	
	/** The suche field 3. */
	protected JTextField					sucheField3					= new JTextField("", 30);
	
	/** The suche field 4. */
	protected JTextField					sucheField4					= new JTextField("", 30);
	
	/** The suche field 5. */
	protected JTextField					sucheField5					= new JTextField("", 30);
	
	/** The user. */
	protected Person						user;
	
	/** The suche field names. */
	protected String[]					sucheFieldNames			= new String[5];

	/**
	 * Instantiates a new sichtfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param sichtfensterCallbacks
	 *           the sichtfenster callbacks
	 * @param sucheFieldNames
	 *           the suche field names
	 */
	public Sichtfenster(ShopRemote server, Person user, SichtfensterCallbacks sichtfensterCallbacks,
			String[] sucheFieldNames) {
		this.sucheFieldNames = sucheFieldNames;
		this.sichtfensterCallbacks = sichtfensterCallbacks;
		this.user = user;
		this.server = server;
		this.setLayout(new MigLayout());
		this.add(overviewButtons, "dock west");
		this.add(auflistungContainer, "wrap, w 100%, h 230!");
		initializeSearchbar(sucheFieldNames);
		initializeAuflistung();
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
	 * Initialize auflistung.
	 */
	private void initializeAuflistung() {

		auflistung.setAutoCreateRowSorter(true);
		callTableUpdate();
		auflistung.setHorizontalScrollEnabled(true);
		auflistung.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader header = auflistung.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.setReorderingAllowed(false);
		initializeHighlighting();
	}

	/**
	 * Initialize highlighting.
	 */
	public abstract void initializeHighlighting();

	/**
	 * Initialize searchbar.
	 *
	 * @param sucheFieldNames
	 *           the suche field names
	 */
	private void initializeSearchbar(String[] sucheFieldNames) {

		overviewButtons.setLayout(new MigLayout());
		overviewButtons.add(alleButton, "wrap 10,w 100!");
		overviewButtons.add(sucheField1, "wrap 10,w 100!");
		overviewButtons.add(sucheField2, "wrap 10,w 100!");
		overviewButtons.add(sucheField3, "wrap 10,w 100!");
		overviewButtons.add(sucheField4, "wrap 10,w 100!");
		overviewButtons.add(sucheField5, "wrap 10,w 100!");
		overviewButtons.add(sucheButton, "wrap 10, w 100!");
		overviewButtons.setVisible(true);
		sucheField1.setHorizontalAlignment(SwingConstants.CENTER);
		sucheField2.setHorizontalAlignment(SwingConstants.CENTER);
		sucheField3.setHorizontalAlignment(SwingConstants.CENTER);
		sucheField4.setHorizontalAlignment(SwingConstants.CENTER);
		sucheField5.setHorizontalAlignment(SwingConstants.CENTER);
		sucheField1.setText(sucheFieldNames[0]);
		sucheField2.setText(sucheFieldNames[1]);
		sucheField3.setText(sucheFieldNames[2]);
		sucheField4.setText(sucheFieldNames[3]);
		sucheField5.setText(sucheFieldNames[4]);
		sucheField1.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

				if (sucheField1.getText().equals(sucheFieldNames[0])) {
					sucheField1.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				if (sucheField1.getText().equals("")) {
					sucheField1.setText(sucheFieldNames[0]);
				}
			}
		});
		sucheField2.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

				if (sucheField2.getText().equals(sucheFieldNames[1])) {
					sucheField2.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				// TODO Auto-generated method stub
				if (sucheField2.getText().equals("")) {
					sucheField2.setText(sucheFieldNames[1]);
				}
			}
		});
		sucheField3.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

				if (sucheField3.getText().equals(sucheFieldNames[2])) {
					sucheField3.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				// TODO Auto-generated method stub
				if (sucheField3.getText().equals("")) {
					sucheField3.setText(sucheFieldNames[2]);
				}
			}
		});
		sucheField4.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

				if (sucheField4.getText().equals(sucheFieldNames[3])) {
					sucheField4.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				// TODO Auto-generated method stub
				if (sucheField4.getText().equals("")) {
					sucheField4.setText(sucheFieldNames[3]);
				}
			}
		});
		sucheField5.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

				if (sucheField5.getText().equals(sucheFieldNames[4])) {
					sucheField5.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				if (sucheField5.getText().equals("")) {
					sucheField5.setText(sucheFieldNames[4]);
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
	 * Tabelle filtern.
	 */
	public abstract void TabelleFiltern();

	/**
	 * Tabellen filter entfernen.
	 */
	public void TabellenFilterEntfernen() {

		auflistung.setFilters(null);
		sucheField1.setText(sucheFieldNames[0]);
		sucheField2.setText(sucheFieldNames[1]);
		sucheField3.setText(sucheFieldNames[2]);
		sucheField4.setText(sucheFieldNames[3]);
		sucheField5.setText(sucheFieldNames[4]);
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

		/**
		 * Artikel aus warenkorb anzeigen.
		 *
		 * @param art
		 *           the artikel
		 * @param anzahl
		 *           the anzahl
		 */
		void artikelAusWarenkorbAnzeigen(Artikel art, int anzahl);

		/**
		 * Ereignis anzeigen.
		 *
		 * @param er
		 *           the er
		 */
		void ereignisAnzeigen(Ereignis er);

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
