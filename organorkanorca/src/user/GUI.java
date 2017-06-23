package user;

import java.io.IOException;
import javax.swing.JOptionPane;
import data_objects.Person;
import domain.eShopCore;
import domain.exceptions.ArticleNonexistantException;
import domain.exceptions.InvalidPersonDataException;
import domain.exceptions.PersonNonexistantException;

public class GUI implements LoginListener {
	
	private eShopCore eShop;
	private Person user;
	LoginWindow loginwindow;
	MainWindow mainwindow;
	
	public GUI() {
		
		try {
			
			eShop = new eShopCore();
			
			loginwindow = new LoginWindow("OrganOrkanOrca eShop", eShop, this);
			
		} catch (IOException | ArticleNonexistantException | PersonNonexistantException | InvalidPersonDataException e1) {
			
			JOptionPane.showMessageDialog(null, "Fehler beim Lesen der Bestandsdaten!");
			
		}
	}
	
	public static void main(String[] args){
		GUI gui = new GUI();
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

	@Override
	public void loginUserUmgehen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginMitarbeiterUmgehen() {
		// TODO Auto-generated method stub
		
	}
}
