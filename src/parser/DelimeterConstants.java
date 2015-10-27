/**
 * Keeps a list of user input constants to be resolved by both LanguageProcessor and Command
 *
 * @author amoshydra
 */
package parser;

public class DelimeterConstants {
		public static final String INPUT_TASK_SPECIFIER_STARTTIME = "-sDate";
		public static final String INPUT_TASK_SPECIFIER_ENDTIME = "-eDate";
		public static final String INPUT_TASK_SPECIFIER_PRIORITY = "-p";

		public static final String INPUT_TASK_FILTER_ALL = "-all";
		public static final String INPUT_TASK_FILTER_DONE = "-done";
		public static final String INPUT_TASK_FILTER_FLOATING = "-floating";

		public static final String INPUT_TASK_SETTING_NEWFILE = "-nf";
		public static final String INPUT_TASK_SETTING_OPENFILE = "-of";
}
