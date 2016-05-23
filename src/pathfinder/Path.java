package pathfinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe repr�sente le chemin, c'est � dire la suite de tuiles qui va du point de d�part � la cible. 
 * Ce n'est ni plus ni moins qu'une liste ordonn�es de paires de coordonn�es
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
	 * Retourne un step � l'index demand�
	 */
	public Step getStep(int index) {
		return (Step) steps.get(index);
	}
	
	/**
	 * Retourne la coordonn�e X d'un step � l'index donn�
	 */
	public int getX(int index) {
		return getStep(index).x;
	}

	/**
	 * Retourne la coordonn�e X d'un step � l'index donn�
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
	 * ins�re un step au d�but du chemin
	 */
	public void prependStep(int x, int y) {
		steps.add(0, new Step(x, y));
	}
	
	/**
	 * v�rifie si le couple de coordonn�es fourni est d�j� pr�sent dans le chemin
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
		
		// les deux m�thodes si dessous sont utiles pour pouvoir ensuite cherche un step donn� dans la liste, en se basant sur leur contenu
		// et par sur leur r�f�rence.
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
