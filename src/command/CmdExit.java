package command;

public class CmdExit extends Command {

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String undo() {
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
