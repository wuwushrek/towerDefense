package application;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_Tonnerre extends TourView {
	public TourView_Tonnerre(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tonnerre")[0],
				Main.infosTour.get("tonnerre")[1],
				Main.infosTour.get("tonnerre")[2]),
				Main.infosImage.get("tonnerre"));
	}
	
	public static List<Vec> midPointReplacement(Vec source , Vec dest ,double displace,double curDetail){
		if(displace<curDetail){
			ArrayList<Vec> liste =new ArrayList<Vec>();
			liste.add(source);
			liste.add(dest);
			return liste;
		}else{
			Vec displacedCenter= source.center(dest).displaceMe(displace);
			List<Vec> retValue = midPointReplacement(source,displacedCenter,displace/2,curDetail);
			retValue.addAll(midPointReplacement(displacedCenter,dest,displace/2,curDetail));
			return retValue;
		}
	}
	
	public static List<Group> jaggedLines(Vec source , Vec dest , Color color, double displacement,double curDetail){
		List<Vec> positions = midPointReplacement(source,dest,displacement , curDetail);
		List<Group> ret = new ArrayList<Group>();
		Vec last = null;
		for (Vec vec : positions){
			System.out.println("X: "+vec.x+", Y: "+vec.y);
			if(last == null){
				last = vec;
			}else{
				ret.add(line(last,vec,color));
				last=vec;
			}
		}
		return ret;
	}
	
	public static void withFade(Group group , long duration,double brightness){
		FadeTransition ft =new FadeTransition(Duration.millis(duration),group);
		ft.setFromValue(brightness/8);
		ft.setToValue(brightness);
		ft.setCycleCount(4);
		ft.setAutoReverse(true);
		ft.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						Main.removeNode(group);
					}
				});
			}
			
		});
		ft.play();
	}
	
	public static Group line(Vec source , Vec dest , Color color){
		Group g = new Group();
		Line refline = new Line(source.x,source.y,dest.x,dest.y);
		refline.setStroke(color);
		refline.setStrokeWidth(4);
		Line line = new Line(source.x,source.y,dest.x,dest.y);
		line.setStroke(color);
		line.setStrokeWidth(4);
		GaussianBlur blur = new GaussianBlur();
		line.setEffect(blur);
		g.getChildren().addAll(refline,line);
		return g;
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget) {

		final double displacement = 100;
		final double curDetail =15;
		
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		Vec startVec = new Vec(xInScene,yInScene);
		Vec destVec = new Vec(xValueTarget.get(),yValueTarget.get());
		List<Group> jagged = jaggedLines(startVec,destVec,Color.WHITESMOKE,displacement,curDetail);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				Main.addAll(jagged);
			}
		});
		for(Group group : jagged){
			withFade(group ,mTour.getIntervalCheck()/2,Math.random() +0.2);
		}
		
	}
}
