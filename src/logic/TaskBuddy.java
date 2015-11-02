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
import parser.LanguageProcessor;
import storage.SettingsFileHandler;
import taskCollections.Task;

public class TaskBuddy {
	
	/*
	 * Constants 
	 */
	private static final String cmdFileName = "commands.xml";
	private static final String logFileName = "log.log";
	private static final String FILE_NAME_SETTING = "settings.cfg";
	private static final String MSG_INVALIDCMD = "Please enter a valid command. For more info, enter help";
	private static final String MSG_TASKFILE_NOTFOUND = "Please enter the task file location";
	
	/*
	 * Global variables
	 */
	private static Logger log;
	private static LanguageProcessor lp;
	private static String taskFileName;
	private static SettingsFileHandler settings;

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
		initTaskFile();
		lp = new LanguageProcessor();
		if(!lp.init(cmdFileName)){
			log.severe("TaskBuddy: Cmd list init failed");
		}
		Command.init(taskFileName);
		initTasks();
	}
	
	private static void initTasks(){
		// Run list: TODO change implementation
		
		Command list = new CmdList();
		resolveCmdAction(list.execute(), list);
	}
	
	private static void initTaskFile(){
		settings = SettingsFileHandler.getInstance();
		if(settings.init()){
			taskFileName = settings.getTaskFile(); // TODO: if null.. do else
		} else {
			do {
				UIHelper.setOutputMsg(MSG_TASKFILE_NOTFOUND);
				taskFileName = UIHelper.getUserInput();
				settings.alterSettingsFile(taskFileName);
				settings.createTaskFile();
			} while((taskFileName = settings.getTaskFile()) == null);
		}
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
			String in = getInput();
			Command toExecute = lp.resolveCmd(in);
			if(toExecute == null){
				UIHelper.setOutputMsg(MSG_INVALIDCMD);
				continue;
			}
			/*UIHelper.appendOutput(toExecute.execute().getOutput());
			if(toExecute.isUndoable()){
				Command.addHistory(toExecute);
			}*/
			resolveCmdAction(toExecute.execute(), toExecute);
		} while (true);
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
