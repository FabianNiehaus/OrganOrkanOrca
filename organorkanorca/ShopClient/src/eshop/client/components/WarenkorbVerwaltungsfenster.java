package eshop.client.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import eshop.client.util.Verwaltungsfenster;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Person;
import eshop.common.data_objects.Rechnung;
import eshop.common.data_objects.Warenkorb;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.ArticleStockNotSufficientException;
import eshop.common.exceptions.BasketNonexistantException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

/**
 * @author Manic
 *
 */
public class WarenkorbVerwaltungsfenster extends Verwaltungsfenster {

	/**
	 * 
	 */
	private static final long	serialVersionUID					= -6170535941906530451L;
	JButton							aendernButton						= new JButton("Anzahl ändern");
	JButton							artikelEntfernenButton			= new JButton("Entfernen");
	JPanel							buttons								= new JPanel();
	String[]							columnHeaders						= {"Artikelnummer", "Artikel", "Preis", "Menge", "Gesamt"};
	JButton							kaufenButton						= new JButton("Kaufen");
	JButton							leerenButton						= new JButton("Leeren");
	JTable							warenkorbAuflistung				= new JTable();
	JScrollPane						warenkorbAuflistungContainer	= new JScrollPane(warenkorbAuflistung);
	Warenkorb						wk;

	public WarenkorbVerwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks listener) {
		super(server, user, listener);
		this.setLayout(new MigLayout("", "114[]0"));
		this.add(new JLabel("Warenkorbverwaltung"), "wrap");
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
		warenkorbAufrufen();
	}

	public boolean artikelImWarenkorbPruefen(Artikel art)
			throws RemoteException, AccessRestrictedException, PersonNonexistantException, ArticleNonexistantException {

		try {
			server.artikelSuchen(art.getArtikelnummer(), user);
			for (Map.Entry<Artikel, Integer> ent : wk.getArtikel().entrySet()) {
				if (ent.getKey().getArtikelnummer() == art.getArtikelnummer()) {
					return true;
				}
			}
			return false;
		} catch (ArticleNonexistantException e) {
			throw new ArticleNonexistantException(art.getBezeichnung(), true);
		}
	}

	@Override
	public void reset() {

		// TODO Auto-generated method stub
	}

	public void warenkorbAufrufen() {

		try {
			wk = server.warenkorbAusgeben(user.getId(), user);
			Map<Artikel, Integer> inhalt = wk.getArtikel();
			warenkorbAuflistung.setModel(new WarenkorbTableModel(inhalt));
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (AccessRestrictedException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (PersonNonexistantException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
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
							server.artikelInWarenkorbAendern(art.getArtikelnummer(), anz, user);
						} else {
							throw new InvalidAmountException();
						}
						wk = server.warenkorbAusgeben(user.getId(), user);
						warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
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
			} else if (e.getSource().equals(artikelEntfernenButton)) {
				try {
					int row = warenkorbAuflistung.getSelectedRow();
					if (row != -1) {
						server.artikelAusWarenkorbEntfernen((int) warenkorbAuflistung.getValueAt(row, 0), user);
						wk = server.warenkorbAusgeben(user.getId(), user);
						warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
					}
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
					wk = server.warenkorbAusgeben(user.getId(), user);
					warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
				} catch (AccessRestrictedException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				} catch (PersonNonexistantException e1) {
					JOptionPane.showMessageDialog(WarenkorbVerwaltungsfenster.this, e1.getMessage());
				}
				warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
				// Warenkorb kaufen
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
					wk = server.warenkorbAusgeben(user.getId(), user);
					warenkorbAuflistung.setModel(new WarenkorbTableModel(wk.getArtikel()));
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

	class WarenkorbTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 5529552191258522247L;
		Vector<String>					columnIdentifiers	= new Vector<>(0);
		String[]							columns				= {"Artikelnummer", "Artikel", "Preis", "Menge", "Gesamt"};
		Vector<Vector<Object>>		dataVector			= new Vector<>(0);

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
