//@@author A0125574A

/**
 * Command to exit TaskBuddy 
 */

package logic.command;

import ui.UIHelper;

public class CmdExit extends Command {

	// Help Info
	private static final String HELP_INFO_EXIT = "Just exit...";

	/**
	 * Exits TaskBuddy
	 * 
	 * @return a CommandAction
	 */
	@Override
	public CommandAction execute() {
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

	/**
	 * Returns a syntax message for exit command
	 * 
	 * @return a syntax message for exit command
	 */
	@Override
	public String getHelpInfo() {
		return HELP_INFO_EXIT;
	}

}
