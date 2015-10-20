package logic.command;

import java.util.List;

import taskCollections.Task;

public class CmdUndo extends Command {
	
	/*
	 * Constants
	 */
	//private static final String MSG_UNDO = "Undo: ";
	private static final String MSG_UNDOEMPTY = "No commands to undo!";

	@Override
	public CommandAction execute() {
		Command toUndo = extractHistory();
		
		//return toUndo == null ? MSG_UNDOEMPTY : MSG_UNDO + toUndo.undo();
		
		if(toUndo == null){
			String outputMsg = MSG_UNDOEMPTY;
			boolean isUndoable = false;
			return new CommandAction(outputMsg, isUndoable);
		}
		
		return toUndo.undo();
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

	
	
}
