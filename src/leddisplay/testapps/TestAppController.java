package leddisplay.testapps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import leddisplay.AlphanumericLedDisplay;
import leddisplay.font.HorizontalDeployment;
import leddisplay.font.VerticalDeployment;

public class TestAppController {
	@FXML
	private Spinner<Integer> lineCountSpinner, charCountSpinner, pixelXCountSpinner, pixelYCountSpinner;
	@FXML
	private Spinner<Double> pixelWidthSpinner, pixelHeightSpinner, pixelGapXSpinner, pixelGapYSpinner, charGapXSpinner, charGapYSpinner, 
		paddingTopSpinner, paddingRightSpinner, paddingBottomSpinner, paddingLeftSpinner, fontSizeSpinner, horizontalShiftSpinner, verticalShiftSpinner;
	@FXML
	private TextArea textArea;
	@FXML
	private AnchorPane displayPane;
	@FXML
	private ColorPicker pixelOffColorPicker, pixelOnColorPicker, backlightColorPicker;
	@FXML
	private ComboBox<String> fontFamilyCombo, fontStyleCombo;
	@FXML
	private ComboBox<HorizontalDeployment> horizontalDeploymentCombo;
	@FXML
	private ComboBox<VerticalDeployment> verticalDeploymentCombo;

	private AlphanumericLedDisplay display;

	@FXML
	private void initialize() {
		display = new AlphanumericLedDisplay();
		display.setPadding(new Insets(AlphanumericLedDisplay.DEFAULT_PADDING));

		initIntegerSpinner(lineCountSpinner, display.lineCountProperty(), 1, 100, AlphanumericLedDisplay.DEFAULT_LINE_COUNT, 1);
		initIntegerSpinner(charCountSpinner, display.charCountProperty(), 1, 100, 8, 1);
		initIntegerSpinner(pixelXCountSpinner, display.pixelCountXProperty(), 1, 100, 10, 1);
		initIntegerSpinner(pixelYCountSpinner, display.pixelCountYProperty(), 1, 100, 14, 1);
		initDoubleSpinner(pixelWidthSpinner, display.pixelWidthProperty(), 0.0, 100.0, 7.0, 1.0);
		initDoubleSpinner(pixelHeightSpinner, display.pixelHeightProperty(), 0.0, 100.0, 7.0, 1.0);
		initDoubleSpinner(pixelGapXSpinner, display.pixelGapXProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_PIXEL_GAP, 1.0);
		initDoubleSpinner(pixelGapYSpinner, display.pixelGapYProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_PIXEL_GAP, 1.0);
		initDoubleSpinner(charGapXSpinner, display.charGapXProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_CHAR_GAP, 1.0);
		initDoubleSpinner(charGapYSpinner, display.charGapYProperty(), 0.0, 100.0, AlphanumericLedDisplay.DEFAULT_CHAR_GAP, 1.0);
		initPaddingSpinner(paddingTopSpinner, display, AlphanumericLedDisplay.DEFAULT_PADDING, (p, x) -> new Insets(x, p.getRight(), p.getBottom(), p.getLeft()));
		initPaddingSpinner(paddingRightSpinner, display, AlphanumericLedDisplay.DEFAULT_PADDING, (p, x) -> new Insets(p.getTop(), x, p.getBottom(), p.getLeft()));
		initPaddingSpinner(paddingBottomSpinner, display, AlphanumericLedDisplay.DEFAULT_PADDING, (p, x) -> new Insets(p.getTop(), p.getRight(), x, p.getLeft()));
		initPaddingSpinner(paddingLeftSpinner, display, AlphanumericLedDisplay.DEFAULT_PADDING, (p, x) -> new Insets(p.getTop(), p.getRight(), p.getBottom(), x));
		display.textProperty().bind(textArea.textProperty());
		textArea.setText("Hello\r\nWorld!");
		pixelOffColorPicker.setValue(AlphanumericLedDisplay.DEFAULT_PIXEL_OFF_COLOR);
		display.pixelOffColorProperty().bind(pixelOffColorPicker.valueProperty());
		pixelOnColorPicker.setValue(AlphanumericLedDisplay.DEFAULT_PIXEL_ON_COLOR);
		display.pixelOnColorProperty().bind(pixelOnColorPicker.valueProperty());
		backlightColorPicker.setValue(AlphanumericLedDisplay.DEFAULT_BACKLIGHT_COLOR);
		display.backlightColorProperty().bind(backlightColorPicker.valueProperty());
		initFontControl();
		initHorizontalDeploymentCombo();
		initVerticalDeploymentCombo();
		initDoubleSpinner(horizontalShiftSpinner, display.horizontalShiftProperty(), -10, 10, 0, 1);
		initDoubleSpinner(verticalShiftSpinner, display.verticalShiftProperty(), -10, 10, 0, 1);
		//
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

	private void initPaddingSpinner(Spinner<Double> spinner, Region region, double initValue, BiFunction<Insets, Double, Insets> modifyFunction) {
		DoubleSpinnerValueFactory factory = new DoubleSpinnerValueFactory(0.0, 100.0, initValue, 1.0);
		spinner.setValueFactory(factory);
		factory.valueProperty().addListener((observable, oldValue, newValue) -> {
			Insets oldPadding = region.getPadding();
			Insets newPadding = modifyFunction.apply(oldPadding, newValue);
			region.setPadding(newPadding);
		});
	}
	
	private void initFontControl() {
		List<String> families = Font.getFamilies();
		fontFamilyCombo.getItems().addAll(families);
		Font defaultFont = display.getFont();
		fontFamilyCombo.setValue(defaultFont.getFamily());
		DoubleSpinnerValueFactory factory = new DoubleSpinnerValueFactory(1, 100, defaultFont.getSize(), 1);
		fontSizeSpinner.setValueFactory(factory);
		FontStyles fontStyles = new FontStyles();
		fontStyleCombo.getItems().setAll(fontStyles.getStyles(defaultFont.getFamily()));
		fontStyleCombo.getSelectionModel().select("Regular");
		
		fontFamilyCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
			Set<String> styles = fontStyles.getStyles(newValue);
			String selectedStyle = fontStyleCombo.getSelectionModel().getSelectedItem();
			fontStyleCombo.getItems().setAll(styles);
			if (selectedStyle == null || !styles.contains(selectedStyle)) {
				if (styles.contains("Regular")) {
					fontStyleCombo.getSelectionModel().select("Regular");
				} else if (styles.size() > 0) {
					ArrayList<String> stylesList = new ArrayList<>(styles);
					Collections.sort(stylesList);
					fontStyleCombo.getSelectionModel().select(stylesList.get(0));
				}
			} else {
				fontStyleCombo.getSelectionModel().select(selectedStyle);
			}
		});
		
