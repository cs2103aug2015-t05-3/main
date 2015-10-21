package test;

import java.util.ArrayList;
import java.util.List;

//logic.command
import logic.command.CmdSearch;
import taskCollections.Task;

//JUnit
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CommandTest {
	
	CmdSearch _testSearch = new CmdSearch();
	String _expectedOutput;
	List<Task> _taskList;
	
	@Test
	public void testCmdSearch(){
		
		/*
		 * Test 1  [Combining multiple inputs using test pairs]
		 * 		1.: When size of taskList == 0, the boolean isID should affect the expected output.
		 */
		_taskList = new ArrayList<Task>();
		
		//Test 1a: Empty taskList and isID == true
		_expectedOutput = "Specified taskID \"0\" not found";
		_testSearch._taskID = 0;
		assertEquals(_expectedOutput, _testSearch.getOutputMsg(_taskList, true));
		
		//Test 1b: Empty taskList and isID == false
		_expectedOutput = "Specified task \"Testing\" not found";
		_testSearch._taskName = "Testing";
		assertEquals(_expectedOutput, _testSearch.getOutputMsg(_taskList, false));
		
		
		/*
		 * Test 2  [Combining multiple inputs using test pairs]
		 * 		1.: When size of taskList > 1, the boolean isID should not affect the expected output.
		 *		2.: Boundary case for this test is [2, MAX_INTEGER] (*not tested yet)
		 */	
		
		_expectedOutput = "[2] instances of \"Testing\" found";
		_testSearch._taskName = "Testing";
		_taskList = new ArrayList<Task>();
		for(int i=0; i<2; i++){
			_taskList.add(new Task("Testing"+i,null, 0, 0, Task.FLAG_TYPE.NULL, Task.PRIORITY_TYPE.NORMAL));
		}
		
		//Test 2a: taskList size == 2 (isID == false)
		assertEquals(_expectedOutput, _testSearch.getOutputMsg(_taskList, false));
		
		//Test 2b: taskList size == 2 (isID == true)
		assertEquals(_expectedOutput, _testSearch.getOutputMsg(_taskList, true));
		
		
		/*
		 * TLE (Unable to test due to inefficient way of adding task to list)
		 * 
		//Test 2b: taskList size > 1 (size == Integer.MAX_VALUE)
		_expectedOutput = "[2147483647] instances of \"Testing\" found";
		_testSearch._taskName = "Testing";
		_taskList = new ArrayList<Task>();
		for(int i=0; i<Integer.MAX_VALUE; i++){
			_taskList.add(new Task("Testing"+i,null, 0, 0, Task.FLAG_TYPE.NULL, Task.PRIORITY_TYPE.NORMAL));
		}
		assertEquals(_expectedOutput, _testSearch.getOutputMsg(_taskList, false));
		*/

		/*
		 * Test 3  [Combining multiple inputs using test pairs]
		 * 		1.: When size of taskList == 1, the boolean isID should not affect the expected output.
		 */
		_expectedOutput = "Task \"TestingFullTaskName\" found";
		_taskList = new ArrayList<Task>();
		_taskList.add(new Task("TestingFullTaskName",null, 0, 0, Task.FLAG_TYPE.NULL, Task.PRIORITY_TYPE.NORMAL));
		
		//Test 3a: taskList size == 1 (isID == false) [In the event search was by taskName]
		assertEquals(_expectedOutput, _testSearch.getOutputMsg(_taskList, false));
		
		//Test 3b: taskList size == 1 (isID == true) [In the event search was by taskID]
		assertEquals(_expectedOutput, _testSearch.getOutputMsg(_taskList, true));
	}
	
}
