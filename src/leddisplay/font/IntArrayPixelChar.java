package leddisplay.font;

import java.awt.Dimension;

class IntArrayPixelChar implements PixelChar {
	private final int[] pixels;
	private final Dimension metricDimension;
	private final int fontSize;

	public IntArrayPixelChar(int[] pixels, Dimension metricDimension, int fontSize) {
		super();
		this.pixels = pixels;
		this.metricDimension = metricDimension;
		this.fontSize = fontSize;
	}

	@Override
	public boolean isPixelSet(int x, int y) {
		int topGap = metricDimension.height - fontSize;
		y -= topGap;
		int pixelWidth = pixels.length / metricDimension.height;
		int leftGap = (metricDimension.width - pixelWidth) / 2;
		int maxRight = pixelWidth + leftGap - 1;
		if (x < leftGap || x > maxRight) {
			return false;
		}
		x -= leftGap;
		int index = pixelWidth * y + x + pixelWidth;
		if (index < 0 || index >= pixels.length) { // XXX index < 0
			return false;
		}
		return pixels[index] != 0;
	}

}