		ObjectBinding<Font> fontBinding = Bindings.createObjectBinding(() -> {
			String family = fontFamilyCombo.getValue();
			double size = fontSizeSpinner.getValue();
			String style = fontStyleCombo.getValue();
			if (style == null) style = "";
			FontWeight weight = style.contains("Bold") ? FontWeight.BOLD : FontWeight.NORMAL;
			FontPosture posture = style.contains("Italic") ? FontPosture.ITALIC : FontPosture.REGULAR;
			return Font.font(family, weight, posture, size);
		}, fontFamilyCombo.valueProperty(), fontSizeSpinner.valueProperty(), fontStyleCombo.valueProperty());
		display.fontProperty().bind(fontBinding);
	}
	
	private void initHorizontalDeploymentCombo() {
		horizontalDeploymentCombo.getItems().addAll(HorizontalDeployment.values());
		horizontalDeploymentCombo.getSelectionModel().select(AlphanumericLedDisplay.DEFAULT_HORIZONTAL_DEPLOYMENT);
		display.horizontalDeploymentProperty().bind(horizontalDeploymentCombo.getSelectionModel().selectedItemProperty());
	}
	
	private void initVerticalDeploymentCombo() {
		verticalDeploymentCombo.getItems().addAll(VerticalDeployment.values());
		verticalDeploymentCombo.getSelectionModel().select(AlphanumericLedDisplay.DEFAULT_VERTICAL_DEPLOYMENT);
		display.verticalDeploymentProperty().bind(verticalDeploymentCombo.getSelectionModel().selectedItemProperty());
	}
	
	public void setStage(Stage primaryStage) {
	}
}
