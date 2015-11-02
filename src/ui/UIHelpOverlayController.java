package ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class UIHelpOverlayController implements Initializable {

	// Constants
	private final String HTML_FILEPATH = "/ui/assets/UIHelpOverlay.html";

	@FXML private WebView helpContentView;
	@FXML private AnchorPane helpAnchorPane;

	public UIHelpOverlayController() {}

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		String url = UIHelpOverlayController.class.getResource(HTML_FILEPATH).toExternalForm();

		WebEngine webEngine = helpContentView.getEngine();
		webEngine.load(url);
	}

	public void onKeyPressed() {

		//TODO user may need to press CTRL+C to copy the help file
		//TODO 						  ALT+Tab

		UIController uIController = UIController.getUIController();
		uIController.hideUIHelpOverlay();
	}
}
