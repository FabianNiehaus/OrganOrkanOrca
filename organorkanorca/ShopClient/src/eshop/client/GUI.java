package eshop.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import eshop.client.util.LoginListener;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.net.ShopEventListener;
import eshop.common.net.ShopRemote;
import net.miginfocom.swing.MigLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class GUI.
 */
public class GUI extends UnicastRemoteObject implements ShopEventListener, WindowListener {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 9030916773132281332L;
	
	/** The listener for login. */
	private ListenerForLogin	listenerForLogin	= new ListenerForLogin();
	
	/** The loginwindow. */
	LoginWindow						loginwindow;
	
	/** The mainwindow. */
	MainWindow						mainwindow;
	
	/** The server. */
	private ShopRemote			server;
	
	/** The host. */
	private String host;

	/**
	 * Instantiates a new gui.
	 *
	 * @throws RemoteException
	 *            the remote exception
	 */
	public GUI() throws RemoteException {
		try {
			
			host = JOptionPane.showInputDialog("Bitte Server angeben",InetAddress.getLocalHost().getHostAddress());
			
			String serviceName = "eShopServer";
			Registry registry = LocateRegistry.getRegistry(host);
			server = (ShopRemote) registry.lookup(serviceName);
			server.addShopEventListener(this);
			loginwindow = new LoginWindow("OrganOrkanOrca eShop", server, listenerForLogin, this);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(0);
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(0);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *           the arguments
	 */
	public static void main(String[] args) {

		try {
			GUI gui = new GUI();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see eshop.common.net.ShopEventListener#handleAllChanged()
	 */
	@Override
	public void handleAllChanged() throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleAllChanged();
	}

	/* (non-Javadoc)
	 * @see eshop.common.net.ShopEventListener#handleArticleChanged(eshop.common.data_objects.Artikel)
	 */
	@Override
	public void handleArticleChanged(Artikel art) throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleArticleChanged(art);
	}

	/* (non-Javadoc)
	 * @see eshop.common.net.ShopEventListener#handleEventChanged(eshop.common.data_objects.Ereignis)
	 */
	@Override
	public void handleEventChanged(Ereignis er) throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleEventChanged(er);
	}

	/* (non-Javadoc)
	 * @see eshop.common.net.ShopEventListener#handleStaffChanged(eshop.common.data_objects.Mitarbeiter)
	 */
	@Override
	public void handleStaffChanged(Mitarbeiter mi) throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleStaffChanged(mi);
	}

	/* (non-Javadoc)
	 * @see eshop.common.net.ShopEventListener#handleUserChanged(eshop.common.data_objects.Kunde)
	 */
	@Override
	public void handleUserChanged(Kunde ku) throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleUserChanged(ku);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {

		JFrame closing = new JFrame("Closing");
		JButton logout = new JButton("Neu anmelden");
		JButton quit = new JButton("Schließen");
		logout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				listenerForLogin.logout();
				closing.dispose();
			}
		});
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					server.removeShopEventListener(GUI.this);
					System.exit(0);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		closing.getContentPane().setLayout(new MigLayout("", "30[]30", "30[]15[]30"));
		closing.getContentPane().add(new JLabel("Möchten Sie sich neu anmelden oder das Programm schließen?"),
				"wrap, span 2");
		closing.getContentPane().add(logout, "left, w 150!");
		closing.getContentPane().add(quit, "right, w 150!");
		closing.pack();
		closing.setLocationRelativeTo(null);
		closing.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	/**
	 * The Class ListenerForLogin.
	 */
	public class ListenerForLogin implements LoginListener {

		/* (non-Javadoc)
		 * @see eshop.client.util.LoginListener#loginCancelled()
		 */
		@Override
		public void loginCancelled() {

			loginwindow.dispose();
		}

		/* (non-Javadoc)
		 * @see eshop.client.util.LoginListener#logout()
		 */
		@Override
		public void logout() {

			loginwindow = new LoginWindow("OrganOrkanOrca server", server, this, GUI.this);
		}

		/* (non-Javadoc)
		 * @see eshop.client.util.LoginListener#userLoggedIn(eshop.common.data_objects.Person)
		 */
		@Override
		public void userLoggedIn(Person user) {

			mainwindow = new MainWindow("OrganOrkanOrca server", user, server, this, GUI.this);
			loginwindow.dispose();
		}
	}
}
