package PowerUp;

import Model.Bomb;
import Model.Player;



public class Pow_BombUp extends PowerUp{

	public Pow_BombUp(int pX, int pY) {
		super(pX, pY);
	}
	
	@Override
	public void affectPlayer(Player pl){
		pl.getSpareBombs().add(new Bomb(0,0));
	}
	
}
