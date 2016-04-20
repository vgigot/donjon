package Model;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class Bomb {
	/**Chaque joueur a un certain nombre de bombes.
	 * Ces bombes s'activent en se posant, et prennent alors une position,
	 * lorsqu'elles ont explose, elles deviennent inactives.
	 * On ne peut poser une bombe que s'il y a des bombes inactives.
	 * Les bombes peuvent avoir aussi plusieurs effets, donnes par le joueur.
	 */

	private int range;
	private int posX; private int posY; //Position en carrés de 32x32.

	private boolean active; //Si la bombe est active.
	private boolean canExplode; //Si on peut y placer une explosion.
	private boolean justLaid; //Si le joueur n'est pas sorti de la zone de la bombe.
	
	private String effect; //N si bombe normale, D si bombe dangereuse, M si bombe-taupe.
	
	private ActionListener bombThread;
	private Timer time;
	
	
	//----------------------------------------------------------
	
	
	public Bomb(int pX, int pY){
		/**Les bombes sont initialisees inactives a l'origine des coordonees,
		 * la portee de l'explosion est fournie par l'objet joueur. Les bombes
		 * sont aussi a l'origine avec effet normal.
		 */
		range = 1; posX=0; posY=0;
		active = false; canExplode = false; justLaid = false;
		effect = "N";
	}
	
	
	public void beginCountdown(){
		/**Lance le compte a rebours pour l'explosion.
		 */
		bombThread = new ActionListener(){	//Le compte a rebours pour l'explosion.
			public void actionPerformed(ActionEvent e) {explode();}
		};
		time = new Timer(2000, bombThread); time.start();
	}
	
	
	public void explode(){
		time.stop();
		active = false; canExplode = true;
	}

	
	//----------------------------------------------------------
	
	
	public int getRange() {
		return range;
	}



	public void setRange(int range) {
		this.range = range;
	}



	public int getPosX() {
		return posX;
	}



	public void setPosX(int posX) {
		this.posX = posX;
	}



	public int getPosY() {
		return posY;
	}



	public void setPosY(int posY) {
		this.posY = posY;
	}



	public boolean getActive() {
		return active;
	}



	public void setActive(boolean active) {
		this.active = active;
	}



	public boolean getCanExplode() {
		return canExplode;
	}



	public void setCanExplode(boolean canExplode) {
		this.canExplode = canExplode;
	}



	public boolean getJustLaid() {
		return justLaid;
	}



	public void setJustLaid(boolean justLaid) {
		this.justLaid = justLaid;
	}



	public String getEffect() {
		return effect;
	}



	public void setEffect(String effect) {
		this.effect = effect;
	}
}
