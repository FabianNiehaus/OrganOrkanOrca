package eshop.common.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;

public interface ShopEventListener extends Remote {

	public void handleArticleChanged(Artikel art) throws RemoteException;

	public void handleBasketChanged(Artikel art) throws RemoteException;

	void handleEventChanged(Ereignis er) throws RemoteException;

	void handleStaffChanged(Mitarbeiter mi) throws RemoteException;

	void handleUserChanged(Kunde ku) throws RemoteException;
}
