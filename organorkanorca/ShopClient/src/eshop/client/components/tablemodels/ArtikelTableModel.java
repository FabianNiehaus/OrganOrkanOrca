package eshop.client.components.tablemodels;

import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;

public class ArtikelTableModel extends ShopTableModel {

	/**
	  * 
	  */
	private static final long	serialVersionUID	= 7021676013626973075L;
	protected String[]			columnNames;
	protected Object[][]			data;

	public ArtikelTableModel(Vector<Artikel> dataVector) {
		columnNames = new String[] {"ArtNr.", "Bezeichnung", "Preis", "Einheit", "Bestand"};
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

	@Override
	public int getColumnCount() {

		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {

		return columnNames[column];
	}

	@Override
	public int getRowCount() {

		return data.length;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {

		return data[arg0][arg1];
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {

		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}
