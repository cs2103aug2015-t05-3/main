/**
 * @author amoshydra
 *
 */
package tds;
import java.util.List;
import java.util.TreeSet;

public class TaskTree implements TaskCollection<Task> {

	private int taskListSize;
	
	public TaskTree() {
		TreeSet<Task> taskTree = new TreeSet<Task>();
		taskListSize = 0;
	}
	
	@Override
	public void add(Task task) {
		// TODO Auto-generated method stub
		increaseTaskListSize();
	}

	@Override
	public void remove(Task task) {
		// TODO Auto-generated method stub
		decreaseTaskListSize();
	}

	@Override
	public void update(Task task) {
		// TODO Auto-generated method stub
		
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
	public int size() {
		// TODO Auto-generated method stub
		return 0;
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


	private void increaseTaskListSize() {
		taskListSize += 1;
	}
	private void decreaseTaskListSize() {
		taskListSize -= 1;
	}
}
