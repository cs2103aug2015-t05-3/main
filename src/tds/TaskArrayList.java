package tds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import tds.comparators.*;

public class TaskArrayList implements TaskCollection<Task> {

	private int taskListSize;
	private ArrayList<Task> taskList;
	
	public TaskArrayList() {
		taskList = new ArrayList<Task>();
		taskListSize = 0;
	}
	
	@Override
	public void rebuild(Collection<Task> collection) {
		taskListSize = collection.size();
		taskList.clear();
		taskList.addAll(collection);
	}
	
	@Override
	public void add(Task task) {
		taskList.add(task);
		increaseTaskListSize();
	}

	@Override
	public Task remove(Task task) {
		Task temp = task;
		taskList.remove(task);
		decreaseTaskListSize();
		return temp;
	}

	@Override
	public Task replace(Task taskOld, Task taskNew) {
		Task temp = taskOld;
		taskOld = taskNew;
		return temp;
	}

	@Override
	public List<Task> searchName(String searchTerm) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		for (Task task : taskList) {
			if (task.getName().contains(searchTerm)) {
				resultList.add(task);
			}
		}
		
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList;
		}
	}

	@Override
	public List<Task> queryTime(long upperBound, long lowerBound, long option) {
		
		// check for invalid bounds
		if (upperBound < lowerBound) {
			return null;
		}
		
		ArrayList<Task> sortedList;
		if (option == Task.GET_VALUE_START_TIME) {
			sortedList = new ArrayList<Task>(getSortedList(new StartTimeComparator()));
		} else if (option == Task.GET_VALUE_END_TIME) {
			sortedList = new ArrayList<Task>(getSortedList(new EndTimeComparator()));
		} else {
			return null;
		}
		
		int fromIndex = getClosestMatchIndex(sortedList, lowerBound, option);
		int toIndex = getClosestMatchIndex(sortedList, upperBound, option);
		
		ArrayList<Task> resultList;
		resultList = new ArrayList<Task>(sortedList.subList(fromIndex, toIndex));
		return resultList;
	}
	
	@Override
	public List<Task> queryStartTime(long startTimeUpperBound, long startTimeLowerBound) {
		return queryTime(startTimeUpperBound, startTimeLowerBound, Task.GET_VALUE_START_TIME);
	}
	
	@Override
	public List<Task> queryEndTime(long endTimeUpperBound, long endTimeLowerBound) {
		return queryTime(endTimeUpperBound, endTimeLowerBound, Task.GET_VALUE_END_TIME);
	}
	
	static int getClosestMatchIndex(ArrayList<Task> list, long value, long option) {
		return getClosestMatchIndex(list, value, option, 0, list.size() - 1);
	}
	
	static int getClosestMatchIndex(ArrayList<Task> list, long checkValue, long option, int startIndex, int endIndex) {
		if (endIndex < startIndex) {
			return startIndex;
		} else {
			// calculate midpoint to cut set in half
			int midIndex = getMidPoint(startIndex, endIndex);
			long midValue = list.get(midIndex).getValue(option);
			
			// three-way comparison
			if (midValue > checkValue) {
				// key is in lower subset
				return getClosestMatchIndex(list, checkValue, option, startIndex, midIndex - 1);
			} else if (midValue < checkValue) {
				// key is in upper subset
				return getClosestMatchIndex(list, checkValue, option, midIndex + 1, endIndex);
			} else {
				// key has been found
				return midIndex;
			}
		}
	}
	
	private static int getMidPoint(int start, int end) {
		int mid = (start + end)/2; 
		return mid;
	}

	@Override
	public List<Task> searchFlag(int flagSearch) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		for (Task task : taskList) {
			if (task.getFlag() == (flagSearch)) {
				resultList.add(task);
			}
		}
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList;
		}
	}

	@Override
	public List<Task> searchPriority(int prioritySearch) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		for (Task task : taskList) {
			if (task.getFlag() == (prioritySearch)) {
				resultList.add(task);
			}
		}
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList;
		}
	}
	
	@Override
	public List<Task> getSortedList(Comparator<Task> comparator) {
		ArrayList<Task> sortedList = new ArrayList<Task>(taskList);
		sortedList.sort(new NameComparator());
		return sortedList;
	}
	
	@Override
	public int size() {
		return taskListSize;
	}

	private void increaseTaskListSize() {
		taskListSize += 1;
	}
	private void decreaseTaskListSize() {
		taskListSize -= 1;
	}
	
}
