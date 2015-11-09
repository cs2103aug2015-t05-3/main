//@@author A0125574A

/**
 * Command to undo a previous command 
 */

package logic.command;

public class CmdUndo extends Command {

	/*
	 * Constants
	 */
	private static final String MSG_UNDOEMPTY = "No commands to undo!";

	// Help Info
	private static final String HELP_INFO_UNDO = "Just undo...";

	/**
	 * Undo a previous command
	 * 
	 * @return a CommandAction
	 */
	@Override
	public CommandAction execute() {
		Command toUndo = extractHistory();

		if (toUndo == null) {
			return new CommandAction(MSG_UNDOEMPTY, false, null);
		} else {
			return undoCommand(toUndo);
		}
	}

	@Override
	public CommandAction undo() {
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
	 * Returns a syntax message for undo command
	 * 
	 * @return a syntax message for undo command
	 */
	@Override
	public String getHelpInfo() {
		return HELP_INFO_UNDO;
	}

	/**
	 * Returns a CommandAction of undoing a previous command
	 * 
	 * @return a CommandAction of undoing a previous command
	 */
	private CommandAction undoCommand(Command toUndo) {

		assert toUndo != null;

		CommandAction undoCommandAction = toUndo.undo();
		return new CommandAction(undoCommandAction.getOutput(), false, undoCommandAction.getTaskList());
	}

}
