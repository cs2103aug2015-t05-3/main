//@@A0126394B
package logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

	// Log formatting head/tail constants
	private static final String HEADTAIL_SEPARATOR = "\n";
	private static final String HEADTAIL_PADDER = "********";
	private static final String HEAD_MSG = " Log start ";
	private static final String TAIL_MSG = "  Log end  ";


	// Log formatting body constants
	private static final String CLASS_OPEN = "[";
	private static final String CLASS_CLOSE = "]";
	private static final String CLASS_METHOD_DELIMETER = ".";
	private static final String LEVEL_OPEN = "[";
	private static final String LEVEL_CLOSE = "]";
	private static final String ATTRIBUTES_SEPARTOR = " - ";
	private static final String LOG_SEPARATOR = "\n";

	// Date format constant
	private static final String DATE_FORMAT_FULL = "yyyy.MM.dd hh:mm:ss.SSS";
	private static final DateFormat df = new SimpleDateFormat(DATE_FORMAT_FULL);

	@Override
	public String format(LogRecord record) {

		String dateStr = df.format(new Date(record.getMillis()));
		String classStr = record.getSourceClassName();
		String methodStr = record.getSourceMethodName();
		String levelStr = String.format("%-7s", record.getLevel().toString());
		String message = formatMessage(record);

		String logBody = dateStr + ATTRIBUTES_SEPARTOR +
				LEVEL_OPEN + levelStr + LEVEL_CLOSE + ATTRIBUTES_SEPARTOR +
				CLASS_OPEN + classStr + CLASS_METHOD_DELIMETER + methodStr + CLASS_CLOSE + ATTRIBUTES_SEPARTOR +
				message + LOG_SEPARATOR;
		return logBody;
	}

	public String getHead(Handler h) {
		String logHead = HEADTAIL_PADDER + HEAD_MSG + HEADTAIL_PADDER
				+ HEADTAIL_SEPARATOR;
		return logHead;
	}

	public String getTail(Handler h) {
		String logTail = HEADTAIL_PADDER + TAIL_MSG + HEADTAIL_PADDER +
				HEADTAIL_SEPARATOR;
		return logTail;
	}
}
