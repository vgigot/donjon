package PowerUp;

import Model.Player;


public class Pow_Heart extends PowerUp {

	public Pow_Heart(int pX, int pY) {
		super(pX, pY);
	}
	
	@Override
	public void affectPlayer(Player pl){
		if(pl.getLives()<=2){pl.setLives(pl.getLives()+1);} //Pas plus de deux vies en meme temps.
	}
	
}
