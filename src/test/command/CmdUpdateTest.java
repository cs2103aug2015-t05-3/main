//@@author A0125574A

/**
 * Testing of CmdUpdate
 */

package test.command;

import java.util.ArrayList;
import java.util.List;

import constants.CmdParameters;

import logic.command.CmdAdd;
import logic.command.CmdUpdate;
import logic.command.Command;
import logic.command.CommandAction;

import taskCollections.Task;
import taskCollections.Task.PRIORITY_TYPE;

public class CmdUpdateTest {

	/*
	 * Constants
	 */
	// Message constants
	private static final String MSG_TASKIDNOTFOUND = "Specified taskID \"%1$s\" not found";
	private static final String MSG_TASKNOUPDATE = "No update was made";
	private static final String MSG_TASKUPDATED = "Updated ID: \"%1$s\"";
	private static final String MSG_INVALIDTIME = "Invalid start/end time given";

	// Variable constant
	// 28Nov15 15:00
	private static final long VALID_START_TIME = 1448722800000L;
	// 30Nov15 15:00
	private static final long INVALID_START_TIME = 1448895600000L;
	// 29Nov15 15:00
	private static final long VALID_END_TIME = 1448780400000L;
	private static final int NO_TIME = 0;

	private static final int VALID_TASKID = 0;
	private static final int INVALID_TASKID = -1;
	private static final String EMPTY_STRING = "";
	private static final String TASK_NAME_1 = "task name 1";
	private static final String TASK_NAME_2 = "task name 2";

	/*
	 * Variables for internal use
	 */
	private Command _testCmdUpdate;
	private CommandTestFunctions ctf;

	public CmdUpdateTest() {
		ctf = new CommandTestFunctions();
	}

	/**
	 * Run all available test cases for CmdAdd
	 */
	public void testCmdUpdate() {
		testCmdUpdate_taskID_field();
		testCmdUpdate_taskName_field();
		testCmdUpdate_time_field();
		testCmdUpdate_priority_field();
	}

	/**
	 * Testing of CmdAdd task ID field
	 */
	public void testCmdUpdate_taskID_field() {
		// Initialize test variables
		CommandAction expectedCA;
		List<Task> expectedTaskList;

		/*
		 * Test 1: Testing ID field
		 */
		ctf.initialize();
		expectedTaskList = null;

		// Test 1a: ID is null
		_testCmdUpdate = new CmdUpdate();
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_ID, null);
		expectedCA = new CommandAction(String.format(MSG_TASKIDNOTFOUND, INVALID_TASKID), false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 1b: ID is invalid
		_testCmdUpdate = new CmdUpdate();
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_ID, INVALID_TASKID + "");
		expectedCA = new CommandAction(String.format(MSG_TASKIDNOTFOUND, INVALID_TASKID), false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 1c: ID is valid, but not in list
		_testCmdUpdate = new CmdUpdate();
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_ID, VALID_TASKID + "");
		expectedCA = new CommandAction(String.format(MSG_TASKIDNOTFOUND, VALID_TASKID), false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 1d: ID is valid and is in list, but not update changes
		addNewTask(TASK_NAME_1);
		_testCmdUpdate = new CmdUpdate();
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_ID, VALID_TASKID + "");
		expectedCA = new CommandAction(MSG_TASKNOUPDATE, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

	}

