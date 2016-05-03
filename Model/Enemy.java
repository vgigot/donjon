package Model;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import PowerUp.PowerUp;


public class Enemy{

	private int id;
	private int posX; int posY; //Position en pixels (pas en carrés de 32x32)
	
	private ArrayList<Bomb> spareBombs = new ArrayList<Bomb>();
	private ArrayList<PowerUp> myPowerUps = new ArrayList<PowerUp>();
	private int fireUp; //La portee des bombes
	private int speed; //La vitesse du joueur
	private String spareEffect; //N si bombe normale, D si bombe dangereuse, M si bombe-taupe.
	
	private int lives; //Le nombre de vies restantes
	private Boolean hit; //Si le joueur est pris dans une explosion
	private int deathPose; //Pour montrer une pose lorsqu'il meurt, purement graphique.
	private ActionListener deathThread;	//Enleve le joueur apres un temps
	private Timer time;
	
	//Pour avancer sous-terre.
	private boolean underground;
	private ActionListener holeThread;	//Enleve le joueur apres un temps
	private int ugTime;
	
	//private Inventory inventory;
	
	
	//----------------------------------------------------------
	
	
	public Enemy(int i, int scaleX, int scaleY){
		id = i;
		placeEnemi(scaleX, scaleY);
		fireUp = 2; speed = 2; spareEffect = "N";
		lives = 1; hit = false; deathPose = 0;
		underground = false; ugTime = 0;
		spareBombs.add(new Bomb(0,0));
		//inventory = inv;
	}
	
	
	//----------------------------------------------------------
	
	
	public void placeEnemi(int scX, int scY){
		/** Place le joueur en fonction de son Id
		 */
		if(id == 1){
			posX = (scX-1)*32; posY = 0;
		}else if(id == 2){
			posX = 0; posY = (scY-1)*32;
		}
	}
	
	
	
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
	
	
	//public void placeBomb(){
	//	Boolean placed = false;
	//	for(int i=0; i<spareBombs.size() && !placed; i++){
		//	Bomb b = spareBombs.get(i);
		//	if(!b.getActive()){
			///	b.setRange(fireUp);
			//	b.setEffect(spareEffect);  spareEffect = "N"; //Pose la bombe a effet.
			//	b.setPosX(gridPosition(posX)); b.setPosY(gridPosition(posY));
			//	b.setActive(true); b.setJustLaid(true);
			//	b.beginCountdown();
			//	placed=true;
///         }
	//	}
//	}
	
	//public void inventory(){
	//	this.inventory.menu();
	//}
	
	//public void useItem() {
	//	this.inventory.useCurrentItem(this);
	//}
	
	//----------------------------------------------------------
	
	
	public void intoGround(){
		/**Met le joueur sous terre et compte le temps
		 * de vie qu'il lui reste avant d'y étouffer.
		 */
	ugTime = 6; underground = true;
		holeThread = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ugTime --;
				if(ugTime <= 0){time.stop(); deathTimer();}
				else{time.stop(); time = new Timer(1000, holeThread); time.start();}
			}
		};
		time = new Timer(4000, holeThread); time.start();
	}
	
	
	
	public void outOfGround(int pX, int pY){
		/**Sort le joueur a la surface, a une certaine position.
		 */
		time.stop(); underground = false;
		posX = pX*32; posY = pY*32;
	}
	
	
	//----------------------------------------------------------
	
	
	public void deathTimer(){
		deathPose = 1;
		deathThread = new ActionListener(){
			public void actionPerformed(ActionEvent e) {toRemove();}
		};
		time = new Timer(1010, deathThread); time.start();
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


	public ArrayList<PowerUp> getMyPowerUps() {
		return myPowerUps;
	}


	public void setMyPowerUps(ArrayList<PowerUp> myPowerUps) {
		this.myPowerUps = myPowerUps;
	}


	public int getFireUp() {
		return fireUp;
	}


	public void setFireUp(int fireUp) {
		this.fireUp = fireUp;
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

	//public Inventory getInventory() {
	//	return this.inventory;
	//}
	
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


	public boolean isUnderground() {
		return underground;
	}


public void setUnderground(boolean underground) {
		this.underground = underground;
	}


	public int getUgTime() {
		return ugTime;
	}


	public void setUgTime(int ugTime) {
		this.ugTime = ugTime;
	}
	
}
