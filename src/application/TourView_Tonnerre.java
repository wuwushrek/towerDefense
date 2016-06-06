package application;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
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
				ALauncher.infosTour.get("tonnerre")[0],
				ALauncher.infosTour.get("tonnerre")[1],
				ALauncher.infosTour.get("tonnerre")[2]),
				ALauncher.infosImage.get("tonnerre"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("tonnerre")[3]);
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
			if(last == null){
				last = vec;
			}else{
				ret.add(line(last,vec,color));
				last=vec;
			}
		}
		return ret;
	}
	
	public static void withFade(Group group ,double brightness,TourView tour , boolean damage){
		FadeTransition ft =new FadeTransition(Duration.millis(200),group);
		ft.setFromValue(brightness/8);
		ft.setToValue(brightness);
		ft.setAutoReverse(true);
		ft.setRate(animRate);
		ft.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						if(damage){
							tour.onShotingEnd();
						}
						ALauncher.removeNode(group);
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
	public void whenShoting(double xValueTarget, double yValueTarget) {

		final double displacement = 120;
		final double curDetail =15;
		
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		Vec startVec = new Vec(xInScene,yInScene);
		Vec destVec = new Vec(xValueTarget,yValueTarget);
		List<Group> jagged = jaggedLines(startVec,destVec,Color.WHITESMOKE,displacement,curDetail);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				ALauncher.addAll(jagged);
				boolean first = true;
				for(Group group : jagged){
					if(first){
						withFade(group ,Math.random() +0.2,TourView_Tonnerre.this,true);
						first=false;
					}else{
						withFade(group ,Math.random() +0.2,TourView_Tonnerre.this,false);
					}
				}
			}
		});
		
		
	}
}
