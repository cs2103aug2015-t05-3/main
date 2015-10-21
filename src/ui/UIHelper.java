/**
 * Provides methods for classes to interact with UIFrame
 * 
 * @author Yan Chan Min Oo
 */
package ui;


import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import taskCollections.Task;


public class UIHelper {
	
	/*
	private static UIFrame frame;
	private static Logger log = Logger.getLogger("log");
	*/
	
	//private static UI user_interface;
	
	/**
	 * Returns a string of the user input. Triggered on enter press.
	 */
	
	public static String[] getUserInput(){
		return UIController.getInput();
	}
	
	
	public static void loadTask() {
		
	}
	
	public static void createUI() {
		UIController.createUI();
	}
	
	
	
	/**
	 * Destroys the UI
	 */
	
	public static void destroyUI(){
		//frame.dispose();
	}
	
	public static void appendOutput(String appendString){
		//frame.setOutputText(frame.getOutputText() + appendString + System.lineSeparator());
	}
	
	//give the entire list for output generation
	public static void setTableOutput(List<Task> completeList){
		UIController.seperateTaskList(completeList);
	}
	
	//set UI's output msg
	public static void setOutputMsg(String s) {
		JOptionPane.showMessageDialog(null, s);
		UIController.setOutputMsg(s);
	}
	
	
	public static void setUserInput(String in){
		//frame.setInputText(in);
	}
	
}
