package logic.command;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import constants.CmdParameters;
import ui.UIHelper;
import logic.TaskBuddy;
import taskCollections.Task;
import taskCollections.TaskTree;

public class CmdDelete extends Command {

	/*
	 * Constants
	 */
	// Variable constants
	private static final int INPUT_NO_DELETE = 0;
	private static final int INPUT_DEFAULT_VALUE = -1;
	
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKDELETED = "Deleted : \"%1$s\"";
	private static final String MSG_NOTASKDELETED = "No task deleted";
	
	private static final String MSG_INVALID_INPUT = "Invalid input.";

	// Error codes
	/*
	 * private enum ERROR { OK, TASKUNSPECIFIED }
	 */

	/*
	 * Variables for internal use
	 */
	private Task deleteTask;
	private String taskName;

	private static Logger log = Logger.getLogger("log_CmdDelete");
	
	public CmdDelete() {

	}

	public CmdDelete(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public String execute() {
		
		deleteTask = getTask();
		
		if(deleteTask != null){
			TaskTree.remove(deleteTask);
			return String.format(MSG_TASKDELETED, deleteTask.getName());
		}

		taskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_NAME);

		if (taskName == null || taskName.equals("")) {
			return MSG_TASKUNSPECIFIED;
		}

		List<Task> deleteTaskList = searchTask(taskName);
		//deleteTaskList = null; // for testing
		assert(deleteTaskList != null);
		
		return deleteTask(deleteTaskList);
		
	}

	@Override
	public String undo() {
		Command add = new CmdAdd();
		add.setTask(deleteTask);
		return add.execute();
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
	
	private String deleteTask(List<Task> deleteTaskList){
		
		//Case 1: List.size is empty
		if(deleteTaskList.isEmpty()){
			return String.format(MSG_TASKNOTFOUND, taskName);
		}
		
		String deletedTaskName;
		
		//Case 2: List.size > 1
		if(deleteTaskList.size() > 1){
			int input = getUserInput(deleteTaskList);
			if(input == INPUT_NO_DELETE){
				return MSG_NOTASKDELETED;
			}else{
				int index = input - 1;
				deleteTask = deleteTaskList.get(index);
				deletedTaskName = deleteTask.getName();
				TaskTree.remove(deleteTask);
				return String.format(MSG_TASKDELETED, deletedTaskName);
			}
		}
		
		//Case 3: List.size == 1
		deleteTask = deleteTaskList.get(0);
		deletedTaskName = deleteTask.getName();
		TaskTree.remove(deleteTask);
		return String.format(MSG_TASKDELETED, deletedTaskName);
	}

	private int getUserInput(List<Task> deleteTaskList){
		
		String output = displayDeleteList(deleteTaskList);
		int input = INPUT_DEFAULT_VALUE; 
		
		//TaskBuddy.printMessage(output);
		UIHelper.appendOutput(output);
		
		while(input <= -1 || input > deleteTaskList.size()){
			//input = processInput(TaskBuddy.getInput());
			input = processInput(UIHelper.getUserInput());
			if(input <= -1 || input > deleteTaskList.size()){
				UIHelper.appendOutput(MSG_INVALID_INPUT);
				
			}
		}
		
		return input;
	}
	
	private String displayDeleteList(List<Task> deleteTaskList){
		
		String output = "";
		
		output = output + "[" + deleteTaskList.size() + "]" +
				" instances of \"" + taskName + "\" found:" + System.lineSeparator() ;
		for(int i=0; i<deleteTaskList.size(); i++){
			output = output + (i+1) + ". " + deleteTaskList.get(i).getName() + System.lineSeparator();
		}
		output = output + "\"0\" to exit" + System.lineSeparator();
		output = output + System.lineSeparator();
		
		return output;
		
	}
	
	//Method to be refactored if possible (Should not be in CmdDelete)
	private int processInput(String input){
		int inputNumber = INPUT_DEFAULT_VALUE;
		
		if(input == null || input.equals("")){
			return inputNumber;
		}
		log.log(Level.INFO, "[Start] format String into Integer");
		try{
			inputNumber = Integer.parseInt(input);
		}catch(NumberFormatException e){
			log.log(Level.WARNING, "NumberFormatException");
		}catch(Exception e){
			log.log(Level.SEVERE, "Unkown Exception");
		}
		log.log(Level.INFO, "[End] format String into Integer");
		return inputNumber;
	}
	
}
