//@@author A0125574A
/**
 * CommandAction is an object which contains a String output message for UI, boolean to
 * determine if command action is undoable, and a list of task(s) to be displayed in UI table,
 * for {@link TaskBuddy} to unpack 
 */

package logic.command;

import java.util.List;

import taskCollections.Task;

public class CommandAction {

	/*
	 * Variables for internal use
	 */
	private String _outputMsg;
	private boolean _isUndoable;
	private List<Task> _taskList;

	/**
	 * Constructs a new CommandAction
	 *
	 * @param outputMsg
	 *            message to be printed above the command line in UI
	 * @param isUndoable
	 *            to determine if a command action can be undone
	 * @param List {@code <Task>}
	 *            list of task(s) to be displayed in UI table
	 */
	public CommandAction(String outputMsg, boolean isUndoable, List<Task> taskList) {
		_outputMsg = outputMsg;
		_isUndoable = isUndoable;
		_taskList = taskList;
	}

	/**
	 * Returns a String message to be printed above the command line in UI
	 *
	 * @return a String message to be printed above the command line in UI
	 */
	public String getOutput() {
		return _outputMsg;
	}

	/**
	 * Returns a boolean to determine if a command action can be undone
	 *
	 * @return a boolean to determine if a command action can be undone
	 */
	public boolean isUndoable() {
		return _isUndoable;
	}

	/**
	 * Returns list of task(s) to be displayed in UI table
	 *
	 * @return a list of task(s) to be displayed in UI table
	 */
	public List<Task> getTaskList() {
		return _taskList;
	}
}
