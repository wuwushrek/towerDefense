package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import pathfinder.AStarPathFinder;
import pathfinder.Mover;
import pathfinder.Path;
import pathfinder.PathFinder;
import pathfinder.TileBasedMap;

class Partie implements TileBasedMap , TourSideInterface, SbireSideInterface, PartieInterface{
	private final static int[] depart = new int[2];
	private final static int[] arrivee = new int[2];

	//private static final int MAX_LEVEL = 3;
	private final static int[] sbireByLevel = new int[]{25 , 25, 35};
	private static final int MAX_VIE = 100;
	private static final int MAX_ARGENT =3000;
	
	private final static int WALL = -1;
	private final static int VIDE = -2;
	private final static int TOUR = -3;
	//private final static int MULTIPLE_SBIRE=-4;
	//private final static int SBIRE = -5;
	
	PathFinder pathFinder;
	int[][] map;
	
	private int rowCount;
	private int columnCount;
	private int nbOfSbireGone=0;
	
	private IntegerProperty pointVie;
	private IntegerProperty sbireTuee;
	private IntegerProperty argent;
	private IntegerProperty score;
	private IntegerProperty level;
	
	List<Tour> mTours;
	List<SbireInterface> mSbires;
	
	volatile boolean isAlive = true;
	volatile boolean LEVEL_DONE =false;
	
	public Partie(int row , int column , int[] ptDepart , int[] ptArrivee){
		rowCount = row;
		columnCount = column;
		
		depart[0]= ptDepart[0];
		depart[1] = ptDepart[1];
		arrivee[0] = ptArrivee[0];
		arrivee[1] = ptArrivee[1];
		
		map = new int[rowCount][columnCount];
		level = new SimpleIntegerProperty(0);
		sbireTuee = new SimpleIntegerProperty(0);
		argent = new SimpleIntegerProperty(MAX_ARGENT);
		pointVie = new SimpleIntegerProperty(MAX_VIE);
		score = new SimpleIntegerProperty(0);
		
		mTours = new ArrayList<Tour>();
		mSbires = new CopyOnWriteArrayList<SbireInterface>();//new ArrayList<Sbire>();
		resetMap();
		//initSbires();
		pathFinder = new AStarPathFinder(this,500,false);
	}
	
	@Override
	public IntegerProperty levelProperty(){
		return level;
	}
	
	public void incrementLevel(){
		level.set(level.get()+1);
	}
	
	@Override
	public IntegerProperty sbireTueeProperty(){
		return sbireTuee;
	}
	
	public synchronized void incrementSbireTuee(){
		sbireTuee.set(sbireTuee.get()+1);
	}
	
	@Override
	public IntegerProperty argentProperty(){
		return argent;
	}
	
	public synchronized void incrementArgent(int value){
		argent.set(argent.get() + value);
	}
	
	public synchronized boolean decrementArgent(int value){
		int newValue = argent.get()-value;
		if(newValue<0){
			return false;
		}
		argent.set(newValue);
		return true;
	}
	
	@Override
	public IntegerProperty pointVieProperty(){
		return pointVie;
	}
	
	public synchronized void incrementPointVie(int value){
		pointVie.set(pointVie.get()+value);
	}
	
	public synchronized void decrementPointVie(int value){
		int newValue = pointVie.get() -value;
		if(newValue<=0){
			pointVie.set(0);
			isAlive = false;
			for(Tour tour: mTours){
				tour.stopTour();
			}
			return;
		}
		pointVie.set(newValue);
	}
	
	
	public IntegerProperty scoreProperty(){
		return score;
	}
	
	public synchronized void incrementScore(int value){
		score.set(score.get()+value);
	}
	
	//A retoucher
	@Override
	public boolean add(Tour tour) {
		if(!decrementArgent(tour.getCost())){
			System.out.println("NOT ENOUGHT MONEY: "+tour.toString());
			return false;
		}
		mTours.add(tour);
		map[tour.getRowIndex()][tour.getColumnIndex()]= TOUR;
		System.out.println("ADDED: "+tour.toString());
		System.out.println("POINT VIE: "+pointVie.get()+", ARGENT: "+ argent.get()+ ", SBIRE TUEES: "+
				sbireTuee.get()+", SCORE: "+score.get());
		return true;
	}

