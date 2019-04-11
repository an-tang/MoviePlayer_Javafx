package application;

import java.awt.Color;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MyScene.fxml"));
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
			scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2) {
						primaryStage.setFullScreen(true);
					} else
						primaryStage.setFullScreen(false);
				}
			});
			primaryStage.setTitle("Movie Explorer");
			// primaryStage.setFullScreen(true);
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
