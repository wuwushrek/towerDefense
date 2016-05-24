package application;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
import javafx.scene.shape.Polygon;

public class Main extends Application {

	private static final int[] startCoord = new int[] { 8, 0 };
	private static final int[] endCoord = new int[] { 2, 5 };

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private static final int ROW_COUNT = 15;
	private static final int COLUMN_COUNT = 12;

	public static final IntegerProperty TILE_SIZE_X = new SimpleIntegerProperty();
	public static final IntegerProperty TILE_SIZE_Y = new SimpleIntegerProperty();

	List<SbireView> mSbires = new ArrayList<SbireView>();

	private Group root = new Group();
	private Stage stage1 = null;
	
	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(root, WIDTH, HEIGHT, Color.GRAY);

		SelectTourMenuItem.setBackgroundImage(new Image(getClass().getResource("items_back.png").toExternalForm()));
		SelectTourMenuItem.setDamageImage(new Image(getClass().getResource("bullet_speed.png").toExternalForm()));
		SelectTourMenuItem.setPrixImage(new Image(getClass().getResource("coin-euro.png").toExternalForm()));
		SelectTourMenuItem.setPorteeImage(new Image(getClass().getResource("portee_im.png").toExternalForm()));
		SelectTourMenuItem.setSpeedImage(new Image(getClass().getResource("sword.png").toExternalForm()));

		TILE_SIZE_X.bind(scene.widthProperty().divide(COLUMN_COUNT));
		TILE_SIZE_Y.bind(scene.heightProperty().divide(ROW_COUNT));

		GridPane mGrid = new GridPane();
		mGrid.setGridLinesVisible(true);
		mGrid.prefWidthProperty().bind(scene.widthProperty());
		mGrid.prefHeightProperty().bind(scene.heightProperty());
		ColumnConstraints cc = new ColumnConstraints();
		// cc.setFillWidth(true);
		cc.prefWidthProperty().bind(TILE_SIZE_X);
		// cc.setHgrow(Priority.ALWAYS);

		for (int i = 0; i < COLUMN_COUNT; i++) {
			mGrid.getColumnConstraints().add(cc);
		}
		RowConstraints rc = new RowConstraints();
		rc.prefHeightProperty().bind(TILE_SIZE_Y);
		// rc.setFillHeight(true);
		// rc.setVgrow(Priority.ALWAYS);

		for (int i = 0; i < ROW_COUNT; i++) {
			mGrid.getRowConstraints().add(rc);
		}

		Partie partie = new Partie(ROW_COUNT, COLUMN_COUNT, startCoord, endCoord);
		Tour tour1 = new Tour(partie, 2, 4, 9, 30, 1000);
		Tour tour2 = new Tour(partie, 4, 3, 9, 10, 1000);
		tour1.launch();
		tour2.launch();
		TourView mView = new TourView(this.getClass().getResource("tourelle.png").toExternalForm(), tour1);
		TourView mView2 = new TourView(this.getClass().getResource("tourelle.png").toExternalForm(), tour2);
		partie.timeToSetSbirePath();
		mGrid.add(mView, mView.getColumn(), mView.getRow());
		GridPane.setHalignment(mView, HPos.CENTER);
		GridPane.setValignment(mView, VPos.CENTER);

		GridPane.setHalignment(mView2, HPos.CENTER);
		GridPane.setValignment(mView2, VPos.CENTER);

		mGrid.add(mView2, mView2.getColumn(), mView2.getRow());
		for (Sbire sbire : partie.mSbires) {
			SbireView sb = new SbireView(getClass().getResource("minion.png").toExternalForm(), sbire, 20, 20);
			sbire.setOnSbireDestroy(sb);
			root.getChildren().add(sb);
			mSbires.add(sb);
		}

		mGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent ev) {
				// TODO Auto-generated method stub
				// int posX = (int) (ev.getSceneX()/TILE_SIZE_X.get());
				// int posY = (int) (ev.getSceneY()/TILE_SIZE_Y.get());
				if (ev.getButton() == MouseButton.SECONDARY) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							for (SbireView sbire : mSbires) {
								sbire.initPathAnimation();
								sbire.play();
								try {
									Thread.sleep(1200);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

					}).start();
				} else {
					// partie.mSbires.get(0).moveTo(posY, posX);
					// System.out.println("COUNT: "+partie.count());
					stage1= new Stage(StageStyle.TRANSPARENT);
					TourMenu tourMenu = new TourMenu();
					Scene scene = new Scene(tourMenu , TourMenu.WIDTH,TourMenu.HEIGHT);
					scene.setFill(null);
					stage1.setScene(scene);
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

		public TourMenu() {

			tourMenu = new TilePane();
			tourMenu.setPrefRows(3);
			tourMenu.setPrefColumns(4);

			for (int i = 0; i < 12; i++) {
				SelectTourMenuItem menuItem = new SelectTourMenuItem(
						new Image(getClass().getResource("tourelle.png").toExternalForm()), "Simple Tourelle", 100, 50, 9,
						500l);
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
			/*contour.getPoints().addAll(width/2,height,width/2-hauteurContour,height-hauteurContour,
					padding,height-hauteurContour,padding,padding,width,padding,width,height-hauteurContour,
					width/2+hauteurContour,height-hauteurContour);*/
			contour.getPoints().addAll(width/2,height,width/2-hauteurContour,height-hauteurContour,
					width/2+hauteurContour,height-hauteurContour);
			contour.setStroke(Color.LIGHTCYAN);
			contour.setFill(Color.LIGHTGRAY);
			this.getChildren().addAll(contour,mScroll);
		}

	}
}
