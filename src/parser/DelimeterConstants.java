/**
 * Keeps a list of user input constants to be resolved by both LanguageProcessor and Command
 *
 * @author amoshydra
 */
package parser;

public class DelimeterConstants {
		public static final String INPUT_TASK_DELIMTER = "-";

		public static final String INPUT_TASK_SPECIFIER_STARTTIME = INPUT_TASK_DELIMTER + "sDate";
		public static final String INPUT_TASK_SPECIFIER_ENDTIME = INPUT_TASK_DELIMTER + "eDate";
		public static final String INPUT_TASK_SPECIFIER_PRIORITY = INPUT_TASK_DELIMTER + "p";

		public static final String INPUT_TASK_FILTER_ALL = INPUT_TASK_DELIMTER + "all";
		public static final String INPUT_TASK_FILTER_DONE = INPUT_TASK_DELIMTER + "done";
		public static final String INPUT_TASK_FILTER_FLOATING = INPUT_TASK_DELIMTER + "floating";

		public static final String INPUT_TASK_SETTING_NEWFILE = INPUT_TASK_DELIMTER + "nf";
		public static final String INPUT_TASK_SETTING_OPENFILE = INPUT_TASK_DELIMTER + "of";
}
