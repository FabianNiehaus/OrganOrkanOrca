package eshop.client.components.tablemodels;

import java.util.Map;
import java.util.Map.Entry;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;

public class WarenkorbTableModel extends ShopTableModel {

	/**
	  * 
	  */
	private static final long	serialVersionUID	= 7021676013626973075L;
	protected String[]			columnNames;
	protected Object[][]			data;
	private double gesamtpreis;

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
	
	public double getGesamtpreis(){
		return gesamtpreis;
	}
}
