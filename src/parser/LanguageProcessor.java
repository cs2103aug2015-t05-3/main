package parser;

import logic.command.*;
import util.StringUtil;
import constants.CmdParameters;

/**
 * Translates and breaks natural language down for computation
 * 
 * @author Yan Chan Min Oo
 *
 */

public class LanguageProcessor{
	/*
	 * Variables
	 */
	//private static LanguageProcessor langProcessor;
	private static CommandProcessor cmdP;
	private static TimeProcessor timeP;

	private String getTaskName(String userCmd) {
		userCmd = StringUtil.getStringAfter(userCmd,"",DelimiterConstants.INPUT_TASK_DELIMITER);
		return StringUtil.trim(userCmd);
	}
	
	private String getTaskID(String userCmd){
		String id = StringUtil.removeFirstWord(userCmd);
		if(id != null){
			try{
				Integer.parseInt(id);
				return id;
			} catch (NumberFormatException e){
				return null;
			}
		}
		return null;
	}
	
	private String getSearchTerm(String userCmd){
		return removeDelimiters(userCmd);
	}
	
	private String removeDelimiters(String s){
		return s;
	}
	
	private String getEndTime(String userCmd){
		userCmd = StringUtil.getStringAfter(userCmd, DelimiterConstants.INPUT_TASK_SPECIFIER_ENDTIME, 
				DelimiterConstants.INPUT_TASK_DELIMITER);
		return StringUtil.trim(userCmd);
	}
	
	private String getStartTime(String userCmd){
		userCmd = StringUtil.getStringAfter(userCmd, DelimiterConstants.INPUT_TASK_SPECIFIER_STARTTIME, 
				DelimiterConstants.INPUT_TASK_DELIMITER);
		return StringUtil.trim(userCmd);
	}
	
	private String getPriority(String userCmd){
		userCmd = StringUtil.getStringAfter(userCmd, DelimiterConstants.INPUT_TASK_SPECIFIER_PRIORITY, 
				DelimiterConstants.INPUT_TASK_DELIMITER);
		return StringUtil.trim(userCmd);
	}
	
	public boolean init(String cmdFileName){
		cmdP = CommandProcessor.getInstance();
		timeP = TimeProcessor.getInstance();
		return cmdP.initCmdList(cmdFileName);
	}
	
	public Command resolveCmd(String userCmd) {
		if(cmdP == null){ // If this is not initialised yet, do not allow any operations
			return null;
		}
		String cmd = cmdP.getCmd(userCmd);
		Command toExecute = cmdP.getCmdType(cmd);
		if (toExecute == null) {
			return null;
		}
		// Remove the command from the string
		userCmd = StringUtil.removeFirstWord(userCmd);
		
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
	 * specified by paramName. Does validation and conversion of parameters as well
	 * 
	 * @param paramName
	 *            The type of parameter to extract
	 * @param fullParam
	 *            The entire parameter string to analyse and extract the
	 *            parameter value from
	 * @return The extracted parameter value, or null if extraction is not successful
	 */
	private String extractParameter(String paramName, String fullParam) {
		String paramValue = null;
		switch (paramName) {
		case CmdParameters.PARAM_NAME_TASK_NAME:
			paramValue = getTaskName(fullParam);
			break;
		case CmdParameters.PARAM_NAME_TASK_ID:
			paramValue = getTaskID(fullParam);
			break;
		case CmdParameters.PARAM_NAME_CMD_SEARCH:
			paramValue = getSearchTerm(fullParam);
			break;
		case CmdParameters.PARAM_NAME_TASK_ENDTIME:
			paramValue = resolveTime(getEndTime(fullParam));
			break;
		case CmdParameters.PARAM_NAME_TASK_STARTTIME:
			paramValue = resolveTime(getStartTime(fullParam));
			break;
		case CmdParameters.PARAM_NAME_TASK_PRIORITY:
			paramValue = getPriority(fullParam);
			break;
		}

		return paramValue;
	}
	
	private String resolveTime(String timeS){
		if(timeS == null){
			return null;
		}
		long time = timeP.resolveTime(timeS);
		return time == TimeProcessor.TIME_INVALID ? null : Long.toString(time);
	}
	/*
	private boolean isID(String id){
		try{
			Integer.parseInt(id);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}*/
}
