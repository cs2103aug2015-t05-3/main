/**
 * Specifies the format all supported commands should follow, as well as hold the data structure which
 * most supported commands will need.
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
	
	public abstract String execute();
	public abstract String undo();
	
	// True if command modifies the data structure in some way (add, delete etc). False otherwise (display, help etc)
	public abstract boolean isManipulative();
	
	/*
	 * Sets the parameters required for some commands. Should be executed before running
	 * execute()
	 */
	public void setParameter(String parameterName, String parameterValue){
		parameters.put(parameterName,parameterValue);
	}
	
	/*
	 * Returns the parameter value of a given parameter name if it's found, null if it isnt
	 */
	protected String getParameterValue(String parameterName){
		return parameters.get(parameterName);	
	}
	
	/*
	 * Sets the task to be manipulated
	 */
	public void setTask(Task t){
		task = t;
	}
	
	/*
	 * Returns the task to be manipulated
	 */
	public Task getTask(){
		return task;
	}
	
	/*
	 * Add a command to the list of history. To be called after successfully executing a 
	 * manipulation command
	 */
	public void addHistory(Command toAdd){
		history.add(toAdd);
	}
}
