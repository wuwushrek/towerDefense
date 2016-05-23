package application;

import javafx.beans.property.DoubleProperty;

public interface TourTarget {
	boolean decrementPointDeVie(int pointVie);
	int getRowIndex();
	int getColumnIndex();
	DoubleProperty xProperty();
	DoubleProperty yProperty();
}
