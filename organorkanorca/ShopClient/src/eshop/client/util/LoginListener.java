package eshop.client.util;

import eshop.common.data_objects.Person;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving login events. The class that is
 * interested in processing a login event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addLoginListener<code> method. When the login event occurs,
 * that object's appropriate method is invoked.
 *
 * @see LoginEvent
 */
public interface LoginListener {

	/**
	 * Login cancelled.
	 */
	public void loginCancelled();

	/**
	 * Logout.
	 */
	public void logout();

	/**
	 * User logged in.
	 *
	 * @param user
	 *           the user
	 */
	public void userLoggedIn(Person user);
}
