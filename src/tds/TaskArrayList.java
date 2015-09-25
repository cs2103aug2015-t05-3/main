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
	
	public TaskArrayList(Collection<Task> collection) {
		taskList = new ArrayList<Task>(collection);
		taskListSize = taskList.size();
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
	
	List<Task> queryTime(ArrayList<Task> sortedList, long upperBound, long lowerBound, long option) {
		
		int fromIndex, toIndex;
		
		if (upperBound < lowerBound) { // invalid bounds, create empty list
			fromIndex = toIndex = 0;
		} else {
			fromIndex = getClosestMatchIndex(sortedList, lowerBound, option);
			toIndex = getClosestMatchIndex(sortedList, upperBound, option);
		}
		
		ArrayList<Task> resultList;
		resultList = new ArrayList<Task>(sortedList.subList(fromIndex, toIndex));
		return resultList;
	}
	
	@Override
	public List<Task> queryStartTime(long startTimeUpperBound, long startTimeLowerBound) {
		ArrayList<Task> sortedList;
		sortedList = new ArrayList<Task>(getSortedList(new StartTimeComparator()));

		return queryTime(sortedList, startTimeUpperBound, startTimeLowerBound, Task.GET_VALUE_START_TIME);
	}
	
	@Override
	public List<Task> queryEndTime(long endTimeUpperBound, long endTimeLowerBound) {
		ArrayList<Task> sortedList;
		sortedList = new ArrayList<Task>(getSortedList(new EndTimeComparator()));
		
		return queryTime(sortedList, endTimeUpperBound, endTimeLowerBound, Task.GET_VALUE_END_TIME);
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

	public List<Task> search(int searchKey, int option) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		for (Task task : taskList) {
			if (task.getValue(option) == (searchKey)) {
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
	public List<Task> searchFlag(int flagSearch) {
		return search(flagSearch, Task.GET_VALUE_FLAG);
	}

	@Override
	public List<Task> searchPriority(int prioritySearch) {
		return search(prioritySearch, Task.GET_VALUE_PRIORITY);
	}
	
	@Override
	public List<Task> getSortedList(Comparator<Task> comparator) {
		ArrayList<Task> sortedList = new ArrayList<Task>(taskList);
		sortedList.sort(comparator);
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
	
	@Override
	public String toString() {
		String buffer = "";
		for (Task task : taskList) {
			buffer += task + "\n";
		}
		return buffer;
	}
}
