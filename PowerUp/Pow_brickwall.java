package PowerUp;


import Model.Player;



public class Pow_brickwall extends PowerUp{
	
	public Pow_brickwall(int pX, int pY) {
		super(pX, pY);
	}
	
	@Override
	public void affectPlayer(Player pl){
		pl.setSpareEffect("M");
	}

}
