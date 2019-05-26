package application;

import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Main extends Application {

	boolean isFullSceen = false;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			URL url = getClass().getResource("MyScene.fxml");
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
						if (!isFullSceen) {
							primaryStage.setFullScreen(true);
							isFullSceen = true;
						}
						else {
							primaryStage.setFullScreen(false);
							isFullSceen = false;
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
