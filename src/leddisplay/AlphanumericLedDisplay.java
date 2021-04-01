package leddisplay;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import leddisplay.font.HorizontalDeployment;
import leddisplay.font.PixelChar;
import leddisplay.font.PixelFontProvider;

public class AlphanumericLedDisplay extends Control {
	public static final int DEFAULT_LINE_COUNT = 2;
	public static final int DEFAULT_CHAR_COUNT = 16;
	public static final int DEFAULT_PIXEL_COUNT_X = 5;
	public static final int DEFAULT_PIXEL_COUNT_Y = 7;
	public static final double DEFAULT_PIXEL_SIZE = 10.0;
	public static final double DEFAULT_PIXEL_GAP = 2.0;
	public static final double DEFAULT_CHAR_GAP = 4.0;
	public static final double DEFAULT_PADDING = 2.0;
	public static final Color DEFAULT_PIXEL_ON_COLOR = Color.rgb(21, 57, 31);
	public static final Color DEFAULT_PIXEL_OFF_COLOR = Color.rgb(92, 114, 98);
	public static final Color DEFAULT_BACKLIGHT_COLOR = Color.rgb(87, 164, 72);
	public static final HorizontalDeployment DEFAULT_HORIZONTAL_DEPLOYMENT = HorizontalDeployment.CENTER;

	private PixelFontProvider fontProvider;
	private Pane pane;
	private AlphanumericChar[][] alphanumerics;

	public AlphanumericLedDisplay() {
		this(Font.font("Casio fx-9860GII", 7)); // XXX
	}

	public AlphanumericLedDisplay(Font font) {
		super();
		setFont(font);
		updatePixelFont();
		
		// XXX refresh all - binding couse memory leakage
		lineCount.addListener((observable, newValue, oldValue) -> refresh());
		charCount.addListener((observable, newValue, oldValue) -> refresh());
		pixelCountX.addListener((observable, newValue, oldValue) -> refresh());
		pixelCountY.addListener((observable, newValue, oldValue) -> refresh());
		pixelWidth.addListener((observable, newValue, oldValue) -> refresh());
		pixelHeight.addListener((observable, newValue, oldValue) -> refresh());
		pixelGapX.addListener((observable, newValue, oldValue) -> refresh());
		pixelGapY.addListener((observable, newValue, oldValue) -> refresh());
		charGapX.addListener((observable, newValue, oldValue) -> refresh());
		charGapY.addListener((observable, newValue, oldValue) -> refresh());
		this.font.addListener((observable, newValue, oldValue) -> {
			updatePixelFont();
			refreshAllText();
		});

		text.addListener((observable, oldValue, newValue) -> {
			if (text.isBound()) {
				CharPrinter printer = new CharPrinter(getLineCount(), getCharCount());
				printer.setText(oldValue);
				printer.initChanges();
				printer.setText(newValue);
				printer.consumeChanges((posX, posY, c) -> {
					PixelChar pixelMatrix = fontProvider.getChar(c);
					alphanumerics[posX][posY].setPixelMatrix(pixelMatrix);
				});
			}
		});
		pixelOnColor.addListener((observable, oldValue, newValue) -> updateColors());
		pixelOffColor.addListener((observable, oldValue, newValue) -> updateColors());
		backlightColor.addListener((observable, oldValue, newValue) -> updateColors());
		horizontalDeployment.addListener((observable, newValue, oldValue) -> refresh());
	}

	private final IntegerProperty lineCount = new SimpleIntegerProperty(this, "lineCount", DEFAULT_LINE_COUNT);
	private final IntegerProperty charCount = new SimpleIntegerProperty(this, "charCount", DEFAULT_CHAR_COUNT);
	private final IntegerProperty pixelCountX = new SimpleIntegerProperty(this, "pixelXCount", DEFAULT_PIXEL_COUNT_X);
	private final IntegerProperty pixelCountY = new SimpleIntegerProperty(this, "pixelYCount", DEFAULT_PIXEL_COUNT_Y);
	private final DoubleProperty pixelWidth = new SimpleDoubleProperty(this, "pixelWidth", DEFAULT_PIXEL_SIZE);
	private final DoubleProperty pixelHeight = new SimpleDoubleProperty(this, "pixelHeight", DEFAULT_PIXEL_SIZE);
	private final DoubleProperty pixelGapX = new SimpleDoubleProperty(this, "pixelGapX", DEFAULT_PIXEL_GAP);
	private final DoubleProperty pixelGapY = new SimpleDoubleProperty(this, "pixelGapY", DEFAULT_PIXEL_GAP);
	private final DoubleProperty charGapX = new SimpleDoubleProperty(this, "charGapX", DEFAULT_CHAR_GAP);
	private final DoubleProperty charGapY = new SimpleDoubleProperty(this, "charGapY", DEFAULT_CHAR_GAP);
	private final StringProperty text = new SimpleStringProperty(this, "text");
	private final ObjectProperty<Color> pixelOnColor = new SimpleObjectProperty<>(this, "pixelOnColor", DEFAULT_PIXEL_ON_COLOR);
	private final ObjectProperty<Color> pixelOffColor = new SimpleObjectProperty<>(this, "pixelOffColor", DEFAULT_PIXEL_OFF_COLOR);
	private final ObjectProperty<Color> backlightColor = new SimpleObjectProperty<>(this, "backlightColor", DEFAULT_BACKLIGHT_COLOR);
	private final ObjectProperty<Font> font = new SimpleObjectProperty<>(this, "font", Font.getDefault());
	private final ObjectProperty<HorizontalDeployment> horizontalDeployment = new SimpleObjectProperty<>(this, "horizontalDeployment", DEFAULT_HORIZONTAL_DEPLOYMENT);
	
