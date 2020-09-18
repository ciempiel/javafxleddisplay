package leddisplay;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import leddisplay.font.Test5x7PixelFontABC;

public class TestApp extends Application {
	private AnchorPane root;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = new AnchorPane();
			addDisplay(root, primaryStage);
			Scene scene = new Scene(root, 800, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addDisplay(AnchorPane pane, Stage stage) {
		AlphanumericLedDisplayConfig config = new AlphanumericLedDisplayConfig();
		config.setPixelGap(1);
		config.setPixelSize(5);
		AlphanumericLedDisplay display = new AlphanumericLedDisplay(config, new Test5x7PixelFontABC());
		pane.getChildren().add(display);

		stage.showingProperty().addListener((observable, oldValue, newValue) -> {
			display.clearDisplay();
			display.print("ABC", 0, 0);
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
