package eshop.client.util;

import javax.swing.JPanel;

import eshop.client.util.Sichtfenster.SichtfensterCallbacks;
import eshop.common.data_objects.Person;
import eshop.common.net.ShopRemote;

public abstract class Verwaltungsfenster extends JPanel {

    public interface VerwaltungsfensterCallbacks {

	public void update(String sichtfenster);

	public void artikelBearbeiten();

	public void kundeBearbeiten();

	public void mitarbeiterBearbeiten();

	public void alleSichtfensterErneuern();
    }

    protected VerwaltungsfensterCallbacks listener = null;
    protected Person			  user;
    protected ShopRemote		  server;
    
    public Verwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks listener){
	this.server = server;
	this.user = user;
	this.listener = listener;
    }
}
