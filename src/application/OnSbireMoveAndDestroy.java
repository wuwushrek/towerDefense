package application;

import javafx.beans.property.DoubleProperty;

public interface OnSbireMoveAndDestroy {
	void onSbireDestroy();
	DoubleProperty xPosProperty();
	DoubleProperty yPosProperty();
}
