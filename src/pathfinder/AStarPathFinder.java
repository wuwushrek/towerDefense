package pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Impl�mentation d'un path finder bas� sur l'algorithme A*.
 */
public class AStarPathFinder implements PathFinder {
	/** The set of nodes that have been searched through */
	private List<Node> closed = new ArrayList<Node>();
	/** The set of nodes that we do not yet consider fully searched */
	private SortedList<Node> open = new SortedList<Node>();
	
	/** The map being searched */
	private TileBasedMap map;
	/** The maximum depth of search we're willing to accept before giving up */
	private int maxSearchDistance;
	
	/** The complete set of nodes across the map */
	private Node[][] nodes;
	/** True if we allow diaganol movement */
	private boolean allowDiagMovement;
	/** The heuristic we're applying to determine which nodes to search first */
	private AStarHeuristic heuristic;
	
	/**
	 * Create a path finder with the default heuristic - closest to target.
	 * 
	 * @param map The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 * @param allowDiagMovement True if the search should try diaganol movement
	 */
	public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement) {
		this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
	}

	/**
	 * initialise le pathFinder avec un carte constitu�e de cases, une profondeur maximal de recherche, et un heuristique.
	 * On pr�cise aussi si les mouvement diagonaux sont autoris�s
	 * 
	 * Important : � chaque case de la carte est associ� un objet "Node", qui va contenir en cours d'algorithme :
	 *   >> d'une part le co�t Heuristique de la case (ie: co�t par rapport � la cible)
	 *   >> d'autre part le co�t qui y arriver depuis la case de d�part.
	 */
	public AStarPathFinder(TileBasedMap map, int maxSearchDistance, 
						   boolean allowDiagMovement, AStarHeuristic heuristic) {
		this.heuristic = heuristic;
		this.map = map;
		this.maxSearchDistance = maxSearchDistance;
		this.allowDiagMovement = allowDiagMovement;
		
		nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()];
		for (int x=0;x<map.getWidthInTiles();x++) {
			for (int y=0;y<map.getHeightInTiles();y++) {
				nodes[x][y] = new Node(x,y);
			}
		}
	}
	
	/**
	 * L'algorithme A*. 
	 * Je ne pr�ciserai pas trop les d�tails de l'algorithme, ils sont tr�s bien expliqu�s ici :
	 * http://fr.wikipedia.org/wiki/Algorithme_A*
	 * 
	 * @see PathFinder#findPath(Mover, int, int, int, int)
	 */
	public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
		// v�rifie que la destination n'est pas bloqu�e
		if (map.blocked(mover, tx, ty)) {
			return null;
		}
		
		// on commence avec un closed list vide, et une open list comportant uniquement la case de d�part. Son co�t est initialis� � 0
		nodes[sx][sy].cost = 0;
		nodes[sx][sy].depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes[sx][sy]);
		
		nodes[tx][ty].parent = null;
		
		// la boucle principale. Tant que la profondeur maxi de recherche n'est pas trouv�e, et tant qu'il y a au moins encore une "next case" 
		// candidate.
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {

			// �tape cl� de l'algo : dans la liste des "next case" candidates, on prend la moins ch�re en terme de co�t "Heuristique".
			// cette case devient temporairement notre "case en cours"
			Node current = getFirstInOpen();
			
			if (current == nodes[tx][ty]) {
				// on est arriv�s ! => on sort
				break;
			}
			
			// la case choisie est transf�r�e de l'open list � la closed list.
			removeFromOpen(current);
			addToClosed(current);
			
			// On fait "le tour" de notre nouvelle case (8 cases potentielles, entre x-1;x+1 et y-1;y+1)
			for (int x=-1;x<2;x++) {
				for (int y=-1;y<2;y++) {
					// on oublie la case courante
					if ((x == 0) && (y == 0)) {
						continue;
					}
					
					// si le mouvement diagonal n'est pas permis... 
					if (!allowDiagMovement) {
						if ((x != 0) && (y != 0)) {
							continue;
						}
					}
					
					// determine the location of the neighbour and evaluate it
					int xp = x + current.x;
					int yp = y + current.y;
					
					// on v�rifie que ce voisin est atteignable (ie: pas hors de la carte, ni � travers un mur, etc... 
					if (isValidLocation(mover,sx,sy,xp,yp)) {
						// le co�t de parcours de ce voisin est �gale au co�t de la case en cours + le co�t pour aller � ce voisin (charge
						// � la carte de nous fournir ce co�t).
						float nextStepCost = current.cost + getMovementCost(mover, current.x, current.y, xp, yp);
						Node neighbour = nodes[xp][yp];
						
						// pour d�bugger la qualit� de l'heuristic, on marque la case comme "visit�e".
						// concr�tement, si � la fin de l'algo, une immense majorit� des cases de la carte ont �t� visit�es, c'est que
						// l'Heuristic � �chou� dans sa mission � orienter la recherche. Il faut donc l'am�liorer.
						map.pathFinderVisited(xp, yp);
						
						// cas particulier. Ce voisin portait d�j� un co�t, ce qui signifie qu'on y �tait d�j� arriv�, mais par un autre chemin
						// Si, en plus, le nouveau co�t est inf�rieur � celui existant, le chemin courant est meilleur. Il faut donc se
						// d�brouiller (cf quelques lignes ci dessous) pour que ce noeud soit remis dans l'open list , c'est � dire la liste 
						// des cases candidates.
						if (nextStepCost < neighbour.cost) {
							if (inOpenList(neighbour)) {
								removeFromOpen(neighbour);
							}
							if (inClosedList(neighbour)) {
								removeFromClosed(neighbour);
							}
						}
						
						// Si ce voisin: 
						// >> 1) est visit� pour la premi�re fois 
						// >> 2) OU a d�j� �t� visit�, mais en emruntant un chemin plus long
						// alors il n'est ni dans la closedList, ni l'open list. 
						// on lui positionne son co�t de parcours, son co�t heuristic, et on l'ajoute � l'open list. 
						if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
							neighbour.cost = nextStepCost;
							neighbour.heuristic = getHeuristicCost(mover, xp, yp, tx, ty);
							maxDepth = Math.max(maxDepth, neighbour.setParent(current));
							addToOpen(neighbour);
						}
					}
				}
			}
		}

		// Si le noeud cible n'a pas de parent, c'est qu'on y est pas arriv�; Le path retoun�e est donc "null".
		// (soit il n'y avait pas de solution, soit on ne l'a pas atteinte dans les limites fix�es par la recherche (profondeux max).
		if (nodes[tx][ty].parent == null) {
			return null;
		}
		
		// Nous avons donc un chemin � reconstituer. Il suffit de partir de la fin, puis de demander le parent de chaque noeud
		// jusqu'� remonter ainsi au noeud de d�part. 
		Path path = new Path();
		Node target = nodes[tx][ty];
		while (target != nodes[sx][sy]) {
			path.prependStep(target.x, target.y);
			target = target.parent;
		}
		path.prependStep(sx,sy);
		
		 
		return path;
	}

	/**
	 * r�cup�re le premier noeud (donc celui dont le co�t Heuristic est le plus faible) dans l'open list
	 */
	protected Node getFirstInOpen() {
		return (Node) open.first();
	}
	
	/**
	 * ajoute un noeud � l'open list
	 */
	protected void addToOpen(Node node) {
		open.add(node);
	}
	
	/**
	 * v�rifie si un noeud appartient � l'open List
	 */
	protected boolean inOpenList(Node node) {
		return open.contains(node);
	}
	
	/**
	 * retire un noeud de l'open list
	 */
	protected void removeFromOpen(Node node) {
		open.remove(node);
	}
	
	/**
	 * ajoute un noeud � la closedList
	 */
	protected void addToClosed(Node node) {
		closed.add(node);
	}
	
	/**
	 * v�rifie si l'�lement fourni est dans la closed list
	 */
	protected boolean inClosedList(Node node) {
		return closed.contains(node);
	}
	
	/**
	 * retire un �l�ment de la closed list
	 */
	protected void removeFromClosed(Node node) {
		closed.remove(node);
	}
	
	/**
	 * v�rifie que le mouvement allant de sx/sy � x/y est valide, c'est � dire qu'on ne sort pas de la carte, et que la 
	 * case de destination n'est pas "bloqu�e" pour le mobile fourni (mur)
	 */
	protected boolean isValidLocation(Mover mover, int sx, int sy, int x, int y) {
		boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles());
		
		if ((!invalid) && ((sx != x) || (sy != y))) {
			invalid = map.blocked(mover, x, y);
		}
		
		return !invalid;
	}
	
	/**
	 * se base sur la carte pour connaitre le co�t d'aller d'un noeud au noeud suivant. 
	 */
	public float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
		return map.getCost(mover, sx, sy, tx, ty);
	}

	/**
	 * se base sur l'Heuristique initialis� en t�te de classe pour calculer le co�t "empirique" d'un noeud. 
	 */
	public float getHeuristicCost(Mover mover, int x, int y, int tx, int ty) {
		return heuristic.getCost(map, mover, x, y, tx, ty);
	}
	
	/**
	 * Une simple Arraylist, mais qui retrie tous ses �l�ment � chaque fois qu'on en ajoute un. 
	 *
	 */
	private class SortedList<T extends Comparable<T>> {
		/** The list of elements */
		private List<T> list = new ArrayList<T>();
		
		/**
		 * retourne le premier noeud de la liste, donc le "meilleur" selon le crit�re de tri; 
		 */
		public T first() {
			return list.get(0);
		}
		
		public void clear() {
			list.clear();
		}
		
		/**
		 * ajoute un �lement et retrie
		 */
		public void add(T o) {
			list.add(o);
			Collections.sort(list);
		}
		
		public void remove(Object o) {
			list.remove(o);
		}
	
		public int size() {
			return list.size();
		}
		
		public boolean contains(Object o) {
			return list.contains(o);
		}
	}
	
	/**
	 * Classe interne : Node est un noeud atomique dans le "graphe" de recherche
	 */
	private class Node implements Comparable<Node> {
		/** coordonn�e x */
		private int x;
		/** coordonn�e y */
		private int y;
		/** le co�t pour aller du point de d�part jusqu'� ce noeud */
		private float cost;
		/** Le noeud parent, c'est � dire celui duquel nous somme partis pour trouver le pr�sent noeud
		 * Remarque : c'est donc un noeud voisin. */
		private Node parent;
		/** le co�t heuristic de ce noeud. Attention � ne pas confondre avec le co�t pour aller ce noeud.
		 * Le co�t heuristic �value de mani�re empirique le co�t "restant" pour aller � la cible. il sera utilis� pour 
		 * aider � choisir le plus rapidement possible le bon chemin */
		private float heuristic;
		/** la profondeur de recherche pour arriver � ce noeud; Utilis� pour quitter l'algorithme quand on consid�re �tre all� trop loin
		 * sur une piste */
		private int depth;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		/**
		 * positionne le parent, et la profondeur.
		 */
		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;
			
			return depth;
		}
		
		/**
		 * Utilis� pour comparer les noeuds entre eux.
		 * @see Comparable#compareTo(Object)
		 */
		public int compareTo(Node o) {
			
			float f = heuristic + cost;
			float of = o.heuristic + o.cost;
			
			if (f < of) {
				return -1;
			} else if (f > of) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
