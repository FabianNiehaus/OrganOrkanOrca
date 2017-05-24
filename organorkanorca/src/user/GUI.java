package user;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

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
			user = eShop.anmelden(1001, "test");
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
		try {
			leftArea.add(new Artikelsichtfenster());
		} catch (AccessRestrictedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rightArea.add(new Warenkorbverwaltungsfenster());

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
		JTable auflistung;
		JButton aktion = new JButton();
		JTextField anzahl = new JTextField();
		
		public Sichtfenster(AbstractTableModel atm, String aktionsname, boolean anzahlZeigen){
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setPreferredSize(new Dimension((int)(GUI.this.getWidth()*0.55), (int)(GUI.this.getHeight()*0.8)));
			
			overviewButtons.setMaximumSize(new Dimension(1024, 40));
						
			auflistung = new JTable(atm);
			auflistung.setAutoCreateRowSorter(true);
			
			this.add(overviewButtons);
			this.add(auflistung);
			this.add(leftAreaActionField);
						
			overviewButtons.setLayout(new BoxLayout(overviewButtons, BoxLayout.X_AXIS));
			alleButton.addActionListener(new ButtonActionListener());
			overviewButtons.add(alleButton);
			sortNrButton.addActionListener(new ButtonActionListener());
			overviewButtons.add(sortNrButton);
			sortBezButton.addActionListener(new ButtonActionListener());
			overviewButtons.add(sortBezButton);
			overviewButtons.add(sucheField);
			sucheButton.addActionListener(new ButtonActionListener());
			overviewButtons.add(sucheButton);
			overviewButtons.setVisible(true);
					
			aktion.setText(aktionsname);
			
			leftAreaActionField.add(aktion);
			if(anzahlZeigen){
				leftAreaActionField.add(anzahl);
			}
		}
		
		public abstract void auflistungInitialize();
		
		public abstract void sortiereNummer();
		
		public abstract void sortiereBezeichnung();
		
		public abstract void suche(String suchStr);
		
		class ButtonActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (ae.getSource().equals(alleButton) || ae.getSource().equals(sortNrButton)){
					sortiereNummer();
				} else if (ae.getSource().equals(sortBezButton)){
					sortiereBezeichnung();
				} else if (ae.getSource().equals(sucheButton)){
					suche(sucheField.getText());
				}
			}
			
		}
	}
	
	
	class ArtikelTableModel extends AbstractTableModel{

		private String[] columnNames = {"Artikelnummer","Bezeichnung","Preis","Packungsgröße","Bestand"};
		private Vector<Vector<Object>> data = new Vector<>();
		
		public ArtikelTableModel(Vector<Artikel> liste) {

			for(Artikel art : liste){
				
				Vector<Object> tmp = new Vector<>();
				
				tmp.addElement(art.getArtikelnummer());
				tmp.addElement(art.getBezeichnung());
				tmp.addElement(art.getPreis());
				tmp.addElement(((Massengutartikel)art).getPackungsgroesse());
				tmp.addElement(art.getBestand());
				
				data.addElement(tmp);
				
			}

		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			return data.elementAt(arg0).elementAt(arg1);
		}
		
		
		
	}
	
	class Artikelsichtfenster extends Sichtfenster{

		public Artikelsichtfenster() throws AccessRestrictedException{
			super(new ArtikelTableModel(eShop.alleArtikelAusgeben(user)),"In Warenkorb", true);		
		}
				
		@Override
		public void auflistungInitialize() {
			//auflistung.set
		}

		@Override
		public void sortiereNummer() {
			/*
			Vector<Artikel> artSort;
			try {
				artSort = eShop.alleArtikelAusgeben(user);
				//Sortieren nach Artikelnummer
				Collections.sort(artSort, 
					(Artikel o1, Artikel o2) -> o1.getArtikelnummer() - o2.getArtikelnummer());
				auflistung.setText("");
				
				for (Artikel art : artSort){
					auflistung.append(art.toString() + "\n");
				}
			} catch (AccessRestrictedException e) {
				auflistung.setText(e.getMessage());
			}
			*/
		}

		@Override
		public void sortiereBezeichnung() {
			/*
			Vector<Artikel> artSort;
			try {
				artSort = eShop.alleArtikelAusgeben(user);
				//Sortieren nach Artikelnummer
				Collections.sort(artSort, 
					(Artikel o1, Artikel o2) -> o1.getBezeichnung().compareTo(o2.getBezeichnung()));
				auflistung.setText("");
				for (Artikel art : artSort){
					auflistung.append(art.toString() + "\n");
				}
			} catch (AccessRestrictedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
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
		
		public Kundensichtfenster(){
			super(null,"Bearbeiten", false);
		}

		@Override
		public void auflistungInitialize() {
			/*
			try {
				auflistung.setText("");
				for (Kunde ku : eShop.alleKundenAusgeben(user)){
					auflistung.append(ku.toString() + "\n");
				}
			} catch (AccessRestrictedException e) {
				auflistung.setText(e.getMessage());
			}	
			*/	
		}

		@Override
		public void sortiereNummer() {
//			Vector<Kunde> kuSort;
//			try {
//				kuSort = eShop.alleKundenAusgeben(user);
//				//Sortieren nach Artikelnummer
//				Collections.sort(kuSort, 
//					(Kunde o1, Kunde o2) -> o1.getId() - o2.getId());
//				auflistung.setText("");
//				for (Kunde ku : kuSort){
//					auflistung.append(ku.toString() + "\n");
//				}
//			} catch (AccessRestrictedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

		@Override
		public void sortiereBezeichnung() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void suche(String suchStr) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Mitarbeitersichtfenster extends Sichtfenster{

		public Mitarbeitersichtfenster(){
			super(null,"Bearbeiten", false);
		}

		@Override
		public void auflistungInitialize() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sortiereNummer() {
//			Vector<Mitarbeiter> maSort;
//			try {
//				maSort = eShop.alleMitarbeiterAusgeben(user);
//				//Sortieren nach Artikelnummer
//				Collections.sort(maSort, 
//					(Mitarbeiter o1, Mitarbeiter o2) -> o1.getId() - o2.getId());
//				auflistung.setText("");
//				for (Mitarbeiter ma : maSort){
//					auflistung.append(ma.toString() + "\n");
//				}
//			} catch (AccessRestrictedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

		@Override
		public void sortiereBezeichnung() {
			// TODO Auto-generated method stub
			
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
				try {
					leftArea.remove(leftArea.getComponent(1));
					leftArea.add(new Artikelsichtfenster());
					leftArea.revalidate();
					leftArea.repaint();
				} catch (AccessRestrictedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ae.getSource().equals(kundenButton)) {
				leftArea.remove(leftArea.getComponent(1));
				leftArea.add(new Kundensichtfenster());
				leftArea.revalidate();
				leftArea.repaint();
			} else if (ae.getSource().equals(mitarbeiterButton)) {
				leftArea.remove(leftArea.getComponent(1));
				leftArea.add(new Mitarbeitersichtfenster());
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
