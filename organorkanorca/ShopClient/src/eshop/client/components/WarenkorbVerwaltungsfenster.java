package eshop.client.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import eshop.client.util.Verwaltungsfenster;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.data_objects.Person;
import eshop.common.data_objects.Rechnung;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.ArticleStockNotSufficientException;
import eshop.common.exceptions.BasketNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class WarenkorbVerwaltungsfenster extends Verwaltungsfenster {

	/**
	 * 
	 */
	private static final long	serialVersionUID		= -107879108721906207L;
	JButton							aendernButton			= new JButton("Anzahl ändern");
	JTextField						anzahlField				= new JTextField(3);
	Artikel							art;
	JTextField						artNrField				= new JTextField(15);
	JLabel							artNrLabel				= new JLabel("Artikel Nr.:");
	JTextField						bestandField			= new JTextField(15);
	JLabel							bestandLabel			= new JLabel("Bestand:");
	int								bestandStore			= 0;
	JTextField						bezeichnungField		= new JTextField("Bezeichnung", 12);
	String							bezeichnungStore		= "";
	JPanel							buttonArea				= new JPanel();
	JPanel							detailArea				= new JPanel();
	JLabel							imageLabel				= new JLabel();
	JTextArea						infoArea					= new JTextArea();
	JLabel							infoLabel				= new JLabel("Informationen:");
	JButton							leerenButton			= new JButton("Warenkorb leeren");
	JPanel							aktionsButtons			= new JPanel();
	JButton							loeschenButton			= new JButton("Artikel löschen");
	JButton							kaufenButton			= new JButton("Alles kaufen");
	int								packungsgroesseStore	= 0;
	JPanel							picture					= new JPanel();
	JTextField						pkggroesseField		= new JTextField(15);
	JLabel							pkggroesseLabel		= new JLabel("Packungsgröße:");
	JTextField						preisField				= new JTextField("Preis", 15);
	double							preisStore				= 0;
	JLabel							anzahlLabel				= new JLabel("Stück");
	int							anzahlStore				= 0;
	

	public WarenkorbVerwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks listener) {
		super(server, user, listener);
		this.setLayout(new MigLayout("", "114[]0"));
		detailArea.setLayout(new MigLayout("", "[]10[]"));
		buttonArea.setLayout(new MigLayout());
		aktionsButtons.setLayout(new MigLayout());
		detailArea.add(new JLabel("Artikeldetails:"), "wrap 10!");
		detailArea.add(picture, "w 160!,h 120!, span 2 4");
		detailArea.add(bezeichnungField, "wrap");
		detailArea.add(preisField, "w 70!");
		detailArea.add(artNrLabel, "right");
		detailArea.add(artNrField, "w 30!");
		detailArea.add(bestandLabel, "right");
		detailArea.add(bestandField, "w 30!");
		detailArea.add(pkggroesseLabel, "right");
		detailArea.add(pkggroesseField, "w 30!, wrap 5!");
		detailArea.add(infoLabel, "wrap");
		detailArea.add(infoArea, "w 100:650:900, span 7 0");
		detailArea.setBackground(Color.WHITE);
		detailArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		ImageIcon image = new ImageIcon("pictures/orkan.jpg");
		picture.setBackground(Color.white);
		picture.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		imageLabel.setIcon(image);
		picture.add(imageLabel);
		// detailArea.setBorder(BorderFactory.createTitledBorder("Artikeldetails:"));
		bezeichnungField.setFont(new Font("Arial", Font.BOLD, 20));
		preisField.setFont(new Font("Arial", Font.BOLD, 14));
		// Border der Felder ausblenden
		bezeichnungField.setBorder(null);
		preisField.setBorder(null);
		artNrField.setBorder(null);
		pkggroesseField.setBorder(null);
		bestandField.setBorder(null);
		// Hintergrund der Felder ausblenden
		setInputFieldsColor(null);
		
		infoArea.setLineWrap(true);
		infoArea.setWrapStyleWord(true);
		infoArea.setText("Als Orkan werden im weiteren Sinn Winde mit Geschwindigkeiten von mindestens "
				+ "64 kn (117,7 km/h = 32,7 m/s) bezeichnet. Orkane k�nnen "
				+ "massive Verw�stungen anrichten und bilden auf See eine Gefahr f�r den Schiffsverkehr.");
		
		aktionsButtons.add(anzahlField, "w 30!");
		aktionsButtons.add(anzahlLabel);
		aktionsButtons.add(aendernButton);
		aktionsButtons.add(loeschenButton);
		aktionsButtons.add(kaufenButton);
		aktionsButtons.add(leerenButton);
		aendernButton.addActionListener(new WarenkorbActionListener());
		kaufenButton.addActionListener(new WarenkorbActionListener());
		loeschenButton.addActionListener(new WarenkorbActionListener());
		leerenButton.addActionListener(new WarenkorbActionListener());

		buttonArea = aktionsButtons;
	
		setInputFieldsEditable(false);
		
		this.add(detailArea, "w 100%, h 200!, wrap");
		this.add(buttonArea, "right");
		this.setVisible(true);
	}

	/**
	 * @param art
	 */
	public void artikelAnzeigen(Artikel art, int anzahl) {
		
		reset();
		
		artNrField.setText(String.valueOf(art.getArtikelnummer()));
		bezeichnungField.setText(art.getBezeichnung());
		preisField.setText(String.valueOf(art.getPreis())+"�");
		if (art instanceof Massengutartikel) {
			pkggroesseField.setText(String.valueOf(((Massengutartikel) art).getPackungsgroesse()));
		} else {
			pkggroesseField.setText("1");
		}
		bestandField.setText(String.valueOf(art.getBestand()));
		anzahlField.setText(String.valueOf(anzahl));
		setStores(art, anzahl);
	}

	public Artikel getArtikel() {

		return art;
	}

	@Override
	public void reset() {

		this.art = null;
		
		clearInputFields();
		setInputFieldsColor(Color.WHITE);

		aendernButton.setText("Andern");
		isBeingChanged = false;
		kaufenButton.setText("Neu");
		isBeingCreated = false;
		
		resetStores();
	}

	public void resetStores() {

		bezeichnungStore = "";
		preisStore = 0;
		packungsgroesseStore = 0;
		bestandStore = 0;
	}

	public void setStores(Artikel art, int anzahl) {
		
		bezeichnungStore = art.getBezeichnung();
		preisStore = art.getPreis();
		if (art instanceof Massengutartikel) {
			packungsgroesseStore = ((Massengutartikel) art).getPackungsgroesse();
		} else {
			packungsgroesseStore = 1;
		}
		bestandStore = art.getBestand();
		anzahlStore = anzahl;
	}

	class WarenkorbActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// Anzahl eines Artikels im Warenkorn ändern
			if (e.getSource().equals(aendernButton)) {
				try {
						int anz = Integer.parseInt(anzahlField.getText());
						if (anz > 0) {
							server.artikelInWarenkorbAendern(art.getArtikelnummer(), anz, user);
						} else {
							throw new InvalidAmountException();
						}
				} catch (ArticleStockNotSufficientException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (BasketNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, "Keine gueltige Anzahl!");
				} catch (InvalidAmountException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (ArticleNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (PersonNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				}
			} else if (e.getSource().equals(loeschenButton)) {
				try {
						server.artikelAusWarenkorbEntfernen(art.getArtikelnummer(), user);
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (ArticleNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (PersonNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				}
			} else if (e.getSource().equals(leerenButton)) {
				try {
					server.warenkorbLeeren(user);
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (PersonNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				}
			} else if (e.getSource().equals(kaufenButton)) {
				try {
					// Formatierungsvorlage fuer Datum
					DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
					Rechnung re = server.warenkorbKaufen(user);
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
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, rechnungsString);
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (InvalidAmountException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (PersonNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				}
			}
		}
	}
	
	private void setInputFieldsEditable(boolean editable) {

		bezeichnungField.setEditable(editable);
		preisField.setEditable(editable);
		pkggroesseField.setEditable(editable);
		bestandField.setEditable(editable);
	}

	private void setInputFieldsColor(Color color) {

		bezeichnungField.setBackground(color);
		preisField.setBackground(color);
		pkggroesseField.setBackground(color);
		bestandField.setBackground(color);
	}
	
	private void clearInputFields() {

		bezeichnungField.setText("");
		preisField.setText("");
		pkggroesseField.setText("");
		bestandField.setText("");
		anzahlField.setText("");
	}
}
