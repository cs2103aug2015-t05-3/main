/**
 * Command to update the details (task_name, start_time, end_time, priority) of a task 
 */

package logic.command;

import util.TimeUtil;

import constants.CmdParameters;
import parser.ParserConstants;

import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;

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
	private static final String HELP_INFO_UPDATE = "<task_ID> [%1$s <task_name>] [%2$s <start_time>] [%3$s <end_time>][%4$s <high/normal/low/h/n/l>]";

	// Variable constants
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
	 * Executes the update command
	 * 
	 * @return a CommandAction object
	 */
	@Override
	public CommandAction execute() {

		if (!hasTaskToUpdate()) {
			return new CommandAction(String.format(MSG_TASKIDNOTFOUND, _taskID), false, null);
		}

		if (hasOptionalParam(proccessOptionalFields())) {
			if (isInvalidTime(_newStartTime, _newEndTime)) {
				return new CommandAction(MSG_INVALIDTIME, false, null);
			}
		} else {
			return new CommandAction(MSG_TASKNOUPDATE, false, null);
		}

		return updateTask(_task, _newTaskName, _newStartTime, _newEndTime, _newPriority);
	}

	@Override
	public CommandAction undo() {
		return updateTask(_task, _prevTaskName, _prevStartTime, _prevEndTime, _prevPriority);
	}

	@Override
	public boolean isUndoable() {
		return true;
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

	@Override
	public String getHelpInfo() {
		return String.format(HELP_INFO_UPDATE, ParserConstants.TASK_SPECIFIER_TASKNAME,
				ParserConstants.TASK_SPECIFIER_STARTTIME, ParserConstants.TASK_SPECIFIER_ENDTIME,
				ParserConstants.TASK_SPECIFIER_PRIORITY);
	}

	private Task proccessTaskID(String paramTaskID) {
		_taskID = Integer.parseInt(paramTaskID);
		return _taskTree.getTask(_taskID);
	}

	private boolean hasTaskToUpdate() {

		String paramTaskID = getParameterValue(CmdParameters.PARAM_NAME_TASK_ID);
		_task = proccessTaskID(paramTaskID);

		if (_task == null) {
			return false;
		} else {
			return true;
		}

	}

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
	 * 
	 *
	 */
	private int proccessOptionalFields() {

		int noOfOptionalParam = OPTIONAL_PARAM_EMPTY;

		noOfOptionalParam += proccessTaskName();
		noOfOptionalParam += proccessStartTime();
		noOfOptionalParam += proccessEndTime();
		noOfOptionalParam += proccessPriority();

		return noOfOptionalParam;

	}

	private int proccessTaskName() {

		String paramTaskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_SNAME);

		if (paramTaskName == null || paramTaskName.equals("")) {
			_newTaskName = _task.getName();
			return NOT_OPTIONAL_PARAM;
		} else {
			_newTaskName = paramTaskName;
			return IS_OPTIONAL_PARAM;
		}

	}

	private int proccessStartTime() {

		String paramStartTime = getParameterValue(CmdParameters.PARAM_NAME_TASK_STARTTIME);

		try {
			_newStartTime = Long.parseLong(paramStartTime);
			return IS_OPTIONAL_PARAM;
		} catch (Exception e) {
			_newStartTime = _task.getStartTime();
			return NOT_OPTIONAL_PARAM;
		}

	}

	private int proccessEndTime() {

		String paramEndTime = getParameterValue(CmdParameters.PARAM_NAME_TASK_ENDTIME);

		try {
			_newEndTime = Long.parseLong(paramEndTime);
			return IS_OPTIONAL_PARAM;
		} catch (Exception e) {
			_newEndTime = _task.getEndTime();
			return NOT_OPTIONAL_PARAM;
		}

	}

	private int proccessPriority() {

		String paramPriority = getParameterValue(CmdParameters.PARAM_NAME_TASK_PRIORITY);

		if (paramPriority == null || paramPriority.equals("")) {
			_newPriority = _task.getPriority();
			return NOT_OPTIONAL_PARAM;
		} else {
			_newPriority = getPriorityType(paramPriority);
			return IS_OPTIONAL_PARAM;
		}

	}

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

	private boolean isInvalidTime(long newStartTime, long newEndTime) {
		if (newStartTime == _task.getStartTime() && newEndTime == _task.getEndTime()) {
			return false;
		}

		if (newStartTime == NO_TIME && newEndTime == NO_TIME) {
			return false;
		}

		if (TimeUtil.compareMinTime(newEndTime, newStartTime) > VALID_TIME_COMPARATOR) {
			return false;
		}

		return true;
	}

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
