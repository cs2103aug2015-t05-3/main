package tds.junit;
import tds.Task;
import tds.Task.FLAG_TYPE;
import tds.Task.PRIORITY_TYPE;
import tds.TaskTree;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Provide a JUnit test for the {@code TaskTree.java}.  
 *
 * @author amoshydra
 */
public class JUnitTaskTreeTest {
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
			1L,
			2L,
			3L,
			4L
			};
	private long[] endTimes = {
			0L,			// floating task
			1441119600000L,	// 01 Sep 2015 15:00:00
			1442734205000L, // 20 Sep 2015 07:30:05
			1443112320000L, // 24 Sep 2015 16:32:00
			1444059228000L	// 05 Oct 2015 15:33:48
			};
	private FLAG_TYPE[] flags = {
			Task.FLAG_TYPE.DONE,
			Task.FLAG_TYPE.NULL,
			Task.FLAG_TYPE.NULL,
			Task.FLAG_TYPE.NULL,
			Task.FLAG_TYPE.NULL
			};
	private PRIORITY_TYPE[] priorities = {
			Task.PRIORITY_TYPE.NORMAL,
			Task.PRIORITY_TYPE.HIGH,
			Task.PRIORITY_TYPE.NORMAL,
			Task.PRIORITY_TYPE.VERY_HIGH,
			Task.PRIORITY_TYPE.NORMAL
			};

	//@TODO fileProcess implementation conflict with current JUnit testing
	public void testAddElementsToTree () {
		TaskTree.init();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);

		Task temp;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			TaskTree.add(temp);
			checkList.add(temp);
		}
		
		String checkString = "[";
		for (Task task : checkList) {
			checkString += task + ",";
		}
		checkString = checkString.substring(0, checkString.length() - 1);
		checkString += "]";
		
		String resultString = "" + TaskTree.getString();
		
		assertEquals(resultString, checkString);
		assertTrue(checkList.equals(new ArrayList<Task>(TaskTree.getList())));
	}

	//@TODO fileProcess implementation conflict with current JUnit testing
	public void testBuildTreeFromCollection () {
		Task temp;
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS); 
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			checkList.add(temp);
		}
		TaskTree.init(checkList);
		assertEquals(TaskTree.getList(), checkList);
	}
	
	@Test
	public void testRemoveElementsFromTree () {
		TaskTree.init();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);

		Task temp;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			TaskTree.add(temp);
			checkList.add(temp);
		}
		
		
		int listSize = TaskTree.size();
		ArrayList<Task> returnList;
		String searchTerm;
		
		// Get original list
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(TaskTree.searchName(searchTerm));
		assertEquals(TaskTree.size(), listSize);
		
		// Remove last element
		TaskTree.remove(returnList.get(returnList.size() - 1));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(TaskTree.searchName(searchTerm));
		assertEquals(TaskTree.size(), listSize -= 1);
	
		// Remove second element
		TaskTree.remove(returnList.get(FIRST_ELEMENT + 1));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(TaskTree.searchName(searchTerm));
		assertEquals(TaskTree.size(), listSize -= 1);
		
		// Remove last element
		TaskTree.remove(returnList.get(FIRST_ELEMENT));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(TaskTree.searchName(searchTerm));
		assertEquals(TaskTree.size(), listSize -= 1);
		
		// Remove non-existing element
		assertFalse(TaskTree.remove(new Task("Dummy")));
	}
	
	@Test
	public void testReplaceElementsFromTree () {
		TaskTree.init();
		int originalListSize = TaskTree.size() - 1;
		
		Task temp;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			TaskTree.add(temp);
		}
		
		ArrayList<Task> returnList, originalList;
		Task taskOld, taskNew;
		String checkString, resultString, replaceTerm;
		
		// Get original list
		originalList = new ArrayList<Task>(TaskTree.getList());
		returnList = new ArrayList<Task>(TaskTree.getList());
		assertEquals(TaskTree.size() - originalListSize, returnList.size());
			
		// Check update name; only the name of this task will be changed
		originalList = new ArrayList<Task>(returnList);
		replaceTerm = "Milk is delicious";
		
		taskOld = returnList.get(FIRST_ELEMENT);
		checkString = taskOld.getName();
		TaskTree.updateName(taskOld, replaceTerm);
		returnList = new ArrayList<Task>(TaskTree.getList());
		assertNotEquals(checkString, replaceTerm);
		
		// Check update end time; only the end time of this task will be changed
		originalList = new ArrayList<Task>(returnList);
		checkString = originalList.toString();
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			TaskTree.updateEndTime(returnList.get(i), 0);
		}
		resultString = returnList.toString();
		assertNotEquals(resultString, checkString);
	}
	
	//@TODO fileProcess implementation conflict with current JUnit testing
	public void testQueryTime() {
		TaskTree.init();

		Task temp;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			TaskTree.add(temp);
		}
		
		long lowerBound;
		long upperBound;
		ArrayList<Task> resultList;
		
		// Setting up test cases
		// first item (inclusive) to more than last element (inclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT] + FIVE_SECONDS;
		resultList = new ArrayList<Task>(TaskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);
		
		// first item (inclusive) to last element (inclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(TaskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);
		
		
		// lowerBound > upperBound
		lowerBound = endTimes[LAST_ELEMENT];
		upperBound = endTimes[FIRST_ELEMENT];
		resultList = new ArrayList<Task>(TaskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == 0);
		
		// item less than first element (inclusive) to first item (inclusive)
		lowerBound = endTimes[LAST_ELEMENT] - FIVE_SECONDS;
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(TaskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == 1);
	}
	
	//@TODO fileProcess implementation conflict with current JUnit testing
	public void testSearchFlagAndPriority() {
		TaskTree.init();

		Task temp;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			TaskTree.add(temp);
		}
		
		FLAG_TYPE searchFlag;
		FLAG_TYPE lowerBoundFlag;
		FLAG_TYPE upperBoundFlag;
		
		PRIORITY_TYPE searchPriority;
		ArrayList<Task> returnList;
		
		// Get done flag
		searchFlag = Task.FLAG_TYPE.DONE;
		returnList = new ArrayList<Task>(TaskTree.searchFlag(searchFlag));
		assertEquals(returnList.size(), 1);
		
		// Get done flag
		searchFlag = Task.FLAG_TYPE.NULL;
		returnList = new ArrayList<Task>(TaskTree.searchFlag(searchFlag));
		assertEquals(returnList.size(), 4);
		
		// Query done flag
		lowerBoundFlag = Task.FLAG_TYPE.NULL;
		upperBoundFlag = Task.FLAG_TYPE.DONE;
		returnList = new ArrayList<Task>(TaskTree.queryFlag(lowerBoundFlag,upperBoundFlag));
		assertEquals(returnList.size(), 5);
		
		// Query done flag
		searchPriority = Task.PRIORITY_TYPE.HIGH;
		returnList = new ArrayList<Task>(TaskTree.queryPriority(searchPriority,searchPriority));
		assertEquals(returnList.size(), 1);
		
		// Query priority normal
		searchPriority = Task.PRIORITY_TYPE.NORMAL;
		returnList = new ArrayList<Task>(TaskTree.searchPriority(searchPriority));
		assertEquals(returnList.size(), 3);		
	}
	
	@SuppressWarnings("unused")
	private void printList(List<Task> returnList) {
		int index;
		if (returnList.isEmpty()) {
			System.out.println("No item in list");
		} else {
			for (int i = 0; i < returnList.size(); i++) {
				index = i + 1;
				System.out.println("" + index + ". " + returnList.get(i));
			}
		}
		System.out.println();
	}

}
