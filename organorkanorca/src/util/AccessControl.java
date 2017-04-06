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
}
