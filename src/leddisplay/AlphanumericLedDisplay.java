package leddisplay;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import leddisplay.font.PixelChar;
import leddisplay.font.PixelFont;
import leddisplay.font.Test5x7PixelFontABC;

public class AlphanumericLedDisplay extends Control {
	public static final int DEFAULT_LINE_COUNT = 2;
	public static final int DEFAULT_CHAR_COUNT = 16;
	public static final int DEFAULT_PIXEL_COUNT_X = 5;
	public static final int DEFAULT_PIXEL_COUNT_Y = 7;
	public static final double DEFAULT_PIXEL_SIZE = 10.0;
	public static final double DEFAULT_PIXEL_GAP = 2.0;

	private final PixelFont font;
	private AlphanumericChar[][] alphanumerics;

	public AlphanumericLedDisplay() {
		this(new Test5x7PixelFontABC());
	}

	public AlphanumericLedDisplay(PixelFont font) {
		super();
		this.font = font;
		// XXX refresh all - binding couse memory leakage
		lineCount.addListener((observable, newValue, oldValue) -> refresh());
		charCount.addListener((observable, newValue, oldValue) -> refresh());
		pixelCountX.addListener((observable, newValue, oldValue) -> refresh());
		pixelCountY.addListener((observable, newValue, oldValue) -> refresh());
		pixelWidth.addListener((observable, newValue, oldValue) -> refresh());
		pixelHeight.addListener((observable, newValue, oldValue) -> refresh());
		pixelGapX.addListener((observable, newValue, oldValue) -> refresh());
		pixelGapY.addListener((observable, newValue, oldValue) -> refresh());
	}

	private final IntegerProperty lineCount = new SimpleIntegerProperty(this, "lineCount", DEFAULT_LINE_COUNT);
	private final IntegerProperty charCount = new SimpleIntegerProperty(this, "charCount", DEFAULT_CHAR_COUNT);
	private final IntegerProperty pixelCountX = new SimpleIntegerProperty(this, "pixelXCount", DEFAULT_PIXEL_COUNT_X);
	private final IntegerProperty pixelCountY = new SimpleIntegerProperty(this, "pixelYCount", DEFAULT_PIXEL_COUNT_Y);
	private final DoubleProperty pixelWidth = new SimpleDoubleProperty(this, "pixelWidth", DEFAULT_PIXEL_SIZE);
	private final DoubleProperty pixelHeight = new SimpleDoubleProperty(this, "pixelHeight", DEFAULT_PIXEL_SIZE);
	private final DoubleProperty pixelGapX = new SimpleDoubleProperty(this, "pixelGapX", DEFAULT_PIXEL_GAP);
	private final DoubleProperty pixelGapY = new SimpleDoubleProperty(this, "pixelGapY", DEFAULT_PIXEL_GAP);

	
	// public void print(String text) {
	//
	// }

	public void print(String text, int posX, int posY) {
		int lastPos = Math.min(getCharCount(), posX + text.length());
		int charIndex = 0;
		for (; posX < lastPos; posX++) {
			char c = text.charAt(charIndex);
			PixelChar pixelMatrix = font.getChar(c);
			alphanumerics[posX][posY].setPixelMatrix(pixelMatrix);
			charIndex++;
		}
	}

	public void clearDisplay() {
		for (int i = 0; i < getCharCount(); i++) {
			for (int j = 0; j < getLineCount(); j++) {
				alphanumerics[i][j].clearPixel();
			}
		}
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new SkinBase<AlphanumericLedDisplay>(this) {
			{
				getChildren().add(createPane());
			}
		};
	}

	private Pane createPane() {
		Pane pane = new Pane();
		alphanumerics = new AlphanumericChar[getCharCount()][getLineCount()];
		pane.setStyle("-fx-background-color: #57a448");
		double width = AlphanumericChar.calcWidth(this);
		double height = AlphanumericChar.calcHeight(this);
		for (int i = 0; i < getCharCount(); i++) {
			for (int j = 0; j < getLineCount(); j++) {
				AlphanumericChar alphanumeric = new AlphanumericChar(this);
				alphanumeric.setLayoutX(width * i);
				alphanumeric.setLayoutY(height * j);
				alphanumerics[i][j] = alphanumeric;
				pane.getChildren().add(alphanumeric);
			}
		}
		return pane;
	}
	
	private void refresh() {
		getChildren().clear();
		getChildren().add(createPane());
	}

	public final IntegerProperty lineCountProperty() {
		return this.lineCount;
	}

	public final int getLineCount() {
		return this.lineCountProperty().get();
	}

	public final void setLineCount(final int lineCount) {
		this.lineCountProperty().set(lineCount);
	}

	public final IntegerProperty charCountProperty() {
		return this.charCount;
	}

	public final int getCharCount() {
		return this.charCountProperty().get();
	}

	public final void setCharCount(final int charCount) {
		this.charCountProperty().set(charCount);
	}

	public final IntegerProperty pixelCountXProperty() {
		return this.pixelCountX;
	}

	public final int getPixelCountX() {
		return this.pixelCountXProperty().get();
	}

	public final void setPixelCountX(final int pixelCountX) {
		this.pixelCountXProperty().set(pixelCountX);
	}

	public final IntegerProperty pixelCountYProperty() {
		return this.pixelCountY;
	}

	public final int getPixelCountY() {
		return this.pixelCountYProperty().get();
	}

	public final void setPixelCountY(final int pixelCountY) {
		this.pixelCountYProperty().set(pixelCountY);
	}

	public final DoubleProperty pixelWidthProperty() {
		return this.pixelWidth;
	}

	public final double getPixelWidth() {
		return this.pixelWidthProperty().get();
	}

	public final void setPixelWidth(final double pixelWidth) {
		this.pixelWidthProperty().set(pixelWidth);
	}

	public final DoubleProperty pixelHeightProperty() {
		return this.pixelHeight;
	}

	public final double getPixelHeight() {
		return this.pixelHeightProperty().get();
	}

	public final void setPixelHeight(final double pixelHeight) {
		this.pixelHeightProperty().set(pixelHeight);
	}

	public final DoubleProperty pixelGapXProperty() {
		return this.pixelGapX;
	}

	public final double getPixelGapX() {
		return this.pixelGapXProperty().get();
	}

	public final void setPixelGapX(final double pixelGapX) {
		this.pixelGapXProperty().set(pixelGapX);
	}

	public final DoubleProperty pixelGapYProperty() {
		return this.pixelGapY;
	}

	public final double getPixelGapY() {
		return this.pixelGapYProperty().get();
	}

	public final void setPixelGapY(final double pixelGapY) {
		this.pixelGapYProperty().set(pixelGapY);
	}

}