//@@A0126394B
package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
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

	private static UIController uIController;
	private static UIHelpOverlayController uIHelpOverlayController;
	private static Stage uIHelpStage;
	private static Scene uIHelpScene;
	private static Scene uiMainScene;
	private static boolean isInitialised;

	FXMLLoader uIMainLoader;
	FXMLLoader uIHelpLoader;

//@@A0076510M
	@Override
	public void start(Stage uiMainstage) {

		LogHandler.getLog().finer(LogHandler.LOG_ENTRY);

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

//@@A0126394B
			AnchorPane uiHelpRoot = (AnchorPane) uIHelpLoader.load();
			uIHelpOverlayController = uIHelpLoader.getController();
			uIHelpScene = new Scene(uiHelpRoot);
			uIHelpStage.setScene(uIHelpScene);
			uIHelpStage.initStyle(StageStyle.UTILITY);

			isInitialised = true;

			LogHandler.getLog().finer(LogHandler.LOG_EXIT);

		} catch (IOException e) {
			String msg = String.format(ERR_LOADING_FILE, e);
			LogHandler.getLog().severe(msg);
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
				Point2D uICenterCoord = getWindowCenterCoor(uiMainScene.getWindow());
				Point2D uIHelpStartCoord = getRelativeStartCoorFromCenter(uICenterCoord, uIHelpStage);
				setStagePosition(uIHelpStartCoord, uIHelpStage);
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

	private Point2D getWindowCenterCoor(Window uiWindow) {

		double xStartPos = uiWindow.getX();
		double xMidLen = uiWindow.getWidth() / 2;
		double xPos = xStartPos + xMidLen;

		double yStartPos = uiWindow.getY();
		double yMidLen = uiWindow.getHeight() / 2;
		double yPos = yStartPos + yMidLen;

		return new Point2D(xPos, yPos);
	}

	private Point2D getRelativeStartCoorFromCenter(Point2D center, Stage stage) {
		double width = stage.getWidth();
		double xPos = center.getX() - width/2;

		double height = stage.getHeight();
		double yPos = center.getY() - height/2;

		return new Point2D(xPos, yPos);
	}

	private void setStagePosition(Point2D coord, Stage stage) {
		stage.setX(coord.getX());
		stage.setY(coord.getY());
	}

//@@A0076510M
	public void run() {
		launch();
	}
}
