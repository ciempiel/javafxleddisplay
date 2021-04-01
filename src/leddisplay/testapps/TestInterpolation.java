package leddisplay.testapps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TestInterpolation extends Application{
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(TestInterpolationController.class.getResource("TestInterpolation.fxml"));
			AnchorPane root = loader.load();
			TestInterpolationController controller = loader.getController();
			controller.setStage(primaryStage);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
