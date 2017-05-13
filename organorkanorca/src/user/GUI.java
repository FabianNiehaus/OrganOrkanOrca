package user;

import javax.swing.*;

import data_objects.Artikel;
import data_objects.Person;
import domain.eShopCore;
import domain.exceptions.AccessRestrictedException;
import domain.exceptions.LoginFailedException;

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
		leftArea.add(new Artikelsichtfenster());
		rightArea.add(new Warenkorbverwaltungsfenster());

		main.setDividerLocation((int)(this.getWidth()*0.60));
		main.setLeftComponent(leftArea);
		main.setRightComponent(rightArea);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
		
	class Artikelsichtfenster extends JPanel{
		
		JPanel overviewButtons = new JPanel();
		JButton alleButton = new JButton("Alle");
		JButton sortNrButton = new JButton("Sort. Nr.");
		JButton sortBezButton = new JButton("Sort. Bez.");
		JButton sucheButton = new JButton("Suche");
		JTextField sucheField = new JTextField();
		
		JPanel leftAreaActionField = new JPanel();
		JTextArea auflistung = new JTextArea();
		JButton aktion = new JButton("In Warenkorb");
		JTextField anzahl = new JTextField();
		
		public Artikelsichtfenster(){
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setPreferredSize(new Dimension((int)(GUI.this.getWidth()*0.55), (int)(GUI.this.getHeight()*0.8)));
			
			overviewButtons.setMaximumSize(new Dimension(1024, 40));
			
			
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
			
			auflistung.setEditable(false);
			try {
				auflistung.setText("");
				for (Artikel art : eShop.alleArtikelAusgeben(user)){
					auflistung.append(art.toString() + "\n");
				}
			} catch (AccessRestrictedException e) {
				auflistung.setText(e.getMessage());
			}
			
			leftAreaActionField.add(aktion);
			leftAreaActionField.add(anzahl);
		}
		
		class ButtonActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (ae.getSource().equals(alleButton) || ae.getSource().equals(sortNrButton)){
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (ae.getSource().equals(sortBezButton)){
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
				}
			}
			
		}
	}
	
	class Kundensichtfenster extends JPanel{
		
		JPanel overviewButtons = new JPanel();
		JButton alleButton = new JButton("Alle");
		JButton sortNrButton = new JButton("Sort. Nr.");
		JButton sortBezButton = new JButton("Sort. Name");
		JButton sucheButton = new JButton("Suche");
		JTextField sucheField = new JTextField();
		
		JPanel leftAreaActionField = new JPanel();
		JTextArea auflistung = new JTextArea();
		JButton aktion = new JButton("Bearbeiten");
		
		public Kundensichtfenster(){
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setPreferredSize(new Dimension((int)(GUI.this.getWidth()*0.55), (int)(GUI.this.getHeight()*0.8)));
			
			overviewButtons.setMaximumSize(new Dimension(1024, 40));
			
			this.add(overviewButtons);
			this.add(auflistung);
			this.add(leftAreaActionField);
			
	//		alleButton.setSize((int) (overviewButtons.getSize().getWidth()*0.15), 30);
	//		sortNrButton.setSize((int) (overviewButtons.getSize().getWidth()*0.15), 30);
	//		sortBezButton.setSize((int) (overviewButtons.getSize().getWidth()*0.15), 30);
	//		sucheButton.setSize((int) (overviewButtons.getSize().getWidth()*0.15), 30);
	//		sucheField.setSize((int) (overviewButtons.getSize().getWidth()*0.2), 30);
			
			overviewButtons.setLayout(new BoxLayout(overviewButtons, BoxLayout.X_AXIS));
			overviewButtons.add(alleButton);
			overviewButtons.add(sortNrButton);
			overviewButtons.add(sortBezButton);
			overviewButtons.add(sucheField);
			overviewButtons.add(sucheButton);
			overviewButtons.setVisible(true);
			
			auflistung.setEditable(false);
			
			leftAreaActionField.add(aktion);
			
			
		}
	}
	
	class Mitarbeitersichtfenster extends JPanel{
		
		JPanel overviewButtons = new JPanel();
		JButton alleButton = new JButton("Alle");
		JButton sortNrButton = new JButton("Sort. Nr.");
		JButton sortBezButton = new JButton("Sort. Name");
		JButton sucheButton = new JButton("Suche");
		JTextField sucheField = new JTextField();
		
		JPanel leftAreaActionField = new JPanel();
		JTextArea auflistung = new JTextArea();
		JButton aktion = new JButton("Bearbeiten");
		
		public Mitarbeitersichtfenster(){
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setPreferredSize(new Dimension((int)(GUI.this.getWidth()*0.55), (int)(GUI.this.getHeight()*0.8)));
			
			overviewButtons.setMaximumSize(new Dimension(1024, 40));
			
			this.add(overviewButtons);
			this.add(auflistung);
			this.add(leftAreaActionField);
			
	//		alleButton.setSize((int) (overviewButtons.getSize().getWidth()*0.15), 30);
	//		sortNrButton.setSize((int) (overviewButtons.getSize().getWidth()*0.15), 30);
	//		sortBezButton.setSize((int) (overviewButtons.getSize().getWidth()*0.15), 30);
	//		sucheButton.setSize((int) (overviewButtons.getSize().getWidth()*0.15), 30);
	//		sucheField.setSize((int) (overviewButtons.getSize().getWidth()*0.2), 30);
			
			overviewButtons.setLayout(new BoxLayout(overviewButtons, BoxLayout.X_AXIS));
			overviewButtons.add(alleButton);
			overviewButtons.add(sortNrButton);
			overviewButtons.add(sortBezButton);
			overviewButtons.add(sucheField);
			overviewButtons.add(sucheButton);
			overviewButtons.setVisible(true);
			
			auflistung.setEditable(false);
			
			leftAreaActionField.add(aktion);
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
				leftArea.add(new Artikelsichtfenster());
				leftArea.revalidate();
				leftArea.repaint();
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
