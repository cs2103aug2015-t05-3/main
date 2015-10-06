package command;

public class CmdMark extends Command{

	@Override
	public String execute() {
		return null;
	}

	@Override
	public String undo() {
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
