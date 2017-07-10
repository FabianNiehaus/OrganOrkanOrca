package eshop.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

public class GUI extends UnicastRemoteObject implements ShopEventListener, WindowListener {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 9030916773132281332L;
	private ListenerForLogin	listenerForLogin	= new ListenerForLogin();
	LoginWindow						loginwindow;
	MainWindow						mainwindow;
	private ShopRemote			server;

	public GUI() throws RemoteException {
		try {
			String serviceName = "eShopServer";
			Registry registry = LocateRegistry.getRegistry();
			server = (ShopRemote) registry.lookup(serviceName);
			server.addShopEventListener(this);
			loginwindow = new LoginWindow("OrganOrkanOrca server", server, listenerForLogin, this);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public static void main(String[] args) {

		try {
			GUI gui = new GUI();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleArticleChanged(Artikel art) throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleArticleChanged(art);
	}

	@Override
	public void handleEventChanged(Ereignis er) throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleEventChanged(er);
	}

	@Override
	public void handleStaffChanged(Mitarbeiter mi) throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleStaffChanged(mi);
	}

	@Override
	public void handleUserChanged(Kunde ku) throws RemoteException, InterruptedException {

		Thread.sleep(400);
		if (mainwindow != null) mainwindow.handleUserChanged(ku);
	}

	@Override
	public void windowActivated(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

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
		closing.getContentPane().add(new JLabel("Möchten Sie sich neu anmelden oder das Programm schließen?"), "wrap, span 2");
		closing.getContentPane().add(logout, "left, w 150!");
		closing.getContentPane().add(quit, "right, w 150!");
		closing.pack();
		closing.setLocationRelativeTo(null);
		closing.setVisible(true);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeiconified(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	@Override
	public void windowOpened(WindowEvent e) {

		// TODO Auto-generated method stub
	}

	public class ListenerForLogin implements LoginListener {

		@Override
		public void loginCancelled() {

			loginwindow.dispose();
		}

		@Override
		public void logout() {

			loginwindow = new LoginWindow("OrganOrkanOrca server", server, this, GUI.this);
		}

		@Override
		public void userLoggedIn(Person user) {

			mainwindow = new MainWindow("OrganOrkanOrca server", user, server, this, GUI.this);
			loginwindow.dispose();
		}
	}
}
