package application;

import javafx.scene.image.Image;
import modele.GameFactory;
import modele.TourInterface;
import modele.TourSideInterface;

public class TourView_Laser extends TourView {

	public TourView_Laser(TourInterface tour, Image tourIm) {
		super(tour, tourIm);
	}
	
	public TourView_Laser(int rowIndex , int columnIndex, TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("laser")[0],
				Main.infosTour.get("laser")[1],
				Main.infosTour.get("laser")[2]),
				Main.infosImage.get("laser"));
	}

}
