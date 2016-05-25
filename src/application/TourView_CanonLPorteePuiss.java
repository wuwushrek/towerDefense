package application;

import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CanonLPorteePuiss extends TourView {
	public TourView_CanonLPorteePuiss(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tour_canonporteepuiss")[0],
				Main.infosTour.get("tour_canonporteepuiss")[1],
				Main.infosTour.get("tour_canonporteepuiss")[2]),
				Main.infosImage.get("tour_canonporteepuiss"));
	}
}
