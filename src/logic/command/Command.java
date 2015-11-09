//@@author A0125496X
/**
 * Specifies the format all supported commands should follow, as well as hold the data structure which
 * most supported commands will need.
 *
 * @author Yan Chan Min Oo
 */

package logic.command;

import java.util.HashMap;
import java.util.Stack;

import taskCollections.Task;
import taskCollections.TaskTree;

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
	// The task tree which all commands will reference to
	protected static TaskTree _taskTree;

	public Command(){
		parameters = new HashMap<String,String>();
	}

	/**
	 * Executes the command
	 * @return a CommandAction object
	 */
	public abstract CommandAction execute();

	/**
	 * Undo an operation. Applies to manipulative commands only
	 * @return a CommandAction object
	 */
	public abstract CommandAction undo();

	/**
	 * Get the list of fields that are required to run this command
	 * @return List of required parameter names
	 *
	 */
	public abstract String[] getRequiredFields();

	/**
	 * Get the list of optional fields that can be used to run this command
	 * @return List of required parameter names
	 */
	public abstract String[] getOptionalFields();
	
	/**
	 * Get the message to display the syntax of a particular command
	 * @return String of the message
	 */
	public abstract String getHelpInfo();

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
	 * undoable command
	 * @param toAdd
	 * 				The command to add to the list of history
	 */
	public static void addHistory(Command toAdd){
		history.add(toAdd);
	}

	/**
	 * Gets and removes the last command which was executed
	 * @return The last command which was executed
	 */
	protected static Command extractHistory(){
		if (history.isEmpty()) {	
			return null;			
		} else {					
			return history.pop();
		}							
	}

	public static void init(){
		_taskTree = TaskTree.getTaskTree();
	}
}
