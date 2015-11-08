package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHandler {

	// Logger String Constants
	public static final String LOG_ENTRY = "ENTRY";
	public static final String LOG_EXIT = "RETURN";

	// Logger file format
	private static final String LOG_TAG = "log_dev";
	private static final String LOG_FILE_NAME = LOG_TAG + ".log";
	private static final String LOG_MESSAGE_FORMAT = "%s: %s";
	private static final String APP_NAME = "TaskBuddy";

	// Logger setting
	private static final Level LOGGING_LEVEL = Level.ALL;
	private static final boolean IS_APPENDING = true;

	private static Logger log;

	/**
	 * Return a consistent {@code log} object across all classes. If this is the
	 * first time {@code getLog()} is called, a logger will be constructed and
	 * returned. Otherwise, it will return an existing {@code logger}.
	 *
	 * @return log object
	 */
	public static Logger getLog() {
		if (log == null) {
			initLog();
		}
		return log;
	}

	private static void initLog() {
		log = Logger.getLogger(LOG_TAG);
		log.setLevel(LOGGING_LEVEL);

		try {
			FileHandler logFile = new FileHandler(LOG_FILE_NAME, IS_APPENDING);
			LogFormatter formatter = new LogFormatter();
			logFile.setFormatter(formatter);
			log.addHandler(logFile);
		} catch (IOException | SecurityException e) {
			log.severe(String.format(LOG_MESSAGE_FORMAT, APP_NAME, e));
		}
	}
}
