package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHandler {

	private static final String LOG_TAG = "log_dev";
	private static final String LOG_FILE_NAME = LOG_TAG + ".log";
	private static final String LOG_MESSAGE = "%s: %s";
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

	/**
	 * Provide consistent method to log across different classes and packages.
	 *
	 * @param level
	 *            One of the message level identifiers, e.g., SEVERE
	 * @param className
	 *            Where this method is being called from
	 * @param message
	 *            The string message (or a key in the message catalog)
	 *
	 */
	public static void log(Level level, String className, String message) {
		if (log == null) {
			initLog();
		}
		String logMsg = String.format(LOG_MESSAGE, className, message);
		log.log(level, logMsg);
	}

	private static void initLog() {
		log = Logger.getLogger(LOG_TAG);
		try {
			log.addHandler(new FileHandler(LOG_FILE_NAME, IS_APPENDING));
			log.setLevel(LOGGING_LEVEL);
		} catch (SecurityException | IOException e) {
			log.severe(String.format(LOG_MESSAGE, APP_NAME, e));
		}
	}
}
