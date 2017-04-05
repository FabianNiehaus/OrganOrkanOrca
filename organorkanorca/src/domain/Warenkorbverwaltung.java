package domain;

import java.util.Vector;
import data_objects.*;

public class Warenkorbverwaltung {
	
	private Vector<Warenkorb> warenkoerbe;

	public Warenkorb getWarenkorb(int id){
		for(Warenkorb wk : warenkoerbe){
			if(wk.getId() == id){
				return wk;
			}
		}
		return null;
	}
	
	public int erstelleWarenkorb(){
		int hoechsteID = 0;
		for(Warenkorb wk : warenkoerbe){
			if(wk.getId() > hoechsteID){
				hoechsteID = wk.getId();
			}
		}
		warenkoerbe.addElement(new Warenkorb(hoechsteID+1));
		
		return hoechsteID+1;
	}
	
	public void loescheWarenkorb(int id){
		for(Warenkorb wk : warenkoerbe){
			if(wk.getId() == id){
				warenkoerbe.remove(wk);
			}
		}
	}
	
	public void aendereWarenkorb(int id, Artikel art, int anz){
		Warenkorb wk = getWarenkorb(id);
		wk.aendereAnzahl(art, anz);
	}
	
	public Vector<Warenkorb> getWarenkoerbe() {
		return warenkoerbe;
	}

}
