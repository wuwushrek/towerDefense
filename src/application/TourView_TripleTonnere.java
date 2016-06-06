package application;

import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_TripleTonnere extends TourView {
	public TourView_TripleTonnere(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				ALauncher.infosTour.get("triple_tonnerre")[0],
				ALauncher.infosTour.get("triple_tonnerre")[1],
				ALauncher.infosTour.get("triple_tonnerre")[2]),
				ALauncher.infosImage.get("triple_tonnerre"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("triple_tonnerre")[3]);
	}
	
	@Override
	public void whenShoting(double xValueTarget, double yValueTarget) {

		final double displacement = 100;
		final double curDetail =15;
		
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		Vec startVec = new Vec(xInScene,yInScene);
		Vec startVec2 = new Vec(xInScene-5,yInScene);
		Vec startVec3 = new Vec(xInScene+5,yInScene);
		Vec destVec = new Vec(xValueTarget,yValueTarget);
		List<Group> jagged = TourView_Tonnerre.jaggedLines(startVec,destVec,Color.WHITE,displacement,curDetail);
		List<Group> jagged2 = TourView_Tonnerre.jaggedLines(startVec2,destVec,Color.WHITE,displacement,curDetail);
		List<Group> jagged3 = TourView_Tonnerre.jaggedLines(startVec3,destVec,Color.WHITE,displacement,curDetail);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				ALauncher.addAll(jagged);
				ALauncher.addAll(jagged2);
				ALauncher.addAll(jagged3);
				boolean first = true;
				for(Group group : jagged){
					if(first){
						TourView_Tonnerre.withFade(group,Math.random() +0.2,TourView_TripleTonnere.this,true);
						first=false;
					}else{
						TourView_Tonnerre.withFade(group,Math.random() +0.2,TourView_TripleTonnere.this,false);
					}
				}
				for(Group group : jagged2){
					if(first){
						TourView_Tonnerre.withFade(group,Math.random() +0.2,TourView_TripleTonnere.this,true);
						first=false;
					}else{
						TourView_Tonnerre.withFade(group,Math.random() +0.2,TourView_TripleTonnere.this,false);
					}
				}
				for(Group group : jagged3){
					if(first){
						TourView_Tonnerre.withFade(group,Math.random() +0.2,TourView_TripleTonnere.this,true);
						first=false;
					}else{
						TourView_Tonnerre.withFade(group,Math.random() +0.2,TourView_TripleTonnere.this,false);
					}
				}
			}
		});
		
		
	}
}
