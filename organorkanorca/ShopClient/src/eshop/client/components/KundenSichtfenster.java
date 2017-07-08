package eshop.client.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.DefaultRowSorter;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import eshop.client.components.tablemodels.PersonenTableModel;
import eshop.client.util.Sichtfenster;
import eshop.client.util.TableColumnAdjuster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

public class KundenSichtfenster extends Sichtfenster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4821072292018595904L;

	public KundenSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		aktion.setText("Bearbeiten");
		aktion.addActionListener(new KundeBearbeitenListener());
		anzahl.setVisible(false);
	}

	class KundeBearbeitenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				listener.kundeBearbeiten(
						server.kundeSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(KundenSichtfenster.this, e1.getMessage());
			} catch (PersonNonexistantException e1) {
				JOptionPane.showMessageDialog(KundenSichtfenster.this, e1.getMessage());
			}
		}
	}

	@Override
	public void callTableUpdate() {

		try {

			model = new PersonenTableModel(server.alleKundenAusgeben(user));

			auflistung.setModel(model);

			auflistung.setPreferredScrollableViewportSize(new Dimension(500, 70));
			auflistung.setFillsViewportHeight(true);

			TableColumnAdjuster tca = new TableColumnAdjuster(auflistung, 30);
			tca.adjustColumns(SwingConstants.CENTER);
			model.fireTableDataChanged();

			auflistung.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(KundenSichtfenster.this, e.getMessage());
		}
	}

	@Override
	public void TabelleFiltern() {
		RowFilter<PersonenTableModel,Object> rf = null;
		try {
            rf = RowFilter.regexFilter(sucheField.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        ((DefaultRowSorter<PersonenTableModel, Integer>) auflistung.getRowSorter()).setRowFilter(rf);
	}

	@Override
	public void TabellenFilterEntfernen() {
		((DefaultRowSorter<PersonenTableModel, Integer>) auflistung.getRowSorter()).setRowFilter(null);
		
	}
	
}
