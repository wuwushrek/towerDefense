package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_FlechetteMortier extends TourView {
	public TourView_FlechetteMortier(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("flechette_mortier")[0],
				Main.infosTour.get("flechette_mortier")[1],
				Main.infosTour.get("flechette_mortier")[2]),
				Main.infosImage.get("flechette_mortier"));
	}
}
