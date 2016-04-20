package Model;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import PowerUp.Pow_BombUp;
import PowerUp.Pow_Clog;
import PowerUp.Pow_DangerBomb;
import PowerUp.Pow_FireDown;
import PowerUp.Pow_FireUp;
import PowerUp.Pow_Heart;
import PowerUp.Pow_MoleBomb;
import PowerUp.Pow_Skate;
import PowerUp.PowerUp;


public class GameModel {
	/**Modele du jeu.
	 * Gere les donees recues du controleur et met a jour la vue.
	 * Gere aussi la dynamique du jeu, nottament les explosions.
	 */
	
	private int scaleX;
	private int scaleY;
	private ArrayList<Integer> avoidX; private ArrayList<Integer> boundsX;
	private ArrayList<Integer> avoidY; private ArrayList<Integer> boundsY;
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Ennemi> ennemis = new ArrayList<Ennemi>();
	private ArrayList<PowerUp> allPowerUps = new ArrayList<PowerUp>();
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	
	private ArrayList<MoleHole> holes = new ArrayList<MoleHole>();
	
	private Boolean over; //Si le jeu est fini ou pas.
	private int winner; //Le joueur qui a gagne, 0 si egalite.
	
	
	//----------------------------------------------------------
	
	
	public GameModel(int scX,int scY,int playerNum) {
    	scaleX = scX; scaleY = scY;
    	over = false; winner = 0;
    	avoidX = new ArrayList<Integer>(Arrays.asList(0,1,scaleX-2,scaleX-1));
    	avoidY = new ArrayList<Integer>(Arrays.asList(0,1,scaleY-2,scaleY-1));
    	boundsX = new ArrayList<Integer>(Arrays.asList(-1, scaleX));
    	boundsY = new ArrayList<Integer>(Arrays.asList(-1, scaleY));
    	layWalls();
    	for(int i=1; i<=playerNum; i++){
    		players.add(new Player(i, scX, scY));
    	}
    }
	
	
	
	public void layWalls(){
		/**Place les murs dans la grille, suivant certaines contraintes:
		 * MURS SOLIDES: cases aux deux coordonees impaires
		 * MURS CASSABLES: cases aleatoires 2/3 fois, sauf aux coins.
		 */
		for(int i=-1; i<=scaleX; i++){	for(int j=-1; j<=scaleY; j++){
			if( boundsX.contains(i) || boundsY.contains(j)){
				walls.add(new Wall(i, j, true));

			}
			else if((i%2 == 1 && j%2 == 1)){
				Random Rd = new Random();
				if(Rd.nextInt(15)==0){  //blocs incassables ==> ennemis
				ennemis.add(new Ennemi(2, i, j));
					//walls.add(new Wall(i, j, true));	
			}
			else if(!avoidX.contains(i) || !avoidY.contains(j)){
				Random rd = new Random();
				if((rd.nextInt(10)==0)){				//Met des blocs cassables 2 fois sur 3
					walls.add(new Wall(i, j, false));
				}
			}
			}}
	}}

	
	
	public void layPowerUp(int pX, int pY){
		/**Place aleatoirement un powerup aux coordonees specifiees. 
		 * le type de powerup est aussi choisi aleatoirement.
		 */
		Random rd = new Random();
		if((rd.nextInt(4)==0)){				//Met des powerup 1 fois sur 4
			PowerUp newPow;
			int diceRoll= rd.nextInt(16);
			if(diceRoll < 4){
				newPow = new Pow_FireUp(pX, pY); //FireUp 1/4
			}else if(diceRoll < 8){
				newPow = new Pow_BombUp(pX, pY); //BombUp 1/4
			}else if(diceRoll < 10){
				newPow = new Pow_Heart(pX, pY); //Heart 1/8
			}else if(diceRoll < 12){
				newPow = new Pow_Skate(pX, pY); //Skate 1/8
			}else if(diceRoll < 13){
				newPow = new Pow_FireDown(pX, pY); //FireDown 1/16
			}else if(diceRoll < 14){
				newPow = new Pow_Clog(pX, pY); //Clog 1/16
			}else if(diceRoll < 15){
				newPow = new Pow_DangerBomb(pX, pY); //D-Bomb 1/16
			}else{
				newPow = new Pow_MoleBomb(pX, pY); //M-Bomb 1/16
			}allPowerUps.add(newPow);
		}
	}
	
	
	//----------------------------------------------------------
	
	
	public Boolean collisionCheck(int x,int y, int id, Boolean underground){
		/**Gere les collisions des joueurs avec les murs et les bombes.
		 * Si le joueur vient de poser une bombe, il peut la traverser.
		 * Si le joueur est sous-terre, il n'est bloque que par les murs solides.
		 */
		Rectangle playerBox = new Rectangle(x+3, y+7, 26, 23);
		Boolean collides = false;
		for (int i=0; i<walls.size();i++){
			Wall w = walls.get(i);
			if(!underground || w.getSolid()){
				Rectangle wallBox = new Rectangle(w.getPosX()*32, w.getPosY()*32, 32, 32);
				if(playerBox.intersects(wallBox)){collides = true;}
			}
		}
		for(int i=0; i<players.size(); i++){
			for(int j=0; j<players.get(i).getSpareBombs().size(); j++){
				Bomb b = players.get(i).getSpareBombs().get(j);
				if(b.getActive()){
					Rectangle bombBox = new Rectangle(b.getPosX()*32, b.getPosY()*32, 32, 32);
					if(!(i+1==id && b.getJustLaid()) && playerBox.intersects(bombBox)){collides = true;}
					else if(i+1==id && b.getJustLaid() && !playerBox.intersects(bombBox)){b.setJustLaid(false);}
				}
			}
		}
		return collides;
	}
	
