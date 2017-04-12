package domain;

import java.util.Vector;
import data_objects.*;

public class Warenkorbverwaltung {
	
	private Vector<Warenkorb> warenkoerbe = new Vector<>();

	public Warenkorb getWarenkorb(Warenkorb wk){
		for(Warenkorb ret : warenkoerbe){
			if(ret.equals(wk)){
				return ret;
			}
		}
		return null;
	}
	
	public Warenkorb erstelleWarenkorb(){
		Warenkorb wk = new Warenkorb();
		warenkoerbe.addElement(wk);
		return wk;
	}
	
	public void loescheWarenkorb(Warenkorb wk){
		for(Warenkorb del : warenkoerbe){
			if(del.equals(wk)){
				warenkoerbe.remove(del);
			}
		}
	}
	
	public void aendereWarenkorb(Warenkorb aend, int position, int anz){
		Warenkorb wk = getWarenkorb(aend);
		wk.aendereAnzahl(position, anz);
	}
	
	public void legeInWarenkorb(Warenkorb wk, Artikel art, int anz){
		wk.speichereArtikel(art, anz);
	}
	
	public Vector<Warenkorb> getWarenkoerbe() {
		return warenkoerbe;
	}
	
	public void leereWarenkorb(Warenkorb wk){
		wk.leereWarkenkorb();
	}

}
