package leddisplay.font;

class HorizontalDeployer {
	private final HorizontalDeployment horizontalDeployment;
	private final int targetWidth;
	private PixelsMatrix matrix;
	
	public HorizontalDeployer(HorizontalDeployment horizontalDeployment, int targetWidth) {
		super();
		this.horizontalDeployment = horizontalDeployment;
		this.targetWidth = targetWidth;
	}

	public PixelsMatrix deploy(PixelsMatrix matrix) {
		this.matrix = matrix;
		
		removeEmptyExternalColumns();
		switch (horizontalDeployment) {
		case CENTER:
			deployCenter();
			break;

		case LEFT:
			deployLeft();
			break;

		case RIGHT:
			deployRight();
			break;
		}
		return new PixelsMatrix(matrix);
	}

	private void removeEmptyExternalColumns() {
		matrix.removeColumnsLeft(matrix.getEmptyColumnsCountLeft());
		matrix.removeColumnsRight(matrix.getEmptyColumnsCountRight());
	}

	private void deployCenter() {
		if (targetWidth > matrix.getWidth()) {
			int columnsToAdd = targetWidth - matrix.getWidth();
			int columnsToAddLeft = columnsToAdd / 2;
			matrix.addEmptyColumnsLeft(columnsToAddLeft);
			int columnsToAddRight = columnsToAdd - columnsToAddLeft;
			matrix.addEmptyColumnsRight(columnsToAddRight);
		}
	}

	private void deployLeft() {
		if (targetWidth > matrix.getWidth()) {
			matrix.addEmptyColumnsRight(targetWidth - matrix.getWidth());
		}
	}

	private void deployRight() {
		if (targetWidth > matrix.getWidth()) {
			matrix.addEmptyColumnsLeft(targetWidth - matrix.getWidth());
		}
	}

}