	/**
	 * Testing of CmdUpdate task name field
	 */
	public void testCmdUpdate_taskName_field() {
		// Initialize test variables
		CommandAction expectedCA;
		Task expectedTask;
		List<Task> expectedTaskList;

		/*
		 * Test 2: Testing task name field (all ID are valid)
		 */
		ctf.initialize();
		expectedTaskList = null;
		addNewTask(TASK_NAME_1);

		// Test 2a: task name is null
		_testCmdUpdate = new CmdUpdate();
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_ID, VALID_TASKID + "");
		expectedCA = new CommandAction(MSG_TASKNOUPDATE, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 2b: task name is an empty String
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", EMPTY_STRING, null, null, null);
		expectedCA = new CommandAction(MSG_TASKNOUPDATE, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 2c: new task name is same as current task name (no changes)
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, null, null, null);
		expectedCA = new CommandAction(MSG_TASKNOUPDATE, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 2d: new task name is different from current task name (changes
		// made)
		expectedTaskList = new ArrayList<Task>();
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_2, null, null, null);
		expectedTask = ctf.createExpectedTask(TASK_NAME_2, NO_TIME, NO_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKUPDATED, VALID_TASKID), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

	}

	/**
	 * Testing of CmdUpdate time field
	 */
	public void testCmdUpdate_time_field() {
		// Initialize test variables
		CommandAction expectedCA;
		Task expectedTask;
		List<Task> expectedTaskList;

		/*
		 * Test 3: Testing time field (all ID are valid)
		 */
		ctf.initialize();
		expectedTaskList = null;
		addNewTask(TASK_NAME_1);

		// Test 3a: start time and end time are both null
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, null, null, null);
		expectedCA = new CommandAction(MSG_TASKNOUPDATE, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 3b: valid start time without end time
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, VALID_START_TIME + "", null, null);
		expectedCA = new CommandAction(MSG_INVALIDTIME, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 3c: start time after end time
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, INVALID_START_TIME + "", VALID_END_TIME + "", null);
		expectedCA = new CommandAction(MSG_INVALIDTIME, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		expectedTaskList = new ArrayList<Task>();
		// Test 3d: start time before end time
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, VALID_START_TIME + "", VALID_END_TIME + "", null);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, VALID_START_TIME, VALID_END_TIME, PRIORITY_TYPE.NORMAL);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKUPDATED, VALID_TASKID), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());
	}

	/**
	 * Testing of CmdUpdate priority field
	 */
	public void testCmdUpdate_priority_field() {
		// Initialize test variables
		CommandAction expectedCA;
		Task expectedTask;
		List<Task> expectedTaskList;

		/*
		 * Test 4: Testing priority field (all ID are valid)(default priority is
		 * normal)
		 */
		ctf.initialize();
		expectedTaskList = null;
		addNewTask(TASK_NAME_1);

		// Test 4a: priority is null
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, null, null, null);
		expectedCA = new CommandAction(MSG_TASKNOUPDATE, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		// Test 4b: priority is normal
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, null, null, CmdParameters.PARAM_VALUE_TASK_PRIORITY_NORM);
		expectedCA = new CommandAction(MSG_TASKNOUPDATE, false, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());

		expectedTaskList = new ArrayList<Task>();
		// Test 4c: priority is high
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, null, null, CmdParameters.PARAM_VALUE_TASK_PRIORITY_HIGH);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, NO_TIME, PRIORITY_TYPE.HIGH);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKUPDATED, VALID_TASKID), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());
		
		expectedTaskList = new ArrayList<Task>();
		// Test 4d: priority is low
		_testCmdUpdate = new CmdUpdate();
		setParameters(VALID_TASKID + "", TASK_NAME_1, null, null, CmdParameters.PARAM_VALUE_TASK_PRIORITY_LOW);
		expectedTask = ctf.createExpectedTask(TASK_NAME_1, NO_TIME, NO_TIME, PRIORITY_TYPE.LOW);
		expectedTaskList.add(expectedTask);
		expectedCA = new CommandAction(String.format(MSG_TASKUPDATED, VALID_TASKID), true, expectedTaskList);
		ctf.assertCommandAction(expectedCA, _testCmdUpdate.execute());
	}

	/**
	 * add new task to Command.xml for testing
	 * 
	 * @param taskName
	 *            task name of new task
	 */
	public void addNewTask(String taskName) {
		Command cmdAdd = new CmdAdd();
		cmdAdd.setParameter(CmdParameters.PARAM_NAME_TASK_NAME, taskName);
		cmdAdd.execute();
	}

	/**
	 * Set parameters for testing
	 * 
	 * @param taskID
	 *            task ID to be set as parameter
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
	public void setParameters(String taskID, String taskName, String startTime, String endTime, String priority) {
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_ID, taskID);
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_SNAME, taskName);
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_STARTTIME, startTime);
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_ENDTIME, endTime);
		_testCmdUpdate.setParameter(CmdParameters.PARAM_NAME_TASK_PRIORITY, priority);
	}

}
