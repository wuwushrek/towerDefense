package application;

import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_TonnerrePlus extends TourView {
	public TourView_TonnerrePlus(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("tonnerre_plus")[0],
				Main.infosTour.get("tonnerre_plus")[1],
				Main.infosTour.get("tonnerre_plus")[2]),
				Main.infosImage.get("tonnerre_plus"));
		mTour.setIntervalCheck(Main.infosTour.get("tonnerre_plus")[3]);
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget) {

		final double displacement = 150;
		final double curDetail =10;
		
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		Vec startVec = new Vec(xInScene,yInScene);
		Vec destVec = new Vec(xValueTarget.get(),yValueTarget.get());
		List<Group> jagged = TourView_Tonnerre.jaggedLines(startVec,destVec,Color.BLUE,displacement,curDetail);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				Main.addAll(jagged);
			}
		});
		for(Group group : jagged){
			TourView_Tonnerre.withFade(group,Math.random() +0.2);
		}
		
	}
}
