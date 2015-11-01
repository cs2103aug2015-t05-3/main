package parser;

import logic.command.*;
import util.StringUtil;

import java.util.logging.Logger;

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
	private static Logger parseLog;
	
	/*
	 * Constant
	 */
	private static final String LOG_FORMAT_RESULT = "%1$s: %2$s | %3$s"; // <User input>: <Parsed result/message> [Optional message]
	private static final String LOG_MSG_INVALIDCMD = "Invalid command";
	private static final String LOG_MSG_NOPARAM = "No parameters given";

	private String getTaskName(String userCmd) {
		String taskName = StringUtil.getStringAfter(userCmd, ParserConstants.TASK_SPECIFIER_TASKNAME, 
				ParserConstants.DELIMITER_TOKEN);
		if(taskName != null){
			return taskName;
		}
		taskName = StringUtil.getStringAfter(userCmd,"",ParserConstants.DELIMITER_TOKEN);
		return StringUtil.trim(taskName);
	}

	private String getTaskID(String userCmd){
		String id = StringUtil.getFirstNumber(userCmd);
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
		if(s == null){
			return null;
		}
		return s.replaceAll("(-\\S+)\\s?", "");
	}

	private String getEndTime(String userCmd){
		userCmd = StringUtil.getStringAfter(userCmd, ParserConstants.TASK_SPECIFIER_ENDTIME,
				ParserConstants.DELIMITER_TOKEN);
		return StringUtil.trim(userCmd);
	}

	private String getStartTime(String userCmd){
		userCmd = StringUtil.getStringAfter(userCmd, ParserConstants.TASK_SPECIFIER_STARTTIME,
				ParserConstants.DELIMITER_TOKEN);
		return StringUtil.trim(userCmd);
	}

	private String getPriority(String userCmd){
		String priority = StringUtil.getStringAfter(userCmd, ParserConstants.TASK_SPECIFIER_PRIORITY,
				ParserConstants.DELIMITER_TOKEN);
		if (priority == null){
			return null;
		}
		return resolvePriority(priority.trim());
	}
	
	private String resolvePriority(String priority){
		for(String aPriority : ParserConstants.TASK_PRIORITY_HIGH){
			if(aPriority.equalsIgnoreCase(priority)){
				return CmdParameters.PARAM_VALUE_TASK_PRIORITY_HIGH;
			}
		}
		for(String aPriority : ParserConstants.TASK_PRIORITY_NORM){
			if(aPriority.equalsIgnoreCase(priority)){
				return CmdParameters.PARAM_VALUE_TASK_PRIORITY_NORM;
			}
		}
		for(String aPriority : ParserConstants.TASK_PRIORITY_LOW){
			if(aPriority.equalsIgnoreCase(priority)){
				return CmdParameters.PARAM_VALUE_TASK_PRIORITY_LOW;
			}
		}
		return null;
	}

	private String getMarkOption(String userCmd){
		if(userCmd.contains(ParserConstants.TASK_MARK_UNMARK)){
			return CmdParameters.PARAM_VALUE_MARK_UNMARK;
		} else {
			return null;
		}
	}

	private String getListOption(String userCmd){
		if(userCmd.contains(ParserConstants.TASK_FILTER_ALL)){
			return CmdParameters.PARAM_VALUE_LIST_ALL;
		} else if (userCmd.contains(ParserConstants.TASK_FILTER_DONE)){
			return CmdParameters.PARAM_VALUE_LIST_DONE;
 		} else if (userCmd.contains(ParserConstants.TASK_SPECIFIER_PRIORITY)){
 			return CmdParameters.PARAM_VALUE_LIST_PRIORITY;
 		} else {
 			return null;
 		}
	}

	public boolean init(String cmdFileName){
		cmdP = CommandProcessor.getInstance();
		timeP = TimeProcessor.getInstance();
		parseLog = Logger.getLogger("parselog");
		return cmdP.initCmdList(cmdFileName);
	}

	public Command resolveCmd(String userCmd) {
		if(cmdP == null){ // If this is not initialised yet, do not allow any operations
			return null;
		}
		
		String cmd = cmdP.getCmd(userCmd); // Extract the command from input
		Command toExecute = cmdP.getCmdType(cmd); // Look up the command table
		
		if (toExecute == null) { // Not a valid command since look up in cmd table failed
			parseLog.info(String.format(LOG_FORMAT_RESULT, userCmd,"", LOG_MSG_INVALIDCMD));
			return null;
		}
		
		
		String param = StringUtil.removeFirstWord(userCmd);// Remove the command from the string, leaving just the parameters behind
		if(param == null){ // No parameters to process
			if(toExecute.getRequiredFields().length == 0){ // No required fields, valid cmd
				parseLog.info(String.format(LOG_FORMAT_RESULT,userCmd,"",LOG_MSG_NOPARAM));
				return toExecute;
			} else {
				return null;
			}
		}

		// Extract all fields which are declared in commands
		for (String requiredField : toExecute.getRequiredFields()) {
			String paramValue = extractParameter(requiredField, param);
			System.out.println(requiredField+": "+paramValue);
			if(paramValue == null){ // Invalid command since required fields arent available
				return null;
			}
			toExecute.setParameter(requiredField, paramValue);
		}
		for (String optionalField : toExecute.getOptionalFields()) {
			String paramValue = extractParameter(optionalField, param);
			System.out.println(optionalField+": "+paramValue);
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
		case CmdParameters.PARAM_NAME_MARK_FLAG:
			paramValue = getMarkOption(fullParam);
			break;
		case CmdParameters.PARAM_NAME_LIST_FLAG:
			paramValue = getListOption(fullParam);
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
