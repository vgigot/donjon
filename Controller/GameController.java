package Controller;


/* Credit a zetcode.com pour des tutoriels sur la creation de jeux en java.
 * http://zetcode.com/tutorials/javagamestutorial/ */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import Board.GameBoard;
import Board.MenuBoard;
import Model.Enemy;
import Model.Player;
import Model.GameModel;
import Model.MoleHole;

public class GameController extends JFrame{
	/** Controleur du jeu.
	 * Sert en meme temps de JFrame.
	 */
	
	private MenuBoard menu;
	private GameBoard board;
	
	private GameModel model;
	private ArrayList<Listener> listeners = new ArrayList<Listener>(); //Listeners du jeu
	private ActionListener menuListener; //Listener du menu
	
	private ActionListener repeatThread; //Thread qui s'execute toutes les 7 ms
	private ActionListener backToMenu; //Thread qui relance le menu apres la fin
	private Timer rTime; private Timer mTime;
	
	private int scaleX;
	private int scaleY;
	private int playerNum;
	private Boolean playing;
	
	private HashMap<Integer, GameBoard> roomBoards = new HashMap<Integer, GameBoard>();
	private HashMap<Integer, GameModel> roomModels = new HashMap<Integer, GameModel>();
	private int roomX = 0;
	private int roomY = 0;
	
