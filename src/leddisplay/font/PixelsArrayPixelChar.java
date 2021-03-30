package leddisplay.font;

class PixelsArrayPixelChar implements PixelChar {
	private final int[] pixels;
	private final int width, height;

	public PixelsArrayPixelChar(int[] pixels, int width, int height) {
		super();
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isPixelSet(int x, int y) {
		int index = y * width + x;
		return pixels[index] != 0;
	}

	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeigth() {
		return height;
	}
	
}