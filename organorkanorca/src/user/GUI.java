package user;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;

public class GUI extends JFrame {
	public GUI(String titel) {
		super(titel);
	}
	
	JSplitPane main = new JSplitPane();
	JPanel leftArea = new JPanel();
	JPanel rightArea = new JPanel();
	
	
		
	public void initialize() {
		this.setSize(1024, 512);
		getContentPane().add(main);

		main.setDividerLocation((int)(this.getWidth()*0.60));
		main.setLeftComponent(leftArea);
		main.setRightComponent(rightArea);
		main.setVisible(true);
		
		leftArea.add(new Artikelsichtfenster());
		rightArea.add(new Warenkorbverwaltungsfenster());

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
		
	private class Artikelsichtfenster extends JPanel{
		
		JPanel moduleButtons = new JPanel();
		JButton artikelButton = new JButton("Artikel");
		JButton kundenButton = new JButton("Kunden");
		JButton mitarbeiterButton = new JButton("Mitarbeiter");
		JButton shopButton = new JButton("Shop");
		
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
			
			this.add(moduleButtons);
			this.add(overviewButtons);
			this.add(auflistung);
			this.add(leftAreaActionField);
			
			moduleButtons.add(artikelButton);
			moduleButtons.add(kundenButton);
			moduleButtons.add(mitarbeiterButton);
			moduleButtons.add(shopButton);
			
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
			leftAreaActionField.add(anzahl);
		}
	}
	
	private class Artikelverwaltungsfenster extends JPanel{
		
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
	
	private class Warenkorbverwaltungsfenster extends JPanel{
		
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
	
	
	
	public static void main(String[] args){
		GUI gui = new GUI("OrganOrkanOrca eShop");
		gui.initialize();
		gui.setVisible(true);
	}
	
}
