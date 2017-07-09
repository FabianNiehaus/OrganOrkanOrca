package eshop.client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import eshop.client.util.LoginListener;
import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;
import eshop.common.data_objects.Person;
import eshop.common.net.ShopEventListener;
import eshop.common.net.ShopRemote;

public class GUI extends UnicastRemoteObject implements ShopEventListener, WindowListener {

    /**
     * 
     */
    private static final long serialVersionUID = -593668905786628709L;

    public interface ShopEventCallbacks {

	public void handleArticleChanged(Artikel art);

	void handleEventChanged(Ereignis er);

	void handleStaffChanged(Mitarbeiter mi);

	void handleUserChanged(Kunde ku);
    }

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
	    server.addShopEventListener(this);
	    loginwindow = new LoginWindow("OrganOrkanOrca server", server, listenerForLogin, this);
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
	    loginwindow = new LoginWindow("OrganOrkanOrca server", server, this, GUI.this);
	}

	@Override
	public void userLoggedIn(Person user) {

	    mainwindow = new MainWindow("OrganOrkanOrca server", user, server, this, GUI.this);
	    loginwindow.dispose();
	}
    }

    @Override
    public void handleArticleChanged(Artikel art) throws RemoteException {
	
	if(mainwindow != null) mainwindow.handleArticleChanged(art);
    }

    @Override
    public void handleEventChanged(Ereignis er) throws RemoteException {

	if(mainwindow != null) mainwindow.handleEventChanged(er);
    }

    @Override
    public void handleStaffChanged(Mitarbeiter mi) throws RemoteException {

	if(mainwindow != null) mainwindow.handleStaffChanged(mi);
    }

    @Override
    public void handleUserChanged(Kunde ku) throws RemoteException {

	if(mainwindow != null) mainwindow.handleUserChanged(ku);
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

	try {
	    server.removeShopEventListener(this);
	    System.exit(0);
	} catch(RemoteException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	
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
}
