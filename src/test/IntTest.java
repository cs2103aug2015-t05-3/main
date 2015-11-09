
package test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import logic.command.CmdAdd;
import logic.command.Command;
import logic.command.CommandAction;
import parser.LanguageProcessor;
import taskCollections.Attributes;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import taskCollections.TaskTree;
import taskCollections.test.TaskTest;
import taskCollections.test.TaskTreeTest;
import test.command.CommandTest;

public class IntTest {
	/*
	 * Variables
	 */
	private LanguageProcessor lp;
	private ArrayList<Task> holdingTasks; // Entire task list
	private ArrayList<Task> filteredTasks; // To be used when testing for
											// filtered results / subset of
											// holding

	/*
	 * Constants
	 */
	private static final String CMD_FILENAME = "commands.xml";
	private static final String TASK_FILENAME = "testtask.xml";
	private static final String TASKOUT_FILENAME = "testtaskout.xml";
	private static final String MSG_INIT_CMDFAILED = "Test init cmd file failed";
	private static final String MSG_INVALIDCMD = "Please enter a valid command. For more info, enter help";

	// ADD
	private static String MSG_TASKADDED;
	private static String MSG_STARTAFTEREND;

	@Test
	public void test() throws Exception {
		init();

		testAddValid();
		assertEquals(getCheckSum(TASKOUT_FILENAME),getCheckSum(TASK_FILENAME));
		testInvalid();
		testUnits();
	}

	private void testInvalid() {
		assertTrue(testInput("add", MSG_INVALIDCMD, false, false));
		assertTrue(testInput("add ", MSG_INVALIDCMD, false, false));
		assertTrue(testInput("add addStartEndFail -sd 7pm -ed 6pm", MSG_STARTAFTEREND, true, false));
	}

	private void testAddValid() {
		holdingTasks.add(new Task("test", 0, 0, FLAG_TYPE.NULL, PRIORITY_TYPE.NORMAL));
		assertTrue(testInput("add test", String.format(MSG_TASKADDED, "test"), true, false));

		holdingTasks.add(new Task("priorityTest", 0, 0, FLAG_TYPE.NULL, PRIORITY_TYPE.HIGH));
		assertTrue(testInput("+ priorityTest -p h", String.format(MSG_TASKADDED, "priorityTest"), true, false));

		holdingTasks.add(new Task("time padding test", 0, 1447456800000L, FLAG_TYPE.NULL, PRIORITY_TYPE.NORMAL));
		assertTrue(testInput("+ time padding test -ed 14nov 720", String.format(MSG_TASKADDED, "time padding test"),
				true, false));

		holdingTasks.add(new Task("2date month p", 1450890000000L, 1450912200000L, FLAG_TYPE.NULL, PRIORITY_TYPE.LOW));
		assertTrue(testInput("add 2date month p -sd 24 dec 1am -ed 24 dec 710 -p l",
				String.format(MSG_TASKADDED, "2date month p"), true, false));

		holdingTasks.add(new Task("2date yr p", 1482516000000L, 1514128200000L, FLAG_TYPE.NULL, PRIORITY_TYPE.LOW));
		assertTrue(testInput("add 2date yr p -sd 24,dec16 2am -ed 24 dec17 23:10 -p l",
				String.format(MSG_TASKADDED, "2date yr p"), true, false));

		
	}
	
	private void testUnits() throws Exception {
		storageTest();
		parserTest();
		cmdTest();
		taskTest();
		taskTreeTest();
	}
	
	private void storageTest() throws Exception {
		StorageTest storageT = new StorageTest();
		StorageTest.beforeClass();
		storageT.testAcceptTaskFile();
		storageT.testArrayList();
		storageT.testAtAdd();
		storageT.testCommandFileLoading();
		storageT.testHashMap();
		storageT.testUpdateAndDelete();
	}
	
	private void parserTest(){
		ParserTest parseT = new ParserTest();
		parseT.test();
	}
	
