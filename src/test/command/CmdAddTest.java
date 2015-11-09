//@@author A0125574A

/**
 * Testing of CmdAdd
 */

package test.command;

import java.util.ArrayList;
import java.util.List;

import constants.CmdParameters;

import logic.command.CmdAdd;
import logic.command.Command;
import logic.command.CommandAction;

import taskCollections.Task;
import taskCollections.Task.PRIORITY_TYPE;

public class CmdAddTest {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKNAMENOTGIVEN = "Please enter a task name";
	private static final String MSG_TASKADDED = "Added : %1$s";
	private static final String MSG_STARTAFTEREND = "Specified start time should be before end time";

	// Variable Constants
	private static final long INVALID_START_TIME = 1448722800000L; // 28Nov15
																	// 15:00
	private static final long VALID_START_TIME = 1448895600000L; // 30Nov15
																	// 15:00
	private static final long VALID_END_TIME = 1448780400000L; // 29Nov15
																	// 15:00
	private static final int NO_TIME = 0;
	private static final String EMPTY_STRING = "";
	private static final String TASK_NAME_1 = "task name 1";
	private static final String TASK_NAME_2 = "task name 2";

	/*
	 * Variables for internal use
	 */
	private Command _testCmdAdd;
	private CommandTestFunctions ctf;

	public CmdAddTest() {
		ctf = new CommandTestFunctions();
	}

	/**
	 * Run all available test cases for CmdAdd
	 */
	public void testCmdAdd() {
		testCmdAdd_taskName_field();
		testCmdAdd_time_field();
		testCmdAdd_priority_field();
	}

	/**
	 * Testing of CmdAdd task name field
	 */
	public void testCmdAdd_taskName_field() {
		// Initialize test variables
		CommandAction expectedCA;
		Task expectedTask;
		List<Task> expectedTaskList;

		/*
		 * Test 1: Testing task name field
		 */
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();

		// Test 1a: task name is null
		_testCmdAdd = new CmdAdd();
		setParameters(null, null, null, null);
		expectedCA = new CommandAction(MSG_TASKNAMENOTGIVEN, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 1b: task name is an empty String
		_testCmdAdd = new CmdAdd();
		setParameters(EMPTY_STRING, null, null, null);
		expectedCA = new CommandAction(MSG_TASKNAMENOTGIVEN, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 1c: task name is not empty
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, null, null, null);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, NO_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_1), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 1d: new task name added to an existing list
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_2, null, null, null);
		expectedTask = ctf.createExpectedTask(TASK_NAME_2, NO_TIME, NO_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_2), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

	}

	/**
	 * Testing of CmdAdd time field
	 */
	public void testCmdAdd_time_field() {
		// Initialize test variables
		CommandAction expectedCA;
		Task expectedTask;
		List<Task> expectedTaskList;

		/*
		 * Test 2: Testing start time and end time field
		 */

		// Test 2a: start time and end time are both null
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, null, null, null);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, NO_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_1), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 2b: start time is null, end time is valid
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, null, VALID_END_TIME + "", null);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, VALID_END_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_1), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 2c: start time is valid, end time is null
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, INVALID_START_TIME + "", null, null);
		expectedCA = new CommandAction(MSG_STARTAFTEREND, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 2d: start time is valid, end time is valid
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, INVALID_START_TIME + "", VALID_END_TIME + "", null);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, INVALID_START_TIME, VALID_END_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_1), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 2e: start time is valid, end time is valid, but start time is
		// before end time
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, VALID_START_TIME + "", VALID_END_TIME + "", null);
		expectedCA = new CommandAction(MSG_STARTAFTEREND, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

	}

	/**
	 * Testing of CmdAdd priority field
	 */
	public void testCmdAdd_priority_field() {
		// Initialize test variables
		CommandAction expectedCA;
		Task expectedTask;
		List<Task> expectedTaskList;

		/*
		 * Test 3: Testing priority field
		 */

		// Test 3a: priority is null
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, null, null, null);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, NO_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_1), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 3b: priority is HIGH
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, null, null, CmdParameters.PARAM_VALUE_TASK_PRIORITY_HIGH);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, NO_TIME, PRIORITY_TYPE.HIGH);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_1), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 3c: priority is NORMAL
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, null, null, CmdParameters.PARAM_VALUE_TASK_PRIORITY_NORM);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, NO_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_1), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

		// Test 3d: priority is LOW
		ctf.initialize();
		expectedTaskList = new ArrayList<Task>();
		_testCmdAdd = new CmdAdd();
		setParameters(TASK_NAME_1, null, null, CmdParameters.PARAM_VALUE_TASK_PRIORITY_LOW);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, NO_TIME, PRIORITY_TYPE.LOW);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKADDED, TASK_NAME_1), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdAdd.execute());

	}

	/**
	 * Set parameters for testing
	 * 
	 * @param taskName
	 *            task name to be set as parameter
	 * 
	 * @param startTime
	 *            start time to be set as parameter
	 * 
	 * @param endTime
	 *            end time to be set as parameter
	 * 
	 * @param priority
	 *            priority to be set as parameter
	 */
	public void setParameters(String taskName, String startTime, String endTime, String priority) {
		_testCmdAdd.setParameter(CmdParameters.PARAM_NAME_TASK_NAME, taskName);
		_testCmdAdd.setParameter(CmdParameters.PARAM_NAME_TASK_STARTTIME, startTime);
		_testCmdAdd.setParameter(CmdParameters.PARAM_NAME_TASK_ENDTIME, endTime);
		_testCmdAdd.setParameter(CmdParameters.PARAM_NAME_TASK_PRIORITY, priority);
	}

}
