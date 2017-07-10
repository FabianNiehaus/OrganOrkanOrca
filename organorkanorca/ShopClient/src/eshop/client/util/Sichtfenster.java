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

public abstract class Sichtfenster extends JPanel {

	/**
	 * 
	 */
	private static final long			serialVersionUID		= 8136926280757449267L;
	protected JPanel						actionField				= new JPanel(new MigLayout("align 50% 50%"));
	protected JButton						alleButton				= new JButton("Alle");
	protected JXTable						auflistung				= new JXTable();
	protected JScrollPane				auflistungContainer	= new JScrollPane(auflistung);
	protected SichtfensterCallbacks	listener					= null;
	protected JPanel						overviewButtons		= new JPanel();
	protected ShopRemote					server;
	protected JButton						sucheButton				= new JButton("Suche");
	protected JTextField					sucheField				= new JTextField("Bezeichnung", 30);
	protected JTextField					sucheField2				= new JTextField("Artikel Nr.", 30);
	protected JTextField					sucheField3				= new JTextField("Einheit", 30);
	protected Person						user;

	public Sichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		this.listener = listener;
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
		
		sucheField.setHorizontalAlignment(JTextField.CENTER);
		sucheField2.setHorizontalAlignment(JTextField.CENTER);
		sucheField3.setHorizontalAlignment(JTextField.CENTER);

		sucheField.addFocusListener(new FocusListener(){
			String text;
	        @Override
	        public void focusGained(FocusEvent e){
	        	text = sucheField.getText();
	        	sucheField.setText("");
	        }

	        
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(sucheField.getText().equals("")){
					sucheField.setText("Bezeichnung");
				}
				
			}
	    });
		
		sucheField2.addFocusListener(new FocusListener(){
			String text;
	        @Override
	        public void focusGained(FocusEvent e){
	        	text = sucheField2.getText();
	        	sucheField2.setText("");
	        }

	        
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(sucheField2.getText().equals("")){
					sucheField2.setText("Artikel Nr.");
				}
				
			}
	    });
		
		sucheField3.addFocusListener(new FocusListener(){
			String text;
	        @Override
	        public void focusGained(FocusEvent e){
	        	text = sucheField3.getText();
	        	sucheField3.setText("");
	        }

	        
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(sucheField3.getText().equals("")){
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

	public abstract void callTableUpdate();

	public void fitTableLayout() {

		TableColumnAdjuster tca = new TableColumnAdjuster(auflistung);
		tca.adjustColumns(SwingConstants.CENTER);
		int columnWidth = auflistung.getWidth() / auflistung.getColumnCount();
		for (int i = 0; i < auflistung.getColumnCount(); i++) {
			auflistung.getColumn(i).setWidth(columnWidth);
		}
		auflistung.setSortOrder(0, SortOrder.ASCENDING);
	}

	public void TabelleFiltern() {

		Filter[] filterArray = {new PatternFilter(".*" + sucheField.getText() + ".*", Pattern.CASE_INSENSITIVE, 1)};
		FilterPipeline filters = new FilterPipeline(filterArray);
		auflistung.setFilters(filters);
	}

	public void TabellenFilterEntfernen() {

		auflistung.setFilters(null);
	}

	public interface SichtfensterCallbacks {

		void artikelAnzeigen(Artikel art);

		void kundeAnzeigen(Kunde ku);

		void mitarbeiterAnzeigen(Mitarbeiter mi);
	}
}
