package eshop.client.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import eshop.client.util.ShopTableModel;
import eshop.client.util.Verwaltungsfenster;
import eshop.client.util.Verwaltungsfenster.VerwaltungsfensterCallbacks;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class ArtikelDetailsfenster extends Verwaltungsfenster {

    /**
     * 
     */
    private static final long serialVersionUID = -107879108721906207L;
    
    Artikel    art;
    JPanel     detailArea		   = new JPanel();
    JLabel     bezeichnungLabel		   = new JLabel();
    JLabel     preisLabel		   = new JLabel();
    JLabel		infoLabel			= new JLabel("Informationen:");
    JTextArea	infoArea			= new JTextArea();
    JTextField	anzahlField			= new JTextField(3);
    JLabel		stueckLabel			= new JLabel("Stück");
    JPanel     buttons			   = new JPanel();
    JPanel     picture				= new JPanel();
    JButton		anzeigenButton		= new JButton("Anzeigen");
    JButton		inWarenkorbButton	= new JButton("hinzufügen");
    JButton		WarenkorbButton		= new JButton("Warenkorb");
    
    JTextField pkggroesseField		   = new JTextField(15);
    JTextField artNrField		   = new JTextField(15);
    JTextField bestandField		   = new JTextField(15);
    
    public ArtikelDetailsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks listener) {
	super(server, user, listener);
		this.setLayout(new MigLayout());
		detailArea.setLayout(new MigLayout());
		buttons.setLayout(new MigLayout());
		this.add(new JLabel("Artikeldetails:"), "cell 1 0");
		this.add(buttons, "cell 0 1");
		this.add(picture, "w 160!,h 120!");
		this.add(detailArea, "cell 1 1, w 100%");
		
		picture.setBackground(Color.lightGray);
		buttons.add(anzeigenButton, "wrap, w 100!");
		buttons.add(anzahlField, "split 2, w 30!");
		buttons.add(stueckLabel, "wrap, w 40!");
		buttons.add(inWarenkorbButton, "wrap, w 100!");
		buttons.add(WarenkorbButton, "w 100!");
		anzeigenButton.addActionListener(new InfosAnzeigenListener());
		detailArea.setBackground(Color.WHITE);
		bezeichnungLabel.setFont(new Font("Arial", Font.BOLD, 30));
		preisLabel.setFont(new Font("Arial", Font.BOLD, 15));
		infoArea.setSize(600, 100);
		infoArea.setLineWrap(true);
		infoArea.setWrapStyleWord(true);
		detailArea.add(bezeichnungLabel, "wrap");
		detailArea.add(preisLabel,"wrap");
		detailArea.add(infoLabel, "wrap");
		detailArea.add(infoArea,"w 100%");

		anzahlField.setText("1");
    }

    public void artikelAnzeigen(Artikel art) {

		this.art = art;
		
		bezeichnungLabel.setText(art.getBezeichnung());
		preisLabel.setText(String.valueOf(art.getPreis()));
		
	}
    
    public class InfosAnzeigenListener implements ActionListener {
    
    	public void actionPerformed(ActionEvent e) {
    		
    		bezeichnungLabel.setText("Orkan");
    		preisLabel.setText("99999,0€");
    		infoArea.setText("Als Orkan werden im weiteren Sinn Winde mit Geschwindigkeiten von mindestens 64 kn "
    				+ "(117,7 km/h = 32,7 m/s) bezeichnet. Auf der Beaufortskala werden Orkane mit der Stärke 12 "
    				+ "klassifiziert. Im engeren Sinn werden darunter außertropische Tiefdruckgebiete verstanden, "
    				+ "in denen Winde mit Orkanstärke auftreten. Orkane können massive Verwüstungen anrichten und "
    				+ "bilden auf See eine Gefahr für den Schiffsverkehr.");
    	    
    	}
    }
}

