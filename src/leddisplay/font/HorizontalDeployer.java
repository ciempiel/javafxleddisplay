package leddisplay.font;

import java.awt.Dimension;

class HorizontalDeployer {
	private final HorizontalDeployment horizontalDeployment;
	private final Dimension targetDimension;
	private PixelsMatrix matrix;
	
	public HorizontalDeployer(HorizontalDeployment horizontalDeployment, Dimension targetDimension) {
		super();
		this.horizontalDeployment = horizontalDeployment;
		this.targetDimension = targetDimension;
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
		if (targetDimension.width > matrix.getWidth()) {
			int columnsToAdd = targetDimension.width - matrix.getWidth();
			int columnsToAddLeft = columnsToAdd / 2;
			matrix.addEmptyColumnsLeft(columnsToAddLeft);
			int columnsToAddRight = columnsToAdd - columnsToAddLeft;
			matrix.addEmptyColumnsRight(columnsToAddRight);
		}
	}

	private void deployLeft() {
		if (targetDimension.width > matrix.getWidth()) {
			int columnsToAdd = targetDimension.width - matrix.getWidth();
			matrix.addEmptyColumnsRight(columnsToAdd);
		}
	}

	private void deployRight() {
		if (targetDimension.width > matrix.getWidth()) {
			int columnsToAdd = targetDimension.width - matrix.getWidth();
			matrix.addEmptyColumnsLeft(columnsToAdd);
		}
	}

}
