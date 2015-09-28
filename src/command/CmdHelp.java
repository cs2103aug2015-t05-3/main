package command;

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
	public boolean isManipulative(){
		return false;
	}
	
}
