package eshop.common.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import eshop.common.data_objects.Artikel;
import eshop.common.data_objects.Ereignis;
import eshop.common.data_objects.Kunde;
import eshop.common.data_objects.Mitarbeiter;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving shopEvent events. The class that is
 * interested in processing a shopEvent event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addShopEventListener<code> method. When the shopEvent event
 * occurs, that object's appropriate method is invoked.
 *
 * @see ShopEventEvent
 */
public interface ShopEventListener extends Remote {

	/**
	 * Handle all changed.
	 *
	 * @throws RemoteException
	 *            the remote exception
	 * @throws InterruptedException
	 *            the interrupted exception
	 */
	public void handleAllChanged() throws RemoteException, InterruptedException;

	/**
	 * Handle article changed.
	 *
	 * @param art
	 *           the artikel
	 * @throws RemoteException
	 *            the remote exception
	 * @throws InterruptedException
	 *            the interrupted exception
	 */
	public void handleArticleChanged(Artikel art) throws RemoteException, InterruptedException;

	/**
	 * Handle event changed.
	 *
	 * @param er
	 *           the er
	 * @throws RemoteException
	 *            the remote exception
	 * @throws InterruptedException
	 *            the interrupted exception
	 */
	public void handleEventChanged(Ereignis er) throws RemoteException, InterruptedException;

	/**
	 * Handle staff changed.
	 *
	 * @param mi
	 *           the mitarbeiter
	 * @throws RemoteException
	 *            the remote exception
	 * @throws InterruptedException
	 *            the interrupted exception
	 */
	public void handleStaffChanged(Mitarbeiter mi) throws RemoteException, InterruptedException;

	/**
	 * Handle user changed.
	 *
	 * @param ku
	 *           the kunde
	 * @throws RemoteException
	 *            the remote exception
	 * @throws InterruptedException
	 *            the interrupted exception
	 */
	public void handleUserChanged(Kunde ku) throws RemoteException, InterruptedException;
}
