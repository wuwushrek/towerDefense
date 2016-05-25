package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_TonnerrePlus extends TourView {
	public TourView_TonnerrePlus(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tonnerre_plus")[0],
				Main.infosTour.get("tonnerre_plus")[1],
				Main.infosTour.get("tonnerre_plus")[2]),
				Main.infosImage.get("tonnerre_plus"));
	}
}
