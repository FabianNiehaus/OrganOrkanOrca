package user;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import org.jdesktop.swingx.JXTable;

import data_objects.Artikel;
import data_objects.Kunde;
import data_objects.Massengutartikel;
import data_objects.Mitarbeiter;
import data_objects.Person;
import data_objects.Rechnung;
import data_objects.Warenkorb;
import domain.eShopCore;
import domain.exceptions.AccessRestrictedException;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.ArticleStockNotSufficientException;
import domain.exceptions.BasketNonexistantException;
import domain.exceptions.InvalidAmountException;
import domain.exceptions.LoginFailedException;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
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
				mainwindow = new MainWindow("OrganOrkanOrca eShop");
				loginwindow.dispose();		
			} catch (NumberFormatException | LoginFailedException e1) {
				JOptionPane.showMessageDialog(loginwindow, "Anmeldung fehlgeschlagen");
			}
		}
	}
	
	private class LoginUserUmgehenListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				user = eShop.anmelden(1001, "test");
			} catch (LoginFailedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mainwindow = new MainWindow("OrganOrkanOrca eShop");
			loginwindow.dispose();
			
		}
		
	}
	
	private class LoginMitarbeiterUmgehenListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				user = eShop.anmelden(9000, "test2");
			} catch (LoginFailedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mainwindow = new MainWindow("OrganOrkanOrca eShop");
			loginwindow.dispose();
			
		}

	}
	
	private class LoginWindow extends JFrame {
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JLabel benutzerLabel = new JLabel("Benutzer");
		JLabel passwortLabel = new JLabel("Passwort");
		
		JTextField benutzerField = new JTextField(10);
		JPasswordField passwortField = new JPasswordField(10);
		
		JLabel headerLabel = new JLabel("Willkommen bei OrganOrkanOrca");
		
		JButton anmeldenButton = new JButton("Login");
		
		public LoginWindow(String titel){
			super(titel);
			
			JPanel form = new JPanel();
			
			form.setLayout(new MigLayout());
			
			form.add(headerLabel, "span 2, wrap");
			form.add(benutzerLabel);
			form.add(benutzerField, "wrap");
			form.add(passwortLabel);
			form.add(passwortField, "wrap");
			form.add(anmeldenButton);
			
			JPanel pU = new JPanel();
			JButton bU = new JButton("Als User anmelden");
			bU.addActionListener(new LoginUserUmgehenListener());
			pU.add(bU);
			
			JPanel pM = new JPanel();
			JButton bM = new JButton("Als Mitarbeiter anmelden");
			bM.addActionListener(new LoginMitarbeiterUmgehenListener());
			pM.add(bM);
			
			tabbedPane.addTab("Anmelden", null, form, "Anmeldung");
			tabbedPane.addTab("User", null, pU, "Shortcut User");
			tabbedPane.addTab("Mitarbeiter", null, pM, "Shortcut Mitarbeiter");
			
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			
			this.getContentPane().add(tabbedPane);
			
			anmeldenButton.addActionListener(new LoginButtonListener());
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			this.setLocationRelativeTo(null);
			
			this.pack();
			
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
		
		public MainWindow(String titel){
			super(titel);
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

			getContentPane().add(main);
			
			artikelButton.addActionListener(new MenuButtonsActionListener());
			moduleButtons.add(artikelButton);
			kundenButton.addActionListener(new MenuButtonsActionListener());
			moduleButtons.add(kundenButton);
			mitarbeiterButton.addActionListener(new MenuButtonsActionListener());
			moduleButtons.add(mitarbeiterButton);
			shopButton.addActionListener(new MenuButtonsActionListener());
			moduleButtons.add(shopButton);
			
			leftArea.setLayout(new BorderLayout());
			
			leftArea.add(moduleButtons,BorderLayout.NORTH);
			
			kundensichtfenster = new Kundensichtfenster();
			artikelsichtfenster = new Artikelsichtfenster();
			mitarbeitersichtfenster = new Mitarbeitersichtfenster();
			
			leftArea.add(artikelsichtfenster,BorderLayout.CENTER);
			
			leftArea.add(new JPanel(),BorderLayout.WEST);
			
			if(user instanceof Kunde){
				warenkorbverwaltungsfenster = new Warenkorbverwaltungsfenster();
				rightArea.add(warenkorbverwaltungsfenster);
			} else {
				artikelverwaltungsfenster = new Artikelverwaltungsfenster();
				rightArea.add(artikelverwaltungsfenster);
			}
	
			
			main.setLeftComponent(leftArea);
			main.setRightComponent(rightArea);
	
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			this.pack();
			
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
			JXTable auflistung = new JXTable();
			JScrollPane auflistungContainer = new JScrollPane(auflistung);
			JButton aktion = new JButton();
			JTextField anzahl = new JTextField(5);
			
			public Sichtfenster(){
				
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
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
	
				leftAreaActionField.add(aktion);
				leftAreaActionField.add(anzahl);
				
				try {
					
					auflistungInitialize();

					//auflistung.setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
					//auflistung.packAll();
					
					//auflistungContainer.
					
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
						
						anzahl.setText("");
						
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
					
				
				etm = new eShopTableModel(data, new String[]{"ArtNr.","Bezeichnung","Preis","Einheit","Bestand"});
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
			
			Artikel art;
			
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
			JButton aendernBestaetigenButton = new JButton("Bestätigen");
			JButton loeschenButton = new JButton("Löschen");
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
				buttons.add(aendernBestaetigenButton);
				buttons.add(loeschenButton);
				buttons.add(neuAnlegenBestaetigenButton);
				
				aendernBestaetigenButton.setVisible(false);
				neuAnlegenBestaetigenButton.setVisible(false);
				
				aendernButton.addActionListener(new ArtikelBearbeitenListener());
				aendernBestaetigenButton.addActionListener(new ArtikelBearbeitenListener());
				
				neuAnlegenButton.addActionListener(new ArtikelNeuAnlegenListener());
				neuAnlegenBestaetigenButton.addActionListener(new ArtikelNeuAnlegenListener());
				
				loeschenButton.addActionListener(new ArtikelLoeschenListener());
				
				this.add(buttons);
				
				artNrField.setEditable(false);
				bezeichnungField.setEditable(false);
				preisField.setEditable(false);
				pkggroesseField.setEditable(false);
				bestandField.setEditable(false);
				
				detailArea.setLayout(new GridLayout(5,2));
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
				this.setVisible(true);
			}
			
			public void artikelAnzeigen(Artikel art){
				this.art = art;
				
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
						
						if(!artNrField.getText().equals("")){
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
							loeschenButton.setVisible(false);
							neuAnlegenBestaetigenButton.setVisible(true);
						}
						
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
										loeschenButton.setVisible(true);
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
			
			public class ArtikelBearbeitenListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(aendernButton)){
						
						if(!artNrField.getText().equals("")){
							
							//Felder editierbar machen
							bezeichnungField.setEditable(true);
							preisField.setEditable(true);
							pkggroesseField.setEditable(true);
							bestandField.setEditable(true);
							
							//Buttons anpassen
							neuAnlegenButton.setVisible(false);
							aendernButton.setVisible(false);
							loeschenButton.setVisible(false);
							aendernBestaetigenButton.setVisible(true);
							
						}

					} else if (e.getSource().equals(aendernBestaetigenButton)){
						
						String bezeichnung = bezeichnungField.getText();
						
						try{
							
							int bestand = Integer.parseInt(bestandField.getText());
							
							try{
								
								double preis = Double.parseDouble(preisField.getText());
								
								try{
									
									int packungsgroesse = Integer.parseInt(pkggroesseField.getText());
									
										
									try {
										Artikel art = eShop.artikelSuchen(Integer.parseInt(artNrField.getText()), user);
										
										art.setBezeichnung(bezeichnung);
										art.setBestand(bestand);
										art.setPreis(preis);
										if(packungsgroesse > 1){
											eShop.artikelLoeschen(art, user);
											art = eShop.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse, user);
										}
										
										//Neu erstellten  Artikel anzeigen
										artikelAnzeigen(art);
										
										//Buttons anpassen
										neuAnlegenButton.setVisible(true);
										aendernButton.setVisible(true);
										loeschenButton.setVisible(true);
										neuAnlegenBestaetigenButton.setVisible(false);
										
										artikelsichtfenster.auflistungInitialize();
										
									} catch (ArticleNonexistantException e1) {
										JOptionPane.showMessageDialog(Artikelverwaltungsfenster.this, e1.getMessage());
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
			
			public class ArtikelLoeschenListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						
						eShop.artikelLoeschen(art, user);
						
						artNrField.setText("");
						bezeichnungField.setText("");
						preisField.setText("");
						pkggroesseField.setText("");
						bestandField.setText("");
						
						art = null;
						
						artikelsichtfenster.auflistungInitialize();
						
					} catch (AccessRestrictedException e1) {
						JOptionPane.showMessageDialog(Artikelverwaltungsfenster.this, e1.getMessage());
					}
				}
			}
			
		}
				
		class Warenkorbverwaltungsfenster extends JPanel{
			
			Warenkorb wk;
			
			JPanel buttons = new JPanel();
			
			JTable warenkorbAuflistung = new JTable();
			JScrollPane warenkorbAuflistungContainer = new JScrollPane(warenkorbAuflistung);
			
			JButton aendernButton = new JButton("Anzahl ändern");
			JButton artikelEntfernenButton = new JButton("Entfernen");
			JButton leerenButton = new JButton("Leeren");
			JButton kaufenButton = new JButton("Kaufen");
			
			public Warenkorbverwaltungsfenster(){
				
				this.add(new JLabel("Warenkorbverwaltung"));
				
				this.add(warenkorbAuflistungContainer);
				
				aendernButton.addActionListener(new WarenkorbActionListener());
				artikelEntfernenButton.addActionListener(new WarenkorbActionListener());
				leerenButton.addActionListener(new WarenkorbActionListener());
				kaufenButton.addActionListener(new WarenkorbActionListener());
				
				buttons.add(aendernButton);
				buttons.add(artikelEntfernenButton);
				buttons.add(leerenButton);
				buttons.add(kaufenButton);
				
				this.add(buttons);
				
				JTableHeader header = warenkorbAuflistung.getTableHeader();
				header.setUpdateTableInRealTime(true);
				header.setReorderingAllowed(false);
				warenkorbAuflistung.setAutoCreateRowSorter(true);
				
				warenkorbAuflistung.setModel(new WarenkorbTableModel());
				
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
				this.setVisible(true);
			}
			
			public void warenkorbAufrufen() throws AccessRestrictedException{
				Warenkorb wk = eShop.warenkorbAusgeben(user);
				Map<Artikel,Integer> inhalt = wk.getArtikel();
				warenkorbAuflistung.setModel(new WarenkorbTableModel(inhalt));
			}
			
			class WarenkorbActionListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					
					//Anzahl eines Artikels im Warenkorn ändern
					if (e.getSource().equals(aendernButton)){
						
						try {
							
							int row = warenkorbAuflistung.getSelectedRow();
							
							if (row != -1) {
							
								int anz = Integer.parseInt(JOptionPane.showInputDialog("Bitte gewünschte Anzahl angeben"));
								
								if (anz > 0){
	
									eShop.artikelInWarenkorbAendern(row, anz, user);
									
								} else {
									throw new InvalidAmountException();
								}
								
								wk = eShop.warenkorbAusgeben(user);
								
								warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
								
							}
							
						} catch (ArticleStockNotSufficientException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						} catch (BasketNonexistantException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						} catch (AccessRestrictedException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, "Keine gültige Anzahl!");
						} catch (InvalidAmountException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						}
					
					//Artikel aus Warenkorb entfernen
					} else if (e.getSource().equals(artikelEntfernenButton)){

						try {
							
							int row = warenkorbAuflistung.getSelectedRow();
							
							if (row != -1) {
							
								int anz = 0;
	
								eShop.artikelInWarenkorbAendern(row, anz, user);
								
								wk = eShop.warenkorbAusgeben(user);
								
								warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
								
							}
							
						} catch (ArticleStockNotSufficientException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						} catch (BasketNonexistantException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						} catch (AccessRestrictedException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						}
						
						
					//Warenkorb leeren
					} else if (e.getSource().equals(leerenButton)){
						
						try {
							
							eShop.warenkorbLeeren(user);
							
							wk = eShop.warenkorbAusgeben(user);
							
							warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
							
						} catch (AccessRestrictedException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						}
						
						warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
						
						
					//Warenkorb kaufen
					} else if (e.getSource().equals(kaufenButton)){
						
						try {

							//Formatierungsvorlage für Datum
							DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
							
							Rechnung re = eShop.warenkorbKaufen(user);
							
							artikelsichtfenster.auflistungInitialize();
							
							String rechnungsString = "";
							
							rechnungsString += "Rechnung" + "\n\n";
							rechnungsString += dateFormat.format(re.getDatum()) + "\n\n";
							rechnungsString += "Kundennummer: " + re.getKu().getId() + "\n";
							rechnungsString += re.getKu().getFirstname() + " " + re.getKu().getLastname() + "\n";
							rechnungsString += re.getKu().getAddress_Street() + "\n";
							rechnungsString += re.getKu().getAddress_Zip() + " " + re.getKu().getAddress_Town() + "\n\n";
							rechnungsString += "Warenkorb" + "\n";
							rechnungsString += re.getWk().toString() + "\n";
							rechnungsString += "Gesamtbetrag: " + re.getGesamt() + "€";
							
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, rechnungsString);
							
							wk = eShop.warenkorbAusgeben(user);
							
							warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
							
						} catch (AccessRestrictedException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						} catch (InvalidAmountException e1) {
							JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
						}
					}
					
				}
				
			}
			
			class WarenkorbTableModel extends AbstractTableModel{
				
				String[] columns = {"Artikel","Preis","Menge","Gesamt"};
				
				Vector<Vector<Object>> dataVector = new Vector<>(0);
				Vector<String> columnIdentifiers = new Vector<>(0);
				
				public WarenkorbTableModel(Map<Artikel,Integer> inhalt) {
					
					columnIdentifiers =  setColumns(columns);
										
					for(Map.Entry<Artikel, Integer> ent : inhalt.entrySet()){
						Vector<Object> tmp = new Vector<>(0);
						
						tmp.addElement(ent.getKey().getBezeichnung());
						tmp.addElement(ent.getKey().getPreis());
						tmp.addElement(ent.getValue());
						tmp.addElement(ent.getKey().getPreis() * ent.getValue());
						
						dataVector.addElement(tmp);
					}
				}
				
				public WarenkorbTableModel(){
					columnIdentifiers =  setColumns(columns);
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
