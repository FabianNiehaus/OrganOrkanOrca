package eshop.client.components.tablemodels;

import java.util.Vector;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;

// TODO: Auto-generated Javadoc
/**
 * The Class ArtikelTableModel.
 */
public class ArtikelTableModel extends ShopTableModel {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 7021676013626973075L;
	
	/** The column names. */
	protected String[]			columnNames;
	
	/** The data. */
	protected Object[][]			data;

	/**
	 * Instantiates a new artikel table model.
	 *
	 * @param dataVector
	 *           the data vector
	 */
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

	/*
	 * (non-Javadoc)
	 * @see eshop.client.components.tablemodels.ShopTableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {

		return columnNames.length;
	}

	/*
	 * (non-Javadoc)
	 * @see eshop.client.components.tablemodels.ShopTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {

		return columnNames[column];
	}

	/*
	 * (non-Javadoc)
	 * @see eshop.client.components.tablemodels.ShopTableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {

		return data.length;
	}

	/*
	 * (non-Javadoc)
	 * @see eshop.client.components.tablemodels.ShopTableModel#getValueAt(int,
	 * int)
	 */
	@Override
	public Object getValueAt(int arg0, int arg1) {

		return data[arg0][arg1];
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * eshop.client.components.tablemodels.ShopTableModel#setValueAt(java.lang.
	 * Object, int, int)
	 */
	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {

		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}
