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
	
	//Log Message
	private static final String LOG_NUMBERFORMATEXCEPTION = "Warning: Task ID parameter is not an integer";

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
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME };
	}

	@Override
	public String getHelpInfo() {
		return HELP_INFO_DELETE;
	}

	private boolean isUndo() {
		if (_task == null) {
			return false;
		} else {
			return true;
		}
	}

	private boolean hasTaskToDelete() {
		String paramTaskID = getParameterValue(CmdParameters.PARAM_NAME_TASK_ID);
		_task = proccessTaskID(paramTaskID);
		if (_task == null || _task.equals("")) {
			return false;
		} else {
			return true;
		}

	}

	private Task proccessTaskID(String paramTaskID) {
		assert paramTaskID != null && !paramTaskID.equals("");
		try{
			_taskID = Integer.parseInt(paramTaskID);
		}catch(NumberFormatException e){
			LogHandler.getLog().log(Level.WARNING, LOG_NUMBERFORMATEXCEPTION, e);
			_taskID = INVALID_TASKID;
		}
		return _taskTree.getTask(_taskID);
	}

	private CommandAction deleteTask(Task task) {
		assert task != null;
		
		_taskTree.remove(task);
		return new CommandAction(String.format(MSG_TASKDELETED, _task.getName()), true,
				_taskTree.searchFlag(FLAG_TYPE.NULL));
	}

}
