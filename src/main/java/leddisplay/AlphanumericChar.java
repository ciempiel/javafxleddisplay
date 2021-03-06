package leddisplay;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import leddisplay.font.EmptyPixelChar;
import leddisplay.font.PixelChar;

class AlphanumericChar extends Control {
	private final AlphanumericLedDisplay display;
	private final Pane pane;

	private Rectangle[][] pixels;
	private PixelChar pixelMatrix;

	public AlphanumericChar(AlphanumericLedDisplay display) {
		super();
		this.display = display;
		pane = new Pane();
		pixelMatrix = new EmptyPixelChar();
	}

	public void setPixelMatrix(PixelChar pixelMatrix) {
		this.pixelMatrix = pixelMatrix;
		updatePixelColors();
	}
	
	public void updatePixelColors() {
		if (pixels == null) {
			return;
		}
		for (int i = 0; i < display.getPixelCountX(); i++) {
			for (int j = 0; j < display.getPixelCountY(); j++) {
				Color color = pixelMatrix.getPixelValue(i, j) != 0 ? display.getPixelOnColor() : display.getPixelOffColor();
				pixels[i][j].setFill(color);
			}
		}
	}

	public void clearPixelColors() {
		for (int i = 0; i < display.getPixelCountX(); i++) {
			for (int j = 0; j < display.getPixelCountY(); j++) {
				pixels[i][j].setFill(display.getPixelOffColor());
			}
		}
	}
	
	public void updatePixelsBounds() {
		for (int i = 0; i < display.getPixelCountX(); i++) {
			for (int j = 0; j < display.getPixelCountY(); j++) {
				setPixelBounds(pixels[i][j], i, j);
			}
		}
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
		pixels = new Rectangle[display.getPixelCountX()][display.getPixelCountY()];
		for (int i = 0; i < display.getPixelCountX(); i++) {
			for (int j = 0; j < display.getPixelCountY(); j++) {
				Rectangle pixel = createPixel(i, j);
				pane.getChildren().add(pixel);
				pixels[i][j] = pixel;
			}
		}
		updatePixelColors();
		return pane;
	}

	private Rectangle createPixel(int indexX, int indexY) {
		Rectangle pixel = new Rectangle();
		setPixelBounds(pixel, indexX, indexY);
		pixel.setFill(display.getPixelOffColor());
		return pixel;
	}

	private void setPixelBounds(Rectangle pixel, int indexX, int indexY) {
		pixel.setWidth(display.getPixelWidth());
		pixel.setHeight(display.getPixelHeight());
		pixel.setLayoutX(getPixelLayoutX(indexX));
		pixel.setLayoutY(getPixelLayoutY(indexY));
	}
	
	private double getPixelLayoutX(int indexX) {
		return (display.getPixelWidth() + display.getPixelGapX()) * indexX;
	}
	
	private double getPixelLayoutY(int indexY) {
		return (display.getPixelHeight() + display.getPixelGapY()) * indexY;
	}
	
	public static double calcWidth(AlphanumericLedDisplay display) {
		return (display.getPixelWidth() + display.getPixelGapX()) * display.getPixelCountX() - display.getPixelGapX();
	}
	
	public static double calcHeight(AlphanumericLedDisplay display) {
		return (display.getPixelHeight() + display.getPixelGapY()) * display.getPixelCountY() - display.getPixelGapY();
	}
}