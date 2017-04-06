package util;

import domain.eShopCore;

public class AccessControl {
	public static boolean checkCore(Object o){
		if (o instanceof eShopCore){
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean checkMitarbeiter(int c){
		if (c == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkKunde(int c){
		if (c == 0){
			return true;
		} else {
			return false;
		}
	}
}
