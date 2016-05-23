package pathfinder;


/**
 * L'agorithme A* ne parcourt pas les différents chemins possible au hasard. A chaque fois qu'il a plusieurs options pour la prochaine case, 
 * il commence par essayer celle dont le "coût" est le plus faible. Cette notion de coût est bien sûr approximative et ne garantira jamais
 * que je premier chemin testé sera le bon, mais offrira un niveau d'optimisation suffisant pour l'algorithme soit rapide. 
 * 
 * Ici, l'idée est de dire que le coût d'une tuile est égale à sa distance absolue à la cible. Ce calcul ne prend pas en compte le fait que
 * des murs peuvent exister entre elle et la cible, mais donne une orientation efficace dans la majorité des cas. 
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
