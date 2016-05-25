package modele;

import application.OnSbireMoveAndDestroy;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import pathfinder.Path;
import pathfinder.Path.Step;

class Sbire implements TourTarget , SbireInterface{
	
	private static Path mPath;
	private int currentPosInPath =1;
	private static int compteurId=0;
	private SbireSideInterface mPartie;
	
	private double initialPointDeVie;
	private IntegerProperty pointDeVie;
	private IntegerProperty rowIndex;
	private IntegerProperty columnIndex;
	private int damages;
	private int argentRapporte;
	private int scoreRapporte;
	private double vitesse;//between 0 and 1
	
	private OnSbireMoveAndDestroy sbireDestroy;
	
	private int id;
	
	public Sbire(SbireSideInterface partie , int pointVie ,int rowIndex, int columnIndex , int damages, int argentRapporte 
			, int scoreRapporte , double vitesse){
		id=compteurId;
		mPartie = partie;
		this.rowIndex= new SimpleIntegerProperty(rowIndex);
		this.columnIndex= new SimpleIntegerProperty(columnIndex);
		this.damages=damages;
		this.argentRapporte=argentRapporte;
		this.scoreRapporte=scoreRapporte;
		pointDeVie = new SimpleIntegerProperty(pointVie);
		initialPointDeVie=pointVie;
		this.vitesse=vitesse;
		compteurId++;
		System.out.println(toString());
	}
	
	public IntegerProperty pointDeVieProperty() {
		return pointDeVie;
	}

	@Override
	public synchronized boolean decrementPointDeVie(int pointVie) {
		if(pointDeVie.get()==0)
			return false;
		int newValue = this.pointDeVie.get()- pointVie;
		if(newValue>0){
			System.out.println(this.toString());
			this.pointDeVie.set(newValue);
			return true;
		}
		this.pointDeVie.set(0);
		destroy(true);
		return false;
	}
	
	public void setOnSbireDestroy(OnSbireMoveAndDestroy sbireDestroy){
		this.sbireDestroy=sbireDestroy;
	}
	
	public synchronized void destroy(boolean isDead){
		mPartie.remove(this, isDead);
		if(sbireDestroy!=null){
			sbireDestroy.onSbireDestroy();
		}
	}

	@Override
	public int getRowIndex() {
		return rowIndex.get();
	}
	
	public boolean moveTo(int destRowIndex , int destColumnIndex){
		//System.out.println("SBIRE : FROM "+rowIndex + ", "+columnIndex+ "  TO: "+destRowIndex +" ,"+destColumnIndex);
		int lastRowIndex = rowIndex.get();
		int lastColumnIndex = columnIndex.get();
		this.rowIndex.set(destRowIndex);
		this.columnIndex.set(destColumnIndex);
		return mPartie.moveFromTo(this, lastRowIndex, lastColumnIndex);
	}

	@Override
	public int getColumnIndex() {
		return columnIndex.get();
	}

	public IntegerProperty rowIndexProperty(){
		return rowIndex;
	}
	
	public IntegerProperty columnIndexProperty(){
		return columnIndex;
	}
	
	public int getDamages() {
		return damages;
	}

	public void setDamages(int damages) {
		this.damages = damages;
	}

	public int getArgentRapporte() {
		return argentRapporte;
	}

	public void setArgentRapporte(int argentRapporte) {
		this.argentRapporte = argentRapporte;
	}
	
	public int getScoreRapporte(){
		return scoreRapporte;
	}
	
	public int getId(){
		return id;
	}
	
	public double getInitialPointDeVie(){
		return initialPointDeVie;
	}
	
	public static void setPath(Path path){
		mPath = path;
	}
	
	public boolean moveNext(){
		Step currentStep = mPath.getStep(currentPosInPath);
		if(this.moveTo(currentStep.getX(), currentStep.getY())){
			currentPosInPath++;
			return true;
		}else{
			return false;
		}
	}
	
	public double getVitesse(){
		return vitesse;
	}
	
	public Path getPath(){
		return mPath;
	}
	@Override
	public String toString() {
		return "Sbire [ID= "+id+", pointDeVie=" + pointDeVie + ", rowIndex=" + rowIndex + ", columnIndex=" + columnIndex + ", damages=" + damages
				+ ", argentRapporte=" + argentRapporte + ", scoreRapporte=" + scoreRapporte + "]";
	}
	
	@Override
	public boolean equals(Object o){
		if( o instanceof Sbire){
			Sbire sbire = (Sbire) o;
			return sbire.id == id;
		}
		return false;
	}

	@Override
	public DoubleProperty xProperty() {
		return sbireDestroy.xPosProperty();
	}

	@Override
	public DoubleProperty yProperty() {
		return sbireDestroy.yPosProperty();
	}
}
