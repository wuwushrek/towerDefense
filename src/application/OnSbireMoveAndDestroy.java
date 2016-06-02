package application;

import javafx.beans.property.DoubleProperty;

public interface OnSbireMoveAndDestroy {
	void onSbireDestroy();
	void onSbireTouched() ;
	DoubleProperty xPosProperty();
	DoubleProperty yPosProperty();
}
