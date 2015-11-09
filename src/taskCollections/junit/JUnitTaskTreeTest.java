//@@A0126394B
package taskCollections.junit;

import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import taskCollections.TaskTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private static TaskTree _taskTree;
	private static final String testTaskFilePath = "test_taskTreeJUnitFile.xml";
	private static final int FIRST_ELEMENT = 0;
	private static final int LAST_ELEMENT = 4;
	private static final int NUM_OF_ITEMS = 5;
	private static final int FIVE_SECONDS = 5000;
	private String[] names = {
			"Buy mushroom",
			"Buy milk",
			"Buy hotdog",
			"Do homework",
			"Learn swimming" };
	private String[] descriptions = {
			"",
			"brand, Meji, 1 litre",
			"extra mustard",
			"",
			"" };
	private long[] startTimes = { 0L, 1L, 2L, 3L, 4L };
	private long[] endTimes = {
			0L, // floating task
			1441119600000L, // 01 Sep 2015 15:00:00
			1442734205000L, // 20 Sep 2015 07:30:05
			1443112320000L, // 24 Sep 2015 16:32:00
			1444059228000L // 05 Oct 2015 15:33:48
			};
	private FLAG_TYPE[] flags = {
			Task.FLAG_TYPE.DONE,
			Task.FLAG_TYPE.NULL,
			Task.FLAG_TYPE.NULL,
			Task.FLAG_TYPE.NULL,
			Task.FLAG_TYPE.NULL };
	private PRIORITY_TYPE[] priorities = {
			Task.PRIORITY_TYPE.NORMAL,
			Task.PRIORITY_TYPE.HIGH,
			Task.PRIORITY_TYPE.NORMAL,
			Task.PRIORITY_TYPE.HIGH,
			Task.PRIORITY_TYPE.NORMAL };

	/**
	 * Initialize a new instance of {@code TaskTree}
	 */
	private void initialiseTaskTree() {
		File testFile = new File(testTaskFilePath);
		if (testFile.exists()) {
			deleteFile(testTaskFilePath);
		}

		_taskTree = TaskTree.getTaskTree();
		_taskTree = null;
		_taskTree = TaskTree.newTaskTree(testTaskFilePath);
	}

	/**
	 * Destroy the current instance of {@code TaskTree}
	 */
	private void deinitialiseTaskTree() {
		_taskTree = null;
		deleteFile(testTaskFilePath);
	}

	/**
	 * Delete a file from the system with the given file path
	 *
	 * @param filePathString
	 *            file path to delete
	 */
	private void deleteFile(String filePathString) {
		// TODO let some other class to create this file for me.
		Path filePath = Paths.get(filePathString);

		try {
			Files.delete(filePath);
		} catch (NoSuchFileException x) {
			//TODO replace error messages
			System.err.format("%s: no such" + " file or directory%n", filePath);
		} catch (DirectoryNotEmptyException x) {
			System.err.format("%s not empty%n", filePath);
		} catch (IOException x) {
			// File permission problems are caught here.
			System.err.println(x);
		}
	}

	private String generateCheckString(ArrayList<Task> checkList) {
		// TODO Replace magic string
		String checkString = "[";
		for (Task task : checkList) {
			checkString += task + ",";
		}
		checkString = checkString.substring(0, checkString.length() - 1);
		checkString += "]";

		return checkString;
	}

	private Task generateSampleTask(int index) {
		Task sampleTask = new Task(names[index],
								   descriptions[index],
								   startTimes[index],
								   endTimes[index],
								   flags[index],
								   priorities[index]);
		return sampleTask;
	}

	private void populateSampleTasks() {
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			Task sampleTask = generateSampleTask(i);
			_taskTree.add(sampleTask);
		}
	}

	/*
	 * Testing methods
	 * 1. add method will increase the size of task tree
	 * 2. make sure taskTree initialisation will always return an empty taskTree.
	 *
	 * Condition
	 * 1. Normal
	 */
	@Test
	public void testInitialiseTaskTree() {
		_taskTree = TaskTree.newTaskTree(testTaskFilePath);
		populateSampleTasks();
		ArrayList<Task> initialList = new ArrayList<Task>(_taskTree.getList());
		assertFalse(initialList.isEmpty());

		initialiseTaskTree();
		ArrayList<Task> initialisedList = new ArrayList<Task>(_taskTree.getList());
		assertTrue(initialisedList.isEmpty());
		deinitialiseTaskTree();
	}

	/*
	 * Testing methods
	 * 1. add
	 * 2. getString
	 *
	 * Condition
	 * 1. Normal
	 */
	@Test
	public void testAddElementsToTree () {
		initialiseTaskTree();

		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);

		Task temp;
		boolean isAllAddedToTaskTree = true;
		boolean isAllAddedToArrayList = true;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = generateSampleTask(i);

			// Add tasks to TaskTree via its add command
			isAllAddedToTaskTree &= _taskTree.add(temp);

			// Add tasks to an ArrayList
			isAllAddedToArrayList &= checkList.add(temp);
		}

		assertTrue(isAllAddedToTaskTree);
		assertTrue(isAllAddedToArrayList);

		String checkString = generateCheckString(checkList);
		String resultString = _taskTree.getString();
		assertEquals(resultString, checkString);

		deinitialiseTaskTree();
	}

	/*
	 * Testing methods
	 * 1. add
	 * 2. size
	 *
	 * Condition
	 * 1. Adding an existing task object
	 */
	@Test
	public void testAddIdenticalElementsToTree () {
		initialiseTaskTree();

		int firstTaskIndex = 1;
		int secondTaskIndex = 2;
		int numberOfSampleTask = 2;

		Task sampleTask1 = generateSampleTask(firstTaskIndex);
		Task sampleTask2 = generateSampleTask(secondTaskIndex);

		boolean isAllAddedToTaskTree = true;

		// Add the first task to TaskTree via its add command
		isAllAddedToTaskTree &= _taskTree.add(sampleTask1);
		assertTrue(isAllAddedToTaskTree);
		assertEquals(_taskTree.size(), firstTaskIndex);

		// Add the second task to TaskTree via its add command
		isAllAddedToTaskTree &= _taskTree.add(sampleTask2);
		assertTrue(isAllAddedToTaskTree);
		assertEquals(_taskTree.size(), secondTaskIndex);

		// Add the first task again to TaskTree via its add command
		isAllAddedToTaskTree &= _taskTree.add(sampleTask1);
		assertFalse(isAllAddedToTaskTree);
		assertEquals(_taskTree.size(), numberOfSampleTask);

		deinitialiseTaskTree();
	}

	/*
	 * Testing methods
	 * 1. add
	 * 2. size
	 *
	 * Condition
	 * 1. Adding an existing task object
	 */
	@Test(expected=NullPointerException.class)
	public void testAddNullElementsToTree () {
		initialiseTaskTree();

		Task sampleTask1 = null;

		// Add the first task to TaskTree via its add command
		_taskTree.add(sampleTask1);

		deinitialiseTaskTree();
	}


	@Test
	public void testRemoveElementsFromTree() {
		initialiseTaskTree();

		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);

		Task temp;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = generateSampleTask(i);
			_taskTree.add(temp);
			checkList.add(temp);
		}

		int listSize = _taskTree.size();
		ArrayList<Task> returnList;
		String searchTerm;

		// Get original list
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(_taskTree.searchName(searchTerm));
		assertEquals(_taskTree.size(), listSize);

		// Remove last element
		_taskTree.remove(returnList.get(returnList.size() - 1));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(_taskTree.searchName(searchTerm));
		assertEquals(_taskTree.size(), listSize -= 1);

		// Remove second element
		_taskTree.remove(returnList.get(FIRST_ELEMENT + 1));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(_taskTree.searchName(searchTerm));
		assertEquals(_taskTree.size(), listSize -= 1);

		// Remove last element
		_taskTree.remove(returnList.get(FIRST_ELEMENT));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(_taskTree.searchName(searchTerm));
		assertEquals(_taskTree.size(), listSize -= 1);

		// Remove non-existing element
		Task extraTask = new Task("Name", "Description", 0, 0, Task.FLAG_TYPE.NULL, Task.PRIORITY_TYPE.HIGH);
		assertFalse(_taskTree.remove(extraTask));
		assertEquals(_taskTree.size(), listSize);

		deinitialiseTaskTree();
	}


	@Test
	public void testReplaceElementsFromTree() {
		initialiseTaskTree();

		populateSampleTasks();

		ArrayList<Task> returnList, originalList;
		Task taskOld;
		String checkString, resultString, replaceTerm;

		// Get original list
		originalList = new ArrayList<Task>(_taskTree.getList());
		returnList = new ArrayList<Task>(_taskTree.getList());
		assertEquals(_taskTree.size(), returnList.size());

		// Check update name; only the name of this task will be changed
		originalList = new ArrayList<Task>(returnList);
		replaceTerm = "Milk is delicious";

		taskOld = returnList.get(FIRST_ELEMENT);
		checkString = taskOld.getName();
		_taskTree.updateName(taskOld, replaceTerm);
		returnList = new ArrayList<Task>(_taskTree.getList());
		assertNotEquals(checkString, replaceTerm);

		// Check update end time; only the end time of this task will be changed
		originalList = new ArrayList<Task>(returnList);
		checkString = originalList.toString();
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			_taskTree.updateEndTime(returnList.get(i), 0);
		}
		resultString = returnList.toString();
		assertNotEquals(resultString, checkString);

		deinitialiseTaskTree();
	}


	@Test
	public void testQueryTime() {
		initialiseTaskTree();

		populateSampleTasks();

		long lowerBound;
		long upperBound;
		ArrayList<Task> resultList;

		// Setting up test cases
		// first item (inclusive) to more than last element (inclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT] + FIVE_SECONDS;
		resultList = new ArrayList<Task>(_taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);

		// first item (inclusive) to last element (inclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(_taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);

		// lowerBound > upperBound
		lowerBound = endTimes[LAST_ELEMENT];
		upperBound = endTimes[FIRST_ELEMENT];
		resultList = new ArrayList<Task>(_taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == 0);

		// item less than first element (inclusive) to first item (inclusive)
		lowerBound = endTimes[LAST_ELEMENT] - FIVE_SECONDS;
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(_taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == 1);

		deinitialiseTaskTree();
	}


	public void testSearchFlagAndPriority() {
		initialiseTaskTree();

		populateSampleTasks();

		FLAG_TYPE searchFlag;
		FLAG_TYPE lowerBoundFlag;
		FLAG_TYPE upperBoundFlag;

		PRIORITY_TYPE searchPriority;
		ArrayList<Task> returnList;

		// Get done flag
		searchFlag = Task.FLAG_TYPE.DONE;
		returnList = new ArrayList<Task>(_taskTree.searchFlag(searchFlag));
		assertEquals(returnList.size(), 1);

		// Get done flag
		searchFlag = Task.FLAG_TYPE.NULL;
		returnList = new ArrayList<Task>(_taskTree.searchFlag(searchFlag));
		assertEquals(returnList.size(), 4);

		// Query done flag
		lowerBoundFlag = Task.FLAG_TYPE.NULL;
		upperBoundFlag = Task.FLAG_TYPE.DONE;
		returnList = new ArrayList<Task>(_taskTree.queryFlag(lowerBoundFlag, upperBoundFlag));
		assertEquals(returnList.size(), 5);

		// Query done flag
		searchPriority = Task.PRIORITY_TYPE.HIGH;
		returnList = new ArrayList<Task>(_taskTree.queryPriority(searchPriority, searchPriority));
		assertEquals(returnList.size(), 1);

		// Query priority normal
		searchPriority = Task.PRIORITY_TYPE.NORMAL;
		returnList = new ArrayList<Task>(_taskTree.searchPriority(searchPriority));
		assertEquals(returnList.size(), 3);

		deinitialiseTaskTree();
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