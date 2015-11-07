package logic.command;

import ui.UIHelper;

public class CmdHelp extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_HELP = "Help shown";

	// Help constants
	private static final String HELP_INFO_HELP = "[<command>]";

	public CmdHelp() {

	}

	@Override
	public CommandAction execute() {
		UIHelper.showHelpPanel();
		return new CommandAction(MSG_HELP, false, null);
	}

	@Override
	public CommandAction undo() {
		// do nothing (Help should not have undo)
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
		return HELP_INFO_HELP;
	}

}
