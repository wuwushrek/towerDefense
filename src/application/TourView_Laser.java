package application;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import modele.GameFactory;
import modele.TourInterface;
import modele.TourSideInterface;

public class TourView_Laser extends TourView {

	public TourView_Laser(TourInterface tour, Image tourIm) {
		super(tour, tourIm);
	}
	
	public TourView_Laser(int rowIndex , int columnIndex, TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("laser")[0],
				Main.infosTour.get("laser")[1],
				Main.infosTour.get("laser")[2]),
				Main.infosImage.get("laser"));
		mTour.setIntervalCheck(Main.infosTour.get("laser")[3]);
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget) {
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		Group laserW = laser(xInScene , yInScene ,xValueTarget,yValueTarget,Color.DARKBLUE);
		TourView_Tonnerre.withFade(laserW,Math.random() +0.2,this);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				Main.addNode(laserW);
			}
		});
		
	}
	
	public Group laser (double xInScene , double yInScene,DoubleProperty xValueTarget,DoubleProperty yValueTarget,Color color){
		Group g = new Group();
		Line refline = new Line();
		refline.setStartX(xInScene);
		refline.setStartY(yInScene);
		refline.endXProperty().bind(xValueTarget);
		refline.endYProperty().bind(yValueTarget);
		refline.setStroke(color);
		refline.setStrokeWidth(4);
		refline.setStrokeLineCap(StrokeLineCap.ROUND);
		Line line = new Line();
		line.setStartX(xInScene);
		line.setStartY(yInScene);
		line.endXProperty().bind(xValueTarget);
		line.endYProperty().bind(yValueTarget);
		line.setStroke(color);
		line.setStrokeWidth(10);
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		GaussianBlur blur = new GaussianBlur();
		line.setEffect(blur);
		g.getChildren().addAll(line,refline);
		return g;
	}

}
