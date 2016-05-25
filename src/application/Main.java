package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modele.GameFactory;
import modele.PartieInterface;
import modele.SbireInterface;
import modele.TourSideInterface;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Main extends Application {
	
	public static final Map<String,Integer[]> infosTour = new HashMap<String,Integer[]>();
	public static final Map<String,Image> infosImage = new HashMap<String,Image>();

	private static final int[] startCoord = new int[] { 8, 0 };
	private static final int[] endCoord = new int[] { 2, 5 };

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private static final int ROW_COUNT = 15;
	private static final int COLUMN_COUNT = 12;

	public static final IntegerProperty TILE_SIZE_X = new SimpleIntegerProperty();
	public static final IntegerProperty TILE_SIZE_Y = new SimpleIntegerProperty();

	List<SbireView> mSbires = new ArrayList<SbireView>();
	private final PartieInterface partie = GameFactory.createPartie(ROW_COUNT, COLUMN_COUNT, startCoord, endCoord);

	private final Group root = new Group();
	private final Stage stage1 = new Stage(StageStyle.TRANSPARENT);
	private final GridPane mGrid = new GridPane();
	private TourMenu mTourMenu;
	
	@Override
	public void start(Stage primaryStage) {
		infosTour.put("tour_archer", new Integer[]{9,20,100,500});
		infosImage.put("tour_archer",new Image(getClass().getResource("archer2.png").toExternalForm()));
		infosTour.put("tour_canonportee", new Integer[]{16,40,200,600});
		infosImage.put("tour_canonportee",new Image(getClass().getResource("portee_longue_faible.png").toExternalForm()));
		infosTour.put("tour_canonporteepuiss", new Integer[]{16,50,300,500});
		infosImage.put("tour_canonporteepuiss",new Image(getClass().getResource("portee_longue_puissant.png").toExternalForm()));
		infosTour.put("canon_simple", new Integer[]{9,40,250,600});
		infosImage.put("canon_simple",new Image(getClass().getResource("canon_simple.png").toExternalForm()));
		infosTour.put("canon_sup", new Integer[]{9,50,300,500});
		infosImage.put("canon_sup",new Image(getClass().getResource("canon_sup.png").toExternalForm()));
		infosTour.put("canon_renforce", new Integer[]{9,60,400,500});
		infosImage.put("canon_renforce",new Image(getClass().getResource("canon_renforce.png").toExternalForm()));
		infosTour.put("flechette_mortier", new Integer[]{16,60,600,400});
		infosImage.put("flechette_mortier",new Image(getClass().getResource("flechette_mortier.png").toExternalForm()));
		infosTour.put("laser", new Integer[]{16,20,300,400});
		infosImage.put("laser",new Image(getClass().getResource("tourelle_laser.png").toExternalForm()));
		infosTour.put("mortier", new Integer[]{25,30,700,700});
		infosImage.put("mortier",new Image(getClass().getResource("mortier_tower.png").toExternalForm()));
		infosTour.put("mortier_gold", new Integer[]{25,40,1000,650});
		infosImage.put("mortier_gold",new Image(getClass().getResource("mortier_gold.png").toExternalForm()));
		infosTour.put("tonnerre", new Integer[]{16,30,500,400});
		infosImage.put("tonnerre",new Image(getClass().getResource("tonnerre_tour.png").toExternalForm()));
		infosTour.put("tonnerre_plus", new Integer[]{16,40,600,350});
		infosImage.put("tonnerre_plus",new Image(getClass().getResource("tonnerre_tour2.png").toExternalForm()));
		infosTour.put("tonnerre_plus_gold", new Integer[]{20,40,800,400});
		infosImage.put("tonnerre_plus_gold",new Image(getClass().getResource("portee_powerful_tonnerre.png").toExternalForm()));
		infosTour.put("triple_tonnerre", new Integer[]{16,50,1000,400});
		infosImage.put("triple_tonnerre",new Image(getClass().getResource("powerful_tonnerre.png").toExternalForm()));
		
		infosImage.put("minion", new Image(getClass().getResource("minion.png").toExternalForm()));
		Scene scene = new Scene(root, WIDTH, HEIGHT, Color.GRAY);

		SelectTourMenuItem.setBackgroundImage(new Image(getClass().getResource("items_back.png").toExternalForm()));
		SelectTourMenuItem.setDamageImage(new Image(getClass().getResource("bullet_speed.png").toExternalForm()));
		SelectTourMenuItem.setPrixImage(new Image(getClass().getResource("coin-euro.png").toExternalForm()));
		SelectTourMenuItem.setPorteeImage(new Image(getClass().getResource("portee_im.png").toExternalForm()));
		SelectTourMenuItem.setSpeedImage(new Image(getClass().getResource("sword.png").toExternalForm()));

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
		
		//Tour tour1 = new Tour(partie, 2, 4, 9, 30, 1000);
		//Tour tour2 = new Tour(partie, 4, 3, 9, 10, 1000);
		//tour1.launch();
		//tour2.launch();
		//TourView mView = new TourView(this.getClass().getResource("tourelle.png").toExternalForm(), tour1);
		//TourView mView2 = new TourView(this.getClass().getResource("tourelle.png").toExternalForm(), tour2);
		//partie.timeToSetSbirePath();
		//mGrid.add(mView, mView.getColumn(), mView.getRow());
		//GridPane.setHalignment(mView, HPos.CENTER);
		//GridPane.setValignment(mView, VPos.CENTER);

		//GridPane.setHalignment(mView2, HPos.CENTER);
		//GridPane.setValignment(mView2, VPos.CENTER);

		//mGrid.add(mView2, mView2.getColumn(), mView2.getRow());
		/*for (SbireInterface sbire : partie.getSbireList()) {
			SbireView sb = new SbireView(infosImage.get("minion"), sbire, 20, 20);
			sbire.setOnSbireDestroy(sb);
			root.getChildren().add(sb);
			mSbires.add(sb);
		}*/

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
					// partie.mSbires.get(0).moveTo(posY, posX);
					// System.out.println("COUNT: "+partie.count());
					final double currentCenterX = ((int) (ev.getSceneX()/TILE_SIZE_X.get()))*TILE_SIZE_X.get() +TILE_SIZE_X.get()/2;
					final double currentCenterY= ((int) (ev.getSceneY()/TILE_SIZE_Y.get()))*TILE_SIZE_Y.get()+TILE_SIZE_Y.get()/2;
					//TourMenu tourMenu = new TourMenu(currentCenterX,currentCenterY);
					//scene.setFill(null);
					mTourMenu.setCurrentX(currentCenterX);
					mTourMenu.setCurrentY(currentCenterY);
					stage1.setX(ev.getScreenX()-TourMenu.WIDTH/2);
					stage1.setY(ev.getScreenY()-TourMenu.HEIGHT);
					stage1.show();
				}
			}

		});

		root.getChildren().addAll(mGrid);
		primaryStage.setScene(scene);
		primaryStage.show();
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
			//rayon.setCenterX(currentX);
			//rayon.setCenterY(currentY);
			rayon.setVisible(false);
			
			root.getChildren().add(rayon);
			
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
}