	@Override
	public boolean remove(Tour tour) {
		// TODO Auto-generated method stub
		map[tour.getRowIndex()][tour.getColumnIndex()]= VIDE;
		tour.stopTour();
		mTours.remove(tour);
		System.out.println("DESTROY: "+tour.toString());
		return true;
	}

	@Override
	public TourTarget lookAndKill(int rowIndex, int columnIndex,int porteeDist) {
		return findSbire(rowIndex,columnIndex,porteeDist);
		// TODO Auto-generated method stub
		/*int minX = rowIndex - porteeRow;
		int minY = columnIndex - porteeColumn;
		int maxX = rowIndex + porteeRow;
		int maxY = columnIndex + porteeColumn;
		
		minX = minX<0? 0:minX;
		minY = minY<0? 0:minY;
		maxX = maxX>rowCount? rowCount:maxX;
		maxY = maxY>columnCount? columnCount:maxY;
		//System.out.println("MINX: "+minX+ " ,MINY: "+minY+" ,MAX X: "+maxX+"MAX Y: "+maxY);
		if(rowIndex>arrivee[0]){
			if(columnIndex>arrivee[1]){
				for(int i= minX ; i<maxX ; i++){
					for(int j=minY; j<maxY ; j++){
						if(!blocked(null,i,j) && map[i][j]!= VIDE){
							Sbire sbire = findSbire(i,j);
							if(sbire!=null){
								System.out.println("Partie TERMINEE: "+isFinishOrOver());
								//sbire.decrementPointDeVie(damage);
								return sbire;
							}
						}
					}
				}
			}else{
				for(int i= minX ; i<maxX ; i++){
					for(int j=maxY-1; j>=minY ; j--){
						if(!blocked(null,i,j) && map[i][j]!= VIDE){
							Sbire sbire = findSbire(i,j);
							if(sbire!=null){
								System.out.println("Partie TERMINEE: "+isFinishOrOver());
								//sbire.decrementPointDeVie(damage);
								return sbire;
							}
						}
					}
				}
			}
		}else{
			if(columnIndex>arrivee[1]){
				for(int i= maxX-1 ; i>=minX ; i--){
					for(int j=minY; j<maxY ; j++){
						if(!blocked(null,i,j) && map[i][j]!= VIDE){
							Sbire sbire = findSbire(i,j);
							if(sbire!=null){
								System.out.println("Partie TERMINEE: "+isFinishOrOver());
								//sbire.decrementPointDeVie(damage);
								return sbire;
							}
						}
					}
				}
			}else{
				for(int i= maxX-1 ; i>=minX ; i--){
					for(int j=maxY-1; j>+minY ; j--){
						if(!blocked(null,i,j) && map[i][j]!= VIDE){
							Sbire sbire = findSbire(i,j);
							if(sbire!=null){
								System.out.println("Partie TERMINEE: "+isFinishOrOver());
								//sbire.decrementPointDeVie(damage);
								return sbire;
							}
						}
					}
				}
			}
			
		}
		return null;*/
	}

	@Override
	public boolean remove(Sbire sbire , boolean isDead) {
		// TODO Auto-generated method stub
		if(!mSbires.remove(sbire))
				return false;
		getAndApplyMoveForSbire(sbire.getRowIndex(),sbire.getColumnIndex(),0,0,false);
		if(!isDead){
			decrementArgent(sbire.getArgentRapporte());//a modifier
			decrementPointVie(sbire.getDamages());
		}else{
			incrementArgent(sbire.getArgentRapporte());
			incrementSbireTuee();
			incrementScore(sbire.getScoreRapporte());
		}
		nbOfSbireGone++;
		LEVEL_DONE = nbOfSbireGone == sbireByLevel[level.get()];
		System.out.println("DESTROY: "+ sbire.toString());
		System.out.println("POINT VIE: "+pointVie.get()+", ARGENT: "+ argent.get()+ ", SBIRE TUEES: "+
		sbireTuee.get()+", SCORE: "+score.get());
		return true;
	}

	@Override
	public boolean isFinishOrOver() {
		return !isAlive || LEVEL_DONE;
	}
	
	
	@Override
	public int getWidthInTiles() {
		return this.rowCount;
	}

	@Override
	public int getHeightInTiles() {
		return this.columnCount;
	}

	@Override
	public void pathFinderVisited(int x, int y) {}

