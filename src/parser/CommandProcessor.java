/**
 * Resolves command related stuff
 * 
 */
package parser;

import java.util.HashMap;

import logic.command.CmdAdd;
import logic.command.CmdDelete;
import logic.command.CmdExit;
import logic.command.CmdHelp;
import logic.command.CmdList;
import logic.command.CmdMark;
import logic.command.CmdSearch;
import logic.command.CmdUndo;
import logic.command.CmdUpdate;
import logic.command.Command;
import storage.CommandFileHandler;
import util.StringUtil;

/**
 * @author Yan Chan Min Oo
 *
 */
public class CommandProcessor {
	/*
	 * Constants
	 */
	private static final String CONFIG_CMD_ADD = "add";
	private static final String CONFIG_CMD_DELETE = "delete";
	private static final String CONFIG_CMD_EXIT = "exit";
	private static final String CONFIG_CMD_HELP = "help";
	private static final String CONFIG_CMD_LIST = "list";
	private static final String CONFIG_CMD_MARK = "mark";
	private static final String CONFIG_CMD_SEARCH = "search";
	private static final String CONFIG_CMD_UNDO = "undo";
	private static final String CONFIG_CMD_UPDATE = "update";
	
	/*
	 * Variables
	 */
	private HashMap<String, String> cmdTable;
	private static CommandProcessor cp;
	
	private CommandProcessor(){
	}
	
	public static CommandProcessor getInstance(){
		if(cp == null){
			cp = new CommandProcessor();
		}
		return cp;
	}
	
	public boolean initCmdList(String cmdFileName) {
		try {
			cmdTable = new CommandFileHandler(cmdFileName).getCmdTable();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * Extracts the command string to execute
	 * 
	 * @param userCmd
	 *            The entire user input string
	 * @return The command string
	 */
	public String getCmd(String userCmd) {
		return StringUtil.getFirstWord(userCmd);
	}
	
	public Command getCmdType(String userCmd) {
		String actualCmd = cmdTable.get(userCmd);

		if (actualCmd == null) {// Unable to resolve the command. Try to use the default command
			actualCmd = userCmd;
		}

		switch (actualCmd) {
		case CONFIG_CMD_ADD:
			return new CmdAdd();
		case CONFIG_CMD_DELETE:
			return new CmdDelete();
		case CONFIG_CMD_EXIT:
			return new CmdExit();
		case CONFIG_CMD_HELP:
			return new CmdHelp();
		case CONFIG_CMD_LIST:
			return new CmdList();
		case CONFIG_CMD_MARK:
			return new CmdMark();
		case CONFIG_CMD_SEARCH:
			return new CmdSearch();
		case CONFIG_CMD_UNDO:
			return new CmdUndo();
		case CONFIG_CMD_UPDATE:
			return new CmdUpdate();
		default:
			return null;
		}
	}

}
