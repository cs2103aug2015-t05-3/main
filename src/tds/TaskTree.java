package tds;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import fileProcessor.TaskFileHandler;

import java.util.ArrayList;
import tds.comparators.*;
import tds.Task.FLAG_TYPE;
import tds.Task.PRIORITY_TYPE;
import tds.Attributes;
import tds.Attributes.TYPE;

/**
 * Provides methods for storing and manipulating {@code Task} via
 * {@code TreeSet}.
 * 
 * @see Task
 * @see Attributes
 * 
 * @author amoshydra
 */
public class TaskTree {

	private static final int TASK_NAME_TREE = Attributes.TYPE.NAME.getValue();
	private static final int TASK_START_TIME_TREE = Attributes.TYPE.START_TIME.getValue();
	private static final int TASK_END_TIME_TREE = Attributes.TYPE.END_TIME.getValue();
	private static final int TASK_FLAG_TREE = Attributes.TYPE.FLAG.getValue();
	private static final int TASK_PRIORITY_TREE = Attributes.TYPE.PRIORITY.getValue();
	private static final int TASK_CREATE_TIME_TREE = Attributes.TYPE.ID.getValue();
	private static final int SIZE_OF_TASK_TREES = Attributes.NUM_OF_ATTRIBUTES;

	private static final String TASK_FILENAME = "tasks.xml";
	
	static TaskFileHandler fileHandler;
	private static ArrayList<TreeSet<Task>> taskTrees;
	private static int taskTreeSize;

	// For managing comparable argument during query
	private static Task fromValueHandler;

	private static final String TO_STRING_OPEN = "[";
	private static final String TO_STRING_CLOSE = "]";
	private static final String TO_STRING_DELIMETER = ",";

	// Prevent instantiation of this constructor 
	private TaskTree() {}
	
	/**
	 * Initialize a new, empty tree set, sorted according to the ordering of
	 * each attributes in the task.
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

		// Fill TaskTree from file storage
		pullFromStorage();
	}

	/**
	 * Initialize new tree sets containing the {@code Task} objects in the
	 * specified {@code TaskTree}, sorted according according to the ordering of
	 * each attributes in the task.
	 * 
	 * @param collection
	 *            whose {@code Task} objects will comprise the new set
	 */
	public static void init(Collection<Task> collection) {
		init();
		for (TreeSet<Task> tree : taskTrees) {
			tree.addAll(collection);
		}
	}

	/**
	 * Adds the specified {@code Task} object to this {@code TaskTree}
	 * 
	 * @param task
	 *            to be added to this {@code TaskTree}
	 * @return true if this {@code TaskTree} did not already contain the
	 *         specified {@code Task} object
	 */
	public static boolean add(Task task) {
		boolean isAdded = true;
		for (TreeSet<Task> tree : taskTrees) {
			if (!tree.add(task)) {
				isAdded = false;
			}
		}
		increaseTaskListSize();
		pushAddToStorage(task);
		return isAdded;
	}

	/**
	 * Removes the specified {@code Task} object to this {@code TaskTree}
	 * 
	 * @param task
	 *            to be removed from this {@code TaskTree}
	 * @return true if this {@code TaskTree} contained the specified
	 *         {@code Task} object
	 */
	public static boolean remove(Task task) {
		boolean isRemoved = true;
		for (TreeSet<Task> tree : taskTrees) {
			if (!tree.remove(task)) {
				isRemoved = false;
			}
		}
		decreaseTaskListSize();
		pushRemoveToStorage(task);
		return isRemoved;
	}

	/**
	 * Replace an old {@code Task} object from this {@code TaskTree} with a new
	 * {@code Task} object. The new {@code Task} object will be treated as a
	 * newly created task.
	 * 
	 * @param oldTask
	 *            to be replaced from this {@code TaskTree}
	 * @param newTask
	 *            to replace the old {@code Task} object
	 * 
	 * @return true if this {@code TaskTree} contained the old {@code Task}
	 *         object and can be replaced by the new {@code Task} object
	 */
	public static boolean replace(Task oldTask, Task newTask) {

		boolean[] checkBits = oldTask.getAttributesDiff(newTask);
		boolean isReplaced = true;

		// Re-insert task based on its modified attributes
		for (int i = 0; i < SIZE_OF_TASK_TREES; i++) {
			if (checkBits[i] == false) {
				isReplaced &= updateAttributeTree(oldTask, newTask, TYPE.get(i));
			}
		}
		// Replace old task with new task for the remaining tree;
		oldTask = newTask;

		return isReplaced;
	}

