package logic.command;

import java.util.ArrayList;
import java.util.List;
import taskCollections.Task;

public class CommandAction {

	private String _output;
	private boolean _isUndoable;
	private List<Task> _taskList;
	
	
	public CommandAction(String output, boolean isUndoable){
		_output = output;
		_isUndoable = isUndoable;
		_taskList = new ArrayList<Task>();
	}
	
	public CommandAction(String output, boolean isUndoable, List<Task> taskList){
		_output = output;
		_isUndoable = isUndoable;
		_taskList = taskList;
	}
	
	public String getOutput(){
		return _output;
	}
	
	public boolean isUndoable(){
		return _isUndoable;
	}
	
	public List<Task> getTaskList(){
		return _taskList;
	}
}
