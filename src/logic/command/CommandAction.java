package logic.command;

import java.util.List;
import taskCollections.Task;

public class CommandAction {

	private String _outputMsg; // Message to be printed above the command line
	private boolean _isUndoable; // Determine if a command action can be undone
	private List<Task> _taskList; // List of tasks to be displayed in UI table

	public CommandAction(String outputMsg, boolean isUndoable, List<Task> taskList) {
		_outputMsg = outputMsg;
		_isUndoable = isUndoable;
		_taskList = taskList;
	}

	public String getOutput() {
		return _outputMsg;
	}

	public boolean isUndoable() {
		return _isUndoable;
	}

	public List<Task> getTaskList() {
		return _taskList;
	}
}
