package eshop.client.util;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Person;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public abstract class Sichtfenster extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 8136926280757449267L;

    protected SichtfensterCallbacks listener		= null;

    protected Person		    user;
    protected ShopRemote	    server;
    protected ShopTableModel	    shoptablemodel;
    protected JPanel		    overviewButtons	= new JPanel();
    protected JButton		    alleButton		= new JButton("Alle");
    protected JButton		    sucheButton		= new JButton("Suche");
    protected JTextField	    sucheField		= new JTextField(30);
    protected JPanel		    actionField		= new JPanel(new MigLayout("align 50% 50%"));
    protected JXTable		    auflistung		= new JXTable();
    protected JScrollPane	    auflistungContainer	= new JScrollPane(auflistung);
    protected JButton		    aktion		= new JButton();
    protected JTextField	    anzahl		= new JTextField(5);
    public Sichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
	this.listener = listener;
	this.user = user;
	this.server = server;
	this.setLayout(new MigLayout("fillx, align 50% 50%"));
	// overviewButtons.setMaximumSize(new Dimension(1024, 40));
	this.add(overviewButtons, "wrap, w 100%");
	this.add(auflistungContainer, "wrap");
	this.add(actionField, "w 100%");
	overviewButtons.setLayout(new BoxLayout(overviewButtons, BoxLayout.X_AXIS));
	alleButton.addActionListener(new TabelleAlleAnzeigenListener());
	overviewButtons.add(alleButton);
	overviewButtons.add(sucheField);
	sucheButton.addActionListener(new TabelleFilternListener());
	overviewButtons.add(sucheButton);
	overviewButtons.setVisible(true);
	JTableHeader header = auflistung.getTableHeader();
	header.setUpdateTableInRealTime(true);
	header.setReorderingAllowed(false);
	actionField.add(aktion);
	actionField.add(anzahl);
	auflistung.setHorizontalScrollEnabled(true);
	callTableUpdate();
    }

    public abstract void callTableUpdate();

    protected void updateTable(Vector<?> dataVector, String[] headerString) throws RemoteException {

	shoptablemodel = new ShopTableModel(dataVector, headerString);
	auflistung.setModel(shoptablemodel);
	TableRowSorter<ShopTableModel> sorter = new TableRowSorter<ShopTableModel>(shoptablemodel);
	auflistung.setRowSorter(sorter);
	TableColumnAdjuster tca = new TableColumnAdjuster(auflistung, 30);
	tca.adjustColumns(SwingConstants.CENTER);
    }

    public interface SichtfensterCallbacks {

	public void alleFensterErneuern();

	public void artikelBearbeiten(Artikel art);

	public void artikelInWarenkorb();

	public void kundeBearbeiten();

	public void mitarbeiterBearbeiten();
    }

    class TabelleAlleAnzeigenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    TableRowSorter<ShopTableModel> sorter = (TableRowSorter<ShopTableModel>) auflistung.getRowSorter();
	    sorter.setRowFilter(null);
	}
    }

    class TabelleFilternListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {

	    TableRowSorter<ShopTableModel> sorter = (TableRowSorter<ShopTableModel>) auflistung.getRowSorter();
	    sorter.setRowFilter(RowFilter.regexFilter(sucheField.getText()));
	    sorter.setSortsOnUpdates(true);
	}
    }
}
