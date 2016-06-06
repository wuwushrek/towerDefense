package modele;

interface TourTarget {
	boolean decrementPointDeVie(int pointVie);
	int getRowIndex();
	int getColumnIndex();
	double xProperty();
	double yProperty();
}
