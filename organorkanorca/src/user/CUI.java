package user;

import domain.Artikelverwaltung;
import domain.Kundenverwaltung;
import domain.Mitarbeiterverwaltung;
import domain.Warenkorbverwaltung;
import domain.eShopCore;

public class CUI {
	
	public static void main(String[] args){
		
		eShopCore eShop = new eShopCore(
				new Artikelverwaltung(),
				new Kundenverwaltung(),
				new Mitarbeiterverwaltung(),
				new Warenkorbverwaltung()
		);
		
	}
}