	public Boolean collisionCheck(int x,int y, int id){
		/**Gere les collisions des joueurs avec les murs et les bombes.
		 * Si le joueur vient de poser une bombe, il peut la traverser.
		 * Pas d'exception pour les joueurs sous terre.
		 */
		Rectangle playerBox = new Rectangle(x+3, y+7, 26, 23);
		Boolean collides = false;
		for (int i=0; i<walls.size();i++){
			Wall w = walls.get(i);
			Rectangle wallBox = new Rectangle(w.getPosX()*32, w.getPosY()*32, 32, 32);
			if(playerBox.intersects(wallBox)){collides = true;}
		}
		for(int i=0; i<players.size(); i++){
			for(int j=0; j<players.get(i).getSpareBombs().size(); j++){
				Bomb b = players.get(i).getSpareBombs().get(j);
				if(b.getActive()){
					Rectangle bombBox = new Rectangle(b.getPosX()*32, b.getPosY()*32, 32, 32);
					if(!(i+1==id && b.getJustLaid()) && playerBox.intersects(bombBox)){
						System.out.println(!(i+1==id && b.getJustLaid()));
						collides = true;
					}
					else if(i+1==id && b.getJustLaid() && !playerBox.intersects(bombBox)){
						System.out.println("Je viens de la laisser");b.setJustLaid(false);
					}
				}
			}
		}
		for(int i=0; i<ennemis.size(); i++){
			for(int j=0; j<ennemis.get(i).getSpareBombs().size(); j++){
				Bomb b = ennemis.get(i).getSpareBombs().get(j);
				if(b.getActive()){
					Rectangle bombBox = new Rectangle(b.getPosX()*32, b.getPosY()*32, 32, 32);
					if(!(i+1==id && b.getJustLaid()) && playerBox.intersects(bombBox)){
						System.out.println(!(i+1==id && b.getJustLaid()));
						collides = true;
					}
				}
			}
		}
		return collides;
	}
	
	
	
	public void powerUpCheck(){
		/**Gere les collisions des joueurs (sur terre) avec les powerups.
		 * Lorsqu'un joueur en touche un (actif), il s'ajoute a la liste de powerups
		 * propre au joueur, il devient inactif, et il execute une action sur lui.
		 */
		for(int i=0; i<players.size(); i++){
			Player pl = players.get(i);
			if(!pl.isUnderground()){
				Rectangle playerBox = new Rectangle(pl.getPosX()+3, pl.getPosY()+7, 26, 23);
				for(int j=0; j<allPowerUps.size(); j++){
					PowerUp pow = allPowerUps.get(j);
					Rectangle powBox = new Rectangle(pow.getPosX()*32, pow.getPosY()*32, 32, 32);
					if(playerBox.intersects(powBox) && pow.getActive()){
						pow.setActive(false);
						pl.getMyPowerUps().add(pow); pow.affectPlayer(pl);
					}
				}
			}
		}
	}
	
	
	
	public void undergroundCheck(){
		/**Lorsqu'un joueur touche le bord inferieur d'un trou, il
		 * est sous-terre. Lorsqu'un joueur sous-terre touche le
		 * bord superieur d'un trou, il ressort a la surface.
		 */
		for(int i=0; i<players.size(); i++){
			Player pl = players.get(i);
			Rectangle playerBox = new Rectangle(pl.getPosX()+3, pl.getPosY()+7, 26, 23);
			if(pl.isUnderground()){
				for(int j=0; j<holes.size() && pl.isUnderground(); j++){
					MoleHole h = holes.get(j);
					Rectangle hBox = new Rectangle(h.getPosX()*32, h.getPosY()*32, 32, 2);
					if(playerBox.intersects(hBox)){pl.outOfGround(h.getPosX(), h.getPosY());}
				}
			}else{
				for(int j=0; j<holes.size() && !pl.isUnderground(); j++){
					MoleHole h = holes.get(j);
					Rectangle hBox = new Rectangle(h.getPosX()*32, h.getPosY()*32 + 30, 32, 2);
					if(playerBox.intersects(hBox)){pl.intoGround();}
				}
			}
		}
	}
	
	
	
