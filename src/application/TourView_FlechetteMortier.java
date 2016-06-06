package application;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_FlechetteMortier extends TourView {
	public TourView_FlechetteMortier(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				ALauncher.infosTour.get("flechette_mortier")[0],
				ALauncher.infosTour.get("flechette_mortier")[1],
				ALauncher.infosTour.get("flechette_mortier")[2]),
				ALauncher.infosImage.get("flechette_mortier"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("flechette_mortier")[3]);
	}
	
	@Override
	public void whenShoting(double xValueTarget, double yValueTarget){
		ImageView balle = new ImageView(ALauncher.infosImage.get("flechette"));
		balle.setRotate(90);
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
		balle.setId("balle");
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				ALauncher.addNode(balle);
				TourView_Mortier.animate(xInScene , yInScene,xValueTarget , yValueTarget,150,balle,TourView_FlechetteMortier.this);
			}
		});
	}
}
