package Model;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import PowerUp.PowerUp;
import Board.GameBoard;


public class Inventory {

	private ArrayList<PowerUp> storage;
	private int currentIndex;
	private boolean opened = false;
	private GameBoard board;
	
	public Inventory(GameBoard board){
		this.storage = new ArrayList<PowerUp>();
		this.currentIndex = 0;
		this.board = board;
	}
	
	public void add(PowerUp pow) {
		if (this.storage.size() < 9) {
			this.currentIndex += 1;
			this.storage.add(pow);
		}
	}
	
	public void useCurrentItem(Player player) {
		if (this.opened) {
			if (this.storage.size() != 0) {
				System.out.println("Taille storage : " + String.valueOf(this.storage.size()));
				System.out.println("Curseur : " + String.valueOf(this.currentIndex));
				PowerUp pow = this.storage.get(this.currentIndex);
				player.getMyPowerUps().add(pow);
				pow.affectPlayer(player);
				this.storage.remove(this.currentIndex);
				this.currentIndex = 0;
			}
		}
	}
	
	public void menu() {
		if (this.opened){ 
			this.board.setInventory(null);
			this.opened = false;
		}
		else {
			this.opened = true;
			this.board.setInventory(this);
		}
	}
	
	public void moveLeft() {
		if (this.currentIndex > 0) {
			this.currentIndex -= 1;
		}
	}
	public void moveRight() {
		if (this.currentIndex < this.storage.size() - 1) {
			this.currentIndex += 1;
		}
	}
	
	public void setBoard(GameBoard board) {
		this.board = board;
	}
	
	public ArrayList<PowerUp> getStorage() {
		return this.storage;
	}
	
	public int getCurrentIndex() {
		return this.currentIndex;
	}
	
}