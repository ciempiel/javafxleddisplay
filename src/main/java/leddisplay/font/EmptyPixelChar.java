package leddisplay.font;

public class EmptyPixelChar implements PixelChar {

	@Override
	public int getPixelValue(int x, int y) {
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeigth() {
		return 0;
	}
	
}