package leddisplay.font;

class VerticalDeployer {
	private final VerticalDeployment verticalDeployment;
	private final PixelFontMetrics fontMetrics;
	private final int targetHeight, shift;
	private PixelsMatrix matrix;

	public VerticalDeployer(VerticalDeployment verticalDeployment, PixelFontMetrics fontMetrics, int targetHeight, int shift) {
		super();
		this.verticalDeployment = verticalDeployment;
		this.fontMetrics = fontMetrics;
		this.targetHeight = targetHeight;
		this.shift = shift;
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
		int toRemoveBottom = 0;
		if (fontMetrics.getHeight() > (fontMetrics.getAscent() + fontMetrics.getDescent())) {
			toRemoveBottom = fontMetrics.getHeight() - (fontMetrics.getAscent() + fontMetrics.getDescent());
		}
		completRowsTop();
		matrix.addEmptyRowsTop(toRemoveBottom);
		applyShift();
		matrix.removeRowsBottom(toRemoveBottom);
		removeExceedingRowsTop();
	}
	
	private void deployBottomCalcDescent() {
		int  toRemoveBottom = 0;
		if (fontMetrics.getHeight() > (fontMetrics.getAscent() + fontMetrics.getCalcDescent())) {
			toRemoveBottom = fontMetrics.getHeight() - (fontMetrics.getAscent() + fontMetrics.getCalcDescent());
		}
		completRowsTop();
		matrix.addEmptyRowsTop(toRemoveBottom);
		applyShift();
		matrix.removeRowsBottom(toRemoveBottom);
		removeExceedingRowsTop();
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
		applyShift();
		removeExceedingRowsTop();
	}

	private void deployTop() {
		removeEmptyExternalRows();
		completRowsBottom();
		applyShift();
		removeExceedingRowsBottom();
	}

	private void deployBottom() {
		removeEmptyExternalRows();
		completRowsTop();
		applyShift();
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
	
	private void applyShift() {
		if (shift > 0) {
			matrix.addEmptyRowsTop(shift);
			matrix.removeRowsBottom(shift);
		} else if (shift < 0) {
			matrix.removeRowsTop(-shift);
			matrix.addEmptyRowsBottom(-shift);
		}
	}

}
