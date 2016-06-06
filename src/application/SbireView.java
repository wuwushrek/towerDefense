package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
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
import javafx.scene.effect.ColorAdjust;
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
	
	private final static long BASIC_STEP_TIME = 2000;
	private int speedRate =1;
	
	private SbireInterface mSbire;
	private ProgressBar mProgress;
	private ImageView mImage;
	
	private PathTransition mMovement;
	private Timeline applyMove = new Timeline();
	private ParallelTransition pt;

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
		mProgress.setPrefWidth(width*1.1);
		ColorAdjust adjust = new ColorAdjust() ;
		adjust.setHue(-0.4);
		mProgress.setEffect(adjust);
		
		
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
		//mMovement.setNode(this);
		mMovement.setAutoReverse(false);
		mMovement.setInterpolator(Interpolator.LINEAR);
		
		applyMove.setCycleCount(mSbire.getPath().getLength());
		applyMove.setDelay(new Duration(BASIC_STEP_TIME*mSbire.getVitesse()/2.0));
		applyMove.getKeyFrames().addAll(
				new KeyFrame(Duration.millis(BASIC_STEP_TIME*mSbire.getVitesse()), new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						mSbire.moveNext();
					}	
		}));
	}

	@Override
	public void onSbireDestroy() {
		// TODO Auto-generated method stub
		//mMovement.stop();
		//applyMove.stop();
		pt.stop();
		this.translateXProperty().removeListener(listener);
		this.translateYProperty().removeListener(listener);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Group parent =(Group) getParent();
				if(parent!= null && SbireView.this != null){
					parent.getChildren().remove(SbireView.this);
				}
			}
		});
	}
	
	@Override
	public void onSbireTouched() {
		// TODO Auto-generated method stub
		FadeTransition ft =new FadeTransition(Duration.millis(100),this);
		ft.setFromValue(1);
		ft.setToValue(0.1);
		ft.setCycleCount(4);
		ft.setAutoReverse(true);
		mProgress.setEffect(new ColorAdjust(255,0,0,1));
		ft.play();
		
	}
	
	//A appliquer apres avoir rajouter le groupe dans la scene
	private void initPathAnimation(Path mPath){
		javafx.scene.shape.Path path = new javafx.scene.shape.Path();
		MoveTo mvo = new MoveTo(this.getLayoutX(),this.getLayoutY());
		path.getElements().add(mvo);
		for(int i=1;i< mPath.getLength();i++){
			Step current = mPath.getStep(i);
			LineTo dest = new LineTo();
			dest.xProperty().bind(Main.TILE_SIZE_X.multiply(current.getY()));
			dest.yProperty().bind(Main.TILE_SIZE_Y.multiply(current.getX()));
			path.getElements().add(dest);
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
	
	/*public void initPathAnimation(){
		initPathAnimation(mSbire.getPath());
	}*/
	public void firstPlay(){
		initPathAnimation(mSbire.getPath());
		pt= new ParallelTransition(this,mMovement,applyMove);
		pt.setRate(speedRate);
		//mMoves.play();
		pt.play();
	}
	
	public void play(){
		//mMovement.play();
		//applyMove.play();
		if(pt!=null){
			pt.play();
		}
	}

	public void pause(){
		//applyMove.pause();
		//mMovement.pause();
		if(pt!=null)
			pt.pause();
	}
	
	public void stop(){
		//applyMove.stop();
		//mMovement.stop();
		if(pt!= null){
			pt.stop();
			this.translateXProperty().removeListener(listener);
			this.translateYProperty().removeListener(listener);
		}
	}
	
	public void setRate(int rate){
		//applyMove.setRate(rate);
		//mMovement.setRate(rate);
		speedRate = rate;
		if(pt!= null){
			pt.setRate(rate);
		}
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
