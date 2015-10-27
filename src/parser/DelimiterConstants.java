/**
 * Keeps a list of user input constants to be resolved by both LanguageProcessor and Command
 *
 * @author amoshydra
 */
package parser;

public class DelimiterConstants {
		public static final String INPUT_TASK_DELIMITER = "-";

		public static final String INPUT_TASK_SPECIFIER_STARTTIME = INPUT_TASK_DELIMITER + "sd";
		public static final String INPUT_TASK_SPECIFIER_ENDTIME = INPUT_TASK_DELIMITER + "ed";
		public static final String INPUT_TASK_SPECIFIER_PRIORITY = INPUT_TASK_DELIMITER + "p";

		public static final String INPUT_TASK_FILTER_ALL = INPUT_TASK_DELIMITER + "all";
		public static final String INPUT_TASK_FILTER_DONE = INPUT_TASK_DELIMITER + "done";
		public static final String INPUT_TASK_FILTER_FLOATING = INPUT_TASK_DELIMITER + "floating";

		public static final String INPUT_TASK_SETTING_NEWFILE = INPUT_TASK_DELIMITER + "nf";
		public static final String INPUT_TASK_SETTING_OPENFILE = INPUT_TASK_DELIMITER + "of";
}
