package application;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_MortierGold extends TourView{
	public TourView_MortierGold(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				ALauncher.infosTour.get("mortier_gold")[0],
				ALauncher.infosTour.get("mortier_gold")[1],
				ALauncher.infosTour.get("mortier_gold")[2]),
				ALauncher.infosImage.get("mortier_gold"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("mortier_gold")[3]);
	}
	@Override
	public void whenShoting(double xValueTarget, double yValueTarget){
		ImageView balle = new ImageView(ALauncher.infosImage.get("boule_feu"));
		balle.setRotate(90);
		balle.setPreserveRatio(true);
		balle.setSmooth(true);
		balle.setCache(true);
		
		balle.setFitWidth(40);
		balle.setFitHeight(20);

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
				TourView_Mortier.animate(xInScene , yInScene,xValueTarget , yValueTarget,150,balle,TourView_MortierGold.this);
			}
		});
	}
}
