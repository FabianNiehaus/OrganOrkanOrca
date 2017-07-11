package eshop.client.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

// TODO: Auto-generated Javadoc
/**
 * The Class WarenkorbVerwaltungsfenster.
 */
public class WarenkorbVerwaltungsfenster extends Verwaltungsfenster {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID		= -107879108721906207L;
	
	/** The aendern button. */
	JButton							aendernButton			= new JButton("Anzahl ändern");
	
	/** The aktions buttons. */
	JPanel							aktionsButtons			= new JPanel();
	
	/** The anzahl field. */
	JTextField						anzahlField				= new JTextField(3);
	
	/** The anzahl label. */
	JLabel							anzahlLabel				= new JLabel("Im Warenkorb:");
	
	/** The anzahl store. */
	int								anzahlStore				= 0;
	
	/** The Artikel. */
	Artikel							art;
	
	/** The Artikel nr field. */
	JTextField						artNrField				= new JTextField(15);
	
	/** The Artikel nr label. */
	JLabel							artNrLabel				= new JLabel("Artikel Nr.:");
	
	/** The bestand field. */
	JTextField						bestandField			= new JTextField(15);
	
	/** The bestand label. */
	JLabel							bestandLabel			= new JLabel("Bestand:");
	
	/** The bestand store. */
	int								bestandStore			= 0;
	
	/** The bezeichnung field. */
	JTextField						bezeichnungField		= new JTextField("Bezeichnung", 12);
	
	/** The bezeichnung store. */
	String							bezeichnungStore		= "";
	
	/** The button area. */
	JPanel							buttonArea				= new JPanel();
	
	/** The detail area. */
	JPanel							detailArea				= new JPanel();
	
	/** The image label. */
	JLabel							imageLabel				= new JLabel();
	
	/** The info area. */
	JTextArea						infoArea					= new JTextArea();
	
	/** The info label. */
	JLabel							infoLabel				= new JLabel("Informationen:");
	
	/** The kaufen button. */
	JButton							kaufenButton			= new JButton("Alles kaufen");
	
	/** The leeren button. */
	JButton							leerenButton			= new JButton("Warenkorb leeren");
	
	/** The loeschen button. */
	JButton							loeschenButton			= new JButton("Artikel löschen");
	
	/** The packungsgroesse store. */
	int								packungsgroesseStore	= 0;
	
	/** The picture. */
	JPanel							picture					= new JPanel();
	
	/** The pkggroesse field. */
	JTextField						pkggroesseField		= new JTextField(15);
	
	/** The pkggroesse label. */
	JLabel							pkggroesseLabel		= new JLabel("Packungsgröße:");
	
	/** The preis field. */
	JTextField						preisField				= new JTextField("Preis", 15);
	
	/** The preis store. */
	double							preisStore				= 0;

	/**
	 * Instantiates a new warenkorb verwaltungsfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param verwaltungsfensterCallbacks
	 *           the verwaltungsfenster callbacks
	 */
	public WarenkorbVerwaltungsfenster(ShopRemote server, Person user,
			VerwaltungsfensterCallbacks verwaltungsfensterCallbacks) {
		super(server, user, verwaltungsfensterCallbacks);
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
		aktionsButtons.add(anzahlLabel);
		aktionsButtons.add(anzahlField, "w 40!");
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
	 * Artikel anzeigen.
	 *
	 * @param art
	 *           the artikel
	 * @param anzahl
	 *           the anzahl
	 */
	public void artikelAnzeigen(Artikel art, int anzahl) {

		reset();
		this.art = art;
		artNrField.setText(String.valueOf(art.getArtikelnummer()));
		bezeichnungField.setText(art.getBezeichnung());
		preisField.setText(String.valueOf(art.getPreis()) + "�");
		if (art instanceof Massengutartikel) {
			pkggroesseField.setText(String.valueOf(((Massengutartikel) art).getPackungsgroesse()));
		} else {
			pkggroesseField.setText("1");
		}
		bestandField.setText(String.valueOf(art.getBestand()));
		anzahlField.setText(String.valueOf(anzahl));
		setStores(art, anzahl);
	}

	/**
	 * Clear input fields.
	 */
	private void clearInputFields() {

		bezeichnungField.setText("");
		preisField.setText("");
		pkggroesseField.setText("");
		bestandField.setText("");
		anzahlField.setText("");
	}

	/**
	 * Gets the artikel.
	 *
	 * @return the artikel
	 */
	public Artikel getArtikel() {

		return art;
	}

	/* (non-Javadoc)
	 * @see eshop.client.util.Verwaltungsfenster#reset()
	 */
	@Override
	public void reset() {

		this.art = null;
		clearInputFields();
		setInputFieldsColor(Color.WHITE);
		resetStores();
	}

	/**
	 * Reset stores.
	 */
	public void resetStores() {

		bezeichnungStore = "";
		preisStore = 0;
		packungsgroesseStore = 0;
		bestandStore = 0;
	}

	/**
	 * Sets the input fields color.
	 *
	 * @param color
	 *           the new input fields color
	 */
	private void setInputFieldsColor(Color color) {

		bezeichnungField.setBackground(color);
		preisField.setBackground(color);
		pkggroesseField.setBackground(color);
		bestandField.setBackground(color);
	}

	/**
	 * Sets the input fields editable.
	 *
	 * @param editable
	 *           the new input fields editable
	 */
	private void setInputFieldsEditable(boolean editable) {

		bezeichnungField.setEditable(editable);
		preisField.setEditable(editable);
		pkggroesseField.setEditable(editable);
		bestandField.setEditable(editable);
	}

	/**
	 * Sets the stores.
	 *
	 * @param art
	 *           the artikel
	 * @param anzahl
	 *           the anzahl
	 */
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

	/**
	 * The listener interface for receiving warenkorbAction events. The class
	 * that is interested in processing a warenkorbAction event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addWarenkorbActionListener<code>
	 * method. When the warenkorbAction event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see WarenkorbActionEvent
	 */
	class WarenkorbActionListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			// Anzahl eines Artikels im Warenkorn ändern
			if (e.getSource().equals(aendernButton)) {
				try {
					int anz = Integer.parseInt(anzahlField.getText());
					if (anz > 0) {
						server.artikelInWarenkorbAendern(art.getArtikelnummer(), anz, user);
						verwaltungsfensterCallbacks.warenkorbAktualisieren();
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
					verwaltungsfensterCallbacks.warenkorbAktualisieren();
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
					verwaltungsfensterCallbacks.warenkorbAktualisieren();
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (PersonNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				}
			} else if (e.getSource().equals(kaufenButton)) {
				try {
					if (!verwaltungsfensterCallbacks.istWarenkorbLeer()) {
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
						rechnungsString += "Gesamtbetrag: " + String.format("%.2f", re.getGesamt()) + "€";
						JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, rechnungsString);
						verwaltungsfensterCallbacks.warenkorbAktualisieren();
					}
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
}
