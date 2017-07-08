package eshop.client.components.tablemodels;

import javax.swing.table.AbstractTableModel;

public class ShopTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3784279748338157614L;
	
	protected String[] columnNames;
	protected Object[][] data;

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
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

}