	private int frequencyCounter = 0;
	
	
	//----------------------------------------------------------
	
	
	public GameController() {
		setTitle("Dungeon Crawler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenu();
	}
	
	
	//----------------------------------------------------------
	
	
	public void mainMenu(){
		/**Cette fonction gere le menu au debut du jeu, nottament il gere
		 * les actions du joueur dans celui-ci, par moyen d'un listener.
		 */
		if(board!=null){remove(board);}
		scaleX = 15; scaleY = 13; playerNum = 2;
		menu = new MenuBoard(scaleX,scaleY); add(menu);
		pack(); setLocationRelativeTo(null); // Met a la taille du board et au centre de l'ecran.
		
		if(menuListener==null){menuListener = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("StartGame")){startGame();}
				
				if(e.getActionCommand().equals("ScaleXUp") && scaleX<27){
					scaleX+=2; menu.displayScX(scaleX); menu.repaint();
				}if(e.getActionCommand().equals("ScaleXDown") && scaleX>7){
					scaleX-=2; menu.displayScX(scaleX); menu.repaint();
				}
				
				if(e.getActionCommand().equals("ScaleYUp") && scaleY<21){
					scaleY+=2; menu.displayScY(scaleY); menu.repaint();
				}if(e.getActionCommand().equals("ScaleYDown") && scaleY>7){
					scaleY-=2; menu.displayScY(scaleY); menu.repaint();
				}
				
				if(e.getActionCommand().equals("PlayerUp") && playerNum<4){
					playerNum+=1; menu.displayPlayers(playerNum); menu.repaint();
				}if(e.getActionCommand().equals("PlayerDown") && playerNum>1){
					playerNum-=1; menu.displayPlayers(playerNum); menu.repaint();
				}
			}
		};}
		menu.addMenuListener(menuListener);
		this.requestFocusInWindow();
	}
	
	
	//----------------------------------------------------------
	
	
	public void startGame(){
		/**Quitte le menu et lance un nouveau jeu.
		 * Charge le modele et la vue d'un jeu, ainsi que les listeners de chaque joueur,
		 * puis lance un thread de rafraichissement toutes les 7 ms.
		 */
		if(model==null){
			remove(menu); playing=true;
			model = new GameModel(scaleX,scaleY,playerNum);
			board = new GameBoard(scaleX,scaleY,model);
			roomBoards.put(0, board);
			roomModels.put(0, model);
			model.setBoard(board);
			for(int i=0; i<model.getPlayers().size(); i++){
				Listener kl = new Listener(model.getPlayers().get(i));
				listeners.add(kl); this.addKeyListener(kl);
			}this.requestFocusInWindow();

			add(board);
			pack();setLocationRelativeTo(null); // Met a la taille du board et au centre de l'ecran.
		
			repeatThread = new ActionListener(){	//Le thread qu'on repete a chaque fois.
				public void actionPerformed(ActionEvent e) {
					rTime.stop(); if(model!=null){stepCycle(); rTime.start();}
				}
			};	rTime = new Timer(7, repeatThread); //On repete le thread toutes les 7 ms.
			rTime.start();
		}
	}
	
	
	//----------------------------------------------------------
	
	
	public void changeRoom(String direction) {
		if (direction == "up") {
			roomY -= 1;
		}
		else if (direction == "down") {
			roomY += 1;
		}
		else if (direction == "left") {
			roomX -= 1;
		}
		else if (direction == "right") {
			roomX += 1;
		}
		int key = 500 * roomX + roomY;
		if (roomBoards.containsKey(key)) {
			model = roomModels.get(key);
			board = roomBoards.get(key);
		}
		else {
			model = new GameModel(scaleX,scaleY,playerNum, model.getPlayers());
			board = new GameBoard(scaleX,scaleY,model);
			roomModels.put(key, model);
			roomBoards.put(key, board);
		}
		model.setBoard(board);

		add(board);
		pack();setLocationRelativeTo(null);
	}
	
	public void stepCycle(){
		/**Le cycle répète à chaque step du jeu (toutes les 7 ms).*/
		movePlayers();
		model.explosionCheck(); model.holeCheck();
		model.hitPlayersCheck(); model.hitWallsCheck();
		model.powerUpCheck(); model.undergroundCheck();
		model.endGameCheck();
		board.repaint();
		if(model.getOver() && playing){
			playing = false;
			backToMenu = new ActionListener(){	//Le thread qu'on repete a chaque fois.
				public void actionPerformed(ActionEvent e) {mTime.stop(); model=null; listeners.clear(); mainMenu();}
			};	mTime = new Timer(4000, backToMenu); //On repete le thread toutes les 7 ms.
			mTime.start();
		}
	}
	
	
	
	public void movePlayers(){
		for(int i=0; i<listeners.size(); i++){
			Listener kl = listeners.get(i);
			if(!model.getOver() && kl.getPlayer().getDeathPose() == 0){ //Si le joueur n'est pas mort.
				int x = kl.getPlayer().getPosX(); int y = kl.getPlayer().getPosY();
				int sp = kl.getPlayer().getSpeed();
				Boolean ug = kl.getPlayer().isUnderground();
				if(kl.getPressed().equals("R") && !model.collisionCheck(x+sp,y,i+1,ug)){
					kl.getPlayer().setPosX(x+sp);
				}else if(kl.getPressed().equals("L") && !model.collisionCheck(x-sp,y,i+1,ug)){
					kl.getPlayer().setPosX(x-sp);
				}else if(kl.getPressed().equals("U") && !model.collisionCheck(x,y-sp,i+1,ug)){
					kl.getPlayer().setPosY(y-sp);
				}else if(kl.getPressed().equals("D") && !model.collisionCheck(x,y+sp,i+1,ug)){
					kl.getPlayer().setPosY(y+sp);
				}else if(kl.getPressed().equals("I")){
					kl.getPlayer().inventory(); kl.reset();
				}else if(kl.getPressed().equals("LL") && !model.collisionCheck(x,y+sp,i+1,ug)){
					kl.getPlayer().getInventory().moveLeft(); kl.reset();
				}else if(kl.getPressed().equals("LR") && !model.collisionCheck(x,y+sp,i+1,ug)){
					kl.getPlayer().getInventory().moveRight(); kl.reset();
				}else if(kl.getPressed().equals("UU")){
					kl.getPlayer().useItem(); kl.reset();
				}else if(kl.getPressed().equals("SW")){
					kl.getPlayer().setAtkState(true); kl.reset();
				}
				model.swordCollision(x, y);
				model.checkDoors(x, y, kl.getPlayer(), this);
				if(kl.getAction()){
					kl.setAction(false);
					if(ug && !model.collisionCheck(x,y,i+1)){
						MoleHole newH = new MoleHole(kl.getPlayer().gridPosition(x), kl.getPlayer().gridPosition(y));
						model.getHoles().add(newH);
						kl.getPlayer().outOfGround(newH.getPosX(), newH.getPosY());
					}else if(!ug){kl.getPlayer().placeBomb();}
				}
			}
			for(int b=0; b<model.getEnemi().size(); b++){
				Enemy en = model.getEnemi().get(b);
				en.updateState(kl.getPlayer().getPosX(), kl.getPlayer().getPosY(), kl.getPlayer());
			}
		}
		this.frequencyCounter += 1;
		if (this.frequencyCounter == 6) {
			for(int i=0; i<model.getEnemi().size(); i++){
				Enemy en = model.getEnemi().get(i);
				if (model.getEnemi().get(i).getDeathPose() == 0) {				
					int dx = 0; int dy = 0;
					String state = en.getState();
					if (state == "depl") {
						int speed = en.getSpeed();
						String direction = en.getDirection();
						if (direction == "up") { dx = 0; dy = -speed; }
						else if (direction == "down") { dx = 0; dy = speed; }
						else if (direction == "left") { dx = -speed; dy = 0; }
						else if (direction == "right") { dx = speed; dy = 0; }
						
						if (!model.collisionCheck(en.getPosX()+dx,en.getPosY()+dy,1,false)) {
							en.setPosX(en.getPosX()+dx);
							en.setPosY(en.getPosY()+dy);
						}
					}
				}
			}
		this.frequencyCounter = 0;
		}
	}	
	
	//----------------------------------------------------------
	
	
	public static void main(String[] args) {

		final GameController gc = new GameController();
		Runnable startThread = new Runnable() {		//Le thread qu'on execute tout au debut.
			public void run() {
				gc.setVisible(true);
			}
		};	EventQueue.invokeLater(startThread); //On invoque le thread au debut.
	}
   
}
