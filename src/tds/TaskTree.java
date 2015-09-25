/**
 * @author amoshydra
 * 
 */
package tds;
import java.util.List;
import java.util.TreeSet;

import tds.comparators.*;

import java.util.ArrayList;
import java.util.Collection;
import tds.TaskAttributeConstants;

public class TaskTree implements TaskCollection<Task> {
	
	private static final int TASK_NAME_TREE 		= TaskAttributeConstants.NAME;
	private static final int TASK_START_TIME_TREE 	= TaskAttributeConstants.START_TIME;
	private static final int TASK_END_TIME_TREE 	= TaskAttributeConstants.END_TIME;
	private static final int TASK_FLAG_TREE 		= TaskAttributeConstants.FLAG;
	private static final int TASK_PRIORITY_TREE 	= TaskAttributeConstants.PRIORITY;
	private static final int TASK_CREATE_TIME_TREE 	= TaskAttributeConstants.ID;
	private static final int SIZE_OF_TASK_TREES = 6;
	
	private ArrayList <TreeSet<Task>> taskTrees;
	private int taskTreeSize;
	private Task lowerBoundHandler;
	private Task upperBoundHandler;
	
	public TaskTree() {
		taskTreeSize = 0;
		taskTrees = new ArrayList<TreeSet<Task>>(SIZE_OF_TASK_TREES);
		
		taskTrees.add(TASK_NAME_TREE, new TreeSet<Task>(new NameComparator()));
		taskTrees.add(TASK_START_TIME_TREE, new TreeSet<Task>(new StartTimeComparator()));
		taskTrees.add(TASK_END_TIME_TREE, new TreeSet<Task>(new EndTimeComparator()));
		taskTrees.add(TASK_FLAG_TREE, new TreeSet<Task>(new FlagComparator()));
		taskTrees.add(TASK_PRIORITY_TREE, new TreeSet<Task>(new PriorityComparator()));
		taskTrees.add(TASK_CREATE_TIME_TREE, new TreeSet<Task>(new IdComparator()));
		
		lowerBoundHandler = new Task("");
		upperBoundHandler = new Task("");
	}
	
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
	public boolean replace(Task taskOld, Task taskNew) {
		boolean isReplaced;
		
		boolean isRemoved = remove(taskOld);
		boolean isAdded = add(taskNew);
		
		isReplaced = isRemoved && isAdded;
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
	public List<Task> queryStartTime(long startTimeUpperBound, long startTimeLowerBound) {
		return query(TASK_START_TIME_TREE, startTimeUpperBound, startTimeLowerBound);
	}

	@Override
	public List<Task> queryEndTime(long endTimeUpperBound, long endTimeLowerBound) {
		return query(TASK_END_TIME_TREE, endTimeUpperBound, endTimeLowerBound);
	}

	public List<Task> queryFlag(int lowerBound, int upperBound) {
		return query(TASK_FLAG_TREE, lowerBound, upperBound);
	}
	
	public List<Task> queryPriority(int lowerBound, int upperBound) {
		return query(TASK_PRIORITY_TREE, lowerBound, upperBound);
	}
	
	public List<Task> query(int treeIndex, long longLowerBound, long longUpperBound) {		
		
		TreeSet<Task> taskTree = taskTrees.get(treeIndex);
		ArrayList<Task> emptyList = new ArrayList<Task>(); 
		boolean isToInclusive;
		Task upperBoundHBuffer;
		
		if (longUpperBound < longLowerBound) {
			return emptyList;
		} else {
			if (longUpperBound == longLowerBound) {
				longUpperBound += 1;
				upperBoundHBuffer = upperBoundHandler;
				isToInclusive = false;
			} else {
				upperBoundHBuffer = new Task("");
				isToInclusive = true;
			}
			switch (treeIndex) {			
				case TASK_END_TIME_TREE:
					lowerBoundHandler.setEndTime(longLowerBound);
					upperBoundHBuffer.setEndTime(longUpperBound);
					break;
				case TASK_START_TIME_TREE:
					lowerBoundHandler.setStartTime(longLowerBound);
					upperBoundHBuffer.setStartTime(longUpperBound);
					break;
				case TASK_PRIORITY_TREE:
					lowerBoundHandler.setPriority((int)longLowerBound);
					upperBoundHBuffer.setPriority((int)longUpperBound);
					break;
				case TASK_FLAG_TREE:
					lowerBoundHandler.setFlag((int)longLowerBound);
					upperBoundHBuffer.setFlag((int)longUpperBound);
					break;
				default:
					return emptyList;
			}
			return new ArrayList<Task>(taskTree.subSet(lowerBoundHandler, true, upperBoundHBuffer, isToInclusive));
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
	
	public List<Task> getSortedList(int treeIndex) {
		return getSortedList(taskTrees.get(treeIndex));
	}
	
	public List<Task> getSortedList(TreeSet<Task> taskTree) {
		ArrayList<Task> resultList = new ArrayList<Task>(taskTreeSize);
		for (Task task : taskTree) {
			resultList.add(task);
		}
		return resultList;
	}

	@Override
	public String toString() {
		return toString(TaskAttributeConstants.ID);
	}
	
	public String toString (int treeIndex) {
		String buffer = "";
		ArrayList<Task> resultList = new ArrayList<Task>(getSortedList(treeIndex));
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
