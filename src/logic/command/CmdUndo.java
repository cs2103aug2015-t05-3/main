package logic.command;

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
		
		return toUndo == null ? new CommandAction(MSG_UNDOEMPTY,false,null) : toUndo.undo();
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
