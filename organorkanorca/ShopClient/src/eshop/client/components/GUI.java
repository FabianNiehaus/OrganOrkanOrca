package eshop.client.components;

import java.io.IOException;
import java.net.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import eshop.common.data_objects.Person;
import eshop.common.exceptions.ArticleNonexistantException;
import eshop.common.exceptions.InvalidPersonDataException;
import eshop.common.exceptions.PersonNonexistantException;
import eshop.common.net.ShopEventListener;
import eshop.common.net.ShopRemote;
import eshop.server.domain.eShopCore;

public class GUI extends UnicastRemoteObject {

    private Person	     user;
    LoginWindow		     loginwindow;
    MainWindow		     mainwindow;
    private ListenerForLogin listenerForLogin = new ListenerForLogin();
    // Shop server
    private ShopRemote	     server;

    public GUI() throws RemoteException {
	try {
	    // Connect to server server
	    String serviceName = "eShopServer";
	    Registry registry = LocateRegistry.getRegistry();
	    server = (ShopRemote) registry.lookup(serviceName);
	    // Register for game events
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
	public void userLoggedIn(Person user) {

	    GUI.this.user = user;
	    mainwindow = new MainWindow("OrganOrkanOrca server", user, server, this);
	    loginwindow.dispose();
	}

	@Override
	public void loginCancelled() {

	    loginwindow.dispose();
	}

	@Override
	public void logout() {

	    GUI.this.user = null;
	    mainwindow.dispose();
	    loginwindow = new LoginWindow("OrganOrkanOrca server", server, this);
	}
    }
}
