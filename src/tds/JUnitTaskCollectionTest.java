/**
 * Provide a JUnit test for the {@code Task.java}.  
 *
 * @author amoshydra
 */

package tds;
import org.junit.Test;

import tds.comparators.*;

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
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_HIGH,
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_VERY_HIGH,
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
		returnIndex = TaskArrayList.getClosestMatchIndex(list, testValue,
				Task.GET_VALUE_END_TIME, FIRST_ELEMENT, LAST_ELEMENT);
		assertTrue(returnIndex == FIRST_ELEMENT);
		
		// Exact match is found
		for (int i = FIRST_ELEMENT; i < NUM_OF_ITEMS; i++) {
			testValue = endTimes[i];
			returnIndex = TaskArrayList.getClosestMatchIndex(list, testValue,
					Task.GET_VALUE_END_TIME, FIRST_ELEMENT, LAST_ELEMENT);
			assertTrue(returnIndex == i);
		}
		
		// Checking value between i and i+1 should always return i+1
		for (int i = FIRST_ELEMENT; i < NUM_OF_ITEMS; i++) {
			testValue = endTimes[i] + FIVE_SECONDS;
			returnIndex = TaskArrayList.getClosestMatchIndex(list, testValue,
					Task.GET_VALUE_END_TIME, FIRST_ELEMENT, LAST_ELEMENT);
			assertTrue(returnIndex == i + 1);
		}
		
		// Greater than last index
		testValue = endTimes[LAST_ELEMENT] + FIVE_SECONDS;
		returnIndex = TaskArrayList.getClosestMatchIndex(list, testValue,
				Task.GET_VALUE_END_TIME, FIRST_ELEMENT, LAST_ELEMENT);
		assertTrue(returnIndex == NUM_OF_ITEMS);
		
	}

	@Test
	public void testGetSortedList() {
		ArrayList<Task> resultList;
		TaskArrayList list = new TaskArrayList();
		
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			list.add(new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]));
		}
		
		// Setting up test cases
		boolean isNameSorted, isStartTimeSorted, isEndTimeSorted, isFlagSorted, isPrioritySorted;
		
		resultList = new ArrayList<Task>(list.getSortedList(new NameComparator()));
		isNameSorted = checkIsAccending(resultList, Task.GET_VALUE_NAME);

		resultList = new ArrayList<Task>(list.getSortedList(new StartTimeComparator()));
		isStartTimeSorted = checkIsAccending(resultList, Task.GET_VALUE_START_TIME);
		
		resultList = new ArrayList<Task>(list.getSortedList(new EndTimeComparator()));
		isEndTimeSorted = checkIsAccending(resultList, Task.GET_VALUE_END_TIME);
		
		resultList = new ArrayList<Task>(list.getSortedList(new FlagComparator()));
		isFlagSorted = checkIsAccending(resultList, Task.GET_VALUE_FLAG);
		
		resultList = new ArrayList<Task>(list.getSortedList(new PriorityComparator()));
		isPrioritySorted = checkIsAccending(resultList, Task.GET_VALUE_PRIORITY);
		
		// Evaluate tests
		assertTrue(isNameSorted);
		assertTrue(isStartTimeSorted);
		assertTrue(isEndTimeSorted);
		assertTrue(isFlagSorted);
		assertTrue(isPrioritySorted);
	}
	
	private boolean checkIsAccending(ArrayList<Task> resultList, int option) {
		boolean isAccending = true;
		for (int i = FIRST_ELEMENT + 1; i < NUM_OF_ITEMS; i++) {
			Task first = resultList.get(i - 1);
			Task second = resultList.get(i);
			if (first.getValue(option) > second.getValue(option)) {
				isAccending = false;
			}
		}
		return isAccending;
	}
	
	private boolean checkIsAccending(ArrayList<Task> resultList, long option) {
		boolean isAccending = true;
		for (int i = FIRST_ELEMENT + 1; i < NUM_OF_ITEMS; i++) {
			Task first = resultList.get(i - 1);
			Task second = resultList.get(i);
			if (first.getValue(option) > second.getValue(option)) {
				isAccending = false;
			}
		}
		return isAccending;
	}

	private boolean checkIsAccending(ArrayList<Task> resultList, String option) {
		boolean isAccending = true;
		for (int i = FIRST_ELEMENT + 1; i < NUM_OF_ITEMS; i++) {
			Task first = resultList.get(i - 1);
			Task second = resultList.get(i);
			if (first.getValue(option).compareTo(second.getValue(option)) > 0) {
				isAccending = false;
			}
		}
		return isAccending;
	}
	
	@Test
	public void testQueryTime() {
		TaskArrayList list = new TaskArrayList();
		ArrayList<Task> resultList;
		
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			list.add(new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]));
		}
		
		long lowerBound;
		long upperBound;
		
		// Setting up test cases
		// first item (inclusive) to more than last element (exclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT] + FIVE_SECONDS;
		resultList = new ArrayList<Task>(list.queryEndTime(upperBound, lowerBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);
		
		// first item (inclusive) to last element (exclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(list.queryEndTime(upperBound, lowerBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS - 1);
		
		// lowerBound > upperBound
		lowerBound = endTimes[LAST_ELEMENT];
		upperBound = endTimes[FIRST_ELEMENT];
		resultList = new ArrayList<Task>(list.queryEndTime(upperBound, lowerBound));
		assertTrue(resultList.size() == 0);
		
		// item less than first element (inclusive) to first item (exclusive)
		lowerBound = endTimes[LAST_ELEMENT] - FIVE_SECONDS;
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(list.queryEndTime(upperBound, lowerBound));
		assertTrue(resultList.size() == 0);
	}

	public void testReplace() {
		TaskArrayList list = new TaskArrayList();
		ArrayList<Task> resultList;
		
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			list.add(new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]));
		}
		
		resultList = new ArrayList<Task>(list.searchName("o"));
		for (Task task : resultList) {
			System.out.println(task);
		}
		System.out.println();
		
		Task temp = resultList.get(2);
		temp.setFlag(Task.FLAG_DONE);
		temp.setName("This is cool");
		
		for (Task task : resultList) {
			System.out.println(task);
		}
		System.out.println();
		
		System.out.println(list);
	}
}
