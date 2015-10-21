package parser;

import logic.command.*;
import constants.CmdParameters;
import constants.UIFieldIndex;

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
	//private static LanguageProcessor langProcessor;
	private static CommandProcessor cmdP;
	private static TimeProcessor timeP;

	/*private String getTaskName(String userCmd) {
		userCmd = StringUtil.removeFirstWord(userCmd);
		return userCmd;
	}
	
	private String getSearchTerm(String userCmd){
		return StringUtil.removeFirstWord(userCmd);
	}*/
	
	public boolean init(String cmdFileName){
		cmdP = CommandProcessor.getInstance();
		timeP = TimeProcessor.getInstance();
		return cmdP.initCmdList(cmdFileName);
	}
	
	public Command resolveCmd(String[] userCmd) {
		if(cmdP == null){ // Not initialised yet
			return null;
		}
		String cmd = cmdP.getCmd(userCmd[UIFieldIndex.INPUT_CMD]);
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
	 * specified by paramName. Does validation and conversion of parameters as well
	 * 
	 * @param paramName
	 *            The type of parameter to extract
	 * @param fullParam
	 *            The entire parameter string to analyse and extract the
	 *            parameter value from
	 * @return The extracted parameter value, or null if extraction is not successful
	 */
	private String extractParameter(String paramName, String[] fullParam) {
		String paramValue = null;
		switch (paramName) {
		case CmdParameters.PARAM_NAME_TASK_NAME:
			paramValue = fullParam[UIFieldIndex.INPUT_TASKNAME];
			break;
		case CmdParameters.PARAM_NAME_TASK_ID:
			paramValue = isID(fullParam[UIFieldIndex.INPUT_TASKID]) ?
					fullParam[UIFieldIndex.INPUT_TASKID] : null;
			break;
		case CmdParameters.PARAM_NAME_CMD_SEARCH:
			paramValue = fullParam[UIFieldIndex.INPUT_SEARCH];//TODO: Split into id or task name
			break;
		case CmdParameters.PARAM_NAME_TASK_ENDTIME:
			long time = timeP.resolveTime(fullParam[UIFieldIndex.INPUT_ENDTIME]);
			paramValue = (time == TimeProcessor.TIME_INVALID) ? null : Long.toString(time);
			break;
		case CmdParameters.PARAM_NAME_TASK_STARTTIME:
			time = timeP.resolveTime(fullParam[UIFieldIndex.INPUT_STARTTIME]);
			paramValue = (time == TimeProcessor.TIME_INVALID) ? null : Long.toString(time);
			break;
		case CmdParameters.PARAM_NAME_TASK_PRIORITY:
			paramValue = fullParam[UIFieldIndex.INPUT_PRIORITY];
			break;
		}

		return paramValue;
	}
	
	private boolean isID(String id){
		try{
			Integer.parseInt(id);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}
}
