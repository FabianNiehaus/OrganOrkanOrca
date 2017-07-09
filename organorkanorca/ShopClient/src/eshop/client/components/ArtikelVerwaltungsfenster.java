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
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class ArtikelVerwaltungsfenster extends Verwaltungsfenster {

	/**
	 * 
	 */
	private static final long serialVersionUID = -107879108721906207L;
	Artikel art;
	JPanel detailArea = new JPanel();
	JLabel artNrLabel = new JLabel("Artikelnummer:");
	JTextField artNrField = new JTextField(15);
	JLabel bezeichnungLabel = new JLabel("Bezeichnung:");
	JTextField bezeichnungField = new JTextField(15);
	JLabel preisLabel = new JLabel("Preis:");
	JTextField preisField = new JTextField(15);
	JLabel pkggroesseLabel = new JLabel("Packungsgröße:");
	JTextField pkggroesseField = new JTextField(15);
	JLabel bestandLabel = new JLabel("Bestand:");
	JTextField bestandField = new JTextField(15);
	JPanel buttons = new JPanel();
	JButton neuAnlegenButton = new JButton("Neu");
	JButton aendernButton = new JButton("Ändern");
	JButton aendernBestaetigenButton = new JButton("Bestätigen");
	JButton loeschenButton = new JButton("Löschen");
	JButton neuAnlegenBestaetigenButton = new JButton("Anlegen");
	
	 String bezeichnungStore;
	    double preisStore;
	    int packungsgroesseStore;
	    int bestandStore;

	public ArtikelVerwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks listener) {
		super(server, user, listener);
		
		this.setLayout(new MigLayout());
		detailArea.setLayout(new MigLayout());
		buttons.setLayout(new MigLayout());
		
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
		
		buttons.add(neuAnlegenButton, "wrap 10, w 100!");
		buttons.add(aendernButton, "wrap 10, w 100!");
		buttons.add(loeschenButton, "wrap 10, w 100!");
		buttons.add(aendernBestaetigenButton, "wrap 10, w 100!");
		buttons.add(neuAnlegenBestaetigenButton, "w 100!");
		
		aendernBestaetigenButton.setVisible(false);
		neuAnlegenBestaetigenButton.setVisible(false);
		aendernButton.addActionListener(new ArtikelBearbeitenListener());
		aendernBestaetigenButton.addActionListener(new ArtikelBearbeitenListener());
		neuAnlegenButton.addActionListener(new ArtikelNeuAnlegenListener());
		neuAnlegenBestaetigenButton.addActionListener(new ArtikelNeuAnlegenListener());
		loeschenButton.addActionListener(new ArtikelLoeschenListener());
		
		artNrField.setEditable(false);
		bezeichnungField.setEditable(false);
		preisField.setEditable(false);
		pkggroesseField.setEditable(false);
		bestandField.setEditable(false);
		
		this.add(new JLabel("Artikelverwaltung"), "wrap");
		this.add(buttons, "dock west, wrap");
		this.add(detailArea, "wrap");
		
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
		
		setStores(art);
		
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
					int bestand = 0;
					String zeile = bestandField.getText();
					String operator = zeile.substring(0, 1);
					if (operator.equals("+") || operator.equals("-")) {
						bestand = Integer.parseInt(zeile.substring(1));
					} else if (operator.equals("0")) {
						bestand = Integer.parseInt(zeile);
					} else if(operator.matches("[1-9]")){
					    bestand = Integer.parseInt(zeile);
					    if(bestand == bestandStore){
						operator = "";
						bestand = bestandStore;
					    }
					} else {					    
						throw new NumberFormatException("Kein gültiger Operator! Nur + und - sind erlaubt!");
					}

					try {
						double preis = Double.parseDouble(preisField.getText());
						try {
							int packungsgroesse = Integer.parseInt(pkggroesseField.getText());
							try {
								art = server.artikelAendern(art.getArtikelnummer(), user, bezeichnung, bestand, operator, preis,
										packungsgroesse);

								artikelAnzeigen(art);
								aendernBestaetigenButton.setVisible(false);
								neuAnlegenButton.setVisible(true);
								aendernButton.setVisible(true);
								loeschenButton.setVisible(true);
								
								// Felder editierbar machen
								bezeichnungField.setEditable(false);
								preisField.setEditable(false);
								pkggroesseField.setEditable(false);
								bestandField.setEditable(false);
								
								ArtikelVerwaltungsfenster.this.repaint();
								setStores(art);
							} catch (AccessRestrictedException e1) {
								JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
							} catch (InvalidAmountException e1) {
								JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
							} catch (RemoteException e1) {
								JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
							} catch (ArticleNonexistantException e1) {
								JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
							}
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this,
									"Keine gueltige Packungsgröße");
						}
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein gueltiger Preis!");
					}
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein gueltiger Bestand!");
				}
			}
		}
	}

	public class ArtikelLoeschenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				server.artikelLoeschen(art.getArtikelnummer(), user);
			} catch (AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			} catch (ArticleNonexistantException e1) {
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
								ArtikelVerwaltungsfenster.this.repaint();
								setStores(art);
							} catch (AccessRestrictedException e1) {
								JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
							} catch (InvalidAmountException e1) {
								JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
							} catch (RemoteException e1) {
								JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
							}
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this,
									"Keine gueltige Packungsgröße");
						}
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein gueltiger Preis!");
					}
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein gueltiger Bestand!");
				}
			}
		}
	}
	
	public Artikel getArtikel(){
		return art;
	}
	
	public void setStores(Artikel art){
	    bezeichnungStore = art.getBezeichnung();
	    preisStore = art. getPreis();
	    if(art instanceof Massengutartikel){
		packungsgroesseStore = ((Massengutartikel) art).getPackungsgroesse();
	    }else {
		packungsgroesseStore = 1;
	    }
	    bestandStore = art.getBestand();
	}	
}
