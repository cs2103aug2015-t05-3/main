//@@author A0125496X
/**
 * Resolves command related stuff
 * 
 */
package parser;

import java.util.HashMap;

import logic.command.*;
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
	public static final String CONFIG_CMD_ADD = "add";
	public static final String CONFIG_CMD_DELETE = "delete";
	public static final String CONFIG_CMD_EXIT = "exit";
	public static final String CONFIG_CMD_HELP = "help";
	public static final String CONFIG_CMD_LIST = "list";
	public static final String CONFIG_CMD_MARK = "mark";
	public static final String CONFIG_CMD_SEARCH = "search";
	public static final String CONFIG_CMD_UNDO = "undo";
	public static final String CONFIG_CMD_UPDATE = "update";
	
	/*
	 * Variables
	 */
	private HashMap<String, String> cmdTable;
	private static CommandProcessor cmdP;
	
	private CommandProcessor(){
	}
	
	public static CommandProcessor getInstance(){
		if(cmdP == null){
			cmdP = new CommandProcessor();
		}
		return cmdP;
	}
	
	public boolean initCmdList(String cmdFileName) {
		try {
			CommandFileHandler cf = new CommandFileHandler();
			if(!cf.loadCommandFile(cmdFileName)){
				cf.generateCommandFile(cmdFileName);
			}
			cmdTable = cf.getCmdTable();
			addDefaultCmds();
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
		String actualCmd = getEffectiveCmd(userCmd);

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
	
	public String getEffectiveCmd(String cmd){
		return cmdTable.get(cmd);
	}
	
	private void addDefaultCmds(){
		cmdTable.put(CONFIG_CMD_DELETE, CONFIG_CMD_DELETE);
		cmdTable.put(CONFIG_CMD_ADD, CONFIG_CMD_ADD);
		cmdTable.put(CONFIG_CMD_EXIT, CONFIG_CMD_EXIT);
		cmdTable.put(CONFIG_CMD_HELP, CONFIG_CMD_HELP);
		cmdTable.put(CONFIG_CMD_LIST, CONFIG_CMD_LIST);
		cmdTable.put(CONFIG_CMD_MARK, CONFIG_CMD_MARK);
		cmdTable.put(CONFIG_CMD_SEARCH, CONFIG_CMD_SEARCH);
		cmdTable.put(CONFIG_CMD_UNDO, CONFIG_CMD_UNDO);
		cmdTable.put(CONFIG_CMD_UPDATE, CONFIG_CMD_UPDATE);
	}

}
