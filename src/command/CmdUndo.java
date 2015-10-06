package command;

public class CmdUndo extends Command {
	
	/*
	 * Constants
	 */
	private static final String MSG_UNDO = "Undo: ";
	private static final String MSG_UNDOEMPTY = "No commands to undo!";

	@Override
	public String execute() {
		Command toUndo = extractHistory();
		
		return toUndo == null ? MSG_UNDOEMPTY : MSG_UNDO + toUndo.undo();
	}

	@Override
	public String undo() {
		return null;
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[0];
	}

	@Override
	public String[] getOptionalFields() {
		return new String[0];
	}

	
	
}
