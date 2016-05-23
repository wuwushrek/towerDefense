package pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Implémentation d'un path finder basé sur l'algorithme A*.
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
	 * initialise le pathFinder avec un carte constituée de cases, une profondeur maximal de recherche, et un heuristique.
	 * On précise aussi si les mouvement diagonaux sont autorisés
	 * 
	 * Important : à chaque case de la carte est associé un objet "Node", qui va contenir en cours d'algorithme :
	 *   >> d'une part le coût Heuristique de la case (ie: coût par rapport à la cible)
	 *   >> d'autre part le coût qui y arriver depuis la case de départ.
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
	 * Je ne préciserai pas trop les détails de l'algorithme, ils sont très bien expliqués ici :
	 * http://fr.wikipedia.org/wiki/Algorithme_A*
	 * 
	 * @see PathFinder#findPath(Mover, int, int, int, int)
	 */
	public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
		// vérifie que la destination n'est pas bloquée
		if (map.blocked(mover, tx, ty)) {
			return null;
		}
		
		// on commence avec un closed list vide, et une open list comportant uniquement la case de départ. Son coût est initialisé à 0
		nodes[sx][sy].cost = 0;
		nodes[sx][sy].depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes[sx][sy]);
		
		nodes[tx][ty].parent = null;
		
		// la boucle principale. Tant que la profondeur maxi de recherche n'est pas trouvée, et tant qu'il y a au moins encore une "next case" 
		// candidate.
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {

			// étape clé de l'algo : dans la liste des "next case" candidates, on prend la moins chère en terme de coût "Heuristique".
			// cette case devient temporairement notre "case en cours"
			Node current = getFirstInOpen();
			
			if (current == nodes[tx][ty]) {
				// on est arrivés ! => on sort
				break;
			}
			
			// la case choisie est transférée de l'open list à la closed list.
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
					
					// on vérifie que ce voisin est atteignable (ie: pas hors de la carte, ni à travers un mur, etc... 
					if (isValidLocation(mover,sx,sy,xp,yp)) {
						// le coût de parcours de ce voisin est égale au coût de la case en cours + le coût pour aller à ce voisin (charge
						// à la carte de nous fournir ce coût).
						float nextStepCost = current.cost + getMovementCost(mover, current.x, current.y, xp, yp);
						Node neighbour = nodes[xp][yp];
						
						// pour débugger la qualité de l'heuristic, on marque la case comme "visitée".
						// concrètement, si à la fin de l'algo, une immense majorité des cases de la carte ont été visitées, c'est que
						// l'Heuristic à échoué dans sa mission à orienter la recherche. Il faut donc l'améliorer.
						map.pathFinderVisited(xp, yp);
						
						// cas particulier. Ce voisin portait déjà un coût, ce qui signifie qu'on y était déjà arrivé, mais par un autre chemin
						// Si, en plus, le nouveau coût est inférieur à celui existant, le chemin courant est meilleur. Il faut donc se
						// débrouiller (cf quelques lignes ci dessous) pour que ce noeud soit remis dans l'open list , c'est à dire la liste 
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
						// >> 1) est visité pour la première fois 
						// >> 2) OU a déjà été visité, mais en emruntant un chemin plus long
						// alors il n'est ni dans la closedList, ni l'open list. 
						// on lui positionne son coût de parcours, son coût heuristic, et on l'ajoute à l'open list. 
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

		// Si le noeud cible n'a pas de parent, c'est qu'on y est pas arrivé; Le path retounée est donc "null".
		// (soit il n'y avait pas de solution, soit on ne l'a pas atteinte dans les limites fixées par la recherche (profondeux max).
		if (nodes[tx][ty].parent == null) {
			return null;
		}
		
		// Nous avons donc un chemin à reconstituer. Il suffit de partir de la fin, puis de demander le parent de chaque noeud
		// jusqu'à remonter ainsi au noeud de départ. 
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
	 * récupére le premier noeud (donc celui dont le coût Heuristic est le plus faible) dans l'open list
	 */
	protected Node getFirstInOpen() {
		return (Node) open.first();
	}
	
	/**
	 * ajoute un noeud à l'open list
	 */
	protected void addToOpen(Node node) {
		open.add(node);
	}
	
	/**
	 * vérifie si un noeud appartient à l'open List
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
	 * ajoute un noeud à la closedList
	 */
	protected void addToClosed(Node node) {
		closed.add(node);
	}
	
	/**
	 * vérifie si l'élement fourni est dans la closed list
	 */
	protected boolean inClosedList(Node node) {
		return closed.contains(node);
	}
	
	/**
	 * retire un élément de la closed list
	 */
	protected void removeFromClosed(Node node) {
		closed.remove(node);
	}
	
	/**
	 * vérifie que le mouvement allant de sx/sy à x/y est valide, c'est à dire qu'on ne sort pas de la carte, et que la 
	 * case de destination n'est pas "bloquée" pour le mobile fourni (mur)
	 */
	protected boolean isValidLocation(Mover mover, int sx, int sy, int x, int y) {
		boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles());
		
		if ((!invalid) && ((sx != x) || (sy != y))) {
			invalid = map.blocked(mover, x, y);
		}
		
		return !invalid;
	}
	
	/**
	 * se base sur la carte pour connaitre le coût d'aller d'un noeud au noeud suivant. 
	 */
	public float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
		return map.getCost(mover, sx, sy, tx, ty);
	}

	/**
	 * se base sur l'Heuristique initialisé en tête de classe pour calculer le coût "empirique" d'un noeud. 
	 */
	public float getHeuristicCost(Mover mover, int x, int y, int tx, int ty) {
		return heuristic.getCost(map, mover, x, y, tx, ty);
	}
	
	/**
	 * Une simple Arraylist, mais qui retrie tous ses élément à chaque fois qu'on en ajoute un. 
	 *
	 */
	private class SortedList<T extends Comparable<T>> {
		/** The list of elements */
		private List<T> list = new ArrayList<T>();
		
		/**
		 * retourne le premier noeud de la liste, donc le "meilleur" selon le critère de tri; 
		 */
		public T first() {
			return list.get(0);
		}
		
		public void clear() {
			list.clear();
		}
		
		/**
		 * ajoute un élement et retrie
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
		/** coordonnée x */
		private int x;
		/** coordonnée y */
		private int y;
		/** le coût pour aller du point de départ jusqu'à ce noeud */
		private float cost;
		/** Le noeud parent, c'est à dire celui duquel nous somme partis pour trouver le présent noeud
		 * Remarque : c'est donc un noeud voisin. */
		private Node parent;
		/** le coût heuristic de ce noeud. Attention à ne pas confondre avec le coût pour aller ce noeud.
		 * Le coût heuristic évalue de manière empirique le coût "restant" pour aller à la cible. il sera utilisé pour 
		 * aider à choisir le plus rapidement possible le bon chemin */
		private float heuristic;
		/** la profondeur de recherche pour arriver à ce noeud; Utilisé pour quitter l'algorithme quand on considère être allé trop loin
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
		 * Utilisé pour comparer les noeuds entre eux.
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
