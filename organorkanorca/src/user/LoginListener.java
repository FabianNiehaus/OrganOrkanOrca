package user;

import data_objects.Person;

public interface LoginListener {
	public void userLoggedIn(Person user);
	public void loginCancelled();
	public void logout();
	public void loginUserUmgehen();
	public void loginMitarbeiterUmgehen();
}
