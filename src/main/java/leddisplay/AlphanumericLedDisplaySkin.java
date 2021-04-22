package leddisplay;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import leddisplay.font.PixelChar;
import leddisplay.font.PixelCharDeployer;
import leddisplay.font.PixelRenderer;

public class AlphanumericLedDisplaySkin extends SkinBase<AlphanumericLedDisplay> {
	private AlphanumericChar[][] alphanumerics;
	private PixelRenderer renderer;
	private PixelCharDeployer deployer;
	
	protected AlphanumericLedDisplaySkin(AlphanumericLedDisplay control) {
		super(control);
		updateAll();
		addBinding();
	}

	private void updateAlphanumerics() {
		getChildren().clear();
		getChildren().add(createNodes());
	}

	private Node createNodes() {
		Pane pane = new Pane();
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
		getSkinnable().lineCountProperty().addListener((observable, newValue, oldValue) -> updateAll());
		getSkinnable().charCountProperty().addListener((observable, newValue, oldValue) -> updateAll());
		getSkinnable().pixelCountXProperty().addListener((observable, newValue, oldValue) -> updateAll());
		getSkinnable().pixelCountYProperty().addListener((observable, newValue, oldValue) -> updateAll());
		getSkinnable().pixelWidthProperty().addListener((observable, newValue, oldValue) -> updateBounds());
		getSkinnable().pixelHeightProperty().addListener((observable, newValue, oldValue) -> updateBounds());
		getSkinnable().pixelGapXProperty().addListener((observable, newValue, oldValue) -> updateBounds());
		getSkinnable().pixelGapYProperty().addListener((observable, newValue, oldValue) -> updateBounds());
		getSkinnable().charGapXProperty().addListener((observable, newValue, oldValue) -> updateBounds());
		getSkinnable().charGapYProperty().addListener((observable, newValue, oldValue) -> updateBounds());
		getSkinnable().fontProperty().addListener((observable, newValue, oldValue) -> updateFont());
		getSkinnable().textProperty().addListener((observable, oldValue, newValue) -> updateText(oldValue, newValue));
		getSkinnable().pixelOnColorProperty().addListener((observable, oldValue, newValue) -> updateColors());
		getSkinnable().pixelOffColorProperty().addListener((observable, oldValue, newValue) -> updateColors());
		getSkinnable().backlightColorProperty().addListener((observable, oldValue, newValue) -> updateColors());
		getSkinnable().horizontalDeploymentProperty().addListener((observable, newValue, oldValue) -> updateDeployerAndAllText());
		getSkinnable().verticalDeploymentProperty().addListener((observable, newValue, oldValue) -> updateDeployerAndAllText());
		getSkinnable().horizontalShiftProperty().addListener((observable, newValue, oldValue) -> updateDeployerAndAllText());
		getSkinnable().verticalShiftProperty().addListener((observable, newValue, oldValue) -> updateDeployerAndAllText());
	}

	private void updateText(String oldValue, String newValue) {
		CharPrinter printer = new CharPrinter(getSkinnable().getLineCount(), getSkinnable().getCharCount());
		printer.setText(oldValue);
		printer.initChanges();
		printer.setText(newValue);
		printer.consumeChanges((posX, posY, c) -> {
			PixelChar pixelMatrix = getChar(c);
			alphanumerics[posX][posY].setPixelMatrix(pixelMatrix);
		});
	}
	
	private void updateAll() {
		updateAlphanumerics();
		updateFontRenderer();
		updateDeployer();
		updateAllText();
	}

	private void updateFontRenderer() {
		renderer = new PixelRenderer(getSkinnable().getFont());		
	}
	
	private void updateFont() {
		updateFontRenderer();
		updateDeployer();
		updateAllText();
	}

	private void updateDeployer() {
		deployer = new PixelCharDeployer(renderer.getMetrics(), (int)getSkinnable().getPixelCountX(), (int)getSkinnable().getPixelCountY());
		deployer.setHorizontalDeployment(getSkinnable().getHorizontalDeployment());
		deployer.setHorizontalShift((int)getSkinnable().getHorizontalShift());
		deployer.setVerticalDeployment(getSkinnable().getVerticalDeployment());
		deployer.setVerticalShift((int)getSkinnable().getVerticalShift());
	}
	
	private void updateDeployerAndAllText() {
		updateDeployer();
		updateAllText();
	}
	
	private void updateBounds() {
		double width = AlphanumericChar.calcWidth(getSkinnable());
		double height = AlphanumericChar.calcHeight(getSkinnable());
		for (int x = 0; x < alphanumerics.length; x++) {
			for (int y = 0; y < alphanumerics[0].length; y++) {
				alphanumerics[x][y].setLayoutX((width + getSkinnable().getCharGapX()) * x);
				alphanumerics[x][y].setLayoutY((height + getSkinnable().getCharGapY()) * y);
				alphanumerics[x][y].updatePixelsBounds();
			}
		}
	}
	
	private void updateColors() {
		for (int i = 0; i < getSkinnable().getCharCount(); i++) {
			for (int j = 0; j < getSkinnable().getLineCount(); j++) {
				alphanumerics[i][j].updatePixelColors();
			}
		}
		setPaneColor();
	}
	
	private void updateAllText() {
		CharPrinter printer = new CharPrinter(getSkinnable().getLineCount(), getSkinnable().getCharCount());
		printer.setText(getSkinnable().getText());
		printer.consumeChanges((posX, posY, c) -> {
			PixelChar pixelMatrix = getChar(c);
			alphanumerics[posX][posY].setPixelMatrix(pixelMatrix);
		});
	}
	
	private void setPaneColor() {
		String webColor = String.valueOf(getSkinnable().getBacklightColor()).replace("0x", "#");
		getSkinnable().setStyle("-fx-background-color: " + webColor);
	}
	
	private PixelChar getChar(char c) {
		return deployer.deploy(renderer.getChar(c));
	}
	
}
