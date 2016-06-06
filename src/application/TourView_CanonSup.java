package application;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CanonSup extends TourView{
	public TourView_CanonSup(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				ALauncher.infosTour.get("canon_sup")[0],
				ALauncher.infosTour.get("canon_sup")[1],
				ALauncher.infosTour.get("canon_sup")[2]),
				ALauncher.infosImage.get("canon_sup"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("canon_sup")[3]);
	}
	
	@Override
	public void whenShoting(double xValueTarget , double yValueTarget){
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
        Color color = Color.LIGHTBLUE;
        
		Circle balls = new Circle();
		balls.setCenterX(xInScene);
		balls.setCenterY(yInScene);
		balls.setRadius(5);
		
		balls.setFill(color);
		balls.setStroke(color.darker());
		balls.setStrokeWidth(2);
		balls.setId("balle");
		
		//balls.setEffect(bloom);
		Point2D start = new Point2D(xInScene,yInScene);
		Point2D end = new Point2D(xValueTarget, yValueTarget);
		Point2D substract = end.subtract(start);
		Point2D axeX = new Point2D(1,0);
		double angleRotate = substract.getY()<0?-axeX.angle(substract):axeX.angle(substract);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				setRotate(90+angleRotate);
				ALauncher.addNode(balls);
				TourView_CanonSimple.animate(xInScene,yInScene,xValueTarget , yValueTarget,balls,TourView_CanonSup.this);
			}
		});
	}
}
