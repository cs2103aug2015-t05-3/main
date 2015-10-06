package command;

import java.util.List;

import constants.CmdParameters;
import tds.Task;
import tds.TaskTree;

public class CmdUpdate extends Command {

	/*
	 * Constants
	 */
	// Variable constants
	private static final int INPUT_NO_UPDATE = 0;
	private static final int INPUT_DEFAULT_VALUE = -1;
	
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKUPDATED = "Updated : \"%1$s\" to \"%2$s\"";
	private static final String MSG_TASKNOTUPDATED = "Task not updated";

	// Error codes
	/*
	 * private enum ERROR { OK, TASKUNSPECIFIED }
	 */

	/*
	 * Variables for internal use
	 */
	private Task updateTask;
	private String taskName;
	
	//variables for undo
	private String prevTaskName;
	//To be included in later version
	private long prevTaskStartTime;
	private long prevTaskEndTime;

	public CmdUpdate() {

	}

	public CmdUpdate(String taskName) {
		this.taskName = taskName;
	}
	
	@Override
	public String execute() {
		
		taskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_NAME);
		
		if (taskName == null || taskName.equals("")) {
			return MSG_TASKUNSPECIFIED;
		}
		
		List<Task> updateTaskList = searchTask(taskName);
		
		return updateTask(updateTaskList);
	}

	@Override
	public String undo() {
		
		Command update = new CmdUpdate();
		update.setTask(updateTask);
		update.setParameter(prevTaskName, null);
		

		return execute();
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_NAME };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME};
	}
	
	private List<Task> searchTask(String taskName){
		return TaskTree.searchName(taskName);
	}
	
	private String updateTask(List<Task> updateTaskList){
		
		//Case 1: List.size is empty
		if(updateTaskList.isEmpty()){
			return String.format(MSG_TASKNOTFOUND, taskName);
		}
		
		//Case 2: List.size == 1
		if(updateTaskList.size() == 1){
			updateTask = updateTaskList.get(0);
			prevTaskName = updateTask.getName();
			//prevTaskName
			//TaskTree.updateName(updateTask, newTaskName)
			//TaskTree.(updateTask);
			return String.format(MSG_TASKUPDATED, taskName);
		}
		
		//Case 3: List.size > 1
		int input = getUserInput(updateTaskList);
		if(input == INPUT_NO_UPDATE){
			return MSG_TASKNOTUPDATED;
		}else{
			updateTask = updateTaskList.get(input);
		}
			/*	
		//Case 2: List.size > 2
		if(deleteTaskList.size() > 1){
			int input = getUserInput(deleteTaskList);
					if(input == INPUT_NO_DELETE){
						return MSG_NOTASKDELETED;
					}else{
						deleteTask = deleteTaskList.get(input);
						TaskTree.remove(deleteTask);
						return String.format(MSG_TASKDELETED, taskName);
					}
				}
				
				//Case 3: List.size == 1
				deleteTask = deleteTaskList.get(0);
				TaskTree.remove(deleteTask);
				return String.format(MSG_TASKDELETED, taskName);
		*/
		return String.format(MSG_TASKUPDATED, taskName);
	}
	
	
	private int getUserInput(List<Task> updateTaskList){
		return 0;
	}

}
