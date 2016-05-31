package modele;

import application.OnTourShot;

public interface TourInterface {
	void setOnTourShot(OnTourShot view);
	int getRowIndex();
	int getColumnIndex();
	void destroy();
	void launch();
	long getIntervalCheck();
	void setIntervalCheck(long pause);
}
