/**
 * @author amoshydra
 * 
 */
package tds;
import java.util.List;
import java.util.TreeSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

public class TaskTree implements TaskCollection<Task> {

	// TODO remove suppress warning

	private int taskTreeSize;
	private HashSet<Task> taskHash;
	@SuppressWarnings("unused")
	private TreeSet<Task> taskNameTree;
	@SuppressWarnings("unused")
	private TreeSet<Task> taskStartTimeTree;
	@SuppressWarnings("unused")
	private TreeSet<Task> taskEndTimeTree;
	@SuppressWarnings("unused")
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
	public void rebuild(Collection<Task> collection) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void add(Task task) {
		taskHash.add(task);
		
		increaseTaskListSize();
	}

	@Override
	public Task remove(Task task) {
		// TODO Auto-generated method stub
		decreaseTaskListSize();
		return null;
	}

	@Override
	public Task replace(Task taskOld, Task taskNew) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> searchName(String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> queryTime(long upperBound, long lowerBound, long option) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> searchPriority(int prioritySearch) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Task> getSortedList(Comparator<Task> comparator) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return taskTreeSize;
	}

	private void increaseTaskListSize() {
		taskTreeSize += 1;
	}
	private void decreaseTaskListSize() {
		taskTreeSize -= 1;
	}
}
