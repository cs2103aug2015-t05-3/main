package logic.command;

import java.util.ArrayList;
import java.util.List;

import constants.CmdParameters;
import taskCollections.Task;
import taskCollections.TaskTree;

public class CmdSearch extends Command {

	/*
	 * Constants
	 */	
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_OUTPUT = "[%1%s] instances of \"%2$s\" found";

	private String _taskName;
	private int _taskID;
	
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
		
		String parameter = getParameterValue(CmdParameters.PARAM_NAME_TASK_NAME);
		
		List<Task> taskList = searchTask(parameter);
		String outputMsg = getOutputMsg(taskList);
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
		return new String[] { CmdParameters.PARAM_NAME_TASK_NAME };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME};
	}
	
	private List<Task> searchTask(String parameter){
		if(isInteger(parameter)){
			return searchByTaskID(_taskID);
		}else{
			return searchByTaskName(_taskName);
		}
	}
	
	private List<Task> searchByTaskName(String taskName){
		return TaskTree.searchName(taskName);
	}
	
	private List<Task> searchByTaskID(int taskID){
		List<Task> taskList = new ArrayList<Task>();
		taskList.add(TaskTree.getList().get(taskID));
		return taskList;
	}
	
	private boolean isInteger(String parameter){
		try{
			_taskID = Integer.parseInt(parameter);
		}catch(NumberFormatException e){
			_taskName = parameter;
			return false;
		}
		return true;
	}
	
	private String getOutputMsg(List<Task> taskList){
		if(taskList.isEmpty()){
			return String.format(MSG_TASKNOTFOUND, _taskName);
		}else{
			return String.format(MSG_OUTPUT, taskList.size() ,_taskName);
		} 
	}
	
}
