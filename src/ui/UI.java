package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;;

public class UI extends Application {

	private static final String STYLESHEET_FILEPATH = "ui/assets/UIStylesheet.css";
	private static final String RESOURCE_FILEPATH = "assets/UI.fxml";
	private static final String HELP_FILEPATH = "assets/UIHelpOverlay.fxml";
	private static final String ICON_FILEPATH = "assets/icon.png";
	private static final String APP_TITLE = "TaskBuddy v0.4";

	private static UIController uiController;
	private static Stage uiHelpOverlayStage;
	private static boolean isInitialised;

	@Override
	public void start(Stage stage) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_FILEPATH));
		FXMLLoader loaderHelp = new FXMLLoader(getClass().getResource(HELP_FILEPATH));
		try {
			AnchorPane root = (AnchorPane) loader.load();
			AnchorPane rootHelp = (AnchorPane) loaderHelp.load();

			uiController = loader.getController();
			Scene scene = new Scene(root);
			stage.setTitle(APP_TITLE);
			stage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_FILEPATH)));
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();

			uiHelpOverlayStage = new Stage();
			Scene sceneHelp = new Scene(rootHelp);
			uiHelpOverlayStage.setScene(sceneHelp);

			scene.getStylesheets().add(STYLESHEET_FILEPATH);

			isInitialised = true;
		} catch (IOException e) {
			e.printStackTrace();
			//TODO exit program?
		}
	}

	public static UIController getController() {
		return uiController;
	}

	public boolean isInitialised() {
		return isInitialised;
	}

	public static Stage getUIHelpOverlayStage() {
		return uiHelpOverlayStage;
	}

	public void run() {
		launch();
	}
}
