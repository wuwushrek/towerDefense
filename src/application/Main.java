package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import modele.GameFactory;
import modele.PartieInterface;
import modele.SbireInterface;

public class Main extends Application {
	
	public static final Map<String,Integer[]> infosTour = new HashMap<String,Integer[]>();
	public static final Map<String,Image> infosImage = new HashMap<String,Image>();

	private static final int[] startCoord = new int[] { 1, 0 };
	private static final int[] endCoord = new int[] { 6,7 };

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private static final int ROW_COUNT = 8;
	private static final int COLUMN_COUNT = 8;
	private static final long BASIC_LAUNCH_TIME = 1200;

	public static final IntegerProperty TILE_SIZE_X = new SimpleIntegerProperty();
	public static final IntegerProperty TILE_SIZE_Y = new SimpleIntegerProperty();

	List<SbireView> mSbires = new ArrayList<SbireView>();
	List<TourView> mTours = new ArrayList<TourView>();
	private int speedRate =1;
	private final PartieInterface partie = GameFactory.createPartie(ROW_COUNT, COLUMN_COUNT, startCoord, endCoord);

	private static final Group parentGroup= new Group();
	private final Stage stage1 = new Stage(StageStyle.TRANSPARENT);
	private final GridPane mGrid = new GridPane();
	private TourMenu mTourMenu;
	private Circle rayon;
	private VBox deleteTour;
	private TourListener mTourListener = new TourListener();
	private SbireLauncher mSbireLauncher;
	private Label suppressionAnim = new Label();
	
	HBox levelDone = new HBox();
	HBox gameOver = new HBox();
	private Button nextLevel;
	private Button playButton;
	private Button pauseButton;
	private Button animRate;
	
	@Override
	public void start(Stage primaryStage) {
		infosTour.put("tour_archer", new Integer[]{9,10,300,1100});
		infosImage.put("tour_archer",new Image(getClass().getResource("archer2.png").toExternalForm()));
		infosTour.put("tour_canonportee", new Integer[]{9,15,400,1000});
		infosImage.put("tour_canonportee",new Image(getClass().getResource("portee_longue_faible.png").toExternalForm()));
		infosTour.put("tour_canonporteepuiss", new Integer[]{9,25,800,1300});
		infosImage.put("tour_canonporteepuiss",new Image(getClass().getResource("portee_longue_puissant.png").toExternalForm()));
		infosTour.put("canon_simple", new Integer[]{9,10,200,1000});
		infosImage.put("canon_simple",new Image(getClass().getResource("canon_simple.png").toExternalForm()));
		infosTour.put("canon_sup", new Integer[]{9,12,500,1000});
		infosImage.put("canon_sup",new Image(getClass().getResource("canon_sup.png").toExternalForm()));
		infosTour.put("canon_renforce", new Integer[]{6,25,800,1300});
		infosImage.put("canon_renforce",new Image(getClass().getResource("canon_renforce.png").toExternalForm()));
		infosTour.put("flechette_mortier", new Integer[]{12,15,1000,1200});
		infosImage.put("flechette_mortier",new Image(getClass().getResource("flechette_mortier.png").toExternalForm()));
		infosTour.put("laser", new Integer[]{12,5,300,1000});
		infosImage.put("laser",new Image(getClass().getResource("tourelle_laser.png").toExternalForm()));
		infosTour.put("mortier", new Integer[]{16,20,1200,1500});
		infosImage.put("mortier",new Image(getClass().getResource("mortier_tower.png").toExternalForm()));
		infosTour.put("mortier_gold", new Integer[]{16,25,1500,1500});
		infosImage.put("mortier_gold",new Image(getClass().getResource("mortier_gold.png").toExternalForm()));
		infosTour.put("tonnerre", new Integer[]{9,20,1000,1300});
		infosImage.put("tonnerre",new Image(getClass().getResource("tonnerre_tour.png").toExternalForm()));
		infosTour.put("tonnerre_plus", new Integer[]{9,20,1700,900});
		infosImage.put("tonnerre_plus",new Image(getClass().getResource("tonnerre_tour2.png").toExternalForm()));
		infosTour.put("tonnerre_plus_gold", new Integer[]{12,25,2000,1200});
		infosImage.put("tonnerre_plus_gold",new Image(getClass().getResource("portee_powerful_tonnerre.png").toExternalForm()));
		infosTour.put("triple_tonnerre", new Integer[]{12,10,1000,1500});
		infosImage.put("triple_tonnerre",new Image(getClass().getResource("powerful_tonnerre.png").toExternalForm()));
		
		infosImage.put("boule_bleu",  new Image(getClass().getResource("boule_bleu.png").toExternalForm()));
		infosImage.put("minion", new Image(getClass().getResource("minion.png").toExternalForm()));
		infosImage.put("boule_feu", new Image(getClass().getResource("boule_feu.png").toExternalForm()));
		infosImage.put("flechette", new Image(getClass().getResource("flechette.png").toExternalForm()));
		infosImage.put("backgroundTest", new Image(getClass().getResource("background.jpg").toExternalForm()));
		infosImage.put("troll", new Image(getClass().getResource("troll.png").toExternalForm()));
		infosImage.put("boss", new Image(getClass().getResource("boss.png").toExternalForm()));
		infosImage.put("depart", new Image(getClass().getResource("depart.png").toExternalForm()));
		infosImage.put("arrivee", new Image(getClass().getResource("arrivee.png").toExternalForm()));
		
		Scene scene = new Scene(parentGroup, WIDTH, HEIGHT, null);
		primaryStage.setMinHeight(450);
		//Background
		ImageView background = new ImageView(infosImage.get("backgroundTest")) ;
		background.fitHeightProperty().bind(scene.heightProperty());
		background.fitWidthProperty().bind(scene.widthProperty());
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()) ;
		
