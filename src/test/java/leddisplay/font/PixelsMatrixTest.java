package leddisplay.font;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class PixelsMatrixTest {
	private PixelsMatrix matrix;

	@Before
	public void setUp() throws Exception {
		setUpLetter_L();
	}

	private void setUpLetter_L() {
		String letter = getLetterL();
		matrix = convert(letter, 8);
	}

	private String getLetterL() {
		String letter
		= "........." + ls()
		+ "........." + ls()
		+ ".+......." + ls()
		+ ".+......." + ls()
		+ ".+......." + ls()
		+ ".+......." + ls()
		+ ".++++++.." + ls()
		+ ".........";
		return letter;
	}

	private String ls() {
		return System.lineSeparator();
	}
	
	public void shouldInitEqualsCopyOfObjectInCloneConstructor() {
		PixelsMatrix copy = new PixelsMatrix(matrix);
		assertEquals(matrix.toString(), copy.toString());
	}
	
	@Test
	public void shouldReturnArrayEqualsConstructorArgs() {
		Random rand = new Random();
		int[][] m1 = new int[8][];
		for (int i=0; i<8; i++) {
			m1[i] = rand.ints().limit(10).map(v -> (v > 0) ? 1 : 0).toArray();
		}
		matrix = new PixelsMatrix(m1);
		int[][] m2 = matrix.toMatrix();
		for (int i=0; i<8; i++) {
			assertArrayEquals(m1[i], m2[i]);
		}
	}

	@Test
	public void shouldReturnExpectedDimersion() {
		assertEquals(8, matrix.getHeigth());
		assertEquals(9, matrix.getWidth());
	}
	
	@Test
	public void shouldRemoveBorderColumnsAndRows() {
		matrix.removeColumnLeft();
		matrix.removeColumnsRight(2);
		matrix.removeRowBottom();
		matrix.removeRowsTop(2);
		assertEquals(5, matrix.getHeigth());
		assertEquals(6, matrix.getWidth());
		String expectedLetter
		= "+....." + ls()
		+ "+....." + ls()
		+ "+....." + ls()
		+ "+....." + ls()
		+ "++++++";
		assertEquals(expectedLetter, matrix.toString());
	}
	
	@Test
	public void shouldGettingAddingEmptyColumnsLeft() {
		assertEquals(1, matrix.getEmptyColumnsCountLeft());
		matrix.addEmptyColumnLeft();
		assertEquals(2, matrix.getEmptyColumnsCountLeft());
		matrix.addEmptyColumnsLeft(2);
		assertEquals(4, matrix.getEmptyColumnsCountLeft());
	}
	
	@Test
	public void shouldGettingAddingEmptyColumnsRight() {
		assertEquals(2, matrix.getEmptyColumnsCountRight());
		matrix.addEmptyColumnRight();
		assertEquals(3, matrix.getEmptyColumnsCountRight());
		matrix.addEmptyColumnsRight(2);
		assertEquals(5, matrix.getEmptyColumnsCountRight());
	}
	
	@Test
	public void shouldGettingAddingEmptyRowsTop() {
		assertEquals(2, matrix.getEmptyRowsCountTop());
		matrix.addEmptyRowTop();
		assertEquals(3, matrix.getEmptyRowsCountTop());
		matrix.addEmptyRowsTop(2);
		assertEquals(5, matrix.getEmptyRowsCountTop());
	}
	
	@Test
	public void shouldGettingAddingEmptyRowsBottom() {
		assertEquals(1, matrix.getEmptyRowsCountBottom());
		matrix.addEmptyRowBottom();
		assertEquals(2, matrix.getEmptyRowsCountBottom());
		matrix.addEmptyRowsBottom(2);
		assertEquals(4, matrix.getEmptyRowsCountBottom());
	}
	
	@Test
	public void shouldReturnsEmptyCountsAsWidthAndHeightWhenLetterRemoved() {
		matrix.removeColumn(1);
		matrix.removeRow(6);
		assertEquals(8, matrix.getEmptyColumnsCountLeft());
		assertEquals(8, matrix.getEmptyColumnsCountRight());
		assertEquals(7, matrix.getEmptyRowsCountTop());
		assertEquals(7, matrix.getEmptyRowsCountBottom());
	}
	
	@Test
	public void shouldToStringEqualsLetter() {
		assertEquals(getLetterL(), matrix.toString());
	}

	private PixelsMatrix convert(String letter, int height) {
		int[] pixels = letter.chars().filter(c -> c != '\r' && c != '\n').map(c -> c != '.' ? 1 : 0).toArray();
		return new PixelsMatrix(pixels, height);
	}

}
