package user;

import javax.swing.*;
import java.awt.Dimension;

public class GUI extends JFrame {
	
	JSplitPane main = new JSplitPane();
	JPanel leftArea = new JPanel();
	JPanel rightArea = new JPanel();
	
	JPanel leftAreaModuleButtons = new JPanel();
	JButton artikelButton = new JButton("Artikel");
	JButton kundenButton = new JButton("Kunden");
	JButton mitarbeiterButton = new JButton("Mitarbeiter");
	JButton shopButton = new JButton("Shop");
	
	JPanel leftAreaOverviewButtons = new JPanel();
	JButton alleButton = new JButton("Alle");
	JButton sortNrButton = new JButton("Sort. Nr.");
	JButton sortBezButton = new JButton("Sort. Bez.");
	JButton sucheButton = new JButton("Suche");
	JTextField sucheField = new JTextField();
	
	JPanel leftAreaActionField = new JPanel();
	JTextArea auflistung = new JTextArea();
	JButton aktion = new JButton("In Warenkorb");
	JTextField anzahl = new JTextField();
		
	public void initialize() {
		this.setSize(1024, 512);
		this.add(main);

		main.setDividerLocation((int)(this.getWidth()*0.60));
		main.setLeftComponent(leftArea);
		main.setRightComponent(rightArea);
		main.setVisible(true);
		
		leftArea.setLayout(new BoxLayout(leftArea, BoxLayout.Y_AXIS));
		
		leftAreaOverviewButtons.setMaximumSize(new Dimension(1024, 40));
		
		leftArea.add(leftAreaModuleButtons);
		leftArea.add(leftAreaOverviewButtons);
		leftArea.add(auflistung);
		leftArea.add(leftAreaActionField);
		
		leftAreaModuleButtons.add(artikelButton);
		leftAreaModuleButtons.add(kundenButton);
		leftAreaModuleButtons.add(mitarbeiterButton);
		leftAreaModuleButtons.add(shopButton);
		
//		alleButton.setSize((int) (leftAreaOverviewButtons.getSize().getWidth()*0.15), 30);
//		sortNrButton.setSize((int) (leftAreaOverviewButtons.getSize().getWidth()*0.15), 30);
//		sortBezButton.setSize((int) (leftAreaOverviewButtons.getSize().getWidth()*0.15), 30);
//		sucheButton.setSize((int) (leftAreaOverviewButtons.getSize().getWidth()*0.15), 30);
//		sucheField.setSize((int) (leftAreaOverviewButtons.getSize().getWidth()*0.2), 30);
		
		leftAreaOverviewButtons.setLayout(new BoxLayout(leftAreaOverviewButtons, BoxLayout.X_AXIS));
		leftAreaOverviewButtons.add(alleButton);
		leftAreaOverviewButtons.add(sortNrButton);
		leftAreaOverviewButtons.add(sortBezButton);
		leftAreaOverviewButtons.add(sucheField);
		leftAreaOverviewButtons.add(sucheButton);
		leftAreaOverviewButtons.setVisible(true);
		
		auflistung.setEditable(false);
		
		leftAreaActionField.add(aktion);
		leftAreaActionField.add(anzahl);
		
		rightArea.add(new Artikelverwaltungsfenster());

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private class Artikelverwaltungsfenster extends JPanel{
		
		public Artikelverwaltungsfenster(){
			JButton b = new JButton("test");
			this.add(b);
			this.setVisible(true);
		}
	}
	
	
	public static void main(String[] args){
		GUI gui = new GUI(/*"OrganOrkanOrca eShop"*/);
		gui.initialize();
		gui.setVisible(true);
	}
	
}
