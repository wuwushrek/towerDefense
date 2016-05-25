package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_TonnerrePlusGold extends TourView {
	public TourView_TonnerrePlusGold(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tonnerre_plus_gold")[0],
				Main.infosTour.get("tonnerre_plus_gold")[1],
				Main.infosTour.get("tonnerre_plus_gold")[2]),
				Main.infosImage.get("tonnerre_plus_gold"));
	}
}
