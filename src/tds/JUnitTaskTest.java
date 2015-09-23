/**
 * Provide a JUnit test for the {@code Task.java}.  
 *
 * @author amoshydra
 */

package tds;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Date;

public class JUnitTaskTest {
	
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
	
	// Setting up different task conditions
	private Task taskOriginal =	new Task(taskName, startTime, endTime, flag);
	private Task taskSameTask =	new Task(taskName, startTime, endTime, flag);
	private Task taskLowCase =	new Task(taskNameLowCase, startTime, endTime, flag);
	private Task taskIsDone =	new Task(taskName, startTime, endTime, Task.FLAG_DONE);
	private Task taskDiffTime =	new Task(taskNameDiff, startTime, startTime, flag);
	private Task taskDiffName =	new Task(taskNameDiff, startTime, endTime, flag);
	
	@Test
	public void testEquals() {
		
		// Setting up test cases
		boolean testEqual, testDiffCase, testDiffName, testDiffTime, testDiffFlag;
		
		testEqual = 	taskOriginal.equals(taskSameTask);
		testDiffCase =	taskOriginal.equals(taskLowCase); 
		testDiffFlag =	taskOriginal.equals(taskIsDone);
		testDiffTime =	taskOriginal.equals(taskDiffTime);
		testDiffName =	taskOriginal.equals(taskDiffName);
			
		// Evaluate test cases
		assertTrue(testEqual);
		assertFalse(testDiffCase);
		assertFalse(testDiffName);
		assertFalse(testDiffTime);
		assertFalse(testDiffFlag);
	}
}
