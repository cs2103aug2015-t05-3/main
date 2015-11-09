//@@author A0076510M

package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import logger.LogHandler;

/**
 * Provides methods for retrieving and modifying the file path of tasks file.
 * @author Zandercx
 */

public class SettingsFileHandler {

	private static final String EMPTY_STRING = "";
	private static final String FILE_PATH_TEXT = "FILE_PATH: ";
	
	private static final String EXCEPTION_FILENOTFOUND = "File Not Found Exception: %1$s";
	private static final String EXCEPTION_IO = "IO Exception: %1$s";
	private static final String EXCEPTION_UNSUPPORTEDENCODING = "Unsupported Encoding Exception: %1$s";
	
	private String _fileName = "settings.cfg";
	private String _taskFileLocation;
	private File _settingsFile;
	private static SettingsFileHandler s;

	private SettingsFileHandler() {

	}

	public static SettingsFileHandler getInstance() {
		if (s == null) {
			s = new SettingsFileHandler();
		}
		return s;
	}

	/**
	 * Retrieves location of tasks file if settings file is present.
	 * @return 
	 * 		true if settings file is found and location is proper. 
	 * 		false otherwise
	 */
	public boolean init() {
		_settingsFile = new File(_fileName);

		if (_settingsFile.exists()) {
			try {
				String input; 
				BufferedReader br = new BufferedReader(new FileReader(_settingsFile));
				input = br.readLine();
				if (input.contains(FILE_PATH_TEXT)) {
					input = input.replace(FILE_PATH_TEXT, EMPTY_STRING);
					_taskFileLocation = input;
					br.close();
					return true;
				} else {
					br.close();
					return false;
				}
			} catch (FileNotFoundException e) {
				LogHandler.getLog().log(Level.SEVERE, String.format(EXCEPTION_FILENOTFOUND, e));
				return false;
			} catch (IOException e) {
				LogHandler.getLog().log(Level.SEVERE, String.format(EXCEPTION_IO, e));
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Modify settings file to include task path
	 * @param 
	 * 		taskFileLocation file path of task file
	 * @return 
	 * 		true if succeeded, false if failed
	 */
	public boolean alterSettingsFile(String taskFileLocation) {
		assert taskFileLocation != null;
		assert !taskFileLocation.isEmpty();
		
		try {
			PrintWriter pw = new PrintWriter(_fileName);
			_taskFileLocation = taskFileLocation;
			pw.println(FILE_PATH_TEXT + _taskFileLocation);
			pw.close();
			return true;
		} catch (FileNotFoundException e) {
			LogHandler.getLog().log(Level.SEVERE, String.format(EXCEPTION_FILENOTFOUND, e));
			return false;
		}
	}

	/**
	 * Creates an empty task file
	 * @return 
	 * 		true if empty tasks file is created successfully. Returns false
	 * 		otherwise.
	 */
	public boolean initializeTaskFile() {
		if (taskFileCheck()) {
			return true;
		} else {
			PrintWriter pw;
			try {
				pw = new PrintWriter(_taskFileLocation, "UTF-8");
				pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
				pw.println("<tasklist>");
				pw.println("</tasklist>");
				pw.close();
				return true;
			} catch (FileNotFoundException e) {
				LogHandler.getLog().log(Level.SEVERE, String.format(EXCEPTION_FILENOTFOUND, e));
				return false;
			} catch (UnsupportedEncodingException e) {
				LogHandler.getLog().log(Level.SEVERE, String.format(EXCEPTION_UNSUPPORTEDENCODING, e));
				return false;
			}
		}
	}

	private boolean taskFileCheck() {
		File taskFile = new File(_taskFileLocation);
		return (taskFile.exists());
	}

	/**
	 * Get task file location
	 * @return 
	 * 		task file location if file is present. Returns null if absent.
	 */
	public String getTaskFile() {
		File taskFile = new File(_taskFileLocation);

		if (taskFile.exists()) {
			return _taskFileLocation;
		} else {
			return null;
		}
	}
}
