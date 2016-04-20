package Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class MoleHole {
	/**
	 * Les trous permettent au joueur d'avancer sous terre, ils sont crees par
	 * des bombes taupe ou par des joueurs qui remontent a la surface.
	 */

	private int posX;
	private int posY; // Position en carrés de 32x32

	private Boolean active; // Faux lorsque le trou doit disparaitre.
	private ActionListener holeThread; // Enleve le trou apres un temps
	private Timer time;

	// ----------------------------------------------------------

	public MoleHole(int pX, int pY) {
		posX = pX;
		posY = pY;
		active = true;
		holeThread = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toRemove();
			}
		};
		time = new Timer(6000, holeThread);
		time.start();
	}

	public void toRemove() {
		time.stop();
		active = false;
	}

	// ----------------------------------------------------------

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
