package application;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CanonSupRenforce extends TourView {
	public TourView_CanonSupRenforce(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("canon_renforce")[0],
				Main.infosTour.get("canon_renforce")[1],
				Main.infosTour.get("canon_renforce")[2]),
				Main.infosImage.get("canon_renforce"));
		mTour.setIntervalCheck(Main.infosTour.get("canon_renforce")[3]);
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
		balls.setStroke(color.darker());
		balls.setStrokeWidth(2);
		
		//balls.setEffect(bloom);
		Point2D start = new Point2D(xInScene,yInScene);
		Point2D end = new Point2D(xValueTarget.get(), yValueTarget.get());
		Point2D substract = end.subtract(start);
		Point2D axeX = new Point2D(1,0);
		double angleRotate = substract.getY()<0?-axeX.angle(substract):axeX.angle(substract);
		System.out.println("Angle en degrees: "+angleRotate);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				
				setRotate(90+angleRotate);
				Main.addNode(balls);
			}
		});
		TourView_CanonSimple.animate(xInScene,yInScene,xValueTarget , yValueTarget,balls,this);
	}
}
