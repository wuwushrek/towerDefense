package application;


import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
	
	 private static final int COLUMNS  =   6;
	 private static final int COUNT    =  36;
	 private static final int OFFSET_X =  0;
	 private static final int OFFSET_Y =  0;
	 private static final int WIDTH    = 100;
	 private static final int HEIGHT   = 100;
	
	private final static long BASIC_STEP_TIME = 2500;
	private int speedRate =1;
	
	private SbireInterface mSbire;
	private ProgressBar mProgress;
	private ImageView mImage;
	
	private PathTransition mMovement;
	private Timeline applyMove = new Timeline();
	private ParallelTransition pt;

	private double currentX ;
	private double currentY;
	private ChangeListener<Number> listener;
	private FadeTransition ft = new FadeTransition();
	private ImageView explosionAnim = new ImageView(ALauncher.infosImage.get("explosion"));
	private final SpriteAnimation explode;
	
	public SbireView(Image im, SbireInterface sbire , int width , int height){
		super();
		this.mSbire=sbire;

		this.setLayoutX(mSbire.getColumnIndex()*ALauncher.getTileSizeX().get());
		this.setLayoutY(mSbire.getRowIndex()*ALauncher.getTileSizeY().get());
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
		        currentX = xInScene;
		        currentY= yInScene;
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
		ft = new FadeTransition(Duration.millis(50),this);
		ft.setFromValue(1);
		ft.setToValue(0.1);
		ft.setAutoReverse(true);
		ft.setCycleCount(4);
		explosionAnim.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
		explode = new SpriteAnimation(
                explosionAnim,
                Duration.millis(300),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT);
		explode.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				ALauncher.removeNode(explosionAnim);
				ALauncher.removeNode(SbireView.this);
			}
			
		});
	}

	@Override
	public void onSbireDestroy(boolean isDead) {
		// TODO Auto-generated method stub
		stop();
		if(isDead){
			this.setVisible(false);
			explosionAnim.setX(currentX-WIDTH/2);
			explosionAnim.setY(currentY-HEIGHT/2);
			ALauncher.addNode(explosionAnim);
			explode.play();
		}else{
			ALauncher.removeNode(this);
		}
		//ALauncher.removeNode(this);
		/*Platform.runLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Group parent =(Group) getParent();
				if(parent!= null && SbireView.this != null){
					parent.getChildren().remove(SbireView.this);
				}
			}
		});*/
	}
	
	@Override
	public void onSbireTouched() {
		ft.playFromStart();
		mProgress.setEffect(new ColorAdjust(255,0,0,1));
	}
	
	//A appliquer apres avoir rajouter le groupe dans la scene
	private void initPathAnimation(Path mPath){
		javafx.scene.shape.Path path = new javafx.scene.shape.Path();
		MoveTo mvo = new MoveTo(this.getLayoutX(),this.getLayoutY());
		path.getElements().add(mvo);
		for(int i=1;i< mPath.getLength();i++){
			Step current = mPath.getStep(i);
			LineTo dest = new LineTo();
			dest.xProperty().bind(ALauncher.getTileSizeX().multiply(current.getY()));
			dest.yProperty().bind(ALauncher.getTileSizeY().multiply(current.getX()));
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
	
	public void firstPlay(){
		initPathAnimation(mSbire.getPath());
		pt= new ParallelTransition(this,mMovement,applyMove);
		pt.setRate(speedRate);
		pt.play();
	}
	
	public void play(){
		if(pt!=null){
			pt.play();
		}
	}

	public void pause(){
		if(pt!=null){
			pt.pause();
		}
	}
	
	public void stop(){
		if(pt!= null){
			pt.stop();
			ft.stop();
		}
	}
	
	public void setRate(int rate){
		speedRate = rate;
		ft.setRate(rate);
		explode.setRate(rate);
		if(pt!= null){
			pt.setRate(rate);
		}
	}
	@Override
	public double xPosProperty() {
		return currentX;
	}

	@Override
	public double yPosProperty() {
		return currentY;
	}

	class SpriteAnimation extends Transition {
		private final ImageView imageView;
	    private final int count;
	    private final int columns;
	    private final int offsetX;
	    private final int offsetY;
	    private final int width;
	    private final int height;

	    private int lastIndex;

	    public SpriteAnimation(
	            ImageView imageView, 
	            Duration duration, 
	            int count,   int columns,
	            int offsetX, int offsetY,
	            int width,   int height) {
	        this.imageView = imageView;
	        this.count     = count;
	        this.columns   = columns;
	        this.offsetX   = offsetX;
	        this.offsetY   = offsetY;
	        this.width     = width;
	        this.height    = height;
	        setCycleDuration(duration);
	        setInterpolator(Interpolator.LINEAR);
	    }

	    protected void interpolate(double k) {
	        final int index = Math.min((int) Math.floor(k * count), count - 1);
	        if (index != lastIndex) {
	            final int x = (index % columns) * width  + offsetX;
	            final int y = (index / columns) * height + offsetY;
	            imageView.setViewport(new Rectangle2D(x, y, width, height));
	            lastIndex = index;
	        }
	    }
	}
	
}
