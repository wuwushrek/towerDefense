package application;

import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.util.Duration;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_Mortier extends TourView {
	public TourView_Mortier(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("mortier")[0],
				Main.infosTour.get("mortier")[1],
				Main.infosTour.get("mortier")[2]),
				Main.infosImage.get("mortier"));
		mTour.setIntervalCheck(Main.infosTour.get("mortier")[3]);
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget){
		ImageView balle = new ImageView(Main.infosImage.get("boule_bleu"));
		balle.setRotate(90);
		balle.setPreserveRatio(true);
		balle.setSmooth(true);
		balle.setCache(true);
		
		balle.setFitWidth(30);
		balle.setFitHeight(15);

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
		
		animate(xInScene , yInScene,xValueTarget , yValueTarget,mTour.getIntervalCheck(),balle);
	}
	
	public static void animate(double xFrom , double yFrom,DoubleProperty xTo, DoubleProperty yTo,long duration,final Node node){
		Path path = new Path();
		path.getElements().add(new MoveTo(xFrom,yFrom));
		QuadCurveTo quadCurve = new QuadCurveTo();
		
		quadCurve.setControlX((xFrom+xTo.get())/2);
		quadCurve.setControlY(yFrom-100);
		quadCurve.xProperty().bind(xTo);
		quadCurve.yProperty().bind(yTo);
		path.getElements().add(quadCurve);
		PathTransition pathTransition = new PathTransition(Duration.millis(duration),path);
		pathTransition.setNode(node);
		pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setAutoReverse(true);
		pathTransition.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				Main.removeNode(node);
			}
			
		});
		pathTransition.play();
	}
}
