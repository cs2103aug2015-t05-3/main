package logic.command;

import java.util.List;

import constants.CmdParameters;
import parser.ParserConstants;
import taskCollections.Task;
import taskCollections.Attributes.TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import taskCollections.Task.FLAG_TYPE;


public class CmdList extends Command {
	
	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_EMPTY_TASKTREE = "No tasks to display";
	private static final String MSG_TOTAL_TASK = "Total tasks in list: [%1$s]";
	
	//Help Info
	private static final String HELP_INFO_LIST = "[%1$s or %2$s or %3$s <high/normal/low/h/n/l>]";
	
	public CmdList(){
		
	}
	
	@Override
	public CommandAction execute(){
		
		String outputMsg;
		boolean isUndoable = false; //List is undoable
		
		if(_taskTree.size() == 0){
			outputMsg = MSG_EMPTY_TASKTREE;
			isUndoable = false;
			return new CommandAction(outputMsg, isUndoable, _taskTree.getList());
		}
		
		String optionalParameter = getParameterValue(CmdParameters.PARAM_NAME_LIST_FLAG);
		List<Task> taskList = proccessParameter(optionalParameter);
		outputMsg = String.format(MSG_TOTAL_TASK, taskList.size());
		isUndoable = false;
		return new CommandAction(outputMsg, isUndoable, taskList);
		
	}
	
	@Override
	public CommandAction undo(){
		//do nothing (List should not have undo)     
		return null;
	}
	
	@Override
	public boolean isUndoable(){
		return false;
	}
	
	@Override
	public String[] getRequiredFields() {
		return new String[]{};
	}

	@Override
	public String[] getOptionalFields() {
		return new String[]{ CmdParameters.PARAM_NAME_LIST_FLAG, CmdParameters.PARAM_NAME_TASK_PRIORITY };
	}
	
	@Override
	public String getHelpInfo(){
		return String.format(HELP_INFO_LIST, ParserConstants.TASK_FILTER_ALL, 
				ParserConstants.TASK_FILTER_DONE, ParserConstants.TASK_SPECIFIER_PRIORITY);
	}
	
	private List<Task> getUndoneTask(){
		return _taskTree.searchFlag(FLAG_TYPE.NULL);
	}
	
	private List<Task> getDoneTask(){
		return _taskTree.searchFlag(FLAG_TYPE.DONE);
	}
	
	private List<Task> getPriorityTask(String priority){
		
		if(priority == null){
			priority = "";
		}
		System.out.println(priority);
		PRIORITY_TYPE priorityType;
		switch(priority){
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
		
		return _taskTree.searchPriority(priorityType);
		
	}
	
	private List<Task> getAllTask(){
		return _taskTree.getList();
	}
	
	private List<Task> proccessParameter(String parameter){
		
		if(parameter == null){
			parameter = "";
		}
		
		List<Task> taskList;
		
		switch(parameter){
			case CmdParameters.PARAM_VALUE_LIST_ALL:
				taskList = getAllTask();
				break;
			case CmdParameters.PARAM_VALUE_LIST_DONE:
				taskList = getDoneTask();
				break;
			case CmdParameters.PARAM_VALUE_LIST_PRIORITY:
				String paramPriority = getParameterValue(CmdParameters.PARAM_NAME_TASK_PRIORITY);
				taskList = getPriorityTask(paramPriority);
				break;
			default:
				taskList = getUndoneTask();
				break;
		}
		
		return taskList;
	}
	
}
