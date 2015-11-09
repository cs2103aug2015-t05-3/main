//@@author A0125574A

/**
 * Command to display a list of {@code Task} 
 */

package logic.command;

import java.util.List;

import constants.CmdParameters;
import parser.ParserConstants;

import taskCollections.Task;
import taskCollections.Task.PRIORITY_TYPE;
import taskCollections.Task.FLAG_TYPE;

public class CmdList extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_EMPTY_TASKTREE = "No tasks to display";
	private static final String MSG_TOTAL_TASK = "Total tasks in list: [%1$s]";

	// Help Info
	private static final String HELP_INFO_LIST = "[%1$s or %2$s or %3$s <high/normal/low/h/n/l>]";

	// Variable constants
	private static final int EMPTY_LIST = 0;

	public CmdList() {

	}

	/**
	 * Display a list of {@code Task}
	 * 
	 * @return a CommandAction
	 */
	@Override
	public CommandAction execute() {

		if (isEmptyTaskList()) {
			return new CommandAction(MSG_EMPTY_TASKTREE, false, _taskTree.getList());
		}

		String optionalParameter = getParameterValue(CmdParameters.PARAM_NAME_LIST_FLAG);
		List<Task> taskList = proccessParameter(optionalParameter);
		return new CommandAction(String.format(MSG_TOTAL_TASK, taskList.size()), false, taskList);

	}

	@Override
	public CommandAction undo() {
		// do nothing (List should not have undo)
		return null;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] {};
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_LIST_FLAG, CmdParameters.PARAM_NAME_TASK_PRIORITY };
	}

	/**
	 * Returns a syntax message for list command
	 * 
	 * @return a syntax message for list command
	 */
	@Override
	public String getHelpInfo() {
		return String.format(HELP_INFO_LIST, ParserConstants.TASK_FILTER_ALL, ParserConstants.TASK_FILTER_DONE,
				ParserConstants.TASK_SPECIFIER_PRIORITY);
	}

	/**
	 * Checks if list is empty
	 * 
	 * @return true if list is empty. false if list is not empty
	 */
	private boolean isEmptyTaskList() {
		assert _taskTree != null;

		if (_taskTree.size() == EMPTY_LIST) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a list of undone {@code Task}
	 * 
	 * @return a list of undone {@code Task}
	 */
	private List<Task> getUndoneTask() {
		assert _taskTree != null;
		return _taskTree.searchFlag(FLAG_TYPE.NULL);
	}

	/**
	 * Returns a list of completed {@code Task}
	 * 
	 * @return a list of completed {@code Task}
	 */
	private List<Task> getDoneTask() {
		assert _taskTree != null;
		return _taskTree.searchFlag(FLAG_TYPE.DONE);
	}

	/**
	 * Returns a list of {@code Task} with specified priority
	 * 
	 * @param priority
	 *            priority type to be listed
	 * 
	 * @return a list of {@code Task} with specified priority
	 */
	private List<Task> getPriorityTask(String priority) {

		if (priority == null) {
			priority = "";
		}
		PRIORITY_TYPE priorityType;
		switch (priority) {
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

		assert _taskTree != null;
		return _taskTree.searchPriority(priorityType);

	}

	/**
	 * Returns a list of all existing {@code Task}
	 * 
	 * @return a list of all existing {@code Task}
	 */
	private List<Task> getAllTask() {
		assert _taskTree != null;
		return _taskTree.getList();
	}

	/**
	 * Process given parameter and return a list of {@code Task} based on the
	 * given parameter
	 * 
	 * @param parameter
	 *            parameter to be processed
	 * 
	 * @return a list of {@code Task} based on given parameter
	 */
	private List<Task> proccessParameter(String parameter) {

		if (parameter == null) {
			parameter = "";
		}

		List<Task> taskList;

		switch (parameter) {
			case CmdParameters.PARAM_VALUE_LIST_ALL:
				taskList = getAllTask();
				break;
			case CmdParameters.PARAM_VALUE_LIST_DONE:
				taskList = getDoneTask();
				break;
			case CmdParameters.PARAM_VALUE_LIST_PRIORITY:
				String paramPriority = getParameterValue(CmdParameters.PARAM_NAME_TASK_PRIORITY);
				taskList = getPriorityTask(paramPriority);
				break;
			default:
				taskList = getUndoneTask();
				break;
		}

		return taskList;
	}

}
