package pathfinder;

/**
 * Cette interface est le contrat de base d'un algorithme "A*", lequel se base sur un Heuristic, c'est � dire d'un outil permettant 
 * d'�valuer empiriquement le co�t d'une noeud du graphe, et orienter ainsi le parcours du graphe dans le but atteindre la cible rapidement.
 * 
 * Impl�menter le contrat revient donc � �tre capable de fournir une formule donnant le "co�t" d'une tuile
 */
public interface AStarHeuristic {

	/**
	 * Calcule le co�t de la tuile aux coordonn�es x/y par rapport � une cible situ�e � tx/ty.
	 * 
	 * @param map la carte (consitut�e de tuiles)
	 * @param mover L'�l�ment qui va emprunter le chemin recherch�; 
	 * @param x coordonn�e x (en nombre de tuile) en abscisse de la tuile dont on cherche le co�t
	 * @param y coordonn�e y (en nombre de tuile) en abscisse de la tuile dont on cherche le co�t
	 * @param tx coordonn�e x (en nombre de tuile) en abscisse de la tuile cible
	 * @param ty coordonn�e y (en nombre de tuile) en abscisse de la tuile cible
	 * @return le co�t de la tuile
	 */
	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty);
}
