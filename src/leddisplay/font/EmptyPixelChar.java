package leddisplay.font;

public class EmptyPixelChar implements PixelChar {

	@Override
	public boolean isPixelSet(int x, int y) {
		return false;
	}
	
}