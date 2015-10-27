package logic.command;

import java.util.List;

import constants.CmdParameters;
import taskCollections.Task;
import taskCollections.Attributes.TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import taskCollections.Task.FLAG_TYPE;


public class CmdList extends Command {
	
	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_EMPTY_TASKTREE = "No task to display";
	private static final String MSG_TOTAL_TASK = "Total tasks in list: [%1$s]";
	
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
		
		String optionalParam = getOptionalFields()[0];
		List<Task> taskList = proccessParam(optionalParam);
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
		return new String[]{ CmdParameters.PARAM_NAME_LIST_FLAG };
	}
	
	private List<Task> getUndoneTask(){
		return _taskTree.searchFlag(FLAG_TYPE.NULL);
	}
	
	private List<Task> getDoneTask(){
		return _taskTree.searchFlag(FLAG_TYPE.DONE);
	}
	
	private List<Task> getPriorityTask(){
		
		/*
		PRIORITY_TYPE priorityType;
		switch(paramPriorityType){
			case "HIGH":
				priorityType = PRIORITY_TYPE.HIGH;
				break;
			case "NORMAL":
				priorityType = PRIORITY_TYPE.NORMAL;
				break;
			case "LOW":
				priorityType = PRIORITY_TYPE.LOW;
				break;
			default:
				priorityType = PRIORITY_TYPE.NORMAL;
				break;
		}
		*/
		
		return _taskTree.getSortedList(TYPE.PRIORITY);
		
	}
	
	private List<Task> getAllTask(){
		return _taskTree.getList();
	}
	
	private List<Task> proccessParam(String param){
		
		List<Task> taskList;
		
		switch(param){
			case CmdParameters.PARAM_VALUE_LIST_ALL:
				taskList = getAllTask();
				break;
			case CmdParameters.PARAM_VALUE_LIST_DONE:
				taskList = getDoneTask();
				break;
			case CmdParameters.PARAM_VALUE_LIST_PRIORITY:
				taskList = getPriorityTask();
				break;
			default:
				taskList = getUndoneTask();
				break;
		}
		
		return taskList;
	}
	
}
