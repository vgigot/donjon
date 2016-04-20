package PowerUp;

import Model.Player;



public class Pow_FireDown extends PowerUp{
	
	public Pow_FireDown(int pX, int pY) {
		super(pX, pY);
	}	

	
	@Override 	
	public void affectPlayer(Player pl){
		int fu = pl.getFireUp();
		if (fu > 1) {pl.setFireUp(fu-1);}
	}
	
}
