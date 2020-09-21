package leddisplay;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import leddisplay.font.PixelFontLoader;

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
		// ------------------------------------------------------------------
		AlphanumericLedDisplayConfig config = new AlphanumericLedDisplayConfig();
		config.setPixelGap(1);
		config.setPixelSize(5);
		AlphanumericLedDisplay display = new AlphanumericLedDisplay(config, new PixelFontLoader("fonts\\casio-fx-9860gii.ttf", 7));
		pane.getChildren().add(display);

		stage.showingProperty().addListener((observable, oldValue, newValue) -> {
			display.clearDisplay();
			display.print("Hello world!", 0, 0);
			display.print("abcdefghijkl", 0, 1);
		});
		// -------------------------------------------------------------------

		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Platform.runLater(() -> {
				display.clearDisplay();
				display.print("ABCDEFGHJKLNMPO", 0, 1);
			});
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Platform.runLater(() -> {
				display.clearDisplay();
				display.print("1234567890", 0, 1);
			});
		}).start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
