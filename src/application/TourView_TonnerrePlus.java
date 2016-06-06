package application;

import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import modele.GameFactory;
import modele.TourSideInterface;

public class TourView_TonnerrePlus extends TourView {
	public TourView_TonnerrePlus(int rowIndex , int columnIndex , TourSideInterface partie){
		super (GameFactory.createTour(partie, rowIndex, columnIndex,
				ALauncher.infosTour.get("tonnerre_plus")[0],
				ALauncher.infosTour.get("tonnerre_plus")[1],
				ALauncher.infosTour.get("tonnerre_plus")[2]),
				ALauncher.infosImage.get("tonnerre_plus"));
		mTour.setIntervalCheck(ALauncher.infosTour.get("tonnerre_plus")[3]);
	}
	
	@Override
	public void whenShoting(double xValueTarget, double yValueTarget) {

		final double displacement = 150;
		final double curDetail =10;
		
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		Vec startVec = new Vec(xInScene,yInScene);
		Vec destVec = new Vec(xValueTarget,yValueTarget);
		List<Group> jagged = TourView_Tonnerre.jaggedLines(startVec,destVec,Color.BLUE,displacement,curDetail);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				ALauncher.addAll(jagged);
				boolean first = true;
				for(Group group : jagged){
					if(first){
						TourView_Tonnerre.withFade(group,Math.random() +0.2,TourView_TonnerrePlus.this,true);
						first=false;
					}else{
						TourView_Tonnerre.withFade(group,Math.random() +0.2,TourView_TonnerrePlus.this,false);
					}
				}
			}
		});
		
	}
}
