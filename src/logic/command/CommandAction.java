package logic.command;

import java.util.List;
import taskCollections.Task;

public class CommandAction {

	private String _outputMsg;		//Message to be printed above the command line
	private boolean _isUndoable;	//Determine if action is undoable
	private List<Task> _taskList;	//List of task to be displayed in table
	
	public CommandAction(String outputMsg, boolean isUndoable, List<Task> taskList){
		_outputMsg = outputMsg;
		_isUndoable = isUndoable;
		_taskList = taskList;
	}
	
	public String getOutput(){
		return _outputMsg;
	}
	
	public boolean isUndoable(){
		return _isUndoable;
	}
	
	public List<Task> getTaskList(){
		return _taskList;
	}
}
