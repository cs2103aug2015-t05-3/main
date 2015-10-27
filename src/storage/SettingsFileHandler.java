package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class SettingsFileHandler {
	
	private String _fileName = "settings.cfg";
	private String _taskFileLocation;
	private File _settingsFile;
	private static SettingsFileHandler s;
	
	private SettingsFileHandler() {
		
	}
	
	public static SettingsFileHandler getInstance() {
		if(s == null){
			s = new SettingsFileHandler();
		}
		return s;
	}
	
	/*
	 * Returns true if settings file is found.
	 * Returns false otherwise.
	*/
	public boolean init() {
		_settingsFile = new File (_fileName);
		
		if (_settingsFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(_settingsFile));
				_taskFileLocation = br.readLine();
				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Returns true if settings file is created successfully.
	 * Generates error and returns false otherwise.
	 */
	public boolean alterSettingsFile(String taskFileLocation) {
		try {
			PrintWriter pw = new PrintWriter(_fileName);
			_taskFileLocation = taskFileLocation;
			pw.println(_taskFileLocation);
			pw.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * Returns true if empty tasks file is created successfully.
	 * Returns false otherwise.
	 */
	public boolean createTaskFile() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(_taskFileLocation, "UTF-8");
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			pw.println("<tasklist>");
			pw.println("</tasklist>");
			pw.close();
			return true;
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
			return false;
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported Encoding");
			return false;
		}	
	}
	
	/*
	 * Returns task file location if file is present.
	 * Returns null if absent.
	 */
	public String getTaskFileLocation() {
		File taskFile = new File (_taskFileLocation);
		
		if (!taskFile.exists()) {
			return null;
		} else {
			return _taskFileLocation;
		}
	}
	/*
	public static void main (String[] args) {
		SettingsFileHandler lol = new SettingsFileHandler();
		boolean exists = lol.init();
		System.out.println(exists);
		if (!exists) {
			lol.alterSettingsFile("D:\\Users\\Zander\\git\\main\\tasks11111.xml");
			System.out.println(lol.getTaskFileLocation());
			
		}
	}
	*/
}