	private void cmdTest(){
		CommandTest cmdT = new CommandTest();
		cmdT.testCommands();
	}
	
	private void taskTest(){
		TaskTest taskT = new TaskTest();
		
		taskT.testCompares();
		taskT.testEquals();
	}
	
	private void taskTreeTest(){
		TaskTreeTest taskTreeT = new TaskTreeTest();
		
		taskTreeT.testAddElementsToTree();
		taskTreeT.testAddIdenticalElementsToTree();
		taskTreeT.testInitialiseTaskTree();
		taskTreeT.testQueryTime();
		taskTreeT.testRemoveElementsFromTree();
		taskTreeT.testReplaceElementsFromTree();
	}

	/**
	 * Tests an input
	 * 
	 * @param input
	 *            The command to test
	 * @param output
	 *            The expected output message from executing the command
	 * @param isValidCmd
	 *            Denotes whether the input command is a valid command found in
	 *            the command table
	 * @param isFilteredOutput
	 *            True if checking is to be done on filteredTasks, false if
	 *            checking is done on holdingTasks
	 * @return True if the given output and task collection are correct. False
	 *         otherwise
	 */
	private boolean testInput(String input, String output, boolean isValidCmd, boolean isFilteredOutput) {

		Command toExecute = lp.resolveCmd(input);
		if (toExecute == null) {
			return !isValidCmd;
		}

		CommandAction result = toExecute.execute();
		if (result == null) { // A command must always give some form of result
			return false;
		}
		if (!output.equals(result.getOutput())) {
			System.out.println("Output mismatch: '" + output + "' expected, '" + result.getOutput() + "' given");
			return false;
		}
		if (isFilteredOutput) {
			return compareTask(filteredTasks, result.getTaskList());
		} else {
			return compareTask(holdingTasks, result.getTaskList());
		}

	}

	/**
	 * Compares 2 lists of tasks to check if they are equal
	 * 
	 * @param mainList
	 *            The expected output list
	 * @param checkList
	 *            The list which is produced from executing commands. Elements
	 *            will be removed from this list during the comparison test
	 * @return True if both lists are equal, false otherwise
	 */
	private boolean compareTask(List<Task> mainList, List<Task> checkList) {
		mainTask: for (Task mainTask : mainList) {
			checkTask: for (int i = 0; i < checkList.size(); i++) {// Look for differences in the 2 tasks
																	
				boolean[] checkDiff = checkList.get(i).getAttributesDiff(mainTask);
				for (int j = 0; j < checkDiff.length; j++) {
					if (j != Attributes.TYPE.ID.getValue() && !checkDiff[j]) {
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

	private void initConstants()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		CmdAdd add = new CmdAdd();
		Field f = add.getClass().getDeclaredField("MSG_TASKADDED");
		f.setAccessible(true);
		MSG_TASKADDED = (String) f.get(add);
		f = add.getClass().getDeclaredField("MSG_STARTAFTEREND");
		f.setAccessible(true);
		MSG_STARTAFTEREND = (String) f.get(add);
	}

	private void init() {
		try {
			initConstants();
		} catch (Exception e) {
			System.err.println("Unable to reflect constants: " + e);
			return;
		}
		holdingTasks = new ArrayList<Task>();
		filteredTasks = new ArrayList<Task>();
		lp = LanguageProcessor.getInstance();
		if (!lp.init(CMD_FILENAME)) {
			System.err.println(MSG_INIT_CMDFAILED);
		}
		createTaskFile(TASK_FILENAME);
		TaskTree.newTaskTree(TASK_FILENAME);
		Command.init();
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

	// @@author A0076510M-reused
	public String getCheckSum(String file) {

		StringBuffer sb1 = null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);

			byte[] dataBytes = new byte[1024];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			;
			byte[] mdbytes = md.digest();
			sb1 = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb1.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			fis.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb1.toString();
	}

}
