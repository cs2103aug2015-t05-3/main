package tds;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayList;
import tds.comparators.*;
import tds.Task.FLAG_TYPE;
import tds.Task.PRIORITY_TYPE;
import tds.TaskAttributeConstants;
import tds.TaskAttributeConstants.TREE_TYPE;

/**
 * Provides methods for storing and manipulating {@code Task} via
 * {@code TreeSet}.
 * 
 * @see Task
 * @see TaskAttributeConstants
 * 
 * @author amoshydra
 */
public class TaskTree {

	private static final int TASK_NAME_TREE = TaskAttributeConstants.NAME;
	private static final int TASK_START_TIME_TREE = TaskAttributeConstants.START_TIME;
	private static final int TASK_END_TIME_TREE = TaskAttributeConstants.END_TIME;
	private static final int TASK_FLAG_TREE = TaskAttributeConstants.FLAG;
	private static final int TASK_PRIORITY_TREE = TaskAttributeConstants.PRIORITY;
	private static final int TASK_CREATE_TIME_TREE = TaskAttributeConstants.ID;
	private static final int SIZE_OF_TASK_TREES = 6;

	private static ArrayList<TreeSet<Task>> taskTrees;
	private static int taskTreeSize;
	private static Task fromValueHandler;

	private static final String TO_STRING_OPEN = "[";
	private static final String TO_STRING_CLOSE = "]";
	private static final String TO_STRING_DELIMETER = ",";

	/**
	 * Constructs new, empty tree set, sorted according to the ordering of each
	 * attributes in the task.
	 */
	public static void init() {
		taskTreeSize = 0;
		taskTrees = new ArrayList<TreeSet<Task>>(SIZE_OF_TASK_TREES);

		taskTrees.add(TASK_NAME_TREE, new TreeSet<Task>(new NameComparator()));
		taskTrees.add(TASK_START_TIME_TREE, new TreeSet<Task>(new StartTimeComparator()));
		taskTrees.add(TASK_END_TIME_TREE, new TreeSet<Task>(new EndTimeComparator()));
		taskTrees.add(TASK_FLAG_TREE, new TreeSet<Task>(new FlagComparator()));
		taskTrees.add(TASK_PRIORITY_TREE, new TreeSet<Task>(new PriorityComparator()));
		taskTrees.add(TASK_CREATE_TIME_TREE, new TreeSet<Task>(new IdComparator()));

		fromValueHandler = new Task("");
	}

	/**
	 * Constructs new tree sets containing the elements in the specified
	 * collection, sorted according according to the ordering of each attributes
	 * in the task.
	 * 
	 * @param collection
	 *            whose elements will comprise the new set
	 */
	public static void init(Collection<Task> collection) {
		init();
		for (TreeSet<Task> tree : taskTrees) {
			tree.addAll(collection);
		}
	}

	
	public static boolean add(Task task) {
		boolean isAdded = true;
		for (TreeSet<Task> tree : taskTrees) {
			if (!tree.add(task)) {
				isAdded = false;
			}
		}
		increaseTaskListSize();
		return isAdded;
	}

	
	public static boolean remove(Task task) {
		boolean isRemoved = true;
		for (TreeSet<Task> tree : taskTrees) {
			if (!tree.remove(task)) {
				isRemoved = false;
			}
		}
		decreaseTaskListSize();
		return isRemoved;
	}

	
	public static boolean replace(Task oldTask, Task newTask) {

		boolean[] checkBits = oldTask.getAttributesDiff(newTask);
		boolean isReplaced = true;

		// Re-insert task based on its modified attributes
		for (int i = 0; i < SIZE_OF_TASK_TREES; i++) {
			if (checkBits[i] == false) {
				isReplaced &= updateAttributeTree(oldTask, newTask, TREE_TYPE.get(i));
			}
		}
		// Replace old task with new task for the remaining tree;
		oldTask = newTask;

		return isReplaced;
	}

	/**
	 * Update an element from this collection with the given new name or
	 * description
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @param newValue
	 *            to update this task
	 * @return true if this collection contained the task and can be modified
	 */
	public static boolean updateName(Task task, String newValue) {
		task.setName(newValue);
		return updateAttributeTree(task, task, TREE_TYPE.NAME);
	}

	/**
	 * Update an element from this collection with the given new start time
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @param newValue
	 *            to update this task
	 * @return true if this collection contained the task and can be modified
	 */
	public static boolean updateStartTime(Task task, long newValue) {
		task.setStartTime(newValue);
		return updateAttributeTree(task, task, TREE_TYPE.START_TIME);
	}

	/**
	 * Update an element from this collection with the given new end time
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @param newValue
	 *            to update this task
	 * @return true if this collection contained the task and can be modified
	 */
	public static boolean updateEndTime(Task task, long newValue) {
		task.setEndTime(newValue);
		return updateAttributeTree(task, task, TREE_TYPE.END_TIME);
	}

