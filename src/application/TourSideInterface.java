package application;

public interface TourSideInterface {
	boolean add(Tour tour);
	boolean remove(Tour tour);
	TourTarget lookAndKill(int posX , int posY , int porteeDist);
	boolean isFinishOrOver();
}
