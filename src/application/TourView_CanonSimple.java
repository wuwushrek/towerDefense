package application;

import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_CanonSimple extends TourView {
	Bloom bloom = new Bloom();
	public TourView_CanonSimple(int rowIndex , int columnIndex, TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				ALauncher.infosTour.get("canon_simple")[0],
				ALauncher.infosTour.get("canon_simple")[1],
				ALauncher.infosTour.get("canon_simple")[2]),
				ALauncher.infosImage.get("canon_simple"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("canon_simple")[3]);
	}
	
	@Override
	public void whenShoting(double xValueTarget , double yValueTarget){
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
        Color color = Color.WHITE;
        
		Circle balls = new Circle();
		balls.setCenterX(xInScene);
		balls.setCenterY(yInScene);
		balls.setRadius(5);
		
		balls.setFill(color);
		
		balls.setStroke(color.darker());
		balls.setStrokeWidth(1);
		balls.setId("balle");
		
		Point2D start = new Point2D(xInScene,yInScene);
		Point2D end = new Point2D(xValueTarget, yValueTarget);
		Point2D substract = end.subtract(start);
		Point2D axeX = new Point2D(1,0);
		double angleRotate = substract.getY()<0?-axeX.angle(substract):axeX.angle(substract);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				setRotate(angleRotate+90);
				ALauncher.addNode(balls);
				animate(xInScene,yInScene,xValueTarget , yValueTarget,balls,TourView_CanonSimple.this);
			}
		});
	}
	
	public static void animate(double xFrom , double yFrom , double xTo , double yTo,
			final Node node, TourView tour){
		Path path = new Path();
		path.getElements().add(new MoveTo(xFrom,yFrom));
		LineTo dest = new LineTo();
		dest.setX(xTo);
		dest.setY(yTo);
		path.getElements().add(dest);
		PathTransition translation = new PathTransition(Duration.millis(250),path);
		translation.setRate(animRate);
		translation.setNode(node);
		translation.setAutoReverse(true);
		translation.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		translation.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				tour.onShotingEnd();
				ALauncher.removeNode(node);
			}
			
		});
		translation.play();
	}
}