	/**
	 * Update an {@code Task} object from this {@code TaskTree} with the given
	 * new name or description
	 * 
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public static boolean updateName(Task task, String newValue) {
		task.setName(newValue);
		return updateAttributeTree(task, task, TYPE.NAME);
	}

	/**
	 * Update an {@code Task} object from this {@code TaskTree} with the given
	 * new start time
	 * 
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public static boolean updateStartTime(Task task, long newValue) {
		task.setStartTime(newValue);
		return updateAttributeTree(task, task, TYPE.START_TIME);
	}

	/**
	 * Update an {@code Task} object from this {@code TaskTree} with the given
	 * new end time
	 * 
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public static boolean updateEndTime(Task task, long newValue) {
		task.setEndTime(newValue);
		return updateAttributeTree(task, task, TYPE.END_TIME);
	}

	/**
	 * Update an {@code Task} object from this {@code TaskTree} with the given
	 * new flag
	 * 
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public static boolean updateFlag(Task task, FLAG_TYPE newValue) {
		task.setFlag(newValue);
		return updateAttributeTree(task, task, TYPE.FLAG);
	}

	/**
	 * Update an {@code Task} object from this {@code TaskTree} with the given
	 * new priority
	 * 
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public static boolean updatePriority(Task task, PRIORITY_TYPE newValue) {
		task.setPriority(newValue);
		return updateAttributeTree(task, task, TYPE.PRIORITY);
	}

	private static boolean updateAttributeTree(Task oldTask, Task newTask, TYPE taskAttributeType) {
		boolean isReplaced = true;

		int treeType = taskAttributeType.getValue();

		isReplaced &= taskTrees.get(treeType).remove(oldTask);
		isReplaced &= taskTrees.get(treeType).add(newTask);

		pushUpdateToStorage(oldTask, newTask);
		return isReplaced;
	}

	/**
	 * Returns a view of the portion of this {@code TaskTree} whose {@code Task}
	 * objects contain the {@code searchTerm}. The returned {@code List} is
	 * backed by this {@code TaskTree}, so changes in the returned {@code List}
	 * are reflected in this {@code TaskTree}, and vice-versa.
	 *
	 * @param searchTerm
	 *            the sequence to search for
	 * @return a view of the portion of this {@code TaskTree} whose {@code Task}
	 *         object contain the {@code searchTerm}
	 */
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

	/**
	 * Returns a view of the portion of this {@code TaskTree} whose {@code Task}
	 * objects range from {@code fromStartTime}, inclusive, to
	 * {@code toStartTime}, inclusive. (If {@code fromStartTime} and
	 * {@code toStartTime} are equal, the returned {@code List} contains
	 * {@code Task} object that matches {@code fromEndTime}.) The returned
	 * {@code List} is backed by this {@code TaskTree}, so changes in the
	 * returned {@code List} are reflected in this {@code TaskTree}, and
	 * vice-versa.
	 * 
	 * @param fromStartTime
	 *            low endpoint (inclusive) of the returned list
	 * @param toStartTime
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this {@code TaskTree} whose {@code Task}
	 *         objects range from {@code fromStartTime}, inclusive, to
	 *         {@code toStartTime}, inclusive
	 */
	public static List<Task> queryStartTime(long fromStartTime, long toStartTime) {
		return query(TYPE.START_TIME, fromStartTime, toStartTime);
	}

