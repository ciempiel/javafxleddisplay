package leddisplay;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import leddisplay.font.PixelChar;

class AlphanumericChar extends Control {
	private final static Color SET_COLOR = Color.rgb(21, 57, 31);
	private final static Color CLEAR_COLOR = Color.rgb(92, 114, 98);

	private final AlphanumericLedDisplayConfig config;

	public AlphanumericChar(AlphanumericLedDisplayConfig config) {
		super();
		this.config = config;
	}

	private Rectangle[][] pixels;

	public void setPixelMatrix(PixelChar pixelMatrix) {
		for (int i = 0; i < config.getPixelXCount(); i++) {
			for (int j = 0; j < config.getPixelYCount(); j++) {
				Color color = pixelMatrix.isPixelSet(i, j) ? SET_COLOR : CLEAR_COLOR;
				pixels[i][j].setFill(color);
			}
		}
	}

	public void clearPixel() {
		for (int i = 0; i < config.getPixelXCount(); i++) {
			for (int j = 0; j < config.getPixelYCount(); j++) {
				pixels[i][j].setFill(CLEAR_COLOR);
			}
		}
	}

	public double calcWidth() {
		return config.getPixelXCount() * config.getPixelSize() + 6 * config.getPixelGap();
	}

	public double calcHeight() {
		return config.getPixelYCount() * config.getPixelSize() + 8 * config.getPixelGap();
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new SkinBase<AlphanumericChar>(this) {
			{
				getChildren().add(createPane());
			}
		};
	}

	private Pane createPane() {
		Pane pane = new Pane();
		pixels = new Rectangle[config.getPixelXCount()][config.getPixelYCount()];
		for (int i = 0; i < config.getPixelXCount(); i++) {
			for (int j = 0; j < config.getPixelYCount(); j++) {
				Rectangle pixel = createRectange();
				pixel.setLayoutX(calcPixelLayoutX(i));
				pixel.setLayoutY(calcPixelLayoutY(j));
				pane.getChildren().add(pixel);
				pixels[i][j] = pixel;
			}
		}
		pane.setPrefWidth(calcWidth());
		pane.setPrefHeight(calcHeight());
		pane.setStyle("-fx-background-color: #57a448");
		// pane.prefWidthProperty().bind(getSkinnable().prefWidthProperty());
		// pane.prefHeightProperty().bind(getSkinnable().prefHeightProperty());
		return pane;
	}

	private double calcPixelLayoutX(int indexX) {
		return (config.getPixelSize() + config.getPixelGap()) * indexX + config.getPixelGap();
	}

	private double calcPixelLayoutY(int indexY) {
		return (config.getPixelSize() + config.getPixelGap()) * indexY + config.getPixelGap();
	}

	private Rectangle createRectange() {
		Rectangle rect = new Rectangle();
		rect.setWidth(config.getPixelSize());
		rect.setHeight(config.getPixelSize());
		return rect;
	}
}