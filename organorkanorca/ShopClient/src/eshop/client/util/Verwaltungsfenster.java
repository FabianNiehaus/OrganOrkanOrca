package eshop.client.util;

import java.awt.Dimension;

import javax.swing.JPanel;

import eshop.common.data_objects.Person;
import eshop.common.net.ShopRemote;

// TODO: Auto-generated Javadoc
/**
 * The Class Verwaltungsfenster.
 */
public abstract class Verwaltungsfenster extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long					serialVersionUID	= -1766082173934041583L;
	
	/** The is being changed. */
	protected boolean								isBeingChanged		= false;
	
	/** The is being created. */
	protected boolean								isBeingCreated		= false;
	
	/** The server. */
	protected ShopRemote							server;
	
	/** The user. */
	protected Person								user;
	
	/** The verwaltungsfenster callbacks. */
	protected VerwaltungsfensterCallbacks	verwaltungsfensterCallbacks;

	/**
	 * Instantiates a new verwaltungsfenster.
	 *
	 * @param server
	 *           the server
	 * @param user
	 *           the user
	 * @param verwaltungsfensterCallbacks
	 *           the verwaltungsfenster callbacks
	 */
	public Verwaltungsfenster(ShopRemote server, Person user, VerwaltungsfensterCallbacks verwaltungsfensterCallbacks) {
		this.server = server;
		this.user = user;
		this.verwaltungsfensterCallbacks = verwaltungsfensterCallbacks;
		setPreferredSize(new Dimension(900, 400));
	}

	/**
	 * Reset.
	 */
	public abstract void reset();

	/**
	 * The Interface VerwaltungsfensterCallbacks.
	 */
	public interface VerwaltungsfensterCallbacks {

		/**
		 * Checks if is t warenkorb leer.
		 *
		 * @return true, if is t warenkorb leer
		 */
		public boolean istWarenkorbLeer();

		/**
		 * Warenkorb aktualisieren.
		 */
		public void warenkorbAktualisieren();
	}
}
