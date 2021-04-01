package leddisplay.font;

import java.util.ArrayList;

class PixelsMatrix implements PixelChar {
	private ArrayList<ArrayList<Integer>> matrix;

	public PixelsMatrix(PixelsMatrix other) {
		matrix = new ArrayList<>();
		for (ArrayList<Integer> row : other.matrix) {
			matrix.add(new ArrayList<>(row));
		}
	}
	
	public PixelsMatrix(int[][] matrix) {
		if (matrix == null) {
			throw new NullPointerException();
		}
		this.matrix = new ArrayList<>();
		int width = matrix[0].length;
		for (int[] row : matrix) {
			if (row.length != width) {
				throw new IllegalArgumentException("Rows must have the same length.");
			}
			ArrayList<Integer> mRow = new ArrayList<>(width);
			for (int i : row) {
				mRow.add(i);
			}
			this.matrix.add(mRow);
		}
	}

	public PixelsMatrix(int[] pixels, int height) {
		if (pixels == null) {
			throw new NullPointerException();
		}
		if (pixels.length % height != 0) {
			throw new IllegalArgumentException("Height must be multiply of the pixels length.");
		}
		matrix = new ArrayList<>(height);
		int columnsNumber = pixels.length / height;
		int index = 0;
		for (int i = 0; i < height; i++) {
			ArrayList<Integer> row = new ArrayList<>(columnsNumber);
			matrix.add(row);
			for (int j = 0; j < columnsNumber; j++) {
				row.add(pixels[index++]);
			}
		}
	}

	@Override
	public boolean isPixelSet(int x, int y) {
		return matrix.get(y).get(x) != 0;
	}

	@Override
	public int getWidth() {
		return matrix.get(0).size();
	}

	@Override
	public int getHeigth() {
		return matrix.size();
	}

	public void addEmptyColumn(int columnIndex) {
		for (ArrayList<Integer> row : matrix) {
			row.add(columnIndex, 0);
		}
	}

	public void addEmptyColumnLeft() {
		addEmptyColumn(0);
	}

	public void addEmptyColumnRight() {
		addEmptyColumn(getWidth());
	}

	public void addEmptyColumnsLeft(int count) {
		for (int i = 0; i < count; i++) {
			addEmptyColumnLeft();
		}
	}

	public void addEmptyColumnsRight(int count) {
		for (int i = 0; i < count; i++) {
			addEmptyColumnRight();
		}
	}

	public void addEmptyRow(int rowIndex) {
		matrix.add(rowIndex, emptyRow());
	}

	public void addEmptyRowTop() {
		matrix.add(0, emptyRow());
	}

	public void addEmptyRowsTop(int count) {
		for (int i = 0; i < count; i++) {
			addEmptyRowTop();
		}
	}

	public void addEmptyRowBottom() {
		matrix.add(emptyRow());
	}

	public void addEmptyRowsBottom(int count) {
		for (int i = 0; i < count; i++) {
			addEmptyRowBottom();
		}
	}

	public void removeColumn(int columnIndex) {
		checkColumnIndexForRemove(columnIndex);
		for (ArrayList<Integer> row : matrix) {
			row.remove(columnIndex);
		}
	}
	
	public void removeColumnLeft() {
		removeColumn(0);
	}
	
	public void removeColumnsLeft(int count) {
		for (int i = 0; i < count; i++) {
			removeColumnLeft();
		}
	}
	
	public void removeColumnRight() {
		removeColumn(getWidth() - 1);
	}
	
	public void removeColumnsRight(int count) {
		for (int i = 0; i < count; i++) {
			removeColumnRight();
		}
	}

	public void removeRow(int rowIndex) {
		checkRowIndexForRemove(rowIndex);
		matrix.remove(rowIndex);
	}

	public void removeRowBottom() {
		removeRow(getHeigth() - 1);
	}

	public void removeRowsBottom(int count) {
		for (int i = 0; i < count; i++) {
			removeRowBottom();
		}
	}

	public void removeRowTop() {
		removeRow(0);
	}

	public void removeRowsTop(int count) {
		for (int i = 0; i < count; i++) {
			removeRowTop();
		}
	}

	public int getEmptyRowsCountTop() {
		for (int i = 0; i < getHeigth(); i++) {
			if (!isRowEmpty(i)) {
				return i;
			}
		}
		return getHeigth();
	}

	public int getEmptyRowsCountBottom() {
		for (int i = getHeigth(); i > 0; i--) {
			if (!isRowEmpty(i - 1)) {
				return getHeigth() - i;
			}
		}
		return getHeigth();
	}

	public int getEmptyColumnsCountLeft() {
		for (int i = 0; i < getWidth(); i++) {
			if (!isColumnEmpty(i)) {
				return i;
			}
		}
		return getWidth();
	}

	public int getEmptyColumnsCountRight() {
		for (int i = getWidth(); i > 0; i--) {
			if (!isColumnEmpty(i - 1)) {
				return getWidth() - i;
			}
		}
		return getWidth();
	}

	public boolean isRowEmpty(int rowIndex) {
		checkRowIndex(rowIndex);
		for (int i : matrix.get(rowIndex)) {
			if (i != 0) {
				return false;
			}
		}
		return true;
	}

	public boolean isColumnEmpty(int columnIndex) {
		checkColumnIndex(columnIndex);
		for (int i = 0; i < getHeigth(); i++) {
			if (matrix.get(i).get(columnIndex) != 0) {
				return false;
			}
		}
		return true;
	}
	
	private void checkColumnIndex(int columnIndex) {
		if (columnIndex > getWidth()) {
			throw new IndexOutOfBoundsException("Width: " + getWidth() + ", columnIndex: " + columnIndex);
		}
	}
	
	private void checkColumnIndexForRemove(int columnIndex) {
		checkColumnIndex(columnIndex);
		if (getWidth() == 1) {
			throw new IllegalStateException("Matrix could not be empty.");
		}
	}

	private void checkRowIndex(int rowIndex) {
		if (rowIndex > getHeigth()) {
			throw new IndexOutOfBoundsException("Height: " + getHeigth() + ", rowIndex: " + rowIndex);
		}
	}

	private void checkRowIndexForRemove(int rowIndex) {
		checkRowIndex(rowIndex);
		if (getHeigth() == 1) {
			throw new IllegalStateException("Matrix could not be empty.");
		}
	}

	private ArrayList<Integer> emptyRow() {
		ArrayList<Integer> result = new ArrayList<>();
		for (int i = 0; i < getWidth(); i++) {
			result.add(0);
		}
		return result;
	}

	public int[][] toMatrix() {
		int[][] result = new int[matrix.size()][];
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Integer> row = matrix.get(i);
			int[] aRow = new int[row.size()];
			for (int j = 0; j < row.size(); j++) {
				aRow[j] = row.get(j);
			}
			result[i] = aRow;
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (int y = 0; y < getHeigth(); y++) {
			if (y > 0) {
				buffer.append(System.lineSeparator());	
			}
			for (int x = 0; x < getWidth(); x++) {
				Integer values = matrix.get(y).get(x);
				buffer.append(values != 0 ? '+' : '.');
			}
		}
		return buffer.toString();
	}

}