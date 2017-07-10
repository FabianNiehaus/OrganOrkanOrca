package eshop.client.util;

import java.awt.Dimension;

import javax.swing.JPanel;

import eshop.common.data_objects.Person;
import eshop.common.net.ShopRemote;

public abstract class Verwaltungsfenster extends JPanel {

	/**
	  * 
	  */
	private static final long					serialVersionUID	= -1766082173934041583L;
	protected boolean								isBeingChanged		= false;
	protected boolean								isBeingCreated		= false;
	protected ShopRemote							server;
	protected Person								user;
	protected VerwaltungsfensterCallbacks	verwaltungsfensterCallbacks;

	public Verwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks verwaltungsfensterCallbacks) {
		this.server = server;
		this.user = user;
		this.verwaltungsfensterCallbacks = verwaltungsfensterCallbacks;
		setPreferredSize(new Dimension(900, 400));
	}

	public abstract void reset();

	public interface VerwaltungsfensterCallbacks {

		public void warenkorbAktualisieren();
		
		public boolean istWarenkorbLeer();
	}
}
