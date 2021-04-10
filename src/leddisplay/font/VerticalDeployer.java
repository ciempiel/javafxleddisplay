package leddisplay.font;

class VerticalDeployer {
	private final VerticalDeployment verticalDeployment;
	private final PixelFontMetrics fontMetrics;
	private final int targetHeight;
	private PixelsMatrix matrix;

	public VerticalDeployer(VerticalDeployment verticalDeployment, PixelFontMetrics fontMetrics, int targetHeight) {
		super();
		this.verticalDeployment = verticalDeployment;
		this.fontMetrics = fontMetrics;
		this.targetHeight = targetHeight;
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
		if (fontMetrics.getHeight() > (fontMetrics.getAscent() + fontMetrics.getCalcDescent())) {
			int gap = fontMetrics.getHeight() - (fontMetrics.getAscent() + fontMetrics.getCalcDescent());
			matrix.removeRowsBottom(gap);
		}
		removeExceedingRowsTop();
		completRowsTop();
	}

	private void deployCenter() {
		removeEmptyExternalRows();
		if (targetHeight > matrix.getHeigth()) {
			int rowsToAdd = targetHeight - matrix.getHeigth();
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
		if (targetHeight > matrix.getHeigth()) {
			matrix.addEmptyRowsBottom(targetHeight - matrix.getHeigth());
		}
	}
	
	private void completRowsTop() {
		if (targetHeight > matrix.getHeigth()) {
			matrix.addEmptyRowsTop(targetHeight - matrix.getHeigth());
		}
	}

	private void removeExceedingRowsTop() {
		if (matrix.getHeigth() > targetHeight) {
			matrix.removeRowsTop(matrix.getHeigth() - targetHeight);
		}
	}
	
	private void removeExceedingRowsBottom() {
		if (matrix.getHeigth() > targetHeight) {
			matrix.removeRowsBottom(matrix.getHeigth() - targetHeight);
		}
	}

}
