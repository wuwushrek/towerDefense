package pathfinder;

/**
 * Ce contrat décrit une carte constituée de cases (on parle aussi de tuiles, d'où son nom).
 */
public interface TileBasedMap {
	/**
	 * retourne le nombre de tuiles en largeur
	 * 
	 * @return le nombre de tuiles en largeur
	 */
	public int getWidthInTiles();

	/**
	 * retourne le nombre de tuiles en hauteur
	 * 
	 * @return le nombre de tuiles en hauteur
	 */
	public int getHeightInTiles();
	
	/**
	 * cette méthode sera appelée à chaque fois que la tuile de coordonnée x/y sera visité par l'algorithme.
	 * Son intérêt est de débugger l'Heuristic, ie vérifier que le nombre de "fausses pistes" n'est pas trop important (sinon cela signifie que
	 * l'algo est inefficace et perd beaucoup de temps).
	 */
	public void pathFinderVisited(int x, int y);
	
	/**
	 * Cette méthode doit retourner "true" quand la case de coordonnées x/y ne peut pas être visitée par l'objet "Mover" fourni.
	 * Par exemple, si c'est un véhicule terrestre, on retourne false si la case x/y contient un mur. Mais pas si c'est véhicule aérien...
	 */
	public boolean blocked(Mover mover, int x, int y);
	
	/**
	 * Fourni le coût d'aller de la case sx/sy à la case tx/ty (qui est supposée être une case voisine). 
	 * Dans un premier temps, on peut se contenter de retourner 1 tout le temps. 
	 * Dans un deuxième temps, on peut aussi dire que le mouvement diagonal est un peu plus cher
	 * Dans un troisième temps, on peut aussi prendre en compte le fait que certain terrains sont plus coûteux que d'autres, selon le véhicule
	 * qui l'emprunte. 
	 */
	public float getCost(Mover mover, int sx, int sy, int tx, int ty);
}
