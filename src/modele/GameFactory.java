package modele;

public class GameFactory {
	
	public static PartieInterface createPartie(int rowCount, int columnCount, int[] depart , int[] arrivee){
		return new Partie(rowCount,columnCount,depart , arrivee);
	}
	
	public static TourInterface createTour(TourSideInterface partie,int rowIndex , int columnIndex, 
			int porteeDist , int damages , int cost ){
		return new Tour(partie,rowIndex,columnIndex,porteeDist , damages , cost);
	}
}
