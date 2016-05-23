package pathfinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente le chemin, c'est à dire la suite de tuiles qui va du point de départ à la cible. 
 * Ce n'est ni plus ni moins qu'une liste ordonnées de paires de coordonnées
 */
public class Path {
	// la listes des steps. 
	private List<Step> steps = new ArrayList<Step>();
	
	
	public Path() {
		
	}

	/**
	 * la longueur du chemin
	 * 
	 * @return la longueur du chemin
	 */
	public int getLength() {
		return steps.size();
	}
	
	/**
	 * Retourne un step à l'index demandé
	 */
	public Step getStep(int index) {
		return (Step) steps.get(index);
	}
	
	/**
	 * Retourne la coordonnée X d'un step à l'index donné
	 */
	public int getX(int index) {
		return getStep(index).x;
	}

	/**
	 * Retourne la coordonnée X d'un step à l'index donné
	 */
	public int getY(int index) {
		return getStep(index).y;
	}
	
	/**
	 * ajoute un step au chemin (au bout)
	 */
	public void appendStep(int x, int y) {
		steps.add(new Step(x,y));
	}

	/**
	 * insère un step au début du chemin
	 */
	public void prependStep(int x, int y) {
		steps.add(0, new Step(x, y));
	}
	
	/**
	 * vérifie si le couple de coordonnées fourni est déjà présent dans le chemin
	 */
	public boolean contains(int x, int y) {
		return steps.contains(new Step(x,y));
	}
	
	/**
	 * un step (classe interne). 
	 */
	public class Step {
		private int x;
		private int y;
		
		public Step(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {return x;}
		public int getY() {return y;}
		
		// les deux méthodes si dessous sont utiles pour pouvoir ensuite cherche un step donné dans la liste, en se basant sur leur contenu
		// et par sur leur référence.
		public int hashCode() {
			return x*y;
		}

		public boolean equals(Object other) {
			if (other instanceof Step) {
				Step o = (Step) other;
				
				return (o.x == x) && (o.y == y);
			}
			
			return false;
		}
	}
}
