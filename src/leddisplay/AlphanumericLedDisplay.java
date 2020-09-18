package leddisplay;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import leddisplay.font.PixelFont;
import leddisplay.font.PixelChar;
import leddisplay.font.Test5x7PixelFontABC;

public class AlphanumericLedDisplay extends Control {
	private final AlphanumericLedDisplayConfig config;
	private final PixelFont font;
	private AlphanumericChar[][] alphanumerics;
	
	public AlphanumericLedDisplay(AlphanumericLedDisplayConfig config, PixelFont font) {
		super();
		this.config = config;
		this.font = font;
		alphanumerics = new AlphanumericChar[config.getCharCount()][config.getLineCount()];
	}

	public AlphanumericLedDisplay(AlphanumericLedDisplayConfig config) {
		this(config, new Test5x7PixelFontABC());
	}

	// public void print(String text) {
	//
	// }

	public void print(String text, int posX, int posY) {
		int lastPos = Math.min(config.getCharCount(), posX + text.length());
		int charIndex = 0;
		for (;posX < lastPos; posX++) {
			char c = text.charAt(charIndex);
			PixelChar pixelMatrix = font.getChar(c);
			alphanumerics[posX][posY].setPixelMatrix(pixelMatrix);
			charIndex++;
		}
	}
	
	public void clearDisplay() {
		for (int i = 0; i < config.getCharCount(); i++) {
			for (int j = 0; j < config.getLineCount(); j++) {
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
		for (int i = 0; i < config.getCharCount(); i++) {
			for (int j = 0; j < config.getLineCount(); j++) {
				AlphanumericChar alphanumeric = new AlphanumericChar(config);
				double layoutX = i * alphanumeric.calcWidth();
				alphanumeric.setLayoutX(layoutX);
				double layoutY = j * alphanumeric.calcHeight();
				alphanumeric.setLayoutY(layoutY);
				alphanumerics[i][j] = alphanumeric;
				pane.getChildren().add(alphanumeric);
			}
		}
		return pane;
	}

}