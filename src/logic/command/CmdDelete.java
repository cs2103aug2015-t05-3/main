//@@author A0125574A

/**
 * Command to delete a specified {@code Task} 
 */

package logic.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import constants.CmdParameters;
import logger.LogHandler;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;

public class CmdDelete extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKDELETED = "Deleted : \"%1$s\"";

	// Help Info
	private static final String HELP_INFO_DELETE = "<task_ID>";

	// Log Message
	private static final String LOG_NUMBERFORMATEXCEPTION = "\"%1$s\" is not an integer";
	private static final String LOG_DELETE_SUCCESS = "Delete Task Success";
	private static final String LOG_DELETE_FAIL = "Delete Task Fail";

	// Variable constant
	private static final int INVALID_TASKID = -1;

	/*
	 * Variables for internal use
	 */
	private Task _task;
	private int _taskID;

	private static Logger log = Logger.getLogger("log_CmdDelete");

	public CmdDelete() {

	}

	/**
	 * Deletes a specified {@code Task}
	 * 
	 * @return a CommandAction
	 */
	@Override
	public CommandAction execute() {

		// Try undo first
		_task = getTask();
		if (isUndo()) {
			return deleteTask(_task);
		}

		if (!hasTaskToDelete()) {
			return new CommandAction(String.format(MSG_TASKIDNOTFOUND, _taskID), false, null);
		}

		return deleteTask(_task);

	}

	/**
	 * Undo the action of a previously deleted {@code Task}
	 * 
	 * @return a CommandAction
	 */
	@Override
	public CommandAction undo() {
		Command add = new CmdAdd(_task.getName());
		add.setTask(_task);
		return add.execute();
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_ID };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] {};
	}

	/**
	 * Returns a syntax message for delete command
	 * 
	 * @return a syntax message for delete command
	 */
	@Override
	public String getHelpInfo() {
		return HELP_INFO_DELETE;
	}

	/**
	 * Determine delete action is called by undo command
	 * 
	 * @return true if delete action is called by undo. false if delete action
	 *         is not called by undo.
	 */
	private boolean isUndo() {
		if (_task == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Check if a {@code Task} exist to be deleted.
	 * 
	 * @return true if {@code Task} is not null. false if {@code Task} is null.
	 */
	private boolean hasTaskToDelete() {
		String paramTaskID = getParameterValue(CmdParameters.PARAM_NAME_TASK_ID);
		_task = proccessTaskID(paramTaskID);
		if (_task == null || _task.equals("")) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Process the given task ID and returns a Task of the specified ID.
	 * 
	 * @param paramTaskID
	 *            a String parameter of task ID.
	 * 
	 * @return a {@code Task} of the specified ID.
	 */
	private Task proccessTaskID(String paramTaskID) {
		assert paramTaskID != null && !paramTaskID.equals("");
		try {
			_taskID = Integer.parseInt(paramTaskID);
		} catch (NumberFormatException e) {
			LogHandler.getLog().log(Level.WARNING, String.format(LOG_NUMBERFORMATEXCEPTION, paramTaskID));
			_taskID = INVALID_TASKID;
		}
		return _taskTree.getTask(_taskID);
	}

	/**
	 * Delete a specified {@code Task}
	 *
	 * @param task
	 *            {@code Task} to be deleted
	 *
	 * @return a CommandAction of deleting a {@code Task} successfully
	 * 
	 */
	private CommandAction deleteTask(Task task) {
		assert task != null;

		try {
			_taskTree.remove(task);
			LogHandler.getLog().log(Level.INFO, LOG_DELETE_SUCCESS);
		} catch (Exception e) {
			LogHandler.getLog().log(Level.WARNING, LOG_DELETE_FAIL, e);
		}

		return new CommandAction(String.format(MSG_TASKDELETED, _task.getName()), true,
				_taskTree.searchFlag(FLAG_TYPE.NULL));
	}

}
