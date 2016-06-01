package application;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CannonLPortee extends TourView {
	public TourView_CannonLPortee(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tour_canonportee")[0],
				Main.infosTour.get("tour_canonportee")[1],
				Main.infosTour.get("tour_canonportee")[2]),
				Main.infosImage.get("tour_canonportee"));
	}
	@Override
	public void whenShoting(DoubleProperty xValueTarget , DoubleProperty yValueTarget){
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
        Color color = Color.BLACK;
        
		Circle balls = new Circle();
		balls.setCenterX(xInScene);
		balls.setCenterY(yInScene);
		balls.setRadius(5);
		
		balls.setFill(color);
		balls.setStroke(color.brighter());
		balls.setStrokeWidth(2);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				Main.addNode(balls);
			}
		});
		TourView_CanonSimple.animate(xInScene,yInScene,xValueTarget , yValueTarget,balls);
	}
}
