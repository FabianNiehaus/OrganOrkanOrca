package eshop.client.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import eshop.client.util.Verwaltungsfenster;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleAlreadyInBasketException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.ArticleStockNotSufficientException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class ArtikelVerwaltungsfenster extends Verwaltungsfenster {

	/**
	 * 
	 */
	private static final long serialVersionUID = -107879108721906207L;

	Artikel		      art;
	    
	    JPanel		      detailArea		  = new JPanel(new MigLayout());
	    JPanel			buttonArea = new JPanel(new MigLayout());
	    JPanel		      mitarbeiterButtons	  = new JPanel(new MigLayout());
	    JPanel	kundenButtons = new JPanel(new MigLayout());
	    JPanel		      picture			  = new JPanel();
	    
	    JTextField		      bezeichnungField		  = new JTextField(15);
	    JTextField		      preisField		  = new JTextField(15);
	    JLabel		      infoLabel			  = new JLabel("Informationen:");
	    JTextArea		      infoArea			  = new JTextArea();
	    JTextField		      anzahlField		  = new JTextField(3);
	    JLabel		      stueckLabel		  = new JLabel("Stück");
	    JTextField		      pkggroesseField		  = new JTextField("Packungsgröße",15);
	    JTextField		      artNrField		  = new JTextField("Artikelnummer",15);
	    JTextField		      bestandField		  = new JTextField("Bestand",15);
	    
	    JButton		      inWarenkorbButton		  = new JButton("Hinzufügen");
	    JButton		      neuAnlegenButton		  = new JButton("Neu");
	    JButton		      aendernButton		  = new JButton("Ändern");
	    JButton		      loeschenButton		  = new JButton("Löschen");
	
	 String bezeichnungStore = "";
	    double preisStore = 0;
	    int packungsgroesseStore = 0;
	    int bestandStore = 0;

	public ArtikelVerwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks listener) {
		super(server, user, listener);
		
		detailArea.add(new JLabel("Artikeldetails:"),"wrap");
		detailArea.add(picture, "w 160!,h 120!");
		detailArea.add(bezeichnungField, "split 2");
		detailArea.add(artNrField, "wrap");
		detailArea.add(preisField, "split 3");
		detailArea.add(pkggroesseField);
		detailArea.add(bestandField, "wrap");
		detailArea.add(infoLabel, "wrap");
		detailArea.add(infoArea, "w 100%");
			
		picture.setBackground(Color.lightGray);
		
		mitarbeiterButtons.add(neuAnlegenButton, "w 100!");
		mitarbeiterButtons.add(aendernButton, "w 100!");
		mitarbeiterButtons.add(loeschenButton, "w 100!");
		
		kundenButtons.add(anzahlField, "split 2, w 30!");
		kundenButtons.add(stueckLabel, "w 40!");
		kundenButtons.add(inWarenkorbButton, "w 100!");
		
		aendernButton.addActionListener(new ArtikelBearbeitenListener());
		neuAnlegenButton.addActionListener(new ArtikelNeuAnlegenListener());
		loeschenButton.addActionListener(new ArtikelLoeschenListener());
		
		if(user instanceof Kunde){
		    buttonArea = kundenButtons;
		} else {
		    buttonArea = mitarbeiterButtons;
		}
		
		artNrField.setEditable(false);
		bezeichnungField.setEditable(false);
		preisField.setEditable(false);
		pkggroesseField.setEditable(false);
		bestandField.setEditable(false);
		
		this.add(detailArea,"wrap");
		this.add(buttonArea,"wrap");
		
		detailArea.setBackground(Color.WHITE);
		bezeichnungField.setFont(new Font("Arial", Font.BOLD, 30));
		preisField.setFont(new Font("Arial", Font.BOLD, 15));
		infoArea.setSize(600, 100);
		infoArea.setLineWrap(true);
		infoArea.setWrapStyleWord(true);
		
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

			if (e.getSource().equals(aendernButton) && !isBeingChanged) {
				if (!artNrField.getText().equals("")) {
					// Felder editierbar machen
					bezeichnungField.setEditable(true);
					preisField.setEditable(true);
					pkggroesseField.setEditable(true);
					bestandField.setEditable(true);
					// Buttons anpassen
					aendernButton.setText("OK");
					isBeingChanged = true;
				}
			} else if (e.getSource().equals(aendernButton) && !isBeingChanged) {
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
								aendernButton.setText("Ändern");
								isBeingChanged = false;
								
								// Felder editierbar machen
								bezeichnungField.setEditable(false);
								preisField.setEditable(false);
								pkggroesseField.setEditable(false);
								bestandField.setEditable(false);
								
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

			if (e.getSource().equals(neuAnlegenButton) && !isBeingCreated) {
				// Artikelnummer ausblenden, kann nicht neu angegeben werden
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
				neuAnlegenButton.setText("OK");
				isBeingCreated = true;
			} else if (e.getSource().equals(neuAnlegenButton) && isBeingCreated) {
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
								artNrField.setVisible(true);
								// Felder editierbar machen
								bezeichnungField.setEditable(false);
								preisField.setEditable(false);
								pkggroesseField.setEditable(false);
								bestandField.setEditable(false);
								// Buttons anpassen
								neuAnlegenButton.setText("Neu");
								isBeingCreated = false;
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

	public void resetStores(){
	    bezeichnungStore = "";
	    preisStore = 0;
	    packungsgroesseStore = 0;
	    bestandStore = 0;
	}
	
	@Override
	public void reset() {


		this.art = null;
		artNrField.setText("");
		bezeichnungField.setText("");
		preisField.setText("");
		pkggroesseField.setText("");
		bestandField.setText("");
		
		bezeichnungField.setEditable(false);
		preisField.setEditable(false);
		pkggroesseField.setEditable(false);
		bestandField.setEditable(false);
		
		resetStores();
	    
	}	
	
	class ArtikelInWarenkorbListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				server.artikelInWarenkorbLegen(art.getArtikelnummer(), Integer.parseInt(anzahlField.getText()), user.getId(),user);
				listener.artikelInWarenkorb();
				anzahlField.setText("");
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Keine gueltige Anzahl!");
			} catch (ArticleNonexistantException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			} catch (ArticleStockNotSufficientException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			} catch (AccessRestrictedException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			} catch (InvalidAmountException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			} catch (ArrayIndexOutOfBoundsException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Kein Artikel ausgewählt");
			} catch (ArticleAlreadyInBasketException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			} catch(PersonNonexistantException e1) {
			    JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			}
		}
	}
	
	class VerlaufAnzeigenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				//Artikel artikel = server.artikelSuchen(art.getArtikelnummer(), user);
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				for (Entry<Integer, Integer> ent : art.getBestandsverlauf().entrySet()) {
					int dayOfYear = ent.getKey();
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
					String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "."
							+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + ".";
					dataset.addValue((int) ent.getValue(), "Bestand", date);
				}
				JFreeChart chart = ChartFactory.createLineChart("Bestandsverlauf", "Tag", "Bestand", dataset);
				ChartFrame chartFrame = new ChartFrame("Bestandsverlauf", chart);
				chartFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				chartFrame.pack();
				chartFrame.setVisible(true);
			} catch (ArrayIndexOutOfBoundsException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Es muss ein Artikel ausgewählt werden!");
			}
		}
	}
}
