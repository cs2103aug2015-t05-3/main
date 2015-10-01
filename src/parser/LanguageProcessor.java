package parser;

import java.util.HashMap;

import command.*;
import fileProcessor.CommandFileHandler;

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
	private static final String CONFIG_CMD_DELETE = "delete";
	private static final String CONFIG_CMD_EDIT = "edit";
	private static final String CONFIG_CMD_LIST = "list";
	private static final String CONFIG_CMD_UNDO = "undo";
	private static final String CONFIG_CMD_SEARCH = "search";
	private static final String CONFIG_CMD_HELP = "help";
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
	
	private String getCmd(String userCmd){
		return userCmd.split(" ")[0]; // TODO: Move to String util
	}
	
	private String getTaskName(String userCmd){
		//String[] a = userCmd.split(" ", 2);
		//for (String s : a)System.out.println("@"+s);
		return userCmd.split(" ", 2)[1]; // TODO: Move to String Util
	}
	
	private Command getCmdType(String userCmd){
		String actualCmd = cmdTable.get(userCmd);
		if (actualCmd == null){
			return null;
		}
		switch(actualCmd){
		case CONFIG_CMD_ADD: return new CmdAdd();
		case CONFIG_CMD_DELETE: return new CmdDelete();
		case CONFIG_CMD_EDIT : return null;
		case CONFIG_CMD_LIST : return new CmdList();
		case CONFIG_CMD_SEARCH : return null;
		case CONFIG_CMD_UNDO : return null;
		case CONFIG_CMD_HELP : return new CmdHelp();
		default: return null;
		}
	}
}
