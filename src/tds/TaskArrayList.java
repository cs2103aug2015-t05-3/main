package tds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskArrayList implements TaskCollection<Task> {

	private int taskTreeSize;
	private ArrayList<Task> taskList;
	
	public TaskArrayList() {
		taskList = new ArrayList<Task>();
		taskTreeSize = 0;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> searchStartTime(long startTimeSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> searchEndTime(long endTimeSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> searchFlag(int flagSearch) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return 0;
	}

}
