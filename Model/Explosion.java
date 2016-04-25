package Model;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;


public class Explosion {

	private int posX; private int posY; //Position en carrés de 32x32
	private int range;
	private Boolean burnedOut; //Vrai lorsque l'explosion a fini.
	private ArrayList<ExplBits> bits = new ArrayList<ExplBits>(); //L'explosion est divisee en morceaux
	
	private ArrayList<Wall> refWalls; //La liste de murs prise du modele.
	private ArrayList<Boolean> broke = new ArrayList<Boolean>(4); //Si on a casse deja un mur dans les 4 directions

	private ActionListener explosionThread;	//Enleve l'explosion apres un temps
	private Timer time;
	
	private Boolean mole; //Si l'explosion vient d'une bombe-taupe, et donc place un trou.
	
	
	//----------------------------------------------------------
	
	
	public Explosion(int pX, int pY, int rg, ArrayList<Wall> w, String effect){
		posX = pX; posY = pY;
		range = rg; burnedOut = false;
		refWalls = w;
		
		//Place chaque morceau d'explosion.
		if(effect.equals("D")){mole = false; dangerBits();}
		else if(effect.equals("M")){mole = true; placeBits();}
		else{mole = false; placeBits();}

		explosionThread = new ActionListener(){
			public void actionPerformed(ActionEvent e) {toRemove();}
		};
		time = new Timer(1000, explosionThread); time.start();
	}
	
	
	//----------------------------------------------------------
	
	
	public void placeBits(){
		/**Place les morceaux d'explosion,
		 * il y a un "for" pour chaque direction pour deux raisons:
		 * pour iterer jusqu'au prochain bloc incassable,
		 * et pour eventuellement mettre des sprites differents.
		 */
		bits.add(new ExplBits(posX, posY, "C"));
		int count = 0;
		for(int i=1; i<=range && checkForWall(posX+i, posY, 0); i++){
			bits.add(new ExplBits(posX+i, posY, "R")); count +=1;
			if(i==range){bits.get(count).setOnEnd(true);}
		}for(int i=1; i<=range && checkForWall(posX-i, posY, 1); i++){
			bits.add(new ExplBits(posX-i, posY, "L")); count +=1;
			if(i==range){bits.get(count).setOnEnd(true);}
		}for(int i=1; i<=range && checkForWall(posX, posY+i, 2); i++){
			bits.add(new ExplBits(posX, posY+i, "U")); count +=1;
			if(i==range){bits.get(count).setOnEnd(true);}
		}for(int i=1; i<=range && checkForWall(posX, posY-i, 3); i++){
			bits.add(new ExplBits(posX, posY-i, "D")); count +=1;
			if(i==range){bits.get(count).setOnEnd(true);}
		}
	}
	
	public void dangerBits(){
		/**Place les morceaux d'explosions issues de bombes dangereuses,
		 * il y a un "for" pour chaque direction pour deux raisons:
		 * pour iterer jusqu'au prochain bloc incassable,
		 * et pour eventuellement mettre des sprites differents.
		 */
		for(int i=-2; i<=2; i++){
			for(int j=-2; j<=2; j++){
				ExplBits newBit = new ExplBits(posX+i, posY+j, "C");
				if(i == -2 || i == 2 || j == -2 || j == 2){newBit.setOnEnd(true);}
				bits.add(newBit);
			}
		}
	}
	
	
	//----------------------------------------------------------
	
	
	public Boolean checkForWall(int x, int y, int brokeDir){
		Boolean check = true;
		for(int i=0; i<refWalls.size(); i++){
			Wall w = refWalls.get(i);
			if(w.getPosX() == x && w.getPosY() == y){
				check = false;
			}
		}
		return check;
	}
	
	
	//----------------------------------------------------------
	
	
	public void toRemove(){
		time.stop();
		burnedOut = true;
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


	public int getRange() {
		return range;
	}


	public void setRange(int range) {
		this.range = range;
	}


	public Boolean getBurnedOut() {
		return burnedOut;
	}


	public void setBurnedOut(Boolean burnedOut) {
		this.burnedOut = burnedOut;
	}


	public ArrayList<ExplBits> getBits() {
		return bits;
	}


	public void setBits(ArrayList<ExplBits> bits) {
		this.bits = bits;
	}


	public ArrayList<Wall> getRefWalls() {
		return refWalls;
	}


	public void setRefWalls(ArrayList<Wall> refWalls) {
		this.refWalls = refWalls;
	}


	public ArrayList<Boolean> getBroke() {
		return broke;
	}


	public void setBroke(ArrayList<Boolean> broke) {
		this.broke = broke;
	}


	public Boolean getMole() {
		return mole;
	}


	public void setMole(Boolean mole) {
		this.mole = mole;
	}
}
