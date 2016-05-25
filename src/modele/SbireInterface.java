package modele;

import application.OnSbireMoveAndDestroy;
import javafx.beans.property.IntegerProperty;
import pathfinder.Path;

public interface SbireInterface {
	int getRowIndex();

	int getColumnIndex();

	IntegerProperty pointDeVieProperty();

	void setOnSbireDestroy(OnSbireMoveAndDestroy view);

	double getInitialPointDeVie();

	Path getPath();

	double getVitesse();

	boolean moveNext();;
}
