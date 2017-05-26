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
import data_objects.Warenkorb;
import domain.eShopCore;
import domain.exceptions.AccessRestrictedException;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.ArticleStockNotSufficientException;
import domain.exceptions.InvalidAmountException;
import domain.exceptions.LoginFailedException;
import net.miginfocom.swing.MigLayout;
import util.IO;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class GUI {
	
	private eShopCore eShop;
	private Person user;
	LoginWindow loginwindow;
	MainWindow mainwindow;
	
	public GUI() {
		try {
			eShop = new eShopCore();
			
			loginwindow = new LoginWindow("OrganOrkanOrca eShop");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class LoginButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				user = eShop.anmelden(loginwindow.benutzerIdAuslesen(), loginwindow.passwortAuslesen());
				mainwindow = new MainWindow();
				loginwindow.dispose();		
			} catch (NumberFormatException | LoginFailedException e1) {
				JOptionPane.showMessageDialog(loginwindow, "Anmeldung fehlgeschlagen");
			}
		}
	}
	
	private class LoginWindow extends JFrame {
		
		JLabel benutzerLabel = new JLabel("Benutzer");
		JLabel passwortLabel = new JLabel("Passwort");
		
		JTextField benutzerField = new JTextField(10);
		JPasswordField passwortField = new JPasswordField(10);
		
		JLabel headerLabel = new JLabel("Willkommen bei OrganOrkanOrca");
		
		JButton anmeldenButton = new JButton("Login");
		
		public LoginWindow(String titel){
			super(titel);
			
			this.setSize(256,192);
			
			Container form = this.getContentPane();
			
			form.setLayout(new MigLayout());
			
			form.add(headerLabel, "span 2, wrap");
			form.add(benutzerLabel);
			form.add(benutzerField, "wrap");
			form.add(passwortLabel);
			form.add(passwortField, "wrap");
			form.add(anmeldenButton);
			
			anmeldenButton.addActionListener(new LoginButtonListener());
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			this.setLocationRelativeTo(null);
			
			this.setVisible(true);
		}
		
		public int benutzerIdAuslesen() throws NumberFormatException{
			return Integer.parseInt(benutzerField.getText());
		}
		
		public String passwortAuslesen(){
			char[] pw = passwortField.getPassword();
			passwortField.setText("");
			return new String(pw);
		}
	}
	
	private class MainWindow extends JFrame {
		
		public MainWindow(){
			initialize();
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
				warenkorbverwaltungsfenster = new Warenkorbverwaltungsfenster();
				rightArea.add(warenkorbverwaltungsfenster);
			} else {
				artikelverwaltungsfenster = new Artikelverwaltungsfenster();
				rightArea.add(artikelverwaltungsfenster);
			}
	
			main.setDividerLocation((int)(this.getWidth()*0.60));
			main.setLeftComponent(leftArea);
			main.setRightComponent(rightArea);
	
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);
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
			JTextField anzahl = new JTextField(5);
			
			public Sichtfenster(){
				
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.setPreferredSize(new Dimension((int)(GUI.MainWindow.this.getWidth()*0.55), (int)(GUI.MainWindow.this.getHeight()*0.8)));
				
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
	
			class ButtonActionListener implements ActionListener {
	
				@Override
				public void actionPerformed(ActionEvent ae) {
					
					if (ae.getSource().equals(sucheButton)){
						suche(sucheField.getText());
					}
				}
				
			}
			
			class ArtikelInWarenkorbListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {

					try {
						Artikel art = eShop.artikelSuchen((int)auflistung.getValueAt(auflistung.getSelectedRow(),0), user);
						
						eShop.artikelInWarenkorbLegen(art.getArtikelnummer(),Integer.parseInt(anzahl.getText()), user);
						
						warenkorbverwaltungsfenster.warenkorbAufrufen();
						
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(Sichtfenster.this, "Keine gültige Anzahl!");
					} catch (ArticleNonexistantException e1) {
						JOptionPane.showMessageDialog(Sichtfenster.this, e1.getMessage());
					} catch (ArticleStockNotSufficientException e1) {
						JOptionPane.showMessageDialog(Sichtfenster.this, e1.getMessage());
					} catch (AccessRestrictedException e1) {
						JOptionPane.showMessageDialog(Sichtfenster.this, e1.getMessage());
					} catch (InvalidAmountException e1) {
						JOptionPane.showMessageDialog(Sichtfenster.this, e1.getMessage());
					}
				}
			}
			
			class ArtikelBearbeitenListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						artikelverwaltungsfenster.artikelAnzeigen(eShop.artikelSuchen((int)auflistung.getValueAt(auflistung.getSelectedRow(),0), user));
					} catch (ArticleNonexistantException e1) {
						JOptionPane.showMessageDialog(Sichtfenster.this, e1.getMessage());
					} catch (AccessRestrictedException e1) {
						JOptionPane.showMessageDialog(Sichtfenster.this, e1.getMessage());
					}
				}
				
			}
		}
		
		class Artikelsichtfenster extends Sichtfenster{
	
			eShopTableModel etm;
			
			public Artikelsichtfenster(){
				super();
				if (user instanceof Kunde){
					aktion.setText("In Warenkorb");
					aktion.addActionListener(new ArtikelInWarenkorbListener());
					anzahl.setVisible(true);
				} else if (user instanceof Mitarbeiter){
					aktion.setText("Bearbeiten");
					aktion.addActionListener(new ArtikelBearbeitenListener());
					anzahl.setVisible(false);
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
					if(art instanceof Massengutartikel){ 
						tmp.addElement(((Massengutartikel)art).getPackungsgroesse());
					} else {
						tmp.addElement(1);
					}
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
			JButton neuAnlegenBestaetigenButton = new JButton("Anlegen");
			
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
				buttons.add(neuAnlegenBestaetigenButton);
				
				neuAnlegenBestaetigenButton.setVisible(false);
				
				neuAnlegenButton.addActionListener(new ArtikelNeuAnlegenListener());
				neuAnlegenBestaetigenButton.addActionListener(new ArtikelNeuAnlegenListener());
				
				this.add(buttons);
				
				detailArea.setLayout(new GridLayout(5,2));
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
				this.setVisible(true);
			}
			
			public void artikelAnzeigen(Artikel art){
				artNrField.setText(String.valueOf(art.getArtikelnummer()));
				bezeichnungField.setText(art.getBezeichnung());
				preisField.setText(String.valueOf(art.getPreis()));
				if(art instanceof Massengutartikel){
					pkggroesseField.setText(String.valueOf(((Massengutartikel)art).getPackungsgroesse()));
				} else {
					pkggroesseField.setText("1");
				}
				bestandField.setText(String.valueOf(art.getBestand()));
			}
			
			public class ArtikelNeuAnlegenListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(neuAnlegenButton)){
						//Artikelnummer ausblenden, kann nicht neu angegeben werden
						artNrLabel.setVisible(false);
						artNrField.setVisible(false);
						
						//Alle Felder leeren
						bezeichnungField.setText("");
						preisField.setText("");
						pkggroesseField.setText("");
						bestandField.setText("");
						
						//Buttons anpassen
						neuAnlegenButton.setVisible(false);
						aendernButton.setVisible(false);
						bestandAendernButton.setVisible(false);
						neuAnlegenBestaetigenButton.setVisible(true);
						
					} else if (e.getSource().equals(neuAnlegenBestaetigenButton)){
						
						String bezeichnung = bezeichnungField.getText();
						
						try{
							
							int bestand = Integer.parseInt(bestandField.getText());
							
							try{
								
								double preis = Double.parseDouble(preisField.getText());
								
								try{
									
									int packungsgroesse = Integer.parseInt(pkggroesseField.getText());
									
									try {
										
										Artikel art = eShop.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse, user);
										 
										//Neu erstellten  Artikel anzeigen
										artikelAnzeigen(art);
										
										//Artikelnummer wieder anzeigen
										artNrLabel.setVisible(true);
										artNrField.setVisible(true);
										
										//Buttons anpassen
										neuAnlegenButton.setVisible(true);
										aendernButton.setVisible(true);
										bestandAendernButton.setVisible(true);
										neuAnlegenBestaetigenButton.setVisible(false);
										
										artikelsichtfenster.auflistungInitialize();
										
									} catch (AccessRestrictedException e1) {
										JOptionPane.showMessageDialog(Artikelverwaltungsfenster.this, e1.getMessage());
									} catch (InvalidAmountException e1) {
										JOptionPane.showMessageDialog(Artikelverwaltungsfenster.this, e1.getMessage());
									}
	
								} catch(NumberFormatException e1){
									JOptionPane.showMessageDialog(Artikelverwaltungsfenster.this, "Keine gültige Packungsgröße");
								}
								
							} catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(Artikelverwaltungsfenster.this, "Kein gültiger Preis!");
							}
							
						} catch(NumberFormatException e1){
							JOptionPane.showMessageDialog(Artikelverwaltungsfenster.this, "Kein gültiger Bestand!");
						}
					}
				}

			}
		}
				
		class Warenkorbverwaltungsfenster extends JPanel{
			
			JPanel buttons = new JPanel();
			
			JTable warenkorbAuflistung = new JTable();
			JScrollPane warenkorbAuflistungContainer = new JScrollPane(warenkorbAuflistung);
			
			JButton neuAnlegenButton = new JButton("Neu");
			JButton aendernButton = new JButton("Ändern");
			JButton bestandAendernButton = new JButton("Bestand");
			
			public Warenkorbverwaltungsfenster(){
				
				this.add(new JLabel("Warenkorbverwaltung"));
				
				this.add(warenkorbAuflistung);
				
				buttons.add(neuAnlegenButton);
				buttons.add(aendernButton);
				buttons.add(bestandAendernButton);
				
				this.add(buttons);
				
				JTableHeader header = warenkorbAuflistung.getTableHeader();
				header.setUpdateTableInRealTime(true);
				header.setReorderingAllowed(false);
				warenkorbAuflistung.setAutoCreateRowSorter(true);
				
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
				this.setVisible(true);
			}
			
			public void warenkorbAufrufen() throws AccessRestrictedException{
				Warenkorb wk = eShop.warenkorbAusgeben(user);
				Map<Artikel,Integer> inhalt = wk.getArtikel();
				warenkorbAuflistung.setModel(new WarenkorbTableModel(inhalt));
			}
			
			class WarenkorbTableModel extends AbstractTableModel{
				
				String[] columnIdentifiers = {"Artikel","Preis","Menge","Gesamt"};
				Vector<Vector<Object>> dataVector = new Vector<>(0);
				
				public WarenkorbTableModel(Map<Artikel,Integer> inhalt) {
					
					
					for(Map.Entry<Artikel, Integer> ent : inhalt.entrySet()){
						Vector<Object> tmp = new Vector<>(0);
						
						tmp.addElement(ent.getKey().getBezeichnung());
						tmp.addElement(ent.getKey().getPreis());
						tmp.addElement(ent.getValue());
						tmp.addElement(ent.getKey().getPreis() * ent.getValue());
						
						dataVector.addElement(tmp);
					}
				}
				
		
				@Override
				public int getColumnCount() {
					return columnIdentifiers.length;
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
					  return columnIdentifiers[column];
				}
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
					
					rightArea.removeAll();
					if(user instanceof Kunde){
						warenkorbverwaltungsfenster = new Warenkorbverwaltungsfenster();
						rightArea.add(warenkorbverwaltungsfenster);
					} else {
						artikelverwaltungsfenster = new Artikelverwaltungsfenster();
						rightArea.add(artikelverwaltungsfenster);
					}
					rightArea.revalidate();
					rightArea.repaint();
					
				} else if (ae.getSource().equals(kundenButton)) {
					leftArea.remove(leftArea.getComponent(1));
					leftArea.add(kundensichtfenster);
					leftArea.revalidate();
					leftArea.repaint();
					
					rightArea.removeAll();
					// TODO Kundenverwaltungsfenster
					
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
	}

	
	
	
	public static void main(String[] args){
		GUI gui = new GUI();
	}
	
}
