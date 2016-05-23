package pathfinder;

/**
 * Ce contrat d�crit une carte constitu�e de cases (on parle aussi de tuiles, d'o� son nom).
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
	 * cette m�thode sera appel�e � chaque fois que la tuile de coordonn�e x/y sera visit� par l'algorithme.
	 * Son int�r�t est de d�bugger l'Heuristic, ie v�rifier que le nombre de "fausses pistes" n'est pas trop important (sinon cela signifie que
	 * l'algo est inefficace et perd beaucoup de temps).
	 */
	public void pathFinderVisited(int x, int y);
	
	/**
	 * Cette m�thode doit retourner "true" quand la case de coordonn�es x/y ne peut pas �tre visit�e par l'objet "Mover" fourni.
	 * Par exemple, si c'est un v�hicule terrestre, on retourne false si la case x/y contient un mur. Mais pas si c'est v�hicule a�rien...
	 */
	public boolean blocked(Mover mover, int x, int y);
	
	/**
	 * Fourni le co�t d'aller de la case sx/sy � la case tx/ty (qui est suppos�e �tre une case voisine). 
	 * Dans un premier temps, on peut se contenter de retourner 1 tout le temps. 
	 * Dans un deuxi�me temps, on peut aussi dire que le mouvement diagonal est un peu plus cher
	 * Dans un troisi�me temps, on peut aussi prendre en compte le fait que certain terrains sont plus co�teux que d'autres, selon le v�hicule
	 * qui l'emprunte. 
	 */
	public float getCost(Mover mover, int sx, int sy, int tx, int ty);
}