	/**
	 * Returns a view of the portion of this {@code TaskTree} whose {@code Task}
	 * objects range from {@code fromEndTime}, inclusive, to {@code toEnd}Time,
	 * inclusive. (If {@code fromEndTime} and {@code toEndTime} are equal, the
	 * returned {@code List} contains {@code Task} object that matches
	 * {@code fromEndTime} only.) The returned {@code List} is backed by this
	 * {@code TaskTree}, so changes in the returned {@code List} are reflected
	 * in this {@code TaskTree}, and vice-versa.
	 * 
	 * @param fromEndTime
	 *            low endpoint (inclusive) of the returned list
	 * @param toEndTime
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this {@code TaskTree} whose {@code Task}
	 *         objects range from {@code fromEndTime}, inclusive, to
	 *         {@code toEndTime}, inclusive
	 */
	public static List<Task> queryEndTime(long fromEndTime, long toEndTime) {
		return query(TYPE.END_TIME, fromEndTime, toEndTime);
	}

	/**
	 * Returns a view of the portion of this {@code TaskTree} whose {@code Task}
	 * objects range from {@code fromFlag}, inclusive, to {@code toFlag},
	 * inclusive. (If {@code fromFlag} and {@code toFlag} are equal, the
	 * returned {@code List} contains {@code Task} object that matches
	 * {@code fromFlag} only.) The returned {@code List} is backed by this
	 * {@code TaskTree}, so changes in the returned {@code List} are reflected
	 * in this {@code TaskTree}, and vice-versa.
	 * 
	 * @param fromFlag
	 *            low endpoint (inclusive) of the returned list
	 * @param toFlag
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this {@code TaskTree} whose {@code Task}
	 *         objects range from {@code fromFlag}, inclusive, to {@code toFlag}
	 *         , inclusive
	 */
	public static List<Task> queryFlag(FLAG_TYPE fromFlag, FLAG_TYPE toFlag) {

		int fromValue = fromFlag.getValue();
		int toValue = toFlag.getValue();

		return query(TYPE.FLAG, fromValue, toValue);
	}

	/**
	 * Returns a view of the portion of this {@code TaskTree} whose {@code Task}
	 * objects range from {@code fromPriority}, inclusive, to {@code toPriority}
	 * , inclusive. (If {@code fromPriority} and {@code toPriority} are equal,
	 * the returned {@code List} contains {@code Task} object that matches
	 * {@code fromPriority} only.) The returned {@code List} is backed by this
	 * {@code TaskTree}, so changes in the returned {@code List} are reflected
	 * in this {@code TaskTree}, and vice-versa.
	 * 
	 * @param fromPriority
	 *            low endpoint (inclusive) of the returned list
	 * @param toPriority
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this {@code TaskTree} whose {@code Task}
	 *         objects range from {@code fromPriority}, inclusive, to
	 *         {@code toPriority}, inclusive
	 */
	public static List<Task> queryPriority(PRIORITY_TYPE fromPriority, PRIORITY_TYPE toPriority) {

		int fromValue = fromPriority.getValue();
		int toValue = toPriority.getValue();

		return query(TYPE.PRIORITY, fromValue, toValue);
	}

