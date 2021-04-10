package leddisplay.font;

class HorizontalDeployer {
	private final HorizontalDeployment deployment;
	private final int targetWidth, shift;
	private PixelsMatrix matrix;
	
	public HorizontalDeployer(HorizontalDeployment deployment, int targetWidth, int shift) {
		super();
		this.deployment = deployment;
		this.targetWidth = targetWidth;
		this.shift = shift;
	}

	public PixelsMatrix deploy(PixelsMatrix matrix) {
		this.matrix = matrix;
		
		removeEmptyExternalColumns();
		deployByType();
		applyShift();
		fitToTargerWidth();
		return new PixelsMatrix(matrix);
	}

	private void removeEmptyExternalColumns() {
		matrix.removeColumnsLeft(matrix.getEmptyColumnsCountLeft());
		matrix.removeColumnsRight(matrix.getEmptyColumnsCountRight());
	}
	
	private void deployByType() {
		switch (deployment) {
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
	
	private void applyShift() {
		if (shift > 0) {
			matrix.addEmptyColumnsLeft(shift);
		} else if (shift < 0) {
			matrix.removeColumnsLeft(-shift);
			matrix.addEmptyColumnsRight(-shift);
		}
	}
	
	private void fitToTargerWidth() {
		if (targetWidth > matrix.getWidth()) {
			matrix.addEmptyColumnsRight(targetWidth - matrix.getWidth());
		}
		if (matrix.getWidth() > targetWidth) {
			matrix.removeColumnsRight(matrix.getWidth() - targetWidth);
		}
	}

}
