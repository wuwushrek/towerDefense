package pathfinder;


/**
 * L'agorithme A* ne parcourt pas les diff�rents chemins possible au hasard. A chaque fois qu'il a plusieurs options pour la prochaine case, 
 * il commence par essayer celle dont le "co�t" est le plus faible. Cette notion de co�t est bien s�r approximative et ne garantira jamais
 * que je premier chemin test� sera le bon, mais offrira un niveau d'optimisation suffisant pour l'algorithme soit rapide. 
 * 
 * Ici, l'id�e est de dire que le co�t d'une tuile est �gale � sa distance absolue � la cible. Ce calcul ne prend pas en compte le fait que
 * des murs peuvent exister entre elle et la cible, mais donne une orientation efficace dans la majorit� des cas. 
 */
public class ClosestHeuristic implements AStarHeuristic {
	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {		
		float dx = tx - x;
		float dy = ty - y;
		
		float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
		
		return result;
	}

}
