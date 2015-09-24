package tds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskArrayList implements TaskCollection<Task> {

	private int taskListSize;
	private ArrayList<Task> taskList;
	
	public TaskArrayList() {
		taskList = new ArrayList<Task>();
		taskListSize = 0;
	}
	
	@Override
	public void addAll(Collection<Task> collection) {
		taskList.addAll(collection);
	}
	
	@Override
	public void add(Task task) {
		taskList.add(task);
	}

	@Override
	public Task remove(Task task) {
		Task temp = task;
		taskList.remove(task);
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
	public List<Task> sortName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> sortStartTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> sortEndTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> sortFlag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return taskListSize;
	}

}
