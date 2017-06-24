package user;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;
import data_objects.Person;
import domain.eShopCore;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.InvalidPersonDataException;
import domain.exceptions.PersonNonexistantException;
import net.ShopEventListener;
import net.ShopRemote;

public class GUI extends UnicastRemoteObject implements ShopEventListener {

	private eShopCore eShop;
	private Person user;
	LoginWindow loginwindow;
	MainWindow mainwindow;

	// Shop server
	private ShopRemote server;

	public GUI() throws RemoteException {

		try {

			// Connect to eShop server
			String serviceName = "eShopCore";
			Registry registry = LocateRegistry.getRegistry();
			server = (ShopRemote) registry.lookup(serviceName);

			// Register for game events
			server.addShopEventListener(this);

			// initialize client
			initialize();

			loginwindow = new LoginWindow("OrganOrkanOrca eShop", eShop, this);

		} catch (IOException e1) {

			JOptionPane.showMessageDialog(null, "Fehler beim Lesen der Bestandsdaten!");

		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void initialize() {

		try {
			
			eShop = new eShopCore();

			loginwindow = new LoginWindow("OrganOrkanOrca eShop", eShop, this);

		} catch (IOException | ArticleNonexistantException | PersonNonexistantException
				| InvalidPersonDataException e1) {

			JOptionPane.showMessageDialog(null, "Fehler beim Lesen der Bestandsdaten!");

		}
	}

	public static void main(String[] args) {
		try {
			GUI gui = new GUI();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void userLoggedIn(Person user) {
		this.user = user;
		mainwindow = new MainWindow("OrganOrkanOrca eShop", user, eShop, this);
		loginwindow.dispose();
	}

	@Override
	public void loginCancelled() {
		loginwindow.dispose();
	}

	@Override
	public void logout() {
		mainwindow.dispose();
		loginwindow = new LoginWindow("OrganOrkanOrca eShop", eShop, this);
	}

}
