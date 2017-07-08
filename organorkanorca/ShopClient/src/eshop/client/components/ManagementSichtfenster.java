package eshop.client.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.TableRowSorter;

import eshop.client.components.tablemodels.EreignisTableModel;
import eshop.client.components.tablemodels.PersonenTableModel;
import eshop.client.util.Sichtfenster;
import eshop.client.util.TableColumnAdjuster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

public class ManagementSichtfenster extends Sichtfenster {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4495211746151221729L;
	JButton speichernButton = new JButton("Bestandsdaten speichern");
	JButton ladenButton = new JButton("Bestandsdaten importieren");

	public ManagementSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
		super(server, user, listener);
		actionField.remove(aktion);
		actionField.remove(anzahl);
		speichernButton.addActionListener(new PersistenceButtonListener());
		ladenButton.addActionListener(new PersistenceButtonListener());
		actionField.add(speichernButton);
		actionField.add(ladenButton);
	}

	class PersistenceButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {

			if (ae.getSource().equals(speichernButton)) {
				try {
					server.schreibeDaten();
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, "Daten erfolgreich gespeichert!");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
				}
			} else if (ae.getSource().equals(ladenButton)) {
				try {
					server.ladeDaten();
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, "Daten erfolgreich geladen!");
				} catch (IOException | ArticleNonexistantException | PersonNonexistantException
						| InvalidPersonDataException e1) {
					JOptionPane.showMessageDialog(ManagementSichtfenster.this, e1.getMessage());
				}
			}
		}
	}

	@Override
	public void callTableUpdate() {

		try {
			model = new EreignisTableModel(server.alleEreignisseAusgeben(user));
			
			auflistung.setModel(model);
			
			auflistung.setPreferredScrollableViewportSize(new Dimension(500, 70));
			auflistung.setFillsViewportHeight(true);
			
			TableColumnAdjuster tca = new TableColumnAdjuster(auflistung, 30);
			tca.adjustColumns(SwingConstants.CENTER);
			model.fireTableDataChanged();
			
			auflistung.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(ManagementSichtfenster.this, e.getMessage());
		}
	}
	
	@Override
	public void TabelleFiltern() {
		RowFilter<EreignisTableModel,Object> rf = null;
		try {
            rf = RowFilter.regexFilter(sucheField.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        ((DefaultRowSorter<EreignisTableModel, Integer>) auflistung.getRowSorter()).setRowFilter(rf);
	}

	@Override
	public void TabellenFilterEntfernen() {
		((DefaultRowSorter<EreignisTableModel, Integer>) auflistung.getRowSorter()).setRowFilter(null);
		
	}
}
