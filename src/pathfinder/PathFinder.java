package pathfinder;

/**
 * Contrat de base d'un pathFinder. Un pathfinder est un algorithme qui sait, à partir d'une position de départ et d'une position
 * d'arrivée, fournir un chemin. 
 */
public interface PathFinder {

	/**
	 * Calcule le chemin le plus court
	 * 
	 * @param mover L'élément qui va emprunter le chemin recherché; 
	 * @param sx coordonnée x (en nombre de tuile) en abscisse de la tuile de départ
	 * @param sy coordonnée y (en nombre de tuile) en abscisse de la tuile d'arrivée
	 * @param tx coordonnée x (en nombre de tuile) en abscisse de la tuile cible
	 * @param ty coordonnée y (en nombre de tuile) en abscisse de la tuile cible
	 * @return le chemin le plus court
	 */
	public Path findPath(Mover mover, int sx, int sy, int tx, int ty);
}
