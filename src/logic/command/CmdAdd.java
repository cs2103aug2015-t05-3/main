/**
 * The add command
 *
 * @author Yan Chan Min Oo
 */
package logic.command;

import constants.CmdParameters;
import taskCollections.Task;
import taskCollections.Task.PRIORITY_TYPE;
import util.TimeUtil;

public class CmdAdd extends Command {

	/*
	 * Constants
	 */
	// Message constants
	// private static final String MSG_TASKUNSPECIFIED = "Please specify a task
	// name";
	private static final String MSG_TASKADDED = "Added : %1$s";
	private static final String MSG_TASKNAMENOTGIVEN = "Please enter a task name";

	// Error codes
	/*
	 * private enum ERROR { OK, TASKUNSPECIFIED }
	 */

	/*
	 * Variables for internal use
	 */
	private Task addTask;
	private String taskName;
	private String taskStartTime;
	private String taskEndTime;
	private String taskPriority;

	public CmdAdd() {

	}

	public CmdAdd(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public CommandAction execute() {

		addTask = getTask(); // Tries to get the task object (if available)

		if (addTask == null) { // Not an existing task. Create a task object
								// from scratch
			// Get details of the task to add
			if (taskName == null) {
				taskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_NAME);
			}
			if (taskName == null || taskName.equals("")) {
				return new CommandAction(MSG_TASKNAMENOTGIVEN, false, _taskTree.getList());
			}
			taskStartTime = getParameterValue(CmdParameters.PARAM_NAME_TASK_STARTTIME);
			taskEndTime = getParameterValue(CmdParameters.PARAM_NAME_TASK_ENDTIME);
			taskPriority = getParameterValue(CmdParameters.PARAM_NAME_TASK_PRIORITY);

			/*
			 * ERROR errCode = validateParam(); // Validate commands if (errCode
			 * != ERROR.OK) { switch (errCode) { case TASKUNSPECIFIED: return
			 * MSG_TASKUNSPECIFIED; } }
			 */

			assignDefaults(); // Assign defaults

			// Convert the start/end times to a format we can use for
			// calculation
			long taskStartTimeL = TimeUtil.sysStringToLongTime(taskStartTime);
			long taskEndTimeL = TimeUtil.sysStringToLongTime(taskEndTime);

			// Convert the priority to a format we can use for storing
			PRIORITY_TYPE taskPriorityP = getPriorityValue(taskPriority);

			addTask = new Task(taskName,null, taskStartTimeL, taskEndTimeL, Task.FLAG_TYPE.NULL, taskPriorityP);
		}

		_taskTree.add(addTask);

		return new CommandAction(String.format(MSG_TASKADDED, taskName), true, _taskTree.getList());
	}

	@Override
	public CommandAction undo() {
		Command delete = new CmdDelete();
		delete.setTask(addTask);
		return delete.execute();
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	/*
	 * Validates the parameters which are to be added to the task
	 */
	/*
	 * private ERROR validateParam() { if (taskName == null) { return
	 * ERROR.TASKUNSPECIFIED; } return ERROR.OK; }
	 */

	/**
	 * Assign default values to unspecified parameters
	 */
	private void assignDefaults() {
		if (taskStartTime == null) {
			taskStartTime = "" + Task.DATE_NULL;
		}
		if (taskEndTime == null) {
			taskEndTime = "" + Task.DATE_NULL;
		}
		if (taskPriority == null) {
			taskPriority = CmdParameters.PARAM_VALUE_TASK_PRIORITY_NORM;
		}
	}

	/*
	 * Converts the priority from string to a format suitable for storage
	 */
	private PRIORITY_TYPE getPriorityValue(String priority) {
		switch (priority) {
		case CmdParameters.PARAM_VALUE_TASK_PRIORITY_HIGH:
			return PRIORITY_TYPE.HIGH;
		case CmdParameters.PARAM_VALUE_TASK_PRIORITY_LOW:
			return PRIORITY_TYPE.LOW;
		default:
			return PRIORITY_TYPE.NORMAL;
		}
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_NAME };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME,
				CmdParameters.PARAM_NAME_TASK_PRIORITY };
	}
}
