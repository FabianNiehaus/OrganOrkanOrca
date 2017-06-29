package eshop.client.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;

public class ShopTableModel extends AbstractTableModel {

	Vector<String>	       columnIdentifiers;
	Vector<Vector<Object>> tableData;

	public <T> ShopTableModel(Vector<T> dataVector, String[] columnNames) {

		if(dataVector.elementAt(0) instanceof Artikel){
			for (Object obj : dataVector) {
				Artikel art = (Artikel) obj;
				Vector<Object> tmp = new Vector<>();
				tmp.addElement(art.getArtikelnummer());
				tmp.addElement(art.getBezeichnung());
				tmp.addElement(art.getPreis());
				if (art instanceof Massengutartikel) {
					tmp.addElement(((Massengutartikel) art).getPackungsgroesse());
				} else {
					tmp.addElement(1);
				}
				tmp.addElement(art.getBestand());
				tableData.addElement(tmp);
			}
		} else if(dataVector.elementAt(0) instanceof Kunde){
			for (Object obj : dataVector) {
				Kunde ku = (Kunde) obj;
				Vector<Object> tmp = new Vector<>();
				tmp.addElement(ku.getId());
				tmp.addElement(ku.getFirstname());
				tmp.addElement(ku.getLastname());
				tmp.addElement(ku.getAddress_Street());
				tmp.addElement(ku.getAddress_Zip());
				tmp.addElement(ku.getAddress_Town());
				tableData.addElement(tmp);
			}
		} else if (dataVector.elementAt(0) instanceof Mitarbeiter){
			for (Object obj : dataVector) {
				Mitarbeiter mi = (Mitarbeiter) obj;
				Vector<Object> tmp = new Vector<>();
				tmp.addElement(mi.getId());
				tmp.addElement(mi.getFirstname());
				tmp.addElement(mi.getLastname());
				tmp.addElement(mi.getAddress_Street());
				tmp.addElement(mi.getAddress_Zip());
				tmp.addElement(mi.getAddress_Town());
				tableData.addElement(tmp);
			}
		} else if(dataVector.elementAt(0) instanceof Ereignis){
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			for (Object obj : dataVector) {
				Ereignis er = (Ereignis) obj;
				Vector<Object> tmp = new Vector<>();
				tmp.addElement(dateFormat.format(er.getWann()));
				tmp.addElement(er.getId());
				tmp.addElement(er.getTyp());
				tmp.addElement(er.getWomit().getArtikelnummer());
				tmp.addElement(er.getWomit().getBezeichnung());
				tmp.addElement(er.getWieviel());
				tmp.addElement(er.getWer().getId());
				tmp.addElement(er.getWer().getFirstname() + " " + er.getWer().getLastname());
				tableData.addElement(tmp);
			}
		}
		
		

	}




	@Override
	public int getColumnCount() {

		return columnIdentifiers.size();
	}

	@Override
	public String getColumnName(int column) {

		return columnIdentifiers.elementAt(column);
	}

	@Override
	public int getRowCount() {

		return tableData.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {

		return tableData.elementAt(arg0).elementAt(arg1);
	}

	public Vector<String> setColumns(String[] columnNames) {

		Vector<String> columns = new Vector<>();
		for (String str : columnNames) {
			columns.addElement(str);
		}
		return columns;
	}
}