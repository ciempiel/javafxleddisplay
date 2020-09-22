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

	private final AlphanumericLedDisplay display;

	private final Pane pane;

	private Rectangle[][] pixels;

	public AlphanumericChar(AlphanumericLedDisplay display) {
		super();
		this.display = display;
		pane = new Pane();
	}

	public void setPixelMatrix(PixelChar pixelMatrix) {
		for (int i = 0; i < display.getPixelCountX(); i++) {
			for (int j = 0; j < display.getPixelCountY(); j++) {
				Color color = pixelMatrix.isPixelSet(i, j) ? SET_COLOR : CLEAR_COLOR;
				pixels[i][j].setFill(color);
			}
		}
	}

	public void clearPixel() {
		for (int i = 0; i < display.getPixelCountX(); i++) {
			for (int j = 0; j < display.getPixelCountY(); j++) {
				pixels[i][j].setFill(CLEAR_COLOR);
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
		return pane;
	}

	private Rectangle createPixel(int indexX, int indexY) {
		Rectangle pixel = new Rectangle();
		pixel.setWidth(display.getPixelWidth());
		pixel.setHeight(display.getPixelHeight());
		pixel.setLayoutX(getPixelLayoutX(indexX));
		pixel.setLayoutY(getPixelLayoutY(indexY));
		return pixel;
	}
	
	private double getPixelLayoutX(int indexX) {
		return (display.getPixelWidth() + display.getPixelGapX()) * indexX + display.getPixelGapX();
	}
	
	private double getPixelLayoutY(int indexY) {
		return (display.getPixelHeight() + display.getPixelGapY()) * indexY + display.getPixelGapY();
	}
	
	public static double calcWidth(AlphanumericLedDisplay display) {
		return (display.getPixelWidth() + display.getPixelGapX()) * display.getPixelCountX() + display.getPixelGapX();
	}
	
	public static double calcHeight(AlphanumericLedDisplay display) {
		return (display.getPixelHeight() + display.getPixelGapY()) * display.getPixelCountY() + display.getPixelGapY();
	}
}