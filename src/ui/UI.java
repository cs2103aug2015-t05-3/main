package ui;

import java.io.IOException;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logger.LogHandler;
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
	private static final String ERR_LOADING_FILE = "Error loading UI: ";
	private static final String APP_TITLE = "TaskBuddy";
	private static final String METHOD_START = "start";

	private static UIController uIController;
	private static UIHelpOverlayController uIHelpOverlayController;
	private static Stage uIHelpStage;
	private static Scene uIHelpScene;
	private static Scene uiMainScene;
	private static boolean isInitialised;

	FXMLLoader uIMainLoader;
	FXMLLoader uIHelpLoader;

	@Override
	public void start(Stage uiMainstage) {

		LogHandler.getLog().entering(getClass().toString(), METHOD_START);

		uIHelpStage = new Stage();

		uIMainLoader = new FXMLLoader(getClass().getResource(RESOURCE_FILEPATH));
		uIHelpLoader = new FXMLLoader(getClass().getResource(HELP_FILEPATH));

		try {
			AnchorPane uiMainRoot = (AnchorPane) uIMainLoader.load();
			uIController = uIMainLoader.getController();
			uiMainScene = new Scene(uiMainRoot);
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

			LogHandler.getLog().exiting(getClass().toString(), METHOD_START);

		} catch (IOException e) {
			String msg = String.format(ERR_LOADING_FILE, e);
			LogHandler.log(Level.SEVERE, getClass().toString(), msg);

			System.err.println(msg);


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

				Point2D uICenterCoord = getScreenCenterCoor();

				double width = uIHelpStage.getWidth();
				double startX = uICenterCoord.getX() - width/2;
				uIHelpStage.setX(startX);

				double height = uIHelpStage.getHeight();
				double startY = uICenterCoord.getY() - height/2;
				uIHelpStage.setY(startY);
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

	private Point2D getScreenCenterCoor() {

		double xStartPos = uiMainScene.getWindow().getX();
		double yStartPos = uiMainScene.getWindow().getY();

		double xMidLen = uiMainScene.getWindow().getWidth() / 2;
		double yMidLen = uiMainScene.getWindow().getHeight() / 2;

		double xPos = xStartPos + xMidLen;
		double yPos = yStartPos + yMidLen;

		return new Point2D(xPos, yPos);
	}

	public void run() {
		launch();
	}
}
