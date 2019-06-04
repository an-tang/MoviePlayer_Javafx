package application;

import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Main extends Application {

	public static boolean isFullScreen = false;

	@Override
	public void start(Stage primaryStage) {
		try {
			URL url = getClass().getResource("MainScene.fxml");
			if (url == null) {
				Platform.exit();
				return;
			}
			Parent root = FXMLLoader.load(url);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2) {
						if (!isFullScreen) {
							primaryStage.setFullScreen(true);
							isFullScreen = true;
						} else {
							primaryStage.setFullScreen(false);
							isFullScreen = false;
						}
					}
				}
			});
			primaryStage.setTitle("Movie Explorer");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
