package leddisplay.font;

import java.awt.Dimension;
import java.awt.FontMetrics;

class PixelCharDeployer implements PixelChar {
	private final PixelsMatrix matrix;
	private final FontMetrics fontMetrics;
	private final Dimension targetDimension;
	
	public PixelCharDeployer(int[] pixels, int height, FontMetrics fontMetrics, Dimension targetDimension) {
		super();
		this.fontMetrics = fontMetrics;
		this.targetDimension = targetDimension;
		matrix = new PixelsMatrix(pixels, height);
		deploy();
	}

	private void deploy() {
		deployVertically();
		deployHorizontally();
	}
	
	private void deployVertically() {
		if (fontMetrics.getHeight() > (fontMetrics.getAscent() + fontMetrics.getDescent())) {
			int gap = fontMetrics.getHeight() - (fontMetrics.getAscent() + fontMetrics.getDescent());
			matrix.removeRowsBottom(gap);
		}
		if (targetDimension.height < matrix.getHeigth()) {
			int rowsToRemove = matrix.getHeigth() - targetDimension.height;
			matrix.removeRowsTop(rowsToRemove);
		} else if (targetDimension.height > matrix.getHeigth()) {
			int rowsToAdd = targetDimension.height - matrix.getHeigth();
			matrix.addEmptyRowsTop(rowsToAdd);
		}
	}
	
	private void deployHorizontally() {
		if (targetDimension.width > matrix.getWidth()) {
			int columnsToAdd = targetDimension.width - matrix.getWidth();
			int columnsToAddLeft = columnsToAdd / 2;
			matrix.addEmptyColumnsLeft(columnsToAddLeft);
			int columnsToAddRight = columnsToAdd - columnsToAddLeft;
			matrix.addEmptyColumnsRight(columnsToAddRight);
		}
	}
	
	@Override
	public boolean isPixelSet(int x, int y) {
		return matrix.isPixelSet(x, y);
	}

	@Override
	public int getWidth() {
		return matrix.getWidth();
	}

	@Override
	public int getHeigth() {
		return matrix.getHeigth();
	}
	
}