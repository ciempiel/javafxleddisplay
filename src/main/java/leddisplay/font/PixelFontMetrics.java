package leddisplay.font;

class PixelFontMetrics {
	private final int height, ascent, descent, calcDescent;
	
	public PixelFontMetrics(int height, int ascent, int descent, int calcDescent) {
		super();
		this.height = height;
		this.ascent = ascent;
		this.descent = descent;
		this.calcDescent = calcDescent;
	}

	public int getHeight() {
		return height;
	}
	
	public int getAscent() {
		return ascent;
	}
	
	public int getDescent() {
		return descent;
	}
	
	public int getCalcDescent() {
		return calcDescent;
	}
}