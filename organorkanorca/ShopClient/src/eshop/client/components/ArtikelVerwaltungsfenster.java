package eshop.client.components;

import java.awt.Color;
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
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

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
	private static final long	serialVersionUID			= -107879108721906207L;
	JButton							aendernButton				= new JButton("�ndern");
	JTextField						anzahlField					= new JTextField(3);
	Artikel							art;
	JTextField						artNrField					= new JTextField(15);
	JLabel							artNrLabel					= new JLabel("Artikel Nr.:");
	JTextField						bestandField				= new JTextField(15);
	JLabel							bestandLabel				= new JLabel("Verf�gbar:");
	int								bestandStore				= 0;
	JTextField						bezeichnungField			= new JTextField("Bezeichnung", 12);
	String							bezeichnungStore			= "";
	JPanel							buttonArea					= new JPanel();
	JPanel							detailArea					= new JPanel();
	JLabel							imageLabel					= new JLabel();
	JTextArea						infoArea						= new JTextArea();
	JLabel							infoLabel					= new JLabel("Informationen:");
	JButton							inWarenkorbButton			= new JButton("Hinzufügen");
	JPanel							kundenButtons				= new JPanel();
	JButton							loeschenButton				= new JButton("L�schen");
	JPanel							mitarbeiterButtons		= new JPanel();
	JButton							neuAnlegenButton			= new JButton("Neu");
	int								packungsgroesseStore		= 0;
	JPanel							picture						= new JPanel();
	JTextField						pkggroesseField			= new JTextField(15);
	JLabel							pkggroesseLabel			= new JLabel("Packungsgr��e:");
	JTextField						preisField					= new JTextField("Preis", 15);
	double							preisStore					= 0;
	JLabel							stueckLabel					= new JLabel("St�ck");
	JButton							verlaufAnzeigenButton	= new JButton("Verlauf anzeigen");
	JScrollPane						infoAreaContainer			= new JScrollPane(infoArea);
	String infoStore = "";

	public ArtikelVerwaltungsfenster(ShopRemote server, Person user,
			VerwaltungsfensterCallbacks verwaltungsfensterCallbacks) {
		super(server, user, verwaltungsfensterCallbacks);
		this.setLayout(new MigLayout("", "114[]0"));
		detailArea.setLayout(new MigLayout("", "[]10[]"));
		buttonArea.setLayout(new MigLayout());
		mitarbeiterButtons.setLayout(new MigLayout());
		kundenButtons.setLayout(new MigLayout());
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
		detailArea.add(infoAreaContainer, "w 100:650:900, h 50, span 7 0");
		DefaultCaret caret = (DefaultCaret) infoArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
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

	private void setInputFieldsBorder(Border border) {

		bezeichnungField.setBorder(border);
		preisField.setBorder(border);
		artNrField.setBorder(border);
		pkggroesseField.setBorder(border);
		bestandField.setBorder(border);
		infoAreaContainer.setBorder(border);
	}

	/**
	 * @param art
	 */
	public void artikelAnzeigen(Artikel art) {

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
		infoArea.setText(art.getArtikelinfo());
		setStores(art);
	}

	private void clearInputFields() {

		bezeichnungField.setText("");
		preisField.setText("");
		pkggroesseField.setText("");
		bestandField.setText("");
		infoArea.setText("");
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
		neuAnlegenButton.setText("Neu");
		isBeingCreated = false;
		resetStores();
	}

	public void resetStores() {

		bezeichnungStore = "";
		preisStore = 0;
		packungsgroesseStore = 0;
		bestandStore = 0;
		infoStore = "";
	}

	private void setInputFieldsColor(Color color) {

		bezeichnungField.setBackground(color);
		preisField.setBackground(color);
		pkggroesseField.setBackground(color);
		bestandField.setBackground(color);
		infoArea.setBackground(color);
	}

	private void setInputFieldsEditable(boolean editable) {

		bezeichnungField.setEditable(editable);
		preisField.setEditable(editable);
		pkggroesseField.setEditable(editable);
		bestandField.setEditable(editable);
		infoArea.setEditable(editable);
	}

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

	public class ArtikelBearbeitenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(aendernButton) && !isBeingChanged && !isBeingCreated) {
				if (!artNrField.getText().equals("")) {
					setInputFieldsColor(Color.LIGHT_GRAY);
					setInputFieldsEditable(true);
					aendernButton.setText("OK");
					isBeingChanged = true;
				}
			} else if (e.getSource().equals(aendernButton) && isBeingChanged) {
				String bezeichnung = bezeichnungField.getText();
				String artikelinfo = infoArea.getText().replace("\n", "").replace("\r", "");;
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

	private class ArtikelInWarenkorbListener implements ActionListener {

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

	private class ArtikelLoeschenListener implements ActionListener {

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

	private class ArtikelNeuAnlegenListener implements ActionListener {

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
				String artikelinfo = infoArea.getText().replace("\n", "").replace("\r", "");;
				try {
					int bestand = Integer.parseInt(bestandField.getText());
					try {
						double preis = Double.parseDouble(preisField.getText());
						try {
							int packungsgroesse = Integer.parseInt(pkggroesseField.getText());
							try {
								Artikel art = server.erstelleArtikel(bezeichnung, bestand, preis, packungsgroesse, user, artikelinfo);
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

	private class VerlaufAnzeigenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				// Artikel artikel = server.artikelSuchen(art.getArtikelnummer(),
				// user);
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
				chartFrame.setLocationRelativeTo(null);
				chartFrame.setVisible(true);
			} catch (ArrayIndexOutOfBoundsException e1) {
				JOptionPane.showMessageDialog(ArtikelVerwaltungsfenster.this, "Es muss ein Artikel ausgewählt werden!");
			}
		}
	}
	
	
}
