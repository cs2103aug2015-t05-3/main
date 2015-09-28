package command;

import tds.Task;
import tds.TaskTree;
import util.TimeUtil;
import java.util.List;


public class CmdList extends Command {
	
	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_EMPTY_TASKTREE = "No task to display";
	

	// Error codes
	/*
	 * private enum ERROR { OK, TASKUNSPECIFIED }
	 */

	/*
	 * Variables for internal use
	 */
	
	
	public CmdList(){
		
	}
	
	@Override
	public String execute(){
		
		if(TaskTree.size() == 0){
			return MSG_EMPTY_TASKTREE;
		}
		
		return listAllTask();
		
	}
	
	@Override
	public String undo(){
		//do nothing (Display should not have undo)
		return null;
	}
	
	@Override
	public boolean isUndoable(){
		return false;
	}
	
	/**
	 * Retrieves all Tasks and format them into a String for printing
	 * 
	 * 
	 * @return Details of  all Tasks in String format
	 * 
	 */
	private String listAllTask(){
		
		String displayAllTask = "";
		List<Task> taskList = TaskTree.getList();
		
		for(int i=0; i<taskList.size(); i++){
			Task currTask = taskList.get(i);
			displayAllTask = displayAllTask + displayTask(currTask) + System.lineSeparator();
		}
		
		return displayAllTask;
	}
	
	/**
	 * Retrieves all details from a Task and format them into a String for printing
	 * 
	 * @param task
	 *            a task
	 *            
	 * 
	 * @return Details of a Task in String format
	 * 
	 */
	private String displayTask(Task task){
		
		String displayTask = 
				"Task ID: " + task.getId() + System.lineSeparator() +
				"Task Name: " + task.getName() + System.lineSeparator() +
				"Task Prioirty: " + task.getPriority() + System.lineSeparator();
		
		if(task.getStartTime() == Task.DATE_NULL){
			String start = TimeUtil.getDate(task.getStartTime());
			displayTask = displayTask + "Start Time: " + start + System.lineSeparator();
		}
		
		if(task.getEndTime() == Task.DATE_NULL){
			String end = TimeUtil.getDate(task.getEndTime());
			displayTask = displayTask + "End Time: " + end + System.lineSeparator();
		}
		
		return displayTask;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[]{};
	}

	@Override
	public String[] getOptionalFields() {
		return new String[]{};
	}
	
}
