package tds;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayList;

import tds.comparators.*;
import tds.TaskAttributeConstants;

/**
 * Provides methods for storing and manipulating {@code Task} via
 * {@code TreeSet}.
 * 
 * @see TaskCollection
 * @see Task
 * @see TaskAttributeConstants
 * 
 * @author amoshydra
 */
public class TaskTree implements TaskCollection<Task> {

	private static final int TASK_NAME_TREE = TaskAttributeConstants.NAME;
	private static final int TASK_START_TIME_TREE = TaskAttributeConstants.START_TIME;
	private static final int TASK_END_TIME_TREE = TaskAttributeConstants.END_TIME;
	private static final int TASK_FLAG_TREE = TaskAttributeConstants.FLAG;
	private static final int TASK_PRIORITY_TREE = TaskAttributeConstants.PRIORITY;
	private static final int TASK_CREATE_TIME_TREE = TaskAttributeConstants.ID;
	private static final int SIZE_OF_TASK_TREES = 6;

	private ArrayList<TreeSet<Task>> taskTrees;
	private int taskTreeSize;
	private Task fromValueHandler;
	private Task toValueHandler;

	/**
	 * Constructs new, empty tree set, sorted according to the ordering of each
	 * attributes in the task.
	 */
	public TaskTree() {
		taskTreeSize = 0;
		taskTrees = new ArrayList<TreeSet<Task>>(SIZE_OF_TASK_TREES);

		taskTrees.add(TASK_NAME_TREE, new TreeSet<Task>(new NameComparator()));
		taskTrees.add(TASK_START_TIME_TREE, new TreeSet<Task>(new StartTimeComparator()));
		taskTrees.add(TASK_END_TIME_TREE, new TreeSet<Task>(new EndTimeComparator()));
		taskTrees.add(TASK_FLAG_TREE, new TreeSet<Task>(new FlagComparator()));
		taskTrees.add(TASK_PRIORITY_TREE, new TreeSet<Task>(new PriorityComparator()));
		taskTrees.add(TASK_CREATE_TIME_TREE, new TreeSet<Task>(new IdComparator()));

		fromValueHandler = new Task("");
		toValueHandler = new Task("");
	}

	/**
	 * Constructs new tree sets containing the elements in the specified
	 * collection, sorted according according to the ordering of each attributes
	 * in the task.
	 * 
	 * @param collection
	 *            whose elements will comprise the new set
	 */
	public TaskTree(Collection<Task> collection) {
		this();
		for (TreeSet<Task> tree : taskTrees) {
			tree.addAll(collection);
		}
	}

	@Override
	public boolean add(Task task) {
		boolean isAdded = true;
		for (TreeSet<Task> tree : taskTrees) {
			if (!tree.add(task)) {
				isAdded = false;
			}
		}
		increaseTaskListSize();
		return isAdded;
	}

	@Override
	public boolean remove(Task task) {
		boolean isRemoved = true;
		for (TreeSet<Task> tree : taskTrees) {
			if (!tree.remove(task)) {
				isRemoved = false;
			}
		}
		decreaseTaskListSize();
		return isRemoved;
	}

