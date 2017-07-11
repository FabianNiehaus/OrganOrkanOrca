package eshop.client.components.tablemodels;

import java.util.Map;
import java.util.Map.Entry;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;

// TODO: Auto-generated Javadoc
/**
 * The Class WarenkorbTableModel.
 */
public class WarenkorbTableModel extends ShopTableModel {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 7021676013626973075L;
	
	/** The column names. */
	protected String[]			columnNames;
	
	/** The data. */
	protected Object[][]			data;
	
	/** The gesamtpreis. */
	private double					gesamtpreis;

	/**
	 * Instantiates a new warenkorb table model.
	 *
	 * @param dataMap
	 *           the data map
	 */
	public WarenkorbTableModel(Map<Artikel, Integer> dataMap) {
		columnNames = new String[] {"ArtNr.", "Bezeichnung", "Einzelpreis", "Im Warenkorb", "Gesamtpreis", "Einheit",
				"Bestand"};
		data = new Object[dataMap.size()][7];
		gesamtpreis = 0;
		int i = 0;
		for (Entry<Artikel, Integer> ent : dataMap.entrySet()) {
			Artikel art = ent.getKey();
			data[i][0] = art.getArtikelnummer();
			data[i][1] = art.getBezeichnung();
			data[i][2] = art.getPreis();
			data[i][3] = ent.getValue();
			data[i][4] = art.getPreis() * ent.getValue();
			gesamtpreis += art.getPreis() * ent.getValue();
			if (art instanceof Massengutartikel) {
				data[i][5] = ((Massengutartikel) art).getPackungsgroesse();
			} else {
				data[i][5] = 1;
			}
			data[i][6] = art.getBestand();
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

	/**
	 * Gets the gesamtpreis.
	 *
	 * @return the gesamtpreis
	 */
	public double getGesamtpreis() {

		return gesamtpreis;
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
