package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_Tonnerre extends TourView {
	public TourView_Tonnerre(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tonnerre")[0],
				Main.infosTour.get("tonnerre")[1],
				Main.infosTour.get("tonnerre")[2]),
				Main.infosImage.get("tonnerre"));
	}
}