		SelectTourMenuItem.setBackgroundImage(new Image(getClass().getResource("items_back.png").toExternalForm()));
		SelectTourMenuItem.setDamageImage(new Image(getClass().getResource("sword.png").toExternalForm()));
		SelectTourMenuItem.setPrixImage(new Image(getClass().getResource("coin-euro.png").toExternalForm()));
		SelectTourMenuItem.setPorteeImage(new Image(getClass().getResource("portee_im.png").toExternalForm()));
		SelectTourMenuItem.setSpeedImage(new Image(getClass().getResource("bullet_speed.png").toExternalForm()));

		TILE_SIZE_X.bind(scene.widthProperty().divide(COLUMN_COUNT));
		TILE_SIZE_Y.bind(scene.heightProperty().divide(ROW_COUNT));
	
		//Menu tours
		mTourMenu = new TourMenu();
		Scene sceneMenu = new Scene(mTourMenu , TourMenu.WIDTH,TourMenu.HEIGHT);
		sceneMenu.setFill(null);
		stage1.setScene(sceneMenu);
		//fin Menu tours

		mGrid.setGridLinesVisible(true);
		mGrid.prefWidthProperty().bind(scene.widthProperty());
		mGrid.prefHeightProperty().bind(scene.heightProperty());
		ColumnConstraints cc = new ColumnConstraints();
		cc.prefWidthProperty().bind(TILE_SIZE_X);

		for (int i = 0; i < COLUMN_COUNT; i++) {
			mGrid.getColumnConstraints().add(cc);
		}
		RowConstraints rc = new RowConstraints();
		rc.prefHeightProperty().bind(TILE_SIZE_Y);

		for (int i = 0; i < ROW_COUNT; i++) {
			mGrid.getRowConstraints().add(rc);
		}
		
		int[][] walls = new int[2*COLUMN_COUNT][2];
		
