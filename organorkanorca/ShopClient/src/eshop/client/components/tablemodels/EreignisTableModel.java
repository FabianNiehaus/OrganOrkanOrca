package eshop.client.components.tablemodels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import eshop.common.data_objects.Ereignis;

public class EreignisTableModel extends ShopTableModel {

	/**
     * 
     */
    private static final long serialVersionUID = -8140938517029973790L;

	public EreignisTableModel(Vector<Ereignis> dataVector) {
		
		columnNames = new String[]{ "Datum", "Event-Nr.", "Aktion", "ArtikelNr.", "ArtikelBez.", "Menge", "ID", "Name" };
		data = new Object[dataVector.size()][8];
		
		int i = 0;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		for (Ereignis er : dataVector) {
			data[i][0] = dateFormat.format(er.getWann());
			data[i][1] = er.getId();
			data[i][2] = er.getTyp();
			data[i][3] = er.getWomit().getArtikelnummer();
			data[i][4] = er.getWomit().getBezeichnung();
			data[i][5] = er.getWieviel();
			data[i][6] = er.getWer().getId();
			data[i][7] = er.getWer().getFirstname() + " " + er.getWer().getLastname();
			i++;
		}
		
	}

}
