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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import modele.TourInterface;

public class TourView extends StackPane implements OnTourShot{
	private TourInterface mTour;
	private ImageView mImage;
	private double displacement = 200;
	private double curDetail =25;

	private Circle boule= new Circle();
	
	public TourView(TourInterface tour, Image tourIm){
		super();
		this.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
		mTour = tour;
		mImage = new ImageView(tourIm);
		mImage.setPreserveRatio(true);
		mImage.setSmooth(true);
		mImage.setCache(true);
		
		mImage.fitWidthProperty().bind(Main.TILE_SIZE_X.multiply(1));
		mImage.fitHeightProperty().bind(Main.TILE_SIZE_Y.multiply(1));

		boule = new Circle();
		boule.setRadius(Main.TILE_SIZE_X.get()/18);
		boule.setFill(Color.BLACK);
		boule.setVisible(false);
		
		getChildren().addAll(mImage,boule);
		mTour.setOnTourShot(this);
		tour.launch();
		
	}
	
	public int getRow(){
		return mTour.getRowIndex();
	}
	
	public int getColumn(){
		return mTour.getColumnIndex();
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget) {
		//Point2D pt = localToScene(this.getLayoutX(),this.getLayoutY());
		Bounds boundsInScene = localToScene(getBoundsInLocal());
        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		Vec startVec = new Vec(xInScene,yInScene);
		Vec destVec = new Vec(xValueTarget.get(),yValueTarget.get());
		//System.out.println("ORIGINAL: "+pt.getX()+", "+pt.getY());
		List<Group> jagged = jaggedLines(startVec,destVec,Color.WHITESMOKE);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				Main.addAll(jagged);
			}
		});
		for(Group group : jagged){
			withFade(group ,150,Math.random() +0.2);
		}
		
	}
	
	@Override
	public void whenTargetDie(){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				boule.setVisible(false);
			}
		});
	}
	
	public Group line(Vec source , Vec dest , Color color){
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
	
	public List<Vec> midPointReplacement(Vec source , Vec dest ,double displace,double curDetail){
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
	
	public List<Group> jaggedLines(Vec source , Vec dest , Color color){
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
	
	public void withFade(Group group , long duration,double brightness){
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
	class Vec {
		double x;
		double y;
		
		public Vec(double x , double y){
			this.x=x;
			this.y=y;
		}
		
		public Vec center(Vec other){
			return new Vec((other.x+x)/2,(other.y+y)/2);
		}
		
		public Vec displaceMe(double f){
			return new Vec(x+(Math.random()-0.5)*f,y+(Math.random()-0.5));
		}
	}
}
