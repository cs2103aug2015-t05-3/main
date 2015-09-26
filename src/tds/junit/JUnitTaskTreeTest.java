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

	private void testAddElementsToTree(TaskTree taskTree, ArrayList<Task> checkList) {
		Task temp;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			taskTree.add(temp);
			checkList.add(temp);
		}
	}
	
	@Test
	public void testAddElementsToTree () {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		String checkString = "";
		for (Task task : checkList) {
			checkString += task + "\n";
		}
		String resultString = "" + taskTree;
		
		assertEquals(resultString, checkString);
		assertTrue(checkList.equals(new ArrayList<Task>(taskTree.getList())));
	}

	@Test
	public void testBuildTreeFromCollection () {
		Task temp;
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS); 
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			checkList.add(temp);
		}
		TaskTree taskTree = new TaskTree(checkList);
		assertEquals(taskTree.getList(), checkList);
	}
	
	@Test
	public void testRemoveElementsFromTree () {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		int listSize = taskTree.size();
		ArrayList<Task> returnList;
		String searchTerm;
		
		// Get original list
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(taskTree.searchName(searchTerm));
		assertEquals(taskTree.size(), listSize);
		
		// Remove last element
		taskTree.remove(returnList.get(returnList.size() - 1));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(taskTree.searchName(searchTerm));
		assertEquals(taskTree.size(), listSize -= 1);
	
		// Remove second element
		taskTree.remove(returnList.get(FIRST_ELEMENT + 1));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(taskTree.searchName(searchTerm));
		assertEquals(taskTree.size(), listSize -= 1);
		
		// Remove last element
		taskTree.remove(returnList.get(FIRST_ELEMENT));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(taskTree.searchName(searchTerm));
		assertEquals(taskTree.size(), listSize -= 1);
		
		// Remove non-existing element
		assertFalse(taskTree.remove(new Task("Dummy")));
	}
	
	@Test
	public void testReplaceElementsFromTree () {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		ArrayList<Task> returnList, originalList;
		Task taskOld, taskNew;
		String checkString, resultString, replaceTerm;
		
		// Get original list
		originalList = new ArrayList<Task>(taskTree.getList());
		returnList = new ArrayList<Task>(taskTree.getList());
		assertEquals(taskTree.size(), returnList.size());
		
		// Check task replace method; task id will be changed
		originalList = new ArrayList<Task>(returnList);
		replaceTerm = "Buy giant mushroom";
		
		taskOld = returnList.get(FIRST_ELEMENT);
		taskNew = new Task(replaceTerm, taskOld.getStartTime(), taskOld.getEndTime(), taskOld.getFlag(), taskOld.getPriority());
		taskTree.replace(taskOld, taskNew);
		returnList = new ArrayList<Task>(taskTree.getList());
		assertEquals(returnList.get(LAST_ELEMENT).getName(), replaceTerm);
		
		// Check update name; only the name of this task will be changed
		originalList = new ArrayList<Task>(returnList);
		replaceTerm = "Milk is delicious";
		
		taskOld = returnList.get(FIRST_ELEMENT);
		checkString = taskOld.getName();
		taskTree.updateName(taskOld, replaceTerm);
		returnList = new ArrayList<Task>(taskTree.getList());
		assertNotEquals(checkString, replaceTerm);
		
		// Check update end time; only the end time of this task will be changed
		originalList = new ArrayList<Task>(returnList);
		checkString = originalList.toString();
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			taskTree.updateEndTime(returnList.get(i), 0);
		}
		resultString = returnList.toString();
		assertNotEquals(resultString, checkString);
	}
	
	@Test
	public void testQueryTime() {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		long lowerBound;
		long upperBound;
		ArrayList<Task> resultList;
		
		// Setting up test cases
		// first item (inclusive) to more than last element (inclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT] + FIVE_SECONDS;
		resultList = new ArrayList<Task>(taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);
		
		// first item (inclusive) to last element (inclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);
		
		
		// lowerBound > upperBound
		lowerBound = endTimes[LAST_ELEMENT];
		upperBound = endTimes[FIRST_ELEMENT];
		resultList = new ArrayList<Task>(taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == 0);
		
		// item less than first element (inclusive) to first item (inclusive)
		lowerBound = endTimes[LAST_ELEMENT] - FIVE_SECONDS;
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == 1);
	}
	
	@Test
	public void testSearchFlagAndPriority() {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		FLAG_TYPE searchFlag;
		FLAG_TYPE lowerBoundFlag;
		FLAG_TYPE upperBoundFlag;
		
		PRIORITY_TYPE searchPriority;
		ArrayList<Task> returnList;
		
		// Get done flag
		searchFlag = Task.FLAG_TYPE.DONE;
		returnList = new ArrayList<Task>(taskTree.searchFlag(searchFlag));
		assertEquals(returnList.size(), 1);
		
		// Get done flag
		searchFlag = Task.FLAG_TYPE.NULL;
		returnList = new ArrayList<Task>(taskTree.searchFlag(searchFlag));
		assertEquals(returnList.size(), 4);
		
		// Query done flag
		lowerBoundFlag = Task.FLAG_TYPE.NULL;
		upperBoundFlag = Task.FLAG_TYPE.DONE;
		returnList = new ArrayList<Task>(taskTree.queryFlag(lowerBoundFlag,upperBoundFlag));
		assertEquals(returnList.size(), 5);
		
		// Query done flag
		searchPriority = Task.PRIORITY_TYPE.HIGH;
		returnList = new ArrayList<Task>(taskTree.queryPriority(searchPriority,searchPriority));
		assertEquals(returnList.size(), 1);
		
		// Query priority normal
		searchPriority = Task.PRIORITY_TYPE.NORMAL;
		returnList = new ArrayList<Task>(taskTree.searchPriority(searchPriority));
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
