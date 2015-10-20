package logic.command;

import java.util.List;

import taskCollections.Task;


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
		
		if(_taskTree.size() == 0){
			String outputMsg = MSG_EMPTY_TASKTREE;
			boolean isUndoable = false;
			return new CommandAction(outputMsg, isUndoable);
		}
		
		List<Task> taskList = getAllTask();
		String outputMsg = String.format(MSG_TOTAL_TASK, taskList.size());
		boolean isUndoable = false;
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
		return new String[]{};
	}
	
	private List<Task> getAllTask(){
		return _taskTree.getList();
	}
	
}
