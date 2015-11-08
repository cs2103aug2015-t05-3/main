/**
 * Provides methods for classes to interact with UIFrame
 *
 * @author Yan Chan Min Oo
 */
package ui;

import java.util.List;
import taskCollections.Task;

public class UIHelper {

	private static UIController uiController;

	/**
	 * Returns a string of the user input. Triggered on enter press.
	 */

	public static String getUserInput() {
		return uiController.getInput();
	}

	public static void loadTask() {

	}

	public static void createUI() {
		UIController.createUI();
		uiController = UI.getController();
	}

	/**
	 * Destroys the UI
	 */

	public static void destroyUI() {
		// frame.dispose();
	}

	public static void appendOutput(String appendString) {
		// frame.setOutputText(frame.getOutputText() + appendString +
		// System.lineSeparator());
	}

	// give the entire list for output generation
	public static void setTableOutput(List<Task> completeList) {
		uiController.generateTablesOutput(completeList);
	}

	// set UI's output msg
	public static void setOutputMsg(String s) {
		uiController.setOutputMsg(s);
	}

	public static void showHelpPanel() {
		uiController.showUIHelpOverlay();
	}

	public static void setUserInput(String in) {
		// frame.setInputText(in);
	}

	public static void setDate(String date) {
		uiController.setTimeDateMsg(date);
	}

	public static void setPendingCount(int count) {
		uiController.setPendingCount(count);
	}

	public static void setOverdueCount(int count) {
		uiController.setOverdueCount(count);
	}

	public static void setDoneCount(int count) {
		uiController.setDoneCount(count);
	}

}
