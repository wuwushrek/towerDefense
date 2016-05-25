package modele;

import java.util.List;

import javafx.beans.property.IntegerProperty;

public interface PartieInterface {
	IntegerProperty levelProperty();
	IntegerProperty sbireTueeProperty();
	IntegerProperty argentProperty();
	IntegerProperty pointVieProperty();
	SbireSideInterface getSbireSideInterface();
	TourSideInterface getTourSideInterface();
	void timeToSetSbirePath();
	List<SbireInterface> getSbireList();
}
