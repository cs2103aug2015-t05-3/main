package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class UI extends Application {

	// File path
	private static final String RESOURCE_FILEPATH = "assets/UI.fxml";
	private static final String HELP_FILEPATH = "assets/UIHelpOverlay.fxml";
	private static final String UI_STYLESHEET_FILEPATH = "ui/assets/UIStylesheet.css";
	private static final String ICON_FILEPATH = "assets/UIIcon.png";

	// Message String constants
	private static final String ERR_LOADING_FILE = "Error loading files required to display UI";

	private static final String APP_TITLE = "TaskBuddy";

	private static UIController uIController;
	private static UIHelpOverlayController uIHelpOverlayController;
	private static Stage uIHelpStage;
	private static Scene uIHelpScene;
	private static boolean isInitialised;

	FXMLLoader uIMainLoader;
	FXMLLoader uIHelpLoader;

	@Override
	public void start(Stage uiMainstage) {

		uIHelpStage = new Stage();

		uIMainLoader = new FXMLLoader(getClass().getResource(RESOURCE_FILEPATH));
		uIHelpLoader = new FXMLLoader(getClass().getResource(HELP_FILEPATH));

		try {
			AnchorPane uiMainRoot = (AnchorPane) uIMainLoader.load();
			uIController = uIMainLoader.getController();
			Scene uiMainScene = new Scene(uiMainRoot);
			uiMainstage.setTitle(APP_TITLE);
			uiMainstage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_FILEPATH)));
			uiMainstage.setScene(uiMainScene);
			uiMainstage.setResizable(false);
			uiMainstage.setOnCloseRequest(e -> System.exit(0));
			uiMainScene.getStylesheets().add(UI_STYLESHEET_FILEPATH);
			uiMainstage.show();

			AnchorPane uiHelpRoot = (AnchorPane) uIHelpLoader.load();
			uIHelpOverlayController = uIHelpLoader.getController();
			uIHelpScene = new Scene(uiHelpRoot);
			uIHelpStage.setScene(uIHelpScene);
			uIHelpStage.initStyle(StageStyle.UTILITY);

			isInitialised = true;

		} catch (IOException e) {
			System.err.println(ERR_LOADING_FILE);
		}
	}

	public static UIController getController() {
		return uIController;
	}

	public static UIHelpOverlayController getUIHelpOverlayController() {
		return uIHelpOverlayController;
	}

	public boolean isInitialised() {
		return isInitialised;
	}

	public void showUIHelpOverlayStage() {

		// Platform.runLater is required to modify UI after starting JavaFx thread.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				uIHelpStage.show();
			}
		});
	}

	public void hideUIHelpOverlayStage() {

		// Platform.runLater is required to modify UI after starting JavaFx thread.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				uIHelpStage.hide();
			}
		});
	}

	public void run() {
		launch();
	}
}
