package logic.command;

import java.util.List;

import constants.CmdParameters;
import parser.ParserConstants;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;

public class CmdUpdate extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNAMENOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	//private static final String MSG_TASKUPDATED = "Updated : \"%1$s\" to \"%2$s\"";
	private static final String MSG_ISNTANCEFOUND = "[%1$s] instances of \"%2$s\" found";
	private static final String MSG_TASKNOTUPDATED = "Empty String. Task not updated";
	private static final String MSG_TASKNOCHANGE = "No changes was made";
	private static final String MSG_TASKUPDATED = "Updated ID: \"%1$s\"";
	private static final String MSG_INVALIDTIME = "Invalid start/end time given";

	//Help Info
	private static final String HELP_INFO_UPDATE = 
			"update <task_ID> [%1$s <task_name>] [%2$s <start_time>] [%3$s <end_time>][%4$s <high/normal/low/h/n/l>]";
	
	/*
	 * Variables for internal use
	 */
	private Task _task;
	private String _taskName;
	private int _taskID;

	private String _newTaskName;
	private long _newStartTime;
	private long _newEndTime;
	private PRIORITY_TYPE _newPriority;

	//variables for undo
	private String _prevTaskName;
	private long _prevStartTime;
	private long _prevEndTime;
	private PRIORITY_TYPE _prevPriority;

	public CmdUpdate() {

	}

	public CmdUpdate(String taskName) {
		_taskName = taskName;
	}

	@Override
	public CommandAction execute() {

		String outputMsg;
		boolean isUndoable;

		//Try undo 1st
		_task = getTask();
		if(isUndo()){
			outputMsg = updateTask(_task, _prevTaskName, _prevStartTime, _prevEndTime, _prevPriority);
			isUndoable = true;
			return new CommandAction(outputMsg, isUndoable,  _taskTree.searchFlag(FLAG_TYPE.NULL));
		}

		String paramTaskID = getRequiredFields()[0];
		_task = proccessTaskID(paramTaskID);
		if(_task == null){
			outputMsg = MSG_TASKIDNOTFOUND;
			isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, null);
		}

		String[] paramOptionalFields = getOptionalFields();
		proccessOptionalFields(paramOptionalFields);
		if(isInvalidTime(_newStartTime, _newEndTime)){
			outputMsg = MSG_INVALIDTIME;
			isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, null);
		}

		outputMsg = updateTask(_task, _newTaskName, _newStartTime, _newEndTime, _newPriority);
		isUndoable = true;
		return new CommandAction(outputMsg, isUndoable, _taskTree.searchFlag(FLAG_TYPE.NULL));
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
		return new String[] { CmdParameters.PARAM_NAME_TASK_ID };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_NAME, CmdParameters.PARAM_NAME_TASK_STARTTIME,
				CmdParameters.PARAM_NAME_TASK_ENDTIME, CmdParameters.PARAM_NAME_TASK_PRIORITY };
	}

	@Override
	public String getHelpInfo(){
		return String.format(HELP_INFO_UPDATE, ParserConstants.TASK_SPECIFIER_TASKNAME,
				ParserConstants.TASK_SPECIFIER_STARTTIME, ParserConstants.TASK_SPECIFIER_ENDTIME,
				ParserConstants.TASK_SPECIFIER_PRIORITY);
	}
	
	private boolean isUndo(){
		if(_task == null){
			return false;
		}else{
			return true;
		}
	}

	private Task proccessTaskID(String paramTaskID){
		paramTaskID = getParameterValue(paramTaskID); // TODO hotfix
		int taskID = Integer.parseInt(paramTaskID);
		_taskID = taskID;
		return _taskTree.getTask(taskID);
	}

	private void proccessOptionalFields(String[] paramOptionalFields){

		if(paramOptionalFields[0] != null){
			_newTaskName = paramOptionalFields[0];
		}else{
			_newTaskName = _task.getName();
		}

		if(paramOptionalFields[1] != null){
			_newStartTime = Long.parseLong(paramOptionalFields[1]);
		}else{
			_newStartTime = _task.getStartTime();
		}

		if(paramOptionalFields[2] != null){
			_newEndTime = Long.parseLong(paramOptionalFields[2]);
		}else{
			_newEndTime = _task.getEndTime();
		}

		if(paramOptionalFields[3] != null){
			_newPriority = getPriorityType(paramOptionalFields[3]);
		}else{
			_newPriority = _task.getPriority();
		}

	}

	private PRIORITY_TYPE getPriorityType(String priorityParam){

		PRIORITY_TYPE priorityType;

		switch(priorityParam){
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

		return priorityType;
	}

	private boolean isInvalidTime(long newStartTime, long newEndTime){

		if((newEndTime-newStartTime) > 0){
			return true;
		}

		return false;
	}

	private String updateTask(Task task, String newTaskName, long newStartTime, long newEndTime, PRIORITY_TYPE newPriority){
		//Set prev task details
		_prevTaskName = task.getName();
		_prevStartTime = task.getStartTime();
		_prevEndTime = task.getEndTime();
		_prevPriority = task.getPriority();

		//Update
		_taskTree.updateName(task, newTaskName);
		_taskTree.updateStartTime(task, newStartTime);
		_taskTree.updateEndTime(task, newEndTime);
		_taskTree.updatePriority(task, newPriority);
		return String.format(MSG_TASKUPDATED, _taskID);
	}

}
