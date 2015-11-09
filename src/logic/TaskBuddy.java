/**
 * Start point of the entire program.
 *
 * @author Yan Chan Min Oo
 */

package logic;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

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
	private static final String cmdFileName = "commands.xml";
	private static final String logFileName = "log.log";
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
	private static void init(){
		initLog();
		UIHelper.createUI();
		lp = LanguageProcessor.getInstance();
		if(!lp.init(cmdFileName)){
			log.severe("TaskBuddy: Cmd list init failed");
		}
		UIHelper.setDate(TimeUtil.getUIFormattedDate(System.currentTimeMillis()));
		initTaskFile();
		initTaskTree(taskFileName);
		Command.init();
		initTasks();
	}

	private static void initTasks(){
		Command list = new CmdList();
		resolveCmdAction(list.execute(), list);
	}

	private static void initTaskFile(){
		SettingsFileHandler settings = SettingsFileHandler.getInstance();
		if(!settings.init()){
			UIHelper.setOutputMsg(MSG_TASKFILE_NOTFOUND);
			settings.alterSettingsFile(UIHelper.getUserInput());
		}
		while(!settings.initializeTaskFile()){
			UIHelper.setOutputMsg(MSG_TASKFILE_REPROMPT);
			settings.alterSettingsFile(UIHelper.getUserInput());
		}
		taskFileName = settings.getTaskFile();
	}

	private static void initTaskTree(String filePath) {
		taskTree = TaskTree.newTaskTree(filePath);
	}

	private static void initLog(){
		log = Logger.getLogger("log");
		try {
			log.addHandler(new FileHandler(logFileName));
		} catch (SecurityException e) {
			log.severe("TaskBuddy: " + e);
		} catch (IOException e) {
			log.severe("TaskBuddy: " + e);
		}
	}

	private static void runCommands(){
		do{
			setUITasksCount();
			String in = getInput();
			Command toExecute = lp.resolveCmd(in);
			if(toExecute == null){
				UIHelper.setOutputMsg(MSG_INVALIDCMD);
				continue;
			}

			resolveCmdAction(toExecute.execute(), toExecute);
		} while (true);
	}

	private static void setUITasksCount() {
		int doneCount = taskTree.getFlagCount(FLAG_TYPE.DONE);
		int pendingCount = taskTree.size() - doneCount;
		int overdueCount = taskTree.getOverdueCount();

		UIHelper.setDoneCount(doneCount);
		UIHelper.setPendingCount(pendingCount);
		UIHelper.setOverdueCount(overdueCount);
	}

	private static void resolveCmdAction(CommandAction action, Command executed){
		String outputMsg = action.getOutput();
		List<Task> tasksToDisplay = action.getTaskList();

		if(outputMsg != null){
			UIHelper.setOutputMsg(outputMsg);
		}
		if(tasksToDisplay != null){
			UIHelper.setTableOutput(tasksToDisplay);
		}
		if(action.isUndoable()){
			Command.addHistory(executed);
		}
	}

	private static String getInput(){
		return UIHelper.getUserInput();
	}

}
