package logic.command;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CmdHelp extends Command{

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_HELP = "Help shown";

	// Help constants
	private static final String HELP_INFO_HELP = "help [<command>]";
	private static final String HELP_ALL =
			"<html>"
			+ "<h1>List of Commands</h1>"
			+ "<tr>"
				+ "<th>Operation</th>"
				+ "<th>Command</th>"
			+ "</tr>"
			+ "<tr>"
				+ "<td>Add a task</td>"
				+ "<td>add &lt;task name&gt;</td>"
			+ "</tr>"
			+ "<tr>"
				+ "<td>Delete a task</td>"
				+ "<td>delete &lt;task name&gt;</td>"
			+ "</tr>"
			+ "<tr>"
				+ "<td>Update a task</td>"
				+ "<td>update &lt;task name&gt;</td>"
			+ "</tr>"
			+ "<tr>"
				+ "<td>Search task(s)</td>"
				+ "<td>search &lt;task name&gt;</td>"
			+ "</tr>"
			+ "<tr>"
				+ "<td>List task(s)</td>"
				+ "<td>list</td>"
			+ "</tr>"
			+ "<tr>"
				+ "<td>Mark a task(s)</td>"
				+ "<td>mark &lt;task name&gt;</td>"
			+ "</tr>"
			+ "<tr>"
				+ "<td>Undo</td>"
				+ "<td>undo</td>"
			+ "</tr>"
			+ "<tr>"
				+ "<td>Exit TaskBuddy</td>"
				+ "<td>exit</td>"
			+ "</tr>"
			+ "</html>";

	public CmdHelp(){

	}

	/*
	// For testing JOptionPane
	public static void main(String[] args){
		CmdHelp help = new CmdHelp();
		help.execute();
	}
	*/

	@Override
	public CommandAction execute(){
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(null, HELP_ALL);
		boolean isUndoable = false;
		return new CommandAction(MSG_HELP, isUndoable, null);
	}

	@Override
	public CommandAction undo(){
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
	
	@Override
	public String getHelpInfo(){
		return HELP_INFO_HELP;
	}

}
