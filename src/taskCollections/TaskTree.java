package taskCollections;

import java.util.List;
import java.util.TreeSet;

import storage.TaskFileHandler;
import taskCollections.Attributes;
import taskCollections.Attributes.TYPE;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import taskCollections.comparators.*;

import java.util.ArrayList;

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

	private static TaskTree _taskTree;
	private static ArrayList<TreeSet<Task>> _taskTrees;
	private static int _taskTreeSize;
	private static TaskFileHandler _fileHandler;

	// TaskTree attributes type
	private static final int TASK_NAME_TREE = Attributes.TYPE.NAME.getValue();
	private static final int TASK_START_TIME_TREE = Attributes.TYPE.START_TIME.getValue();
	private static final int TASK_END_TIME_TREE = Attributes.TYPE.END_TIME.getValue();
	private static final int TASK_FLAG_TREE = Attributes.TYPE.FLAG.getValue();
	private static final int TASK_PRIORITY_TREE = Attributes.TYPE.PRIORITY.getValue();
	private static final int TASK_ID_TREE = Attributes.TYPE.ID.getValue();
	private static final int SIZE_OF_TASK_TREES = Attributes.NUM_OF_ATTRIBUTES;

	// Message Constants
	private final String MSG_ERR_SEARCH_TERM_EMPTY = "Search term is empty";

	// For managing comparable argument during query
	private static Task fromValueHandler;

	private final String TO_STRING_OPEN = "[";
	private final String TO_STRING_CLOSE = "]";
	private final String TO_STRING_DELIMETER = ",";
	private final String CHECK_STRING_CONCATOR = " ";

	// Prevent instantiation of this constructor
	private TaskTree() {
	}

	// Constructor of TaskTree
	/**
	 * Initialize a new, empty tree set, sorted according to the ordering of
	 * each attributes in the task.
	 *
	 * @param taskFilePath
	 *            directed to the storage XML file for tasks.
	 *
	 */
	private static void init(String taskFilePath) {

		_taskTree = new TaskTree();

		_taskTreeSize = 0;
		_taskTrees = new ArrayList<TreeSet<Task>>(SIZE_OF_TASK_TREES);

		_taskTrees.add(TASK_NAME_TREE, new TreeSet<Task>(new NameComparator()));
		_taskTrees.add(TASK_START_TIME_TREE, new TreeSet<Task>(new StartTimeComparator()));
		_taskTrees.add(TASK_END_TIME_TREE, new TreeSet<Task>(new EndTimeComparator()));
		_taskTrees.add(TASK_FLAG_TREE, new TreeSet<Task>(new FlagComparator()));
		_taskTrees.add(TASK_PRIORITY_TREE, new TreeSet<Task>(new PriorityComparator()));
		_taskTrees.add(TASK_ID_TREE, new TreeSet<Task>(new IdComparator()));

		fromValueHandler = Task.getVirtualTask();

		// Fill TaskTree from file storage
		iniTaskFileHandler(taskFilePath);
		pullFromStorage();
	}

	/**
	 * Construct and return a new instance of {@code TaskTree}. If the taskTree
	 * has already been constructed, the previous instance is replaced.
	 *
	 * @return a new instance of {@code TaskTree}. This new {@code TaskTree}
	 *         will replace the previous instance of the TaskTree.
	 */
	public static TaskTree newTaskTree(String taskFilePath) {
		init(taskFilePath);
		return getTaskTree();
	}

	/**
	 * Return the original instance of {@code TaskTree}
	 *
	 * @return the original instance of {@code TaskTree}
	 */
	public static TaskTree getTaskTree() {
		return _taskTree;
	}

	// TaskTree Task operation: Add and Remove
	/**
	 * Adds the specified {@code Task} object to this {@code TaskTree}
	 *
	 * @param task
	 *            to be added to this {@code TaskTree}
	 * @return true if this {@code TaskTree} did not already contain the
	 *         specified {@code Task} object
	 */
	public boolean add(Task task) {

		if (task == null) {
			throw new NullPointerException();
		}

		boolean isAddedToData = true;
		boolean isAddedToFile = true;

		for (TreeSet<Task> tree : _taskTrees) {
			isAddedToData &= tree.add(task);
			assert isAddedToData;
		}

		if (isAddedToData) {
			increaseTaskListSize();
			isAddedToFile &= pushAddToStorage(task);
		}

		return isAddedToData && isAddedToFile;
	}

	/**
	 * Removes the specified {@code Task} object to this {@code TaskTree}
	 *
	 * @param task
	 *            to be removed from this {@code TaskTree}
	 * @return true if this {@code TaskTree} contained the specified
	 *         {@code Task} object
	 */
	public boolean remove(Task task) {

		if (task == null) {
			throw new NullPointerException();
		}

		boolean isRemovedFromData = true;
		boolean isRemovedFromFile = true;

		for (TreeSet<Task> tree : _taskTrees) {
			isRemovedFromData &= tree.remove(task);
			assert isRemovedFromData;
		}
		if (isRemovedFromData) {
			decreaseTaskListSize();
			isRemovedFromFile &= pushRemoveToStorage(task);
		}
		return isRemovedFromData && isRemovedFromFile;
	}

	// TaskTree Task operation: Update
	/**
	 * Update a {@code Task} object from this {@code TaskTree} with the given
	 * new name
	 *
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public boolean updateName(Task task, String newValue) {
		task.setName(newValue);
		return updateAttributeTree(task, task, TYPE.NAME);
	}

	/**
	 * Update a {@code Task} object from this {@code TaskTree} with the given
	 * new description
	 *
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public void updateDescription(Task task, String newValue) {
		task.setDescription(newValue);
	}

	/**
	 * Update a {@code Task} object from this {@code TaskTree} with the given
	 * new start time
	 *
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public boolean updateStartTime(Task task, long newValue) {
		task.setStartTime(newValue);
		return updateAttributeTree(task, task, TYPE.START_TIME);
	}

	/**
	 * Update a {@code Task} object from this {@code TaskTree} with the given
	 * new end time
	 *
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public boolean updateEndTime(Task task, long newValue) {
		task.setEndTime(newValue);
		return updateAttributeTree(task, task, TYPE.END_TIME);
	}

	/**
	 * Update a {@code Task} object from this {@code TaskTree} with the given
	 * new flag
	 *
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public boolean updateFlag(Task task, FLAG_TYPE newValue) {
		task.setFlag(newValue);
		return updateAttributeTree(task, task, TYPE.FLAG);
	}

	/**
	 * Update a {@code Task} object from this {@code TaskTree} with the given
	 * new priority
	 *
	 * @param task
	 *            to be modified from this {@code TaskTree}
	 * @param newValue
	 *            to update this task
	 * @return true if this {@code TaskTree} contained the task and can be
	 *         modified
	 */
	public boolean updatePriority(Task task, PRIORITY_TYPE newValue) {
		task.setPriority(newValue);
		return updateAttributeTree(task, task, TYPE.PRIORITY);
	}

	private boolean updateAttributeTree(Task oldTask, Task newTask, TYPE taskAttributeType) {

		int treeType = taskAttributeType.getValue();

		boolean isReplaced, isAdded, isRemoved;
		isReplaced = isAdded = isRemoved = false;

		isRemoved = _taskTrees.get(treeType).remove(oldTask);
		if (isRemoved) {
			isAdded = _taskTrees.get(treeType).add(newTask);
		}
		isReplaced = isAdded & isRemoved;

		if (isReplaced) {
			pushUpdateToStorage(oldTask, newTask);
		}
		return isReplaced;
	}

	// TaskTree Task operation: Search and query
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
	 * @throws IllegalArgumentException
	 *             if search term is empty
	 */
	public List<Task> searchName(String searchTerm) {
		ArrayList<Task> resultList = new ArrayList<Task>(_taskTreeSize);

		if (searchTerm == null) {
			throw new IllegalArgumentException(MSG_ERR_SEARCH_TERM_EMPTY);
		}

		boolean isCaseInsensitive = checkLowercase(searchTerm);
		String checkNameString, checkDescString;
		String checkString;

		for (Task task : _taskTrees.get(TASK_NAME_TREE)) {

			checkNameString = task.getName();
			checkDescString = task.getDescription();
			checkString = checkNameString + CHECK_STRING_CONCATOR + checkDescString;

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
	public List<Task> queryStartTime(long fromStartTime, long toStartTime) {
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
	public List<Task> queryEndTime(long fromEndTime, long toEndTime) {
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
	public List<Task> queryFlag(FLAG_TYPE fromFlag, FLAG_TYPE toFlag) {

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
	public List<Task> queryPriority(PRIORITY_TYPE fromPriority, PRIORITY_TYPE toPriority) {

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
	public List<Task> query(TYPE taskAttributeType, long fromValueL, long toValueL) {

		int treeType = taskAttributeType.getValue();

		TreeSet<Task> taskTree = _taskTrees.get(treeType);
		ArrayList<Task> emptyList = new ArrayList<Task>();
		boolean isToInclusive;
		Task toValueHdlBuffer;

		if (toValueL < fromValueL) {
			return emptyList;
		} else {
			toValueHdlBuffer = Task.getVirtualTask();
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
			case ID:
				fromValueHandler.setId((int) fromValueL);
				toValueHdlBuffer.setId((int) toValueL);
				break;
			default:
				return emptyList;
			}
			return new ArrayList<Task>(taskTree.subSet(fromValueHandler, true, toValueHdlBuffer, isToInclusive));
		}
	}

	/**
	 * Returns a task from this {@code TaskTree} via its id. The returned
	 * {@code Task} is backed by this {@code TaskTree}, so changes in the
	 * returned {@code Task} are reflected in this {@code TaskTree}, and
	 * vice-versa.
	 *
	 * @param id
	 *            to get
	 * @return a task from this {@code TaskTree} via its ID if found; otherwise null.
	 *
	 * @see taskCollections.Task
	 *
	 */
	public Task getTask(int id) {

		TreeSet<Task> idTree = _taskTrees.get(TASK_ID_TREE);

		for (Task task : idTree) {
			if (task.getId() == id)
				return task;
		}
		return null;
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
	 * @see taskCollections.Task
	 *
	 */
	public List<Task> searchFlag(FLAG_TYPE type) {
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
	 * @see taskCollections.Task
	 *
	 */
	public List<Task> searchPriority(PRIORITY_TYPE type) {
		int priorityValue = type.getValue();

		return query(TYPE.PRIORITY, priorityValue, priorityValue);
	}

	// TaskTree operation: getList
	/**
	 * Returns a view of this {@code TaskTree} whose {@code Task} objects are
	 * sorted according to order it is created. The returned {@code List} is
	 * backed by this {@code TaskTree}, so changes in the returned {@code List}
	 * are reflected in this {@code TaskTree}, and vice-versa.
	 *
	 * @return a view of this {@code TaskTree}.
	 */
	public List<Task> getList() {
		int treeType = Attributes.TYPE.ID.getValue();
		return getSortedList(_taskTrees.get(treeType));
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
	 * @see taskCollections.Attributes
	 */
	public List<Task> getSortedList(TYPE taskAttributeType) {
		int taskAttributeIndex = taskAttributeType.getValue();
		return getSortedList(_taskTrees.get(taskAttributeIndex));
	}

	private List<Task> getSortedList(TreeSet<Task> taskTree) {
		ArrayList<Task> resultList = new ArrayList<Task>(taskTree);
		return resultList;
	}

	// TaskTree operation: getString
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
	public String getString() {
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
	public String getString(TYPE taskAttributeType) {

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

	// TaskTree operation: size related
	/**
	 * Return total number of task in this tree
	 *
	 * @return number of task in this tree
	 */
	public int size() {
		return _taskTreeSize;
	}

	private void increaseTaskListSize() {
		_taskTreeSize += 1;
	}

	private void decreaseTaskListSize() {
		_taskTreeSize -= 1;
	}

	// Storage related methods

	// TaskTree operation: File Storage related
	/**
	 * Push a new task to storage file.
	 *
	 * @throws Exception
	 */
	private boolean pushAddToStorage(Task task) {
		boolean isPushSuccessful = true;

		isPushSuccessful &= _fileHandler.add(task);

		return isPushSuccessful;
	}

	/**
	 * Remove a task from the storage
	 *
	 * @throws Exception
	 */
	private boolean pushRemoveToStorage(Task task) {
		int taskId = task.getId();
		boolean isPullSuccessful = true;

		isPullSuccessful &= _fileHandler.delete(taskId);

		return isPullSuccessful;
	}

	/**
	 * Update a new task to storage file.
	 *
	 * @throws Exception
	 */
	private boolean pushUpdateToStorage(Task oldTask, Task newTask) {
		int oldId = oldTask.getId();
		int newId = newTask.getId();
		boolean isPushSuccessful = true;

		if (oldId == newId) {
			isPushSuccessful &= _fileHandler.update(newTask);
		} else {
			isPushSuccessful &= _fileHandler.delete(oldId);
			isPushSuccessful &= _fileHandler.add(newTask);
		}

		return isPushSuccessful;
	}

	/**
	 * To retrieve task list the Task file. This method is called upon the
	 * starting of this program.
	 */
	private static void pullFromStorage() {
		ArrayList<Task> taskList;
		taskList = _fileHandler.retrieveTaskList();

		for (TreeSet<Task> tree : _taskTrees) {
			tree.addAll(taskList);
		}
		_taskTreeSize = taskList.size();
	}

	/**
	 * This method will be called to construct the taskFileHandler for file
	 * storage
	 *
	 * @param taskFilePath
	 *            directed to the storage XML file for tasks.
	 */
	private static void iniTaskFileHandler(String taskFilePath) {
		_fileHandler = new TaskFileHandler(taskFilePath);
	}
}