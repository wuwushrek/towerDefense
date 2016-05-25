package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CanonSupRenforce extends TourView {
	public TourView_CanonSupRenforce(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("canon_renforce")[0],
				Main.infosTour.get("canon_renforce")[1],
				Main.infosTour.get("canon_renforce")[2]),
				Main.infosImage.get("canon_renforce"));
	}
}
