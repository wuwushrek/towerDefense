package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_MortierGold extends TourView{
	public TourView_MortierGold(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("mortier_gold")[0],
				Main.infosTour.get("mortier_gold")[1],
				Main.infosTour.get("mortier_gold")[2]),
				Main.infosImage.get("mortier_gold"));
	}
}
