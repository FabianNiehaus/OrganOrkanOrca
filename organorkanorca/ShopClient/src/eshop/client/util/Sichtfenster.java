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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Person;
import eshop.common.net.ShopRemote;

public abstract class Sichtfenster extends JPanel {

    public interface SichtfensterCallbacks {

	public void artikelInWarenkorb();

	public void artikelBearbeiten(Artikel art);

	public void kundeBearbeiten();

	public void mitarbeiterBearbeiten();

	public void alleSichtfensterErneuern();
    }

    protected SichtfensterCallbacks listener		= null;
    protected Person		    user;
    protected ShopRemote	    server;
    protected ShopTableModel	    shoptablemodel;
    protected JPanel		    overviewButtons	= new JPanel();
    protected JButton		    alleButton		= new JButton("Alle");
    protected JButton		    sucheButton		= new JButton("Suche");
    protected JTextField	    sucheField		= new JTextField();
    protected JPanel		    leftAreaActionField	= new JPanel();
    protected JTable		    auflistung		= new JTable();
    protected JScrollPane	    auflistungContainer	= new JScrollPane(auflistung);
    protected JButton		    aktion		= new JButton();
    protected JTextField	    anzahl		= new JTextField(5);

    public Sichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
	this.listener = listener;
	this.user = user;
	this.server = server;
	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	overviewButtons.setMaximumSize(new Dimension(1024, 40));
	this.add(overviewButtons);
	this.add(auflistungContainer);
	this.add(leftAreaActionField);
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
	leftAreaActionField.add(aktion);
	leftAreaActionField.add(anzahl);
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
