package application;


public interface OnSbireMoveAndDestroy {
	void onSbireDestroy(boolean isDead);
	void onSbireTouched() ;
	double xPosProperty();
	double yPosProperty();
}
