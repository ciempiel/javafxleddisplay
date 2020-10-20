package leddisplay.testapps;

import java.util.function.BiFunction;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import leddisplay.AlphanumericLedDisplay;
import leddisplay.font.PixelFontLoader;

public class TestInterpolationController {
	@FXML
	private Spinner<Integer> lineCountSpinner, charCountSpinner, pixelXCountSpinner, pixelYCountSpinner;
	@FXML
	private Spinner<Double> pixelWidthSpinner, pixelHeightSpinner, pixelGapXSpinner, pixelGapYSpinner, charGapXSpinner, charGapYSpinner, 
		paddingTopSpinner, paddingRightSpinner, paddingBottomSpinner, paddingLeftSpinner;
	@FXML
	private TextArea textArea;
	@FXML
	private AnchorPane displayPane;
	@FXML
	private ColorPicker pixelOffColorPicker, pixelOnColorPicker, backlightColorPicker;

	private AlphanumericLedDisplay display;

	@FXML
	private void initialize() {
		PixelFontLoader font = new PixelFontLoader("fonts\\casio-fx-9860gii.ttf", 7);
		display = new AlphanumericLedDisplay(font);

		initIntegerSpinner(lineCountSpinner, display.lineCountProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_LINE_COUNT, 1);
		initIntegerSpinner(charCountSpinner, display.charCountProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_CHAR_COUNT, 1);
		initIntegerSpinner(pixelXCountSpinner, display.pixelCountXProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_PIXEL_COUNT_X, 1);
		initIntegerSpinner(pixelYCountSpinner, display.pixelCountYProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_PIXEL_COUNT_Y, 1);
		initDoubleSpinner(pixelWidthSpinner, display.pixelWidthProperty(), 0.0, 100.0, 7.0, 1.0);
		initDoubleSpinner(pixelHeightSpinner, display.pixelHeightProperty(), 0.0, 100.0, 7.0, 1.0);
		initDoubleSpinner(pixelGapXSpinner, display.pixelGapXProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_PIXEL_GAP, 1.0);
		initDoubleSpinner(pixelGapYSpinner, display.pixelGapYProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_PIXEL_GAP, 1.0);
		initDoubleSpinner(charGapXSpinner, display.charGapXProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_CHAR_GAP, 1.0);
		initDoubleSpinner(charGapYSpinner, display.charGapYProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_CHAR_GAP, 1.0);
		initPaddingSpinner(paddingTopSpinner, display, (p, x) -> new Insets(x, p.getRight(), p.getBottom(), p.getLeft()));
		initPaddingSpinner(paddingRightSpinner, display, (p, x) -> new Insets(p.getTop(), x, p.getBottom(), p.getLeft()));
		initPaddingSpinner(paddingBottomSpinner, display, (p, x) -> new Insets(p.getTop(), p.getRight(), x, p.getLeft()));
		initPaddingSpinner(paddingLeftSpinner, display, (p, x) -> new Insets(p.getTop(), p.getRight(), p.getBottom(), x));
		display.textProperty().bind(textArea.textProperty());
		pixelOffColorPicker.setValue(AlphanumericLedDisplay.DEFAULT_PIXEL_OFF_COLOR);
		display.pixelOffColorProperty().bind(pixelOffColorPicker.valueProperty());
		pixelOnColorPicker.setValue(AlphanumericLedDisplay.DEFAULT_PIXEL_ON_COLOR);
		display.pixelOnColorProperty().bind(pixelOnColorPicker.valueProperty());
		backlightColorPicker.setValue(AlphanumericLedDisplay.DEFAULT_BACKLIGHT_COLOR);
		display.backlightColorProperty().bind(backlightColorPicker.valueProperty());
		displayPane.getChildren().add(display);
	}

	private void initDoubleSpinner(Spinner<Double> spinner, DoubleProperty property, double min, double max, double initValue, double amountToStepBy) {
		DoubleSpinnerValueFactory factory = new DoubleSpinnerValueFactory(min, max, initValue, amountToStepBy);
		spinner.setValueFactory(factory);
		property.bind(factory.valueProperty());
	}

	private void initIntegerSpinner(Spinner<Integer> spinner, IntegerProperty property, int min, int max, int initValue, int amountToStepBy) {
		IntegerSpinnerValueFactory factory = new IntegerSpinnerValueFactory(min, max, initValue, amountToStepBy);
		spinner.setValueFactory(factory);
		property.bind(factory.valueProperty());
	}

	private void initPaddingSpinner(Spinner<Double> spinner, Region region, BiFunction<Insets, Double, Insets> modifyFunction) {
		DoubleSpinnerValueFactory factory = new DoubleSpinnerValueFactory(0.0, 100.0, 0.0, 1.0);
		spinner.setValueFactory(factory);
		factory.valueProperty().addListener((observable, oldValue, newValue) -> {
			Insets oldPadding = region.getPadding();
			Insets newPadding = modifyFunction.apply(oldPadding, newValue);
			region.setPadding(newPadding);
		});
	}
	
	public void setStage(Stage primaryStage) {
	}
}
