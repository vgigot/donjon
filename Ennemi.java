package Model;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

//import PowerUp.PowerUp;


public class Ennemi{

	private int id;
	private int posX; int posY; //Position en pixels (pas en carrés de 32x32)
	
	private ArrayList<Bomb> spareBombs = new ArrayList<Bomb>();
	//private ArrayList<PowerUp> myPowerUps = new ArrayList<PowerUp>();
	private int fireUp; //La portee des bombes/armes
	private int speed; //La vitesse du joueur
	private String spareEffect; //N si bombe normale, D si bombe dangereuse, M si bombe-taupe.
	
	private int lives; //Le nombre de vies restantes
	private Boolean hit; //Si le joueur est pris dans une explosion
	private int deathPose; //Pour montrer une pose lorsqu'il meurt, purement graphique.
	private ActionListener deathThread;	//Enleve le joueur apres un temps
	private Timer time;
	
	
	
	//----------------------------------------------------------
	
	
	public Ennemi(int i, int scaleX, int scaleY){
		//id = i;
		//placePlayer(scaleX, scaleY);
		fireUp = 2; speed = 2; spareEffect = "N";
		lives = i; hit = false; deathPose = 0;
		//underground = false; ugTime = 0;
		spareBombs.add(new Bomb(0,0));
	}
	
	
	//----------------------------------------------------------
	
	
	//public void placePlayer(int scX, int scY){
		/** Place le joueur en fonction de son Id
		 */
		//if(id == 1){
			//posX = 0; posY = 0;
		//}else if(id == 2){
		//	posX = (scX-1)*32; posY = (scY-1)*32;
		//}else if(id == 3){
		//	posX = (scX-1)*32; posY = 0;
		//}else if(id == 4){
		//	posX = 0; posY = (scY-1)*32;
		//}
	//}
	
	
	
	public int gridPosition(int pos){
		/**Calcule dans quel intervalle de 32 pixels se trouve une
		 position donnee en pixels (x ou y)
		 */
		int gridPos=-1;
		int centerPos = pos+16; //Position du centre de l'image
		for(int i=0; i<=centerPos; i+=32 ){
			gridPos++;
		}
		return gridPos;
	}
	
	
	//----------------------------------------------------------
	
	
	public void placeBomb(){
		Boolean placed = false;
		for(int i=0; i<spareBombs.size() && !placed; i++){
			Bomb b = spareBombs.get(i);
			if(!b.getActive()){
				b.setRange(fireUp);
				b.setEffect(spareEffect);  spareEffect = "N"; //Pose la bombe a effet.
				b.setPosX(gridPosition(posX)); b.setPosY(gridPosition(posY));
				b.setActive(true); b.setJustLaid(true);
				b.beginCountdown();
				placed=true;
			}
		}
	}
	
	

	//----------------------------------------------------------
	
	
	public void deathTimer(){
		deathPose = 1;
		deathThread = new ActionListener(){
			public void actionPerformed(ActionEvent e) {toRemove();}
		};
		time = new Timer(900, deathThread); time.start();
	}
	
	
	//----------------------------------------------------------
	
	
	public void toRemove(){
		time.stop(); deathPose = 2;
	}


	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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


	public ArrayList<Bomb> getSpareBombs() {
		return spareBombs;
	}


	public void setSpareBombs(ArrayList<Bomb> spareBombs) {
		this.spareBombs = spareBombs;
	}


	



	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public String getSpareEffect() {
		return spareEffect;
	}


	public void setSpareEffect(String spareEffect) {
		this.spareEffect = spareEffect;
	}


	public int getLives() {
		return lives;
	}


	public void setLives(int lives) {
		this.lives = lives;
	}


	public Boolean getHit() {
		return hit;
	}


	public void setHit(Boolean hit) {
		this.hit = hit;
	}


	public int getDeathPose() {
		return deathPose;
	}


	public void setDeathPose(int deathPose) {
		this.deathPose = deathPose;
	}



	
}







