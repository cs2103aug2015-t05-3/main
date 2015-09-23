package command;

public abstract class Command {
	
	//public Stack historyStack;
	//DataStructure?
	
	public abstract String execute();
	public abstract String undo();
}
