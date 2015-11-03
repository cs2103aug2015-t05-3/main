package logic.command;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import constants.CmdParameters;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;

public class CmdDelete extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKDELETED = "Deleted : \"%1$s\"";

	//Help Info
	private static final String HELP_INFO_DELETE = "<task_ID>";
	
	/*
	 * Variables for internal use
	 */
	private Task _task;

	private static Logger log = Logger.getLogger("log_CmdDelete");
	
	public CmdDelete() {

	}

	@Override
	public CommandAction execute() {

		//Try undo first
		_task = getTask();
		if(isUndo()){
			String outputMsg = deleteTask(_task);
			boolean isUndoable = true;
			return new CommandAction(outputMsg, isUndoable, _taskTree.searchFlag(FLAG_TYPE.NULL));
		}
		
		String paramTaskID = getParameterValue(CmdParameters.PARAM_NAME_TASK_ID);
		_task = proccessTaskID(paramTaskID);
		if(_task == null){
			String outputMsg = String.format(MSG_TASKIDNOTFOUND, paramTaskID);
			boolean isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, null);
		}
		
		String outputMsg = deleteTask(_task);
		boolean isUndoable = true;
		return new CommandAction(outputMsg, isUndoable, _taskTree.searchFlag(FLAG_TYPE.NULL));
		
	}

	@Override
	public CommandAction undo() {
		Command add = new CmdAdd(_task.getName());
		add.setTask(_task);
		return add.execute();
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_ID };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME};
	}
	
	@Override
	public String getHelpInfo(){
		return HELP_INFO_DELETE;
	}
	
	private boolean isUndo(){
		if(_task == null){
			return false;
		}else{
			return true;
		}
	}
	
	private Task proccessTaskID(String paramTaskID){
		int taskID = Integer.parseInt(paramTaskID);
		return _taskTree.getTask(taskID);
	}
	
	private String deleteTask(Task task){
		_taskTree.remove(task);
		return String.format(MSG_TASKDELETED, _task.getName());
	}
	
	
}
