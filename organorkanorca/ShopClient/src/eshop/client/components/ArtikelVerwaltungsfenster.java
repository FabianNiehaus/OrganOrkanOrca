package eshop.client.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

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

// TODO: Auto-generated Javadoc
/**
 * The Class ArtikelVerwaltungsfenster.
 */
public class ArtikelVerwaltungsfenster extends Verwaltungsfenster {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID			= -107879108721906207L;
	
	/** The aendern button. */
	JButton							aendernButton				= new JButton("Ändern");
	
	/** The anzahl field. */
	JTextField						anzahlField					= new JTextField(3);
	
	/** The Artikel. */
	Artikel							art;
	
	/** The Artikel nr field. */
	JTextField						artNrField					= new JTextField(15);
	
	/** The Artikel nr label. */
	JLabel							artNrLabel					= new JLabel("Artikel Nr.:");
	
	/** The bestand field. */
	JTextField						bestandField				= new JTextField(15);
	
	/** The bestand label. */
	JLabel							bestandLabel				= new JLabel("Verfügbar:");
	
	/** The bestand store. */
	int								bestandStore				= 0;
	
	/** The bezeichnung field. */
	JTextField						bezeichnungField			= new JTextField("Bezeichnung", 12);
	
	/** The bezeichnung store. */
	String							bezeichnungStore			= "";
	
	/** The button area. */
	JPanel							buttonArea					= new JPanel();
	
	/** The detail area. */
	JPanel							detailArea					= new JPanel();
	
	/** The image label. */
	JLabel							imageLabel					= new JLabel();
	
	/** The info area. */
	JTextArea						infoArea						= new JTextArea();
	
	/** The info label. */
	JLabel							infoLabel					= new JLabel("Informationen:");
	
	/** The in warenkorb button. */
	JButton							inWarenkorbButton			= new JButton("Hinzufügen");
	
	/** The kunden buttons. */
	JPanel							kundenButtons				= new JPanel();
	
	/** The loeschen button. */
	JButton							loeschenButton				= new JButton("Löschen");
	
	/** The mitarbeiter buttons. */
	JPanel							mitarbeiterButtons		= new JPanel();
	
	/** The neu anlegen button. */
	JButton							neuAnlegenButton			= new JButton("Neu");
	
	/** The packungsgroesse store. */
	int								packungsgroesseStore		= 0;
	
	/** The picture. */
	JPanel							picture						= new JPanel();
	
	/** The pkggroesse field. */
	JTextField						pkggroesseField			= new JTextField(15);
	
	/** The pkggroesse label. */
	JLabel							pkggroesseLabel			= new JLabel("Packungsgröße:");
	
	/** The preis label. */
	JLabel							preisLabel					= new JLabel("Einzelpreis:");
	
	/** The preis field. */
	JTextField						preisField					= new JTextField("Preis", 15);
	
	/** The preis store. */
	double							preisStore					= 0;
	
	/** The stueck label. */
	JLabel							stueckLabel					= new JLabel("Stück");
	
	/** The verlauf anzeigen button. */
	JButton							verlaufAnzeigenButton	= new JButton("Verlauf anzeigen");
	
	/** The info area container. */
	JScrollPane						infoAreaContainer			= new JScrollPane(infoArea);
	
	/** The info store. */
	String							infoStore					= "";
	
	/** The euro label. */
	JLabel							euroLabel					= new JLabel("\u20ac");

