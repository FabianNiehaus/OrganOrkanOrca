package eshop.client.util;

import java.awt.Dimension;

import javax.swing.JPanel;

import eshop.common.data_objects.Person;
import eshop.common.net.ShopRemote;

public abstract class Verwaltungsfenster extends JPanel {

	/**
	  * 
	  */
	private static final long serialVersionUID = -1766082173934041583L;

	public interface VerwaltungsfensterCallbacks {

		public void artikelInWarenkorb();
	}

	protected VerwaltungsfensterCallbacks	listener			= null;
	protected Person								user;
	protected ShopRemote							server;
	protected boolean								isBeingChanged	= false;
	protected boolean								isBeingCreated	= false;

	public Verwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks listener) {
		this.server = server;
		this.user = user;
		this.listener = listener;
		setPreferredSize(new Dimension(900, 400));
	}

	public abstract void reset();
}
