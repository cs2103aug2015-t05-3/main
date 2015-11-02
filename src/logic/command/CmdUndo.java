package logic.command;

import java.util.List;

import taskCollections.Task;

public class CmdUndo extends Command {
	
	/*
	 * Constants
	 */
	//private static final String MSG_UNDO = "Undo: ";
	private static final String MSG_UNDOEMPTY = "No commands to undo!";

	//Help Info
	private static final String HELP_INFO_UNDO = "Just undo...";
	
	@Override
	public CommandAction execute() {
		Command toUndo = extractHistory();
		System.out.println("Undo History: " + toUndo);
		
		if(toUndo == null){
			return new CommandAction(MSG_UNDOEMPTY,false,null);
		}else{
			CommandAction undoCommandAction = toUndo.undo();
			String outputMsg = undoCommandAction.getOutput();
			boolean isUndoable = false;
			List<Task> taskList = undoCommandAction.getTaskList();
			return new CommandAction(outputMsg, isUndoable, taskList);
		}
	}

	@Override
	public CommandAction undo() {
		return null;
	}

	@Override
	public boolean isUndoable() {
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

	@Override
	public String getHelpInfo(){
		return HELP_INFO_UNDO;
	}
	
}
