package modele;

import application.OnTourShot;

class Tour implements TourInterface,Runnable {
	private static final long INTERVAL_CHECK = 500;
	private static int accelerateFactor =1;
	private TourSideInterface mPartie;
	private int rowIndex;
	private int columnIndex;
	private int porteeDist;
	private int damages;
	private int cost;
	private long interval_check;
	private boolean isLooking = false;

	private TourTarget target = null;
	private OnTourShot whenShoting;

	public Tour(TourSideInterface partie, int rowIndex, int columnIndex, int porteeDist, int damages, int cost) {
		super();
		mPartie = partie;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.porteeDist = porteeDist;
		this.damages = damages;
		this.cost = cost;
		interval_check = INTERVAL_CHECK;
		mPartie.add(this);
	}

	@Override
	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	@Override
	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getPorteeDist() {
		return porteeDist;
	}

	public void setPorteeDist(int porteeDist) {
		this.porteeDist = porteeDist;
	}

	public int getDamages() {
		return damages;
	}

	public void setDamages(int damages) {
		this.damages = damages;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void reset(){
		target= null;
	}
	
	@Override
	public long getIntervalCheck() {
		return interval_check;
	}

	@Override 
	public void setIntervalCheck(long intervalTime) {
		interval_check = intervalTime;
	}

	public void lookForEnnemyAndKill() {
		target = mPartie.lookAndKill(rowIndex, columnIndex, porteeDist);
		if (target == null)
			return;
		if(whenShoting != null){
			whenShoting.whenShoting(target.xProperty(), target.yProperty());
		}else{
			target.decrementPointDeVie(damages);
		}
	}

	@Override
	public void destroy() {
		mPartie.remove(this);
		isLooking = false;
	}

	@Override
	public void launch() {
		isLooking = true;
		new Thread(this).start();
	}

	public void stopTour() {
		isLooking = false;
	}

	public void setOnTourShot(OnTourShot tourShot) {
		whenShoting = tourShot;
	}
	
	public static void setRate(int rate){
		accelerateFactor=rate;
	}
	
	@Override
	public boolean damage(){
		if(target!= null && !target.decrementPointDeVie(damages)){
			target= null;
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void run() {
		while (!mPartie.isFinishOrOver() && isLooking) {
			if (target == null) {
				lookForEnnemyAndKill();
			} else {
				if (inRange()) {
					if(whenShoting!= null){
						whenShoting.whenShoting(target.xProperty(), target.yProperty());
					}else{
						target.decrementPointDeVie(damages);
					}
				}else{
					target=null;
				}
			}
			try {
				Thread.sleep(interval_check/accelerateFactor);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("TOUR STOPPEE: "+ Thread.currentThread().getName());
	}

	private boolean inRange() {
		int dist = (target.getRowIndex() - rowIndex) * (target.getRowIndex() - rowIndex)
				+ (target.getColumnIndex() - columnIndex) * (target.getColumnIndex() - columnIndex);
		return dist < porteeDist;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Tour) {
			Tour t = (Tour) o;
			return this.rowIndex == t.rowIndex && this.columnIndex == t.columnIndex;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Tour [rowIndex=" + rowIndex + ", columnIndex=" + columnIndex + ", porteeDist=" + porteeDist
				+ ", damages=" + damages + ", cost=" + cost + ", isLooking=" + isLooking + "]";
	}
}
