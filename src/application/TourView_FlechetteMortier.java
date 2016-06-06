package application;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_FlechetteMortier extends TourView {
	public TourView_FlechetteMortier(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("flechette_mortier")[0],
				Main.infosTour.get("flechette_mortier")[1],
				Main.infosTour.get("flechette_mortier")[2]),
				Main.infosImage.get("flechette_mortier"));
		mTour.setIntervalCheck(Main.infosTour.get("flechette_mortier")[3]);
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget){
		ImageView balle = new ImageView(Main.infosImage.get("flechette"));
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
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				Main.addNode(balle);
			}
		});
		TourView_Mortier.animate(xInScene , yInScene,xValueTarget , yValueTarget,150,balle,this);
	}
}
