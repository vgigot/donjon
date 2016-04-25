package PowerUp;

import Model.Player;



public class Pow_Skate extends PowerUp {

	
	public Pow_Skate(int pX, int pY) {
		super(pX, pY);
	}	
	
	@Override 	
	public void affectPlayer(Player pl){
		int sp = pl.getSpeed();
		if(sp <4){pl.setSpeed(sp+1);}
	}
	
}
