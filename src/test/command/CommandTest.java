//@@author A0125574A

/**
 * Testing of Command
 */

package test.command;

//JUnit
import org.junit.Test;

public class CommandTest {

	/*
	 * Variables for internal use
	 */
	CmdAddTest _CmdAddTest;
	CmdDeleteTest _CmdDeleteTest;
	CmdUpdateTest _CmdUpdateTest;

	@Test
	public void testCommands() {
		testCmdAdd();
		testCmdDelete();
		testCmdUpdate();
	}

	/**
	 * Testing of CmdAdd
	 */
	public void testCmdAdd() {
		_CmdAddTest = new CmdAddTest();
		_CmdAddTest.testCmdAdd();
	}

	/**
	 * Testing of CmdDelete
	 */
	public void testCmdDelete() {
		_CmdDeleteTest = new CmdDeleteTest();
		_CmdDeleteTest.testCmdDelete();
	}

	/**
	 * Testing of CmdUpdate
	 */
	public void testCmdUpdate() {
		_CmdUpdateTest = new CmdUpdateTest();
		_CmdUpdateTest.testCmdUpdate();
	}

}