	/**
	 * Returns a view of the portion of this {@code TaskTree} whose {@code Task}
	 * objects range from {@code fromValueL}, inclusive, to {@code toValueL},
	 * inclusive given the specified {@code taskAttributeType}. (If
	 * {@code fromValueL} and {@code toValueL} are equal, the returned
	 * {@code List} contains {@code Task} object that matches {@code fromValueL}
	 * only.) The returned {@code List} is backed by this {@code TaskTree}, so
	 * changes in the returned {@code List} are reflected in this
	 * {@code TaskTree}, and vice-versa. To query flag and priority, please use
	 * its respective query method.
	 * 
	 * @param taskAttributeType
	 *            the attribute type to be query with.
	 * @param fromValueL
	 *            low endpoint (inclusive) of the returned list
	 * @param toValueL
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this {@code TaskTree} whose {@code Task}
	 *         objects range from {@code fromValueL}, inclusive, to
	 *         {@code toValueL}, inclusive
	 */
	public static List<Task> query(TYPE taskAttributeType, long fromValueL, long toValueL) {

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

	/**
	 * Returns a view of the portion of this {@code TaskTree} whose {@code Task}
	 * objects matches the {@code flagSearch}. The returned {@code List} is
	 * backed by this {@code TaskTree}, so changes in the returned {@code List}
	 * are reflected in this {@code TaskTree}, and vice-versa.
	 *
	 * @param type
	 *            to search for
	 * @return a view of the portion of this {@code TaskTree} whose {@code Task}
	 *         objects match the {@code flagSearch}
	 * @see tds.Task
	 * 
	 */
	public static List<Task> searchFlag(FLAG_TYPE type) {
		int flagValue = type.getValue();

		return query(TYPE.FLAG, flagValue, flagValue);
	}

	/**
	 * Returns a view of the portion of this {@code TaskTree} whose {@code Task}
	 * objects matches the {@code prioritySearch}. The returned {@code List} is
	 * backed by this {@code TaskTree}, so changes in the returned {@code List}
	 * are reflected in this {@code TaskTree}, and vice-versa.
	 *
	 * @param type
	 *            to search for
	 * @return a view of the portion of this {@code TaskTree} whose {@code Task}
	 *         objects match the {@code prioritySearch}
	 * @see tds.Task
	 * 
	 */
	public static List<Task> searchPriority(PRIORITY_TYPE type) {
		int priorityValue = type.getValue();

		return query(TYPE.PRIORITY, priorityValue, priorityValue);
	}

	/**
	 * Returns a view of this {@code TaskTree} whose {@code Task} objects are
	 * sorted according to order it is created. The returned {@code List} is
	 * backed by this {@code TaskTree}, so changes in the returned {@code List}
	 * are reflected in this {@code TaskTree}, and vice-versa.
	 *
	 * @return a view of this {@code TaskTree}.
	 */
	public static List<Task> getList() {
		int treeType = Attributes.TYPE.ID.getValue();
		return getSortedList(taskTrees.get(treeType));
	}

	/**
	 * Returns a view of this {@code TaskTree} whose {@code Task} objects are
	 * sorted according to its specified attribute type. The returned
	 * {@code List} is backed by this {@code TaskTree}, so changes in the
	 * returned {@code List} are reflected in this {@code TaskTree}, and
	 * vice-versa.
	 *
	 * @param taskAttributeType
	 *            the attribute type to be sorted with.
	 * @return a view of this {@code TaskTree} whose {@code Task} objects are
	 *         sorted according to its specified attribute type.
	 * @see tds.Attributes
	 */
	public static List<Task> getSortedList(TYPE taskAttributeType) {

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
	 * Returns a string representation of this {@code TaskTree}. The string
	 * representation consists of a list of the {@code TaskTree}'s {@code Task}
	 * objects in the order they are returned by its iterator, enclosed in
	 * square brackets ("[]"). Adjacent {@code Task} objects are separated by
	 * the characters ", " (comma and space). {@code Task} objects are converted
	 * to strings as by String.toString(Object).
	 * 
	 * @return a string representation of this task tree in a list.
	 * 
	 */
	public static String getString() {
		return getString(Attributes.TYPE.ID);
	}

	/**
	 * Return a string representation of this {@code TaskTree} sorted in order
	 * of the specified {@code taskAttributeType}. The string representation
	 * consists of a list of the {@code TaskTree}'s {@code Task} objects in the
	 * order they are returned by its iterator, enclosed in square brackets
	 * ("[]"). Adjacent {@code Task} objects are separated by the characters
	 * ", " (comma and space). {@code Task} objects are converted to strings as
	 * by String.toString(Object).
	 * 
	 * @param taskAttributeType
	 *            the attribute type to be printed.
	 * @return a string representation of this task tree in a list.
	 */

	public static String getString(TYPE taskAttributeType) {

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

	private static void pushAddToStorage(Task task) {
		try {
			fileHandler.add(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void pushRemoveToStorage(Task task) {
		int taskId = task.getId();
		try {
			fileHandler.delete(taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void pushUpdateToStorage(Task oldTask, Task newTask) {
		//TODO wait for implementation of update
	}
	
	/**
	 * To retrieve task list the Task file. This method is called upon the
	 * starting of this program.
	 */
	private static void pullFromStorage() {
		
		ArrayList <Task> taskList;
		try {
			fileHandler = new TaskFileHandler(TASK_FILENAME);
			taskList = fileHandler.retrieveTaskList();
			for (TreeSet<Task> tree : taskTrees) {
				tree.addAll(taskList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
