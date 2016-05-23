package pathfinder;

/**
 * Cette interface est le contrat de base d'un algorithme "A*", lequel se base sur un Heuristic, c'est à dire d'un outil permettant 
 * d'évaluer empiriquement le coût d'une noeud du graphe, et orienter ainsi le parcours du graphe dans le but atteindre la cible rapidement.
 * 
 * Implémenter le contrat revient donc à être capable de fournir une formule donnant le "coût" d'une tuile
 */
public interface AStarHeuristic {

	/**
	 * Calcule le coût de la tuile aux coordonnées x/y par rapport à une cible située à tx/ty.
	 * 
	 * @param map la carte (consitutée de tuiles)
	 * @param mover L'élément qui va emprunter le chemin recherché; 
	 * @param x coordonnée x (en nombre de tuile) en abscisse de la tuile dont on cherche le coût
	 * @param y coordonnée y (en nombre de tuile) en abscisse de la tuile dont on cherche le coût
	 * @param tx coordonnée x (en nombre de tuile) en abscisse de la tuile cible
	 * @param ty coordonnée y (en nombre de tuile) en abscisse de la tuile cible
	 * @return le coût de la tuile
	 */
	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty);
}
