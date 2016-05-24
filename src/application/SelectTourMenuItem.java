package application;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SelectTourMenuItem extends StackPane{
	private static Image damageImage;
	private static Image prixImage;
	private static Image porteeImage;
	private static Image speedImage;
	private static Image backgroundImage;
	
	private ImageView tourImage;
	private ImageView backgroundIm;
	private ImageView damageIm;
	private ImageView prixIm;
	private ImageView porteeIm;
	private ImageView speedIm;
	private Label damages;
	private Label prix;
	private Label portee;
	private Label name;
	private Label speed;
	
	private Rectangle botRec;
	private Rectangle bgRec;
	private final Color textFill = Color.WHITE;
	public  static final int  DIM =115; 
	public SelectTourMenuItem(Image tourImage, String name ,int damages , int prix , int portee ,long speed){
		this.setPrefWidth(DIM);
		this.setPrefHeight(DIM);
		
		this.tourImage = new ImageView(tourImage);
		this.tourImage.setPreserveRatio(true);
		this.tourImage.setSmooth(true);
		this.tourImage.setCache(true);
		this.tourImage.setFitWidth(40);
		this.tourImage.setFitHeight(40);

		backgroundIm = new ImageView(backgroundImage);
		damageIm = new ImageView(damageImage);
		prixIm = new ImageView(prixImage);
		porteeIm = new ImageView(porteeImage);
		speedIm = new ImageView(speedImage);
		
		backgroundIm.setFitWidth(DIM-2);
		backgroundIm.setFitHeight(DIM-2);
		
		this.prix = new Label();
		initLabel(this.prix,prixIm,textFill,""+prix,ContentDisplay.RIGHT,20);
		
		this.portee=new Label();
		initLabel(this.portee,porteeIm,textFill,""+portee,ContentDisplay.LEFT,10);
		this.portee.setFont(new Font(9));
		
		this.damages=new Label();
		initLabel(this.damages,damageIm,textFill,""+damages,ContentDisplay.LEFT,10);
		this.damages.setFont(new Font(9));
		
		this.name= new Label(name);
		this.name.setTextAlignment(TextAlignment.CENTER);
		
		this.speed= new Label();
		initLabel(this.speed,speedIm,textFill,""+speed,ContentDisplay.LEFT,10);
		this.speed.setFont(new Font(9));
		
		bgRec = new Rectangle();
		bgRec.setOpacity(0.0);
		botRec = new Rectangle();
		botRec.setFill(Color.LIGHTGRAY);
		StackPane bottom = new StackPane();
		bottom.setPadding(new Insets(5));
		bottom.prefWidthProperty().bind(this.widthProperty());
		bottom.prefHeightProperty().bind(this.heightProperty().divide(5));
		botRec.setArcWidth(7);
		botRec.setArcHeight(7);
		botRec.setOpacity(0.7);
		botRec.widthProperty().bind(this.widthProperty().subtract(7));
		botRec.heightProperty().bind(this.heightProperty().divide(5));

		bottom.getChildren().addAll(botRec,this.prix);
		BorderPane border = new BorderPane();
		
		VBox vbRight= new VBox(5);
		vbRight.getChildren().addAll(this.portee,this.damages);
		vbRight.setAlignment(Pos.BOTTOM_LEFT);
		//vbRight.setPrefWidth(35);
		vbRight.prefWidthProperty().bind(this.widthProperty().divide(3));;
		VBox vbLeft = new VBox(5);
		vbLeft.getChildren().add(this.speed);
		vbLeft.setAlignment(Pos.BOTTOM_CENTER);
		vbLeft.prefWidthProperty().bind(this.widthProperty().divide(3));
		//vbLeft.setPrefWidth(35);
		
		HBox hb = new HBox(5);
		hb.setAlignment(Pos.CENTER);
		hb.setPadding(new Insets(7));
		hb.getChildren().add(this.name);
		
		border.setTop(hb);
		border.setBottom(bottom);
		border.setLeft(vbLeft);
		border.setRight(vbRight);
		border.setCenter(this.tourImage);
		border.prefWidthProperty().bind(backgroundIm.fitWidthProperty());
		border.prefHeightProperty().bind(backgroundIm.fitHeightProperty());

		bgRec.setWidth(DIM);
        bgRec.setHeight(DIM);
        bgRec.setArcHeight(10.0);
        bgRec.setArcWidth(10.0);
        bgRec.setOpacity(0.0);
		
		this.setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
                bgRec.setFill(Color.BLUE);
                bgRec.setOpacity(0.5);
			}
			
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
                bgRec.setOpacity(0.0);
			}
			
		});
		this.getChildren().addAll(bgRec,backgroundIm,border);
	}
	
	private void initLabel(Label label,ImageView image,Color textColor,String text,ContentDisplay cd, int width){
		label.setText(text);
		image.setPreserveRatio(true);
		image.setCache(true);
		image.setSmooth(true);
		image.setFitWidth(width);
		image.setFitHeight(width);
		label.setGraphic(image);
		label.setTextFill(textColor);
		label.setContentDisplay(cd);
	}
	
	public static Image getDamageImage() {
		return damageImage;
	}
	public static void setDamageImage(Image damageImage) {
		SelectTourMenuItem.damageImage = damageImage;
	}
	public static Image getPrixImage() {
		return prixImage;
	}
	public static void setPrixImage(Image prixImage) {
		SelectTourMenuItem.prixImage = prixImage;
	}
	public static Image getPorteeImage() {
		return porteeImage;
	}
	public static void setPorteeImage(Image porteeImage) {
		SelectTourMenuItem.porteeImage = porteeImage;
	}
	public static Image getSpeedImage() {
		return speedImage;
	}
	public static void setSpeedImage(Image speedImage) {
		SelectTourMenuItem.speedImage = speedImage;
	}
	public static void setBackgroundImage(Image backgroundImage){
		SelectTourMenuItem.backgroundImage=backgroundImage;
	}
}
