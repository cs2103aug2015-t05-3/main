package logic.command;

public class CmdMark extends Command{

	@Override
	public CommandAction execute() {
		return null;
	}

	@Override
	public CommandAction undo() {
		return null;
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public String[] getRequiredFields() {
		return null;
	}

	@Override
	public String[] getOptionalFields() {
		return null;
	}

}
