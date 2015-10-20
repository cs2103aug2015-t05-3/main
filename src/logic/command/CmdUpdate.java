package logic.command;

import java.util.List;

import constants.CmdParameters;
import taskCollections.Task;
import ui.UIHelper;

public class CmdUpdate extends Command {

	/*
	 * Constants
	 */	
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNAMENOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKUPDATED = "Updated : \"%1$s\" to \"%2$s\"";
	private static final String MSG_ISNTANCEFOUND = "[%1%s] instances of \"%2$s\" found";
	private static final String MSG_TASKNOTUPDATED = "Empty String. Task not updated";
	private static final String MSG_TASKNOCHANGE = "No changes was made";
	
	/*
	 * Variables for internal use
	 */
	private Task _task;
	private String _taskName;
	private int _taskID;
	private boolean _isID;
	
	//variables for undo
	private String _prevTaskName;
	//To be included in later version
	private long prevTaskStartTime;
	private long prevTaskEndTime;

	public CmdUpdate() {

	}

	public CmdUpdate(String taskName) {
		_taskName = taskName;
	}
	
	@Override
	public CommandAction execute() {
		
		//Try undo 1st
		_task = getTask();
		if(isUndo()){
			String outputMsg = updateTask(_task, _prevTaskName);
			boolean isUndoable = true;
			return new CommandAction(outputMsg, isUndoable, _taskTree.getList());
		}
		
		String parameter = getParameterValue(CmdParameters.PARAM_NAME_CMD_SEARCH);	
		if (parameter == null || parameter.equals("")) {
			//return MSG_TASKUNSPECIFIED;
			return new CommandAction(MSG_TASKUNSPECIFIED, false, _taskTree.getList());
		}
		
		CmdSearch search = new CmdSearch();
		_isID = search.isInteger(parameter);
		List<Task> taskList = search.searchTask(_isID, _taskID, _taskName);
		
		return processList(taskList);
	}

	@Override
	public CommandAction undo() {
		
		Command update = new CmdUpdate();
		update.setTask(_task);
		update.setParameter(_prevTaskName, null);
		
		return update.execute();
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_CMD_SEARCH };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME};
	}
	
	private boolean isUndo(){
		if(_task == null){
			return false;
		}else{
			return true;
		}
	}
	
	private String updateTask(Task task, String newTaskName){
		_prevTaskName = task.getName();
		_taskTree.updateName(task, newTaskName);
		return String.format(MSG_TASKUPDATED, _prevTaskName, task.getName());
	}
	
	private CommandAction processList(List<Task> taskList){
		
		//Case 1: List is empty (nothing found)
		if(taskList.isEmpty()){
			String outputMsg = "";
			if(_isID){
				outputMsg = String.format(MSG_TASKIDNOTFOUND, _taskID);
			}else{
				outputMsg = String.format(MSG_TASKNAMENOTFOUND, _taskName);
			}
			boolean isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, _taskTree.getList());
		}
		
		//Case 2: List.size > 1 (more than 1 instance found)
		if(taskList.size() > 1){
			String outputMsg = String.format(MSG_ISNTANCEFOUND, _taskName);
			boolean isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, taskList);
		}
		
		//Case 3: List.size == 1 (task to be updated found)
		_task = taskList.get(0);
		
		String newTaskName = getUserInput(_task.getName());
		
		//Empty taskName
		if(newTaskName == null || newTaskName.equals("")){
			String outputMsg = MSG_TASKNOTUPDATED;
			boolean isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, _taskTree.getList());
		}
		
		//Same taskName
		if(newTaskName.equals(_task.getName())){
			String outputMsg = MSG_TASKNOCHANGE;
			boolean isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, _taskTree.getList());
		}
		
		String outputMsg = updateTask(_task, newTaskName);
		boolean isUndoable = true;
		return new CommandAction(outputMsg, isUndoable, _taskTree.getList());
		
	}
	
	private String getUserInput(String currTaskName){
		String newTaskName = "";
		
		UIHelper.setUserInput(currTaskName);
		newTaskName = UIHelper.getUserInput().trim();
		
		return newTaskName;
	}

}