		for(int i=0;i<COLUMN_COUNT; i++){
			walls[i][0]=0;
			walls[i][1]=i;
			walls[i+COLUMN_COUNT][0]=ROW_COUNT-1;
			walls[i+COLUMN_COUNT][1]=i;
		}
		
		//On met les murs (les bords et precise les points de depart et d'arrivee
		partie.setWalls(walls);
		ImageView depart = new ImageView(infosImage.get("depart"));
		depart.setSmooth(true);
		depart.setCache(true);
		depart.fitHeightProperty().bind(TILE_SIZE_Y);
		depart.fitWidthProperty().bind(TILE_SIZE_X);
		mGrid.add(depart, startCoord[1], startCoord[0]);
		GridPane.setHalignment(depart, HPos.CENTER);
		GridPane.setValignment(depart, VPos.CENTER);
		
		ImageView arrivee = new ImageView(infosImage.get("arrivee"));
		arrivee.setSmooth(true);
		arrivee.setCache(true);
		arrivee.fitHeightProperty().bind(TILE_SIZE_Y);
		arrivee.fitWidthProperty().bind(TILE_SIZE_X);
		mGrid.add(arrivee, endCoord[1], endCoord[0]);
		GridPane.setHalignment(arrivee, HPos.CENTER);
		GridPane.setValignment(arrivee, VPos.CENTER);

		mGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent ev) {
				if(stage1.isShowing()){
					stage1.close();
					return;
				}
				if (ev.getButton() == MouseButton.PRIMARY && !mTourMenu.isDisable()) {
					int xIndex = (int) (ev.getSceneX()/TILE_SIZE_X.get());
					int yIndex =(int) (ev.getSceneY()/TILE_SIZE_Y.get());
					if(partie.isPathPossible(yIndex, xIndex)){
						final double currentCenterX = xIndex*TILE_SIZE_X.get() +TILE_SIZE_X.get()/2;
						final double currentCenterY= yIndex*TILE_SIZE_Y.get()+TILE_SIZE_Y.get()/2;
						mTourMenu.checkEnable(partie.argentProperty().get());
						mTourMenu.setCurrentX(currentCenterX);
						mTourMenu.setCurrentY(currentCenterY);
						stage1.setX(ev.getScreenX()-TourMenu.WIDTH/2);
						stage1.setY(ev.getScreenY()-TourMenu.HEIGHT);
						stage1.show();
					}
				}
			}

		});
		
		//Mise en place des differents menus
		//Top Menu
		HBox topMenu = new HBox(25);
		topMenu.prefWidthProperty().bind(scene.widthProperty());
		topMenu.setPadding(new Insets(5));
		topMenu.setFillHeight(false);
		topMenu.setAlignment(Pos.CENTER);
		
		HBox ressourceBox = new HBox(25);
		ressourceBox.setId("hboxLabels");
		Label ressourceLabel = new Label("MONEY");
		ressourceLabel.setId("labelTopMenu");
		Label ressourceValue = new Label(""+partie.argentProperty().get());
		partie.argentProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						ressourceValue.setText(""+newValue);
					}
				});
			}
		});
		ressourceValue.setId("moneyView");
		ressourceBox.getChildren().addAll(ressourceLabel,ressourceValue);
		
		HBox livesBox = new HBox(25);
		livesBox.setId("hboxLabels");
		Label livesLabel = new Label("LIVES");
		livesLabel.setId("labelTopMenu");
		Label livesValue = new Label(""+partie.pointVieProperty().get());
		partie.pointVieProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						livesValue.setText(""+newValue);
					}
				});
			}
		});
		livesValue.setId("livesView");
		livesBox.getChildren().addAll(livesLabel,livesValue);
		
		HBox score = new HBox(30);
		score.setId("hboxLabels");
		Label scoreLabel= new Label("SCORE");
		scoreLabel.setId("labelTopMenu");
		Label scoreValue = new Label(""+partie.scoreProperty().get());
		partie.scoreProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						scoreValue.setText(""+newValue);
					}
				});
			}
		});
		scoreValue.setId("scoreView");
		score.getChildren().addAll(scoreLabel,scoreValue);
		
		HBox chronometerBox = new HBox(25);
		chronometerBox.setId("hboxLabels");
		Label chronometerLabel = new Label("TIME");
		chronometerLabel.setId("labelTopMenu");
		Label chronometerValue = new Label();
		chronometerValue.textProperty().bind(time);
		chronometerValue.setId("chronoView");
		chronometerBox.getChildren().addAll(chronometerLabel,chronometerValue);
		topMenu.getChildren().addAll(ressourceBox,livesBox,score,chronometerBox);
		
		//Bottom Menu
		HBox leftBottomMenu = new HBox(5);
		leftBottomMenu.prefWidthProperty().bind(scene.widthProperty());
		leftBottomMenu.layoutYProperty().bind(scene.heightProperty().subtract(leftBottomMenu.heightProperty()));
		leftBottomMenu.setPadding(new Insets(0,0,5,0));
		leftBottomMenu.setAlignment(Pos.CENTER);
		leftBottomMenu.setFillHeight(false);
		
		playButton = new Button();
		playButton.setId("play_button");
		playButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				playGame();
			}
		});
		pauseButton = new Button();
		pauseButton.setId("pause_button");
		pauseButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				pauseGame();
			}
		});
		pauseButton.setDisable(true);
		
		HBox level = new HBox(10);
		level.setId("hboxLabels");
		Label levelLabel = new Label("LEVEL");
		levelLabel.setId("labelTopMenu");
		Label levelValue = new Label(""+partie.levelProperty().get()+"/"+partie.getNumberOfLevel());
		partie.levelProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						levelValue.setText(""+newValue+"/"+partie.getNumberOfLevel());
					}
				});
			}
		});
		levelValue.setId("levelView");
		level.getChildren().addAll(levelLabel,levelValue);
		
		HBox wave = new HBox(10);
		wave.setId("hboxLabels");
		Label waveLabel = new Label("SBIRE");
		waveLabel.setId("labelTopMenu");
		Label waveValue = new Label(""+partie.sbireTueeProperty().get()+"/"+partie.getCurrentSbireNumber());
		partie.sbireTueeProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						waveValue.setText(""+newValue+"/"+partie.getCurrentSbireNumber());
					}
				});
			}
		});
		waveValue.setId("waveView");
		wave.getChildren().addAll(waveLabel,waveValue);
		
		Button menuView = new Button();
		menuView.setId("menuButton");
		
		animRate = new Button("x1");
		animRate.setId("animRateView");
		animRate.setDisable(true);
		animRate.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				changeGameSpeed();
			}
			
		});
		
		leftBottomMenu.getChildren().addAll(menuView,animRate,level,wave,playButton,pauseButton);
		/*Fin mise en places Menus*/
		
		rayon = new Circle();
		rayon.setFill(Color.LIGHTGRAY);
		rayon.setOpacity(0.7);
		rayon.setStrokeWidth(3);
		rayon.setStroke(Color.CYAN);
		rayon.setVisible(false);
		//rayon.setDisable(true);
		
		deleteTour = new VBox();
		//StackPane.setAlignment(deleteTour, );
		deleteTour.prefWidthProperty().bind(TILE_SIZE_X);
		Label deleteButton = new Label("DELETE");
		deleteButton.setId("delete_button");
		deleteTour.setAlignment(Pos.CENTER);
		deleteTour.setId("hboxLabels");
		deleteTour.getChildren().add(deleteButton);
		
		suppressionAnim.setId("moneyView");
		suppressionAnim.setVisible(false);
		
		//Level Done view 
		levelDone.prefWidthProperty().bind(scene.widthProperty());
		levelDone.prefHeightProperty().bind(scene.heightProperty());
		levelDone.setAlignment(Pos.CENTER);
		levelDone.setId("gameFinishBackground");
		levelDone.setOpacity(0.0);
		levelDone.setFillHeight(false);
		
		VBox mainVbox = new VBox(5);
		mainVbox.setPadding(new Insets(15));
		mainVbox.setId("hboxLabels");
		Label labelLevel = new Label("LEVEL COMPLETE !!!!");
		labelLevel.setId("levelComplete");
		StackPane counter = new StackPane();
		Circle center = new Circle();
		center.setRadius(150);
		center.setId("backCounter");
		Label counterLabel = new Label();
		counterLabel.textProperty().bind(timeNextLevel);
		counterLabel.setId("counterLabel");
		Reflection r = new Reflection();
		r.setBottomOpacity(0);
		counterLabel.setEffect(r);
		counter.getChildren().addAll(center,counterLabel);
		nextLevel = new Button("GO ! Next Level");
		nextLevel.setDisable(true);
		nextLevel.setId("mainMenu");
		nextLevel.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				timeNextLevel.set(""+nextLevelWait);
				parentGroup.getChildren().remove(levelDone);
				chronometer.play();
			}
			
		});
		mainVbox.getChildren().addAll(labelLevel,counter,nextLevel);
		partie.levelDoneProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean old, Boolean newValue) {
				if(newValue == true){
					for (SbireView sbireView: mSbires){
						sbireView.stop();
					}
					Platform.runLater(new Runnable(){
						@Override
						public void run() {
							nextLevel.setDisable(true);
							playButton.setDisable(false);
							pauseButton.setDisable(true);
							animRate.setDisable(true);
							speedRate=1;
							animRate.setText("x1");
							partie.nextLevelInitialisation();
							parentGroup.getChildren().add(levelDone);
							parentGroup.getChildren().removeAll(mSbires);
							mSbires.clear();
						}
						
					});
					FadeTransition showSlow = new FadeTransition();
					showSlow.setDuration(Duration.millis(500));
					showSlow.setByValue(0.0);
					showSlow.setToValue(0.8);
					showSlow.setAutoReverse(true);
					showSlow.setNode(levelDone);
					showSlow.setOnFinished(new EventHandler<ActionEvent>(){
						@Override
						public void handle(ActionEvent event) {
							chronometerNextLevel.play();
						}
						
					});
					showSlow.play();
				}
			}
			
		});
		
		levelDone.getChildren().add(mainVbox);
		/*End level done view*/
		
		//Game Over view
		gameOver.prefWidthProperty().bind(scene.widthProperty());
		gameOver.prefHeightProperty().bind(scene.heightProperty());
		gameOver.setAlignment(Pos.CENTER);
		gameOver.setId("gameOverView");
		gameOver.setFillHeight(false);
		
		VBox mainOver = new VBox(25);
		mainOver.setId("hboxLabels");
		mainOver.setFillWidth(false);
		ImageView overLabel = new ImageView();
		overLabel.setPreserveRatio(true);
		overLabel.setCache(true);
		overLabel.setSmooth(true);
		overLabel.setId("overLabel");
		ImageView teteMort = new ImageView();
		teteMort.setPreserveRatio(true);
		teteMort.setSmooth(true);
		teteMort.setCache(true);
		teteMort.setId("teteMort");
		teteMort.setFitWidth(180);
		Button mainMenu = new Button("Menu");
		mainMenu.setId("mainMenu");
		Button exitButton = new Button("Quit");
		exitButton.setId("mainMenu");
		exitButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				primaryStage.close();
			}
			
		});
		partie.aliveProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean old, Boolean newValue) {
				if(newValue == false){
					for (SbireView sbireView: mSbires){
						sbireView.stop();
					}
					Platform.runLater(new Runnable(){
						@Override
						public void run() {
							parentGroup.getChildren().add(gameOver);
							parentGroup.getChildren().removeAll(mSbires);
							mSbires.clear();
						}
						
					});
					FadeTransition showSlow = new FadeTransition();
					showSlow.setDuration(Duration.millis(500));
					showSlow.setByValue(0.0);
					showSlow.setToValue(0.8);
					showSlow.setAutoReverse(true);
					showSlow.setNode(gameOver);
					showSlow.play();
				}
			}
			
		});
		HBox buttonBox = new HBox(15);
		//exitButton.prefWidthProperty().bind(mainMenu.widthProperty());
		buttonBox.getChildren().addAll(mainMenu,exitButton);
		mainOver.getChildren().addAll(overLabel,teteMort,buttonBox);
		gameOver.getChildren().add(mainOver);
		gameOver.setOpacity(0.0);
		/*end Game over view*/
		
		parentGroup.getChildren().addAll(background,rayon,mGrid,topMenu,leftBottomMenu,suppressionAnim);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		chronometerNextLevel.setAutoReverse(true);
		chronometerNextLevel.setCycleCount(Animation.INDEFINITE);
		chronometer.setAutoReverse(true);
		chronometer.setCycleCount(Animation.INDEFINITE);
		chronometer.play();
	}

	public static void main(String[] args) {
		launch(args);
	}

	class TourMenu extends Group {
		private final static int WIDTH = 495;
		private final static int HEIGHT = 270;

		double padding = 10;
		double hauteurContour = 25;

		private ScrollPane mScroll;
		private TilePane tourMenu;
		private Polygon contour;
		
		private double currentX=0;
		private double currentY=0;

		public TourMenu() {
			tourMenu = new TilePane();
			tourMenu.setPrefColumns(4);

			for (String name : infosTour.keySet()) {
				SelectTourMenuItem menuItem = new SelectTourMenuItem(
						infosImage.get(name), name,infosTour.get(name)[1], infosTour.get(name)[2],
						infosTour.get(name)[0],infosTour.get(name)[3]);
				menuItem.setOnMouseEntered(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent ev) {
						menuItem.mouseEntered();
						rayon.setVisible(true);
						rayon.setRadius(menuItem.getPortee()*Math.min(TILE_SIZE_X.get(), TILE_SIZE_Y.get()));
					}
					
				});
				menuItem.setOnMouseExited(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent arg0) {
						menuItem.mouseExited();
						rayon.setVisible(false);
					}
				});
				menuItem.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent e){
						TourView mView = createTour(name,(int) (currentY/TILE_SIZE_Y.get()),(int) (currentX/TILE_SIZE_X.get()));
						mTours.add(mView);
						//lancement des tours
						mGrid.add(mView, mView.getColumn(), mView.getRow());
						GridPane.setHalignment(mView, HPos.CENTER);
						GridPane.setValignment(mView, VPos.CENTER);
						rayon.setVisible(false);
						stage1.close();
						mView.addEventHandler(MouseEvent.ANY,mTourListener);
					}
				});
				tourMenu.getChildren().add(menuItem);
			}

			mScroll = new ScrollPane();
			mScroll.setContent(tourMenu);
			mScroll.setPrefWidth(WIDTH - 2 * padding);
			mScroll.setPrefHeight(HEIGHT -padding - hauteurContour);
			mScroll.setLayoutX(padding);
			mScroll.setLayoutY(padding);
			mScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
			mScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			mScroll.setOpacity(0.8);

			double width = WIDTH;
			double height = HEIGHT;
			contour = new Polygon();
			contour.getPoints().addAll(width/2,height,width/2-hauteurContour,height-hauteurContour,
					width/2+hauteurContour,height-hauteurContour);
			contour.setStroke(Color.LIGHTCYAN);
			contour.setFill(Color.LIGHTGRAY);
			this.getChildren().addAll(contour,mScroll);
		}
		
		public void checkEnable(int moneyLeft){
			for(Node node :tourMenu.getChildren()){
				SelectTourMenuItem tourItem = (SelectTourMenuItem) node;
				if(tourItem.getPrix()>moneyLeft){
					tourItem.setDisable(true);
				}else{
					tourItem.setDisable(false);
				}
			}
		}
		
		public void setCurrentX(double currentX){
			this.currentX=currentX;
			rayon.setCenterX(currentX);
		}
		
		public void setCurrentY(double currentY){
			this.currentY=currentY;
			rayon.setCenterY(currentY);
		}
		
		public TourView createTour(String name , int rowIndex , int columnIndex ){
			if(name.equals("tour_archer")){
				return new TourView_Acher(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("tour_canonportee")){
				return new TourView_CannonLPortee(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("tour_canonporteepuiss")){
				return new TourView_CanonLPorteePuiss(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("canon_simple")){
				return new TourView_CanonSimple(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("canon_sup")){
				return new TourView_CanonSup(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("canon_renforce")){
				return new TourView_CanonSupRenforce(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("flechette_mortier")){
				return new TourView_FlechetteMortier(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("laser")){
				return new TourView_Laser(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("mortier")){
				return new TourView_Mortier(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("mortier_gold")){
				return new TourView_MortierGold(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("tonnerre")){
				return new TourView_Tonnerre(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("tonnerre_plus")){
				return new TourView_TonnerrePlus(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("tonnerre_plus_gold")){
				return new TourView_TonnerrePlusGold(rowIndex ,columnIndex,partie.getTourSideInterface());
			}else if(name.equals("triple_tonnerre")){
				return new TourView_TripleTonnere(rowIndex ,columnIndex,partie.getTourSideInterface());
			}
			return null;
		}
	}
	
	public static void addNode(Node node){
		parentGroup.getChildren().add(node);
	}
	public static void addAll(List<Group> nodes){
		parentGroup.getChildren().addAll(nodes);
	}
	
	public static void removeNode(Node node){
		parentGroup.getChildren().remove(node);
	}
	
	public void pauseGame(){
		new Thread(new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(mSbireLauncher!=null && mSbireLauncher.isAlive()){
					mSbireLauncher.suspend();
				}
			}
			
		}).start();
		for(SbireView sbire: mSbires){
			sbire.pause();
		}
		for(TourView tour : mTours){
			tour.pause();
		}
		
		pauseButton.setDisable(true);
		playButton.setDisable(false);
	}
	
	public void playGame(){
		new Thread(new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(mSbireLauncher!= null && mSbireLauncher.isAlive()){
					mSbireLauncher.resume();
				}
			}
		}).start();
		if(compteur!=selectionTimeInt){
			compteur=0;
			return;
		}
		for(TourView tour : mTours){
			tour.play();
		}
		for(SbireView sbire: mSbires){
			sbire.play();
		}
		pauseButton.setDisable(false);
		playButton.setDisable(true);
	}
	
	public void changeGameSpeed(){
		if(animRate.getText().equals("x1")){
			animRate.setText("x2");
			speedRate=2;
		}else if(animRate.getText().equals("x2")){
			animRate.setText("x3");
			speedRate=3;
		}else if(animRate.getText().equals("x3")){
			animRate.setText("x1");
			speedRate=1;
		}
		partie.setRateFactor(speedRate);
		TourView.setRate(speedRate);
		for(SbireView sbire : mSbires){
			sbire.setRate(speedRate);
		}
	}
	
	private final String selectionTime = "02:00";
	private final StringProperty time = new SimpleStringProperty(selectionTime);
	private final int selectionTimeInt = 2*60;
	private int compteur = selectionTimeInt;
	private final Timeline chronometer = new Timeline(new KeyFrame(Duration.millis(1000),
			new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent t){
			compteur--;
			int min = compteur/60;
			int sec = compteur % 60;
			time.set("0"+min+":"+(sec<10?"0"+sec:sec));
			if(compteur<=0){
				partie.initSbiresOnLevel();
				for (SbireInterface sbire : partie.getSbireList()) {
					SbireView sb;
					if(sbire.getNom().equals("minionSbire")){
						sb = new SbireView(infosImage.get("minion"), sbire, 40, 40);
					}else if(sbire.getNom().equals("troll")){
						sb = new SbireView(infosImage.get("troll"), sbire, 40, 40);
					}else{
						sb = new SbireView(infosImage.get("boss"), sbire, 40, 40);
					}
					sbire.setOnSbireDestroy(sb);
					parentGroup.getChildren().add(sb);
					mSbires.add(sb);
				}
				mSbireLauncher = new SbireLauncher();
				mSbireLauncher.start();
				stage1.close();
				mTourMenu.setDisable(true);
				compteur =selectionTimeInt;
				time.set(selectionTime);
				pauseButton.setDisable(false);
				playButton.setDisable(true);
				animRate.setDisable(false);
				chronometer.stop();
				
			}
		}
	}));
	
	private final int nextLevelWait = 10;
	private int compteurNextLevel = nextLevelWait;
	private final StringProperty timeNextLevel = new SimpleStringProperty(""+nextLevelWait);
	private final Timeline chronometerNextLevel = new Timeline(new KeyFrame(Duration.millis(1000),
			new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent t){
			compteurNextLevel--;
			timeNextLevel.set(""+compteurNextLevel);
			if(compteurNextLevel ==0){
				compteurNextLevel= nextLevelWait;
				nextLevel.setDisable(false);
				mTourMenu.setDisable(false);
				chronometerNextLevel.stop();
			}
		}
	}));
	
	class TourListener implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent event) {
			TourView tourSource = (TourView) event.getSource();
			if(event.getEventType()== MouseEvent.MOUSE_ENTERED){
				if(compteur!=selectionTimeInt){
					tourSource.getChildren().add(deleteTour);
				}
			}else if (event.getEventType()==MouseEvent.MOUSE_EXITED){
				tourSource.getChildren().remove(deleteTour);
			}else if(event.getEventType()==MouseEvent.MOUSE_CLICKED){
				if(compteur!=selectionTimeInt){
					event.consume();
					int xPosition =((int) event.getSceneX()/TILE_SIZE_X.get())*TILE_SIZE_X.get()+TILE_SIZE_X.get()/2;
					int yPosition =((int) event.getSceneY()/TILE_SIZE_Y.get())*TILE_SIZE_Y.get()+TILE_SIZE_Y.get()/2;
					tourSource.getTour().destroy();
					mGrid.getChildren().remove(tourSource);
					mTours.remove(tourSource);
					suppressionAnim.setVisible(true);
					suppressionAnim.setText(""+tourSource.getTour().getCost());
					suppressionAnim.setLayoutX(xPosition);
					suppressionAnim.setLayoutY(yPosition);
					TranslateTransition translate = new TranslateTransition();
					translate.setByY(-TILE_SIZE_Y.get());
					translate.setDuration(Duration.millis(200));
					translate.setNode(suppressionAnim);
					ScaleTransition scale = new ScaleTransition();
					scale.setDuration(Duration.millis(200));
					scale.setNode(suppressionAnim);
					scale.setFromX(1.0);
					scale.setFromY(1.0);
					scale.setToX(2.0);
					scale.setToY(2.0);
					translate.setOnFinished(new EventHandler<ActionEvent>(){
						@Override
						public void handle(ActionEvent arg0) {
							System.out.println("translation FINIE");
							scale.stop();
							suppressionAnim.setVisible(false);
						}
					});
					translate.play();
					scale.play();
				}
			}
		}
	}
	class SbireLauncher extends Thread {
		@Override
		public void run(){
			for (SbireView sbire : mSbires) {
				try {
					Thread.sleep(BASIC_LAUNCH_TIME/speedRate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sbire.firstPlay();
			}
		}
	}
}
