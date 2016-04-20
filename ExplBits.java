package Model;

public class ExplBits {
	/**
	 * Les morceaux d'une explosion, de structure (et comportement, surtout
	 * colisionnel) tres proche aux murs. A mettre sous une meme classe mere
	 * pour les collisions?
	 */

	private int posX;
	private int posY; // Position en carrés de 32x32
	private String dir; // Direction du bras de l'explosion (R, L, U, D ou C)
	private Boolean onEnd; // Si c'est le boutde l'explosion

	// ----------------------------------------------------------

	public ExplBits(int pX, int pY, String d) {
		posX = pX;
		posY = pY;
		dir = d;
		onEnd = false;
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

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public Boolean getOnEnd() {
		return onEnd;
	}

	public void setOnEnd(Boolean onEnd) {
		this.onEnd = onEnd;
	}
}
