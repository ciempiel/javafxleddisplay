package leddisplay.font;

class EmptyPixelChar implements PixelChar {

	@Override
	public boolean isPixelSet(int x, int y) {
		return false;
	}
	
}