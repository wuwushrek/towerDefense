package modele;

import javafx.beans.property.DoubleProperty;

interface TourTarget {
	boolean decrementPointDeVie(int pointVie);
	int getRowIndex();
	int getColumnIndex();
	DoubleProperty xProperty();
	DoubleProperty yProperty();
}
