package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_TripleTonnere extends TourView {
	public TourView_TripleTonnere(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("triple_tonnerre")[0],
				Main.infosTour.get("triple_tonnerre")[1],
				Main.infosTour.get("triple_tonnerre")[2]),
				Main.infosImage.get("triple_tonnerre"));
	}
}