	public void hitPlayersCheck(){
		/**Cette fonction gere la survie des joueurs:
		 * 1) Si le joueur est mort, on declenche une animation.
		 * 2) Si l'animation  de mort est finie, on pose les powerups du joueur
		 *    aleatoirement, puis il est efface de la liste de joueurs.
		 * 3) Si le joueur est vivant (et sur terre), et s'il est pris dans une
		 *    explosion, il perd une vie, et reste invulnerable jusqu'a ce qu'il
		 *    sorte de l'explosion.
		 */
		for(int i=0; i<players.size(); i++){
			Player pl = players.get(i);
			
			//JOUEUR MORT
			if(pl.getLives() == 0 && pl.getDeathPose() == 0){pl.deathTimer();}
			
			//JOUEUR MORT - ANIMATION FINIE
			else if(pl.getDeathPose() == 2){
				for(int j=0; j<pl.getMyPowerUps().size(); j++){
					PowerUp pow = pl.getMyPowerUps().get(j);
					pow.setActive(true);
					Random rd = new Random();
					pow.setPosX(rd.nextInt((scaleX-1)/2 - 1)*2 + 2);
					pow.setPosY(rd.nextInt((scaleY-1)/2 - 1)*2 + 2);
				}
				powerUpInWallCheck(pl.getMyPowerUps());
				pl.setDeathPose(3);
				
			//JOUEUR VIVANT (ET SUR TERRE)
			}else if(!pl.isUnderground()){
				Boolean notColliding = true;
				Rectangle playerBox = new Rectangle(pl.getPosX()+3, pl.getPosY()+7, 26, 23);
				for(int j=0; j<explosions.size(); j++){
					for(int k=0; k<explosions.get(j).getBits().size(); k++){
						ExplBits bit = explosions.get(j).getBits().get(k);
						Rectangle explBox = new Rectangle(bit.getPosX()*32, bit.getPosY()*32, 32, 32);
						if(playerBox.intersects(explBox)){
							notColliding = false;
							if(!pl.getHit()){pl.setHit(true); pl.setLives(pl.getLives() - 1);}
						}
					}
				}
				if(notColliding && pl.getHit()){pl.setHit(false);}
			}
		}
	}
	
	
	
	public void hitWallsCheck(){
		/**Enleve les murs casses et met un powerup aleatoirement
		 */
		for(int i=0; i<walls.size(); i++){
			Wall w = walls.get(i);
			if(w.getBreaking() == 2){walls.remove(i); layPowerUp(w.getPosX(), w.getPosY());}
		}
	}
	
	
	//----------------------------------------------------------
	
	
	public void powerUpInWallCheck(ArrayList<PowerUp> powList){
		/**Cette fonction cherche les eventuels murs cassables sur desquels
		 * il y a des powerups, et les enleve.
		 */
		for(int i=0; i<powList.size(); i++){
			PowerUp pow = powList.get(i);
			for(int j=0; j<walls.size(); j++){
				Wall w = walls.get(j);
				if(w.getPosX() == pow.getPosX() && w.getPosY() == pow.getPosY()){walls.remove(j);}
			}
		}
	}
	
	
	
	public void holeCheck(){
		/**Enleve les trous apres un temps.
		 */
		for(int i=0; i<holes.size(); i++){
			MoleHole h = holes.get(i);
			if(!h.getActive()){holes.remove(h);}
		}
	}
	
	
	
