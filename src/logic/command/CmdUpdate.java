package logic.command;

import util.TimeUtil;
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
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKNOUPDATE = "No update was made";
	private static final String MSG_TASKUPDATED = "Updated ID: \"%1$s\"";
	private static final String MSG_INVALIDTIME = "Invalid start/end time given";

	//Help Info
	private static final String HELP_INFO_UPDATE = 
			"<task_ID> [%1$s <task_name>] [%2$s <start_time>] [%3$s <end_time>][%4$s <high/normal/low/h/n/l>]";
	
	private static final int NO_TIME = 0;
	
	/*
	 * Variables for internal use
	 */
	private Task _task;
	private int _taskID;
	private boolean _noParam;

	//variables for updating
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

		String paramTaskID = getParameterValue(CmdParameters.PARAM_NAME_TASK_ID);
		_task = proccessTaskID(paramTaskID);
		if(_task == null){
			outputMsg = String.format(MSG_TASKIDNOTFOUND, paramTaskID);
			isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, null);
		}

		proccessOptionalFields();
		
		if(_noParam){
			outputMsg = MSG_TASKNOUPDATE;
			isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, null);
		}
		
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
		String outputMsg = updateTask(_task, _prevTaskName, _prevStartTime, _prevEndTime, _prevPriority);
		boolean isUndoable = true;
		return new CommandAction(outputMsg, isUndoable, _taskTree.searchFlag(FLAG_TYPE.NULL));
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
		return new String[] { CmdParameters.PARAM_NAME_TASK_SNAME, CmdParameters.PARAM_NAME_TASK_STARTTIME,
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
		int taskID = Integer.parseInt(paramTaskID);
		_taskID = taskID;
		return _taskTree.getTask(taskID);
	}

	private void proccessOptionalFields(){
		int nullParamCounter = 0;
		String paramTaskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_SNAME);
		if(paramTaskName == null || paramTaskName.equals("")){
			_newTaskName = _task.getName();
			nullParamCounter++;
		}else{
			_newTaskName = paramTaskName;
		}

		String paramStartTime = getParameterValue(CmdParameters.PARAM_NAME_TASK_STARTTIME);
		try{
			_newStartTime = Long.parseLong(paramStartTime);
		}catch(Exception e){
			_newStartTime = _task.getStartTime();
			nullParamCounter++;
		}
		
		String paramEndTime = getParameterValue(CmdParameters.PARAM_NAME_TASK_ENDTIME);
		try{
			_newEndTime = Long.parseLong(paramEndTime);
		}catch(Exception e){
			_newEndTime = _task.getEndTime();
			nullParamCounter++;
		}

		String paramPriority = getParameterValue(CmdParameters.PARAM_NAME_TASK_PRIORITY);
		if(paramPriority == null || paramPriority.equals("")){
			_newPriority = _task.getPriority();
			nullParamCounter++;
		}else{
			_newPriority = getPriorityType(paramPriority);
		}
		
		if(nullParamCounter == 4){
			_noParam = true;
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
		
		System.out.println("NewStartTime: "+newStartTime);
		System.out.println("NewEndTime: "+newEndTime);
		System.out.println("prevStartTime: "+_task.getStartTime());
		System.out.println("prevEndTime: "+_task.getEndTime());
		
		if(newStartTime == _task.getStartTime() && newEndTime == _task.getEndTime()){
			return false;
		}
		
		if(newStartTime == NO_TIME && newEndTime == NO_TIME){
			return false;
		}
		
		if(TimeUtil.compareMinTime(newEndTime, newStartTime)<=0){
			return true;
		}
		
		/*
		if((newEndTime-newStartTime) < 0){
			return true;
		}
		*/
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
