package ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class UIHelpOverlay {

	private static final String STYLESHEET_FILEPATH = "ui/assets/UIStylesheet.css";
	private static final String RESOURCE_FILEPATH = "assets/UIHelpOverlay.fxml";
	private static final String ICON_FILEPATH = "assets/icon.png";
	private static final String APP_TITLE = "TaskBuddy v0.4";

	UIHelpOverlayController uiHelpOverlayController;

	public void start(Stage stage) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_FILEPATH));
		try {
			AnchorPane root = (AnchorPane) loader.load();

			uiHelpOverlayController = loader.getController();
			Scene scene = new Scene(root);
			stage.setTitle(APP_TITLE);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			//TODO exit program?
		}
	}
}
