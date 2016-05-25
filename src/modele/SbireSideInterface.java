package modele;

public interface SbireSideInterface {
	boolean remove(Sbire sbire , boolean isDead);
	boolean moveFromTo(Sbire sbire, int fromX , int fromY);
}
