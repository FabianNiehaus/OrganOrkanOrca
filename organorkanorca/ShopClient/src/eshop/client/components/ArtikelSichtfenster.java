package eshop.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import eshop.client.util.Sichtfenster;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.exceptions.AccessRestrictedException;
import eshop.common.exceptions.ArticleAlreadyInBasketException;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.ArticleStockNotSufficientException;
import eshop.common.exceptions.InvalidAmountException;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

public class ArtikelSichtfenster extends Sichtfenster {

    /**
     * 
     */
    private static final long serialVersionUID = -5439399681692245672L;
    
    JButton verlaufAnzeigenButton = new JButton("Verlauf");
    
    public ArtikelSichtfenster(ShopRemote server, Person user, SichtfensterCallbacks listener) {
	super(server, user, listener);
	//verlaufAnzeigenButton.setLayout(new MigLayout());
	if (user instanceof Kunde) {
	    aktion.setText("In Warenkorb");
	    aktion.addActionListener(new ArtikelInWarenkorbListener());
	    anzahl.setVisible(true);
	    
	} else if (user instanceof Mitarbeiter) {
	    aktion.setText("Bearbeiten");
	    aktion.addActionListener(new ArtikelBearbeitenListener());
	    anzahl.setVisible(false);
	    verlaufAnzeigenButton.addActionListener(new VerlaufAnzeigenListener());
	    actionField.add(verlaufAnzeigenButton);
	}
    }

    class VerlaufAnzeigenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    try {
		Artikel art = server.artikelSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user);
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Entry<Integer, Integer> ent : art.getBestandsverlauf().entrySet()) {
		    int dayOfYear = (int) ent.getKey();
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
	    } catch(ArticleNonexistantException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    } catch(AccessRestrictedException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    } catch(ArrayIndexOutOfBoundsException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, "Es muss ein Artikel ausgew‰hlt werden!");
	    } catch(RemoteException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    }
	}
    }

    class ArtikelBearbeitenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    try {
		listener.artikelBearbeiten(
			server.artikelSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user));
	    } catch(RemoteException | ArticleNonexistantException | AccessRestrictedException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    }
	}
    }

    class ArtikelInWarenkorbListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    try {
		Artikel art = server.artikelSuchen((int) auflistung.getValueAt(auflistung.getSelectedRow(), 0), user);
		server.artikelInWarenkorbLegen(art.getArtikelnummer(), Integer.parseInt(anzahl.getText()), user);
		// warenkorbverwaltungsfenster.warenkorbAufrufen();
		listener.artikelInWarenkorb();
		anzahl.setText("");
	    } catch(NumberFormatException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, "Keine gueltige Anzahl!");
	    } catch(ArticleNonexistantException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    } catch(ArticleStockNotSufficientException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    } catch(AccessRestrictedException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    } catch(InvalidAmountException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    } catch(ArrayIndexOutOfBoundsException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, "Kein Artikel ausgew√§hlt");
	    } catch(ArticleAlreadyInBasketException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    } catch(RemoteException e1) {
		JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e1.getMessage());
	    }
	}
    }

    @Override
    public void callTableUpdate() {

	try {
	    updateTable(server.alleArtikelAusgeben(user),
		    new String[] { "ArtNr.", "Bezeichnung", "Preis", "Einheit", "Bestand" });
	} catch(RemoteException | AccessRestrictedException e) {
	    JOptionPane.showMessageDialog(ArtikelSichtfenster.this, e.getMessage());
	}
    }
}
