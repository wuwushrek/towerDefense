package application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ALauncher extends Application {
	
	public static final Map<String,Integer[]> infosTour = new HashMap<String,Integer[]>();
	public static final Map<String,Image> infosImage = new HashMap<String,Image>();
	
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 700;
	
	Scene scene;
	private Group mainPage = new Group();
	static HBox menuGroup;
	private static CreateGame game;
	
	
	@Override
	public void start(Stage primaryStage) {
		
		/*Pre enregistrement des capacites des tourelles et des images associees aux tourelles*/
		infosTour.put("tour_archer", new Integer[]{9,35,400,1100});
		infosImage.put("tour_archer",new Image(getClass().getResource("archer2.png").toExternalForm()));
		infosTour.put("tour_canonportee", new Integer[]{9,35,400,1100});
		infosImage.put("tour_canonportee",new Image(getClass().getResource("portee_longue_faible.png").toExternalForm()));
		infosTour.put("tour_canonporteepuiss", new Integer[]{9,45,900,1200});
		infosImage.put("tour_canonporteepuiss",new Image(getClass().getResource("portee_longue_puissant.png").toExternalForm()));
		infosTour.put("canon_simple", new Integer[]{9,30,300,1100});
		infosImage.put("canon_simple",new Image(getClass().getResource("canon_simple.png").toExternalForm()));
		infosTour.put("canon_sup", new Integer[]{9,35,1100,1000});
		infosImage.put("canon_sup",new Image(getClass().getResource("canon_sup.png").toExternalForm()));
		infosTour.put("canon_renforce", new Integer[]{9,55,1500,1300});
		infosImage.put("canon_renforce",new Image(getClass().getResource("canon_renforce.png").toExternalForm()));
		infosTour.put("flechette_mortier", new Integer[]{13,30,1300,1100});
		infosImage.put("flechette_mortier",new Image(getClass().getResource("flechette_mortier.png").toExternalForm()));
		infosTour.put("laser", new Integer[]{13,25,1200,1000});
		infosImage.put("laser",new Image(getClass().getResource("tourelle_laser.png").toExternalForm()));
		infosTour.put("mortier", new Integer[]{16,55,1800,1400});
		infosImage.put("mortier",new Image(getClass().getResource("mortier_tower.png").toExternalForm()));
		infosTour.put("mortier_gold", new Integer[]{16,60,2000,1200});
		infosImage.put("mortier_gold",new Image(getClass().getResource("mortier_gold.png").toExternalForm()));
		infosTour.put("tonnerre", new Integer[]{9,25,1000,900});
		infosImage.put("tonnerre",new Image(getClass().getResource("tonnerre_tour.png").toExternalForm()));
		infosTour.put("tonnerre_plus", new Integer[]{9,35,1500,900});
		infosImage.put("tonnerre_plus",new Image(getClass().getResource("tonnerre_tour2.png").toExternalForm()));
		infosTour.put("tonnerre_plus_gold", new Integer[]{13,45,2000,900});
		infosImage.put("tonnerre_plus_gold",new Image(getClass().getResource("portee_powerful_tonnerre.png").toExternalForm()));
		infosTour.put("triple_tonnerre", new Integer[]{13,25,3000,900});//15 par foudre
		infosImage.put("triple_tonnerre",new Image(getClass().getResource("powerful_tonnerre.png").toExternalForm()));
		
		/*IMportation des Images concernant l'environnement et les menus*/
		infosImage.put("items_back", new Image(getClass().getResource("items_back.png").toExternalForm()));
		infosImage.put("sword", new Image(getClass().getResource("sword.png").toExternalForm()));
		infosImage.put("coin-euro", new Image(getClass().getResource("coin-euro.png").toExternalForm()));
		infosImage.put("portee_im", new Image(getClass().getResource("portee_im.png").toExternalForm()));
		infosImage.put("bullet_speed", new Image(getClass().getResource("bullet_speed.png").toExternalForm()));
		infosImage.put("boule_bleu",  new Image(getClass().getResource("boule_bleu.png").toExternalForm()));
		infosImage.put("minion", new Image(getClass().getResource("minion.png").toExternalForm()));
		infosImage.put("boule_feu", new Image(getClass().getResource("boule_feu.png").toExternalForm()));
		infosImage.put("flechette", new Image(getClass().getResource("flechette.png").toExternalForm()));
		infosImage.put("backgroundTest", new Image(getClass().getResource("snowback.jpg").toExternalForm()));/*A retirer */
		infosImage.put("troll", new Image(getClass().getResource("troll.png").toExternalForm()));
		infosImage.put("boss", new Image(getClass().getResource("boss.png").toExternalForm()));
		infosImage.put("depart", new Image(getClass().getResource("depart.png").toExternalForm()));
		infosImage.put("arrivee", new Image(getClass().getResource("arrivee.png").toExternalForm()));
		infosImage.put("NO ITEM",  new Image(getClass().getResource("no_item.png").toExternalForm()));
		infosImage.put("GRASS MAP",  new Image(getClass().getResource("grass_template.jpg").toExternalForm()));
		infosImage.put("SNOW MAP",new Image(getClass().getResource("snowback.jpg").toExternalForm()));
		infosImage.put("SAND MAP",  new Image(getClass().getResource("sand_map.jpg").toExternalForm()));
		infosImage.put("explosion", new Image(getClass().getResource("explosion.png").toExternalForm()));
		
		/*Creation de la scene*/
		scene = new Scene(mainPage, WIDTH, HEIGHT, null);
		primaryStage.setMinHeight(450);

		//Ajout du style de tous les composants
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()) ;
		menuGroup = createMainMenu();
		mainPage.getChildren().add(menuGroup);
		
		primaryStage.setTitle("Tower Defense");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	
	public HBox createMainMenu(){
		
		HBox background = new HBox();
		background.setFillHeight(false);
		background.setId("mainMenuBackground");
		background.setAlignment(Pos.CENTER);
		background.prefWidthProperty().bind(scene.widthProperty());
		background.prefHeightProperty().bind(scene.heightProperty());
		
		TilePane pane = new TilePane();
		pane.setPrefColumns(3);
		pane.setPrefRows(2);
		pane.setHgap(25);
		pane.setVgap(25);
		
		ImageView sandBackground = new ImageView(infosImage.get("SAND MAP"));
		ImageView snowBackground = new ImageView(infosImage.get("SNOW MAP"));
		ImageView grassBackground = new ImageView(infosImage.get("GRASS MAP"));
		initMainMenuImage(pane,new String[]{"SAND MAP","SNOW MAP","GRASS MAP"},new ImageView[]{sandBackground,snowBackground,grassBackground});
		
		background.getChildren().add(pane);
		return background;
	}
	
	private void initMainMenuImage(TilePane mPane ,String[] name,ImageView[] mImage){
		for(int i=0;i<mImage.length;i++){
			final String id = name[i];
			VBox vBox = new VBox(5);
			vBox.setId("mainMenuImage");
			mImage[i].setSmooth(true);
			mImage[i].setCache(true);
			//mImage[i].setFitHeight(140);
			//mImage[i].setFitWidth(200);
			mImage[i].fitHeightProperty().bind(scene.heightProperty().divide(4.2));
			mImage[i].fitWidthProperty().bind(scene.widthProperty().divide(4));
			Label label = new Label(name[i]);
			label.setId("labelTopMenu");
			vBox.getChildren().addAll(mImage[i],label);
			vBox.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					onItemClick(id);
				}
			});
			mPane.getChildren().add(vBox);
		}
		int itemCount =mPane.getPrefColumns()*mPane.getPrefRows()-mImage.length;
		for(int i=0;i< itemCount ; i++){
			ImageView noItem = new ImageView(infosImage.get("NO ITEM"));
			noItem.setCache(true);
			noItem.setSmooth(true);
			noItem.fitHeightProperty().bind(scene.heightProperty().divide(4.2));
			noItem.fitWidthProperty().bind(scene.widthProperty().divide(4));
			//noItem.setId("mainMenuImage");
			Label label = new Label("NO ITEM");
			label.setId("labelTopMenu");
			VBox vbox = new VBox(5);
			vbox.setId("mainMenuImage");
			vbox.getChildren().addAll(noItem,label);
			mPane.getChildren().add(vbox);
		}
	}
	
	public void onItemClick(String id){
		menuGroup.setDisable(true);
		if(id.equals("SAND MAP")){
			int[] startCoord = {1,0};
			int[] endCoord = {6,7};
			int rowCount = 8;
			int columnCount =8;
			int[][] walls = new int[columnCount*2][2];
			for(int i=0;i<columnCount; i++){
				walls[i][0]=0;
				walls[i][1]=i;
				walls[i+columnCount][0]=rowCount-1;
				walls[i+ columnCount][1]=i;
			}
			game = new CreateGame(id,walls,rowCount,columnCount,startCoord,endCoord,scene);
		}else if(id.equals("SNOW MAP")) {
			int[] startCoord = {8,0};
			int[] endCoord = {1,9};
			int rowCount = 10;
			int columnCount =10;
			int[][] walls = new int[columnCount*2+2][2];
			for(int i=0;i<columnCount; i++){
				walls[i][0]=0;
				walls[i][1]=i;
				walls[i+columnCount][0]=rowCount-1;
				walls[i+ columnCount][1]=i;
			}
			walls[2*columnCount][0]=1;
			walls[2*columnCount][1]=8;
			walls[2*columnCount+1][0]=2;
			walls[2*columnCount+1][1]=8;
			game = new CreateGame(id,walls,rowCount,columnCount,startCoord,endCoord,scene);
		}else {
			int[] startCoord = {4,0};
			int[] endCoord = {4,9};
			int rowCount = 10;
			int columnCount =10;
			int[][] walls = new int[columnCount*2][2];
			for(int i=0;i<columnCount; i++){
				walls[i][0]=0;
				walls[i][1]=i;
				walls[i+columnCount][0]=rowCount-1;
				walls[i+ columnCount][1]=i;
			}
			game = new CreateGame(id,walls,rowCount,columnCount,startCoord,endCoord,scene);
		}
		gameStartAnimation(scene,menuGroup,game);
	}
	
	public static void gameStartAnimation(Scene scene ,Node currentDisplay, Node nextDisplay){
		Group parent = ((Group) scene.getRoot());
		parent.getChildren().add(nextDisplay);
		double width = scene.widthProperty().get();
		KeyFrame start = new KeyFrame(Duration.ZERO,
                 new KeyValue(nextDisplay.translateXProperty(), width),
                 new KeyValue(currentDisplay.translateXProperty(), 0));
        KeyFrame end = new KeyFrame(Duration.millis(1500),
                 new KeyValue(nextDisplay.translateXProperty(), 0),
                 new KeyValue(currentDisplay.translateXProperty(), -width));
         Timeline slide = new Timeline(start, end);
         slide.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				if (nextDisplay instanceof CreateGame){
					((CreateGame) nextDisplay).startCounter();
				}else{
					((CreateGame) currentDisplay).closeGame();
				}
				currentDisplay.setDisable(false);
				parent.getChildren().remove(currentDisplay);
			}
        	 
         });
         slide.play();
	}
	
	public  static IntegerProperty getTileSizeX(){
		return game.getTileSizeX();
	}
	
	public static  IntegerProperty getTileSizeY(){
		return game.getTileSizeY();
	}
	
	public static void addAll(List<Group> groups){
		game.getChildren().addAll(groups);
	}
	
	public static void addNode(Node groups){
		game.getChildren().addAll(groups);
	}
	
	public static void removeNode(Node node){
		game.getChildren().remove(node);
	}
}