	@Override
	public boolean blocked(Mover mover, int x, int y) {
		return (map[x][y]== WALL || map[x][y] == TOUR);
	}

	@Override
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		return 1;
	}
	
	public static void setDepart(int x , int y){
		depart[0]=x;
		depart[1]=y;
	}
	
	public static void setArrivee(int x , int y){
		arrivee[0]=x;
		arrivee[1]=y;
	}
	
	public void resetMap(){
		for ( int i=0;i<rowCount;i++){
			for (int j=0;j<columnCount;j++){
				map[i][j]=VIDE;
			}
		}
	}
	
	public void setWalls(int[][] walls){
		for(int i=0; i<walls.length;i++){
			map[walls[i][0]][walls[i][0]]= WALL;
		}
	}
	
	@Override
	public void initSbiresOnLevel(){
		mSbires = new CopyOnWriteArrayList<SbireInterface>();//new ArrayList<Sbire>();
		for(int i = 0 ; i<sbireByLevel[level.get()]; i++){
			Sbire sbire = new Sbire(this,100,depart[0],depart[1],25,100, 5 ,1.0);
			mSbires.add(sbire);
		}
		map[depart[0]][depart[1]] = sbireByLevel[level.get()];
	}

	@Override
	public boolean moveFromTo(Sbire sbire, int fromRow, int fromColumn) {
		if(sbire.getRowIndex()==arrivee[0] && sbire.getColumnIndex() == arrivee[1]){
			sbire.destroy(false);
			return false;
		}
		if(sbire.getRowIndex()>=0 && sbire.getRowIndex()<rowCount && sbire.getColumnIndex()>=0 && sbire.getColumnIndex()<columnCount){
			getAndApplyMoveForSbire(fromRow,fromColumn,sbire.getRowIndex(),sbire.getColumnIndex(),true);
			return true;
		}
		return false;
	}
	
	private TourTarget findSbire(int rowIndex, int columnIndex, int porteeDist){
		SbireInterface res=null;
		int minDistArrive = Integer.MAX_VALUE;
		for(SbireInterface sbire: mSbires){
			int dist = (sbire.getRowIndex()-rowIndex)*(sbire.getRowIndex()-rowIndex)+
					(sbire.getColumnIndex()-columnIndex)*(sbire.getColumnIndex()-columnIndex);
			int dist2 = (sbire.getRowIndex()-arrivee[0])*(sbire.getRowIndex()-arrivee[0])+
					(sbire.getColumnIndex()-arrivee[1])*(sbire.getColumnIndex()-arrivee[1]);
			if(dist<porteeDist && dist2<minDistArrive){
				minDistArrive=dist2;
				res=sbire;
			}
		}
		return (Sbire) res;
	}
	/*private Sbire findSbire(int rowIndex , int columnIndex){
		for(Sbire sbire: mSbires){
			if (sbire.getRowIndex()== rowIndex && sbire.getColumnIndex()== columnIndex)
				return sbire;
		}
		return null;
	}*/
	
	private synchronized void getAndApplyMoveForSbire(int fromX , int fromY , int toX , int toY , 
			boolean testDestination){
		int current = map[fromX][fromY];
		if(current==1){
			map[fromX][fromY]= VIDE;
		}else if (current>1){
			map[fromX][fromY]--;
		}
		if(testDestination){
			int dest = map[toX][toY];
			if(dest==VIDE){
				map[toX][toY]=1;
			}else if(dest>=1){
				map[toX][toY]++;
			}
		}
	}
	
	public int count(){
		int res=0;
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				if(map[i][j]>0){
					res+=map[i][j];
				}
			}
		}
		return res;
	}
	
	@Override
	public List<SbireInterface> getSbireList(){
		return mSbires;
	}
	
	public void addSbire(int rowIndex , int columnIndex){
		Sbire sbire = new Sbire(this,100,rowIndex,columnIndex,50,300, 5,1.0);
		getAndApplyMoveForSbire(0,0,rowIndex,columnIndex,true);
		mSbires.add(sbire);
	}
	
	
	public void timeToSetSbirePath(){
		Path path = pathFinder.findPath(new Mover(), depart[0], depart[1], arrivee[0], arrivee[1]);
		Sbire.setPath(path);
	}

	@Override
	public SbireSideInterface getSbireSideInterface() {
		return this;
	}

	@Override
	public TourSideInterface getTourSideInterface() {
		return this;
	}
}