	/**
	 * Update an element from this collection with the given new flag
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @param newValue
	 *            to update this task
	 * @return true if this collection contained the task and can be modified
	 */
	public static boolean updateFlag(Task task, FLAG_TYPE newValue) {
		task.setFlag(newValue);
		return updateAttributeTree(task, task, TREE_TYPE.FLAG);
	}

	/**
	 * Update an element from this collection with the given new priority
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @param newValue
	 *            to update this task
	 * @return true if this collection contained the task and can be modified
	 */
	public static boolean updatePriority(Task task, PRIORITY_TYPE newValue) {
		task.setPriority(newValue);
		return updateAttributeTree(task, task, TREE_TYPE.PRIORITY);
	}

	private static boolean updateAttributeTree(Task oldTask, Task newTask, TREE_TYPE taskAttributeType) {
		boolean isReplaced = true;

		int treeType = taskAttributeType.getValue();
		
		isReplaced &= taskTrees.get(treeType).remove(oldTask);
		isReplaced &= taskTrees.get(treeType).add(newTask);

		return isReplaced;
	}

	
	public static List<Task> searchName(String searchTerm) {
		ArrayList<Task> resultList = new ArrayList<Task>(taskTreeSize);

		boolean isCaseInsensitive = checkLowercase(searchTerm);
		String checkString;

		for (Task task : taskTrees.get(TASK_NAME_TREE)) {

			checkString = task.getName();
			if (isCaseInsensitive) {
				checkString = checkString.toLowerCase();
			}

			if (checkString.contains(searchTerm)) {
				resultList.add(task);
			}
		}
		return resultList;
	}

	private static boolean checkLowercase(String text) {
		int textLength = text.length();
		char charInText;

		for (int i = 0; i < textLength; i++) {
			charInText = text.charAt(i);
			if (charInText >= 'A' && charInText <= 'Z') {
				return false;
			}
		}
		return true;
	}

	
	public static List<Task> queryStartTime(long fromStartTime, long toStartTime) {
		return query(TREE_TYPE.START_TIME, fromStartTime, toStartTime);
	}

	
	public static List<Task> queryEndTime(long fromEndTime, long toEndTime) {
		return query(TREE_TYPE.END_TIME, fromEndTime, toEndTime);
	}

	/**
	 * Returns a view of the portion of this collection whose elements range
	 * from {@code fromFlag}, inclusive, to {@code toFlag}, inclusive. (If
	 * {@code fromFlag} and {@code toFlag} are equal, the returned {@code List}
	 * contains element that matches {@code fromFlag} only.) The returned
	 * {@code List} is backed by this collection, so changes in the returned
	 * {@code List} are reflected in this collection, and vice-versa.
	 * 
	 * @param fromFlag
	 *            low endpoint (inclusive) of the returned list
	 * @param toFlag
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this collection whose elements range
	 *         from {@code fromFlag}, inclusive, to {@code toFlag}, inclusive
	 */
	public static List<Task> queryFlag(FLAG_TYPE fromFlag, FLAG_TYPE toFlag) {
		
		int fromValue = fromFlag.getValue();
		int toValue = toFlag.getValue();
		
		return query(TREE_TYPE.FLAG, fromValue, toValue);
	}

	/**
	 * Returns a view of the portion of this collection whose elements range
	 * from {@code fromPriority}, inclusive, to {@code toPriority}, inclusive.
	 * (If {@code fromPriority} and {@code toPriority} are equal, the returned
	 * {@code List} contains element that matches {@code fromPriority} only.)
	 * The returned {@code List} is backed by this collection, so changes in the
	 * returned {@code List} are reflected in this collection, and vice-versa.
	 * 
	 * @param fromPriority
	 *            low endpoint (inclusive) of the returned list
	 * @param toPriority
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this collection whose elements range
	 *         from {@code fromPriority}, inclusive, to {@code toPriority},
	 *         inclusive
	 */
	public static List<Task> queryPriority(PRIORITY_TYPE fromPriority, PRIORITY_TYPE toPriority) {
		
		int fromValue = fromPriority.getValue();
		int toValue = toPriority.getValue();
		
		return query(TREE_TYPE.PRIORITY, fromValue, toValue);
	}

