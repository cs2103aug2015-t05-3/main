package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class UI extends Application {

	private static final String RESOURCE_FILEPATH = "UI.fxml";
	private static final String ICON_FILEPATH = "bal.png";
	private static final String APP_TITLE = "TaskBuddy v0.2";

	@Override
	public void start(Stage stage) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(RESOURCE_FILEPATH));

			Scene scene = new Scene(root);
	        stage.setTitle(APP_TITLE);
	        stage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_FILEPATH)));
	        stage.setScene(scene);
	        stage.setResizable(false);
	        stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void run() {
		launch();
	}
}
