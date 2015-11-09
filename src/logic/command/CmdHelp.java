//@@author A0125574A

/**
 * Command to display a help window containing syntax of all command 
 */

package logic.command;

import ui.UIHelper;

public class CmdHelp extends Command {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_HELP = "Help shown";

	// Help constants
	private static final String HELP_INFO_HELP = "Just help...";

	public CmdHelp() {

	}

	/**
	 * Display help message with syntax of other commands
	 * 
	 * @return a CommandAction
	 */
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

	/**
	 * Returns a syntax message for help command
	 * 
	 * @return a syntax message for help command
	 */
	@Override
	public String getHelpInfo() {
		return HELP_INFO_HELP;
	}

}
