package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CanonSup extends TourView{
	public TourView_CanonSup(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("canon_sup")[0],
				Main.infosTour.get("canon_sup")[1],
				Main.infosTour.get("canon_sup")[2]),
				Main.infosImage.get("canon_sup"));
	}
}
