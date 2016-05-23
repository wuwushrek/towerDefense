package application;

import javafx.beans.property.DoubleProperty;

public interface OnTourShot {
	void whenShoting(DoubleProperty xValueTarget , DoubleProperty yValueTarget);
	void whenTargetDie();
}
