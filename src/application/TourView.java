package application;

import javafx.beans.property.DoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modele.TourInterface;

public class TourView extends StackPane implements OnTourShot{
	protected TourInterface mTour;
	private ImageView mImage;

	private Circle boule= new Circle();
	
	public TourView(TourInterface tour, Image tourIm){
		super();
		//this.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
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
		/*Bounds boundsInScene = localToScene(getBoundsInLocal());
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
		}*/
		
	}
	
	@Override
	public void whenTargetDie(){
		
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
