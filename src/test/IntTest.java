package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import logic.command.Command;
import logic.command.CommandAction;
import parser.LanguageProcessor;
import taskCollections.Attributes;
import taskCollections.Task;
import taskCollections.Task.*;

public class IntTest {
	/*
	 * Variables
	 */
	private LanguageProcessor lp;
	private ArrayList<Task> holdingTasks; // Entire task list
	private ArrayList<Task> filteredTasks; // To be used when testing for filtered results / subset of holding

	/*
	 * Constants
	 */
	private static final String CMD_FILENAME = "commands.xml";
	private static final String TASK_FILENAME = "testtask.xml";
	private static final String MSG_INIT_CMDFAILED = "Test init cmd file failed";
	private static final String MSG_INVALIDCMD = "Please enter a valid command. For more info, enter help";
	
	private static final String MSG_TASKADDED = "Added : %1$s";

	@Test
	public void test() {
		init();
		
		holdingTasks.add(new Task("test",0,0,FLAG_TYPE.NULL,PRIORITY_TYPE.NORMAL));
		assertTrue(testInput("add test", String.format(MSG_TASKADDED, "test"), true, false));
		holdingTasks.add(new Task("priorityTest",0,0,FLAG_TYPE.NULL,PRIORITY_TYPE.HIGH));
		assertTrue(testInput("+ priorityTest -p h", String.format(MSG_TASKADDED, "priorityTest"), true, false));
	}

	/**
	 * Tests an input
	 * @param input
	 * 			The command to test
	 * @param output
	 * 			The expected output message from executing the command
	 * @param isValidCmd
	 * 			Denotes whether the input command is a valid command found in the command table
	 * @param isFilteredOutput
	 * 			True if checking is to be done on filteredTasks, false if checking is done on holdingTasks
	 * @return
	 * 			True if the given output and task collection are correct. False otherwise
	 */
	private boolean testInput(String input, String output, boolean isValidCmd, boolean isFilteredOutput){
		
		Command toExecute = lp.resolveCmd(input);
		if(toExecute == null){
			return !isValidCmd;
		}
		
		CommandAction result = toExecute.execute();
		if(result == null){ // A command must always give some form of result
			return false;
		}
		if(!output.equals(result.getOutput())){
			System.out.println("Output mismatch: '"+output+"' expected, '"+result.getOutput()+"' given");
			return false;
		}
		if(isFilteredOutput){
			return compareTask(filteredTasks, result.getTaskList());
		} else {
			return compareTask(holdingTasks, result.getTaskList());
		}

	}
	
	/**
	 * Compares 2 lists of tasks to check if they are equal
	 * @param mainList
	 * 			The expected output list
	 * @param checkList
	 * 			The list which is produced from executing commands. Elements will be removed from this list
	 * 			during the comparison test
	 * @return
	 * 			True if both lists are equal, false otherwise
	 */
	private boolean compareTask(List<Task> mainList, List<Task> checkList){
		mainTask:
		for(Task mainTask : mainList){
			checkTask:
			for(int i = 0; i < checkList.size(); i++){// Look for differences in the 2 tasks
				boolean[] checkDiff = checkList.get(i).getAttributesDiff(mainTask);
				for(int j = 0; j < checkDiff.length; j++){
					if(j != Attributes.TYPE.ID.getValue() && !checkDiff[j]){
						continue checkTask; // Not the task we're looking for
					}
				}
				// Correct task
				checkList.remove(i); // Remove the task to reduce processing
				continue mainTask; // Move on to the next main task
			}
			return false; // Exhausted the list of check list
		}
		return checkList.size() == 0;
	}

	private void init(){
		holdingTasks = new ArrayList<Task>();
		filteredTasks = new ArrayList<Task>();
		lp = new LanguageProcessor();
		if(!lp.init(CMD_FILENAME)){
			System.err.println(MSG_INIT_CMDFAILED);
		}
		createTaskFile(TASK_FILENAME);
		Command.init(TASK_FILENAME);
	}

	private void createTaskFile(String fileName) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(fileName, "UTF-8");
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			pw.println("<tasklist>");
			pw.println("</tasklist>");
			pw.close();
			return;
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
			return;
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported Encoding");
			return;
		}
	}

}
