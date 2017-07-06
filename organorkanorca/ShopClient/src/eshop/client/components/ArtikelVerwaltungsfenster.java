package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eshop.client.util.Verwaltungsfenster;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import net.miginfocom.swing.MigLayout;

public class ArtikelVerwaltungsfenster extends Verwaltungsfenster {

    /**
     * 
     */
    private static final long serialVersionUID = -107879108721906207L;
    
    Artikel    art;
    JPanel     detailArea		   = new JPanel();
    JLabel     artNrLabel		   = new JLabel("Artikelnummer:");
    JTextField artNrField		   = new JTextField(15);
    JLabel     bezeichnungLabel		   = new JLabel("Bezeichnung:");
    JTextField bezeichnungField		   = new JTextField(15);
    JLabel     preisLabel		   = new JLabel("Preis:");
    JTextField preisField		   = new JTextField(15);
    JLabel     pkggroesseLabel		   = new JLabel("Packungsgröße:");
    JTextField pkggroesseField		   = new JTextField(15);
    JLabel     bestandLabel		   = new JLabel("Bestand:");
    JTextField bestandField		   = new JTextField(15);
    JPanel     buttons			   = new JPanel();
    JButton    neuAnlegenButton		   = new JButton("Neu");
    JButton    aendernButton		   = new JButton("Ändern");
    JButton    aendernBestaetigenButton	   = new JButton("Bestätigen");
    JButton    loeschenButton		   = new JButton("Löschen");
    JButton    neuAnlegenBestaetigenButton = new JButton("Anlegen");

    public ArtikelVerwaltungsfenster() {
	this.setLayout(new MigLayout());
	detailArea.setLayout(new MigLayout());
	this.add(new JLabel("Artikelverwaltung"), "align center, wrap");
	detailArea.add(artNrLabel);
	detailArea.add(artNrField, "wrap");
	detailArea.add(bezeichnungLabel);
	detailArea.add(bezeichnungField, "wrap");
	detailArea.add(preisLabel);
	detailArea.add(preisField, "wrap");
	detailArea.add(pkggroesseLabel);
	detailArea.add(pkggroesseField, "wrap");
	detailArea.add(bestandLabel);
	detailArea.add(bestandField, "wrap");
	this.add(detailArea, "wrap");
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
	this.add(buttons, "align center, wrap");
	artNrField.setEditable(false);
	bezeichnungField.setEditable(false);
	preisField.setEditable(false);
	pkggroesseField.setEditable(false);
	bestandField.setEditable(false);
	this.setVisible(true);
    }

    public void artikelAnzeigen(Artikel art) {

	this.art = art;
	artNrField.setText(String.valueOf(art.getArtikelnummer()));
	bezeichnungField.setText(art.getBezeichnung());
	preisField.setText(String.valueOf(art.getPreis()));
	if (art instanceof Massengutartikel) {
	    pkggroesseField.setText(String.valueOf(((Massengutartikel) art).getPackungsgroesse()));
	} else {
	    pkggroesseField.setText("1");
	}
	bestandField.setText(String.valueOf(art.getBestand()));
    }