	public void explosionCheck(){
		/**Cette fonction a quatre parties:
		 * 1) On cherche des bombes pretes a exploser et on y cree des explosions.
		 * 2) On cherche des explosions epuisees et on les enleve.
		 * 3) On cherche les murs cassables/powerups touches par les explosions et on les enleve.
		 * 4) On cherche les bombes traversees par les explosions et on y cree des explosions.
		 */
		
		//PARTIE 1
		for(int i=0; i<players.size(); i++){
			for(int j=0; j<players.get(i).getSpareBombs().size(); j++){
				Bomb b = players.get(i).getSpareBombs().get(j);
				if(b.getCanExplode()){
					explosions.add(new Explosion(b.getPosX(), b.getPosY(), b.getRange(), walls, b.getEffect()));
					b.setCanExplode(false); b.setEffect("N");  //La bombe ne peut plus exploser (+ redevient normale)
				}
			}
		}
		for(int i=0; i<explosions.size(); i++){
			Explosion exp = explosions.get(i);
			
			//PARTIE 2
			if(exp.getBurnedOut()){
				if(exp.getMole()){holes.add(new MoleHole(exp.getPosX(), exp.getPosY()));} //Ajoute un trou si bombe-taupe.
				explosions.remove(exp);
			}else{ for(int j=0; j<exp.getBits().size(); j++){
				ExplBits bit = exp.getBits().get(j);
				
				//PARTIE 3
				if(!bit.getOnEnd()){ //Pour que le morceau casse des objets, il ne peut pas etre au bout.
					for(int k=0; k<walls.size(); k++){
						Wall w = walls.get(k);
						if(w.getBreaking() == 0 && breakCheck(w, bit.getPosX(), bit.getPosY(), bit.getDir())){w.deathTimer();}
					}for(int k=0; k<allPowerUps.size(); k++){
						PowerUp pow = allPowerUps.get(k);
						if(breakCheck(pow, bit.getPosX(), bit.getPosY(), bit.getDir())){allPowerUps.remove(k);}
					}
				}
				
				//PARTIE 4
				for(int k=0; k<players.size(); k++){
					for(int l=0; l<players.get(k).getSpareBombs().size(); l++){
						Bomb b = players.get(k).getSpareBombs().get(l);
						if(b.getActive() && b.getPosX() == bit.getPosX() && b.getPosY() == bit.getPosY()){
							b.explode();
						}
					}
				}
			}}
		}
	}


	//----------------------------------------------------------


	public Boolean breakCheck(Wall w, int x, int y, String d){
		/**Casse un mur s'il touche une certaine position*/
		Boolean mainCheck = false;
		if(!w.getSolid()){
			Boolean check1 = (w.getPosX()+1 == x && w.getPosY() == y) && (d.equals("L") || d.equals("C"));
			Boolean check2 = (w.getPosX()-1 == x && w.getPosY() == y) && (d.equals("R") || d.equals("C"));
			Boolean check3 = (w.getPosX() == x && w.getPosY()+1 == y) && (d.equals("D") || d.equals("C"));
			Boolean check4 = (w.getPosX() == x && w.getPosY()-1 == y) && (d.equals("U") || d.equals("C"));
			if(check1 || check2 || check3 || check4){mainCheck = true;}
		}return mainCheck;
	}



	public Boolean breakCheck(PowerUp pow, int x, int y, String d){
		/**Casse un powerup s'il touche une certaine position*/
		Boolean mainCheck = false;
		if(pow.getActive()){
			Boolean check1 = (pow.getPosX()+1 == x && pow.getPosY() == y) && (d.equals("L") || d.equals("C"));
			Boolean check2 = (pow.getPosX()-1 == x && pow.getPosY() == y) && (d.equals("R") || d.equals("C"));
			Boolean check3 = (pow.getPosX() == x && pow.getPosY()+1 == y) && (d.equals("D") || d.equals("C"));
			Boolean check4 = (pow.getPosX() == x && pow.getPosY()-1 == y) && (d.equals("U") || d.equals("C"));
			if(check1 || check2 || check3 || check4){mainCheck = true;}
		}return mainCheck;
	}


	//----------------------------------------------------------


	public void endGameCheck(){
		/**Cette fonction se charge de finir le jeu lorsque au plus un joueur
		 * reste encore en vie.
		 */
		int checkWinner = 0;
		over = true;
		for(int i=0; i<getPlayers().size(); i++){
			if(getPlayers().get(i).getDeathPose() != 3){
				if(checkWinner == 0){checkWinner = i+1;}
				else{over = false;}
			}
		}if(over){winner = checkWinner;}
	}


	//----------------------------------------------------------


	public ArrayList<Player> getPlayers() {
		return players;
	}



	public void setEnnemis(ArrayList<Ennemi> ennemis) {
		this.ennemis = ennemis;
	}

	public ArrayList<Ennemi> getEnnemis() {
		return ennemis;
	}



	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}


	public ArrayList<Wall> getWalls() {
		return walls;
	}



	public void setWalls(ArrayList<Wall> walls) {
		this.walls = walls;
	}



	public ArrayList<PowerUp> getAllPowerUps() {
		return allPowerUps;
	}



	public void setAllPowerUps(ArrayList<PowerUp> allPowerUps) {
		this.allPowerUps = allPowerUps;
	}



	public ArrayList<Explosion> getExplosions() {
		return explosions;
	}



	public void setExplosions(ArrayList<Explosion> explosions) {
		this.explosions = explosions;
	}



	public ArrayList<MoleHole> getHoles() {
		return holes;
	}



	public void setHoles(ArrayList<MoleHole> holes) {
		this.holes = holes;
	}



	public Boolean getOver() {
		return over;
	}



	public void setOver(Boolean over) {
		this.over = over;
	}



	public int getWinner() {
		return winner;
	}



	public void setWinner(int winner) {
		this.winner = winner;
	}

}
