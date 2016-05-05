package Board;

import Model.GameModel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Model.Bomb;
import Model.ExplBits;
import Model.Explosion;
import Model.MoleHole;
import Model.Player;
import Model.Wall;
import Model.Enemy;
import PowerUp.Pow_BombUp;
import PowerUp.Pow_Clog;
import PowerUp.Pow_DangerBomb;
import PowerUp.Pow_FireDown;
import PowerUp.Pow_FireUp;
import PowerUp.Pow_Heart;
import PowerUp.Pow_MoleBomb;
import PowerUp.Pow_Skate;
import PowerUp.PowerUp;

import Model.Inventory;


public class GameBoard extends JPanel {
	/**
	 * 
	 */
	/** Vue (tableau) du jeu.
	 * L'interface graphique du jeu. S'actualise a partir de son modele.
	 * Sert de JPanel dans le JFrame du controleur.
	 */
	
	private int scaleX;
	private int scaleY;
	private GameModel model;
	
	private Image groundSpr;
	private ArrayList<Image> bomberSpr = new ArrayList<Image>();
	private ArrayList<Image> bomberDeadSpr = new ArrayList<Image>();
	private ArrayList<Image> EnSpr = new ArrayList<Image>();
	private ArrayList<Image> EnDeadSpr = new ArrayList<Image>();
	private ArrayList<Image> PlayerSpr = new ArrayList<Image>();
	private ArrayList<Image> wallSpr = new ArrayList<Image>();
	private ArrayList<Image> powSpr = new ArrayList<Image>();
	private Image bombSpr; private Image bombUnacSpr;
	private Image explosionSpr; private Image explosionEndSpr;
	
	private Image holeSpr;
	private ArrayList<Image> arrowSpr = new ArrayList<Image>();
	
	private ArrayList<Image> victory = new ArrayList<Image>();
	
	private Inventory inventory = null;
	
	
	//----------------------------------------------------------
	
	
	 public GameBoard(int scX,int scY, GameModel gm) {
    	scaleX = scX+2; scaleY = scY+2;
    	model = gm;
    	getAllSprites();
    	setPreferredSize(new Dimension(scaleX*32, scaleY*32));
    }

    
    
	private void getAllSprites() {
		groundSpr = getSprite("Ground.png");
		
		victory.add(getSprite("victory0.png"));
		for(int i=1; i<=model.getPlayers().size(); i++){
			bomberSpr.add(getSprite("bomber"+Integer.toString(i)+".png"));
			bomberDeadSpr.add(getSprite("bomberdead"+Integer.toString(i)+".png"));
			victory.add(getSprite("victory"+Integer.toString(i)+".png"));
		}
		
		wallSpr.add(getSprite("wall.jpg")); wallSpr.add(getSprite("Box.png")); wallSpr.add(getSprite("NoBox.png"));
		
		for(int i=1; i<=model.getEnemy().size(); i++){
			EnSpr.add(getSprite("enemy.png"));
			EnDeadSpr.add(getSprite("enemydead.png"));
		}
		PlayerSpr.add(getSprite("Knight.png"));
		powSpr.add(getSprite("fireUp.png")); powSpr.add(getSprite("bombUp.png"));
		powSpr.add(getSprite("heart.png")); powSpr.add(getSprite("skate.png"));
		powSpr.add(getSprite("fireDown.png")); powSpr.add(getSprite("clog.png"));
		powSpr.add(getSprite("dangerBomb.png")); powSpr.add(getSprite("moleBomb.png"));
		
		bombSpr = getSprite("bomb.png"); bombUnacSpr = getSprite("bombunactive.png");
		explosionSpr = getSprite("explosion.png"); explosionEndSpr = getSprite("explosionend.png");
		
		holeSpr = getSprite("hole.png");
		for(int i=0; i<=6; i++){
			arrowSpr.add(getSprite("arrow"+String.valueOf(i)+".png"));
		}
		
	}

	
	
