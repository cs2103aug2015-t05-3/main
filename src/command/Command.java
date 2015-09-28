/**
 * Specifies the format all supported commands should follow, as well as hold the data structure which
 * most supported commands will need.
 * 
 * @author Yan Chan Min Oo
 */

package command;

import java.util.HashMap;
import java.util.Stack;

import tds.Task;

public abstract class Command {
	
	/*
	 * List of data structures to be used by command and its subclasses
	 */
	
	// Stores the list of parameters. Name of parameter -> Value of parameter
	private HashMap<String,String> parameters;
	// Stores the list of command which were executed by the user
	private static Stack<Command> history = new Stack<Command>();
	// The task that the command is trying to manipulate
	private Task task;
	
	public Command(){
		parameters = new HashMap<String,String>();
	}
	
	/**
	 * Executes the command
	 * @return The string to output for user display
	 */
	public abstract String execute();
	
	/**
	 * Undo an operation. Applies to manipulative commands only
	 * @return The string to output for user display
	 */
	public abstract String undo();
	
	/**
	 * Determines if the command manipulates the task data structure in any way
	 * @return True if command modifies the data structure in some way (add, delete etc).
	 *  False otherwise (display, help etc)
	 */
	public abstract boolean isManipulative();
	
	/**
	 * Sets the parameters required for some commands. Should be called before calling
	 * execute()
	 * @param parameterName
	 * 				The name of parameter to set
	 * @param parameterValue
	 * 				The value of parameter to set
	 */
	public void setParameter(String parameterName, String parameterValue){
		parameters.put(parameterName,parameterValue);
	}
	
	/**
	 * Returns the value of a given parameter name.
	 * @param parameterName
	 * 				The parameter name to look up
	 * @return The parameter value it's found, null if it isn't
	 */
	protected String getParameterValue(String parameterName){
		return parameters.get(parameterName);	
	}
	
	/**
	 * Sets the task to be worked on. Should be called before calling executed()
	 * @param t 
	 * 				The task to set
	 */
	public void setTask(Task t){
		task = t;
	}
	
	/**
	 * Returns the task to be worked on
	 * @return The task if it's found, null if it isn't
	 */
	public Task getTask(){
		return task;
	}
	
	/**
	 * Add a command to the list of history. To be called after successfully executing a 
	 * manipulation command
	 * @param toAdd 
	 * 				The command to add to the list of history
	 */
	public void addHistory(Command toAdd){
		history.add(toAdd);
	}
}
