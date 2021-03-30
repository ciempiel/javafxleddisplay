package leddisplay.font;

public interface PixelChar {
	boolean isPixelSet(int x, int y);
	
	default int getWidth() {
		return 0;
	}
	
	default int getHeigth() {
		return 0;
	}
	
}