//@@author A0125574A

/**
 * Methods for initializing test variables and asserting CommandAction
 */

package test.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import logic.command.Command;
import logic.command.CommandAction;

import taskCollections.Task;
import taskCollections.TaskTree;

import parser.TimeProcessor;

public class CommandTestFunctions {

	// Variable constants
	private static final String COMMAND_TEST_FILE = "src/test/command/CommandTest.xml";
	private static final String DEFAULT_FILE_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
			+ "\n<tasklist>\n</tasklist>\n";
	
	/*
	 * Variables for internal use
	 */
	private TimeProcessor tp;
	
	public CommandTestFunctions(){
		tp = TimeProcessor.getInstance();
	}
	
	/**
	 * Assert if contents of CommandAction from test results are equal to
	 * expected results.
	 *
	 * @param expectedCA
	 *            CommandAction of expected result
	 *
	 * @param testCA
	 *            CommandAction of test result
	 */
	public void assertCommandAction(CommandAction expectedCA, CommandAction testCA) {

		// Assert String outputMsg
		assertEquals(expectedCA.getOutput(), testCA.getOutput());

		// Assert boolean isUndoable
		if (expectedCA.isUndoable()) {
			assertTrue(testCA.isUndoable()); // assert true if expected is true
		} else {
			assertFalse(testCA.isUndoable());// assert false if expected is
												// false
		}

		// Assert List<Task> taskList
		assertEquals(expectedCA.getTaskList(), testCA.getTaskList());

	}

	/**
	 * Initialization of necessary variables for testing (i.e. TaskTree,
	 * Command.xml)
	 */
	public void initialize() {
		resetCommandXML();
		Task.resetTaskClassId();
		TaskTree.newTaskTree(COMMAND_TEST_FILE);
		Command.init();	
	}

	/**
	 * Reset Command.xml in a clean state (without any {@code Task})
	 */
	public void resetCommandXML() {
		try {
			PrintWriter pw = new PrintWriter(COMMAND_TEST_FILE);
			pw.write(DEFAULT_FILE_CONTENT);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates and returns an expected {@code Task}
	 *
	 * @param taskName
	 *            task name of expected {@code Task}
	 *
	 * @param startTime
	 *            start time of expected {@code Task}
	 * 
	 * @param endTime
	 *            end time of expected {@code Task}
	 * 
	 * @param priority
	 *            priority of expected {@code Task}
	 * 
	 * @return an expected {@code Task} created with given parameters.
	 */
	public Task createExpectedTask(String taskName, long startTime, long endTime, Task.PRIORITY_TYPE priority) {
		Task expectedTask = new Task(taskName, startTime, endTime, Task.FLAG_TYPE.NULL, priority);
		// To match with the appropriate task ID
		expectedTask.setId(expectedTask.getId() + 1);
		return expectedTask;
	}
	
	/**
	 * Utilize TimeProcessor to resolve time in String format to long
	 *
	 * @param time
	 *            time in String format
	 * 
	 * @return time in long format
	 */
	public long resolveTime(String time){
		return tp.resolveTime(time);
	}
}
