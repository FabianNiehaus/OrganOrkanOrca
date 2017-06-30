package eshop.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import eshop.client.components.Artikelsichtfenster;
import eshop.client.components.Artikelverwaltungsfenster;
import eshop.client.components.Kundensichtfenster;
import eshop.client.components.Mitarbeitersichtfenster;
import eshop.client.components.Personenverwaltungsfenster;
import eshop.client.util.LoginListener;
import eshop.client.util.Sichtfenster;
import eshop.client.util.TableColumnAdjuster;
import eshop.client.util.Sichtfenster.SichtfensterCallbacks;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Massengutartikel;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.data_objects.Rechnung;
import eshop.common.data_objects.Warenkorb;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleAlreadyInBasketException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.ArticleStockNotSufficientException;
import eshop.common.exceptions.BasketNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.MaxIDsException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopEventListener;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class MainWindow extends JFrame implements ShopEventListener, SichtfensterCallbacks {

	Person			user;
	ShopRemote			server;
	LoginListener		loginListener;
	JPanel			main		  = (JPanel) this.getContentPane();
	JPanel			leftArea	  = new JPanel(new MigLayout());
	JPanel			rightArea	  = new JPanel(new MigLayout());
	JPanel			moduleButtons	  = new JPanel();
	JButton			artikelButton	  = new JButton("Artikel");
	JButton			kundenButton	  = new JButton("Kunden");
	JButton			mitarbeiterButton = new JButton("Mitarbeiter");
	JButton			shopButton	  = new JButton("Shop");
	JButton			logoutButton	  = new JButton("Logout");
	Kundensichtfenster		kundensichtfenster;
	Artikelsichtfenster		artikelsichtfenster;
	Mitarbeitersichtfenster	mitarbeitersichtfenster;
	ShopManagement		shopManagement;
	Warenkorbverwaltungsfenster	warenkorbverwaltungsfenster;
	Artikelverwaltungsfenster	artikelverwaltungsfenster;
	Personenverwaltungsfenster	kundenverwaltungsfenster;
	Personenverwaltungsfenster	mitarbeiterverwaltungsfenster;
	double			prefWidth	  = 0;
	double			maxWidthLeft	  = 0;
	double			maxWidthRight	  = 0;

	public MainWindow(String titel, Person user, ShopRemote server, LoginListener loginListener) {
		super(titel);
		this.user = user;
		this.server = server;
		this.loginListener = loginListener;
		initialize();
	}

	@Override
	public void handleArticleChanged(Artikel art) {

		try {
			artikelsichtfenster.auflistungInitialize();
			artikelsichtfenster.adjustColumns();
		} catch(AccessRestrictedException e) {
			removeAll();
			add(new JLabel(e.getMessage()));
		} catch(RemoteException e) {
			JOptionPane.showMessageDialog(artikelsichtfenster, e.getMessage());
		}
		if (user instanceof Kunde) {
			try {
				if (server.artikelInWarenkorb(art, user)) {
					warenkorbverwaltungsfenster.warenkorbAufrufen();
				}
			} catch(RemoteException | AccessRestrictedException e) {
				JOptionPane.showMessageDialog(artikelsichtfenster, e.getMessage());
			}
		} else if (user instanceof Mitarbeiter) {
			artikelverwaltungsfenster = new Artikelverwaltungsfenster();
		}
	}

	@Override
	public void handleEventChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleUserChanged() {
		// TODO Auto-generated method stub

	}

	public void initialize() {

		this.setLayout(new MigLayout("", "30[]30[]30", "30[]30"));
		artikelButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(artikelButton);
		kundenButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(kundenButton);
		mitarbeiterButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(mitarbeiterButton);
		shopButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(shopButton);
		logoutButton.addActionListener(new MenuButtonsActionListener());
		moduleButtons.add(logoutButton);
		leftArea.add(moduleButtons, "wrap, dock center");
		kundensichtfenster = new Kundensichtfenster();
		artikelsichtfenster = new Artikelsichtfenster();
		mitarbeitersichtfenster = new Mitarbeitersichtfenster();
		shopManagement = new ShopManagement();
		leftArea.add(artikelsichtfenster, "dock center");
		if (user instanceof Kunde) {
			warenkorbverwaltungsfenster = new Warenkorbverwaltungsfenster();
			rightArea.add(warenkorbverwaltungsfenster, "dock center");
		} else {
			artikelverwaltungsfenster = new Artikelverwaltungsfenster();
			rightArea.add(artikelverwaltungsfenster, "dock center");
		}
		main.add(leftArea);
		main.add(rightArea);
		setWindowSize();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * 
	 */
	public void setWindowSize() {

		if (artikelsichtfenster != null)
			maxWidthLeft = Math.max(maxWidthLeft, artikelsichtfenster.getPreferredSize().getWidth() + 15);
		if (kundensichtfenster != null)
			maxWidthLeft = Math.max(maxWidthLeft, kundensichtfenster.getPreferredSize().getWidth() + 15);
		if (mitarbeitersichtfenster != null)
			maxWidthLeft = Math.max(maxWidthLeft, mitarbeitersichtfenster.getPreferredSize().getWidth() + 15);
		if (shopManagement != null)
			maxWidthLeft = Math.max(maxWidthLeft, shopManagement.getPreferredSize().getWidth() + 15);
		if (warenkorbverwaltungsfenster != null)
			maxWidthRight = Math.max(maxWidthRight, warenkorbverwaltungsfenster.getPreferredSize().getWidth() + 15);
		if (artikelverwaltungsfenster != null)
			maxWidthRight = Math.max(maxWidthRight, artikelverwaltungsfenster.getPreferredSize().getWidth() + 15);
		if (kundenverwaltungsfenster != null)
			maxWidthRight = Math.max(maxWidthRight, kundenverwaltungsfenster.getPreferredSize().getWidth() + 15);
		if (mitarbeiterverwaltungsfenster != null)
			maxWidthRight = Math.max(maxWidthRight, mitarbeiterverwaltungsfenster.getPreferredSize().getWidth() + 15);
		leftArea.setPreferredSize(new Dimension((int) maxWidthLeft, (int) leftArea.getPreferredSize().getHeight()));
		// leftArea.setMinimumSize(leftArea.getPreferredSize());
		rightArea.setPreferredSize(new Dimension((int) maxWidthRight, (int) rightArea.getPreferredSize().getHeight()));
		// rightArea.setMinimumSize(rightArea.getPreferredSize());
		prefWidth = maxWidthLeft + maxWidthRight;
		this.setPreferredSize(new Dimension((int) prefWidth, (int) this.getPreferredSize().getHeight()));
	}

	

	class MenuButtonsActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {

			if (ae.getSource().equals(artikelButton)) {
				leftArea.remove(kundensichtfenster);
				leftArea.remove(mitarbeitersichtfenster);
				leftArea.remove(shopManagement);
				leftArea.add(artikelsichtfenster, BorderLayout.CENTER);
				leftArea.repaint();
				rightArea.removeAll();
				if (user instanceof Kunde) {
					rightArea.add(warenkorbverwaltungsfenster);
				} else {
					artikelverwaltungsfenster = new Artikelverwaltungsfenster();
					rightArea.add(artikelverwaltungsfenster);
				}
				rightArea.repaint();
				MainWindow.this.pack();
			} else if (ae.getSource().equals(kundenButton)) {
				if (user instanceof Mitarbeiter) {
					leftArea.remove(artikelsichtfenster);
					leftArea.remove(mitarbeitersichtfenster);
					leftArea.remove(shopManagement);
					leftArea.add(kundensichtfenster, BorderLayout.CENTER);
					leftArea.repaint();
					rightArea.removeAll();
					try {
						kundenverwaltungsfenster = new Personenverwaltungsfenster("Kundenverwaltung", "Kunde");
						rightArea.add(kundenverwaltungsfenster);
					} catch(Exception e) {
						JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
					}
					MainWindow.this.pack();
				} else {
					JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
				}
			} else if (ae.getSource().equals(mitarbeiterButton)) {
				if (user instanceof Mitarbeiter) {
					leftArea.remove(artikelsichtfenster);
					leftArea.remove(kundensichtfenster);
					leftArea.remove(shopManagement);
					leftArea.add(mitarbeitersichtfenster, BorderLayout.CENTER);
					leftArea.repaint();
					rightArea.removeAll();
					try {
						mitarbeiterverwaltungsfenster = new Personenverwaltungsfenster("Mitarbeiterverwaltung",
								"Mitarbeiter");
						rightArea.add(mitarbeiterverwaltungsfenster);
					} catch(Exception e) {
						JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
					}
					MainWindow.this.pack();
				} else {
					JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
				}
			} else if (ae.getSource().equals(shopButton)) {
				if (user instanceof Mitarbeiter) {
					leftArea.remove(artikelsichtfenster);
					leftArea.remove(kundensichtfenster);
					leftArea.remove(mitarbeitersichtfenster);
					leftArea.add(shopManagement, BorderLayout.CENTER);
					leftArea.revalidate();
					leftArea.repaint();
					rightArea.removeAll();
					MainWindow.this.pack();
				} else {
					JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
				}
			} else if (ae.getSource().equals(logoutButton)) {
				loginListener.logout();
			}
		}
	}

	class Warenkorbverwaltungsfenster extends JPanel {

		Warenkorb   wk;
		JPanel	    buttons			 = new JPanel();
		JTable	    warenkorbAuflistung		 = new JTable();
		JScrollPane warenkorbAuflistungContainer = new JScrollPane(warenkorbAuflistung);
		JButton	    aendernButton		 = new JButton("Anzahl ändern");
		JButton	    artikelEntfernenButton	 = new JButton("Entfernen");
		JButton	    leerenButton		 = new JButton("Leeren");
		JButton	    kaufenButton		 = new JButton("Kaufen");

		public Warenkorbverwaltungsfenster() {
			this.setLayout(new MigLayout());
			this.add(new JLabel("Warenkorbverwaltung"), "align center, wrap");
			this.add(warenkorbAuflistungContainer, "wrap");
			aendernButton.addActionListener(new WarenkorbActionListener());
			artikelEntfernenButton.addActionListener(new WarenkorbActionListener());
			leerenButton.addActionListener(new WarenkorbActionListener());
			kaufenButton.addActionListener(new WarenkorbActionListener());
			buttons.add(aendernButton);
			buttons.add(artikelEntfernenButton);
			buttons.add(leerenButton);
			buttons.add(kaufenButton);
			this.add(buttons, "align center, wrap");
			JTableHeader header = warenkorbAuflistung.getTableHeader();
			header.setUpdateTableInRealTime(true);
			header.setReorderingAllowed(false);
			warenkorbAuflistung.setAutoCreateRowSorter(true);
			warenkorbAuflistung.setModel(new WarenkorbTableModel());
			this.setVisible(true);
		}

		public void warenkorbAufrufen() throws AccessRestrictedException, RemoteException {

			Warenkorb wk = server.warenkorbAusgeben(user);
			Map<Artikel, Integer> inhalt = wk.getArtikel();
			warenkorbAuflistung.setModel(new WarenkorbTableModel(inhalt));
		}

		class WarenkorbActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Anzahl eines Artikels im Warenkorn ändern
				if (e.getSource().equals(aendernButton)) {
					try {
						int row = warenkorbAuflistung.getSelectedRow();
						if (row != -1) {
							Artikel art = server.artikelSuchen(((int) warenkorbAuflistung.getValueAt(row, 0)), user);
							int anz = Integer.parseInt(JOptionPane.showInputDialog("Bitte gewuenschte Anzahl angeben"));
							if (anz > 0) {
								server.artikelInWarenkorbAendern(art, anz, user);
							} else {
								throw new InvalidAmountException();
							}
							wk = server.warenkorbAusgeben(user);
							warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
						}
					} catch(ArticleStockNotSufficientException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(BasketNonexistantException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(AccessRestrictedException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(NumberFormatException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, "Keine gueltige Anzahl!");
					} catch(InvalidAmountException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(ArticleNonexistantException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(RemoteException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					}
					// Artikel aus Warenkorb entfernen
				} else if (e.getSource().equals(artikelEntfernenButton)) {
					try {
						int row = warenkorbAuflistung.getSelectedRow();
						if (row != -1) {
							Artikel art = server.artikelSuchen(((int) warenkorbAuflistung.getValueAt(row, 0)), user);
							server.artikelAusWarenkorbEntfernen(art, user);
							wk = server.warenkorbAusgeben(user);
							warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
						}
					} catch(AccessRestrictedException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(ArticleNonexistantException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(RemoteException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					}
					// Warenkorb leeren
				} else if (e.getSource().equals(leerenButton)) {
					try {
						server.warenkorbLeeren(user);
						wk = server.warenkorbAusgeben(user);
						warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
					} catch(AccessRestrictedException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(RemoteException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					}
					warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
					// Warenkorb kaufen
				} else if (e.getSource().equals(kaufenButton)) {
					try {
						// Formatierungsvorlage fuer Datum
						DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
						Rechnung re = server.warenkorbKaufen(user);
						artikelsichtfenster.auflistungInitialize();
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
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, rechnungsString);
						wk = server.warenkorbAusgeben(user);
						warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
					} catch(AccessRestrictedException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(InvalidAmountException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					} catch(RemoteException e1) {
						JOptionPane.showMessageDialog(warenkorbverwaltungsfenster, e1.getMessage());
					}
				}
			}
		}

		class WarenkorbTableModel extends AbstractTableModel {

			String[]		   columns	     = { "Artikelnummer", "Artikel", "Preis", "Menge", "Gesamt" };
			Vector<Vector<Object>> dataVector	     = new Vector<>(0);
			Vector<String>	   columnIdentifiers = new Vector<>(0);

			public WarenkorbTableModel() {
				columnIdentifiers = setColumns(columns);
			}

			public WarenkorbTableModel(Map<Artikel, Integer> inhalt) {
				columnIdentifiers = setColumns(columns);
				for (Map.Entry<Artikel, Integer> ent : inhalt.entrySet()) {
					Vector<Object> tmp = new Vector<>(0);
					tmp.addElement(ent.getKey().getArtikelnummer());
					tmp.addElement(ent.getKey().getBezeichnung());
					tmp.addElement(ent.getKey().getPreis());
					tmp.addElement(ent.getValue());
					tmp.addElement(ent.getKey().getPreis() * ent.getValue());
					dataVector.addElement(tmp);
				}
			}

			@Override
			public int getColumnCount() {

				return columnIdentifiers.size();
			}

			@Override
			public String getColumnName(int column) {

				return columnIdentifiers.elementAt(column);
			}

			@Override
			public int getRowCount() {

				return dataVector.size();
			}

			@Override
			public Object getValueAt(int arg0, int arg1) {

				return dataVector.elementAt(arg0).elementAt(arg1);
			}

			public Vector<String> setColumns(String[] columnNames) {

				Vector<String> columns = new Vector<>();
				for (String str : columnNames) {
					columns.addElement(str);
				}
				return columns;
			}
		}
	}

	@Override
	public void artikelInWarenkorb() {
		try {
			warenkorbverwaltungsfenster.warenkorbAufrufen();
		} catch (RemoteException | AccessRestrictedException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}

	@Override
	public void artikelBearbeiten() {
		artikelverwaltungsfenster.artikelAnzeigen(
				server.artikelSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
	}

	@Override
	public void kundeBearbeiten() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mitarbeiterBearbeiten() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void alleSichtfensterErneuern() {
		artikelsichtfenster = new ArtikelSichtfenster();
		kundensichtfenster = new KundenSichtfenster();
		mitarbeitersichtfenster = new MitarbeiterSichtfenster();
		artikelverwaltungsfenster = new Artikelverwaltungsfenster();
		kundenverwaltungsfenster = new Personenverwaltungsfenster("Kundenverwaltung", "Kunde");
		mitarbeiterverwaltungsfenster = new Personenverwaltungsfenster("Mitarbeiterverwaltung",
				"Mitarbeiter");
		
	}
	
	class MenuButtonsActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {

			if (ae.getSource().equals(artikelButton)) {
				leftArea.remove(kundensichtfenster);
				leftArea.remove(mitarbeitersichtfenster);
				leftArea.remove(shopManagement);
				leftArea.add(artikelsichtfenster, BorderLayout.CENTER);
				leftArea.repaint();
				rightArea.removeAll();
				if (user instanceof Kunde) {
					rightArea.add(warenkorbverwaltungsfenster);
				} else {
					artikelverwaltungsfenster = new Artikelverwaltungsfenster();
					rightArea.add(artikelverwaltungsfenster);
				}
				rightArea.repaint();
				MainWindow.this.pack();
			} else if (ae.getSource().equals(kundenButton)) {
				if (user instanceof Mitarbeiter) {
					leftArea.remove(artikelsichtfenster);
					leftArea.remove(mitarbeitersichtfenster);
					leftArea.remove(shopManagement);
					leftArea.add(kundensichtfenster, BorderLayout.CENTER);
					leftArea.repaint();
					rightArea.removeAll();
					try {
						kundenverwaltungsfenster = new Personenverwaltungsfenster("Kundenverwaltung", "Kunde");
						rightArea.add(kundenverwaltungsfenster);
					} catch(Exception e) {
						JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
					}
					MainWindow.this.pack();
				} else {
					JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
				}
			} else if (ae.getSource().equals(mitarbeiterButton)) {
				if (user instanceof Mitarbeiter) {
					leftArea.remove(artikelsichtfenster);
					leftArea.remove(kundensichtfenster);
					leftArea.remove(shopManagement);
					leftArea.add(mitarbeitersichtfenster, BorderLayout.CENTER);
					leftArea.repaint();
					rightArea.removeAll();
					try {
						mitarbeiterverwaltungsfenster = new Personenverwaltungsfenster("Mitarbeiterverwaltung",
								"Mitarbeiter");
						rightArea.add(mitarbeiterverwaltungsfenster);
					} catch(Exception e) {
						JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
					}
					MainWindow.this.pack();
				} else {
					JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
				}
			} else if (ae.getSource().equals(shopButton)) {
				if (user instanceof Mitarbeiter) {
					leftArea.remove(artikelsichtfenster);
					leftArea.remove(kundensichtfenster);
					leftArea.remove(mitarbeitersichtfenster);
					leftArea.add(shopManagement, BorderLayout.CENTER);
					leftArea.revalidate();
					leftArea.repaint();
					rightArea.removeAll();
					MainWindow.this.pack();
				} else {
					JOptionPane.showMessageDialog(MainWindow.this, "Kein Zugriff!");
				}
			} else if (ae.getSource().equals(logoutButton)) {
				loginListener.logout();
			}
		}
	}

	
}