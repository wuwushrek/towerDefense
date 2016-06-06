package application;

import java.util.ArrayList;
import java.util.List;

import modele.GameFactory;
import modele.PartieInterface;
import modele.SbireInterface;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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

public class CreateGame extends Group {

	private int ROW_COUNT = 8;
	private int COLUMN_COUNT = 8;
	private long BASIC_LAUNCH_TIME = 1200;

	public static final IntegerProperty TILE_SIZE_X = new SimpleIntegerProperty();
	public static final IntegerProperty TILE_SIZE_Y = new SimpleIntegerProperty();

	List<SbireView> mSbires = new ArrayList<SbireView>();
	List<TourView> mTours = new ArrayList<TourView>();
	private int speedRate = 1;
	private PartieInterface partie;// = GameFactory.createPartie(ROW_COUNT,
									// COLUMN_COUNT, startCoord, endCoord);

	private final Stage stage1 = new Stage(StageStyle.TRANSPARENT);
	private final GridPane mGrid = new GridPane();
	private TourMenu mTourMenu;
	private Circle rayon;
	private VBox deleteTour;
	private TourListener mTourListener = new TourListener();
	private SbireLauncher mSbireLauncher;
	private Label suppressionAnim = new Label();// Animation lors de la
												// suppression tour

	HBox levelDone = new HBox();
	HBox gameOver = new HBox();
	private Button nextLevel;
	private Button playButton;
	private Button pauseButton;
	private Button animRate;
	private Button menuView;
	private Button mainMenu;

	private FadeTransition startPointAnim;

