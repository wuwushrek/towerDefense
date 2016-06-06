package application;

import javafx.beans.property.DoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import modele.TourInterface;

public class TourView extends StackPane implements OnTourShot{
	protected TourInterface mTour;
	private ImageView mImage;
	protected static final long animDuration = 150;
	protected static int animRate =1;
	
	public TourView(TourInterface tour, Image tourIm){
		super();
		mTour = tour;
		mImage = new ImageView(tourIm);
		mImage.setPreserveRatio(true);
		mImage.setSmooth(true);
		mImage.setCache(true);
		
		mImage.fitWidthProperty().bind(Main.TILE_SIZE_X.multiply(1));
		mImage.fitHeightProperty().bind(Main.TILE_SIZE_Y.multiply(1));
		
		mTour.setOnTourShot(this);
		//mTour.launch();
		getChildren().addAll(mImage);
	}
	
	public void pause(){
		mTour.stopTour();
	}
	public void play(){
		mTour.launch();
	}
	
	public int getRow(){
		return mTour.getRowIndex();
	}
	
	public int getColumn(){
		return mTour.getColumnIndex();
	}
	
	public TourInterface getTour(){
		return mTour;
	}
	
	public static void setRate(int rate){
		animRate = rate;
	}
	
	public void onShotingEnd(){
		mTour.damage();
	}
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget) {
		
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
