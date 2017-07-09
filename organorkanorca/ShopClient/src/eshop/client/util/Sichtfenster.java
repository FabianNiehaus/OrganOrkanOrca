package eshop.client.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public Sichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
	this.listener = listener;
	this.user = user;
	this.server = server;
	this.setLayout(new MigLayout());
	this.add(overviewButtons, "dock west");
	//this.add(actionField);
	this.add(auflistungContainer,"wrap, w 100%");
	
	overviewButtons.setLayout(new MigLayout());
	overviewButtons.add(alleButton, "wrap 10,w 100!");
	overviewButtons.add(sucheField, "wrap 10,w 100!");
	overviewButtons.add(sucheButton, "wrap 10, w 100!");
	overviewButtons.add(actionField, "wrap 10, w 100!");
	overviewButtons.setVisible(true);
	actionField.setBackground(Color.CYAN);

	auflistung.setAutoCreateRowSorter(true);
		
		callTableUpdate();

		auflistung.setHorizontalScrollEnabled(true);
		//auflistung.setPreferredScrollableViewportSize(new Dimension(500, 70));
		//auflistung.setFillsViewportHeight(true);
		auflistung.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JTableHeader header = auflistung.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.setReorderingAllowed(false);	
		
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

		void artikelAnzeigen(Artikel art);

		void mitarbeiterAnzeigen(Mitarbeiter mi);

		void kundeAnzeigen(Kunde ku);
	}

}
