//@@author A0125574A

/**
 * Command to update the details (task_name, start_time, end_time, priority) of a {@code Task} 
 */

package logic.command;

import util.TimeUtil;

import java.util.logging.Level;

import constants.CmdParameters;
import parser.ParserConstants;

import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;

import logger.LogHandler;

public class CmdUpdate extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKNOUPDATE = "No update was made";
	private static final String MSG_TASKUPDATED = "Updated ID: \"%1$s\"";
	private static final String MSG_INVALIDTIME = "Invalid start/end time given";

	// Help Info
	private static final String HELP_INFO_UPDATE = "<task_ID> [%1$s <task_name>] [%2$s <start_time>] "
			+ "[%3$s <end_time>][%4$s <high/normal/low/h/n/l>]";

	// Log Message
	private static final String LOG_NUMBERFORMATEXCEPTION = "Warning: Task ID parameter is not an integer";

	// Variable constants
	private static final int INVALID_TASKID = -1;
	private static final int OPTIONAL_PARAM_EMPTY = 0;
	private static final int IS_OPTIONAL_PARAM = 1;
	private static final int NOT_OPTIONAL_PARAM = 0;
	private static final int NO_TIME = 0;
	private static final int VALID_TIME_COMPARATOR = 1;

	/*
	 * Variables for internal use
	 */
	private Task _task;
	private int _taskID;

	// variables for updating
	private String _newTaskName;
	private long _newStartTime;
	private long _newEndTime;
	private PRIORITY_TYPE _newPriority;

	// variables for undo
	private String _prevTaskName;
	private long _prevStartTime;
	private long _prevEndTime;
	private PRIORITY_TYPE _prevPriority;

	public CmdUpdate() {

	}

	/**
	 * Updates new details to a specified {@code Task}
	 * 
	 * @return a CommandAction
	 */
	@Override
	public CommandAction execute() {

		// Check if there is a task to be updated
		if (!hasTaskToUpdate()) {
			return new CommandAction(String.format(MSG_TASKIDNOTFOUND, _taskID), false, null);
		}

		// Initialize optional parameter values
		if (hasOptionalParam(proccessOptionalFields())) {
			if (isInvalidTime(_newStartTime, _newEndTime)) {
				return new CommandAction(MSG_INVALIDTIME, false, null);
			}
		} else {
			return new CommandAction(MSG_TASKNOUPDATE, false, null);
		}

		return updateTask(_task, _newTaskName, _newStartTime, _newEndTime, _newPriority);
	}

	/**
	 * Undo the changes to a previously updated {@code Task}
	 * 
	 * @return a CommandAction
	 */
	@Override
	public CommandAction undo() {
		return updateTask(_task, _prevTaskName, _prevStartTime, _prevEndTime, _prevPriority);
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_ID };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_SNAME, CmdParameters.PARAM_NAME_TASK_STARTTIME,
				CmdParameters.PARAM_NAME_TASK_ENDTIME, CmdParameters.PARAM_NAME_TASK_PRIORITY };
	}

	/**
	 * Returns a syntax message for update command
	 * 
	 * @return a syntax message for update command
	 */
	@Override
	public String getHelpInfo() {
		return String.format(HELP_INFO_UPDATE, ParserConstants.TASK_SPECIFIER_TASKNAME,
				ParserConstants.TASK_SPECIFIER_STARTTIME, ParserConstants.TASK_SPECIFIER_ENDTIME,
				ParserConstants.TASK_SPECIFIER_PRIORITY);
	}

	/**
	 * Process the given task ID and returns a Task of the specified ID.
	 * 
	 * @param paramTaskID
	 *            a String parameter of task ID.
	 * 
	 * @return a {@code Task} of the specified ID.
	 */
	private Task processTaskID(String paramTaskID) {
		assert paramTaskID != null && !paramTaskID.equals("");

		try {
			_taskID = Integer.parseInt(paramTaskID);
		} catch (NumberFormatException e) {
			LogHandler.getLog().log(Level.WARNING, LOG_NUMBERFORMATEXCEPTION, e);
			_taskID = INVALID_TASKID;
		}

		return _taskTree.getTask(_taskID);
	}

	/**
	 * Check if a {@code Task} exist to be updated.
	 * 
	 * @return true if {@code Task} is not null. false if {@code Task} is null.
	 */
	private boolean hasTaskToUpdate() {

		String paramTaskID = getParameterValue(CmdParameters.PARAM_NAME_TASK_ID);
		_task = processTaskID(paramTaskID);

		if (_task == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Check if there is/are optional parameter(s) given.
	 * 
	 * @return true if there is at least one optional parameter. false if there
	 *         is no optional parameter.
	 */
	private boolean hasOptionalParam(int noOfOptionalParam) {
		if (noOfOptionalParam == OPTIONAL_PARAM_EMPTY) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Initializes {@code _newTaskName}, {@code _newStartTime},
	 * {@code _newEndTime}, {@code _newPriority}, with optional parameters
	 * values.
	 *
	 * @return number of optional parameter(s) processed.
	 */
	private int proccessOptionalFields() {

		int noOfOptionalParam = OPTIONAL_PARAM_EMPTY;

		// Initializes each variable and calculate total number of optional
		// parameter(s)
		noOfOptionalParam += processTaskName();
		noOfOptionalParam += processStartTime();
		noOfOptionalParam += processEndTime();
		noOfOptionalParam += processPriority();

		return noOfOptionalParam;

	}

	/**
	 * Initializes {@code _newTaskName} with task name optional parameters
	 * value.
	 *
	 * @return {@code IS_OPTIONAL_PARAM} if task name optional parameter is
	 *         processed. else return {@code NOT_OPTIONAL_PARAM}
	 */
	private int processTaskName() {

		String paramTaskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_SNAME);

		if (paramTaskName == null || paramTaskName.equals("")) {
			_newTaskName = _task.getName();
			return NOT_OPTIONAL_PARAM;
		} else {
			_newTaskName = paramTaskName;
			return IS_OPTIONAL_PARAM;
		}

	}

	/**
	 * Initializes {@code _newStartTime} with start time optional parameters
	 * value.
	 *
	 * @return {@code IS_OPTIONAL_PARAM} if start time optional parameter is
	 *         processed. else return {@code NOT_OPTIONAL_PARAM}
	 */
	private int processStartTime() {

		String paramStartTime = getParameterValue(CmdParameters.PARAM_NAME_TASK_STARTTIME);

		try {
			_newStartTime = Long.parseLong(paramStartTime);
			return IS_OPTIONAL_PARAM;
		} catch (Exception e) {
			_newStartTime = _task.getStartTime();
			return NOT_OPTIONAL_PARAM;
		}

	}

	/**
	 * Initializes {@code _newEndTime} with end time optional parameters value.
	 *
	 * @return {@code IS_OPTIONAL_PARAM} if end time optional parameter is
	 *         processed. else return {@code NOT_OPTIONAL_PARAM}
	 */
	private int processEndTime() {

		String paramEndTime = getParameterValue(CmdParameters.PARAM_NAME_TASK_ENDTIME);

		try {
			_newEndTime = Long.parseLong(paramEndTime);
			return IS_OPTIONAL_PARAM;
		} catch (Exception e) {
			_newEndTime = _task.getEndTime();
			return NOT_OPTIONAL_PARAM;
		}

	}

	/**
	 * Initializes {@code _newPriority} with priority optional parameters value.
	 *
	 * @return {@code IS_OPTIONAL_PARAM} if priority optional parameter is
	 *         processed. else return {@code NOT_OPTIONAL_PARAM}
	 */
	private int processPriority() {

		String paramPriority = getParameterValue(CmdParameters.PARAM_NAME_TASK_PRIORITY);

		if (paramPriority == null || paramPriority.equals("")) {
			_newPriority = _task.getPriority();
			return NOT_OPTIONAL_PARAM;
		} else {
			_newPriority = getPriorityType(paramPriority);
			return IS_OPTIONAL_PARAM;
		}

	}

	/**
	 * Returns a {@code PRIORITY_TYPE} according to priority parameter
	 *
	 * @param priorityParam
	 *            a String parameter of priority
	 *
	 * @return {@code IS_OPTIONAL_PARAM} if priority optional parameter is
	 *         processed. else return {@code NOT_OPTIONAL_PARAM}
	 */
	private PRIORITY_TYPE getPriorityType(String priorityParam) {

		PRIORITY_TYPE priorityType;

		switch (priorityParam) {
			case CmdParameters.PARAM_VALUE_TASK_PRIORITY_HIGH:
				priorityType = PRIORITY_TYPE.HIGH;
				break;
			case CmdParameters.PARAM_VALUE_TASK_PRIORITY_NORM:
				priorityType = PRIORITY_TYPE.NORMAL;
				break;
			case CmdParameters.PARAM_VALUE_TASK_PRIORITY_LOW:
				priorityType = PRIORITY_TYPE.LOW;
				break;
			default:
				priorityType = PRIORITY_TYPE.NORMAL;
				break;
		}

		return priorityType;

	}

	/**
	 * Checks if either start time or end time is invalid
	 *
	 * @param newStartTime
	 *            start time to be updated to Task
	 * 
	 * @param newEndTime
	 *            end time to be updated to Task
	 *
	 * @return true if either start time or end time is invalid. false if both
	 *         start time and end time are valid
	 */
	private boolean isInvalidTime(long newStartTime, long newEndTime) {

		assert newStartTime >= 0 && newEndTime >= 0;

		// Check if start time and end time is unchanged
		if (newStartTime == _task.getStartTime() && newEndTime == _task.getEndTime()) {
			return false;
		}

		// Check if start time and end time is to be removed
		if (newStartTime == NO_TIME && newEndTime == NO_TIME) {
			return false;
		}

		// Check if start time is earlier than end time
		if (TimeUtil.compareMinTime(newEndTime, newStartTime) >= VALID_TIME_COMPARATOR) {
			return false;
		}

		return true;
	}

	/**
	 * Update new details to a specified {@code Task}
	 *
	 * @param task
	 *            {@code Task} to be updated
	 * 
	 * @param newTaskName
	 *            new task name to be updated
	 * 
	 * @param newStartTime
	 *            new start time to be updated
	 * 
	 * @param newEndTime
	 *            new end time to be updated
	 * 
	 * @param newPriority
	 *            new priority to be updated
	 *
	 * @return a CommandAction of updating a {@code Task} successfully
	 * 
	 */
	private CommandAction updateTask(Task task, String newTaskName, long newStartTime, long newEndTime,
			PRIORITY_TYPE newPriority) {
		// Set previous task details
		_prevTaskName = task.getName();
		_prevStartTime = task.getStartTime();
		_prevEndTime = task.getEndTime();
		_prevPriority = task.getPriority();

		// Update task with new details
		_taskTree.updateName(task, newTaskName);
		_taskTree.updateStartTime(task, newStartTime);
		_taskTree.updateEndTime(task, newEndTime);
		_taskTree.updatePriority(task, newPriority);

		return new CommandAction(String.format(MSG_TASKUPDATED, _taskID), true, _taskTree.searchFlag(FLAG_TYPE.NULL));
	}

}
