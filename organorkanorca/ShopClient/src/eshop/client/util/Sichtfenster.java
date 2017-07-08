package eshop.client.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
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

public abstract class Sichtfenster extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8136926280757449267L;

	protected SichtfensterCallbacks listener = null;	

	protected Person user;
	protected ShopRemote server;
	protected JPanel overviewButtons = new JPanel();
	protected JButton alleButton = new JButton("Alle");
	protected JButton sucheButton = new JButton("Suche");
	protected JTextField sucheField = new JTextField(30);
	protected JPanel actionField = new JPanel(new MigLayout("align 50% 50%"));
	protected JXTable auflistung = new JXTable();
	protected JScrollPane auflistungContainer = new JScrollPane(auflistung);
	protected JButton aktion = new JButton();
	protected JTextField anzahl = new JTextField(5);

	public Sichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		this.listener = listener;
		this.user = user;
		this.server = server;
			
		auflistung.setAutoCreateRowSorter(true);
		
		callTableUpdate();
		fitTableLayout();

		auflistung.setHorizontalScrollEnabled(true);
		//auflistung.setPreferredScrollableViewportSize(new Dimension(500, 70));
		//auflistung.setFillsViewportHeight(true);
		auflistung.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JTableHeader header = auflistung.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.setReorderingAllowed(false);		
		
		this.setLayout(new MigLayout("fillx, align 50% 50%"));
		// overviewButtons.setMaximumSize(new Dimension(1024, 40));
		this.add(overviewButtons, "wrap, w 100%");
		this.add(auflistungContainer, "wrap");
		this.add(actionField, "w 100%");
		
		overviewButtons.setLayout(new BoxLayout(overviewButtons, BoxLayout.X_AXIS));
		overviewButtons.add(alleButton);
		overviewButtons.add(sucheField);
		overviewButtons.add(sucheButton);
		overviewButtons.setVisible(true);
		
		actionField.add(aktion);
		actionField.add(anzahl);
		
		
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

	public abstract void callTableUpdate();
	
	public void fitTableLayout(){
	    TableColumnAdjuster tca = new TableColumnAdjuster(auflistung, 30);
	    tca.adjustColumns(SwingConstants.CENTER);
	    
	    //auflistung.setSortOrder(1, SortOrder.ASCENDING);
	    auflistung.setSortOrder(0, SortOrder.ASCENDING);
	}
	
	public void TabelleFiltern() {
	    	    
	    Filter[] filterArray = {new PatternFilter(".*"+sucheField.getText()+".*", Pattern.CASE_INSENSITIVE, 1)};
	    
	    FilterPipeline filters = new FilterPipeline(filterArray);
	    
	    auflistung.setFilters(filters);

	}

	public void TabellenFilterEntfernen() {
	    auflistung.setFilters(null);
		
	}

	public interface SichtfensterCallbacks {

		public void alleFensterErneuern();

		public void artikelBearbeiten(Artikel art);

		public void artikelInWarenkorb();

		public void kundeBearbeiten(Kunde kunde);

		public void mitarbeiterBearbeiten(Mitarbeiter mi);
	}

}
