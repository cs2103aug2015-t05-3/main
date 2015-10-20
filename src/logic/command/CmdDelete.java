package logic.command;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import constants.CmdParameters;
import taskCollections.Task;

public class CmdDelete extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNAMENOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_ISNTANCEFOUND = "[%1%s] instances of \"%2$s\" found";
	private static final String MSG_TASKDELETED = "Deleted : \"%1$s\"";

	/*
	 * Variables for internal use
	 */
	private Task _task;
	private String _taskName;
	private int _taskID;
	private boolean _isID;

	private static Logger log = Logger.getLogger("log_CmdDelete");
	
	public CmdDelete() {

	}

	public CmdDelete(String taskName) {
		_taskName = taskName;
	}

	@Override
	public CommandAction execute() {

		//Try undo first
		_task = getTask();
		if(isUndo()){
			String outputMsg = deleteTask(_task);
			boolean isUndoable = true;
			return new CommandAction(outputMsg, isUndoable, _taskTree.getList());
		}
		
		String parameter = getParameterValue(CmdParameters.PARAM_NAME_CMD_SEARCH);
		if (parameter == null || parameter.equals("")) {
			boolean isUndoable = false;
			return new CommandAction(MSG_TASKUNSPECIFIED, isUndoable, _taskTree.getList());
		}
		
		CmdSearch search = new CmdSearch();
		_isID = search.isInteger(parameter);
		if(_isID){
			_taskID = Integer.parseInt(parameter);
		} else {
			_taskName = parameter;
		}
		List<Task> taskList = search.searchTask(_isID, _taskID, _taskName);
		
		return processList(taskList);
		
	}

	@Override
	public CommandAction undo() {
		Command add = new CmdAdd();
		add.setTask(_task);
		return add.execute();
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
	
	private String deleteTask(Task task){
		_taskTree.remove(task);
		return String.format(MSG_TASKDELETED, _task.getName());
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
		
		//Case 3: List.size == 1 (task to be deleted found)
		_task = taskList.get(0);
		String outputMsg = deleteTask(_task);
		boolean isUndoable = true;
		return new CommandAction(outputMsg, isUndoable, _taskTree.getList());
		
	}	
	
}
