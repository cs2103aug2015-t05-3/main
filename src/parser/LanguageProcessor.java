package parser;

import java.util.HashMap;

import command.*;
import fileProcessor.CommandFileHandler;
import util.StringUtil;

/**
 * Translates and breaks natural language down for computation
 * @author Yan Chan Min Oo
 *
 */

public class LanguageProcessor {
	/*
	 * Constants
	 */
	//private static final String FILE_NAME_CONFIG = "config.xml";
	private static final String CONFIG_CMD_ADD = "add";
	private static final String CONFIG_CMD_CLEAR = "clear";
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
	private HashMap<String,String> cmdTable;
	
	public LanguageProcessor(String cmdFileName){
		initCmdList(cmdFileName);// TODO: EXIT PROGRAM OR SOMETHING IF CANT INIT
	}
	
	public boolean initCmdList(String cmdFileName){
		try {
			cmdTable = new CommandFileHandler(cmdFileName).getCmdTable();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public Command resolveCmd(String userCmd){
		String cmd = getCmd(userCmd);
		Command toExecute = getCmdType(cmd);
		if(toExecute == null){
			return null;
		}
		for(String requiredField : toExecute.getRequiredFields()){
			toExecute.setParameter(requiredField, getTaskName(userCmd));
		}
		for(String optionalField : toExecute.getOptionalFields()){
			toExecute.setParameter(optionalField, getTaskName(userCmd));
		}
		return toExecute;
	}
	
	/**
	 * Extracts the command string to execute
	 * @param userCmd The entire user input string
	 * @return The command string
	 */
	private String getCmd(String userCmd){
		return StringUtil.getFirstWord(userCmd);
	}
	
	private String getTaskName(String userCmd){
		StringUtil.removeFirstWord(userCmd);
		return userCmd.split(" ", 2)[1]; // TODO: Move to String Util
	}
	
	private Command getCmdType(String userCmd){
		String actualCmd = cmdTable.get(userCmd);
		
		if (actualCmd == null){// Unable to resolve the command. Try to use the default command
			actualCmd = userCmd;
		}
		
		switch(actualCmd){
		case CONFIG_CMD_ADD: return new CmdAdd();
		case CONFIG_CMD_CLEAR: return new CmdClear();
		case CONFIG_CMD_DELETE: return new CmdDelete();
		case CONFIG_CMD_EXIT: return new CmdExit();
		case CONFIG_CMD_HELP : return new CmdHelp();
		case CONFIG_CMD_LIST : return new CmdList();
		case CONFIG_CMD_MARK : return new CmdMark();
		case CONFIG_CMD_SEARCH : return new CmdSearch();
		case CONFIG_CMD_UNDO : return new CmdUndo();
		case CONFIG_CMD_UPDATE : return new CmdUpdate();
		default: return null;
		}
	}
}
