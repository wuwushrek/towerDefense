package application;

import javafx.scene.image.Image;
import modele.GameFactory;
import modele.TourInterface;
import modele.TourSideInterface;

public class TourView_Acher extends TourView {
	
	public TourView_Acher(TourInterface tour, Image tourIm) {
		super(tour,tourIm);
	}

	public TourView_Acher(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tour_archer")[0],
				Main.infosTour.get("tour_archer")[1],
				Main.infosTour.get("tour_archer")[2]),
				Main.infosImage.get("tour_archer"));
	}
}
