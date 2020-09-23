package leddisplay;

@FunctionalInterface
interface CharChangeListener {
	void update(int posX, int posY, char c);
}