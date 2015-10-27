package logic.command;

import constants.CmdParameters;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;

public class CmdMark extends Command{

	/*
	 * Constants
	 */	
	// Message constants
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKMARKED = "Marked: \"%1$s\"";
	private static final String MSG_TASKUNMARKED = "Unmarked: \"%1$s\"";
	private static final String MSG_TASKALREADYMARKED = "Task: \"%1$s\" already marked";
	private static final String MSG_TASKALREADYUNMARKED = "Task: \"%1$s\" already marked";
	
	/*
	 * Variables for internal use
	 */
	private Task _task;
	
	public CmdMark() {

	}
	
	@Override
	public CommandAction execute() {
		
		String outputMsg;
		boolean isUndoable;
		
		
		//Try undo first
		_task = getTask();
		if(isUndo()){
			if(isMarked(_task)){
				outputMsg = unmarkTask(_task);
			}else{
				outputMsg = markTask(_task);
			}
			isUndoable = true;
			return new CommandAction(outputMsg, isUndoable, _taskTree.searchFlag(FLAG_TYPE.NULL));
		}
				
		
		String paramTaskID = getRequiredFields()[0];
		_task = proccessTaskID(paramTaskID);
		if(_task == null){
			outputMsg = MSG_TASKIDNOTFOUND;
			isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, _taskTree.searchFlag(FLAG_TYPE.NULL));
		}
				
		String optionalParameter = getOptionalFields()[0];
		return proccessParameter(optionalParameter);
	}

	@Override
	public CommandAction undo() {
		return null;
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
		return new String[] { CmdParameters.PARAM_NAME_MARK_FLAG };
	}

	private boolean isUndo(){
		if(_task == null){
			return false;
		}else{
			return true;
		}
	}
	
	private boolean isMarked(Task task){
		if(task.getFlag() == FLAG_TYPE.NULL){
			return false;
		}else{
			return true;
		}
	}
	
	private String markTask(Task task){
		String outputMsg = "";
		_taskTree.updateFlag(task, FLAG_TYPE.DONE);
		outputMsg = String.format(MSG_TASKMARKED, task.getName());
		return outputMsg;
	}
	
	private String unmarkTask(Task task){
		String outputMsg = "";
		_taskTree.updateFlag(task, FLAG_TYPE.NULL);
		outputMsg = String.format(MSG_TASKUNMARKED, task.getName());
		return outputMsg;
	}
	
	private Task proccessTaskID(String paramTaskID){
		int taskID = Integer.parseInt(paramTaskID);
		return _taskTree.getTask(taskID);
	}
	
	private CommandAction proccessParameter(String parameter){
		
		String outputMsg;
		boolean isUndoable;
		
		if(parameter == null){
			if(_task.getFlag() == FLAG_TYPE.DONE){
				outputMsg = String.format(MSG_TASKALREADYMARKED, _task.getName());
				isUndoable = false;
			}else{
				outputMsg = markTask(_task);
				isUndoable = true;
			}
		}else{
			if(_task.getFlag() == FLAG_TYPE.NULL){
				outputMsg = String.format(MSG_TASKALREADYUNMARKED, _task.getName());
				isUndoable = false;
			}else{
				outputMsg = unmarkTask(_task);
				isUndoable = true;
			}
		}
		
		return new CommandAction(outputMsg, isUndoable, _taskTree.searchFlag(FLAG_TYPE.NULL));
	}
	
}
