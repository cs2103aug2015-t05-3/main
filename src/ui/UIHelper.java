//@@author A0125496X
/**
 * Provides methods for classes to interact with UIController
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

	public static void createUI() {
		UIController.createUI();
		uiController = UI.getController();
	}

	// give the entire list for output generation
	public static void setTableOutput(List<Task> completeList) {
		uiController.generateTablesOutput(completeList);
	}

	// set UI's output msg
//@@author A0126394B
	public static void setOutputMsg(String s) {
		uiController.setOutputMsg(s);
	}

	public static void showHelpPanel() {
		uiController.showUIHelpOverlay();
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
