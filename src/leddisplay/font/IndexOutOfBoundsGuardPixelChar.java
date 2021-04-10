package leddisplay.font;

class IndexOutOfBoundsGuardPixelChar implements PixelChar {
	private final PixelChar delegate;

	public IndexOutOfBoundsGuardPixelChar(PixelChar delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public int getPixelValue(int x, int y) {
		if (x >= delegate.getWidth() || y >= delegate.getHeigth() || x < 0 || y < 0) {
			return 0;
		}
		return delegate.getPixelValue(x, y);
	}

	@Override
	public int getWidth() {
		return delegate.getWidth();
	}

	@Override
	public int getHeigth() {
		return delegate.getHeigth();
	}

}