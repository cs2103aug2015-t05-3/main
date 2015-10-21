package logic.command;

import java.util.ArrayList;
import java.util.List;

import constants.CmdParameters;
import taskCollections.Task;

public class CmdSearch extends Command {

	/*
	 * Constants
	 */	
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name or task ID";
	private static final String MSG_TASKNAMENOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKFOUND = "Task \"%1$s\" found";
	private static final String MSG_ISNTANCEFOUND = "[%1$s] instances of \"%2$s\" found";

	
	/*
	 * Variables for internal use
	 */
	private String _taskName;
	private int _taskID;
	private boolean _isID;
	
	public CmdSearch() {

	}

	public CmdSearch(String taskName) {
		_taskName = taskName;
	}

	public CmdSearch(int taskID) {
		_taskID = taskID;
	}
	
	@Override
	public CommandAction execute() {
		
		String parameter = getParameterValue(CmdParameters.PARAM_NAME_CMD_SEARCH);
		if (parameter == null || parameter.equals("")) {
			boolean isUndoable = false;
			return new CommandAction(MSG_TASKUNSPECIFIED, isUndoable, _taskTree.getList());
		}
		
		_isID = isInteger(parameter);
		if(_isID){
			_taskID = Integer.parseInt(parameter);
		} else {
			_taskName = parameter;
		}
		List<Task> taskList = searchTask(_isID, _taskID, _taskName);
		String outputMsg = getOutputMsg(taskList, _isID);
		boolean isUndoable = false;
		
		return new CommandAction(outputMsg, isUndoable, taskList);
	}

	@Override
	public CommandAction undo() {
		//do nothing (Search should not have undo)
		return null;
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_CMD_SEARCH };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME};
	}
	
	public List<Task> searchTask(boolean isID, int taskID, String taskName){
		if(isID){
			return searchByTaskID(taskID);
		}else{
			return searchByTaskName(taskName);
		}
	}
	
	public List<Task> searchByTaskName(String taskName){
		return _taskTree.searchName(taskName);
	}
	
	public List<Task> searchByTaskID(int taskID){
		List<Task> taskList = new ArrayList<Task>();
		Task searched = _taskTree.getTask(taskID);
		if(searched != null){
			taskList.add(searched);
		}
		
		return taskList;
	}
	
	public boolean isInteger(String parameter){
		try{
			_taskID = Integer.parseInt(parameter);
		}catch(NumberFormatException e){
			_taskName = parameter;
			return false;
		}
		return true;
	}
	
	public String getOutputMsg(List<Task> taskList, boolean isID){
		
		//Case 1 : List isEmpty
		if(taskList.isEmpty()){
			if(isID){
				return String.format(MSG_TASKIDNOTFOUND, _taskID);
			}else{
				return String.format(MSG_TASKNAMENOTFOUND, _taskName);
			}
		}

		//Case 2 : List.size > 1 (Since ID is unique)
		if(taskList.size() > 1){
			return String.format(MSG_ISNTANCEFOUND, taskList.size() ,_taskName);		
		} 
		
		//Case 3: List.size == 1
		return String.format(MSG_TASKFOUND, taskList.get(0).getName());
	
	}
	
}
