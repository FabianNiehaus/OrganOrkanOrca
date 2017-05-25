package user;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import data_objects.Artikel;
import data_objects.Kunde;
import data_objects.Massengutartikel;
import data_objects.Mitarbeiter;
import data_objects.Person;
import domain.eShopCore;
import domain.exceptions.AccessRestrictedException;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.LoginFailedException;
import util.IO;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class GUI extends JFrame {
	
	private eShopCore eShop;
	private Person user;
	
	public GUI(String titel) {
		super(titel);
		try {
			eShop = new eShopCore();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			user = eShop.anmelden(9000, "test2");
		} catch (LoginFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	JSplitPane main = new JSplitPane();
	JPanel leftArea = new JPanel();
	JPanel rightArea = new JPanel();
	
	JPanel moduleButtons = new JPanel();
	JButton artikelButton = new JButton("Artikel");
	JButton kundenButton = new JButton("Kunden");
	JButton mitarbeiterButton = new JButton("Mitarbeiter");
	JButton shopButton = new JButton("Shop");
	
	Kundensichtfenster kundensichtfenster;
	Artikelsichtfenster artikelsichtfenster;
	Mitarbeitersichtfenster mitarbeitersichtfenster;
	
	Warenkorbverwaltungsfenster warenkorbverwaltungsfenster;
	Artikelverwaltungsfenster artikelverwaltungsfenster;
		
	public void initialize() {
		
		this.setSize(1024, 512);
		getContentPane().add(main);
		
		artikelButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(artikelButton);
		kundenButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(kundenButton);
		mitarbeiterButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(mitarbeiterButton);
		shopButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(shopButton);
		
		leftArea.add(moduleButtons);
		
		kundensichtfenster = new Kundensichtfenster();
		artikelsichtfenster = new Artikelsichtfenster();
		mitarbeitersichtfenster = new Mitarbeitersichtfenster();
		
		leftArea.add(artikelsichtfenster);
		
		if(user instanceof Kunde){
			rightArea.add(new Warenkorbverwaltungsfenster());
		}

		main.setDividerLocation((int)(this.getWidth()*0.60));
		main.setLeftComponent(leftArea);
		main.setRightComponent(rightArea);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
		
	abstract class Sichtfenster extends JPanel{
		
		JPanel overviewButtons = new JPanel();
		JButton alleButton = new JButton("Alle");
		JButton sortNrButton = new JButton("Sort. Nr.");
		JButton sortBezButton = new JButton("Sort. Bez.");
		JButton sucheButton = new JButton("Suche");
		JTextField sucheField = new JTextField();
		
		JPanel leftAreaActionField = new JPanel();
		JTable auflistung = new JTable();
		JScrollPane auflistungContainer = new JScrollPane(auflistung);
		JButton aktion = new JButton();
		JTextField anzahl = new JTextField();
		
		public Sichtfenster(){
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setPreferredSize(new Dimension((int)(GUI.this.getWidth()*0.55), (int)(GUI.this.getHeight()*0.8)));
			
			overviewButtons.setMaximumSize(new Dimension(1024, 40));
					
			this.add(overviewButtons);
			this.add(auflistungContainer);
			this.add(leftAreaActionField);
						
			overviewButtons.setLayout(new BoxLayout(overviewButtons, BoxLayout.X_AXIS));
			alleButton.addActionListener(new ButtonActionListener());
			overviewButtons.add(alleButton);
			overviewButtons.add(sucheField);
			sucheButton.addActionListener(new ButtonActionListener());
			overviewButtons.add(sucheButton);
			overviewButtons.setVisible(true);
			
			JTableHeader header = auflistung.getTableHeader();
			header.setUpdateTableInRealTime(true);
			header.setReorderingAllowed(false);
			auflistung.setAutoCreateRowSorter(true);

			leftAreaActionField.add(aktion);
			leftAreaActionField.add(anzahl);
			
			try {
				auflistungInitialize();
			} catch (AccessRestrictedException e) {
				removeAll();
				add(new JLabel(e.getMessage()));
			}

		}
		
		public abstract void auflistungInitialize() throws AccessRestrictedException;
		
		public abstract void suche(String suchStr);
		
//		public abstract Vector<Vector<Object>> createTableData(Vector<Object> daten);

		class ButtonActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent ae) {
				
				if (ae.getSource().equals(sucheButton)){
					suche(sucheField.getText());
				}
			}
			
		}
	}
	
	class eShopTableModel extends AbstractTableModel{
		
		Vector<String> columnIdentifiers;
		Vector<Vector<Object>> dataVector;
		
		public eShopTableModel(Vector<Vector<Object>> data, String[] columnNames) {
			
			columnIdentifiers = setColumns(columnNames);
			dataVector = data;
		}
		
		public Vector<String> setColumns(String[] columnNames){
			Vector<String> columns = new Vector<>();
			
			for(String str : columnNames){
				columns.addElement(str);
			}
			
			return columns;
		}

		@Override
		public int getColumnCount() {
			return columnIdentifiers.size();
		}

		@Override
		public int getRowCount() {
			return dataVector.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			return dataVector.elementAt(arg0).elementAt(arg1);
		}
		
		@Override
		public String getColumnName(int column) {
			  return columnIdentifiers.elementAt(column);
		}
		
	}
	
	class Artikelsichtfenster extends Sichtfenster{

		eShopTableModel etm;
		
		public Artikelsichtfenster(){
			super();
			if (user instanceof Kunde){
				aktion.setText("In Warenkorb");
				aktion.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
					}
				});
			} else if (user instanceof Mitarbeiter){
				aktion.setText("Bearbeiten");
				aktion.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
					}
				});
			}
		}
				
		@Override
		public void auflistungInitialize() throws AccessRestrictedException {
			
				
			Vector<Vector<Object>> data = new Vector<>();
			
			for(Artikel art : eShop.alleArtikelAusgeben(user)){
				
				Vector<Object> tmp = new Vector<>();
				
				tmp.addElement(art.getArtikelnummer());
				tmp.addElement(art.getBezeichnung());
				tmp.addElement(art.getPreis());
				tmp.addElement(((Massengutartikel)art).getPackungsgroesse());
				tmp.addElement(art.getBestand());
				
				data.addElement(tmp);
			}
				
			
			etm = new eShopTableModel(data, new String[]{"Artikelnummer","Bezeichnung","Preis","Packungsgröße","Bestand"});
			auflistung.setModel(etm);
		}

		@Override
		public void suche(String suchStr) {
			/*
			try {
				int suchInt = Integer.parseInt(suchStr);
				try {
					Artikel art = eShop.artikelSuchen(suchInt, user);
					auflistung.setText(art.toString());
				} catch (ArticleNonexistantException ane) {
					// TODO Exception-Handling
				} catch (AccessRestrictedException are){
					// TODO Exception-Handling
				}
					
			} catch (NumberFormatException e) {
				try {
					Vector<Artikel> art = eShop.artikelSuchen(suchStr, user);
					auflistung.setText("");
					for (Artikel artikel : art) {
						auflistung.append(artikel.toString() + "\n");
					}
				} catch (ArticleNonexistantException ane) {
					// TODO Exception-Handling
				} catch (AccessRestrictedException are){
					// TODO Exception-Handling
				}
			}
			*/
		}
	}
	
	class Kundensichtfenster extends Sichtfenster{
		
		eShopTableModel etm;
		
		public Kundensichtfenster(){
			super();
			aktion.setText("Bearbeiten");
		}
				
		@Override
		public void auflistungInitialize() throws AccessRestrictedException {
			
				
			Vector<Vector<Object>> data = new Vector<>();
			
			for(Kunde ku : eShop.alleKundenAusgeben(user)){
				
				Vector<Object> tmp = new Vector<>();
				
				tmp.addElement(ku.getId());
				tmp.addElement(ku.getFirstname());
				tmp.addElement(ku.getLastname());
				tmp.addElement(ku.getAddress_Street());
				tmp.addElement(ku.getAddress_Zip());
				tmp.addElement(ku.getAddress_Town());
				
				data.addElement(tmp);
			}
				
			
			etm = new eShopTableModel(data, new String[]{"Kundennummer","Vorname","Nachname","Straße","PLZ","Ort"});
			auflistung.setModel(etm);
		}

		@Override
		public void suche(String suchStr) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class Mitarbeitersichtfenster extends Sichtfenster{
		
		eShopTableModel etm;
		
		public Mitarbeitersichtfenster(){
			super();
			aktion.setText("Bearbeiten");
		}
				
		@Override
		public void auflistungInitialize() throws AccessRestrictedException {
			
				
			Vector<Vector<Object>> data = new Vector<>();
			
			for(Mitarbeiter mi : eShop.alleMitarbeiterAusgeben(user)){
				
				Vector<Object> tmp = new Vector<>();
				
				tmp.addElement(mi.getId());
				tmp.addElement(mi.getFirstname());
				tmp.addElement(mi.getLastname());
				
				data.addElement(tmp);
			}
				
			
			etm = new eShopTableModel(data, new String[]{"Mitarbeiternummer","Vorname","Nachname"});
			auflistung.setModel(etm);
		}

		@Override
		public void suche(String suchStr) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Shopstatistics extends JPanel{
		
		public Shopstatistics(){

		}
	}
	
	class Artikelverwaltungsfenster extends JPanel{
		
		JPanel detailArea = new JPanel();
		
		JLabel artNrLabel = new JLabel("Artikelnummer:");
		JTextField artNrField = new JTextField();
		JLabel bezeichnungLabel = new JLabel("Bezeichnung:");
		JTextField bezeichnungField = new JTextField();
		JLabel preisLabel = new JLabel("Preis:");
		JTextField preisField = new JTextField();
		JLabel pkggroesseLabel = new JLabel("Packungsgröße:");
		JTextField pkggroesseField = new JTextField();
		JLabel bestandLabel = new JLabel("Bestand:");
		JTextField bestandField = new JTextField();
		
		JPanel buttons = new JPanel();
		
		JButton neuAnlegenButton = new JButton("Neu");
		JButton aendernButton = new JButton("Ändern");
		JButton bestandAendernButton = new JButton("Bestand");
		
		public Artikelverwaltungsfenster(){
			
			this.add(new JLabel("Artikelverwaltung"));
			
			detailArea.add(artNrLabel);
			detailArea.add(artNrField);
			detailArea.add(bezeichnungLabel);
			detailArea.add(bezeichnungField);
			detailArea.add(preisLabel);
			detailArea.add(preisField);
			detailArea.add(pkggroesseLabel);
			detailArea.add(pkggroesseField);
			detailArea.add(bestandLabel);
			detailArea.add(bestandField);
			
			this.add(detailArea);
			
			buttons.add(neuAnlegenButton);
			buttons.add(aendernButton);
			buttons.add(bestandAendernButton);
			
			this.add(buttons);
			
			detailArea.setLayout(new GridLayout(5,2));
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			this.setVisible(true);
		}	
	}
	
	class Warenkorbverwaltungsfenster extends JPanel{
		
		JTextArea detailArea = new JTextArea();
		
		JPanel buttons = new JPanel();
		
		JButton neuAnlegenButton = new JButton("Neu");
		JButton aendernButton = new JButton("Ändern");
		JButton bestandAendernButton = new JButton("Bestand");
		
		public Warenkorbverwaltungsfenster(){
			
			this.add(new JLabel("Warenkorbverwaltung"));
			
			this.add(detailArea);
			
			buttons.add(neuAnlegenButton);
			buttons.add(aendernButton);
			buttons.add(bestandAendernButton);
			
			this.add(buttons);
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			this.setVisible(true);
		}
	}
	
	class MenuButtonsActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(artikelButton)){
				leftArea.remove(leftArea.getComponent(1));
				leftArea.add(artikelsichtfenster);
				leftArea.revalidate();
				leftArea.repaint();
			} else if (ae.getSource().equals(kundenButton)) {
				leftArea.remove(leftArea.getComponent(1));
				leftArea.add(kundensichtfenster);
				leftArea.revalidate();
				leftArea.repaint();
			} else if (ae.getSource().equals(mitarbeiterButton)) {
				leftArea.remove(leftArea.getComponent(1));
				leftArea.add(mitarbeitersichtfenster);
				leftArea.revalidate();
				leftArea.repaint();
			} else if (ae.getSource().equals(shopButton)) {
				leftArea.remove(leftArea.getComponent(1));
				leftArea.add(new Shopstatistics());
				leftArea.revalidate();
				leftArea.repaint();
			}
			
		}
		
	}
	
	
	
	public static void main(String[] args){
		GUI gui = new GUI("OrganOrkanOrca eShop");
		gui.initialize();
		gui.setVisible(true);
	}
	
}
