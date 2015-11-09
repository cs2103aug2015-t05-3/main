//@@author A0125574A

/**
 * Testing of CmdDelete
 */

package test.command;

import java.util.ArrayList;
import java.util.List;

import constants.CmdParameters;

import logic.command.CmdAdd;
import logic.command.CmdDelete;
import logic.command.Command;
import logic.command.CommandAction;

import taskCollections.Task;

public class CmdDeleteTest {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKDELETED = "Deleted : \"%1$s\"";

	// Variable constant
	private static final int VALID_TASKID = 0;
	private static final int INVALID_TASKID = -1;
	private static final String TASKNAME = "task name";

	/*
	 * Variables for internal use
	 */
	private Command _testCmdDelete;
	private CommandTestFunctions ctf;

	public CmdDeleteTest() {
		ctf = new CommandTestFunctions();
	}

	/**
	 * Run all available test cases for CmdAdd
	 */
	public void testCmdDelete() {
		testCmdDelete_ID_field();
	}

	/**
	 * Testing of CmdDelete task ID field
	 */
	public void testCmdDelete_ID_field() {
		// Initialize test variables
		CommandAction expectedCA;
		List<Task> expectedTaskList;

		/*
		 * Test 1: Testing ID field
		 */
		ctf.initialize();
		expectedTaskList = null;

		// Test 1a: ID is null
		_testCmdDelete = new CmdDelete();
		_testCmdDelete.setParameter(CmdParameters.PARAM_NAME_TASK_ID, null);
		expectedCA = new CommandAction(String.format(MSG_TASKIDNOTFOUND, INVALID_TASKID), false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdDelete.execute());

		// Test 1b: ID is invalid
		_testCmdDelete = new CmdDelete();
		_testCmdDelete.setParameter(CmdParameters.PARAM_NAME_TASK_ID, INVALID_TASKID + "");
		expectedCA = new CommandAction(String.format(MSG_TASKIDNOTFOUND, INVALID_TASKID), false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdDelete.execute());

		// Test 1c: ID is valid, but not in list
		_testCmdDelete = new CmdDelete();
		_testCmdDelete.setParameter(CmdParameters.PARAM_NAME_TASK_ID, VALID_TASKID + "");
		expectedCA = new CommandAction(String.format(MSG_TASKIDNOTFOUND, VALID_TASKID), false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdDelete.execute());

		// Test 1d: ID is valid and is in list
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		addNewTask();
		_testCmdDelete = new CmdDelete();
		_testCmdDelete.setParameter(CmdParameters.PARAM_NAME_TASK_ID, VALID_TASKID + "");
		expectedCA = new CommandAction(String.format(MSG_TASKDELETED, TASKNAME), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdDelete.execute());
	}

	/**
	 * add new task to Command.xml for testing
	 * 
	 */
	public void addNewTask() {
		Command cmdAdd = new CmdAdd();
		cmdAdd.setParameter(CmdParameters.PARAM_NAME_TASK_NAME, TASKNAME);
		cmdAdd.execute();
	}

}
