package logic.command;

import ui.UIHelper;

public class CmdExit extends Command {

	@Override
	public CommandAction execute() {
		UIHelper.destroyUI();
		System.exit(0);
		return new CommandAction("", false);
	}

	@Override
	public CommandAction undo() {
		//do nothing (Exit should not have undo)
		return null;
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String[] getRequiredFields() {
		// TODO Auto-generated method stub
		return new String[0];
	}

	@Override
	public String[] getOptionalFields() {
		// TODO Auto-generated method stub
		return new String[0];
	}

}
