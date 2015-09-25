/**
 * Provide a JUnit test for the {@code Task.java}.  
 *
 * @author amoshydra
 */

package tds.junit;
import org.junit.Test;

import tds.Task;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Date;

public class JUnitTaskTest {
	
	@SuppressWarnings("unused")
	private static final int MINUTE = 60;
	private static final int HOUR = 3600;

	// Initializing task attributes
	private String taskName 		= "Buy Milk tonight";
	private String taskNameLowCase	= "buy milk tonight";
	private String taskNameDiff	= "Buy Milk tomorrow";

	private Date nowTime 	= new Date();
	private long startTime	= nowTime.getTime() % 1000;
	private long endTime	= startTime + (2 * HOUR);
	
	private int flag = Task.FLAG_NULL;
	private int priority = Task.PRIORITY_NORMAL;
	
	// Setting up different task conditions
	private static final int TASK_ORIGINAL = 0;
	private static final int TASK_SAME_TASK = 1;
	private static final int TASK_LOW_CASE = 2;
	private static final int TASK_IS_DONE = 3;
	private static final int TASK_DIFF_TIME = 4;
	private static final int TASK_DIFF_NAME = 5;
	private static final int TASK_DIFF_PRIORITY = 6;
	@SuppressWarnings("unused")
	private static final int TASK_ARRAY_SIZE = 7;
	
	Task[] tasks = {new Task(taskName, startTime, endTime, flag, priority),
			new Task(taskName, startTime, endTime, flag, priority),
			new Task(taskNameLowCase, startTime, endTime, flag, priority),
			new Task(taskName, startTime, endTime, Task.FLAG_DONE, priority),
			new Task(taskNameDiff, startTime, startTime, flag, priority),
			new Task(taskNameDiff, startTime, endTime, flag, priority),
			new Task(taskNameDiff, startTime, endTime, flag, Task.PRIORITY_VERY_HIGH)
			};
	
	@Test
	public void testEquals() {
		
		// Setting up test cases
		boolean testEqual, testDiffCase, testDiffName, testDiffTime, testDiffFlag, testDiffPriority;
		
		testEqual = 	tasks[TASK_ORIGINAL].equals(tasks[TASK_SAME_TASK]);
		testDiffCase =	tasks[TASK_ORIGINAL].equals(tasks[TASK_LOW_CASE]); 
		testDiffName =	tasks[TASK_ORIGINAL].equals(tasks[TASK_DIFF_NAME]);
		testDiffTime =	tasks[TASK_ORIGINAL].equals(tasks[TASK_DIFF_TIME]);
		testDiffFlag =	tasks[TASK_ORIGINAL].equals(tasks[TASK_IS_DONE]);
		testDiffPriority =	tasks[TASK_ORIGINAL].equals(tasks[TASK_DIFF_PRIORITY]);
			
		// Evaluate test cases
		assertTrue(testEqual);
		assertFalse(testDiffCase);
		assertFalse(testDiffName);
		assertFalse(testDiffTime);
		assertFalse(testDiffFlag);
		assertFalse(testDiffPriority);
	}

	@Test
	public void testCompares() {
		
		// Setting up test cases
		int testSameTask, testDiffCase, testDiffName, testDiffTime, testDiffFlag, testDiffPriority;
		
		testSameTask = tasks[TASK_ORIGINAL].compareTo(tasks[TASK_SAME_TASK]);
		testDiffCase = tasks[TASK_ORIGINAL].compareTo(tasks[TASK_LOW_CASE]);
		testDiffName = tasks[TASK_ORIGINAL].compareTo(tasks[TASK_DIFF_NAME]);
		testDiffTime = tasks[TASK_ORIGINAL].compareTo(tasks[TASK_DIFF_TIME]);
		testDiffFlag = tasks[TASK_ORIGINAL].compareTo(tasks[TASK_IS_DONE]);
		testDiffPriority = tasks[TASK_ORIGINAL].compareTo(tasks[TASK_DIFF_PRIORITY]);
		
		// Evaluate test cases
		assertTrue(testSameTask == 0);
		assertTrue(testDiffCase < 0);		// "Buy Milk tonight" is smaller than "buy milk tomorrow"
		assertTrue(testDiffName > 0);		// "Buy Milk tonight" is greater than "Buy Milk tomorrow"
		assertTrue(testDiffTime > 0);		// "2 * HOUR" is greater than "0"
		assertTrue(testDiffFlag < 0);		// "IS_NULL" is smaller than "IS_DONE"
		assertTrue(testDiffPriority > 0);	// "NORMAL" is greater than "VERY_HIGH"
	}
}
