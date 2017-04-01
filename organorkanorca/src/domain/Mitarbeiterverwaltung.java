package domain;

import java.util.Vector;
import data_objects.Mitarbeiter;

public class Mitarbeiterverwaltung {

	/**
	 * @param mitarbeiter Verwaltete Mitarbeiter
	 */
	public Mitarbeiterverwaltung(Vector<Mitarbeiter> mitarbeiter) {
		super();
		this.mitarbeiter = mitarbeiter;
	}

	private Vector<Mitarbeiter> mitarbeiter;

	public Vector<Mitarbeiter> getMitarbeiter() {
		return mitarbeiter;
	}

	public void setMitarbeiter(Vector<Mitarbeiter> mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}
}
