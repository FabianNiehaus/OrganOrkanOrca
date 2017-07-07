package eshop.common.data_objects;

import java.io.Serializable;

/**
 * @author Fabian Niehaus Enumerator-Objekt fuer die Ereignisspeicherung
 */
public enum Typ implements Serializable {
	EINLAGERUNG, AUSLAGERUNG, KAUF, NEU;
}
