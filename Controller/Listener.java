package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Player;

public class Listener implements KeyListener {

	private Player player;

	private String pressed;
	private String prMemory;
	private String released;
	/*
	 * NB: la memoire sert a gerer le fait qu'on presse deux touches en meme
	 * temps et on en relache une.
	 */

	private Boolean action;

	private int right;
	private int left;
	private int up;
	private int down;
	private int bomb;

	// ----------------------------------------------------------

	public Listener(Player pl) {
		player = pl;
		setKeys();
		pressed = "";
		prMemory = "";
		released = "";
		action = false;
	}

	public void setKeys() {
		/*
		 * if(player.getId() == 1){ right = KeyEvent.VK_D; left = KeyEvent.VK_A;
		 * up = KeyEvent.VK_W; down = KeyEvent.VK_S; bomb = KeyEvent.VK_Q;
		 */
		if (player.getId() == 1) {
			right = KeyEvent.VK_D;
			left = KeyEvent.VK_Q;
			up = KeyEvent.VK_Z;
			down = KeyEvent.VK_S;
			bomb = KeyEvent.VK_A;
		} else if (player.getId() == 2) {
			right = KeyEvent.VK_L;
			left = KeyEvent.VK_J;
			up = KeyEvent.VK_I;
			down = KeyEvent.VK_K;
			bomb = KeyEvent.VK_U;
		} else if (player.getId() == 3) {
			right = KeyEvent.VK_RIGHT;
			left = KeyEvent.VK_LEFT;
			up = KeyEvent.VK_UP;
			down = KeyEvent.VK_DOWN;
			bomb = KeyEvent.VK_CONTROL;
		} else {
			right = KeyEvent.VK_NUMPAD3;
			left = KeyEvent.VK_NUMPAD1;
			up = KeyEvent.VK_NUMPAD5;
			down = KeyEvent.VK_NUMPAD2;
			bomb = KeyEvent.VK_NUMPAD4;
		}
	}

	// ----------------------------------------------------------

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		String prSaved = pressed;

		if (e.getKeyCode() == right) {
			pressed = "R";
			memoryPressed(prSaved);
		} else if (e.getKeyCode() == left) {
			pressed = "L";
			memoryPressed(prSaved);
		} else if (e.getKeyCode() == up) {
			pressed = "U";
			memoryPressed(prSaved);
		} else if (e.getKeyCode() == down) {
			pressed = "D";
			memoryPressed(prSaved);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == right) {
			released = "R";
			memoryReleased();
		} else if (e.getKeyCode() == left) {
			released = "L";
			memoryReleased();
		} else if (e.getKeyCode() == up) {
			released = "U";
			memoryReleased();
		} else if (e.getKeyCode() == down) {
			released = "D";
			memoryReleased();
		}

		if (e.getKeyCode() == bomb && player.getDeathPose() == 0) {
			action = true;
		}
	}

	// ----------------------------------------------------------

	public void memoryPressed(String saved) {
		/**
		 * Gere la memoire lorsque l'on PRESSE une touche de mouvement. On sauve
		 * si la touche pressee n'est pas la meme que la derniere.
		 */
		if (!saved.equals(pressed)) {
			prMemory = saved; // On garde en memoire le mvt precedent si pas
								// repetition.
		}
	}

	public void memoryReleased() {
		/**
		 * Gere la memoire lorsque l'on RELACHE une touche de mouvement. Si la
		 * touche en memoire n'est pas celle que l'on vient de relacher,
		 * incluant s'il n'y a pas de touche en memoire (string vide), on charge
		 * le mvt en memoire. La memoire est ensuite effacee.
		 */
		if (!released.equals(prMemory)) {
			pressed = prMemory;
		}
		prMemory = "";
	}

	// ----------------------------------------------------------

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getPressed() {
		return pressed;
	}

	public void setPressed(String pressed) {
		this.pressed = pressed;
	}

	public Boolean getAction() {
		return action;
	}

	public void setAction(Boolean action) {
		this.action = action;
	}

}
