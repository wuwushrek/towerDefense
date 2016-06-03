package modele;

import java.util.List;

import javafx.beans.property.IntegerProperty;

public interface PartieInterface {
	IntegerProperty levelProperty();
	IntegerProperty sbireTueeProperty();
	IntegerProperty argentProperty();
	IntegerProperty pointVieProperty();
	IntegerProperty scoreProperty();
	int getNumberOfLevel();
	int getCurrentSbireNumber();
	SbireSideInterface getSbireSideInterface();
	TourSideInterface getTourSideInterface();
	void timeToSetSbirePath();
	void initSbiresOnLevel();
	List<SbireInterface> getSbireList();
}
