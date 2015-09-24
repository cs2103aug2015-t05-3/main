/**
 * Provide a JUnit test for the {@code Task.java}.  
 *
 * @author amoshydra
 */

package tds;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

public class JUnitTaskCollectionTest {

	private static final int FIRST_ELEMENT = 0;
	private static final int LAST_ELEMENT = 4;
	private static final int NUM_OF_ITEMS = 5;
	private static final int FIVE_SECONDS = 5000;
	private String[] names = {
			"Buy mushroom", 
			"Buy milk", 
			"Buy hotdog", 
			"Do homework",
			"Learn swimming"
			};
	private long[] startTimes = {
			0L,
			0L,
			0L,
			0L,
			0L
			};
	private long[] endTimes = {
			0L,			// floating task
			1441119600000L,	// 01 Sep 2015 15:00:00
			1442734205000L, // 20 Sep 2015 07:30:05
			1443112320000L, // 24 Sep 2015 16:32:00
			1444059228000L	// 05 Oct 2015 15:33:48
			};
	private int[] flags = {
			Task.FLAG_DONE,
			Task.FLAG_NULL,
			Task.FLAG_NULL,
			Task.FLAG_NULL,
			Task.FLAG_NULL
			};
	private int[] priorities = {
			Task.PRIORITY_HIGH,
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_NORMAL
			};
	
	@Test
	public void testGetClosestMatchIndex() {
		ArrayList<Task> list = new ArrayList<Task>();
		
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			list.add(new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]));
		}
		
		long testValue;
		int returnIndex;
		
		// Smaller than first index
		testValue = endTimes[FIRST_ELEMENT] - FIVE_SECONDS;
		returnIndex = TaskArrayList.getClosestMatchIndex(list, testValue, FIRST_ELEMENT, LAST_ELEMENT);
		assertTrue(returnIndex == FIRST_ELEMENT);
		
		// Exact match is found
		for (int i = FIRST_ELEMENT; i < NUM_OF_ITEMS; i++) {
			testValue = endTimes[i];
			returnIndex = TaskArrayList.getClosestMatchIndex(list, testValue, FIRST_ELEMENT, LAST_ELEMENT);
			assertTrue(returnIndex == i);
		}
		
		// Checking value between i and i+1 should always return i+1
		for (int i = FIRST_ELEMENT; i < NUM_OF_ITEMS; i++) {
			testValue = endTimes[i] + FIVE_SECONDS;
			returnIndex = TaskArrayList.getClosestMatchIndex(list, testValue, FIRST_ELEMENT, LAST_ELEMENT);
			assertTrue(returnIndex == i + 1);
		}
		
		// Greater than last index
		testValue = endTimes[LAST_ELEMENT] + FIVE_SECONDS;
		returnIndex = TaskArrayList.getClosestMatchIndex(list, testValue, FIRST_ELEMENT, LAST_ELEMENT);
		assertTrue(returnIndex == NUM_OF_ITEMS);
		
	}
}