	/**
	 * Returns a view of the portion of this collection whose elements range
	 * from {@code fromValueL}, inclusive, to {@code toValueL}, inclusive given
	 * the specified {@code taskAttributeType}. (If {@code fromValueL} and
	 * {@code toValueL} are equal, the returned {@code List} contains element
	 * that matches {@code fromValueL} only.) The returned {@code List} is
	 * backed by this collection, so changes in the returned {@code List} are
	 * reflected in this collection, and vice-versa.
	 * 
	 * @param taskAttributeType
	 *            the attribute type to be query with.
	 * @param fromValueL
	 *            low endpoint (inclusive) of the returned list
	 * @param toValueL
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this collection whose elements range
	 *         from {@code fromValueL}, inclusive, to {@code toValueL},
	 *         inclusive
	 */
	public static List<Task> query(TREE_TYPE taskAttributeType, long fromValueL, long toValueL) {

		int treeType = taskAttributeType.getValue();
		
		TreeSet<Task> taskTree = taskTrees.get(treeType);
		ArrayList<Task> emptyList = new ArrayList<Task>();
		boolean isToInclusive;
		Task toValueHdlBuffer;

		if (toValueL < fromValueL) {
			return emptyList;
		} else {
			toValueHdlBuffer = new Task("");
			if (toValueL == fromValueL) {
				isToInclusive = false;
			} else {
				isToInclusive = true;
			}
			switch (taskAttributeType) {
			case END_TIME:
				fromValueHandler.setEndTime(fromValueL);
				toValueHdlBuffer.setEndTime(toValueL);
				break;
			case START_TIME:
				fromValueHandler.setStartTime(fromValueL);
				toValueHdlBuffer.setStartTime(toValueL);
				break;
			case PRIORITY:
				fromValueHandler.setPriority(PRIORITY_TYPE.get((int) fromValueL));
				toValueHdlBuffer.setPriority(PRIORITY_TYPE.get((int) toValueL));
				break;
			case FLAG:
				fromValueHandler.setFlag(FLAG_TYPE.get((int) fromValueL));
				toValueHdlBuffer.setFlag(FLAG_TYPE.get((int) toValueL));
				break;
			default:
				return emptyList;
			}
			return new ArrayList<Task>(taskTree.subSet(fromValueHandler, true, toValueHdlBuffer, isToInclusive));
		}
	}
	
	
	public static List<Task> searchFlag(FLAG_TYPE type) {
		int flagValue = type.getValue();
		
		return query(TREE_TYPE.FLAG, flagValue, flagValue);
	}

	
	public static List<Task> searchPriority(PRIORITY_TYPE type) {
		int priorityValue = type.getValue();
		
		return query(TREE_TYPE.PRIORITY, priorityValue, priorityValue);
	}

	public static List<Task> getList() {
		return getSortedList(taskTrees.get(TaskAttributeConstants.ID));
	}

	public static List<Task> getSortedList(TREE_TYPE taskAttributeType) {
		
		return getSortedList(taskTrees.get(taskAttributeType.getValue()));
	}

	private static List<Task> getSortedList(TreeSet<Task> taskTree) {
		ArrayList<Task> resultList = new ArrayList<Task>(taskTreeSize);
		for (Task task : taskTree) {
			resultList.add(task);
		}
		return resultList;
	}

	/**
	 * Return a string representation of this task tree in a list.
	 * 
	 * @return a string representation of this task tree in a list.
	 */

	/**
	 * Returns a string representation of this collection. The string
	 * representation consists of a list of the collection's elements in the
	 * order they are returned by its iterator, enclosed in square brackets
	 * ("[]"). Adjacent elements are separated by the characters ", " (comma and
	 * space). Elements are converted to strings as by String.toString(Object).
	 */
	public static String getString() {
		return getString(TaskAttributeConstants.TREE_TYPE.ID);
	}

	/**
	 * Return a string representation of this collection sorted in order of the
	 * specified {@code taskAttributeType}. The string representation consists
	 * of a list of the collection's elements in the order they are returned by
	 * its iterator, enclosed in square brackets ("[]"). Adjacent elements are
	 * separated by the characters ", " (comma and space). Elements are
	 * converted to strings as by String.toString(Object).
	 * 
	 * @param taskAttributeType
	 *            the attribute type to be printed.
	 * @return a string representation of this task tree in a list.
	 */
	
	public static String getString(TREE_TYPE taskAttributeType) {
		
		ArrayList<Task> resultList = new ArrayList<Task>(getSortedList(taskAttributeType));
		int listSize = resultList.size();
		int lastIndex = listSize - 1;

		String buffer = TO_STRING_OPEN;
		for (int i = 0; i < lastIndex; i++) {
			buffer += resultList.get(i) + TO_STRING_DELIMETER;
		}
		buffer += resultList.get(lastIndex) + TO_STRING_CLOSE;

		return buffer;
	}

	
	public static int size() {
		return taskTreeSize;
	}

	private static void increaseTaskListSize() {
		taskTreeSize += 1;
	}

	private static void decreaseTaskListSize() {
		taskTreeSize -= 1;
	}
}
