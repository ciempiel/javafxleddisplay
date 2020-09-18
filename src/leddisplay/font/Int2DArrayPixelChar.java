package leddisplay.font;

class Int2DArrayPixelChar implements PixelChar {
	private final int[][] matrix;
	
	public Int2DArrayPixelChar(int[][] matrix) {
		super();
		this.matrix = matrix;
	}

	@Override
	public boolean isPixelSet(int x, int y) {
		return matrix[y][x] != 0 ? true : false;
	}
	
}