	/**
	 * Instantiates a new artikel verwaltungsfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param verwaltungsfensterCallbacks
	 *           the verwaltungsfenster callbacks
	 */
	public ArtikelVerwaltungsfenster(ShopRemote server, Person user,
			VerwaltungsfensterCallbacks verwaltungsfensterCallbacks) {
		super(server, user, verwaltungsfensterCallbacks);
		this.setLayout(new MigLayout("", "114[]0"));
		detailArea.setLayout(new MigLayout());
		buttonArea.setLayout(new MigLayout());
		mitarbeiterButtons.setLayout(new MigLayout());
		kundenButtons.setLayout(new MigLayout());
		detailArea.add(new JLabel("Artikeldetails:"), "wrap 10!");
		detailArea.add(picture, "w 160!,h 120!, span 1 4");
		detailArea.add(bezeichnungField, "wrap, span 10");
		detailArea.add(preisLabel);
		detailArea.add(preisField, "w 60");
		detailArea.add(euroLabel, "gapright 20");
		detailArea.add(artNrLabel);
		detailArea.add(artNrField, "w 30!, gapright 20");
		detailArea.add(bestandLabel);
		detailArea.add(bestandField, "w 30!, gapright 20");
		detailArea.add(pkggroesseLabel);
		detailArea.add(pkggroesseField, "w 30!, wrap 5!");
		detailArea.add(infoLabel, "wrap, span 3");
		detailArea.add(infoAreaContainer, "w 650!, h 50, span 10 0");
		preisField.setHorizontalAlignment(SwingConstants.RIGHT);
		artNrField.setHorizontalAlignment(SwingConstants.RIGHT);
		bestandField.setHorizontalAlignment(SwingConstants.RIGHT);
		pkggroesseField.setHorizontalAlignment(SwingConstants.RIGHT);
		detailArea.setBackground(Color.WHITE);
		detailArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		picture.setBackground(Color.white);
		picture.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		imageLabel.setIcon(new ImageIcon("pictures/orkan.jpg"));
		picture.add(imageLabel);
		bezeichnungField.setFont(new Font("Arial", Font.BOLD, 20));
		preisLabel.setFont(new Font("Arial", Font.BOLD, 14));
		preisField.setFont(new Font("Arial", Font.BOLD, 14));
		euroLabel.setFont(new Font("Arial", Font.BOLD, 14));
		setInputFieldsBorder(null);
		setInputFieldsColor(null);
		setInputFieldsEditable(false);
		infoArea.setLineWrap(true);
		infoArea.setWrapStyleWord(true);
		mitarbeiterButtons.add(verlaufAnzeigenButton);
		mitarbeiterButtons.add(neuAnlegenButton, "w 100!");
		mitarbeiterButtons.add(aendernButton, "w 100!");
		mitarbeiterButtons.add(loeschenButton, "w 100!");
		kundenButtons.add(anzahlField, "split 2, w 30!");
		kundenButtons.add(stueckLabel, "w 40!");
		kundenButtons.add(inWarenkorbButton, "w 100!");
		verlaufAnzeigenButton.addActionListener(new VerlaufAnzeigenListener());
		aendernButton.addActionListener(new ArtikelBearbeitenListener());
		neuAnlegenButton.addActionListener(new ArtikelNeuAnlegenListener());
		loeschenButton.addActionListener(new ArtikelLoeschenListener());
		inWarenkorbButton.addActionListener(new ArtikelInWarenkorbListener());
		if (user instanceof Kunde) {
			buttonArea = kundenButtons;
		} else {
			buttonArea = mitarbeiterButtons;
		}
		artNrField.setEditable(false);
		bezeichnungField.setEditable(false);
		preisField.setEditable(false);
		pkggroesseField.setEditable(false);
		bestandField.setEditable(false);
		this.add(detailArea, "w 100%, h 200!, wrap");
		this.add(buttonArea, "right");
		this.setVisible(true);
	}

	/**
	 * Artikel anzeigen.
	 *
	 * @param art
	 *           the artikel
	 */
	public void artikelAnzeigen(Artikel art) {

		reset();
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
		infoArea.setText(art.getArtikelinfo());
		imageLabel.setIcon(new ImageIcon(art.getPicture()));
		infoArea.setCaretPosition(0);
		setStores(art);
	}

	/**
	 * Clear input fields.
	 */
	private void clearInputFields() {

		bezeichnungField.setText("");
		preisField.setText("");
		pkggroesseField.setText("");
		bestandField.setText("");
		infoArea.setText("");
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
		setInputFieldsEditable(false);
		imageLabel.setIcon(new ImageIcon("pictures/orkan.jpg"));
		aendernButton.setText("Andern");
		isBeingChanged = false;
		neuAnlegenButton.setText("Neu");
		isBeingCreated = false;
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
		infoStore = "";
	}

	/**
	 * Sets the input fields border.
	 *
	 * @param border
	 *           the new input fields border
	 */
	private void setInputFieldsBorder(Border border) {

		bezeichnungField.setBorder(border);
		preisField.setBorder(border);
		artNrField.setBorder(border);
		pkggroesseField.setBorder(border);
		bestandField.setBorder(border);
		infoAreaContainer.setBorder(border);
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
		infoArea.setBackground(color);
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
		infoArea.setEditable(editable);
	}

	/**
	 * Sets the stores.
	 *
	 * @param art
	 *           the new stores
	 */
	public void setStores(Artikel art) {

		bezeichnungStore = art.getBezeichnung();
		preisStore = art.getPreis();
		if (art instanceof Massengutartikel) {
			packungsgroesseStore = ((Massengutartikel) art).getPackungsgroesse();
		} else {
			packungsgroesseStore = 1;
		}
		bestandStore = art.getBestand();
		infoStore = art.getArtikelinfo();
	}

