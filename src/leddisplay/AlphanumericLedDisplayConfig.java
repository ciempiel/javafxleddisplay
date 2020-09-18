package leddisplay;

public class AlphanumericLedDisplayConfig {
	public static final int DEFAULT_LINE_COUNT = 2;
	public static final int DEFAULT_CHAR_COUNT = 16;
	public static final int DEFAULT_PIXEL_X_COUNT = 5;
	public static final int DEFAULT_PIXEL_Y_COUNT = 7;
	public static final double DEFAULT_PIXEL_SIZE = 10.0;
	public static final double DEFAULT_PIXEL_GAP = 2.0;

	private int lineCount = DEFAULT_LINE_COUNT;
	private int charCount = DEFAULT_CHAR_COUNT;
	private int pixelXCount = DEFAULT_PIXEL_X_COUNT;
	private int pixelYCount = DEFAULT_PIXEL_Y_COUNT;
	private double pixelSize = DEFAULT_PIXEL_SIZE;
	private double pixelGap = DEFAULT_PIXEL_GAP;

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public int getCharCount() {
		return charCount;
	}

	public void setCharCount(int charCount) {
		this.charCount = charCount;
	}

	public int getPixelXCount() {
		return pixelXCount;
	}

	public void setPixelXCount(int pixelXCount) {
		this.pixelXCount = pixelXCount;
	}

	public int getPixelYCount() {
		return pixelYCount;
	}

	public void setPixelYCount(int pixelYCount) {
		this.pixelYCount = pixelYCount;
	}

	public double getPixelSize() {
		return pixelSize;
	}

	public void setPixelSize(double pixelSize) {
		this.pixelSize = pixelSize;
	}

	public double getPixelGap() {
		return pixelGap;
	}

	public void setPixelGap(double pixelGap) {
		this.pixelGap = pixelGap;
	}

}