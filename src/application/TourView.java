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

public class TourView extends StackPane implements OnTourShot{
	private Tour mTour;
	private ImageView mImage;

	private Circle boule= new Circle();
	
	public TourView(String pathImage, Tour tour){
		super();
		mImage = new ImageView(new Image(pathImage));
		mImage.setPreserveRatio(true);
		mImage.setSmooth(true);
		mImage.setCache(true);
		
		mImage.fitWidthProperty().bind(Main.TILE_SIZE_X);
		mImage.fitHeightProperty().bind(Main.TILE_SIZE_Y);
		//this.setFitWidth(30);
		//this.setFitHeight(20);

		boule = new Circle();
		boule.setRadius(Main.TILE_SIZE_X.get()/18);
		boule.setFill(Color.BLACK);
		boule.setVisible(false);
		
		getChildren().addAll(mImage,boule);
		this.mTour=tour;
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
		// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				//getChildren().remove(boule);
				//boule = new Circle();
				//boule.setRadius(Main.TILE_SIZE_X.get()/18);
				//boule.setFill(Color.BLACK);
				boule.setVisible(false);
				//getChildren().add(boule);
			}
		});
	}
}