	/**
	 * The listener interface for receiving artikelBearbeiten events. The class
	 * that is interested in processing a artikelBearbeiten event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addArtikelBearbeitenListener<code>
	 * method. When the artikelBearbeiten event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ArtikelBearbeitenEvent
	 */
	public class ArtikelBearbeitenListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(aendernButton) && !isBeingChanged && !isBeingCreated) {
				if (!artNrField.getText().equals("")) {
					setInputFieldsColor(Color.LIGHT_GRAY);
					setInputFieldsEditable(true);
					bestandField.setToolTipText(
							"<html>Bestand erhöhen/verringern mit +<Zuwachs> und -<Abgang>.<br>Bestand auf Null setzen mit 0.</html>");
					aendernButton.setText("OK");
					isBeingChanged = true;
				}
			} else if (e.getSource().equals(aendernButton) && isBeingChanged) {
				String bezeichnung = bezeichnungField.getText();
				String artikelinfo = infoArea.getText().replace("\n", "").replace("\r", "");
				;
				try {
					int bestand = 0;
					String zeile = bestandField.getText();
					String operator = zeile.substring(0, 1);
					if (operator.equals("+") || operator.equals("-")) {
						bestand = Integer.parseInt(zeile.substring(1));
					} else if (operator.equals("0")) {
						bestand = Integer.parseInt(zeile);
					} else if (operator.matches("[1-9]")) {
						bestand = Integer.parseInt(zeile);
						if (bestand == bestandStore) {
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
										packungsgroesse, artikelinfo);
								artikelAnzeigen(art);
								aendernButton.setText("Andern");
								isBeingChanged = false;
								setInputFieldsColor(Color.WHITE);
								setInputFieldsEditable(false);
								setStores(art);
								bestandField.setToolTipText("");
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
							JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Keine gueltige Packungsgröße");
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

	/**
	 * The listener interface for receiving artikelInWarenkorb events. The class
	 * that is interested in processing a artikelInWarenkorb event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the component's
	 * <code>addArtikelInWarenkorbListener<code> method. When the
	 * artikelInWarenkorb event occurs, that object's appropriate method is
	 * invoked.
	 *
	 * @see ArtikelInWarenkorbEvent
	 */
	private class ArtikelInWarenkorbListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				server.artikelInWarenkorbLegen(art.getArtikelnummer(), Integer.parseInt(anzahlField.getText()),
						user.getId(), user);
				anzahlField.setText("");
				verwaltungsfensterCallbacks.warenkorbAktualisieren();
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Artikel zum Warenkorb hinzugefügt");
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
			} catch (PersonNonexistantException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, e1.getMessage());
			}
		}
	}

	/**
	 * The listener interface for receiving artikelLoeschen events. The class
	 * that is interested in processing a artikelLoeschen event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addArtikelLoeschenListener<code>
	 * method. When the artikelLoeschen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ArtikelLoeschenEvent
	 */
	private class ArtikelLoeschenListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
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

	/**
	 * The listener interface for receiving artikelNeuAnlegen events. The class
	 * that is interested in processing a artikelNeuAnlegen event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addArtikelNeuAnlegenListener<code>
	 * method. When the artikelNeuAnlegen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ArtikelNeuAnlegenEvent
	 */
	private class ArtikelNeuAnlegenListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(neuAnlegenButton) && !isBeingCreated && !isBeingChanged) {
				artNrField.setVisible(false);
				setInputFieldsColor(Color.LIGHT_GRAY);
				clearInputFields();
				setInputFieldsEditable(true);
				neuAnlegenButton.setText("OK");
				isBeingCreated = true;
			} else if (e.getSource().equals(neuAnlegenButton) && isBeingCreated) {
				String bezeichnung = bezeichnungField.getText();
				String artikelinfo = infoArea.getText().replace("\n", "").replace("\r", "");
				;
				try {
					int bestand = Integer.parseInt(bestandField.getText());
					try {
						double preis = Double.parseDouble(preisField.getText());
						try {
							int packungsgroesse = Integer.parseInt(pkggroesseField.getText());
							try {
								Artikel art = server.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse, user,
										artikelinfo, "");
								artikelAnzeigen(art);
								artNrField.setVisible(true);
								setInputFieldsColor(Color.WHITE);
								setInputFieldsEditable(false);
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
							JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Keine gueltige Packungsgröße");
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

	/**
	 * The listener interface for receiving verlaufAnzeigen events. The class
	 * that is interested in processing a verlaufAnzeigen event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addVerlaufAnzeigenListener<code>
	 * method. When the verlaufAnzeigen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see VerlaufAnzeigenEvent
	 */
	private class VerlaufAnzeigenListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				// Artikel artikel = server.artikelSuchen(art.getArtikelnummer(),
				// user);
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				for (Entry<Integer, Integer> ent : art.getBestandsverlauf().entrySet()) {
					if(ent != null){
						int dayOfYear = ent.getKey();
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
						String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "."
								+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + ".";
						dataset.addValue((int) ent.getValue(), "Bestand", date);
					}
				}
				JFreeChart chart = ChartFactory.createLineChart("Bestandsverlauf " + art.getBezeichnung(), "Tag", "Bestand", dataset);
				ChartFrame chartFrame = new ChartFrame("Bestandsverlauf", chart);
				chartFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				chartFrame.pack();
				//chartFrame.setResizable(false);
				chartFrame.setLocationRelativeTo(null);
				chartFrame.setVisible(true);
			} catch (ArrayIndexOutOfBoundsException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Es muss ein Artikel ausgewählt werden!");
			}
		}
	}
}
