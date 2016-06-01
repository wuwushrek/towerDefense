package application;

import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_TripleTonnere extends TourView {
	public TourView_TripleTonnere(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				Main.infosTour.get("triple_tonnerre")[0],
				Main.infosTour.get("triple_tonnerre")[1],
				Main.infosTour.get("triple_tonnerre")[2]),
				Main.infosImage.get("triple_tonnerre"));
		mTour.setIntervalCheck(Main.infosTour.get("triple_tonnerre")[3]);
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget) {

		final double displacement = 300;
		final double curDetail =80;
		
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		Vec startVec = new Vec(xInScene,yInScene);
		Vec startVec2 = new Vec(xInScene-5,yInScene);
		Vec startVec3 = new Vec(xInScene+5,yInScene);
		Vec destVec = new Vec(xValueTarget.get(),yValueTarget.get());
		List<Group> jagged = TourView_Tonnerre.jaggedLines(startVec,destVec,Color.WHITE,displacement,curDetail);
		List<Group> jagged2 = TourView_Tonnerre.jaggedLines(startVec2,destVec,Color.WHITE,displacement,curDetail);
		List<Group> jagged3 = TourView_Tonnerre.jaggedLines(startVec3,destVec,Color.WHITE,displacement,curDetail);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				Main.addAll(jagged);
				Main.addAll(jagged2);
				Main.addAll(jagged3);
			}
		});
		for(Group group : jagged){
			TourView_Tonnerre.withFade(group,Math.random() +0.2);
		}
		for(Group group : jagged2){
			TourView_Tonnerre.withFade(group,Math.random() +0.2);
		}
		for(Group group : jagged3){
			TourView_Tonnerre.withFade(group,Math.random() +0.2);
		}
		
	}
}
