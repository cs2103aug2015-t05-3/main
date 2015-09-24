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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> queryEndTime(long endTimeUpperBound, long endTimeLowerBound) {
		// TODO Auto-generated method stub
		return null;
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
