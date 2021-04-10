package leddisplay.font;

class Int2DArrayPixelChar implements PixelChar {
	private final int[][] matrix;

	public Int2DArrayPixelChar(int[][] matrix) {
		super();
		this.matrix = matrix;
	}

	@Override
	public int getPixelValue(int x, int y) {
		return matrix[y][x];
	}

	@Override
	public int getWidth() {
		return matrix[0].length;
	}

	@Override
	public int getHeigth() {
		return matrix.length;
	}

}