package leddisplay.font;

import java.awt.Dimension;
import java.awt.FontMetrics;

class VerticalDeployer {
	private final VerticalDeployment verticalDeployment;
	private final FontMetrics fontMetrics;
	private final Dimension targetDimension;
	private PixelsMatrix matrix;

	public VerticalDeployer(VerticalDeployment verticalDeployment, FontMetrics fontMetrics, Dimension targetDimension) {
		super();
		this.verticalDeployment = verticalDeployment;
		this.fontMetrics = fontMetrics;
		this.targetDimension = targetDimension;
	}

	public PixelsMatrix deploy(PixelsMatrix matrix) {
		this.matrix = matrix;

		switch (verticalDeployment) {
		case BOTTOM_FONT_DESCENT:
			deployBottomFontDescent();
			break;

		case BOTTOM_CALC_DESCENT:
			deployBottomCalcDescent();
			break;

		case CENTER:
			deployCenter();
			break;

		case TOP:
			deployTop();
			break;

		case BOTTOM:
			deployBottom();
			break;
		}
		return new PixelsMatrix(matrix);
	}

	private void deployBottomFontDescent() {
		if (fontMetrics.getHeight() > (fontMetrics.getAscent() + fontMetrics.getDescent())) {
			int gap = fontMetrics.getHeight() - (fontMetrics.getAscent() + fontMetrics.getDescent());
			matrix.removeRowsBottom(gap);
		}
		removeExceedingRowsTop();
		completRowsTop();
	}

	private void deployBottomCalcDescent() {
		// TODO Auto-generated method stub
		
		deployBottomFontDescent();
	}

	private void deployCenter() {
		removeEmptyExternalRows();
		if (targetDimension.height > matrix.getHeigth()) {
			int rowsToAdd = targetDimension.height - matrix.getHeigth();
			int rowsToAddTop = rowsToAdd / 2;
			matrix.addEmptyRowsTop(rowsToAddTop);
			int rowsToAddBottom = rowsToAdd - rowsToAddTop;
			matrix.addEmptyRowsBottom(rowsToAddBottom);
		}
		removeExceedingRowsTop();
	}

	private void deployTop() {
		removeEmptyExternalRows();
		completRowsBottom();
		removeExceedingRowsBottom();
	}

	private void deployBottom() {
		removeEmptyExternalRows();
		completRowsTop();
		removeExceedingRowsTop();
	}

	private void removeEmptyExternalRows() {
		matrix.removeRowsTop(matrix.getEmptyRowsCountTop());
		matrix.removeRowsBottom(matrix.getEmptyRowsCountBottom());
	}
	
	private void completRowsBottom() {
		if (targetDimension.height > matrix.getHeigth()) {
			matrix.addEmptyRowsBottom(targetDimension.height - matrix.getHeigth());
		}
	}
	
	private void completRowsTop() {
		if (targetDimension.height > matrix.getHeigth()) {
			matrix.addEmptyRowsTop(targetDimension.height - matrix.getHeigth());
		}
	}

	private void removeExceedingRowsTop() {
		if (matrix.getHeigth() > targetDimension.height) {
			matrix.removeRowsTop(matrix.getHeigth() - targetDimension.height);
		}
	}
	
	private void removeExceedingRowsBottom() {
		if (matrix.getHeigth() > targetDimension.height) {
			matrix.removeRowsBottom(matrix.getHeigth() - targetDimension.height);
		}
	}

}
