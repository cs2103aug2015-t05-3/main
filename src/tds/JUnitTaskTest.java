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

	@Test
	public void testEqual() {
		
		// Initializing task attributes
		String taskName = "Buy Milk tonight";
		String taskNameDiff = "Buy Milk tomorrow";

		Date timeNow = new Date();
		long startTime = timeNow.getTime() % 1000;
		long endTime = startTime + (2 * HOUR);
		
		int flag = Task.FLAG_NULL;
		
		// Setting up different task conditions
		Task taskOriginal = new Task(taskName, startTime, endTime, flag);
		Task taskSame = new Task(taskName, startTime, endTime, flag);
		Task taskLowerCase = new Task(taskName.toLowerCase(), startTime, endTime, flag);
		Task taskIsDone = new Task(taskName, startTime, endTime, Task.FLAG_DONE);
		Task taskDiff = new Task(taskNameDiff, startTime, endTime, flag);
		
		// Setting up test cases
		boolean testEqual, testDiffCase, testDiffName, testDiffTime, testDiffFlag;
		testEqual = testDiffCase = testDiffFlag = testDiffTime = testDiffName = false;
		
		assertTrue(testEqual);
		assertFalse(testDiffCase);
		assertFalse(testDiffName);
		assertFalse(testDiffTime);
		assertFalse(testDiffFlag);
	}
	
}
