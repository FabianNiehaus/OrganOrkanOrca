package eshop.client.components.tablemodels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import eshop.common.data_objects.Ereignis;

// TODO: Auto-generated Javadoc
/**
 * The Class EreignisTableModel.
 */
public class EreignisTableModel extends ShopTableModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8140938517029973790L;

	/**
	 * Instantiates a new ereignis table model.
	 *
	 * @param dataVector
	 *           the data vector
	 */
	public EreignisTableModel(Vector<Ereignis> dataVector) {
		columnNames = new String[] {"Datum", "Event-Nr.", "Aktion", "ArtikelNr.", "ArtikelBez.", "Menge", "ID", "Name"};
		if (dataVector.elementAt(0) != null) {
			data = new Object[dataVector.size()][8];
			int i = 0;
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			for (Ereignis er : dataVector) {
				System.out.println(i);
				data[i][0] = dateFormat.format(er.getWann());
				data[i][1] = er.getId();
				data[i][2] = er.getTyp();
				data[i][3] = er.getWomit_Nr();
				data[i][4] = er.getWomit_Bezeichnung();
				data[i][5] = er.getWieviel();
				data[i][6] = er.getWer_Id();
				data[i][7] = er.getWer_Name();
				i++;
			}
		}
	}
}
