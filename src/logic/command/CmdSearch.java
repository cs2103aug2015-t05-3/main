package logic.command;

import java.util.List;

import constants.CmdParameters;

import taskCollections.Task;

public class CmdSearch extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_KEYWORD_UNSPECIFIED = "Please specify a keyword to search";
	private static final String MSG_KEYWORD_NOTFOUND = "Specified keyword \"%1$s\" not found";
	private static final String MSG_TASKFOUND = "Task \"%1$s\" found";
	private static final String MSG_ISNTANCEFOUND = "[%1$s] instances of \"%2$s\" found";

	// Help Info
	private static final String HELP_INFO_SEARCH = "<keyword>";

	/*
	 * Variables for internal use
	 */
	public String _keyword;

	public CmdSearch() {

	}

	@Override
	public CommandAction execute() {
		if (!hasKeywordToSearch()) {
			return new CommandAction(MSG_KEYWORD_UNSPECIFIED, false, null);
		}

		return searchKeyword();
	}

	@Override
	public CommandAction undo() {
		// do nothing (Search should not have undo)
		return null;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_CMD_SEARCH };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME };
	}

	@Override
	public String getHelpInfo() {
		return HELP_INFO_SEARCH;
	}

	private boolean hasKeywordToSearch() {
		_keyword = getParameterValue(CmdParameters.PARAM_NAME_CMD_SEARCH);
		if (_keyword == null || _keyword.equals("")) {
			return false;
		} else {
			return true;
		}

	}

	private CommandAction searchKeyword() {
		List<Task> taskList = getTaskList(_keyword);
		String outputMsg = getOutputMsg(taskList);
		return new CommandAction(outputMsg, false, taskList);
	}

	private List<Task> getTaskList(String keyword) {
		return _taskTree.searchName(keyword);
	}

	private String getOutputMsg(List<Task> taskList) {

		// Case 1 : List isEmpty
		if (taskList.isEmpty()) {
			return String.format(MSG_KEYWORD_NOTFOUND, _keyword);
		}

		// Case 2 : List.size > 1 (Since ID is unique)
		if (taskList.size() > 1) {
			return String.format(MSG_ISNTANCEFOUND, taskList.size(), _keyword);
		}

		// Case 3: List.size == 1
		return String.format(MSG_TASKFOUND, taskList.get(0).getName());

	}

}
