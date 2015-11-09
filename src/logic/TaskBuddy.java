//@@author A0125496X
/**
 * Start point of the entire program.
 *
 * @author Yan Chan Min Oo
 */

package logic;

import java.util.List;
import java.util.logging.Logger;

import logger.LogHandler;
import logic.command.*;
import ui.UIHelper;
import util.TimeUtil;
import parser.LanguageProcessor;
import storage.SettingsFileHandler;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.TaskTree;

public class TaskBuddy {

	/*
	 * Constants
	 */
	private static final String CMD_FILENAME = "commands.xml";
	private static final String MSG_INVALIDCMD = "Please enter a valid command. For more info, enter help";
	private static final String MSG_TASKFILE_NOTFOUND = "Please enter the name or location of file to open or create";
	private static final String MSG_TASKFILE_REPROMPT = "Please enter another file name";
	/*
	 * Global variables
	 */
	private static Logger log;
	private static LanguageProcessor lp;
	private static String taskFileName;
	private static TaskTree taskTree;

	public static void main(String[] args) {

		// Initialise all the variables
		init();

		// (Loop) Execute commands
		runCommands();
	}

	/**
	 * Initialises all the necessary variables
	 */
	private static void init() {
		log = LogHandler.getLog(); // Init the log

		// Set up the UI
		UIHelper.createUI();
		UIHelper.setDate(TimeUtil.getUIFormattedDate(System.currentTimeMillis()));

		// Init the parser component
		lp = LanguageProcessor.getInstance();
		if (!lp.init(CMD_FILENAME)) {
			log.severe("TaskBuddy: Cmd list init failed");
		}

		initTaskFile(); // Init the storage component for tasks
		initTaskTree(taskFileName); // Load the tasks to task collection
		Command.init(); // Init the logic component
		displayTaskList(); // Display the task list on the UI
	}

	/**
	 * Displays the task list
	 */
	private static void displayTaskList() {
		Command list = new CmdList();
		resolveCmdAction(list.execute(), list);
	}

	/**
	 * Creates / Open the task file
	 */
	private static void initTaskFile() {
		SettingsFileHandler settings = SettingsFileHandler.getInstance();

		if (!settings.init()) { // Create the settings file if it's not found
			UIHelper.setOutputMsg(MSG_TASKFILE_NOTFOUND);
			// Write the task file path to settings
			settings.alterSettingsFile(UIHelper.getUserInput());
		}
		
		// Create the task file path defined in settings
		while (!settings.initializeTaskFile()) {
			// Unable to initialise task file. Open/Create another file
			UIHelper.setOutputMsg(MSG_TASKFILE_REPROMPT);
			settings.alterSettingsFile(UIHelper.getUserInput());
		}
		
		taskFileName = settings.getTaskFile(); // Get the final task file name
	}

	private static void initTaskTree(String filePath) {
		taskTree = TaskTree.newTaskTree(filePath);
	}

	/**
	 * Main routine of the program to execute commands
	 */
	private static void runCommands() {
		do {
			setUITasksCount(); // Display the list of statuses of tasks
			String in = getInput(); // Get the input from user
			Command toExecute = lp.resolveCmd(in); // Parse the command
			
			if (toExecute == null) {
				// Unable to parse command
				UIHelper.setOutputMsg(MSG_INVALIDCMD);
				continue;
			}

			// Perform relevant actions from the executed command
			resolveCmdAction(toExecute.execute(), toExecute);
		} while (true);
	}

	//@@author A0126394B
	/**
	 * Displays the number of task counts for overdue, pending, and completed
	 */
	private static void setUITasksCount() {
		int doneCount = taskTree.getFlagCount(FLAG_TYPE.DONE);
		int pendingCount = taskTree.size() - doneCount;
		int overdueCount = taskTree.getOverdueCount();

		UIHelper.setDoneCount(doneCount);
		UIHelper.setPendingCount(pendingCount);
		UIHelper.setOverdueCount(overdueCount);
	}

	/**
	 * Performs necessary actions from the command executed
	 * @param action
	 * 			The result after executing a command
	 * @param executed
	 * 			The command which was executed
	 */
	private static void resolveCmdAction(CommandAction action, Command executed) {
		String outputMsg = action.getOutput();
		List<Task> tasksToDisplay = action.getTaskList();

		if (outputMsg != null) { // Display the message to display after executing a cmd
			UIHelper.setOutputMsg(outputMsg);
		}
		if (tasksToDisplay != null) { // The list of tasks after executing a cmd
			UIHelper.setTableOutput(tasksToDisplay);
		}
		if (action.isUndoable()) { // Add to the list of history if it's undoable
			Command.addHistory(executed);
		}
	}

	private static String getInput() {
		return UIHelper.getUserInput();
	}

}