	public CreateGame(String mapId, int[][] walls, int rowCount,
			int columnCount, int[] startCoord, int[] endCoord, Scene mScene) {

		// IMage pour le menu de selection
		SelectTourMenuItem.setBackgroundImage(ALauncher.infosImage
				.get("items_back"));
		SelectTourMenuItem.setDamageImage(ALauncher.infosImage.get("sword"));
		SelectTourMenuItem.setPrixImage(ALauncher.infosImage.get("coin-euro"));
		SelectTourMenuItem.setPorteeImage(ALauncher.infosImage.get("portee_im"));
		SelectTourMenuItem
				.setSpeedImage(ALauncher.infosImage.get("bullet_speed"));

		// Initialisation de la carte et creation de la partie
		ROW_COUNT = rowCount;
		COLUMN_COUNT = columnCount;
		// this.startCoord = startCoord;
		// this.endCoord = endCoord;
		partie = GameFactory.createPartie(ROW_COUNT, COLUMN_COUNT, startCoord,
				endCoord);
		partie.setWalls(walls);

		// Taille des carreaux
		TILE_SIZE_X.bind(mScene.widthProperty().divide(COLUMN_COUNT));
		TILE_SIZE_Y.bind(mScene.heightProperty().divide(ROW_COUNT));

		// Menu tours
		mTourMenu = new TourMenu();
		Scene sceneMenu = new Scene(mTourMenu, TourMenu.WIDTH, TourMenu.HEIGHT);
		sceneMenu.setFill(null);
		stage1.setScene(sceneMenu);
		// fin Menu tours

		mGrid.setGridLinesVisible(true);
		mGrid.prefWidthProperty().bind(mScene.widthProperty());
		mGrid.prefHeightProperty().bind(mScene.heightProperty());
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
		// Image de fond de la map
		ImageView background = new ImageView(ALauncher.infosImage.get(mapId));
		background.fitHeightProperty().bind(mScene.heightProperty());
		background.fitWidthProperty().bind(mScene.widthProperty());

		// Marquage des points de depart et d'arrivees
		StackPane depart = new StackPane();
		depart.prefWidthProperty().bind(TILE_SIZE_X);
		depart.prefHeightProperty().bind(TILE_SIZE_Y);
		depart.setId("depart");
		mGrid.add(depart, startCoord[1], startCoord[0]);
		GridPane.setHalignment(depart, HPos.CENTER);
		GridPane.setValignment(depart, VPos.CENTER);
		startPointAnim = new FadeTransition(Duration.millis(1500), depart);
		startPointAnim.setFromValue(0.5);
		startPointAnim.setToValue(0.0);
		startPointAnim.setCycleCount(Animation.INDEFINITE);
		startPointAnim.setAutoReverse(true);

		// Marquage point arrivee
		ImageView arrivee = new ImageView(ALauncher.infosImage.get("arrivee"));
		arrivee.setSmooth(true);
		arrivee.setCache(true);
		arrivee.fitHeightProperty().bind(TILE_SIZE_Y);
		arrivee.fitWidthProperty().bind(TILE_SIZE_X);
		mGrid.add(arrivee, endCoord[1], endCoord[0]);
		GridPane.setHalignment(arrivee, HPos.CENTER);
		GridPane.setValignment(arrivee, VPos.CENTER);

		// Mise en place du menu Top
		HBox topMenu = new HBox(25);
		topMenu.prefWidthProperty().bind(mScene.widthProperty());
		topMenu.setPadding(new Insets(5));
		topMenu.setFillHeight(false);
		topMenu.setAlignment(Pos.CENTER);

		HBox ressourceBox = new HBox(25);
		ressourceBox.setId("hboxLabels");
		Label ressourceLabel = new Label("MONEY");
		ressourceLabel.setId("labelTopMenu");
		Label ressourceValue = new Label("" + partie.argentProperty().get());
		partie.argentProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ressourceValue.setText("" + newValue);
					}
				});
			}
		});
		ressourceValue.setId("moneyView");
		ressourceBox.getChildren().addAll(ressourceLabel, ressourceValue);

		HBox livesBox = new HBox(25);
		livesBox.setId("hboxLabels");
		Label livesLabel = new Label("LIVES");
		livesLabel.setId("labelTopMenu");
		Label livesValue = new Label("" + partie.pointVieProperty().get());
		partie.pointVieProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						livesValue.setText("" + newValue);
					}
				});
			}
		});
		livesValue.setId("livesView");
		livesBox.getChildren().addAll(livesLabel, livesValue);

		HBox score = new HBox(30);
		score.setId("hboxLabels");
		Label scoreLabel = new Label("SCORE");
		scoreLabel.setId("labelTopMenu");
		Label scoreValue = new Label("" + partie.scoreProperty().get());
		partie.scoreProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						scoreValue.setText("" + newValue);
					}
				});
			}
		});
		scoreValue.setId("scoreView");
		score.getChildren().addAll(scoreLabel, scoreValue);

		HBox chronometerBox = new HBox(25);
		chronometerBox.setId("hboxLabels");
		Label chronometerLabel = new Label("TIME");
		chronometerLabel.setId("labelTopMenu");
		Label chronometerValue = new Label();
		chronometerValue.textProperty().bind(time);
		chronometerValue.setId("chronoView");
		chronometerBox.getChildren().addAll(chronometerLabel, chronometerValue);
		topMenu.getChildren().addAll(ressourceBox, livesBox, score,
				chronometerBox);

		// Mise en Place du bottom Menu comportant les boutons play , pause ,
		// speed
		HBox leftBottomMenu = new HBox(5);
		leftBottomMenu.prefWidthProperty().bind(mScene.widthProperty());
		leftBottomMenu.layoutYProperty().bind(
				mScene.heightProperty().subtract(
						leftBottomMenu.heightProperty()));
		leftBottomMenu.setPadding(new Insets(0, 0, 5, 0));
		leftBottomMenu.setAlignment(Pos.CENTER);
		leftBottomMenu.setFillHeight(false);

		playButton = new Button();
		playButton.setId("play_button");

		pauseButton = new Button();
		pauseButton.setId("pause_button");
		pauseButton.setDisable(true);

		menuView = new Button();
		menuView.setId("menuButton");
		menuView.setOnAction(e-> ALauncher.gameStartAnimation(mScene, this, ALauncher.menuGroup));

		animRate = new Button("x1");
		animRate.setId("animRateView");
		animRate.setDisable(true);

		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playButton.setDisable(true);
				menuView.setDisable(true);
				animRate.setDisable(true);
				playGame();
				menuView.setDisable(false);
				animRate.setDisable(false);
			}
		});
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playButton.setDisable(true);
				pauseButton.setDisable(true);
				menuView.setDisable(true);
				animRate.setDisable(true);
				pauseGame();
				menuView.setDisable(false);
				animRate.setDisable(false);
			}
		});
		animRate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				pauseButton.setDisable(true);
				menuView.setDisable(true);
				changeGameSpeed();
				pauseButton.setDisable(false);
				menuView.setDisable(false);
			}

		});

		HBox level = new HBox(10);
		level.setId("hboxLabels");
		Label levelLabel = new Label("LEVEL");
		levelLabel.setId("labelTopMenu");
		Label levelValue = new Label("1" + "/"
				+ partie.getNumberOfLevel());
		partie.levelProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						levelValue.setText("" + (newValue.intValue()+1) + "/"
								+ partie.getNumberOfLevel());
					}
				});
			}
		});
		levelValue.setId("levelView");
		level.getChildren().addAll(levelLabel, levelValue);

		HBox wave = new HBox(10);
		wave.setId("hboxLabels");
		Label waveLabel = new Label("SBIRE");
		waveLabel.setId("labelTopMenu");
		Label waveValue = new Label("" + partie.sbireTueeProperty().get() + "/"
				+ partie.getCurrentSbireNumber());
		partie.sbireTueeProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						waveValue.setText("" + newValue + "/"
								+ partie.getCurrentSbireNumber());
					}
				});
			}
		});
		waveValue.setId("waveView");
		wave.getChildren().addAll(waveLabel, waveValue);

		leftBottomMenu.getChildren().addAll(menuView, animRate, level, wave,
				playButton, pauseButton);

		// POrtee des tours lors de la phase de selection
		rayon = new Circle();
		rayon.setFill(Color.LIGHTGRAY);
		rayon.setOpacity(0.7);
		rayon.setStrokeWidth(3);
		rayon.setStroke(Color.CYAN);
		rayon.setVisible(false);
		rayon.setId("portee_tour");

		// VUe de suppression d'une tour
		deleteTour = new VBox();
		deleteTour.prefWidthProperty().bind(TILE_SIZE_X);
		Label deleteButton = new Label("DELETE");
		deleteButton.setId("delete_button");
		deleteTour.setAlignment(Pos.CENTER);
		deleteTour.setId("hboxLabels");
		deleteTour.getChildren().add(deleteButton);

		// Style label de suppression
		suppressionAnim.setId("moneyView");
		suppressionAnim.setVisible(false);

		// Level Done view
		levelDone.prefWidthProperty().bind(mScene.widthProperty());
		levelDone.prefHeightProperty().bind(mScene.heightProperty());
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
		counter.getChildren().addAll(center, counterLabel);
		nextLevel = new Button("GO ! Next Level");
		nextLevel.setDisable(true);
		nextLevel.setId("mainMenu");
		nextLevel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				timeNextLevel.set("" + nextLevelWait);
				partie.nextLevelInitialisation();
				getChildren().remove(levelDone);
				chronometer.play();
			}

		});
		mainVbox.getChildren().addAll(labelLevel, counter, nextLevel);
		partie.levelDoneProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean old, Boolean newValue) {
				if (newValue == true) {
					for (SbireView sbireView : mSbires) {
						sbireView.stop();
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							nextLevel.setDisable(true);
							playButton.setDisable(false);
							pauseButton.setDisable(true);
							animRate.setDisable(true);
							while(speedRate!=1){
								changeGameSpeed();
							}
							getChildren().add(levelDone);
							getChildren().removeAll(mSbires);
							mSbires.clear();
							for(TourView tour : mTours){
								tour.setRotate(0.0);
							}
						}

					});
					FadeTransition showSlow = new FadeTransition();
					showSlow.setDuration(Duration.millis(500));
					showSlow.setByValue(0.0);
					showSlow.setToValue(0.8);
					showSlow.setAutoReverse(true);
					showSlow.setNode(levelDone);
					showSlow.setOnFinished(new EventHandler<ActionEvent>() {
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

		// Game Over view
		gameOver.prefWidthProperty().bind(mScene.widthProperty());
		gameOver.prefHeightProperty().bind(mScene.heightProperty());
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
		mainMenu = new Button("Menu");
		mainMenu.setId("mainMenu");
		mainMenu.setOnAction(e->ALauncher.gameStartAnimation(mScene, this, ALauncher.menuGroup));
		Button exitButton = new Button("Quit");
		exitButton.setId("mainMenu");
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mScene.getWindow().hide();
				// primaryStage.close();
			}

		});
		partie.aliveProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean old, Boolean newValue) {
				if (newValue == false) {
					for (SbireView sbireView : mSbires) {
						sbireView.stop();
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							getChildren().add(gameOver);
							getChildren().removeAll(mSbires);
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
		buttonBox.getChildren().addAll(mainMenu, exitButton);
		mainOver.getChildren().addAll(overLabel, teteMort, buttonBox);
		gameOver.getChildren().add(mainOver);
		gameOver.setOpacity(0.0);

		// ouvre la fenetre de choix de tourelles lorsqu on est dans la phase de
		// choix
		mGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent ev) {
				if (stage1.isShowing()) {
					stage1.close();
					return;
				}
				if (ev.getButton() == MouseButton.PRIMARY
						&& !mTourMenu.isDisable()) {
					int xIndex = (int) (ev.getSceneX() / TILE_SIZE_X.get());
					int yIndex = (int) (ev.getSceneY() / TILE_SIZE_Y.get());
					if (partie.isPathPossible(yIndex, xIndex)) {
						final double currentCenterX = xIndex
								* TILE_SIZE_X.get() + TILE_SIZE_X.get() / 2;
						final double currentCenterY = yIndex
								* TILE_SIZE_Y.get() + TILE_SIZE_Y.get() / 2;
						mTourMenu.checkEnable(partie.argentProperty().get());
						mTourMenu.setCurrentX(currentCenterX);
						mTourMenu.setCurrentY(currentCenterY);
						stage1.setX(ev.getScreenX() - TourMenu.WIDTH / 2);
						stage1.setY(ev.getScreenY() - TourMenu.HEIGHT);
						stage1.show();
					}
				}
			}

		});

		// Enfin on rajoute tous les elements de la fenetre graphique
		getChildren().addAll(background, rayon, mGrid, topMenu, leftBottomMenu,
				suppressionAnim);

	}
	
	public Button getMainMenu(){
		return mainMenu;
	}
	
	public Button getMenuView(){
		return menuView;
	}

	private void pauseGame() {
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (mSbireLauncher != null && mSbireLauncher.isAlive()) {
					mSbireLauncher.suspend();
				}
			}

		}).start();
		for (SbireView sbire : mSbires) {
			sbire.pause();
		}
		for (TourView tour : mTours) {
			tour.pause();
		}

		pauseButton.setDisable(true);
		playButton.setDisable(false);
	}

	private void playGame() {
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (mSbireLauncher != null && mSbireLauncher.isAlive()) {
					mSbireLauncher.resume();
				}
			}
		}).start();
		if (compteur != selectionTimeInt) {
			compteur = 0;
			return;
		}
		for (TourView tour : mTours) {
			tour.play();
		}
		for (SbireView sbire : mSbires) {
			sbire.play();
		}
		pauseButton.setDisable(false);
		playButton.setDisable(true);
	}

	private void changeGameSpeed() {
		if (animRate.getText().equals("x1")) {
			animRate.setText("x2");
			speedRate = 2;
		} else if (animRate.getText().equals("x2")) {
			animRate.setText("x3");
			speedRate = 3;
		} else if (animRate.getText().equals("x3")) {
			animRate.setText("x1");
			speedRate = 1;
		}
		partie.setRateFactor(speedRate);
		TourView.setRate(speedRate);
		for (SbireView sbire : mSbires) {
			sbire.setRate(speedRate);
		}
	}

	public void startCounter() {
		chronometerNextLevel.setAutoReverse(true);
		chronometerNextLevel.setCycleCount(Animation.INDEFINITE);
		chronometer.setAutoReverse(true);
		chronometer.setCycleCount(Animation.INDEFINITE);
		chronometer.play();
		startPointAnim.play();
	}

	public void closeGame() {
		chronometer.stop();
		startPointAnim.stop();
		chronometerNextLevel.stop();
		if(mSbireLauncher!=null){
			try {
				mSbireLauncher.interrupt();
			} catch (Exception e) {
				System.out.println("Interruption lancement");
			}
		}
		
		for (SbireView sbire : mSbires) {
			sbire.stop();
		}
		for (TourView tour : mTours) {
			tour.pause();
		}
		// A rajouter code
	}

	public IntegerProperty getTileSizeX() {
		return TILE_SIZE_X;
	}

	public IntegerProperty getTileSizeY() {
		return TILE_SIZE_Y;
	}

	/*
	 * Attributs ou l'on s'occupe du chronometrage lors de la phase d'achat
	 */
	private final String selectionTime = "02:00";
	private final StringProperty time = new SimpleStringProperty(selectionTime);
	private final int selectionTimeInt = 2 * 60;
	private int compteur = selectionTimeInt;
	private final Timeline chronometer = new Timeline(new KeyFrame(
			Duration.millis(1000), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					compteur--;
					int min = compteur / 60;
					int sec = compteur % 60;
					time.set("0" + min + ":" + (sec < 10 ? "0" + sec : sec));
					if (compteur <= 0) {
						partie.initSbiresOnLevel();
						for (SbireInterface sbire : partie.getSbireList()) {
							SbireView sb;
							if (sbire.getNom().equals("minionSbire")) {
								sb = new SbireView(
										ALauncher.infosImage.get("minion"),
										sbire, 40, 40);
							} else if (sbire.getNom().equals("troll")) {
								sb = new SbireView(
										ALauncher.infosImage.get("troll"), sbire,
										40, 40);
							} else {
								sb = new SbireView(
										ALauncher.infosImage.get("boss"), sbire,
										40, 40);
							}
							sbire.setOnSbireDestroy(sb);
							getChildren().add(sb);
							mSbires.add(sb);
						}
						mSbireLauncher = new SbireLauncher();
						mSbireLauncher.start();
						stage1.close();
						mTourMenu.setDisable(true);
						compteur = selectionTimeInt;
						time.set(selectionTime);
						pauseButton.setDisable(false);
						playButton.setDisable(true);
						animRate.setDisable(false);
						chronometer.stop();

					}
				}
			}));

	/*
	 * Arret quelques secondes apres que le level soit finie pour tout
	 * reinitialiser
	 */
	private final int nextLevelWait = 3;
	private int compteurNextLevel = nextLevelWait;
	private final StringProperty timeNextLevel = new SimpleStringProperty(""
			+ nextLevelWait);
	private final Timeline chronometerNextLevel = new Timeline(new KeyFrame(
			Duration.millis(1000), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					compteurNextLevel--;
					timeNextLevel.set("" + compteurNextLevel);
					if (compteurNextLevel == 0) {
						compteurNextLevel = nextLevelWait;
						nextLevel.setDisable(false);
						mTourMenu.setDisable(false);
						chronometerNextLevel.stop();
					}
				}
			}));

	/**
	 * Classe permettant d'afficher le menu de selection des tourelles
	 */
	class TourMenu extends Group {
		private final static int WIDTH = 505;
		private final static int HEIGHT = 270;

		double padding = 10;
		double hauteurContour = 25;

		private ScrollPane mScroll;
		private TilePane tourMenu;
		private Polygon contour;

		private double currentX = 0;
		private double currentY = 0;

		public TourMenu() {
			tourMenu = new TilePane();
			tourMenu.setPrefColumns(4);

			for (String name : ALauncher.infosTour.keySet()) {
				SelectTourMenuItem menuItem = new SelectTourMenuItem(
						ALauncher.infosImage.get(name), name,
						ALauncher.infosTour.get(name)[1],
						ALauncher.infosTour.get(name)[2],
						ALauncher.infosTour.get(name)[0],
						ALauncher.infosTour.get(name)[3]);
				menuItem.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent ev) {
						menuItem.mouseEntered();
						rayon.setVisible(true);
						rayon.setRadius(menuItem.getPortee()
								* Math.min(TILE_SIZE_X.get(), TILE_SIZE_Y.get()));
					}

				});
				menuItem.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						menuItem.mouseExited();
						rayon.setVisible(false);
					}
				});
				menuItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						TourView mView = createTour(name,
								(int) (currentY / TILE_SIZE_Y.get()),
								(int) (currentX / TILE_SIZE_X.get()));
						mTours.add(mView);
						// lancement des tours
						mGrid.add(mView, mView.getColumn(), mView.getRow());
						GridPane.setHalignment(mView, HPos.CENTER);
						GridPane.setValignment(mView, VPos.CENTER);
						rayon.setVisible(false);
						stage1.close();
						mView.addEventHandler(MouseEvent.ANY, mTourListener);
					}
				});
				tourMenu.getChildren().add(menuItem);
			}

			mScroll = new ScrollPane();
			mScroll.setContent(tourMenu);
			mScroll.setPrefWidth(WIDTH - 2 * padding);
			mScroll.setPrefHeight(HEIGHT - padding - hauteurContour);
			mScroll.setLayoutX(padding);
			mScroll.setLayoutY(padding);
			mScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
			mScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			mScroll.setOpacity(0.8);

			double width = WIDTH;
			double height = HEIGHT;
			contour = new Polygon();
			contour.getPoints().addAll(width / 2, height,
					width / 2 - hauteurContour, height - hauteurContour,
					width / 2 + hauteurContour, height - hauteurContour);
			contour.setStroke(Color.LIGHTCYAN);
			contour.setFill(Color.LIGHTGRAY);
			this.getChildren().addAll(contour, mScroll);
		}

		public void checkEnable(int moneyLeft) {
			for (Node node : tourMenu.getChildren()) {
				SelectTourMenuItem tourItem = (SelectTourMenuItem) node;
				if (tourItem.getPrix() > moneyLeft) {
					tourItem.setDisable(true);
				} else {
					tourItem.setDisable(false);
				}
			}
		}

		public void setCurrentX(double currentX) {
			this.currentX = currentX;
			rayon.setCenterX(currentX);
		}

		public void setCurrentY(double currentY) {
			this.currentY = currentY;
			rayon.setCenterY(currentY);
		}

		public TourView createTour(String name, int rowIndex, int columnIndex) {
			if (name.equals("tour_archer")) {
				return new TourView_Acher(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("tour_canonportee")) {
				return new TourView_CannonLPortee(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("tour_canonporteepuiss")) {
				return new TourView_CanonLPorteePuiss(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("canon_simple")) {
				return new TourView_CanonSimple(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("canon_sup")) {
				return new TourView_CanonSup(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("canon_renforce")) {
				return new TourView_CanonSupRenforce(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("flechette_mortier")) {
				return new TourView_FlechetteMortier(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("laser")) {
				return new TourView_Laser(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("mortier")) {
				return new TourView_Mortier(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("mortier_gold")) {
				return new TourView_MortierGold(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("tonnerre")) {
				return new TourView_Tonnerre(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("tonnerre_plus")) {
				return new TourView_TonnerrePlus(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("tonnerre_plus_gold")) {
				return new TourView_TonnerrePlusGold(rowIndex, columnIndex,
						partie.getTourSideInterface());
			} else if (name.equals("triple_tonnerre")) {
				return new TourView_TripleTonnere(rowIndex, columnIndex,
						partie.getTourSideInterface());
			}
			return null;
		}
	}

	/**
	 * Listener des evenements lies aux clicks ou plus sur les tourelles On
	 * pourrait eventuellement rajouter les infos sur la tourelle grace a cette
	 * classe Faute de temps
	 */
	class TourListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			TourView tourSource = (TourView) event.getSource();
			if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
				if (compteur != selectionTimeInt) {
					tourSource.getChildren().add(deleteTour);
				}
			} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
				tourSource.getChildren().remove(deleteTour);
			} else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
				if (compteur != selectionTimeInt) {
					suppressionAnim.setTranslateX(0);
					suppressionAnim.setTranslateY(0);
					event.consume();
					int xPosition = ((int) event.getSceneX() / TILE_SIZE_X
							.get()) * TILE_SIZE_X.get() + TILE_SIZE_X.get() / 2;
					int yPosition = ((int) event.getSceneY() / TILE_SIZE_Y
							.get()) * TILE_SIZE_Y.get() + TILE_SIZE_Y.get() / 2;
					tourSource.getTour().destroy();
					mGrid.getChildren().remove(tourSource);
					mTours.remove(tourSource);
					suppressionAnim.setVisible(true);
					suppressionAnim
							.setText("" + tourSource.getTour().getCost());
					suppressionAnim.setLayoutX(xPosition);
					suppressionAnim.setLayoutY(yPosition);
					TranslateTransition translate = new TranslateTransition();
					translate.setByY(-TILE_SIZE_Y.get());
					translate.setDuration(Duration.millis(300));
					translate.setNode(suppressionAnim);
					ScaleTransition scale = new ScaleTransition();
					scale.setDuration(Duration.millis(300));
					scale.setNode(suppressionAnim);
					scale.setFromX(1.0);
					scale.setFromY(1.0);
					scale.setToX(2.0);
					scale.setToY(2.0);
					translate.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
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

	/*
	 * Lancement des SBires dans l'arene
	 */
	class SbireLauncher extends Thread {
		@Override
		public void run() {
			for (SbireView sbire : mSbires) {
				try {
					Thread.sleep(BASIC_LAUNCH_TIME / speedRate);
				} catch (InterruptedException e) {
					System.out.println("INterruption");
					return;
				}
				sbire.firstPlay();
			}
		}
	}

}