	// public void print(String text) {
	//
	// }

	public void print(String text, int posX, int posY) {
		if (textProperty().isBound()) {

			// XXX throw
			return;
		}

		int lastPos = Math.min(getCharCount(), posX + text.length());
		int charIndex = 0;
		for (; posX < lastPos; posX++) {
			char c = text.charAt(charIndex);
			PixelChar pixelMatrix = fontProvider.getChar(c);
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
	
	private void updatePixelFont() {
		fontProvider = new PixelFontProvider(getFont(), (int)getPixelCountX(), (int)getPixelCountY());
		fontProvider.setHorizontalDeployment(getHorizontalDeployment());
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new SkinBase<AlphanumericLedDisplay>(this) {
			{
				getChildren().add(createNode());
			}
		};
	}

	private Node createNode() {
		pane = new Pane();
		alphanumerics = new AlphanumericChar[getCharCount()][getLineCount()];
		setPaneColor();
		double width = AlphanumericChar.calcWidth(this);
		double height = AlphanumericChar.calcHeight(this);
		for (int i = 0; i < getCharCount(); i++) {
			for (int j = 0; j < getLineCount(); j++) {
				AlphanumericChar alphanumeric = new AlphanumericChar(this);
				alphanumeric.setLayoutX((width + getCharGapX()) * i);
				alphanumeric.setLayoutY((height + getCharGapY()) * j);
				alphanumerics[i][j] = alphanumeric;
				pane.getChildren().add(alphanumeric);
			}
		}
		return new Group(pane); // XXX if not wrapped, background when padding doesn't work
	}

	private void setPaneColor() {
		String webColor = String.valueOf(getBacklightColor()).replace("0x", "#");
		setStyle("-fx-background-color: " + webColor);
	}

	private void refresh() {
		// XXX poprawiæ
		updatePixelFont();
		
		getChildren().clear();
		getChildren().add(createNode());
		refreshAllText();
	}

	private void refreshAllText() {
		CharPrinter printer = new CharPrinter(getLineCount(), getCharCount());
		printer.setText(getText());
		printer.consumeChanges((posX, posY, c) -> {
			PixelChar pixelMatrix = fontProvider.getChar(c);
			alphanumerics[posX][posY].setPixelMatrix(pixelMatrix);
		});
	}

	private void updateColors() {
		for (int i = 0; i < getCharCount(); i++) {
			for (int j = 0; j < getLineCount(); j++) {
				alphanumerics[i][j].updatePixels();
			}
		}
		setPaneColor();
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

	public final DoubleProperty charGapXProperty() {
		return this.charGapX;
	}

	public final double getCharGapX() {
		return this.charGapXProperty().get();
	}

	public final void setCharGapX(final double charGapX) {
		this.charGapXProperty().set(charGapX);
	}

	public final DoubleProperty charGapYProperty() {
		return this.charGapY;
	}

	public final double getCharGapY() {
		return this.charGapYProperty().get();
	}

	public final void setCharGapY(final double charGapY) {
		this.charGapYProperty().set(charGapY);
	}

	public final StringProperty textProperty() {
		return this.text;
	}

	public final java.lang.String getText() {
		return this.textProperty().get();
	}

	public final void setText(final java.lang.String text) {
		this.textProperty().set(text);
	}

	public final ObjectProperty<Color> pixelOnColorProperty() {
		return this.pixelOnColor;
	}

	public final javafx.scene.paint.Color getPixelOnColor() {
		return this.pixelOnColorProperty().get();
	}

	public final void setPixelOnColor(final javafx.scene.paint.Color pixelOnColor) {
		this.pixelOnColorProperty().set(pixelOnColor);
	}

	public final ObjectProperty<Color> pixelOffColorProperty() {
		return this.pixelOffColor;
	}

	public final javafx.scene.paint.Color getPixelOffColor() {
		return this.pixelOffColorProperty().get();
	}

	public final void setPixelOffColor(final javafx.scene.paint.Color pixelOffColor) {
		this.pixelOffColorProperty().set(pixelOffColor);
	}

	public final ObjectProperty<Color> backlightColorProperty() {
		return this.backlightColor;
	}

	public final javafx.scene.paint.Color getBacklightColor() {
		return this.backlightColorProperty().get();
	}

	public final void setBacklightColor(final javafx.scene.paint.Color backlightColor) {
		this.backlightColorProperty().set(backlightColor);
	}

	public final ObjectProperty<Font> fontProperty() {
		return this.font;
	}

	public final javafx.scene.text.Font getFont() {
		return this.fontProperty().get();
	}

	public final void setFont(final javafx.scene.text.Font font) {
		this.fontProperty().set(font);
	}

	public final ObjectProperty<HorizontalDeployment> horizontalDeploymentProperty() {
		return this.horizontalDeployment;
	}
	
	public final HorizontalDeployment getHorizontalDeployment() {
		return this.horizontalDeploymentProperty().get();
	}

	public final void setHorizontalDeployment(final HorizontalDeployment horizontalDeployment) {
		this.horizontalDeploymentProperty().set(horizontalDeployment);
	}
	
}