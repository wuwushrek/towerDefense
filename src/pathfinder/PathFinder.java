package pathfinder;

/**
 * Contrat de base d'un pathFinder. Un pathfinder est un algorithme qui sait, � partir d'une position de d�part et d'une position
 * d'arriv�e, fournir un chemin. 
 */
public interface PathFinder {

	/**
	 * Calcule le chemin le plus court
	 * 
	 * @param mover L'�l�ment qui va emprunter le chemin recherch�; 
	 * @param sx coordonn�e x (en nombre de tuile) en abscisse de la tuile de d�part
	 * @param sy coordonn�e y (en nombre de tuile) en abscisse de la tuile d'arriv�e
	 * @param tx coordonn�e x (en nombre de tuile) en abscisse de la tuile cible
	 * @param ty coordonn�e y (en nombre de tuile) en abscisse de la tuile cible
	 * @return le chemin le plus court
	 */
	public Path findPath(Mover mover, int sx, int sy, int tx, int ty);
}
