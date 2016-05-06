package PowerUp;

import Model.Player;

public class PowerUp {
	/**
	 * Les powerup sont generes aleatoirement lorsque un mur est casse, ils sont
	 * sauvegardes dans une liste du modele. Lorsqu'un joueur en ramasse un
	 * (actif), il s'ajoute a une liste de powerups propre au joueur, il devient
	 * inactif, et il execute une action sur lui. Lorsqu'un joueur meurt, ses
	 * powerups sont reactives et replaces aleatoirement au lieu de certaines
	 * briques.
	 */

	private int posX;
	private int posY; // Position en carrés de 32x32
	private Boolean active;

	// ----------------------------------------------------------

	public PowerUp(int pX, int pY) {
		posX = pX;
		posY = pY;
		active = true; // Le powerup peut etre rammase
	}

	public void affectPlayer(Player pl) {
		/**
		 * Cette fonction s'applique au joueur qui ramasse le powerup. Elle est
		 * differente selon la sousclasse.
		 */
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
