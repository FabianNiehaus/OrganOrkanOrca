package eshop.client.components;

import java.rmi.RemoteException;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;

import eshop.client.components.tablemodels.PersonenTableModel;
import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;

public class MitarbeiterSichtfenster extends Sichtfenster {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3884938912530629406L;
	private PersonenTableModel	model;

	public MitarbeiterSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks sichtfensterCallbacks) {
		super(server, user, sichtfensterCallbacks, new String[]{"ID","Vorname","Nachname","Straße","Wohnort"});
		auflistung.getSelectionModel().addListSelectionListener(new MitarbeiterAnzeigenListener());
	}

	@Override
	public void callTableUpdate() {

		try {
			model = new PersonenTableModel(server.alleMitarbeiterAusgeben(user));
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					auflistung.setModel(model);
					fitTableLayout();
				}
			});
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e.getMessage());
		}
	}

	class MitarbeiterAnzeigenListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {

			try {
				if (auflistung.getSelectedRow() != -1) sichtfensterCallbacks.mitarbeiterAnzeigen(
						server.mitarbeiterSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
				return;
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e1.getMessage());
			} catch (PersonNonexistantException e1) {
				JOptionPane.showMessageDialog(MitarbeiterSichtfenster.this, e1.getMessage());
			}
		}
	}

	@Override
	public void TabelleFiltern() {

		if(sucheField1.getText().equals(sucheFieldNames[0])){
			sucheField1.setText("");
		}
		if(sucheField2.getText().equals(sucheFieldNames[1])){
			sucheField2.setText("");
		}
		if(sucheField3.getText().equals(sucheFieldNames[2])){
			sucheField3.setText("");
		}
		if(sucheField4.getText().equals(sucheFieldNames[3])){
			sucheField4.setText("");
		}
		if(sucheField5.getText().equals(sucheFieldNames[4])){
			sucheField5.setText("");
		}

		Filter[] filterArray = {new PatternFilter(".*" + sucheField1.getText() + ".*", Pattern.CASE_INSENSITIVE, 0),
				new PatternFilter(".*" + sucheField2.getText() + ".*", Pattern.CASE_INSENSITIVE, 1),
				new PatternFilter(".*" + sucheField3.getText() + ".*", Pattern.CASE_INSENSITIVE, 2),
				new PatternFilter(".*" + sucheField4.getText() + ".*", Pattern.CASE_INSENSITIVE, 3),
				new PatternFilter(".*" + sucheField5.getText() + ".*", Pattern.CASE_INSENSITIVE, 5)};
		FilterPipeline filters = new FilterPipeline(filterArray);
		auflistung.setFilters(filters);
		
	}

	@Override
	public void initializeHighlighting() {

		// TODO Auto-generated method stub
		
	}
}