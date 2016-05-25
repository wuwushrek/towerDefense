package application;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import modele.TourInterface;

public class TourView extends StackPane implements OnTourShot{
	private TourInterface mTour;
	private ImageView mImage;

	private Circle boule= new Circle();
	
	public TourView(TourInterface tour, Image tourIm){
		super();
		
		mTour = tour;
		mImage = new ImageView(tourIm);
		mImage.setPreserveRatio(true);
		mImage.setSmooth(true);
		mImage.setCache(true);
		
		mImage.fitWidthProperty().bind(Main.TILE_SIZE_X.multiply(1.5));
		mImage.fitHeightProperty().bind(Main.TILE_SIZE_Y.multiply(1.5));

		boule = new Circle();
		boule.setRadius(Main.TILE_SIZE_X.get()/18);
		boule.setFill(Color.BLACK);
		boule.setVisible(false);
		
		getChildren().addAll(mImage,boule);
		mTour.setOnTourShot(this);
		
	}
	
	public int getRow(){
		return mTour.getRowIndex();
	}
	
	public int getColumn(){
		return mTour.getColumnIndex();
	}
	
	@Override
	public void whenShoting(DoubleProperty xValueTarget, DoubleProperty yValueTarget) {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				boule.setVisible(true);
			}
		});
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(boule);
		transition.setDuration(Duration.millis(150));
		Point2D pt = sceneToLocal(xValueTarget.get(),yValueTarget.get());
		transition.setFromX(boule.getCenterX());
		transition.setFromY(boule.getCenterY());
		transition.setToX(pt.getX());
		transition.setToY(pt.getY());
		transition.playFromStart();
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
}
