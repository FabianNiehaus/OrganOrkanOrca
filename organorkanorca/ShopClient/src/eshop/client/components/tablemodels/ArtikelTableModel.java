package eshop.client.components.tablemodels;

import java.util.Vector;

import eshop.client.util.ShopTableModel;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;

public class ArtikelTableModel extends ShopTableModel {

	public ArtikelTableModel(Vector<Artikel> dataVector) {
		
		columnNames = new String[]{ "ArtNr.", "Bezeichnung", "Preis", "Einheit", "Bestand" };
		data = new Object[dataVector.size()][5];
		
		int i = 0;
		
		for (Artikel art : dataVector) {
			data[i][0] = art.getArtikelnummer();
			data[i][1] = art.getBezeichnung();
			data[i][2] = art.getPreis();
			if (art instanceof Massengutartikel) {
				data[i][3] = ((Massengutartikel) art).getPackungsgroesse();
			} else {
				data[i][3] = 1;
			}
			data[i][4] = art.getBestand();
			i++;
		}
		
	}

}
