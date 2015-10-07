package command;

import ui.UIHelper;

public class CmdClear extends Command{

	// Variable constants
	private static final String EMPTY_STRING = "";
	
	@Override
	public String execute() {
		//Current problem: new line 
		UIHelper.setOutput(EMPTY_STRING);
		return EMPTY_STRING;
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
