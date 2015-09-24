/**
 * @author amoshydra
 *
 */
package tds;
import java.util.List;
import java.util.TreeSet;
import java.util.Collection;
import java.util.HashSet;

public class TaskTree implements TaskCollection<Task> {

	private int taskTreeSize;
	private HashSet<Task> taskHash;
	private TreeSet<Task> taskNameTree;
	private TreeSet<Task> taskStartTimeTree;
	private TreeSet<Task> taskEndTimeTree;
	private TreeSet<Task> taskFlagTree;
	
	public TaskTree() {
		taskHash = new HashSet<Task>();
		taskNameTree = new TreeSet<Task>();
		taskStartTimeTree = new TreeSet<Task>();
		taskEndTimeTree = new TreeSet<Task>();
		taskFlagTree = new TreeSet<Task>();
		taskTreeSize = 0;
	}

	@Override
	public void addAll(Collection<Task> collection) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void add(Task task) {
		taskHash.add(task);
		
		increaseTaskListSize();
	}

	@Override
	public void remove(Task task) {
		// TODO Auto-generated method stub
		decreaseTaskListSize();
	}

	@Override
	public void replace(Task taskOld, Task taskNew) {
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
		return taskTreeSize;
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
		taskTreeSize += 1;
	}
	private void decreaseTaskListSize() {
		taskTreeSize -= 1;
	}
}