	@Override
	public boolean replace(Task oldTask, Task newTask) {

		boolean[] checkBits = oldTask.getAttributesDiff(newTask);
		boolean isReplaced = true;

		// Re-insert task based on its modified attributes
		for (int i = 0; i < SIZE_OF_TASK_TREES; i++) {
			if (checkBits[i] == false) {
				isReplaced &= updateAttributeTree(oldTask, newTask, i);
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
	 * @return true if this collection contained the task and can be modified
	 */
	public boolean updateName(Task task, String newValue) {
		task.setName(newValue);
		return updateAttributeTree(task, task, TaskAttributeConstants.NAME);
	}

	/**
	 * Update an element from this collection with the given new start time
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @return true if this collection contained the task and can be modified
	 */
	public boolean updateStartTime(Task task, long newValue) {
		task.setStartTime(newValue);
		return updateAttributeTree(task, task, TaskAttributeConstants.START_TIME);
	}

	/**
	 * Update an element from this collection with the given new end time
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @return true if this collection contained the task and can be modified
	 */
	public boolean updateEndTime(Task task, long newValue) {
		task.setEndTime(newValue);
		return updateAttributeTree(task, task, TaskAttributeConstants.END_TIME);
	}

	/**
	 * Update an element from this collection with the given new flag
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @return true if this collection contained the task and can be modified
	 */
	public boolean updateFlag(Task task, int newValue) {
		task.setFlag(newValue);
		return updateAttributeTree(task, task, TaskAttributeConstants.FLAG);
	}

	/**
	 * Update an element from this collection with the given new priority
	 * 
	 * @param task
	 *            to be modified from this collection
	 * @return true if this collection contained the task and can be modified
	 */
	public boolean updatePriority(Task task, int newValue) {
		task.setPriority(newValue);
		return updateAttributeTree(task, task, TaskAttributeConstants.PRIORITY);
	}

	private boolean updateAttributeTree(Task oldTask, Task newTask, int taskAttributeType) {
		boolean isReplaced = true;

		isReplaced &= taskTrees.get(taskAttributeType).remove(oldTask);
		isReplaced &= taskTrees.get(taskAttributeType).add(newTask);

		return isReplaced;
	}

	@Override
	public List<Task> searchName(String searchTerm) {
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

	private boolean checkLowercase(String text) {
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

	@Override
	public List<Task> queryStartTime(long fromStartTime, long toStartTime) {
		return query(TASK_START_TIME_TREE, fromStartTime, toStartTime);
	}

	@Override
	public List<Task> queryEndTime(long fromEndTime, long toEndTime) {
		return query(TASK_END_TIME_TREE, fromEndTime, toEndTime);
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
	public List<Task> queryFlag(int fromFlag, int toFlag) {
		return query(TASK_FLAG_TREE, fromFlag, toFlag);
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
	public List<Task> queryPriority(int fromPriority, int toPriority) {
		return query(TASK_PRIORITY_TREE, fromPriority, toPriority);
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
	 *            the attribute type to be sorted with.
	 * @param fromValueL
	 *            low endpoint (inclusive) of the returned list
	 * @param toValueL
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this collection whose elements range
	 *         from {@code fromValueL}, inclusive, to {@code toValueL},
	 *         inclusive
	 */
	public List<Task> query(int taskAttributeType, long fromValueL, long toValueL) {

		TreeSet<Task> taskTree = taskTrees.get(taskAttributeType);
		ArrayList<Task> emptyList = new ArrayList<Task>();
		boolean isToInclusive;
		Task toValueHdlBuffer;

		if (toValueL < fromValueL) {
			return emptyList;
		} else {
			if (toValueL == fromValueL) {
				toValueL += 1;
				toValueHdlBuffer = toValueHandler;
				isToInclusive = false;
			} else {
				toValueHdlBuffer = new Task("");
				isToInclusive = true;
			}
			switch (taskAttributeType) {
			case TASK_END_TIME_TREE:
				fromValueHandler.setEndTime(fromValueL);
				toValueHdlBuffer.setEndTime(toValueL);
				break;
			case TASK_START_TIME_TREE:
				fromValueHandler.setStartTime(fromValueL);
				toValueHdlBuffer.setStartTime(toValueL);
				break;
			case TASK_PRIORITY_TREE:
				fromValueHandler.setPriority((int) fromValueL);
				toValueHdlBuffer.setPriority((int) toValueL);
				break;
			case TASK_FLAG_TREE:
				fromValueHandler.setFlag((int) fromValueL);
				toValueHdlBuffer.setFlag((int) toValueL);
				break;
			default:
				return emptyList;
			}
			return new ArrayList<Task>(taskTree.subSet(fromValueHandler, true, toValueHdlBuffer, isToInclusive));
		}
	}

	@Override
	public List<Task> searchFlag(int flagSearch) {
		return query(TASK_FLAG_TREE, flagSearch, flagSearch);
	}

	@Override
	public List<Task> searchPriority(int prioritySearch) {
		return query(TASK_PRIORITY_TREE, prioritySearch, prioritySearch);
	}

	public List<Task> getList() {
		return getSortedList(taskTrees.get(TaskAttributeConstants.ID));
	}

	public List<Task> getSortedList(int taskAttributeType) {
		return getSortedList(taskTrees.get(taskAttributeType));
	}

	private List<Task> getSortedList(TreeSet<Task> taskTree) {
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

	@Override
	public String toString() {
		return toString(TaskAttributeConstants.ID);
	}

	/**
	 * Return a string representation of this task tree in a list given the
	 * specific {@code taskAttributeType}.
	 * 
	 * @param taskAttributeType
	 *            the attribute type to be sorted with.
	 * @return a string representation of this task tree in a list.
	 */
	public String toString(int taskAttributeType) {
		String buffer = "";
		ArrayList<Task> resultList = new ArrayList<Task>(getSortedList(taskAttributeType));
		for (Task task : resultList) {
			buffer += "" + task + "\n";
		}
		return buffer;
	}

	@Override
	public int size() {
		return taskTreeSize;
	}

	private void increaseTaskListSize() {
		taskTreeSize += 1;
	}

	private void decreaseTaskListSize() {
		taskTreeSize -= 1;
	}
}
