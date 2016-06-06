package application;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modele.GameFactory;
import modele.TourInterface;
import modele.TourSideInterface;

public class TourView_Acher extends TourView {
	
	public TourView_Acher(TourInterface tour, Image tourIm) {
		super(tour,tourIm);
	}

	public TourView_Acher(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				ALauncher.infosTour.get("tour_archer")[0],
				ALauncher.infosTour.get("tour_archer")[1],
				ALauncher.infosTour.get("tour_archer")[2]),
				ALauncher.infosImage.get("tour_archer"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("tour_archer")[3]);
	}
	
	@Override
	public void whenShoting(double xValueTarget, double yValueTarget){
		ImageView balle = new ImageView(ALauncher.infosImage.get("flechette"));
		//balle.setRotate(90);
		balle.setPreserveRatio(true);
		balle.setSmooth(true);
		balle.setCache(true);
		
		balle.setFitWidth(25);
		balle.setFitHeight(10);

		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY();
		balle.setX(xInScene);
		balle.setY(yInScene);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				ALauncher.addNode(balle);
				TourView_CanonSimple.animate(xInScene , yInScene,xValueTarget , yValueTarget,balle,TourView_Acher.this);
			}
		});
	}
}