	private Image getSprite(String file) {
    	/** Charge un des sprites */
		ImageIcon newSprite = new ImageIcon(this.getClass().getResource("/"+file));
		Image sprite = newSprite.getImage();
		return sprite;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	
	//----------------------------------------------------------
	
	public void drawPowerUp(Graphics g, PowerUp pow) {
		drawPowerUp(g, pow, pow.getPosX()*32+32, pow.getPosY()*32+32);
	}
	
	public void drawPowerUp(Graphics g, PowerUp pow, int X, int Y) {
		if(pow instanceof Pow_FireUp){
			g.drawImage(powSpr.get(0), X, Y, null);
		}else if(pow instanceof Pow_BombUp){
			g.drawImage(powSpr.get(1), X, Y, null);
		}else if(pow instanceof Pow_Heart){
			g.drawImage(powSpr.get(2), X, Y, null);
		}else if(pow instanceof Pow_Skate){
			g.drawImage(powSpr.get(3), X, Y, null);
		}else if(pow instanceof Pow_FireDown){
			g.drawImage(powSpr.get(4), X, Y, null);
		}else if(pow instanceof Pow_Clog){
			g.drawImage(powSpr.get(5), X, Y, null);
		}else if(pow instanceof Pow_DangerBomb){
			g.drawImage(powSpr.get(6), X, Y, null);
		}else if(pow instanceof Pow_MoleBomb){
			g.drawImage(powSpr.get(7), X, Y, null);
		}
	}
	
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		// Remplit le board de sprites du sol.
		for(int i=-1; i<scaleX-1; i++){ 	for(int j=-1; j<scaleY-1; j++){
			g.drawImage(groundSpr, i*32+32, j*32+32, null);
		}	}
		
		// Affiche les murs.
		for(int i=0; i<model.getWalls().size(); i++){
			Wall w = model.getWalls().get(i);
			if(w.getSolid()){
				g.drawImage(wallSpr.get(0), w.getPosX()*32+32, w.getPosY()*32+32, null); // Affiche les murs solides
			}else if(w.getBreaking() == 0){
				g.drawImage(wallSpr.get(1), w.getPosX()*32+32, w.getPosY()*32+32, null); // Affiche les murs non solides
			}else{
				g.drawImage(wallSpr.get(2), w.getPosX()*32+32, w.getPosY()*32+32, null); // Affiche les murs en train de casser
			}
		}
		
		// Affiche les trous.
		for(int i=0; i<model.getHoles().size(); i++){
			MoleHole h = model.getHoles().get(i);
			g.drawImage(holeSpr, h.getPosX()*32+32, h.getPosY()*32+32, null);
		}
		
		// Affiche les powerups actifs.
		for(int i=0; i<model.getAllPowerUps().size(); i++){
			PowerUp pow = model.getAllPowerUps().get(i);
			if(pow.getActive()){
				drawPowerUp(g, pow);
			}
		}

		// Affiche les bombes actives.
		for(int i=0; i<model.getPlayers().size(); i++){
			for(int j=0; j<model.getPlayers().get(i).getSpareBombs().size(); j++){
				Bomb b = model.getPlayers().get(i).getSpareBombs().get(j);
				if(b.getActive()){
					g.drawImage(bombSpr, b.getPosX()*32+32, b.getPosY()*32+32, null);
				}/*else{
					g.drawImage(bombUnacSpr, b.getPosX()*32+32, b.getPosY()*32+32, null);
				}*/
			}
		}
		
		// Affiche les explosions.
		for(int i=0; i<model.getExplosions().size(); i++){
			Explosion ex = model.getExplosions().get(i);
			for(int j=0; j<ex.getBits().size(); j++){
				ExplBits bit = ex.getBits().get(j);
				if(bit.getOnEnd()){
					g.drawImage(explosionEndSpr, bit.getPosX()*32+32, bit.getPosY()*32+32, null);
				}else{
					g.drawImage(explosionSpr, bit.getPosX()*32+32, bit.getPosY()*32+32, null);
				}
			}
		}
		
		// Affiche les joueurs.
		for(int i=0; i<model.getPlayers().size(); i++){
			Player pl = model.getPlayers().get(i);
			if(pl.isUnderground()){
				if(pl.getDeathPose() <= 2){
					g.drawImage(arrowSpr.get(pl.getUgTime()), pl.getPosX()+32, pl.getPosY(), null);
				}
			}else{
				if(pl.getDeathPose() == 0){
					//g.drawImage(bomberSpr.get(pl.getId()-1), pl.getPosX()+32, pl.getPosY(), null);
					g.drawImage(PlayerSpr.get(0), pl.getPosX()+34, pl.getPosY()+25, null);
				}else if(pl.getDeathPose() == 1){
					g.drawImage(bomberDeadSpr.get(pl.getId()-1), pl.getPosX()+32, pl.getPosY(), null);
				}
			}
		}
		
		// Affiche les ennemis.
				for(int i=0; i<model.getEnemy().size(); i++){
					Enemy en = model.getEnemy().get(i);
					//if(pl.isUnderground()){
						//if(pl.getDeathPose() <= 2){
						//	g.drawImage(arrowSpr.get(pl.getUgTime()), pl.getPosX()+32, pl.getPosY(), null);
						//}
					//}else{
						if(en.getDeathPose() == 0){
							g.drawImage(EnSpr.get(0), en.getPosX()+32, en.getPosY()+20, null);
						}else if(en.getDeathPose() == 1){
							g.drawImage(EnDeadSpr.get(0), en.getPosX()+32, en.getPosY(), null);
						}
					}
				
		
				
		
		// Affiche l'ecran de victoire a la fin du jeu.
		if(model.getOver()){
			g.drawImage(victory.get(model.getWinner()), scaleX*32/2 - 112, scaleY*32/2 - 112, null);
		}
		
		// Affiche l'ecran d'inventaire
		if (this.inventory != null) {
			int menu_X = 120;
			int menu_Y = 100;
			int X, Y;
			PowerUp pow;
			Image sprite = getSprite("inventory.png");
			g.drawImage(sprite, menu_X, menu_Y, null);
			int n = this.inventory.getStorage().size();
			
			int cellSize = 73;

			for (int j = 0; j < n; j++){
				pow = this.inventory.getStorage().get(j);
				
				
				Y = menu_Y + 48 + (j / 3) * cellSize;
				X = menu_X + 53 + (j % 3) * cellSize;
				
				drawPowerUp(g, pow, X, Y);
			}
			int deph = 56;
			if (this.inventory.getCurrentIndex() < 1) {
				
				X = menu_X + deph; Y = menu_Y + deph;
			}
			else {
				Y = menu_Y + deph + ((this.inventory.getCurrentIndex()) / 3) * cellSize;
				X = menu_X + deph + ((this.inventory.getCurrentIndex()) % 3) * cellSize;
			}
			
			sprite = getSprite("cursor.png");
			g.drawImage(sprite, X, Y, null);
		}
	
	}
}
