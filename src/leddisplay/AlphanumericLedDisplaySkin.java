package leddisplay;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import leddisplay.font.PixelChar;
import leddisplay.font.PixelCharDeployer;
import leddisplay.font.PixelRenderer;

public class AlphanumericLedDisplaySkin extends SkinBase<AlphanumericLedDisplay> {
	private Pane pane;
	private AlphanumericChar[][] alphanumerics;
	private PixelRenderer renderer;
	private PixelCharDeployer deployer;
	
	protected AlphanumericLedDisplaySkin(AlphanumericLedDisplay control) {
		super(control);
		construct();
		addBinding();
		
		// XXX
		updatePixelFont();
		refreshAllText();
	}

	private void construct() {
		getChildren().add(createNodes());
	}

	private Node createNodes() {
		pane = new Pane();
		alphanumerics = new AlphanumericChar[getSkinnable().getCharCount()][getSkinnable().getLineCount()];
		setPaneColor();
		double width = AlphanumericChar.calcWidth(getSkinnable());
		double height = AlphanumericChar.calcHeight(getSkinnable());
		for (int i = 0; i < getSkinnable().getCharCount(); i++) {
			for (int j = 0; j < getSkinnable().getLineCount(); j++) {
				AlphanumericChar alphanumeric = new AlphanumericChar(getSkinnable());
				alphanumeric.setLayoutX((width + getSkinnable().getCharGapX()) * i);
				alphanumeric.setLayoutY((height + getSkinnable().getCharGapY()) * j);
				alphanumerics[i][j] = alphanumeric;
				pane.getChildren().add(alphanumeric);
			}
		}
		// if not wrapped, background when padding doesn't work
		return new Group(pane);
	}
	
	private void addBinding() {
		// XXX refresh all - binding couse memory leakage
		getSkinnable().lineCountProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().charCountProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().pixelCountXProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().pixelCountYProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().pixelWidthProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().pixelHeightProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().pixelGapXProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().pixelGapYProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().charGapXProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().charGapYProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().fontProperty().addListener((observable, newValue, oldValue) -> refresh());

		getSkinnable().textProperty().addListener((observable, oldValue, newValue) -> {
			CharPrinter printer = new CharPrinter(getSkinnable().getLineCount(), getSkinnable().getCharCount());
			printer.setText(oldValue);
			printer.initChanges();
			printer.setText(newValue);
			printer.consumeChanges((posX, posY, c) -> {
				PixelChar pixelMatrix = getChar(c);
				alphanumerics[posX][posY].setPixelMatrix(pixelMatrix);
			});
		});
		getSkinnable().pixelOnColorProperty().addListener((observable, oldValue, newValue) -> updateColors());
		getSkinnable().pixelOffColorProperty().addListener((observable, oldValue, newValue) -> updateColors());
		getSkinnable().backlightColorProperty().addListener((observable, oldValue, newValue) -> updateColors());
		getSkinnable().horizontalDeploymentProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().verticalDeploymentProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().horizontalShiftProperty().addListener((observable, newValue, oldValue) -> refresh());
		getSkinnable().verticalShiftProperty().addListener((observable, newValue, oldValue) -> refresh());
	}
	
	private void updatePixelFont() {
		renderer = new PixelRenderer(getSkinnable().getFont());
		
		deployer = new PixelCharDeployer(renderer.getMetrics(), (int)getSkinnable().getPixelCountX(), (int)getSkinnable().getPixelCountY());
		deployer.setHorizontalDeployment(getSkinnable().getHorizontalDeployment());
		deployer.setHorizontalShift((int)getSkinnable().getHorizontalShift());
		deployer.setVerticalDeployment(getSkinnable().getVerticalDeployment());
		deployer.setVerticalShift((int)getSkinnable().getVerticalShift());
	}
	
	private void setPaneColor() {
		String webColor = String.valueOf(getSkinnable().getBacklightColor()).replace("0x", "#");
		getSkinnable().setStyle("-fx-background-color: " + webColor);
	}
	
	private void refresh() {
		// XXX poprawiæ
		updatePixelFont();
		
		getChildren().clear();
		getChildren().add(createNodes());
		refreshAllText();
	}
	
	private void updateColors() {
		for (int i = 0; i < getSkinnable().getCharCount(); i++) {
			for (int j = 0; j < getSkinnable().getLineCount(); j++) {
				alphanumerics[i][j].updatePixels();
			}
		}
		setPaneColor();
	}
	
	private void refreshAllText() {
		CharPrinter printer = new CharPrinter(getSkinnable().getLineCount(), getSkinnable().getCharCount());
		printer.setText(getSkinnable().getText());
		printer.consumeChanges((posX, posY, c) -> {
			PixelChar pixelMatrix = getChar(c);
			alphanumerics[posX][posY].setPixelMatrix(pixelMatrix);
		});
	}
	
	private PixelChar getChar(char c) {
		return deployer.deploy(renderer.getChar(c));
	}
	
}
