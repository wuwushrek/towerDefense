package application;

import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
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
				Main.infosTour.get("canon_simple")[0],
				Main.infosTour.get("canon_simple")[1],
				Main.infosTour.get("canon_simple")[2]),
				Main.infosImage.get("canon_simple"));
		mTour.setIntervalCheck(Main.infosTour.get("canon_simple")[3]);
		bloom.setThreshold(0.6);
		//setRotate(90);
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget , DoubleProperty yValueTarget){
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
		
		//balls.setEffect(bloom);
		Point2D start = new Point2D(xInScene,yInScene);
		Point2D end = new Point2D(xValueTarget.get(), yValueTarget.get());
		Point2D substract = end.subtract(start);
		Point2D axeX = new Point2D(1,0);
		double angleRotate = substract.getY()<0?-axeX.angle(substract):axeX.angle(substract);
		System.out.println("Angle rotate en degree: "+angleRotate);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				setRotate(angleRotate+90);
				Main.addNode(balls);
			}
		});
		animate(xInScene,yInScene,xValueTarget , yValueTarget,balls);
	}
	
	public static void animate(double xFrom , double yFrom , DoubleProperty xTo , DoubleProperty yTo,final Node node){
		Path path = new Path();
		path.getElements().add(new MoveTo(xFrom,yFrom));
		LineTo dest = new LineTo();
		dest.xProperty().bind(xTo);
		dest.yProperty().bind(yTo);
		path.getElements().add(dest);
		PathTransition translation = new PathTransition(Duration.millis(animDuration),path);
		translation.setNode(node);
		translation.setAutoReverse(true);
		translation.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		translation.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				Main.removeNode(node);
			}
			
		});
		translation.play();
	}
}
