package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_Mortier extends TourView {
	public TourView_Mortier(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("mortier")[0],
				Main.infosTour.get("mortier")[1],
				Main.infosTour.get("mortier")[2]),
				Main.infosImage.get("mortier"));
	}
}
