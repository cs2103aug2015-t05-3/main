package logic.command;

import ui.UIHelper;

public class CmdExit extends Command {

	// Help Info
	private static final String HELP_INFO_EXIT = "Just exit...";

	@Override
	public CommandAction execute() {
		UIHelper.destroyUI();
		System.exit(0);
		return new CommandAction("", false, null);
	}

	@Override
	public CommandAction undo() {
		// do nothing (Exit should not have undo)
		return null;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] {};
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] {};
	}

	@Override
	public String getHelpInfo() {
		return HELP_INFO_EXIT;
	}

}
