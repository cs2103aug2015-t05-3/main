package command;

import java.util.List;

import constants.CmdParameters;
import tds.Task;
import tds.TaskTree;

public class CmdSearch extends Command {

	/*
	 * Constants
	 */	
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKDELETED = "Deleted : %1$s";
	private static final String MSG_NOTASKDELETED = "No task deleted";


	// Error codes
	/*
	 * private enum ERROR { OK, TASKUNSPECIFIED }
	 */

	/*
	 * Variables for internal use
	 */
	private String taskName;
	
	public CmdSearch() {

	}

	public CmdSearch(String taskName) {
		this.taskName = taskName;
	}

	
	@Override
	public String execute() {
		
		taskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_NAME);
		System.out.println(taskName);
		List<Task> searchTaskList = searchTask(taskName);
		
		return displaySearchList(searchTaskList);
	}

	@Override
	public String undo() {
		//do nothing (Search should not have undo)
		return null;
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_NAME };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME};
	}
	
	private List<Task> searchTask(String taskName){
		return TaskTree.searchName(taskName);
	}
	
	public String displaySearchList(List<Task> searchTaskList){
		
		String output = "";
		
		output = output + searchTaskList.size() +
				" instances of \"" + taskName + "\" found:" + System.lineSeparator() ;
		for(int i=0; i<searchTaskList.size(); i++){
			output = output + (i+1) + ". " + searchTaskList.get(i).getName() + System.lineSeparator();
		}
		output = output + System.lineSeparator();
		
		return output;
		
	}

}
