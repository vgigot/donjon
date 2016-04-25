package PowerUp;

import Model.Player;



public class Pow_MoleBomb extends PowerUp{

	public Pow_MoleBomb(int pX, int pY) {
		super(pX, pY);
	}
	
	@Override
	public void affectPlayer(Player pl){
		pl.setSpareEffect("M");
	}
	
}
