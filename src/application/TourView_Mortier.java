package application;

import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.application.Platform;
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
				ALauncher.infosTour.get("mortier")[0],
				ALauncher.infosTour.get("mortier")[1],
				ALauncher.infosTour.get("mortier")[2]),
				ALauncher.infosImage.get("mortier"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("mortier")[3]);
	}
	
	@Override
	public void whenShoting(double xValueTarget, double yValueTarget){
		ImageView balle = new ImageView(ALauncher.infosImage.get("boule_bleu"));
		balle.setRotate(90);
		balle.setPreserveRatio(true);
		balle.setSmooth(true);
		balle.setCache(true);
		
		balle.setFitWidth(30);
		balle.setFitHeight(15);

		balle.setId("balle");

		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY();
		balle.setX(xInScene);
		balle.setY(yInScene);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				ALauncher.addNode(balle);
				animate(xInScene , yInScene,xValueTarget , yValueTarget,100,balle,TourView_Mortier.this);
			}
		});
		
	}
	
	public static void animate(double xFrom , double yFrom,double xTo, double yTo,
			double hauteur ,final Node node, TourView tour){
		Path path = new Path();
		path.getElements().add(new MoveTo(xFrom,yFrom));
		QuadCurveTo quadCurve = new QuadCurveTo();
		
		quadCurve.setControlX((xFrom+xTo)/2);
		quadCurve.setControlY(0);//Math.min(yFrom, yTo)+Math.abs((yFrom-yTo)/2));//Math.min(yFrom, yFrom)+Math.abs((yFrom-yTo)/2) yFrom);
		//quadCurve.xProperty().bind(xTo);
		//quadCurve.yProperty().bind(yTo);
		quadCurve.setX(xTo);
		quadCurve.setY(yTo);
		path.getElements().add(quadCurve);
		PathTransition pathTransition = new PathTransition(Duration.millis(400),path);
		pathTransition.setRate(animRate);
		pathTransition.setNode(node);
		pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setAutoReverse(true);
		pathTransition.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				tour.onShotingEnd();
				ALauncher.removeNode(node);
			}
			
		});
		pathTransition.play();
	}
}
