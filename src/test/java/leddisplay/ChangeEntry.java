package leddisplay;

class ChangeEntry {
	private int posX, posY;
	private char c;

	public ChangeEntry(int posX, int posY, char c) {
		super();
		this.posX = posX;
		this.posY = posY;
		this.c = c;
	}

	public boolean compare(int posX, int posY, char c) {
		return this.posX == posX & this.posY == posY & this.c == c;
	}
}