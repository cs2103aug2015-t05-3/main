package logic.command;

import java.util.logging.Level;

import constants.CmdParameters;
import logger.LogHandler;
import parser.ParserConstants;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;

public class CmdMark extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKMARKED = "Marked: \"%1$s\"";
	private static final String MSG_TASKUNMARKED = "Unmarked: \"%1$s\"";
	private static final String MSG_TASKALREADYMARKED = "Task: \"%1$s\" already marked";
	private static final String MSG_TASKALREADYUNMARKED = "Task: \"%1$s\" already unmarked";

	// Help Info
	private static final String HELP_INFO_MARK = "<task_ID> [%1$s]";
	
	// Log Message
	private static final String LOG_NUMBERFORMATEXCEPTIOM = "Warning: Task ID parameter is not an integer";

	// Variable Constant
	private static final int INVALID_TASKID = -1;
	
	/*
	 * Variables for internal use
	 */
	private Task _task;
	private int _taskID;
	private boolean _isUnmarkParam;

	public CmdMark() {

	}

	@Override
	public CommandAction execute() {

		if (!hasTaskToMark()) {
			return new CommandAction(String.format(MSG_TASKIDNOTFOUND, _taskID), false, null);
		}

		proccessOptionalParam();
		if (_isUnmarkParam) {
			return unmarkTask(_task);
		} else {
			return markTask(_task);
		}

	}

	@Override
	public CommandAction undo() {

		// reverse order in undo
		if (_isUnmarkParam) {
			return markTask(_task);
		} else {
			return unmarkTask(_task);
		}

	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_ID };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_MARK_FLAG };
	}

	@Override
	public String getHelpInfo() {
		return String.format(HELP_INFO_MARK, ParserConstants.TASK_MARK_UNMARK);
	}

	private boolean hasTaskToMark() {
		String paramTaskID = getParameterValue(CmdParameters.PARAM_NAME_TASK_ID);
		_task = proccessTaskID(paramTaskID);
		if (_task == null || _task.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	private Task proccessTaskID(String paramTaskID) {
		assert paramTaskID != null && paramTaskID.equals("");

		try {
			_taskID = Integer.parseInt(paramTaskID);
		} catch (NumberFormatException e) {
			LogHandler.getLog().log(Level.WARNING, LOG_NUMBERFORMATEXCEPTIOM, e);
			_taskID = INVALID_TASKID;
		}

		return _taskTree.getTask(_taskID);
	}

	private void proccessOptionalParam() {
		String optionalParam = getParameterValue(CmdParameters.PARAM_NAME_MARK_FLAG);
		if (optionalParam != CmdParameters.PARAM_VALUE_MARK_UNMARK) {
			_isUnmarkParam = false;
		} else {
			_isUnmarkParam = true;
		}
	}

	private boolean isMarked(Task task) {
		assert task != null;
		
		if (task.getFlag() == FLAG_TYPE.NULL) {
			return false;
		} else {
			return true;
		}
	}

	private CommandAction markTask(Task task) {
		assert task != null;

		if (isMarked(task)) {
			return new CommandAction(String.format(MSG_TASKALREADYMARKED, _task.getName()), false,
					_taskTree.searchFlag(FLAG_TYPE.NULL));
		} else {
			_taskTree.updateFlag(task, FLAG_TYPE.DONE);
			return new CommandAction(String.format(MSG_TASKMARKED, task.getName()), true,
					_taskTree.searchFlag(FLAG_TYPE.NULL));
		}
	}

	private CommandAction unmarkTask(Task task) {
		assert task != null;

		if (isMarked(task)) {
			_taskTree.updateFlag(task, FLAG_TYPE.NULL);
			return new CommandAction(String.format(MSG_TASKUNMARKED, task.getName()), true,
					_taskTree.searchFlag(FLAG_TYPE.NULL));
		} else {

			return new CommandAction(String.format(MSG_TASKALREADYUNMARKED, _task.getName()), false,
					_taskTree.searchFlag(FLAG_TYPE.NULL));
		}
	}

}
