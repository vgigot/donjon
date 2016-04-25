package PowerUp;

import Model.Player;



public class Pow_FireUp extends PowerUp{

	public Pow_FireUp(int pX, int pY) {
		super(pX, pY);
	}
	
	@Override
	public void affectPlayer(Player pl){
		pl.setFireUp(pl.getFireUp()+1);
	}
	
}

