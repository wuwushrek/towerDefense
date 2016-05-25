package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CanonSimple extends TourView {
	
	public TourView_CanonSimple(int rowIndex , int columnIndex, TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("canon_simple")[0],
				Main.infosTour.get("canon_simple")[1],
				Main.infosTour.get("canon_simple")[2]),
				Main.infosImage.get("canon_simple"));
	}
}
