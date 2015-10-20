package parser;

import java.util.HashMap;

import logic.command.*;
import storage.CommandFileHandler;
import util.StringUtil;
import constants.CmdParameters;

/**
 * Translates and breaks natural language down for computation
 * 
 * @author Yan Chan Min Oo
 *
 */

public class LanguageProcessor {
	/*
	 * Variables
	 */
	private static LanguageProcessor langProcessor;
	private static CommandProcessor cmdP;

	private String getTaskName(String userCmd) {
		userCmd = StringUtil.removeFirstWord(userCmd);
		return userCmd;
	}
	
	public boolean init(String cmdFileName){
		cmdP = CommandProcessor.getInstance();
		return cmdP.initCmdList(cmdFileName);
	}
	
	public Command resolveCmd(String userCmd) {
		String cmd = cmdP.getCmd(userCmd);
		Command toExecute = cmdP.getCmdType(cmd);
		if (toExecute == null) {
			return null;
		}
		for (String requiredField : toExecute.getRequiredFields()) {
			String paramValue = extractParameter(requiredField, userCmd);
			if(paramValue == null){
				return null;
			}
			toExecute.setParameter(requiredField, paramValue);
		}

		for (String optionalField : toExecute.getOptionalFields()) {
			String paramValue = extractParameter(optionalField, userCmd);
			if(paramValue != null){
				toExecute.setParameter(optionalField, paramValue);
			}
		}

		return toExecute;
	}

	/**
	 * Analyses the full parameter string, and extracts the necessary info
	 * specified by paramName
	 * 
	 * @param paramName
	 *            The type of parameter to extract
	 * @param fullParam
	 *            The entire parameter string to analyse and extract the
	 *            parameter value from
	 * @return The extracted parameter value
	 */
	private String extractParameter(String paramName, String fullParam) {
		String paramValue = null;
		switch (paramName) {
		case CmdParameters.PARAM_NAME_TASK_NAME:
			paramValue = getTaskName(fullParam);
		case CmdParameters.PARAM_NAME_CMD_SEARCH:
		case CmdParameters.PARAM_NAME_TASK_ENDTIME:
		case CmdParameters.PARAM_NAME_TASK_STARTTIME:
		case CmdParameters.PARAM_NAME_TASK_PRIORITY:
		}

		return paramValue;
	}
}
