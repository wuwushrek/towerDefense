package application;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.util.Duration;
import modele.SbireInterface;
import pathfinder.Path;
import pathfinder.Path.Step;

public class SbireView extends VBox implements OnSbireMoveAndDestroy{
	
	private final static long BASIC_STEP_TIME = 1500;
	
	private SbireInterface mSbire;
	private ProgressBar mProgress;
	private ImageView mImage;
	
	private PathTransition mMovement;
	private Timeline applyMove = new Timeline();

	private DoubleProperty currentX = new SimpleDoubleProperty();
	private DoubleProperty currentY = new SimpleDoubleProperty();
	private ChangeListener<Number> listener;
	
	public SbireView(Image im, SbireInterface sbire , int width , int height){
		super();
		this.mSbire=sbire;

		this.setLayoutX(mSbire.getColumnIndex()*Main.TILE_SIZE_X.get());
		this.setLayoutY(mSbire.getRowIndex()*Main.TILE_SIZE_Y.get());
		this.setAlignment(Pos.CENTER);
		
		mImage = new ImageView(im);
		mImage.setPreserveRatio(true);
		mImage.setSmooth(true);
		mImage.setCache(true);
		mImage.setFitWidth(width);
		mImage.setFitHeight(height);
		
		mProgress = new ProgressBar();
		mProgress.setPrefWidth(width);
		mProgress.setPrefHeight(height);
		mProgress.progressProperty().bind(mSbire.pointDeVieProperty().divide(mSbire.getInitialPointDeVie()));
		this.setSpacing(5);
		this.getChildren().add(mProgress);
		this.getChildren().add(mImage);
		
		listener = new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				Bounds boundsInScene = localToScene(getBoundsInLocal());
		        double xInScene = boundsInScene.getMinX()+boundsInScene.getWidth()/2;
		        double yInScene = boundsInScene.getMinY()+boundsInScene.getHeight()/2;
		        currentX.set(xInScene);
		        currentY.set(yInScene);
			}
			
		};
		this.translateXProperty().addListener(listener);
		this.translateYProperty().addListener(listener);
		
		mMovement = new PathTransition();
		mMovement.setDuration(Duration.millis((long) (BASIC_STEP_TIME*mSbire.getPath().getLength()*mSbire.getVitesse())));
		mMovement.setNode(this);
		mMovement.setAutoReverse(false);
		mMovement.setInterpolator(Interpolator.LINEAR);
		
		applyMove.setCycleCount(Animation.INDEFINITE);
		applyMove.setDelay(new Duration(BASIC_STEP_TIME*mSbire.getVitesse()/2.0));
		applyMove.getKeyFrames().addAll(
				new KeyFrame(new Duration(BASIC_STEP_TIME*mSbire.getVitesse()), new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						if(!mSbire.moveNext()){
							applyMove.stop();
						};
					}	
		}));
	}

	@Override
	public void onSbireDestroy() {
		// TODO Auto-generated method stub
		mMovement.stop();
		applyMove.stop();
		this.translateXProperty().removeListener(listener);
		this.translateYProperty().removeListener(listener);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Group parent =(Group) getParent();
				parent.getChildren().remove(SbireView.this);
			}
		});
	}
	
	//A appliquer apres avoir rajouter le groupe dans la scene
	private void initPathAnimation(Path mPath){
		javafx.scene.shape.Path path = new javafx.scene.shape.Path();
		MoveTo mvo = new MoveTo(this.getLayoutX(),this.getLayoutY());
		System.out.print(mvo);
		path.getElements().add(mvo);
		for(int i=1;i< mPath.getLength();i++){
			Step current = mPath.getStep(i);
			path.getElements().add(new LineTo(current.getY()*Main.TILE_SIZE_X.get(),current.getX()*Main.TILE_SIZE_Y.get()));
		}
		javafx.scene.shape.Path pathLocal = new javafx.scene.shape.Path();
	    path.getElements().forEach(elem->{
	        if(elem instanceof MoveTo){
	            Point2D m = this.sceneToLocal(((MoveTo)elem).getX(),((MoveTo)elem).getY());
	            Point2D mc = new Point2D(m.getX()+this.getWidth()/2d,m.getY()+this.getHeight()/2d);
	            pathLocal.getElements().add(new MoveTo(mc.getX(),mc.getY()));
	        } else if(elem instanceof LineTo){
	            Point2D l = this.sceneToLocal(((LineTo)elem).getX(),((LineTo)elem).getY());
	            Point2D lc = new Point2D(l.getX()+this.getWidth()/2d,l.getY()+this.getHeight()/2d);
	            pathLocal.getElements().add(new LineTo(lc.getX(),lc.getY()));
	        }
	    });
		mMovement.setPath(pathLocal);
	}
	
	public void initPathAnimation(){
		initPathAnimation(mSbire.getPath());
	}
	public void play(){
		//mMoves.play();
		mMovement.play();
		applyMove.play();
	}

	@Override
	public DoubleProperty xPosProperty() {
		// TODO Auto-generated method stub
		return currentX;
	}

	@Override
	public DoubleProperty yPosProperty() {
		// TODO Auto-generated method stub
		return currentY;
	}
	
}
