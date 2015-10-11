package logic.command;

public class CmdHelp extends Command{

	public CmdHelp(){
		
	}
	
	@Override
	public String execute(){
		return null;
	}
	
	@Override
	public String undo(){
		//do nothing (Help should not have undo)
		return null;
	}
	
	@Override
	public boolean isUndoable(){
		return false;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[]{};
	}

	@Override
	public String[] getOptionalFields() {
		// TODO Auto-generated method stub
		return new String[]{};
	}
	
}
