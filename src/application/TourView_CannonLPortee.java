package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CannonLPortee extends TourView {
	public TourView_CannonLPortee(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tour_canonportee")[0],
				Main.infosTour.get("tour_canonportee")[1],
				Main.infosTour.get("tour_canonportee")[2]),
				Main.infosImage.get("tour_canonportee"));
	}
}
