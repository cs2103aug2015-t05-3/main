/**
 * Start point of the entire program.
 * 
 * @author Yan Chan Min Oo
 */

package logic;

import java.util.Scanner;
import command.*;
import tds.TaskTree;
import parser.LanguageProcessor;

public class TaskBuddy {
	
	/*
	 * Constants 
	 */
	private static final String cmdFileName = "commands.xml";
	private static final String taskFileName = "tasks.xml";
	
	/*
	 * Global variables
	 */
	private static Scanner _in;
	private static LanguageProcessor lp;

	public static void main(String[] args) {
		// Get task file location
		
		// Load tasks
		init();
		
		// Load GUI (NEXT TIME, IGNORE FOR NOW)
		
		// (Loop) Execute commands
		runCommands();
	}
	
	/**
	 * Initialises all the necessary variables
	 */
	private static void init(){
		_in = new Scanner(System.in);
		lp = new LanguageProcessor(cmdFileName);
		TaskTree.init(taskFileName);
	}
	
	private static void runCommands(){
		String cmd;
		do{
			System.out.print("Command: ");
			cmd = getInput();
			Command toExecute = lp.resolveCmd(cmd);
			if(toExecute == null){
				System.out.println("Invalid cmd!");
				continue;
			}
			System.out.println(toExecute.execute());
			if(toExecute.isUndoable()){
				Command.addHistory(toExecute);
			}
		} while (!cmd.equals("exit"));
		_in.close();
	}
	
	public static String getInput(){
		return _in.nextLine();
	}
	
	public static void printMessage(String message){
		System.out.println(message);
	}

}