    public class ArtikelBearbeitenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    if (e.getSource().equals(aendernButton)) {
		if (!artNrField.getText().equals("")) {
		    // Felder editierbar machen
		    bezeichnungField.setEditable(true);
		    preisField.setEditable(true);
		    pkggroesseField.setEditable(true);
		    bestandField.setEditable(true);
		    // Buttons anpassen
		    neuAnlegenButton.setVisible(false);
		    aendernButton.setVisible(false);
		    loeschenButton.setVisible(false);
		    aendernBestaetigenButton.setVisible(true);
		    ArtikelVerwaltungsfenster.this.repaint();
		}
	    } else if (e.getSource().equals(aendernBestaetigenButton)) {
		String bezeichnung = bezeichnungField.getText();
		try {
		    int bestand = Integer.parseInt(bestandField.getText());
		    try {
			double preis = Double.parseDouble(preisField.getText());
			try {
			    int packungsgroesse = Integer.parseInt(pkggroesseField.getText());
			    try {
				Artikel art = server.artikelSuchen(Integer.parseInt(artNrField.getText()), user);
				art.setBezeichnung(bezeichnung);
				server.erhoeheArtikelBestand(art.getArtikelnummer(), bestand, user);
				art.setPreis(preis);
				if (packungsgroesse > 1) {
				    server.artikelLoeschen(art, user);
				    art = server.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse, user);
				}
				// Neu erstellten Artikel anzeigen
				artikelAnzeigen(art);
				// Buttons anpassen
				aendernBestaetigenButton.setVisible(false);
				neuAnlegenButton.setVisible(true);
				aendernButton.setVisible(true);
				loeschenButton.setVisible(true);
				listener.update("Artikel");
				ArtikelVerwaltungsfenster.this.repaint();
			    } catch(ArticleNonexistantException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			    } catch(AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			    } catch(InvalidAmountException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			    } catch(RemoteException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			    }
			} catch(NumberFormatException e1) {
			    JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this,
				    "Keine gueltige Packungsgröße");
			}
		    } catch(NumberFormatException e1) {
			JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein gueltiger Preis!");
		    }
		} catch(NumberFormatException e1) {
		    JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein gueltiger Bestand!");
		}
	    }
	}
    }

    public class ArtikelLoeschenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    try {
		server.artikelLoeschen(art, user);
		artNrField.setText("");
		bezeichnungField.setText("");
		preisField.setText("");
		pkggroesseField.setText("");
		bestandField.setText("");
		art = null;
		listener.update("artikel");
	    } catch(AccessRestrictedException e1) {
		JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
	    } catch(RemoteException e1) {
		JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
	    }
	}
    }

    public class ArtikelNeuAnlegenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    if (e.getSource().equals(neuAnlegenButton)) {
		// Artikelnummer ausblenden, kann nicht neu angegeben werden
		artNrLabel.setVisible(false);
		artNrField.setVisible(false);
		// Alle Felder leeren
		bezeichnungField.setText("");
		preisField.setText("");
		pkggroesseField.setText("");
		bestandField.setText("");
		// Felder editierbar machen
		bezeichnungField.setEditable(true);
		preisField.setEditable(true);
		pkggroesseField.setEditable(true);
		bestandField.setEditable(true);
		// Buttons anpassen
		neuAnlegenButton.setVisible(false);
		aendernButton.setVisible(false);
		loeschenButton.setVisible(false);
		neuAnlegenBestaetigenButton.setVisible(true);
		ArtikelVerwaltungsfenster.this.repaint();
	    } else if (e.getSource().equals(neuAnlegenBestaetigenButton)) {
		String bezeichnung = bezeichnungField.getText();
		try {
		    int bestand = Integer.parseInt(bestandField.getText());
		    try {
			double preis = Double.parseDouble(preisField.getText());
			try {
			    int packungsgroesse = Integer.parseInt(pkggroesseField.getText());
			    try {
				Artikel art = server.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse,
					user);
				// Neu erstellten Artikel anzeigen
				artikelAnzeigen(art);
				// Artikelnummer wieder anzeigen
				artNrLabel.setVisible(true);
				artNrField.setVisible(true);
				// Felder editierbar machen
				bezeichnungField.setEditable(false);
				preisField.setEditable(false);
				pkggroesseField.setEditable(false);
				bestandField.setEditable(false);
				// Buttons anpassen
				neuAnlegenButton.setVisible(true);
				aendernButton.setVisible(true);
				loeschenButton.setVisible(true);
				neuAnlegenBestaetigenButton.setVisible(false);
				listener.update("artikel");
				ArtikelVerwaltungsfenster.this.repaint();
			    } catch(AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			    } catch(InvalidAmountException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			    } catch(RemoteException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			    }
			} catch(NumberFormatException e1) {
			    JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this,
				    "Keine gueltige Packungsgröße");
			}
		    } catch(NumberFormatException e1) {
			JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein gueltiger Preis!");
		    }
		} catch(NumberFormatException e1) {
		    JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein gueltiger Bestand!");
		}
	    }
	}
    }
}
