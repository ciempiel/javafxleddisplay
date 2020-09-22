package leddisplay.testapps;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import leddisplay.AlphanumericLedDisplay;

public class TestInterpolationController {
	@FXML
	private Spinner<Integer> lineCountSpinner, charCountSpinner, pixelXCountSpinner, pixelYCountSpinner;
	@FXML
	private Spinner<Double> pixelWidthSpinner, pixelHeightSpinner, pixelGapXSpinner, pixelGapYSpinner;
	@FXML
	private AnchorPane displayPane;
	
	private AlphanumericLedDisplay display;
	
	@FXML
	private void initialize() {
		display = new AlphanumericLedDisplay();
		
		initIntegerSpinner(lineCountSpinner, display.lineCountProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_LINE_COUNT, 1);
		initIntegerSpinner(charCountSpinner, display.charCountProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_CHAR_COUNT, 1);
		initIntegerSpinner(pixelXCountSpinner, display.pixelCountXProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_PIXEL_COUNT_X, 1);
		initIntegerSpinner(pixelYCountSpinner, display.pixelCountYProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_PIXEL_COUNT_Y, 1);
		initDoubleSpinner(pixelWidthSpinner, display.pixelWidthProperty(), 0.0, 100.0, 7.0, 1.0);
		initDoubleSpinner(pixelHeightSpinner, display.pixelHeightProperty(), 0.0, 100.0, 7.0, 1.0);
		initDoubleSpinner(pixelGapXSpinner, display.pixelGapXProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_PIXEL_GAP, 1.0);
		initDoubleSpinner(pixelGapYSpinner, display.pixelGapYProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_PIXEL_GAP, 1.0);
		
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

	public void setStage(Stage primaryStage) {
	}
}
