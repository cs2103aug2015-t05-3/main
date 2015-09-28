package command;

import constants.CmdParameters;
import tds.Task;
import tds.TaskTree;

public class CmdDelete extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKDELETED = "Deleted : %1$s";

	// Error codes
	/*
	 * private enum ERROR { OK, TASKUNSPECIFIED }
	 */

	/*
	 * Variables for internal use
	 */
	private Task deleteTask;
	private String taskName;

	public CmdDelete() {

	}

	public CmdDelete(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public String execute() {

		deleteTask = getTask(); // Tries to get the task object (if available)

		if (deleteTask == null) {
			return MSG_TASKUNSPECIFIED;
		}

		boolean isRemoved = TaskTree.remove(deleteTask);
		if (isRemoved) {
			return String.format(MSG_TASKNOTFOUND, taskName);
		}

		return String.format(MSG_TASKDELETED, taskName);
	}

	@Override
	public String undo() {
		Command add = new CmdAdd();
		add.setTask(deleteTask);
		return add.execute();
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_NAME };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME};
	}

}
