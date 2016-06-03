package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;
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
	
	//A modifier tour qui decrement avant que arme fait effet
	public static final Map<String,Integer[]> infosTour = new HashMap<String,Integer[]>();
	public static final Map<String,Image> infosImage = new HashMap<String,Image>();

	private static final int[] startCoord = new int[] { 0, 0 };
	private static final int[] endCoord = new int[] { 7,7 };

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private static final int ROW_COUNT = 8;
	private static final int COLUMN_COUNT = 8;

	public static final IntegerProperty TILE_SIZE_X = new SimpleIntegerProperty();
	public static final IntegerProperty TILE_SIZE_Y = new SimpleIntegerProperty();

	List<SbireView> mSbires = new ArrayList<SbireView>();
	List<TourView> mTours = new ArrayList<TourView>();
	private final PartieInterface partie = GameFactory.createPartie(ROW_COUNT, COLUMN_COUNT, startCoord, endCoord);

	private static final Group parentGroup= new Group();
	private final Stage stage1 = new Stage(StageStyle.TRANSPARENT);
	private final GridPane mGrid = new GridPane();
	private TourMenu mTourMenu;
	
	@Override
	public void start(Stage primaryStage) {
		infosTour.put("tour_archer", new Integer[]{9,10,100,1100});
		infosImage.put("tour_archer",new Image(getClass().getResource("archer2.png").toExternalForm()));
		infosTour.put("tour_canonportee", new Integer[]{11,15,200,1000});
		infosImage.put("tour_canonportee",new Image(getClass().getResource("portee_longue_faible.png").toExternalForm()));
		infosTour.put("tour_canonporteepuiss", new Integer[]{9,25,300,1300});
		infosImage.put("tour_canonporteepuiss",new Image(getClass().getResource("portee_longue_puissant.png").toExternalForm()));
		infosTour.put("canon_simple", new Integer[]{9,10,100,1100});
		infosImage.put("canon_simple",new Image(getClass().getResource("canon_simple.png").toExternalForm()));
		infosTour.put("canon_sup", new Integer[]{9,15,300,1000});
		infosImage.put("canon_sup",new Image(getClass().getResource("canon_sup.png").toExternalForm()));
		infosTour.put("canon_renforce", new Integer[]{9,25,400,1300});
		infosImage.put("canon_renforce",new Image(getClass().getResource("canon_renforce.png").toExternalForm()));
		infosTour.put("flechette_mortier", new Integer[]{16,15,600,1300});
		infosImage.put("flechette_mortier",new Image(getClass().getResource("flechette_mortier.png").toExternalForm()));
		infosTour.put("laser", new Integer[]{12,5,300,800});
		infosImage.put("laser",new Image(getClass().getResource("tourelle_laser.png").toExternalForm()));
		infosTour.put("mortier", new Integer[]{16,10,700,1300});
		infosImage.put("mortier",new Image(getClass().getResource("mortier_tower.png").toExternalForm()));
		infosTour.put("mortier_gold", new Integer[]{16,20,1000,1200});
		infosImage.put("mortier_gold",new Image(getClass().getResource("mortier_gold.png").toExternalForm()));
		infosTour.put("tonnerre", new Integer[]{9,20,500,1300});
		infosImage.put("tonnerre",new Image(getClass().getResource("tonnerre_tour.png").toExternalForm()));
		infosTour.put("tonnerre_plus", new Integer[]{9,20,800,800});
		infosImage.put("tonnerre_plus",new Image(getClass().getResource("tonnerre_tour2.png").toExternalForm()));
		infosTour.put("tonnerre_plus_gold", new Integer[]{12,25,800,1300});
		infosImage.put("tonnerre_plus_gold",new Image(getClass().getResource("portee_powerful_tonnerre.png").toExternalForm()));
		infosTour.put("triple_tonnerre", new Integer[]{12,30,1000,1300});
		infosImage.put("triple_tonnerre",new Image(getClass().getResource("powerful_tonnerre.png").toExternalForm()));
		
		infosImage.put("boule_bleu",  new Image(getClass().getResource("boule_bleu.png").toExternalForm()));
		infosImage.put("minion", new Image(getClass().getResource("minion.png").toExternalForm()));
		infosImage.put("boule_feu", new Image(getClass().getResource("boule_feu.png").toExternalForm()));
		infosImage.put("flechette", new Image(getClass().getResource("flechette.png").toExternalForm()));
		infosImage.put("backgroundTest", new Image(getClass().getResource("background.jpg").toExternalForm()));
		Scene scene = new Scene(parentGroup, WIDTH, HEIGHT, null);
		
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

		mGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent ev) {
				if(stage1.isShowing()){
					stage1.close();
					return;
				}
				if (ev.getButton() == MouseButton.SECONDARY) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							for (SbireView sbire : mSbires) {
								sbire.initPathAnimation();
								sbire.play();
								try {
									Thread.sleep(1200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}

					}).start();
				} else {
					final double currentCenterX = ((int) (ev.getSceneX()/TILE_SIZE_X.get()))*TILE_SIZE_X.get() +TILE_SIZE_X.get()/2;
					final double currentCenterY= ((int) (ev.getSceneY()/TILE_SIZE_Y.get()))*TILE_SIZE_Y.get()+TILE_SIZE_Y.get()/2;
					mTourMenu.setCurrentX(currentCenterX);
					mTourMenu.setCurrentY(currentCenterY);
					stage1.setX(ev.getScreenX()-TourMenu.WIDTH/2);
					stage1.setY(ev.getScreenY()-TourMenu.HEIGHT);
					stage1.show();
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
		Label ressourceValue = new Label();
		ressourceValue.textProperty().bind(partie.argentProperty().asString());
		ressourceValue.setId("moneyView");
		ressourceBox.getChildren().addAll(ressourceLabel,ressourceValue);
		
		HBox livesBox = new HBox(25);
		livesBox.setId("hboxLabels");
		Label livesLabel = new Label("LIVES");
		livesLabel.setId("labelTopMenu");
		Label livesValue = new Label();
		livesValue.textProperty().bind(partie.pointVieProperty().asString());
		livesValue.setId("livesView");
		livesBox.getChildren().addAll(livesLabel,livesValue);
		
		HBox score = new HBox(30);
		score.setId("hboxLabels");
		Label scoreLabel= new Label("SCORE");
		scoreLabel.setId("labelTopMenu");
		Label scoreValue = new Label();
		scoreValue.textProperty().bind(partie.scoreProperty().asString());
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
		//leftBottomMenu.layoutXProperty().bind(ressourceBox.layoutXProperty());
		leftBottomMenu.setPadding(new Insets(0,0,5,0));
		leftBottomMenu.setAlignment(Pos.CENTER);
		leftBottomMenu.setFillHeight(false);
		
		Button playButton = new Button();
		playButton.setId("play_button");
		Button pauseButton = new Button();
		pauseButton.setId("pause_button");
		pauseButton.setDisable(true);
		
		HBox level = new HBox(10);
		level.setId("hboxLabels");
		Label levelLabel = new Label("LEVEL");
		levelLabel.setId("labelTopMenu");
		Label levelValue = new Label();
		levelValue.setId("levelView");
		levelValue.textProperty().bind(partie.levelProperty().asString().concat("/"+partie.getNumberOfLevel()));
		level.getChildren().addAll(levelLabel,levelValue);
		
		HBox wave = new HBox(10);
		wave.setId("hboxLabels");
		Label waveLabel = new Label("SBIRE");
		waveLabel.setId("labelTopMenu");
		Label waveValue = new Label();
		waveValue.textProperty().bind(partie.sbireTueeProperty().asString().concat("/"+partie.getCurrentSbireNumber()));
		waveValue.setId("waveView");
		wave.getChildren().addAll(waveLabel,waveValue);
		
		Button menuView = new Button();
		menuView.setId("menuButton");
		
		Button animRate = new Button("x1");
		animRate.setId("animRateView");
		leftBottomMenu.getChildren().addAll(menuView,animRate,level,wave,playButton,pauseButton);
		/*Fin mise en places Menus*/
		
		parentGroup.getChildren().addAll(background,mGrid,topMenu,leftBottomMenu);
		primaryStage.setScene(scene);
		primaryStage.show();
		
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
		private Circle rayon;
		
		private double currentX=0;
		private double currentY=0;

		public TourMenu() {
			rayon = new Circle();
			rayon.setFill(Color.LIGHTGRAY);
			rayon.setOpacity(0.7);
			rayon.setStrokeWidth(3);
			rayon.setStroke(Color.CYAN);
			rayon.setVisible(false);
			
			parentGroup.getChildren().add(rayon);
			
			tourMenu = new TilePane();
			tourMenu.setPrefRows(3);
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
						

						//Tour tour = new Tour(partie,(int) (currentY/TILE_SIZE_Y.get()), (int) (currentX/TILE_SIZE_X.get()), 12, 30, 200);
						TourView mView = createTour(name,(int) (currentY/TILE_SIZE_Y.get()),(int) (currentX/TILE_SIZE_X.get()));
						mTours.add(mView);
						//lancement des tours
						mGrid.add(mView, mView.getColumn(), mView.getRow());
						GridPane.setHalignment(mView, HPos.CENTER);
						GridPane.setValignment(mView, VPos.CENTER);
						partie.timeToSetSbirePath();
						
						rayon.setVisible(false);
						stage1.close();
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
	
	private final String selectionTime = "00:04";
	private final StringProperty time = new SimpleStringProperty(selectionTime);
	private int compteur = 4;
	private final Timeline chronometer = new Timeline(new KeyFrame(Duration.millis(1000),
			new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent t){
			compteur--;
			int min = compteur/60;
			int sec = compteur % 60;
			time.set("0"+min+":"+sec);
			if(compteur==0){
				partie.initSbiresOnLevel();
				for (SbireInterface sbire : partie.getSbireList()) {
					SbireView sb = new SbireView(infosImage.get("minion"), sbire, 40, 40);
					sbire.setOnSbireDestroy(sb);
					parentGroup.getChildren().add(sb);
					mSbires.add(sb);
				}
				partie.timeToSetSbirePath();
				stage1.close();
				mTourMenu.setDisable(true);
				compteur =2*60;
				time.set(selectionTime);
				chronometer.stop();
			}
		}
	}));
}
