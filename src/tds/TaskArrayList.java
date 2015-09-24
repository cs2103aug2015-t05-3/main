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
	public List<Task> queryStartTime(long startTimeUpperBound, long startTimeLowerBound) {
		
		// check for invalid bounds
		if (startTimeUpperBound < startTimeLowerBound) {
			return null;
		}
		
		ArrayList<Task> sortedList;
		sortedList = new ArrayList<Task>(getSortedList(new StartTimeComparator()));
		
		int fromIndex = getClosestMatchIndex(sortedList, startTimeLowerBound);
		int toIndex = getClosestMatchIndex(sortedList, startTimeUpperBound);
		
		ArrayList<Task> resultList;
		resultList = new ArrayList<Task>(sortedList.subList(fromIndex, toIndex));
		return resultList;
	}

	@Override
	public List<Task> queryEndTime(long endTimeUpperBound, long endTimeLowerBound) {
		// check for invalid bounds
		if (endTimeUpperBound < endTimeLowerBound) {
			return null;
		}
		
		ArrayList<Task> sortedList;
		sortedList = new ArrayList<Task>(getSortedList(new EndTimeComparator()));
		
		//TODO This method does not work for endTime yet.
		int fromIndex = getClosestMatchIndex(sortedList, endTimeLowerBound);
		int toIndex = getClosestMatchIndex(sortedList, endTimeUpperBound);
		
		ArrayList<Task> resultList;
		resultList = new ArrayList<Task>(sortedList.subList(fromIndex, toIndex));
		return resultList;
	}
	
	private int getClosestMatchIndex(ArrayList<Task> list, long value) {
		return getClosestMatchIndex(list, value, 0, list.size() - 1);
	}
	
	private int getClosestMatchIndex(ArrayList<Task> list, long checkValue, int startIndex, int endIndex) {
		if (endIndex <= startIndex) {
			return startIndex;
		} else {
			// calculate midpoint to cut set in half
			int midIndex = getMidPoint(startIndex, endIndex);
			long midValue = list.get(midIndex).getStartTime();
			
			// three-way comparison
			if (midValue > checkValue)
				// key is in lower subset
				return getClosestMatchIndex(list, checkValue, startIndex, midIndex - 1);
			else if (midValue < checkValue)
				// key is in upper subset
				return getClosestMatchIndex(list, checkValue, midIndex + 1, endIndex);
			else
				// key has been found
				return midIndex;
		}
	}
	
	private int getMidPoint(int start, int end) {
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
