/**
 * Start point of the entire program.
 * 
 * @author Yan Chan Min Oo
 */

import java.util.Scanner;

import command.*;
import tds.TaskTree;

public class TaskBuddy {
	
	/*
	 * Constants 
	 */
	private static final String configFileName = "config.xml";
	
	/*
	 * Global variables
	 */
	private static Scanner _in;

	public static void main(String[] args) {
		// Load arguments from config file
		
		// Get task file location
		
		// Load tasks
		TaskTree.init();
		
		// Load GUI (NEXT TIME, IGNORE FOR NOW)
		
		// (Loop) Execute commands
		runCommands();
	}
	
	private static void runCommands(){
		String cmd;
		do{
			cmd = getInput();
		} while (!cmd.equals("exit"));
	}
	
	private static String getInput(){
		return _in.nextLine();
	}

}
