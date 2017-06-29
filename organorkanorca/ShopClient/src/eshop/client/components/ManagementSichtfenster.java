package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;

import eshop.client.util.Sichtfenster;
import eshop.client.util.TableColumnAdjuster;
import eshop.client.util.Sichtfenster.SichtfensterCallbacks;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

class ManagementSichtfenster extends Sichtfenster {

	JButton		   speichernButton     = new JButton("Bestandsdaten speichern");
	JButton		   ladenButton	       = new JButton("Bestandsdaten importieren");
	EreignisTableModel etm;
	JScrollPane	   auflistungContainer = new JScrollPane(auflistung);

	public ManagementSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		
		this.remove(aktion);

		speichernButton.addActionListener(new PersistenceButtonListener());
		ladenButton.addActionListener(new PersistenceButtonListener());
		this.add(speichernButton, "dock center");
		this.add(ladenButton, "wrap, dock center");
	}

	public void auflistungInitialize() throws AccessRestrictedException, RemoteException {

		Vector<Vector<Object>> data = new Vector<>();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		for (Ereignis er : server.alleEreignisseAusgeben(user)) {
			Vector<Object> tmp = new Vector<>();
			tmp.addElement(dateFormat.format(er.getWann()));
			tmp.addElement(er.getId());
			tmp.addElement(er.getTyp());
			tmp.addElement(er.getWomit().getArtikelnummer());
			tmp.addElement(er.getWomit().getBezeichnung());
			tmp.addElement(er.getWieviel());
			tmp.addElement(er.getWer().getId());
			tmp.addElement(er.getWer().getFirstname() + " " + er.getWer().getLastname());
			data.addElement(tmp);
		}
		etm = new EreignisTableModel(data);
		auflistung.setModel(etm);
		TableRowSorter<EreignisTableModel> sorter = new TableRowSorter<EreignisTableModel>(etm);
		auflistung.setRowSorter(sorter);
		TableColumnAdjuster tca = new TableColumnAdjuster(auflistung, 30);
		tca.adjustColumns(SwingConstants.CENTER);
	}

	class EreignisTableModel extends AbstractTableModel {

		Vector<String>	   columnIdentifiers;
		Vector<Vector<Object>> dataVector;

		public EreignisTableModel(Vector<Vector<Object>> data) {
			columnIdentifiers = setColumns(
					new String[] { "Datum", "Id", "Aktion", "ArtikelNr", "Artikel", "Anzahl", "UserID", "Name" });
			dataVector = data;
		}

		@Override
		public int getColumnCount() {

			return columnIdentifiers.size();
		}

		@Override
		public String getColumnName(int column) {

			return columnIdentifiers.elementAt(column);
		}

		@Override
		public int getRowCount() {

			return dataVector.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {

			return dataVector.elementAt(arg0).elementAt(arg1);
		}

		public Vector<String> setColumns(String[] columnNames) {

			Vector<String> columns = new Vector<>();
			for (String str : columnNames) {
				columns.addElement(str);
			}
			return columns;
		}
	}

	class PersistenceButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {

			if (ae.getSource().equals(speichernButton)) {
				try {
					server.schreibeDaten();
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, "Daten erfolgreich gespeichert!");
				} catch(IOException e1) {
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
				}
			} else if (ae.getSource().equals(ladenButton)) {
				try{
					server.ladeDaten();
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, "Daten erfolgreich geladen!");
				} catch(IOException | ArticleNonexistantException | PersonNonexistantException | InvalidPersonDataException e1) {
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
				}
			}
		}
	}
	
	@Override
	public void callTableUpdate() {
		try {
			updateTable(server.alleEreignisseAusgeben(user), new String[] { "ArtNr.", "Bezeichnung", "Preis", "Einheit", "Bestand" } );
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(ManagementSichtfenster.this, e.getMessage());
		}
		
	}
}
