package eshop.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import eshop.client.util.LoginListener;
import eshop.common.data_objects.Person;
import eshop.common.net.ShopRemote;

public class GUI extends UnicastRemoteObject {

    LoginWindow		     loginwindow;
    MainWindow		     mainwindow;
    private ListenerForLogin listenerForLogin = new ListenerForLogin();
    // Shop server
    private ShopRemote	     server;

    public GUI() throws RemoteException {
	try {
	    String serviceName = "eShopServer";
	    Registry registry = LocateRegistry.getRegistry();
	    server = (ShopRemote) registry.lookup(serviceName);
	    server.addShopEventListener(mainwindow);
	    loginwindow = new LoginWindow("OrganOrkanOrca server", server, listenerForLogin);
	} catch(RemoteException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch(NotBoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	}
    }

    public static void main(String[] args) {

	try {
	    GUI gui = new GUI();
	} catch(RemoteException e) {
	    e.printStackTrace();
	}
    }

    public class ListenerForLogin implements LoginListener {

	@Override
	public void loginCancelled() {

	    loginwindow.dispose();
	}

	@Override
	public void logout() {

	    mainwindow.dispose();
	    loginwindow = new LoginWindow("OrganOrkanOrca server", server, this);
	}

	@Override
	public void userLoggedIn(Person user) {

	    mainwindow = new MainWindow("OrganOrkanOrca server", user, server, this);
	    loginwindow.dispose();
	}
    }
}
