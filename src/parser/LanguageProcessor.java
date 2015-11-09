package parser;

import logic.command.*;
import util.StringUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;

import javax.management.monitor.MonitorSettingException;

import constants.CmdParameters;
import logger.LogHandler;

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
	private static LanguageProcessor langP; // A copy of itself
	private static CommandProcessor cmdP;
	private static TimeProcessor timeP;
	private static Logger parseLog;

	/*
	 * Constant
	 */
	// <User input>: <Parsed parameters> [Optional message]
	private static final String LOG_FORMAT_RESULT = "%1$s: %2$s | %3$s"; 
	private static final String LOG_MSG_INVALIDCMD = "Invalid command";
	private static final String LOG_MSG_NOPARAM = "No parameters given";
	private static final String LOG_MSG_REQUIRED_NOTFOUND = "Required field not found";
	private static final String LOG_MSG_OPTIONAL_INVALID = "Invalid optional field";
	
	private static final String REGEX_LIMIT = ParserConstants.DELIMITER_TOKEN + "\\w+"; // The pattern to determine a flag
	private static final String FIELD_INVALID = ""; // Denotes a field is invalid

	private LanguageProcessor() {
	}

	/**
	 * Get an instance of itself
	 * 
	 * @return The instance of itself
	 */
	public static LanguageProcessor getInstance() {
		if (langP == null) {
			langP = new LanguageProcessor();
		}

		return langP;
	}

	/**
	 * Initialises all the parser sub components
	 * 
	 * @param cmdFileName
	 *            The command file to use for its alternative commands
	 * @return True if the command file is initialises successfully, false
	 *         otherwise
	 */
	public boolean init(String cmdFileName) {
		cmdP = CommandProcessor.getInstance();
		timeP = TimeProcessor.getInstance();
		parseLog = LogHandler.getLog();
		return cmdP.initCmdList(cmdFileName);
	}

	/**
	 * Extracts the task name from an input, which is the string in the
	 * parameters until the first flag
	 * 
	 * @param userCmd
	 *            The string to extract from
	 * @return The extracted task name
	 */
	private String getTaskName(String userCmd) {
		String taskName = StringUtil.getStringAfter(userCmd, "", REGEX_LIMIT);
		return StringUtil.trim(taskName);
	}

	/**
	 * Extracts the specified task name from an input, which is the string after
	 * the name flag to the next flag
	 * 
	 * @param userCmd
	 *            The string to extract from
	 * @return The extracted task name
	 */
	private String getTaskSName(String userCmd) {
		String taskName = StringUtil.getStringAfter(userCmd, ParserConstants.TASK_SPECIFIER_TASKNAME, REGEX_LIMIT);
		return StringUtil.trim(taskName);
	}

	/**
	 * Extracts the task ID from an input, which is the first word in the
	 * parameters
	 * 
	 * @param userCmd
	 *            The string to extract from
	 * @return The extracted task ID
	 */
	private String getTaskID(String userCmd) {
		String id = StringUtil.getFirstWord(userCmd);

		if (id != null) { // ID is found
			try {
				Integer.parseInt(id);
				return id;
			} catch (NumberFormatException e) {
				return FIELD_INVALID; // This field is invalid for sure
			}
		}
		return null; // Unable to find task ID
	}

	/**
	 * Extracts the search term from an input, which is the entire parameter
	 * 
	 * @param userCmd
	 *            The string to extract from
	 * @return The extracted search term
	 */
	private String getSearchTerm(String userCmd) {
		return userCmd;
	}

	/**
	 * Extracts the end time from an input, which is the string after end time
	 * flag to the next flag
	 * 
	 * @param userCmd
	 *            The string to extract from
	 * @return The extracted end time
	 */
	private String getEndTime(String userCmd) {
		userCmd = StringUtil.getStringAfter(userCmd, ParserConstants.TASK_SPECIFIER_ENDTIME, REGEX_LIMIT);
		return StringUtil.trim(userCmd);
	}

	/**
	 * Extracts the start time from an input, which is the string after start
	 * time flag to the next flag
	 * 
	 * @param userCmd
	 *            The string to extract from
	 * @return The extracted start time
	 */
	private String getStartTime(String userCmd) {
		userCmd = StringUtil.getStringAfter(userCmd, ParserConstants.TASK_SPECIFIER_STARTTIME, REGEX_LIMIT);
		return StringUtil.trim(userCmd);
	}

	/**
	 * Extracts the priority from an input, which is the string after priority flag
	 * to the next flag
	 * @param userCmd
	 * 			The string to extract from
	 * @return
	 * 			The extracted priority
	 */
	private String getPriority(String userCmd) {
		String priority = StringUtil.getStringAfter(userCmd, ParserConstants.TASK_SPECIFIER_PRIORITY, REGEX_LIMIT);
		if (priority == null) {
			return null;
		}
		return resolvePriority(priority.trim());
	}

	/**
	 * Resolve a priority specifier (high/norm/low) to its respective value in parameters
	 * @param priority
	 * 			The priority to resolve
	 * @return
	 * 			The resolved priority
	 */
	private String resolvePriority(String priority) {
		// High priority
		for (String aPriority : ParserConstants.TASK_PRIORITY_HIGH) {
			if (aPriority.equalsIgnoreCase(priority)) {
				return CmdParameters.PARAM_VALUE_TASK_PRIORITY_HIGH;
			}
		}
		// Norm priority
		for (String aPriority : ParserConstants.TASK_PRIORITY_NORM) {
			if (aPriority.equalsIgnoreCase(priority)) {
				return CmdParameters.PARAM_VALUE_TASK_PRIORITY_NORM;
			}
		}
		// Low priority
		for (String aPriority : ParserConstants.TASK_PRIORITY_LOW) {
			if (aPriority.equalsIgnoreCase(priority)) {
				return CmdParameters.PARAM_VALUE_TASK_PRIORITY_LOW;
			}
		}
		return FIELD_INVALID; // Unable to resolve priority
	}

	/**
	 * Get the unmark option if it is available
	 * @param userCmd
	 * 			The string to analyse
	 * @return  Returns the unmark flag if it is available, null otherwise
	 */
	private String getMarkOption(String userCmd) {
		if (userCmd.contains(ParserConstants.TASK_MARK_UNMARK)) {
			return CmdParameters.PARAM_VALUE_MARK_UNMARK;
		} else {
			return null;
		}
	}

	/**
	 * Get the list flags
	 * @param userCmd
	 * 			The string to analyse
	 * @return Returns the list flag if it is found, null otherwise
	 */
	private String getListOption(String userCmd) {
		String flag = StringUtil.getFirstWord(userCmd);

		if (flag == null || flag.isEmpty()) {
			return null;
		} else if (StringUtil.getStringAfter(userCmd, flag, "") != null) {
			return FIELD_INVALID;
		} else if (flag.equals(ParserConstants.TASK_FILTER_ALL)) {
			return CmdParameters.PARAM_VALUE_LIST_ALL;
		} else if (flag.equals(ParserConstants.TASK_FILTER_DONE)) {
			return CmdParameters.PARAM_VALUE_LIST_DONE;
		} else if (flag.equals(ParserConstants.TASK_SPECIFIER_PRIORITY)) {
			return CmdParameters.PARAM_VALUE_LIST_PRIORITY;
		} else {
			return FIELD_INVALID;
		}
	}

	/**
	 * Resolves a user input command to a command instance
	 * @param userCmd
	 * 			The string to resolve
	 * @return
	 * 			The command instance
	 */
	public Command resolveCmd(String userCmd) {
		if (cmdP == null) { // If this is not initialised yet, do not allow any operations
			return null;
		}

		String cmd = cmdP.getCmd(userCmd); // Extract the command from input
		Command toExecute = cmdP.getCmdType(cmd); // Look up the command table

		if (toExecute == null) { // Not a valid command since look up in cmd table failed
			parseLog.info(String.format(LOG_FORMAT_RESULT, userCmd, "", LOG_MSG_INVALIDCMD));
			return null;
		}

		// Remove the command from the string, leaving just the parameters behind
		String param = StringUtil.removeFirstWord(userCmd);
		
		if (param == null) { // No parameters to process
			if (toExecute.getRequiredFields().length == 0) { // No required fields, valid cmd
				parseLog.info(String.format(LOG_FORMAT_RESULT, userCmd, "", LOG_MSG_NOPARAM));
				return toExecute;
			} else { // Invalid command since the required fields are not fullfilled
				return null;
			}
		}

		// Checks whether the flags given by the user is valid
		if (!hasValidFlags(toExecute.getOptionalFields(), userCmd)) {
			return null;
		}
		
		StringBuilder parameters = new StringBuilder(); // For logging purposes

		// Extract all required fields which are declared in commands
		for (String requiredField : toExecute.getRequiredFields()) {
			
			String paramValue = extractParameter(requiredField, param);
			parameters.append(requiredField + ": " + paramValue + "\\t");
			
			// Invalid command since the required fields aren't there
			if (paramValue == null || paramValue.equals(FIELD_INVALID)) {
				parseLog.info(String.format(LOG_FORMAT_RESULT, userCmd, 
						parameters, LOG_MSG_REQUIRED_NOTFOUND));
				return null;
			}
			// Set the parameter for this field
			toExecute.setParameter(requiredField, paramValue);
		}
		// Extract all optional fields which are declared in commands
		for (String optionalField : toExecute.getOptionalFields()) {
			
			String paramValue = extractParameter(optionalField, param);
			parameters.append(optionalField + ": " + paramValue + "\\t");
			
			if (paramValue != null) { // User entered something for this field
				
				// Invalid command since a field is misused
				if (paramValue.equals(FIELD_INVALID)) {
					parseLog.info(String.format(LOG_FORMAT_RESULT, userCmd,
							parameters, LOG_MSG_OPTIONAL_INVALID));
					return null;
				}
				// Set the parameter for this field
				toExecute.setParameter(optionalField, paramValue);
			}
		}
		return toExecute;
	}

	/**
	 * Analyses the full parameter string, and extracts the necessary info
	 * specified by paramName. Does validation and conversion of parameters as
	 * well
	 *
	 * @param paramName
	 *            The type of parameter to extract
	 * @param fullParam
	 *            The entire parameter string to analyse and extract the
	 *            parameter value from
	 * @return The extracted parameter value, or null if extraction is not
	 *         successful
	 */
	private String extractParameter(String paramName, String fullParam) {
		String paramValue = null;
		switch (paramName) {
			case CmdParameters.PARAM_NAME_TASK_NAME:
				paramValue = getTaskName(fullParam);
				break;
			case CmdParameters.PARAM_NAME_TASK_SNAME:
				paramValue = getTaskSName(fullParam);
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

	/**
	 * Resolves a user formatted date to a date in system time
	 * 
	 * @param timeS
	 *            The user date to resolve
	 * @return The system time in string
	 */
	private String resolveTime(String timeS) {
		if (timeS == null) {
			return null;
		}
		
		long time = timeP.resolveTime(timeS); // Resolve the time
		if (time == TimeProcessor.TIME_INVALIDFORMAT) { // Unable to resolve time
			return FIELD_INVALID;
		}
		return Long.toString(time); // Convert the time to String
	}

	/**
	 * Returns the list of flags supported for a field
	 * 
	 * @param field
	 *            The field to find
	 * @return The list of flags for that field
	 */
	private String[] getFlagForField(String field) {

		switch (field) {
			case CmdParameters.PARAM_NAME_TASK_SNAME:
				return new String[] { ParserConstants.TASK_SPECIFIER_TASKNAME };
			case CmdParameters.PARAM_NAME_TASK_ENDTIME:
				return new String[] { ParserConstants.TASK_SPECIFIER_ENDTIME };
			case CmdParameters.PARAM_NAME_TASK_STARTTIME:
				return new String[] { ParserConstants.TASK_SPECIFIER_STARTTIME };
			case CmdParameters.PARAM_NAME_TASK_PRIORITY:
				return new String[] { ParserConstants.TASK_SPECIFIER_PRIORITY };
			case CmdParameters.PARAM_NAME_LIST_FLAG:
				return new String[] { ParserConstants.TASK_FILTER_ALL, ParserConstants.TASK_FILTER_DONE };
			case CmdParameters.PARAM_NAME_MARK_FLAG:
				return new String[] { ParserConstants.TASK_MARK_UNMARK };
		}

		return null;
	}

	/**
	 * Checks whether the flags entered by the user is valid
	 * 
	 * @param fields
	 *            The fields of the command
	 * @param userCmd
	 *            The user input to check
	 * @return True if the flags are valid, false otherwise
	 */
	private boolean hasValidFlags(String[] fields, String userCmd) {
		String[] userFlags = StringUtil.getOccurrences(userCmd, "(-\\S+)\\s?"); // Get all the flags
		HashSet<String> cmdFlags = new HashSet<>();

		// Compile the list of supported flags for that command
		for (String aField : fields) {
			cmdFlags.addAll(Arrays.asList(getFlagForField(aField)));
		}

		// Check whether the flag entered by user is supported by that command
		for (String aFlag : userFlags) {
			if (!cmdFlags.contains(aFlag)) { // An unsupported flag is found
				return false;
			}
		}

		return true;
	}

}
