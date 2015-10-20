/**
 * Start point of the entire program.
 * 
 * @author Yan Chan Min Oo
 */

package logic;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import logic.command.*;
import ui.UIHelper;
import parser.LanguageProcessor;

public class TaskBuddy {
	
	/*
	 * Constants 
	 */
	private static final String cmdFileName = "commands.xml";
	private static final String taskFileName = "tasks.xml";
	private static final String logFileName = "log.log";
	private static final String MSG_INVALIDCMD = "Please enter a valid command. For more info, enter help";
	
	/*
	 * Global variables
	 */
	private static Logger log;
	private static LanguageProcessor lp;

	public static void main(String[] args) {
		// Get task file location
		
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
		lp = new LanguageProcessor();
		if(!lp.init(cmdFileName)){
			log.severe("TaskBuddy: Cmd list init failed");
		}
		Command.init(taskFileName);
		UIHelper.createUI();
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
			String cmd = getInput();
			Command toExecute = lp.resolveCmd(cmd);
			if(toExecute == null){
				UIHelper.appendOutput(MSG_INVALIDCMD);
				continue;
			}
			UIHelper.appendOutput(toExecute.execute().getOutput());
			if(toExecute.isUndoable()){
				Command.addHistory(toExecute);
			}
		} while (true);
	}
	
	private static String getInput(){
		return "";
		//return UIHelper.getUserInput();
	}

}
